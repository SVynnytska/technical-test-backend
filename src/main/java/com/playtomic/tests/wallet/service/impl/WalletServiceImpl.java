package com.playtomic.tests.wallet.service.impl;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Optional;
import java.util.function.BiFunction;

import com.playtomic.tests.wallet.entity.MovementType;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final PaymentService paymentService;
    private final EnumMap<MovementType, BiFunction<Wallet, BigDecimal, Wallet>> movementsMap;

    public WalletServiceImpl(WalletRepository walletRepository,
          PaymentService paymentService) {
        this.walletRepository = walletRepository;
        this.paymentService = paymentService;
        this.movementsMap = new EnumMap<>(MovementType.class);
        movementsMap.put(MovementType.CHARGE, Wallet::charge);
        movementsMap.put(MovementType.RECHARGE, this::recharge);
    }

    @Override
    @Transactional
    public Optional<Wallet> move(Long walletId, BigDecimal amount, MovementType type) {
        return walletRepository.getWithWriteLock(walletId)
                               .map(wallet -> movementsMap.get(type).apply(wallet, amount))
                               .map(walletRepository::save);
    }

    private Wallet recharge(Wallet wallet, BigDecimal amount) {
        try {
            paymentService.charge(amount);
            return wallet.recharge(amount);
        } catch (PaymentServiceException e) {
            throw new RechargeFailedException("Recharge failed.", e);
        }
    }
}
