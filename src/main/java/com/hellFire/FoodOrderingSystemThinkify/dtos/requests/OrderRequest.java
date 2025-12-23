package com.hellFire.FoodOrderingSystemThinkify.dtos.requests;

import com.hellFire.FoodOrderingSystemThinkify.models.enums.RestaurantSelection;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    @NotNull(message = "User Id can not be null")
    private Long userId;
    @NotEmpty(message = "Ordering items can not be empty")
    private List<@Valid OrderItemRequest> orderItems;

    private RestaurantSelection strategy;
}
