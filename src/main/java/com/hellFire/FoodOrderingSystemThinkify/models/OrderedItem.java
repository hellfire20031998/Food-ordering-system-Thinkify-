package com.hellFire.FoodOrderingSystemThinkify.models;

import lombok.Data;

@Data
public class OrderedItem {
    private Long id;
    private Menu menu;
    private int quantity;
}
