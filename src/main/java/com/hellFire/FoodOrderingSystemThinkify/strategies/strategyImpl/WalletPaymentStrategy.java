package com.hellFire.FoodOrderingSystemThinkify.strategies.strategyImpl;

import com.hellFire.FoodOrderingSystemThinkify.models.AppUser;
import com.hellFire.FoodOrderingSystemThinkify.strategies.IPaymentStrategy;

public class WalletPaymentStrategy implements IPaymentStrategy {
    @Override
    public boolean pay(AppUser user, double amount) {
        double balance = user.getWallet().getItemPrice().getPrice();

        if (balance >= amount) {
            user.getWallet().getItemPrice().setPrice(balance - amount);
            return true;
        }

        return false;
    }

}
