package com.kozachuk.service.bank.controller;

import com.kozachuk.service.bank.dto.ErrorResponse;
import com.kozachuk.service.bank.exceptions.CommonServiceException;
import com.kozachuk.service.bank.exceptions.Errors;
import com.kozachuk.service.bank.repository.entity.Amount;
import com.kozachuk.service.bank.service.AmountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/bank-service/")
public class BankServiceController {
    @Autowired
    private AmountService amountService;

    @GetMapping(path="amount", produces = MediaType.APPLICATION_JSON_VALUE)
    public Amount getCurrentAmount(Principal principal) throws CommonServiceException {
        String username = principal.getName();
        return amountService.getAmountForUser(username);
    }

    @PostMapping(path="amount/{amount}/putup", produces = MediaType.APPLICATION_JSON_VALUE)
    public Amount putAmountIntoAccount(Principal principal, @PathVariable("amount") BigDecimal amount) throws CommonServiceException {
        String username = principal.getName();
        return amountService.putInto(username, amount);
    }

    @PostMapping(path="amount/{amount}/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
    public Amount withdrawAmountFromAccount(Principal principal, @PathVariable("amount") BigDecimal amount) throws CommonServiceException {
        String username = principal.getName();
        return amountService.withdraw(username, amount);
    }

    @PostMapping(path="amount/{amount}/transfer/{recipient}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void transferAmountFromCurrentUserToRecipient(Principal principal
                                                    , @PathVariable("amount") BigDecimal amount
                                                    , @PathVariable("recipient") String recipient)
    throws CommonServiceException {
        String username = principal.getName();
        amountService.transferMoney(username, recipient, amount);
    }

    @ExceptionHandler({ CommonServiceException.class})
    public ResponseEntity<ErrorResponse> handleException(CommonServiceException exception) {
        ErrorResponse response = new ErrorResponse(exception.getCode(), exception.getMessage());
        return new ResponseEntity(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({ Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse response = new ErrorResponse(Errors.INTERNAL_ERROR.getCode(), exception.getMessage());
        return new ResponseEntity(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}