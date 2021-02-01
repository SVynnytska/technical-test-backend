package com.playtomic.tests.wallet.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal balance;

    public Wallet charge(BigDecimal amount) {
        if (!canBeCharged(amount)) {
            throw new IllegalAmountException(
                  String.format("Amount %s is greater than wallet amount.", amount));
        }
        this.balance = balance.subtract(amount);
        return this;
    }

    public Wallet recharge(BigDecimal amount) {
        this.balance = balance.add(amount);
        return this;
    }

    private boolean canBeCharged(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

}
