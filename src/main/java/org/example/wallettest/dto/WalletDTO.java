package org.example.wallettest.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletDTO {
    private UUID id;

    @JsonSerialize(using = org.example.wallettest.dto.BigDecimalSerializer.class)
    private BigDecimal balance;
}
