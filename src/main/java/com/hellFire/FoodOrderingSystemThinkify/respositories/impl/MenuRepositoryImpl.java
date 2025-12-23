package com.hellFire.FoodOrderingSystemThinkify.respositories.impl;

import com.hellFire.FoodOrderingSystemThinkify.models.Menu;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IMenuRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hellFire.FoodOrderingSystemThinkify.utils.Utils.getNextId;

@Repository
public class MenuRepositoryImpl implements IMenuRepository {

    private final HashMap<Long, Menu> menus = new HashMap<>();

    @Override
    public Menu findById(Long id) {
        return menus.getOrDefault(id, null);
    }

    @Override
    public Menu save(Menu menu) {
        menu.setId(getNextId());
        menus.put(menu.getId(), menu);
        return menu;
    }

    @Override
    public List<Menu> saveAll(List<Menu> menuList) {
        return menuList.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    public List<Menu> findAll() {
        return new ArrayList<>(menus.values());
    }

    @Override
    public List<Menu> findByRestaurantId(Long restaurantId) {
        return menus.values().stream().filter(s -> Objects.equals(s.getRestaurant().getId(), restaurantId)).collect(Collectors.toList());
    }

    @Override
    public List<Menu> findByItemName(String itemName) {
        return List.of();
    }

}
