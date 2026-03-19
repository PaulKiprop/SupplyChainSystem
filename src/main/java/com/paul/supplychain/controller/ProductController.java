package com.paul.supplychain.controller;

import com.paul.supplychain.dto.DtoMapper;
import com.paul.supplychain.dto.ProductRequestDto;
import com.paul.supplychain.dto.ProductResponseDto;
import com.paul.supplychain.service.impl.ProductServiceImpl;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<ProductResponseDto>> getAll() {
        List<ProductResponseDto> dtos = service.getAllProducts()
                .stream()
                .map(DtoMapper::toProductDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(DtoMapper.toProductDto(service.getProductById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.ok("Product Deleted successfully");
    }
}
