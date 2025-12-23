package com.hellFire.FoodOrderingSystemThinkify.exceptions;

public class RestaurantNotFoundException extends BusinessException{

    public RestaurantNotFoundException(String message){
        super(message);
    }
}
