package com.hellFire.FoodOrderingSystemThinkify.models.pojo;

import com.hellFire.FoodOrderingSystemThinkify.models.enums.Currency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemPrice {
    @DecimalMin(value = "1.0", message = "minimum price at least 1")
    private Double price;
    @Valid
    private Currency currency;
}
