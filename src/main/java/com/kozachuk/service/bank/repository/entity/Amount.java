package com.kozachuk.service.bank.repository.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "AMOUNTS")
public class Amount {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private BigDecimal amount;

    public Amount() {}

    public Amount(Long userId, BigDecimal amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getId() {
        return id;
    }

    public void addAmount(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

    public void withdraft(BigDecimal amount) {
        this.amount = this.amount.subtract(amount);
    }
}
