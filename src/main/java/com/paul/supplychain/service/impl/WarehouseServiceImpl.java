package com.paul.supplychain.service.impl;

import com.paul.supplychain.dto.WarehouseRequestDto;
import com.paul.supplychain.dto.WarehouseUpdateRequestDto;
import com.paul.supplychain.entity.Warehouse;
import com.paul.supplychain.exception.NotFoundException;
import com.paul.supplychain.repository.WarehouseRepository;
import com.paul.supplychain.util.PageableSanitizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl {

    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse createWarehouse(WarehouseRequestDto dto) {
        Warehouse warehouse = Warehouse.builder()
                .name(dto.name())
                .location(dto.location())
                .build();
        return warehouseRepository.save(warehouse);
    }

    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Warehouse not found"));
    }

    public Page<Warehouse> getAllWarehouses(Pageable pageable) {
        return warehouseRepository.findAll(PageableSanitizer.sanitize(pageable));
    }

    public Warehouse updateWarehouse(Long id, WarehouseUpdateRequestDto dto) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Warehouse not found"));

        warehouse.setName(dto.name());
        warehouse.setLocation(dto.location());

        return warehouseRepository.save(warehouse);
    }
}
