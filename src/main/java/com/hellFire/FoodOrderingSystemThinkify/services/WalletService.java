package com.hellFire.FoodOrderingSystemThinkify.services;

import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.WalletDto;
import com.hellFire.FoodOrderingSystemThinkify.models.Wallet;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IWalletRepository;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletService {

    @Autowired
    private IWalletRepository walletRepository;

    public Wallet saveWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }
    public List<Wallet> getWallets() {
        return walletRepository.findAll();
    }

    public WalletDto toDto(Wallet wallet) {
        WalletDto dto = new WalletDto();
        dto.setId(wallet.getId());
        dto.setItemPrice(wallet.getItemPrice());
        return dto;
    }

    public List<WalletDto> toDtoList(List<Wallet> wallets) {
       return wallets.stream().map(this::toDto).collect(Collectors.toList());
    }
}
