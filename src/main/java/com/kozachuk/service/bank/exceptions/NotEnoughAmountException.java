package com.kozachuk.service.bank.exceptions;

public class NotEnoughAmountException extends CommonServiceException {
    public NotEnoughAmountException(){
        super(Errors.NOT_ENOUGH_AMOUNTS.getMessage(), Errors.NOT_ENOUGH_AMOUNTS.getCode());
    }

}
