package com.hellFire.FoodOrderingSystemThinkify.dtos.requests;

import com.hellFire.FoodOrderingSystemThinkify.models.pojo.ItemPrice;
import lombok.Data;

@Data
public class AddWalletRequest {
    private ItemPrice itemPrice;
}
