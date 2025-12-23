package com.hellFire.FoodOrderingSystemThinkify.strategies.strategyImpl;

import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.OrderItemRequest;
import com.hellFire.FoodOrderingSystemThinkify.models.Menu;
import com.hellFire.FoodOrderingSystemThinkify.models.Restaurant;
import com.hellFire.FoodOrderingSystemThinkify.strategies.IRestaurantSelectionStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.hellFire.FoodOrderingSystemThinkify.utils.Utils.normalize;

public class LowestCostStrategy implements IRestaurantSelectionStrategy {

    @Override
    public Restaurant selectRestaurant(Map<Restaurant, List<Menu>> restaurants,
                                       List<OrderItemRequest> orderItems) {

        return restaurants.entrySet().stream()
                .filter(restaurantListEntry -> restaurantListEntry.getKey().canAcceptOrder())
                .min(Comparator.comparing(entry -> {
                    Restaurant r = entry.getKey();
                    List<Menu> menus = entry.getValue();

                    double total = 0;

                    for (OrderItemRequest itemReq : orderItems) {
                        Menu menu = menus.stream()
                                .filter(m -> Objects.equals(normalize(m.getItemName()), normalize(itemReq.getItemName())))
                                .findFirst()
                                .orElseThrow();

                        total += menu.getItemPrice().getPrice() * itemReq.getQuantity();
                    }

                    return total;
                }))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

}
