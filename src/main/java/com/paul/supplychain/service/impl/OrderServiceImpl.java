package com.paul.supplychain.service.impl;

import com.paul.supplychain.dto.CreateOrderItemRequestDto;
import com.paul.supplychain.dto.CreateOrderRequestDto;
import com.paul.supplychain.dto.UpdateOrderStatusRequestDto;
import com.paul.supplychain.entity.*;
import com.paul.supplychain.exception.BadRequestException;
import com.paul.supplychain.exception.NotFoundException;
import com.paul.supplychain.repository.InventoryRepository;
import com.paul.supplychain.repository.ProductRepository;
import com.paul.supplychain.repository.SupplyOrderRepository;
import com.paul.supplychain.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl {

    private final SupplyOrderRepository orderRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public OrderServiceImpl(
            SupplyOrderRepository orderRepository,
            WarehouseRepository warehouseRepository,
            ProductRepository productRepository,
            InventoryRepository inventoryRepository
    ) {
        this.orderRepository = orderRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public SupplyOrder createOrder(CreateOrderRequestDto dto) {
        if (dto.items() == null || dto.items().isEmpty()) {
            throw new BadRequestException("Order must contain at least one item");
        }

        Warehouse warehouse = warehouseRepository.findById(dto.warehouseId())
                .orElseThrow(() -> new NotFoundException("Warehouse not found"));

        validateNoDuplicateProducts(dto.items());

        SupplyOrder order = new SupplyOrder();
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PENDING);
        order.setWarehouse(warehouse);
        order.setExpectedDeliveryDate(dto.expectedDeliveryDate());
        order.setNotes(dto.notes());

        List<OrderItem> items = new ArrayList<>();
        for (CreateOrderItemRequestDto itemDto : dto.items()) {
            if (itemDto.quantity() == null || itemDto.quantity() <= 0) {
                throw new BadRequestException("Quantity must be positive");
            }

            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + itemDto.productId()));

            double unitPrice = product.getPrice() == null ? 0.0 : product.getPrice();
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDto.quantity());
            item.setUnitPrice(unitPrice);
            item.setLineTotal(unitPrice * itemDto.quantity());
            items.add(item);
        }
        order.setItems(items);

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setFromStatus(null);
        history.setToStatus(OrderStatus.PENDING);
        history.setChangedAt(LocalDateTime.now());
        history.setComment("Order created");
        order.setStatusHistory(new ArrayList<>(List.of(history)));

        return orderRepository.save(order);
    }

    public SupplyOrder getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public List<SupplyOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public SupplyOrder updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto dto) {
        if (dto.status() == null) {
            throw new BadRequestException("Status is required");
        }

        SupplyOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        OrderStatus currentStatus = order.getStatus();
        OrderStatus targetStatus = dto.status();

        if (currentStatus == targetStatus) {
            return order;
        }

        validateStatusTransition(currentStatus, targetStatus);

        if (targetStatus == OrderStatus.CONFIRMED) {
            reserveInventory(order);
        }

        if (targetStatus == OrderStatus.CANCELLED &&
                (currentStatus == OrderStatus.CONFIRMED || currentStatus == OrderStatus.PICKING)) {
            releaseInventory(order);
        }

        if (targetStatus == OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        order.setStatus(targetStatus);

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setFromStatus(currentStatus);
        history.setToStatus(targetStatus);
        history.setChangedAt(LocalDateTime.now());
        history.setComment(dto.comment());
        order.getStatusHistory().add(history);

        return orderRepository.save(order);
    }

    private void reserveInventory(SupplyOrder order) {
        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepository
                    .findByWarehouse_IdAndProduct_Id(order.getWarehouse().getId(), item.getProduct().getId())
                    .orElseThrow(() -> new BadRequestException(
                            "No inventory for product " + item.getProduct().getName() + " in warehouse " + order.getWarehouse().getName()
                    ));

            if (inventory.getQuantity() < item.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + item.getProduct().getName());
            }
        }

        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepository
                    .findByWarehouse_IdAndProduct_Id(order.getWarehouse().getId(), item.getProduct().getId())
                    .orElseThrow(() -> new NotFoundException("Inventory not found"));
            inventory.setQuantity(inventory.getQuantity() - item.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    private void releaseInventory(SupplyOrder order) {
        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepository
                    .findByWarehouse_IdAndProduct_Id(order.getWarehouse().getId(), item.getProduct().getId())
                    .orElseThrow(() -> new NotFoundException("Inventory not found"));
            inventory.setQuantity(inventory.getQuantity() + item.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    private void validateNoDuplicateProducts(List<CreateOrderItemRequestDto> items) {
        Set<Long> productIds = new HashSet<>();
        for (CreateOrderItemRequestDto item : items) {
            if (!productIds.add(item.productId())) {
                throw new BadRequestException("Duplicate product in order: " + item.productId());
            }
        }
    }

    private void validateStatusTransition(OrderStatus from, OrderStatus to) {
        boolean valid = switch (from) {
            case PENDING -> to == OrderStatus.CONFIRMED || to == OrderStatus.CANCELLED;
            case CONFIRMED -> to == OrderStatus.PICKING || to == OrderStatus.CANCELLED;
            case PICKING -> to == OrderStatus.SHIPPED || to == OrderStatus.CANCELLED;
            case SHIPPED -> to == OrderStatus.DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };

        if (!valid) {
            throw new BadRequestException("Invalid status transition: " + from + " -> " + to);
        }
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
    }
}
