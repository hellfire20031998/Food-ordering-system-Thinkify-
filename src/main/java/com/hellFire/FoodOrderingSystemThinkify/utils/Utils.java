package com.hellFire.FoodOrderingSystemThinkify.utils;

import java.util.concurrent.atomic.AtomicLong;

public class Utils {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public static Long getNextId() {
        return ID_GENERATOR.getAndIncrement();
    }

    public static String normalize(String itemName) {
        return itemName.trim().toLowerCase();
    }

}
