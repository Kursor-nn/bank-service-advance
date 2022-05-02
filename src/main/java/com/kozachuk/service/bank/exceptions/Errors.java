package com.kozachuk.service.bank.exceptions;

public enum Errors {
    AUTH("ERROR-CODE-0002", "User is not found"),
    NO_AMOUNTS("ERROR-CODE-0001", "No amount is found."),
    WRONG_AMOUNTS("ERROR-CODE-0003", "Wrong input amount. Amount must be greater 0."),
    NOT_ENOUGH_AMOUNTS("ERROR-CODE-0004", "Not enough amounts for withdraw operations.");

    Errors(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
