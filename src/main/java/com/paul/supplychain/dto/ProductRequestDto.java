package com.paul.supplychain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequestDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "SKU is required")
        String sku,

        String description,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        Double price
) {
}
