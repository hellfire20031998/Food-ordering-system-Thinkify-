package com.hellFire.FoodOrderingSystemThinkify.dtos.responses;

import lombok.Data;

@Data
public class OrderedItemDto {
    private Long id;
    private MenuDto menu;
    private int quantity;
}
