package com.hellFire.FoodOrderingSystemThinkify.respositories;

import com.hellFire.FoodOrderingSystemThinkify.models.OrderedItem;

import java.util.List;

public interface IOrderedItemRepository {

    OrderedItem save(OrderedItem item);
    List<OrderedItem> findAll();
    List<OrderedItem> saveAll(List<OrderedItem> orderedItems);
    OrderedItem findById(Long id);
}
