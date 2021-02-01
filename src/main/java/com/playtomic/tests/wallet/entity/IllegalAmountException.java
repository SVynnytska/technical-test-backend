package com.playtomic.tests.wallet.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalAmountException extends IllegalArgumentException {

    public IllegalAmountException(String message) {
        super(message);
    }
}
