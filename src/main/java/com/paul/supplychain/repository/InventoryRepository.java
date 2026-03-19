package com.paul.supplychain.repository;

import com.paul.supplychain.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("""
        SELECT new map(p.name as product, SUM(i.quantity) as totalstock)
         FROM Inventory i
            JOIN i.product p
            GROUP BY p.name
""")
    List<Map<String, Object>> getStockSummary();
    List<Inventory> findByQuantityLessThan(Integer quantity);
    void deleteByProduct_Id(Long productId);
    Optional<Inventory> findByWarehouse_IdAndProduct_Id(Long warehouseId, Long productId);
}
