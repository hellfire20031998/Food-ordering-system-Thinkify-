package com.hellFire.FoodOrderingSystemThinkify.strategies.factory;

import com.hellFire.FoodOrderingSystemThinkify.models.enums.PaymentMethod;
import com.hellFire.FoodOrderingSystemThinkify.strategies.IPaymentStrategy;
import com.hellFire.FoodOrderingSystemThinkify.strategies.strategyImpl.CodPaymentStrategy;
import com.hellFire.FoodOrderingSystemThinkify.strategies.strategyImpl.UPIPaymentStrategy;
import com.hellFire.FoodOrderingSystemThinkify.strategies.strategyImpl.WalletPaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class PaymentFactory {

    public IPaymentStrategy getPaymentStrategy(PaymentMethod method) {
            if (PaymentMethod.COD.equals(method)) {
                return new CodPaymentStrategy();
            } else if (PaymentMethod.UPI.equals(method)) {
                return new UPIPaymentStrategy();
            }else {
                return new WalletPaymentStrategy();
            }
    }
}
