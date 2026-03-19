package com.paul.supplychain.dto;

import com.paul.supplychain.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrderStatusHistoryResponseDto(
        OrderStatus fromStatus,
        OrderStatus toStatus,
        LocalDateTime changedAt,
        String comment
) {
}
