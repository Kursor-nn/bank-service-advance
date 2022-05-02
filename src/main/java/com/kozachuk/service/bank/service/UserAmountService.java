package com.kozachuk.service.bank.service;

import com.kozachuk.service.bank.component.AmountComponent;
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
    private AmountComponent amountComponent;

    public Amount getAmountForUser(String username) throws CommonServiceException {
        if(username == null) throw new IllegalArgumentException();

        User currentUser = userRepo.findByUsername(username);
        Amount currentAmount = amountRepo.findAmountByUserId(currentUser.getId());

        if (currentAmount == null) throw new NoAmountException();

        return currentAmount;
    }

    public Amount putInto(String username, BigDecimal amount) throws CommonServiceException {
        if(username == null) throw new IllegalArgumentException();
        if(amount == null) throw new IllegalArgumentException();

        User currentUser = userRepo.findByUsername(username);
        Long userId = currentUser.getId();

        Amount currentAmount = amountComponent.putInto(userId, amount);

        return currentAmount;
    }

    public Amount withdraw(String username, BigDecimal amount) throws CommonServiceException {
        if(username == null) throw new IllegalArgumentException();
        if(amount == null) throw new IllegalArgumentException();

        User currentUser = userRepo.findByUsername(username);
        Long userId = currentUser.getId();

        Amount currentAmount = amountComponent.withdraft(userId, amount);

        return currentAmount;
    }

    public void transferMoney(String sender, String recipient, BigDecimal amount) throws CommonServiceException {
        if(amount == null) throw new IllegalArgumentException();

        User currentUser = getUserByName(sender);
        User recipientUser = getUserByName(recipient);

        if(recipientUser == null) throw new IllegalArgumentException();

        Long userId = currentUser.getId();
        Long recipientUserId = recipientUser.getId();

        amountComponent.transferAmount(userId, recipientUserId, amount);
    }

    private User getUserByName(String username){
        if(username == null) throw new IllegalArgumentException();
        return userRepo.findByUsername(username);
    }
}
