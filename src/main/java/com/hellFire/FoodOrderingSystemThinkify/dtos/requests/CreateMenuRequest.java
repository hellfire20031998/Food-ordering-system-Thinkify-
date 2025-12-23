package com.hellFire.FoodOrderingSystemThinkify.dtos.requests;

import com.hellFire.FoodOrderingSystemThinkify.models.pojo.ItemPrice;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMenuRequest {
    @NotEmpty(message = "item name can not be empty")
    private String itemName;

    @NotNull
    @Valid
    private ItemPrice itemPrice;
}
