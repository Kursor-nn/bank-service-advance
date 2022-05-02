package com.kozachuk.service.bank.component;

import com.kozachuk.service.bank.exceptions.CommonServiceException;
import com.kozachuk.service.bank.exceptions.NotEnoughAmountException;
import com.kozachuk.service.bank.exceptions.WrongAmountException;
import com.kozachuk.service.bank.repository.AmountRepo;
import com.kozachuk.service.bank.repository.entity.Amount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;

@Component
public class AmountComponent {
    @Autowired
    private AmountRepo amountRepo;

    @Transactional(rollbackFor = { CommonServiceException.class, RuntimeException.class})
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Amount putInto(Long userId, BigDecimal extraAmounts) throws CommonServiceException{
        Amount amount = topUp(userId, extraAmounts);
        return amountRepo.save(amount);
    }

    @Transactional(rollbackFor = { CommonServiceException.class, RuntimeException.class })
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Amount withdraft(Long userId, BigDecimal extraAmounts) throws CommonServiceException {
        Amount amount = withdraw(userId, extraAmounts);
        return amountRepo.save(amount);
    }


    @Transactional(rollbackFor = { CommonServiceException.class, RuntimeException.class })
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void transferAmount(Long senderId, Long recipientId, BigDecimal amount) throws CommonServiceException {
        if(amount.compareTo(BigDecimal.ZERO) < 0) throw new WrongAmountException();

        Amount senderAmount = withdraw(senderId, amount);
        Amount recipientAmount = topUp(recipientId, amount);

        amountRepo.save(senderAmount);
        amountRepo.save(recipientAmount);
    }

    private Amount topUp(Long userId, BigDecimal extraAmounts) throws WrongAmountException {
        if(extraAmounts == null) throw new IllegalArgumentException();
        if(extraAmounts.compareTo(BigDecimal.ZERO) <= 0) throw new WrongAmountException();

        Amount currentAmount = amountRepo.findAmountByUserId(userId);

        if (currentAmount == null) {
            currentAmount = new Amount(userId, BigDecimal.ZERO);
        }

        currentAmount.addAmount(extraAmounts);

        return currentAmount;
    }
    private Amount withdraw(Long userId, BigDecimal extraAmounts) throws CommonServiceException {
        if(extraAmounts.compareTo(BigDecimal.ZERO) <= 0) throw new WrongAmountException();

        Amount currentAmount = amountRepo.findAmountByUserId(userId);

        if (currentAmount == null) {
            currentAmount = new Amount(userId, BigDecimal.ZERO);
        }

        currentAmount.withdraft(extraAmounts);

        if(currentAmount.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughAmountException();
        }

        return currentAmount;
    }
}
