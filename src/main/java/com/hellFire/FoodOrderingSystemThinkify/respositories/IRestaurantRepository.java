package com.hellFire.FoodOrderingSystemThinkify.respositories;

import com.hellFire.FoodOrderingSystemThinkify.models.Restaurant;

import java.util.List;

public interface IRestaurantRepository {

    Restaurant save(Restaurant restaurant);
    Restaurant findById(Long id);
    Restaurant findByName(String name);
    List<Restaurant> findAll();
}
