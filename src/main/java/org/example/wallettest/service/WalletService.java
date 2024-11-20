package org.example.wallettest.service;

import org.example.wallettest.dto.WalletDTO;

import java.math.BigDecimal;
import java.util.UUID;

public interface WalletService {
    WalletDTO createWallet();
    WalletDTO updateBalance(UUID walletId, String operationType, BigDecimal amount);
    BigDecimal getBalance(UUID walletId);
}
