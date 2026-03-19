package com.paul.supplychain.service.impl;

import com.paul.supplychain.dto.SupplierRequestDto;
import com.paul.supplychain.entity.Product;
import com.paul.supplychain.entity.Supplier;
import com.paul.supplychain.exception.NotFoundException;
import com.paul.supplychain.repository.ProductRepository;
import com.paul.supplychain.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl {

    private final SupplierRepository supplierRepo;
    private final ProductRepository productRepo;

    public SupplierServiceImpl(SupplierRepository supplierRepo, ProductRepository productRepo) {
        this.supplierRepo = supplierRepo;
        this.productRepo = productRepo;
    }

    public Supplier createSupplier(SupplierRequestDto dto) {
        Supplier supplier = new Supplier();
        supplier.setName(dto.name());
        supplier.setEmail(dto.email());
        return supplierRepo.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepo.findAll();
    }

    public Supplier assignProduct(Long supplierId, Long productId) {

        Supplier supplier = supplierRepo.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        supplier.getProducts().add(product);

        return supplierRepo.save(supplier);
    }
}
