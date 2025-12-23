package com.hellFire.FoodOrderingSystemThinkify.models;

import com.hellFire.FoodOrderingSystemThinkify.models.pojo.ItemPrice;
import lombok.Data;

@Data
public class Menu {
    private Long id;
    private String itemName;
    private Restaurant restaurant;
    private ItemPrice itemPrice;
}
