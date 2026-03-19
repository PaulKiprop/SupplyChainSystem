package com.paul.supplychain.dto;

public record OrderItemResponseDto(
        Long productId,
        String productName,
        String sku,
        Integer quantity,
        Double unitPrice,
        Double lineTotal
) {
}
