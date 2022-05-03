package com.kozachuk.service.bank.repository;

import com.kozachuk.service.bank.repository.entity.Amount;
import org.springframework.data.repository.CrudRepository;

public interface AmountRepo extends CrudRepository<Amount, Long> {
    /**
     * Find actual amount by user id
     * @param userId
     * @return amount
     */
    Amount findAmountByUserId(Long userId);

    /**
     * Save amount
     * @param amount
     * @return
     */
    Amount save(Amount amount);

}