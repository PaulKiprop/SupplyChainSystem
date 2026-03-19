package com.paul.supplychain.dto;

public record InventoryResponseDto(
        Long id,
        Long warehouseId,
        String warehouseName,
        Long productId,
        String productName,
        Integer quantity
) {
}
