package com.kozachuk.service.bank.repository;

import com.kozachuk.service.bank.repository.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
    /**
     * Find use entity by username
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * Find User entity by user id
     * @param id
     * @return
     */
    User findById(long id);
}