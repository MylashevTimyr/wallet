package org.example.wallettest.controller;

import lombok.RequiredArgsConstructor;
import org.example.wallettest.dto.WalletDTO;
import org.example.wallettest.service.RateLimiterService;
import org.example.wallettest.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    private final WalletService walletService;
    private final RateLimiterService rateLimiterService;

    @PostMapping
    public ResponseEntity<WalletDTO> createWallet() {
        WalletDTO wallet = walletService.createWallet();
        logger.info("Wallet created with ID: {}", wallet.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    @PostMapping("/{walletId}")
    public ResponseEntity<WalletDTO> performOperation(
            @RequestParam UUID walletId,
            @RequestParam String operationType,
            @RequestParam BigDecimal amount) {

        if (!rateLimiterService.tryConsume(walletId)) {
            logger.warn("Rate limit exceeded for wallet {}", walletId);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        WalletDTO updatedWallet = walletService.updateBalance(walletId, operationType, amount);
        logger.info("Operation {} performed on wallet {}: new balance {}", operationType, walletId, updatedWallet.getBalance());
        return ResponseEntity.ok(updatedWallet);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID walletId) {
        BigDecimal balance = walletService.getBalance(walletId);
        logger.info("Balance retrieved for wallet {}: {}", walletId, balance);
        return ResponseEntity.ok(balance);
    }
}