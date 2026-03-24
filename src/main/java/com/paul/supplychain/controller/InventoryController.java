package com.paul.supplychain.controller;

import com.paul.supplychain.dto.*;
import com.paul.supplychain.service.impl.InventoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryServiceImpl service;

    public InventoryController(InventoryServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/{warehouseId}/products/{productId}")
    public ResponseEntity<InventoryResponseDto> addInventory(
            @PathVariable Long warehouseId,
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok(DtoMapper.toInventoryDto(service.addInventory(warehouseId, productId, quantity)));
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<PageResponseDto<InventoryResponseDto>> getInventory(
            @PathVariable Long warehouseId,
            @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(
                PageResponseDto.from(service.getInventoryByWarehouse(warehouseId, pageable).map(DtoMapper::toInventoryDto))
        );
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<InventoryResponseDto> adjustQuantity(
            @PathVariable Long id,
            @Valid @RequestBody InventoryAdjustRequestDto dto
    ) {
        return ResponseEntity.ok(DtoMapper.toInventoryDto(service.adjustQuantity(id, dto.quantity())));
    }
}