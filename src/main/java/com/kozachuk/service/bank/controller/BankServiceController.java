package com.kozachuk.service.bank.controller;

import com.kozachuk.service.bank.dto.ErrorResponse;
import com.kozachuk.service.bank.exceptions.CommonServiceException;
import com.kozachuk.service.bank.repository.entity.Amount;
import com.kozachuk.service.bank.service.UserAmountService;
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
    private UserAmountService userAmountService;

    @GetMapping(path="amount", produces = MediaType.APPLICATION_JSON_VALUE)
    public Amount getCurrentAmount(Principal principal) throws CommonServiceException {
        String username = principal.getName();
        return userAmountService.getAmountForUser(username);
    }

    @PostMapping(path="amount/{amount}/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public Amount putAmountIntoAccount(Principal principal, @PathVariable("amount") BigDecimal amount) throws CommonServiceException {
        String username = principal.getName();
        return userAmountService.putAmountIntoAccount(username, amount);
    }

    @PostMapping(path="amount/{amount}/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
    public Amount withdrawAmountFromAccount(Principal principal, @PathVariable("amount") BigDecimal amount) throws CommonServiceException {
        String username = principal.getName();
        return userAmountService.withdrawAmountFromAccount(username, amount);
    }


    @PostMapping(path="amount/{amount}/{recipient}/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    public void transferAmountFromCurrentUserToNewUser(Principal principal, @PathVariable("amount") BigDecimal amount, @PathVariable("recipient") String recipient) throws CommonServiceException {
        String username = principal.getName();
        userAmountService.transferMoney(username, recipient, amount);
    }

    @ExceptionHandler({ CommonServiceException.class})
    public ResponseEntity<ErrorResponse> handleException(CommonServiceException exception) {
        ErrorResponse response = new ErrorResponse(exception.getCode(), exception.getMessage());
        return new ResponseEntity(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}