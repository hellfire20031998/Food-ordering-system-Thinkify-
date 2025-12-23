package com.hellFire.FoodOrderingSystemThinkify.respositories.impl;

import com.hellFire.FoodOrderingSystemThinkify.models.OrderedItem;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IOrderedItemRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.hellFire.FoodOrderingSystemThinkify.utils.Utils.getNextId;

@Repository
public class OrderedItemRepositoryImpl implements IOrderedItemRepository {

    private final HashMap<Long, OrderedItem> orderedItems = new HashMap<>();

    @Override
    public OrderedItem save(OrderedItem item) {
        item.setId(getNextId());
        orderedItems.put(item.getId(), item);
        return item;
    }

    @Override
    public List<OrderedItem> findAll() {
        return new ArrayList<>(orderedItems.values());
    }

    @Override
    public List<OrderedItem> saveAll(List<OrderedItem> orderedItems) {
        return orderedItems.stream().map(this::save).collect(Collectors.toList());
    }
}
