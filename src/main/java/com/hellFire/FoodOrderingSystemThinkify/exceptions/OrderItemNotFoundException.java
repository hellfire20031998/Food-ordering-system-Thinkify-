package com.hellFire.FoodOrderingSystemThinkify.exceptions;

public class OrderItemNotFoundException extends BusinessException{
    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
