package org.example.wallettest.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Table(name= "wallet")
public class Wallet {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}
