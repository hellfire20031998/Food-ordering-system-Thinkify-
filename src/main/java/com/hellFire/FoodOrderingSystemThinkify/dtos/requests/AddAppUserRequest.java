package com.hellFire.FoodOrderingSystemThinkify.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddAppUserRequest {
    @NotBlank(message = "User name can not be blank")
    private String name;
}
