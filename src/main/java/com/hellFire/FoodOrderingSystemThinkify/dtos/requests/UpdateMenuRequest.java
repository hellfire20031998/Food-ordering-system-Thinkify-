package com.hellFire.FoodOrderingSystemThinkify.dtos.requests;

import com.hellFire.FoodOrderingSystemThinkify.models.pojo.ItemPrice;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateMenuRequest {
    @NotNull(message = "menuId can not be null")
    private Long id;
    private String itemName;
    private ItemPrice itemPrice;
}
