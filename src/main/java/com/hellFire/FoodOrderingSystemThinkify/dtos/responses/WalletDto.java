package com.hellFire.FoodOrderingSystemThinkify.dtos.responses;

import com.hellFire.FoodOrderingSystemThinkify.models.pojo.ItemPrice;
import lombok.Data;

@Data
public class WalletDto {
    private Long id;
    private ItemPrice itemPrice;
}
