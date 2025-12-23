package com.hellFire.FoodOrderingSystemThinkify.respositories.impl;

import com.hellFire.FoodOrderingSystemThinkify.models.Restaurant;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IRestaurantRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.hellFire.FoodOrderingSystemThinkify.utils.Utils.getNextId;

@Repository
public class RestaurantRepositoryImpl implements IRestaurantRepository {

    private final HashMap<Long, Restaurant> restaurants = new HashMap<>();

    @Override
    public Restaurant save(Restaurant restaurant) {
        restaurant.setId(getNextId());
        restaurants.put(restaurant.getId(), restaurant);
        return restaurant;
    }

    @Override
    public Restaurant findById(Long id) {
        return restaurants.getOrDefault(id, null);
    }

    @Override
    public Restaurant findByName(String name) {
        return restaurants.values().stream().filter(restaurant -> restaurant.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants.values());
    }
}
