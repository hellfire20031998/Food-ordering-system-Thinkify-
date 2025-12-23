package com.hellFire.FoodOrderingSystemThinkify.respositories;


import com.hellFire.FoodOrderingSystemThinkify.models.Wallet;

import java.util.List;

public interface IWalletRepository {

    public Wallet save(Wallet wallet);
    public Wallet findById(Long id);
    public List<Wallet> findAll();
}
