package com.kozachuk.service.bank.component;

import com.kozachuk.service.bank.exceptions.CommonServiceException;
import com.kozachuk.service.bank.exceptions.Errors;
import com.kozachuk.service.bank.exceptions.NotEnoughAmountException;
import com.kozachuk.service.bank.exceptions.WrongAmountException;
import com.kozachuk.service.bank.repository.AmountRepo;
import com.kozachuk.service.bank.repository.entity.Amount;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AmountComponentTest  {
    @Mock
    AmountRepo amountRepo;
    @InjectMocks
    AmountComponent amountComponent;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void put_into_user_id_is_null_excetion_expected() {
        assertThrows(IllegalArgumentException.class, () -> {
            amountComponent.putInto(null, BigDecimal.ONE);
        });
    }

    @Test
    public void put_into_amount_is_null_excetion_expected() {
        assertThrows(IllegalArgumentException.class, () -> {
            amountComponent.putInto(1L, null);
        });
    }

    @Test
    public void put_into_amount_is_less_then_zero_excetion_expected() {
        WrongAmountException exception = assertThrows(WrongAmountException.class, () -> {
            amountComponent.putInto(1L, BigDecimal.valueOf(-1L));
        });

        assertEquals(Errors.WRONG_AMOUNTS.getCode(), exception.getCode());
        assertEquals(Errors.WRONG_AMOUNTS.getMessage(), exception.getMessage());
    }


    @Test
    public void put_into_not_existed_amount_success() throws CommonServiceException {
        when(amountRepo.save(any(Amount.class))).thenAnswer(a -> a.getArgument(0));

        Amount amount = amountComponent.putInto(1L, BigDecimal.TEN);

        assertEquals(BigDecimal.TEN, amount.getAmount());
        assertEquals(1L, amount.getUserId());
    }

    @Test
    public void put_into_existed_amount_success() throws CommonServiceException {
        Amount expectedAmount = new Amount(1L, BigDecimal.TEN);

        when(amountRepo.save(any(Amount.class))).thenAnswer(a -> a.getArgument(0));
        when(amountRepo.findAmountByUserId(1L)).thenReturn(expectedAmount);

        Amount amount = amountComponent.putInto(1L, BigDecimal.TEN);

        assertEquals(BigDecimal.valueOf(20L), amount.getAmount());
        assertEquals(1L, amount.getUserId());
    }

    @Test
    public void withdraw_user_id_is_null_exception_expected() {
        assertThrows(IllegalArgumentException.class, () -> {
            amountComponent.withdraft(null, BigDecimal.ONE);
        });
    }

    @Test
    public void withdraw_amount_is_null_exception_expected() {
        assertThrows(IllegalArgumentException.class, () -> {
            amountComponent.withdraft(1L, null);
        });
    }

    @Test
    public void withdraw_amount_is_less_then_zero_exception_expected() {
        WrongAmountException exception = assertThrows(WrongAmountException.class, () -> {
            amountComponent.putInto(1L, BigDecimal.valueOf(-1L));
        });

        assertEquals(Errors.WRONG_AMOUNTS.getCode(), exception.getCode());
        assertEquals(Errors.WRONG_AMOUNTS.getMessage(), exception.getMessage());
    }


    @Test
    public void withdraw_from_not_existed_amount_exception_expected() {
        NotEnoughAmountException exception = assertThrows(NotEnoughAmountException.class, () -> {
            amountComponent.withdraft(1L, BigDecimal.TEN);
        });

        assertEquals(Errors.NOT_ENOUGH_AMOUNTS.getMessage(), exception.getMessage());
        assertEquals(Errors.NOT_ENOUGH_AMOUNTS.getCode(), exception.getCode());
    }

    @Test
    public void withdraw_from_existed_amount_not_enough_amount_expected_exception() {
        Amount expectedAmount = new Amount(1L, BigDecimal.ONE);

        when(amountRepo.findAmountByUserId(1L)).thenReturn(expectedAmount);

        NotEnoughAmountException exception = assertThrows(NotEnoughAmountException.class, () -> {
            amountComponent.withdraft(1L, BigDecimal.TEN);
        });

        assertEquals(Errors.NOT_ENOUGH_AMOUNTS.getMessage(), exception.getMessage());
        assertEquals(Errors.NOT_ENOUGH_AMOUNTS.getCode(), exception.getCode());
    }

    @Test
    public void withdraw_from_existed_amount_with_enough_amount_success() throws CommonServiceException {
        Amount expectedAmount = new Amount(1L, BigDecimal.TEN);

        when(amountRepo.save(any(Amount.class))).thenAnswer(a -> a.getArgument(0));
        when(amountRepo.findAmountByUserId(1L)).thenReturn(expectedAmount);

        Amount amount = amountComponent.withdraft(1L, BigDecimal.ONE);

        assertEquals(1L, amount.getUserId());
        assertEquals(BigDecimal.valueOf(9), amount.getAmount());
    }

    @Test
    public void transfer_money_arguments_are_null_exception_expected(){
        assertThrows(IllegalArgumentException.class, () -> {
            amountComponent.transferAmount(1L, 2L, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            amountComponent.transferAmount(1L, null, BigDecimal.ZERO);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            amountComponent.transferAmount(null, 2L, BigDecimal.ZERO);
        });
    }

    @Test
    public void transfer_money_amount_is_less_than_zero(){
        WrongAmountException exception = assertThrows(WrongAmountException.class, () -> {
            amountComponent.transferAmount(1L, 2L, BigDecimal.valueOf(-12L));
        });

        assertEquals(Errors.WRONG_AMOUNTS.getMessage(), exception.getMessage());
        assertEquals(Errors.WRONG_AMOUNTS.getCode(), exception.getCode());
    }
}

