package com.paul.supplychain.controller;

import com.paul.supplychain.dto.*;
import com.paul.supplychain.service.impl.SupplierServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<PageResponseDto<SupplierResponseDto>> getAll(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(
                PageResponseDto.from(service.getAllSuppliers(pageable).map(DtoMapper::toSupplierDto))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody SupplierUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(DtoMapper.toSupplierDto(service.updateSupplier(id, dto)));
    }

    @PostMapping("/{supplierId}/products/{productId}")
    public ResponseEntity<SupplierResponseDto> assignProduct(
            @PathVariable Long supplierId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(DtoMapper.toSupplierDto(service.assignProduct(supplierId, productId)));
    }
}
