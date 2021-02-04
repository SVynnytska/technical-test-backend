package com.playtomic.tests.wallet.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.playtomic.tests.wallet.entity.MovementType;
import com.playtomic.tests.wallet.entity.Wallet;

public interface WalletService {

    Optional<Wallet> move(Long walletId, BigDecimal amount, MovementType type);
}
