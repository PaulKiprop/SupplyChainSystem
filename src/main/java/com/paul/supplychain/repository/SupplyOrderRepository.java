package com.paul.supplychain.repository;

import com.paul.supplychain.entity.SupplyOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplyOrderRepository extends JpaRepository<SupplyOrder, Long> {
    Optional<SupplyOrder> findByOrderNumber(String orderNumber);
}
