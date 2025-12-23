package com.hellFire.FoodOrderingSystemThinkify.models;

import com.hellFire.FoodOrderingSystemThinkify.models.enums.OrderItemStatus;
import lombok.Data;

@Data
public class OrderedItem {
    private Long id;
    private Menu menu;
    private int quantity;
    private OrderItemStatus orderItemStatus;
}
