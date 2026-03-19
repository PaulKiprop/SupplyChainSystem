package com.paul.supplychain.controller;

import com.paul.supplychain.dto.DtoMapper;
import com.paul.supplychain.dto.WarehouseRequestDto;
import com.paul.supplychain.dto.WarehouseResponseDto;
import com.paul.supplychain.entity.Warehouse;
import com.paul.supplychain.exception.NotFoundException;
import com.paul.supplychain.repository.WarehouseRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseRepository warehouseRepo;

    public WarehouseController(WarehouseRepository warehouseRepo) {
        this.warehouseRepo = warehouseRepo;
    }

    @PostMapping
    public ResponseEntity<WarehouseResponseDto> create(@Valid @RequestBody WarehouseRequestDto dto) {
        Warehouse warehouse = Warehouse.builder()
                .name(dto.name())
                .location(dto.location())
                .build();

        warehouse = warehouseRepo.save(warehouse);
        return ResponseEntity.ok(DtoMapper.toWarehouseDto(warehouse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> getById(@PathVariable Long id) {
        Warehouse warehouse = warehouseRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Warehouse not found"));

        return ResponseEntity.ok(DtoMapper.toWarehouseDto(warehouse));
    }

    @GetMapping
    public ResponseEntity<List<WarehouseResponseDto>> getAll() {
        List<WarehouseResponseDto> dtos = warehouseRepo.findAll()
                .stream()
                .map(DtoMapper::toWarehouseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
