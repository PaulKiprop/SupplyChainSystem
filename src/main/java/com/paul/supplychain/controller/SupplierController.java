package com.paul.supplychain.controller;

import com.paul.supplychain.dto.DtoMapper;
import com.paul.supplychain.dto.SupplierRequestDto;
import com.paul.supplychain.dto.SupplierResponseDto;
import com.paul.supplychain.service.impl.SupplierServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierServiceImpl service;

    public SupplierController(SupplierServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SupplierResponseDto> create(@Valid @RequestBody SupplierRequestDto dto) {
        return ResponseEntity.ok(DtoMapper.toSupplierDto(service.createSupplier(dto)));
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponseDto>> getAll() {
        List<SupplierResponseDto> dtos = service.getAllSuppliers()
                .stream()
                .map(DtoMapper::toSupplierDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{supplierId}/products/{productId}")
    public ResponseEntity<SupplierResponseDto> assignProduct(
            @PathVariable Long supplierId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(DtoMapper.toSupplierDto(service.assignProduct(supplierId, productId)));
    }
}
