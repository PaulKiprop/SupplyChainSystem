package com.paul.supplychain.dto;

import jakarta.validation.constraints.NotNull;

public record InventoryAdjustRequestDto(
        @NotNull(message = "Quantity is required")
        Integer quantity
) {
}