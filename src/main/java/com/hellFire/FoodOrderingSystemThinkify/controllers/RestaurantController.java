package com.hellFire.FoodOrderingSystemThinkify.controllers;


import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.CreateMenuRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.CreateRestaurantRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.UpdateMenuRequest;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.MenuItemNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.RestaurantAlreadyExistException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.RestaurantNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.services.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<?> getAllRestaurants() {
        return new ResponseEntity<>(restaurantService.getAllRestaurants(), HttpStatus.OK);
    }

    @PostMapping("/onboard")
    public ResponseEntity<?> onboard(
            @Valid @RequestBody CreateRestaurantRequest request) {
        try {
            return new ResponseEntity<>(restaurantService.onboard(request), HttpStatus.CREATED);
        } catch (RestaurantAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{restaurantName}/menu")
    public ResponseEntity<?> updateMenu(
            @PathVariable String restaurantName,
            @RequestBody UpdateMenuRequest request) {
        try {
            return new ResponseEntity<>( restaurantService.updateMenu(restaurantName, request), HttpStatus.OK);
        } catch (RestaurantNotFoundException | MenuItemNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{restaurantName}/menu")
    public ResponseEntity<?> addMenu(
            @PathVariable String restaurantName,
            @RequestBody @Valid List<CreateMenuRequest> request) {
        try {
            return new ResponseEntity<>( restaurantService.addMenu(restaurantName, request), HttpStatus.OK);
        } catch (RestaurantNotFoundException | MenuItemNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

