package com.playtomic.tests.wallet.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.playtomic.tests.wallet.entity.Wallet;

public interface WalletService {

    Optional<Wallet> charge(Long walletId, BigDecimal amount);

    Optional<Wallet> recharge(Long walletId, BigDecimal amount);
}
