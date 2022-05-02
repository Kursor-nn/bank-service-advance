package com.kozachuk.service.bank.dto;

public class ErrorResponse {
    private String message;
    private String code;


    public ErrorResponse(String code, String message) {
        this.message = message;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"message\": \"" + message + '\"' +
                ", \"code\": \"" + code + '\"' +
                '}';
    }
}
