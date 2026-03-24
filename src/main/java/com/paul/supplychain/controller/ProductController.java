package com.paul.supplychain.controller;

import com.paul.supplychain.dto.*;
import com.paul.supplychain.service.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductServiceImpl service;

    public ProductController(ProductServiceImpl service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(DtoMapper.toProductDto(service.createProduct(dto)));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ProductResponseDto>> getAll(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(
                PageResponseDto.from(service.getAllProducts(pageable).map(DtoMapper::toProductDto))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(DtoMapper.toProductDto(service.getProductById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(DtoMapper.toProductDto(service.updateProduct(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.ok("Product Deleted successfully");
    }
}
