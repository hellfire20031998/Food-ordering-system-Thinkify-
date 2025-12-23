package com.hellFire.FoodOrderingSystemThinkify.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateRestaurantRequest {
    @NotBlank(message = "Restaurant name can not be blank")
    private String name;
    
    @Min(value = 1, message = "Max orders must be at least 1")
    private int maxOrders;

    @DecimalMax(value = "5.0")
    @DecimalMin(value = "1.0", message = "Min rating must be 1")
    private double rating;

    @NotEmpty(message = "menu items can not be empty")
    private List<@Valid CreateMenuRequest> menuRequests;
}
