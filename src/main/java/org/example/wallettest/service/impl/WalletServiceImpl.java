package org.example.wallettest.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.wallettest.dto.WalletDTO;
import org.example.wallettest.entity.Wallet;
import org.example.wallettest.mapper.WalletMapper;
import org.example.wallettest.repository.WalletRepository;
import org.example.wallettest.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public WalletDTO createWallet() {
        Wallet wallet = new Wallet();
        Wallet savedWallet = walletRepository.save(wallet);
        return WalletMapper.toDTO(savedWallet);
    }

    @Override
    @Transactional
    public WalletDTO updateBalance(UUID walletId, String operationType, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        switch (operationType) {
            case "DEPOSIT" -> wallet.setBalance(wallet.getBalance().add(amount));
            case "WITHDRAW" -> {
                if (wallet.getBalance().compareTo(amount) < 0) {
                    throw new IllegalArgumentException("Insufficient balance");
                }
                wallet.setBalance(wallet.getBalance().subtract(amount));
            }
            default -> throw new IllegalArgumentException("Invalid operation type");
        }
        return WalletMapper.toDTO(walletRepository.save(wallet));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
        return wallet.getBalance();
    }
}