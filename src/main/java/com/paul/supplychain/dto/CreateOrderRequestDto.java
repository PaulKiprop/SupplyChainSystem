package com.paul.supplychain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CreateOrderRequestDto(
        @NotNull(message = "Warehouse ID is required")
        Long warehouseId,

        LocalDateTime expectedDeliveryDate,
        String notes,

        @NotEmpty(message = "Order must have at least one item")
        List<@Valid CreateOrderItemRequestDto> items
) {
}
