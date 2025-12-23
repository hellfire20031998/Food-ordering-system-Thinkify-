package com.hellFire.FoodOrderingSystemThinkify.strategies;

import com.hellFire.FoodOrderingSystemThinkify.models.AppUser;

public interface IPaymentStrategy {
    boolean pay(AppUser user, double amount);
}
