package com.kozachuk.service.bank.exceptions;

public class NoAmountException extends CommonServiceException {
    public NoAmountException() {
        super(Errors.NO_AMOUNTS.getMessage(), Errors.NO_AMOUNTS.getCode());
    }
}
