package com.playtomic.tests.wallet.api;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class PaymentRequest {

    @Min(value = 0)
    @NotNull
    private BigDecimal amount;
}
