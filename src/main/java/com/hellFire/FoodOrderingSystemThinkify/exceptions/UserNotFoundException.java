package com.hellFire.FoodOrderingSystemThinkify.exceptions;

public class UserNotFoundException extends BusinessException{
    public UserNotFoundException(String message){
        super(message);
    }
}
