package com.hellFire.FoodOrderingSystemThinkify.models;

import com.hellFire.FoodOrderingSystemThinkify.models.enums.OrderFullFilledBy;
import com.hellFire.FoodOrderingSystemThinkify.models.enums.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class Order {
    private Long id;
    private AppUser user;
    private List<OrderedItem> orderedItems;
    private OrderStatus status;
    private OrderFullFilledBy fullFilledBy;
}
