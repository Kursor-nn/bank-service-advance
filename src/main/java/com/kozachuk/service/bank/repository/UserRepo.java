package com.kozachuk.service.bank.repository;

import com.kozachuk.service.bank.repository.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findById(long id);
}