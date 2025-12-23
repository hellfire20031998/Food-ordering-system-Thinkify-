package com.hellFire.FoodOrderingSystemThinkify.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@Data
public class Restaurant {
    private Long id;
    private String name;
    private double rating;
    private int maxOrders;
    private int currentOrders = 0;

    public boolean canAcceptOrder() {
        return currentOrders < maxOrders;
    }

    public void acceptOrder() {
        currentOrders++;
    }

    public void completeOrder() {
        currentOrders--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
