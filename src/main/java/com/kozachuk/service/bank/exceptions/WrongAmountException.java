package com.kozachuk.service.bank.exceptions;

public class WrongAmountException extends CommonServiceException {
    public WrongAmountException() {
        super(Errors.WRONG_AMOUNTS.getMessage(), Errors.WRONG_AMOUNTS.getCode());
    }
}
