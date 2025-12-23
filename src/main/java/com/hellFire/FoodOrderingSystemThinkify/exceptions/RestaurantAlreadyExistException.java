package com.hellFire.FoodOrderingSystemThinkify.exceptions;

public class RestaurantAlreadyExistException extends BusinessException{
    public RestaurantAlreadyExistException(String message){
        super(message);
    }
}
