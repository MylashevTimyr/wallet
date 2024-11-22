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
        int updatedRows;

        switch (operationType) {
            case "DEPOSIT" ->
                    updatedRows = walletRepository.deposit(walletId, amount);
            case "WITHDRAW" ->
                    updatedRows = walletRepository.withdraw(walletId, amount);
            default ->
                    throw new IllegalArgumentException("Invalid operation type");
        }
        if (updatedRows == 0) {
            throw new IllegalStateException("Operation failed: wallet not found or insufficient balance.");
        }

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
        return WalletMapper.toDTO(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
        return wallet.getBalance();
    }
}
