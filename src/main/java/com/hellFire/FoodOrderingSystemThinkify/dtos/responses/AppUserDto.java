package com.hellFire.FoodOrderingSystemThinkify.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {
    private Long id;
    private String name;
}
