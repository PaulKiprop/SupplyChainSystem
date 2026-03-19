package com.paul.supplychain.dto;

import com.paul.supplychain.entity.Inventory;
import com.paul.supplychain.entity.OrderItem;
import com.paul.supplychain.entity.OrderStatusHistory;
import com.paul.supplychain.entity.Product;
import com.paul.supplychain.entity.SupplyOrder;
import com.paul.supplychain.entity.Supplier;
import com.paul.supplychain.entity.Warehouse;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

//    Product response
    public static ProductResponseDto toProductDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getDescription(),
                product.getPrice()
        );
    }

//    Supplier response
    public static SupplierResponseDto toSupplierDto(Supplier supplier) {
        List<ProductResponseDto> products = supplier.getProducts()
                .stream()
                .map(DtoMapper::toProductDto)
                .collect(Collectors.toList());

        return new SupplierResponseDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getEmail(),
                products
        );
    }
    // Warehouse
    public static WarehouseResponseDto toWarehouseDto(Warehouse warehouse) {
        return new WarehouseResponseDto(
                warehouse.getId(),
                warehouse.getName(),
                warehouse.getLocation()
        );
    }

    // Inventory
    public static InventoryResponseDto toInventoryDto(Inventory inventory) {
        return new InventoryResponseDto(
                inventory.getId(),
                inventory.getWarehouse().getId(),
                inventory.getWarehouse().getName(),
                inventory.getProduct().getId(),
                inventory.getProduct().getName(),
                inventory.getQuantity()
        );
    }

    public static OrderResponseDto toOrderDto(SupplyOrder order) {
        List<OrderItemResponseDto> items = order.getItems()
                .stream()
                .map(DtoMapper::toOrderItemDto)
                .collect(Collectors.toList());

        List<OrderStatusHistoryResponseDto> statusHistory = order.getStatusHistory()
                .stream()
                .map(DtoMapper::toOrderStatusHistoryDto)
                .collect(Collectors.toList());

        return new OrderResponseDto(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getWarehouse().getId(),
                order.getWarehouse().getName(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getExpectedDeliveryDate(),
                order.getDeliveredAt(),
                order.getNotes(),
                items,
                statusHistory
        );
    }

    public static OrderItemResponseDto toOrderItemDto(OrderItem item) {
        return new OrderItemResponseDto(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getSku(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getLineTotal()
        );
    }

    public static OrderStatusHistoryResponseDto toOrderStatusHistoryDto(OrderStatusHistory history) {
        return new OrderStatusHistoryResponseDto(
                history.getFromStatus(),
                history.getToStatus(),
                history.getChangedAt(),
                history.getComment()
        );
    }
}
