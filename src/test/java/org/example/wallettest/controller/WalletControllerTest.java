package org.example.wallettest.controller;

import org.example.wallettest.dto.WalletDTO;
import org.example.wallettest.service.RateLimiterService;
import org.example.wallettest.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WalletControllerTest {

    @InjectMocks
    private WalletController walletController;

    @Mock
    private WalletService walletService;

    @Mock
    private RateLimiterService rateLimiterService;

    private UUID walletId;
    private WalletDTO walletDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        walletId = UUID.randomUUID();
        walletDTO = new WalletDTO();
        walletDTO.setId(walletId);
        walletDTO.setBalance(BigDecimal.ZERO);
    }

    @Test
    public void testCreateWallet() {
        when(walletService.createWallet()).thenReturn(walletDTO);

        ResponseEntity<WalletDTO> response = walletController.createWallet();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(walletDTO, response.getBody());
        verify(walletService, times(1)).createWallet();
    }

    @Test
    public void testPerformOperation_Success() {
        String operationType = "DEPOSIT";
        BigDecimal amount = BigDecimal.valueOf(100.00);

        when(rateLimiterService.tryConsume(walletId)).thenReturn(true);
        when(walletService.updateBalance(walletId, operationType, amount)).thenReturn(walletDTO);

        ResponseEntity<WalletDTO> response = walletController.performOperation(walletId, operationType, amount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(walletDTO, response.getBody());
        verify(walletService, times(1)).updateBalance(walletId, operationType, amount);
    }

    @Test
    public void testPerformOperation_RateLimitExceeded() {
        String operationType = "DEPOSIT";
        BigDecimal amount = BigDecimal.valueOf(100.00);

        when(rateLimiterService.tryConsume(walletId)).thenReturn(false);

        ResponseEntity<WalletDTO> response = walletController.performOperation(walletId, operationType, amount);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        verify(walletService, never()).updateBalance(any(), any(), any());
    }

    @Test
    public void testGetBalance_Success() {
        when(walletService.getBalance(walletId)).thenReturn(BigDecimal.valueOf(200.00));

        ResponseEntity<BigDecimal> response = walletController.getBalance(walletId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BigDecimal.valueOf(200.00), response.getBody());
        verify(walletService, times(1)).getBalance(walletId);
    }
}