package com.playtomic.tests.wallet.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RechargeFailedException extends RuntimeException {

    public RechargeFailedException(String message, Exception cause) {
        super(message, cause);
    }
}
