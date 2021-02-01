package com.playtomic.tests.wallet.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public Optional<Wallet> charge(Long walletId, BigDecimal amount) {
        return walletRepository.getWithWriteLock(walletId)
                               .map(wallet -> wallet.charge(amount))
                               .map(walletRepository::save);
    }

    @Override
    @Transactional
    public Optional<Wallet> recharge(Long walletId, BigDecimal amount) {
        return walletRepository.getWithWriteLock(walletId)
                               .map(wallet -> recharge(amount, wallet))
                               .map(walletRepository::save);
    }

    private Wallet recharge(BigDecimal amount, Wallet wallet) {
        try {
            paymentService.charge(amount);
            return wallet.recharge(amount);
        } catch (PaymentServiceException e) {
            throw new RechargeFailedException("Recharge failed.", e);
        }
    }
}
