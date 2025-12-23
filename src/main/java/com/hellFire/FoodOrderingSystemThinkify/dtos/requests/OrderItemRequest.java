package com.hellFire.FoodOrderingSystemThinkify.dtos.requests;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotBlank(message = "order item name can not be blank")
    private String itemName;
    @Min(value = 1, message = "minimum quantity at least be 1")
    private Integer quantity;
}
