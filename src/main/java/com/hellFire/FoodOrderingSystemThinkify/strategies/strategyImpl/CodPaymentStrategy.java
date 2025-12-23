package com.hellFire.FoodOrderingSystemThinkify.strategies.strategyImpl;

import com.hellFire.FoodOrderingSystemThinkify.models.AppUser;
import com.hellFire.FoodOrderingSystemThinkify.strategies.IPaymentStrategy;

public class CodPaymentStrategy implements IPaymentStrategy {
    @Override
    public boolean pay(AppUser user, double amount) {
        return false;
    }
}
