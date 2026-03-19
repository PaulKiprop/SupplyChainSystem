package com.paul.supplychain.controller;

import com.paul.supplychain.dto.DtoMapper;
import com.paul.supplychain.dto.InventoryResponseDto;
import com.paul.supplychain.entity.Inventory;
import com.paul.supplychain.service.impl.InventoryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        Inventory inventory = service.addInventory(warehouseId, productId, quantity);
        return ResponseEntity.ok(DtoMapper.toInventoryDto(inventory));
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<List<InventoryResponseDto>> getInventory(@PathVariable Long warehouseId) {
        List<InventoryResponseDto> dtos = service.getInventoryByWarehouse(warehouseId)
                .stream()
                .map(DtoMapper::toInventoryDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
