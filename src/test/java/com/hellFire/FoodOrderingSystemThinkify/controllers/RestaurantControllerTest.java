package com.hellFire.FoodOrderingSystemThinkify.controllers;

import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.CreateMenuRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.CreateRestaurantRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.UpdateMenuRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.MenuDto;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.RestaurantDto;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.MenuItemNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.RestaurantAlreadyExistException;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.RestaurantNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.models.enums.Currency;
import com.hellFire.FoodOrderingSystemThinkify.models.pojo.ItemPrice;
import com.hellFire.FoodOrderingSystemThinkify.services.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllRestaurants() throws Exception {

        RestaurantDto r1 = new RestaurantDto();
        r1.setName("R1");
        r1.setRating(4.5);

        RestaurantDto r2 = new RestaurantDto();
        r2.setName("R2");
        r2.setRating(4.0);

        Mockito.when(restaurantService.getAllRestaurants())
                .thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/restaurant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("R1"))
                .andExpect(jsonPath("$[0].rating").value(4.5))
                .andExpect(jsonPath("$[1].name").value("R2"))
                .andExpect(jsonPath("$[1].rating").value(4.0));
    }

    @Test
    void testOnboardRestaurant_success() throws Exception {

        CreateRestaurantRequest request = new CreateRestaurantRequest();
        request.setName("R1");
        request.setMaxOrders(5);
        request.setRating(4.5);
        request.setMenuRequests(Collections.emptyList());

        RestaurantDto response = new RestaurantDto();
        response.setName("R1");
        response.setRating(4.5);
        response.setMenuItems(Collections.emptyList());

        Mockito.when(restaurantService.onboard(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/restaurant/onboard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("R1"))
                .andExpect(jsonPath("$.rating").value(4.5));
    }

    @Test
    void testOnboardRestaurant_alreadyExists() throws Exception {

        CreateRestaurantRequest request = new CreateRestaurantRequest();
        request.setName("R1");
        request.setMaxOrders(5);
        request.setRating(4.5);
        request.setMenuRequests(Collections.emptyList());

        Mockito.when(restaurantService.onboard(Mockito.any()))
                .thenThrow(new RestaurantAlreadyExistException("Restaurant exists"));

        mockMvc.perform(post("/restaurant/onboard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict())
                .andExpect(content().string("Restaurant exists"));
    }

    @Test
    void testUpdateMenu_success() throws Exception {

        UpdateMenuRequest request = new UpdateMenuRequest();
        request.setItemName("Dosa");
        request.setItemPrice(new ItemPrice(60.0, Currency.INR));

        Mockito.when(restaurantService.updateMenu(Mockito.eq(1L), Mockito.any()))
                .thenReturn("Menu Updated");

        mockMvc.perform(put("/restaurant/1/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Menu Updated"));
    }

    @Test
    void testUpdateMenu_restaurantNotFound() throws Exception {

        UpdateMenuRequest request = new UpdateMenuRequest();
        request.setItemName("Dosa");
        request.setItemPrice(new ItemPrice(60.0, Currency.INR));

        Mockito.when(restaurantService.updateMenu(Mockito.eq(1L), Mockito.any()))
                .thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        mockMvc.perform(put("/restaurant/1/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict())
                .andExpect(content().string("Restaurant not found"));
    }

    @Test
    void testAddMenu_success() throws Exception {

        CreateMenuRequest menu = new CreateMenuRequest();
        menu.setItemName("Idli");
        menu.setItemPrice(new ItemPrice(60.0, Currency.INR));

        MenuDto menuDto = new MenuDto();
        menuDto.setId(null);
        menuDto.setItemName("Idli");
        menuDto.setItemPrice(new ItemPrice(60.0, Currency.INR));

        Mockito.when(restaurantService.addMenu(Mockito.eq(1L), Mockito.any()))
                .thenReturn(List.of(menuDto));

        mockMvc.perform(post("/restaurant/1/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(menu))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Idli"))
                .andExpect(jsonPath("$[0].itemPrice.price").value(60.0))
                .andExpect(jsonPath("$[0].itemPrice.currency").value("INR"));
    }

    @Test
    void testAddMenu_menuItemNotFound() throws Exception {

        CreateMenuRequest menu = new CreateMenuRequest();
        menu.setItemName("Idli");
        menu.setItemPrice(new ItemPrice(60.0, Currency.INR));

        List<CreateMenuRequest> req = List.of(menu);

        Mockito.when(restaurantService.addMenu(Mockito.eq(1L), Mockito.any()))
                .thenThrow(new MenuItemNotFoundException("Menu item not found"));

        mockMvc.perform(post("/restaurant/1/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isConflict())
                .andExpect(content().string("Menu item not found"));
    }
}
