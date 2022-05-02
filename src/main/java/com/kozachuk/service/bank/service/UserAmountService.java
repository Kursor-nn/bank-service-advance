package com.kozachuk.service.bank.service;

import com.kozachuk.service.bank.component.AmountBLComponent;
import com.kozachuk.service.bank.exceptions.CommonServiceException;
import com.kozachuk.service.bank.exceptions.NoAmountException;
import com.kozachuk.service.bank.repository.AmountRepo;
import com.kozachuk.service.bank.repository.UserRepo;
import com.kozachuk.service.bank.repository.entity.Amount;
import com.kozachuk.service.bank.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserAmountService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AmountRepo amountRepo;
    @Autowired
    private AmountBLComponent amountBLComponent;

    public Amount getAmountForUser(String username) throws CommonServiceException {
        User currentUser = userRepo.findByUsername(username);
        Amount currentAmount = amountRepo.findAmountByUserId(currentUser.getId());

        if (currentAmount == null) throw new NoAmountException();

        return currentAmount;
    }


    public Amount putAmountIntoAccount(String username, BigDecimal amount) throws CommonServiceException {
        User currentUser = userRepo.findByUsername(username);
        Long userId = currentUser.getId();

        Amount currentAmount = amountBLComponent.topUpAccount(userId, amount);

        return currentAmount;
    }

    public Amount withdrawAmountFromAccount(String username, BigDecimal amount) throws CommonServiceException {
        User currentUser = userRepo.findByUsername(username);
        Long userId = currentUser.getId();
        Amount currentAmount = amountBLComponent.withdraftAccount(userId, amount);

        return currentAmount;
    }

    public void transferMoney(String sender, String recipient, BigDecimal amount) throws CommonServiceException {
        if(sender == null) throw new IllegalArgumentException();
        if(recipient == null) throw new IllegalArgumentException();
        if(amount == null) throw new IllegalArgumentException();

        User currentUser = userRepo.findByUsername(sender);

        User recipientUser = userRepo.findByUsername(recipient);
        if(recipientUser == null) throw new IllegalArgumentException();

        Long userId = currentUser.getId();
        Long recipientUserId = recipientUser.getId();
        amountBLComponent.transferAmount(userId, recipientUserId, amount);
    }
}
