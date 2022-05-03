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

    /**
     * Operation adds amount into account
     * @param userId  - account id
     * @param amounts - input amount
     * @return saved amount
     * @throws CommonServiceException
     */
    @Transactional(rollbackFor = { CommonServiceException.class, RuntimeException.class})
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Amount putInto(Long userId, BigDecimal amounts) throws CommonServiceException{
        Amount amount = topUp(userId, amounts);
        return amountRepo.save(amount);
    }

    /**
     * Operation withdraft amount from account
     * @param userId - account id
     * @param amount - amount for withdrawing
     * @return - saved amount
     * @throws CommonServiceException
     */
    @Transactional(rollbackFor = { CommonServiceException.class, RuntimeException.class })
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Amount withdraft(Long userId, BigDecimal amount) throws CommonServiceException {
        Amount withdrawedAmount = withdraw(userId, amount);
        return amountRepo.save(withdrawedAmount);
    }

    /**
     * Operation transfers amount between users
     * @param senderId - source of amount
     * @param recipientId - target of amount
     * @param amount - amount
     * @throws CommonServiceException
     */
    @Transactional(rollbackFor = { CommonServiceException.class, RuntimeException.class })
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void transferAmount(Long senderId, Long recipientId, BigDecimal amount) throws CommonServiceException {
        if(amount == null) throw new IllegalArgumentException();
        if(amount.compareTo(BigDecimal.ZERO) < 0) throw new WrongAmountException();

        Amount senderAmount = withdraw(senderId, amount);
        Amount recipientAmount = topUp(recipientId, amount);

        amountRepo.save(senderAmount);
        amountRepo.save(recipientAmount);
    }

    private Amount topUp(Long userId, BigDecimal amounts) throws WrongAmountException {
        if(userId == null) throw new IllegalArgumentException();
        if(amounts == null) throw new IllegalArgumentException();
        if(amounts.compareTo(BigDecimal.ZERO) < 0) throw new WrongAmountException();

        Amount currentAmount = amountRepo.findAmountByUserId(userId);

        if (currentAmount == null) {
            currentAmount = new Amount(userId, BigDecimal.ZERO);
        }

        currentAmount.addAmount(amounts);

        return currentAmount;
    }

    private Amount withdraw(Long userId, BigDecimal amounts) throws CommonServiceException {
        if(userId == null) throw new IllegalArgumentException();
        if(amounts == null) throw new IllegalArgumentException();
        if(amounts.compareTo(BigDecimal.ZERO) < 0) throw new WrongAmountException();

        Amount currentAmount = amountRepo.findAmountByUserId(userId);

        if (currentAmount == null) {
            currentAmount = new Amount(userId, BigDecimal.ZERO);
        }

        currentAmount.withdraft(amounts);

        if(currentAmount.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughAmountException();
        }

        return currentAmount;
    }
}
