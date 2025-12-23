package com.hellFire.FoodOrderingSystemThinkify.controllers;

import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.OrderRequest;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.OrderNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.RestaurantNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.UserNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> place(@RequestBody @Valid OrderRequest request) {
        try {
            return new ResponseEntity<>(orderService.placeOrder(request), HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllOrders() {
        try {
            return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/restaurant/{restaurantId}/order/{orderId}/complete/{itemId}")
    public ResponseEntity<?> markOrderItemComplete(@PathVariable Long orderId,
                                                   @PathVariable Long itemId,
                                                   @PathVariable Long restaurantId) {
        try {
            return new ResponseEntity<>(orderService.completeOrderItem(orderId, itemId, restaurantId), HttpStatus.OK);
        } catch (OrderNotFoundException | RestaurantNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

