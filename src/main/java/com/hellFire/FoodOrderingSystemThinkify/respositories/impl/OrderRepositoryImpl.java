package com.hellFire.FoodOrderingSystemThinkify.respositories.impl;

import com.hellFire.FoodOrderingSystemThinkify.models.Order;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.hellFire.FoodOrderingSystemThinkify.utils.Utils.getNextId;

@Repository
public class OrderRepositoryImpl implements IOrderRepository {

    private final HashMap<Long, Order> orders = new HashMap<>();

    @Override
    public Order save(Order order) {
        order.setId(getNextId());
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public List<Order> saveAll(List<Order> orders) {
        return orders.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order findById(Long id) {
        return orders.getOrDefault(id, null);
    }
}
