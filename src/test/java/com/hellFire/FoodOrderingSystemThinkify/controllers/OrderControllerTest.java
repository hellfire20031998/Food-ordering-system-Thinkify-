package com.hellFire.FoodOrderingSystemThinkify.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.OrderItemRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.OrderRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.OrderDto;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.OrderedItemDto;
import com.hellFire.FoodOrderingSystemThinkify.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequest request;

    @BeforeEach
    void setup() {
        OrderItemRequest item = new OrderItemRequest();
        item.setItemName("Pizza");
        item.setQuantity(2);

        request = new OrderRequest();
        request.setUserId(1L);
        request.setOrderItems(List.of(item));
    }

    @Test
    void testPlaceOrder_success() throws Exception {

        OrderDto dto = new OrderDto();
        dto.setId(10L);

        Mockito.when(orderService.placeOrder(Mockito.any()))
                .thenReturn(dto);

        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void testGetAllOrders_success() throws Exception {

        OrderDto dto = new OrderDto();
        dto.setId(10L);

        Mockito.when(orderService.getAllOrders())
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/order/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L));
    }

    @Test
    void testMarkOrderItemComplete_success() throws Exception {

        OrderedItemDto itemDto = new OrderedItemDto();
        itemDto.setId(5L);
        itemDto.setQuantity(2);

        Mockito.when(orderService.completeOrderItem(Mockito.eq(1L), Mockito.eq(5L), Mockito.eq(3L)))
                .thenReturn(itemDto);

        mockMvc.perform(put("/order/restaurant/3/order/1/complete/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L));
    }

    @Test
    void testPlaceOrder_userNotFound() throws Exception {

        Mockito.when(orderService.placeOrder(Mockito.any()))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


}
