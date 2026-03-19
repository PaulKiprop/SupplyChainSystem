package com.paul.supplychain.dto;

import jakarta.validation.constraints.NotBlank;

public record WarehouseRequestDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Location is required")
        String location
) {
}
