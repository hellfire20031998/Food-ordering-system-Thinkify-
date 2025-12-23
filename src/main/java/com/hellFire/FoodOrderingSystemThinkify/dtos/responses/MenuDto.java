package com.hellFire.FoodOrderingSystemThinkify.dtos.responses;

import com.hellFire.FoodOrderingSystemThinkify.models.pojo.ItemPrice;
import lombok.Data;

@Data
public class MenuDto {
    private Long id;
    private String itemName;
    private ItemPrice itemPrice;
}
