package com.hellFire.FoodOrderingSystemThinkify.exceptions;

public class MenuItemNotFoundException extends BusinessException{
    public MenuItemNotFoundException(String message){
        super(message);
    }
}
