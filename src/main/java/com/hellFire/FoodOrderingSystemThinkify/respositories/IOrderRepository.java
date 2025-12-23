package com.hellFire.FoodOrderingSystemThinkify.respositories;

import com.hellFire.FoodOrderingSystemThinkify.models.Order;

import java.util.List;

public interface IOrderRepository {

    Order save(Order order);
    List<Order> saveAll(List<Order> orders);
    List<Order> findAll();
    Order findById(Long id);
}
