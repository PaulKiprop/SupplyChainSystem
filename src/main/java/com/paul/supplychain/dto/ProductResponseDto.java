package com.paul.supplychain.dto;

public record ProductResponseDto(
        Long id,
        String name,
        String sku,
        String description,
        Double price
) {
}
