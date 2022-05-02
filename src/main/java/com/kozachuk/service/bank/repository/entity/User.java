package com.kozachuk.service.bank.repository.entity;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String username;
    private Boolean enabled;

    protected User() {}

    public User(String firstName, Boolean enabled) {
        this.username = firstName;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Boolean isEnabled() {
        return enabled;
    }
}
