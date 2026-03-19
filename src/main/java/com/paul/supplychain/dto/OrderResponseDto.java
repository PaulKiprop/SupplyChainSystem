package com.paul.supplychain.dto;

import com.paul.supplychain.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long id,
        String orderNumber,
        OrderStatus status,
        Long warehouseId,
        String warehouseName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime expectedDeliveryDate,
        LocalDateTime deliveredAt,
        String notes,
        List<OrderItemResponseDto> items,
        List<OrderStatusHistoryResponseDto> statusHistory
) {
}
