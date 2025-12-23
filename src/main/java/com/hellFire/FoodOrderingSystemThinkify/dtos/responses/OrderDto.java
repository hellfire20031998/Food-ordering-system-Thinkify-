package com.hellFire.FoodOrderingSystemThinkify.dtos.responses;

import com.hellFire.FoodOrderingSystemThinkify.models.enums.OrderFullFilledBy;
import com.hellFire.FoodOrderingSystemThinkify.models.enums.OrderStatus;
import com.hellFire.FoodOrderingSystemThinkify.models.enums.PaymentStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private AppUserDto user;
    private List<OrderedItemDto> orderedItems;
    private OrderStatus status;
    private OrderFullFilledBy fullFilledBy;
    private PaymentStatus paymentStatus;
    private Double totalAmount;
}
