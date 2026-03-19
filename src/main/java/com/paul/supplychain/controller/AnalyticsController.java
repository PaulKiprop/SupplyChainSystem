package com.paul.supplychain.controller;

import com.paul.supplychain.dto.*;
import com.paul.supplychain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final InventoryRepository inventoryRepo;

    // Total stock per product
    @GetMapping("/stock")
    public List<Map<String, Object>> getStockSummary() {
        return inventoryRepo.getStockSummary();
    }

    // Low stock products
    @GetMapping("/low-stock")
    public List<InventoryResponseDto> getLowStock() {
        return inventoryRepo.findByQuantityLessThan(50)
                .stream()
                .map(DtoMapper::toInventoryDto)
                .toList();
    }
}