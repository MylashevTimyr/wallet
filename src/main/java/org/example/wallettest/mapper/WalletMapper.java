package org.example.wallettest.mapper;

import org.example.wallettest.dto.WalletDTO;
import org.example.wallettest.entity.Wallet;

public class WalletMapper {

    public static WalletDTO toDTO(Wallet wallet) {
        if (wallet == null) {
            return null;
        }
        WalletDTO dto = new WalletDTO();
        dto.setId(wallet.getId());
        dto.setBalance(wallet.getBalance());
        return dto;
    }

}