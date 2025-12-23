package com.hellFire.FoodOrderingSystemThinkify.models.pojo;

import com.hellFire.FoodOrderingSystemThinkify.models.enums.Currency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class ItemPrice {
    @DecimalMin(value = "1.0", message = "minimum price at least 1")
    private Double price;
    @Valid
    private Currency currency;
}
