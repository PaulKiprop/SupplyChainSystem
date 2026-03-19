package com.paul.supplychain.dto;

import com.paul.supplychain.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(
        @NotNull(message = "Status is required")
        OrderStatus status,
        String comment
) {
}
