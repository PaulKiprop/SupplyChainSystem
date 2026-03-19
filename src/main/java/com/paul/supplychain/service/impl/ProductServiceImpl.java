package com.paul.supplychain.service.impl;

import com.paul.supplychain.dto.ProductRequestDto;
import com.paul.supplychain.entity.Product;
import com.paul.supplychain.exception.ConflictException;
import com.paul.supplychain.exception.NotFoundException;
import com.paul.supplychain.repository.InventoryRepository;
import com.paul.supplychain.repository.ProductRepository;
import com.paul.supplychain.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final SupplierRepository supplierRepository;

    public ProductServiceImpl(
            ProductRepository productRepository,
            InventoryRepository inventoryRepository,
            SupplierRepository supplierRepository
    ) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.supplierRepository = supplierRepository;
    }

    public Product createProduct(ProductRequestDto dto) {
        productRepository.findBySku(dto.sku())
                .ifPresent(existing -> {
                    throw new ConflictException("Product with SKU '" + dto.sku() + "' already exists");
                });

        Product product = new Product();
        product.setName(dto.name());
        product.setSku(dto.sku());
        product.setDescription(dto.description());
        product.setPrice(dto.price());

        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Transactional
    public void deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        // Supplier is the owning side of product_suppliers; update it first.
        for (var supplier : new ArrayList<>(product.getSuppliers())) {
            supplier.getProducts().remove(product);
        }
        supplierRepository.flush();

        inventoryRepository.deleteByProduct_Id(productId);

        productRepository.delete(product);
    }
}
