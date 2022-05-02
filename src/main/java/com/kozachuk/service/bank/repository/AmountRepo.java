package com.kozachuk.service.bank.repository;

import com.kozachuk.service.bank.repository.entity.Amount;
import org.springframework.data.repository.CrudRepository;

public interface AmountRepo extends CrudRepository<Amount, Long> {
    Amount findAmountByUserId(Long userId);
    Amount save(Amount amount);

}