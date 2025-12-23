package com.hellFire.FoodOrderingSystemThinkify.models;

import com.hellFire.FoodOrderingSystemThinkify.models.pojo.ItemPrice;
import lombok.Data;

@Data
public class Wallet {
    private Long id;
    private ItemPrice itemPrice;
}
