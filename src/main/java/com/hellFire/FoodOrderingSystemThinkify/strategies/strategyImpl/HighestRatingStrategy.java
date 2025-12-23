package com.hellFire.FoodOrderingSystemThinkify.strategies.strategyImpl;

import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.OrderItemRequest;
import com.hellFire.FoodOrderingSystemThinkify.models.Menu;
import com.hellFire.FoodOrderingSystemThinkify.models.Restaurant;
import com.hellFire.FoodOrderingSystemThinkify.strategies.IRestaurantSelectionStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HighestRatingStrategy implements IRestaurantSelectionStrategy {

    @Override
    public Restaurant selectRestaurant(Map<Restaurant, List<Menu>> restaurants, List<OrderItemRequest> orderItems) {

        if (restaurants == null || restaurants.isEmpty()) {
            return null;
        }

        return restaurants.keySet().stream()
                .filter(Restaurant::canAcceptOrder)
                .max(Comparator.comparing(Restaurant::getRating))
                .orElse(null);

    }
}
