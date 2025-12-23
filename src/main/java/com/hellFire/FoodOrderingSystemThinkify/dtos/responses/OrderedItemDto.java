package com.hellFire.FoodOrderingSystemThinkify.dtos.responses;

import com.hellFire.FoodOrderingSystemThinkify.models.enums.OrderItemStatus;
import lombok.Data;

@Data
public class OrderedItemDto {
    private Long id;
    private MenuDto menu;
    private int quantity;
    private OrderItemStatus orderItemStatus;
    private RestaurantDto restaurant;
}
