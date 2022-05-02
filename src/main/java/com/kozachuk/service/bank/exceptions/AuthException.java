package com.kozachuk.service.bank.exceptions;

public class AuthException extends CommonServiceException {
    public AuthException() {
        super(Errors.AUTH.getMessage(), Errors.AUTH.getMessage());
    }
}
