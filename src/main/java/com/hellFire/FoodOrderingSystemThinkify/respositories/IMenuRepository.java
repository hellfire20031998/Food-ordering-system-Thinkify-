package com.hellFire.FoodOrderingSystemThinkify.respositories;

import com.hellFire.FoodOrderingSystemThinkify.models.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IMenuRepository {
    Menu findById(Long id);
    Menu save(Menu menu);
    List<Menu> saveAll(List<Menu> menuList);
    List<Menu> findAll();
    List<Menu> findByRestaurantId(Long restaurantId);
    List<Menu> findByItemName(String itemName);

}
