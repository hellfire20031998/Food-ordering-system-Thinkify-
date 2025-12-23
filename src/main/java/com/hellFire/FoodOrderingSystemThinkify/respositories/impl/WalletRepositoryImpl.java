package com.hellFire.FoodOrderingSystemThinkify.respositories.impl;

import com.hellFire.FoodOrderingSystemThinkify.models.Wallet;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IWalletRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hellFire.FoodOrderingSystemThinkify.utils.Utils.getNextId;

@Repository
public class WalletRepositoryImpl implements IWalletRepository {

    private final Map<Long, Wallet> wallets = new HashMap<Long, Wallet>();
    @Override
    public Wallet save(Wallet wallet) {
        if(wallet.getId() == null){
            wallet.setId(getNextId());
        }
        wallets.put(wallet.getId(), wallet);
        return wallet;
    }

    @Override
    public Wallet findById(Long id) {
        return wallets.getOrDefault(id, null);
    }

    @Override
    public List<Wallet> findAll() {
        return new ArrayList<Wallet>(wallets.values());
    }
}
