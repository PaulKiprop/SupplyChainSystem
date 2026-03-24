package com.paul.supplychain.controller;

import com.paul.supplychain.dto.*;
import com.paul.supplychain.service.impl.WarehouseServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseServiceImpl warehouseService;

    public WarehouseController(WarehouseServiceImpl warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    public ResponseEntity<WarehouseResponseDto> create(@Valid @RequestBody WarehouseRequestDto dto) {
        return ResponseEntity.ok(DtoMapper.toWarehouseDto(warehouseService.createWarehouse(dto)));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<WarehouseResponseDto>> getAll(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(
                PageResponseDto.from(warehouseService.getAllWarehouses(pageable).map(DtoMapper::toWarehouseDto))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(DtoMapper.toWarehouseDto(warehouseService.getWarehouseById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(DtoMapper.toWarehouseDto(warehouseService.updateWarehouse(id, dto)));
    }
}