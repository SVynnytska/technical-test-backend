package com.playtomic.tests.wallet.api;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.playtomic.tests.wallet.entity.MovementType;
import lombok.Getter;

@Getter
public class MovementRequest {

    @Min(value = 0)
    @NotNull
    private BigDecimal amount;
    @NotNull
    private MovementType type;
}
