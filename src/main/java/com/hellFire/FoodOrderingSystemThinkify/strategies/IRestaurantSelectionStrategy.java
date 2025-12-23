package com.hellFire.FoodOrderingSystemThinkify.strategies;

import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.OrderItemRequest;
import com.hellFire.FoodOrderingSystemThinkify.models.Menu;
import com.hellFire.FoodOrderingSystemThinkify.models.Restaurant;

import java.util.List;
import java.util.Map;

public interface IRestaurantSelectionStrategy {
    Restaurant selectRestaurant(Map<Restaurant, List<Menu>> restaurants,
                                List<OrderItemRequest> orderItems);
}
