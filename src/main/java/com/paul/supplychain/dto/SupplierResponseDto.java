package com.paul.supplychain.dto;

import java.util.List;

public record SupplierResponseDto(
        Long id,
        String name,
        String email,
        List<ProductResponseDto> products
) {
}
