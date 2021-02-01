package com.playtomic.tests.wallet.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import com.playtomic.tests.wallet.entity.IllegalAmountException;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.WalletService;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class WalletServiceIT {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletService walletService;

    @Before
    public void setUp() {
        walletRepository.save(new Wallet(1L, BigDecimal.valueOf(10000)));
    }

    @Test(expected = IllegalAmountException.class)
    public void charge_biggerThanBalance_exception() {
        walletService.charge(1L, BigDecimal.valueOf(10001));
    }

    @Test(expected = RechargeFailedException.class)
    public void recharge_smallerThanThreshold_exception() {
        walletService.recharge(1L, BigDecimal.valueOf(9));
    }

    @Test
    @SneakyThrows
    public void charge_concurrent_ok() {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Callable<Wallet>> tasks = new ArrayList<>();
        int charges = 0;

        for (int i = 1; i <= 100; i++) {
            charges = charges + i;
            tasks.add(getCallable(1, walletService::charge, i));
        }

        executor.invokeAll(tasks);
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertThat(walletRepository.findOne(1L).getBalance())
              .isEqualTo(BigDecimal.valueOf(10000).subtract(BigDecimal.valueOf(charges)).setScale(2));
    }

    @Test
    @SneakyThrows
    public void recharge_concurrent_ok() {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Callable<Wallet>> tasks = new ArrayList<>();
        int recharges = 0;

        for (int i = 10; i <= 30; i++) {
            recharges = recharges + i;
            tasks.add(getCallable(1, walletService::recharge, i));
        }

        executor.invokeAll(tasks);
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertThat(walletRepository.findOne(1L).getBalance())
              .isEqualTo(BigDecimal.valueOf(10000).add(BigDecimal.valueOf(recharges)).setScale(2));
    }

    private Callable<Wallet> getCallable(
          long walletId,
          BiFunction<Long, BigDecimal, Optional<Wallet>> method,
          long amount) {
        return () -> method.apply(walletId, BigDecimal.valueOf(amount)).get();
    }

}
