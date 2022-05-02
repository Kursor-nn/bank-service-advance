package com.kozachuk.service.bank.exceptions;

public class CommonServiceException extends Exception {
    private String message;
    private String code;

    public CommonServiceException(String message, String code){
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
