package com.paul.supplychain.service.impl;

import com.paul.supplychain.entity.Inventory;
import com.paul.supplychain.entity.Product;
import com.paul.supplychain.entity.Warehouse;
import com.paul.supplychain.exception.BadRequestException;
import com.paul.supplychain.exception.NotFoundException;
import com.paul.supplychain.repository.InventoryRepository;
import com.paul.supplychain.repository.ProductRepository;
import com.paul.supplychain.repository.WarehouseRepository;
import com.paul.supplychain.util.PageableSanitizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl {

    private final InventoryRepository inventoryRepo;
    private final WarehouseRepository warehouseRepo;
    private final ProductRepository productRepo;

    public InventoryServiceImpl(InventoryRepository inventoryRepo,
                                WarehouseRepository warehouseRepo,
                                ProductRepository productRepo) {
        this.inventoryRepo = inventoryRepo;
        this.warehouseRepo = warehouseRepo;
        this.productRepo = productRepo;
    }

    public Inventory addInventory(Long warehouseId, Long productId, Integer quantity) {
        Warehouse warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new NotFoundException("Warehouse not found"));
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Inventory inventory = Inventory.builder()
                .warehouse(warehouse)
                .product(product)
                .quantity(quantity)
                .build();

        return inventoryRepo.save(inventory);
    }

    public Page<Inventory> getInventoryByWarehouse(Long warehouseId, Pageable pageable) {
        warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new NotFoundException("Warehouse not found"));
        return inventoryRepo.findByWarehouse_Id(warehouseId, PageableSanitizer.sanitize(pageable));
    }

    /**
     * Sets the absolute quantity for an inventory record.
     * Quantity must be zero or greater.
     */
    public Inventory adjustQuantity(Long inventoryId, Integer newQuantity) {
        if (newQuantity < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }

        Inventory inventory = inventoryRepo.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory record not found"));

        inventory.setQuantity(newQuantity);
        return inventoryRepo.save(inventory);
    }
}
