package com.hellFire.FoodOrderingSystemThinkify.dtos.responses;

import lombok.Data;

import java.util.List;

@Data
public class RestaurantDto {
    private Long id;
    private String name;
    private List<MenuDto> menuItems;
    private double rating;
}
