package com.hellFire.FoodOrderingSystemThinkify.exceptions;

public class MenuItemAlreadyExistException extends BusinessException{
    public MenuItemAlreadyExistException(String message) {
        super(message);
    }
}
