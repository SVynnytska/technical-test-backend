package com.playtomic.tests.wallet.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class WalletTest {

    @Test
    public void charge_sameAmountAsBalance_ok() {
        Wallet wallet = new Wallet(1L, BigDecimal.TEN);
        wallet.charge(BigDecimal.TEN);

        assertThat(wallet.getBalance()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void charge_smallerAmountThanBalance_ok() {
        Wallet wallet = new Wallet(1L, BigDecimal.TEN);
        wallet.charge(BigDecimal.valueOf(9.9));

        assertThat(wallet.getBalance()).isEqualTo(BigDecimal.valueOf(0.1));
    }

    @Test(expected = IllegalAmountException.class)
    public void charge_biggerAmountThanBalance_exception() {
        Wallet wallet = new Wallet(1L, BigDecimal.TEN);
        wallet.charge(BigDecimal.valueOf(11));
    }
}
