package com.paul.supplychain;

import com.paul.supplychain.dto.CreateOrderItemRequestDto;
import com.paul.supplychain.dto.CreateOrderRequestDto;
import com.paul.supplychain.dto.UpdateOrderStatusRequestDto;
import com.paul.supplychain.entity.*;
import com.paul.supplychain.exception.BadRequestException;
import com.paul.supplychain.repository.InventoryRepository;
import com.paul.supplychain.repository.ProductRepository;
import com.paul.supplychain.repository.SupplyOrderRepository;
import com.paul.supplychain.repository.WarehouseRepository;
import com.paul.supplychain.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceIntegrationTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private SupplyOrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    private Warehouse warehouse;
    private Product product;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        inventoryRepository.deleteAll();
        productRepository.deleteAll();
        warehouseRepository.deleteAll();

        warehouse = Warehouse.builder()
                .name("Test Warehouse")
                .location("Berlin")
                .build();
        warehouse = warehouseRepository.save(warehouse);

        product = Product.builder()
                .name("Test Product")
                .sku("TEST-SKU-1")
                .description("Test Product Description")
                .price(100.0)
                .build();
        product = productRepository.save(product);

        inventory = Inventory.builder()
                .warehouse(warehouse)
                .product(product)
                .quantity(20)
                .build();
        inventory = inventoryRepository.save(inventory);
    }

    @Test
    void createConfirmCancelOrderShouldReserveAndReleaseInventory() {
        CreateOrderRequestDto createDto = new CreateOrderRequestDto(
                warehouse.getId(),
                null,
                "Integration test order",
                List.of(new CreateOrderItemRequestDto(product.getId(), 5))
        );

        SupplyOrder createdOrder = orderService.createOrder(createDto);

        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        assertEquals(1, createdOrder.getItems().size());

        Inventory inventoryAfterCreate = inventoryRepository
                .findByWarehouse_IdAndProduct_Id(warehouse.getId(), product.getId())
                .orElseThrow();
        assertEquals(20, inventoryAfterCreate.getQuantity());

        SupplyOrder confirmedOrder = orderService.updateOrderStatus(
                createdOrder.getId(),
                new UpdateOrderStatusRequestDto(OrderStatus.CONFIRMED, "Reserve stock")
        );

        assertEquals(OrderStatus.CONFIRMED, confirmedOrder.getStatus());

        Inventory inventoryAfterConfirm = inventoryRepository
                .findByWarehouse_IdAndProduct_Id(warehouse.getId(), product.getId())
                .orElseThrow();
        assertEquals(15, inventoryAfterConfirm.getQuantity());

        SupplyOrder cancelledOrder = orderService.updateOrderStatus(
                createdOrder.getId(),
                new UpdateOrderStatusRequestDto(OrderStatus.CANCELLED, "Cancel order")
        );

        assertEquals(OrderStatus.CANCELLED, cancelledOrder.getStatus());

        Inventory inventoryAfterCancel = inventoryRepository
                .findByWarehouse_IdAndProduct_Id(warehouse.getId(), product.getId())
                .orElseThrow();
        assertEquals(20, inventoryAfterCancel.getQuantity());
    }

    @Test
    void confirmOrderShouldFailWhenInventoryIsInsufficient() {
        CreateOrderRequestDto createDto = new CreateOrderRequestDto(
                warehouse.getId(),
                null,
                "Insufficient stock order",
                List.of(new CreateOrderItemRequestDto(product.getId(), 25))
        );

        SupplyOrder createdOrder = orderService.createOrder(createDto);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> orderService.updateOrderStatus(
                        createdOrder.getId(),
                        new UpdateOrderStatusRequestDto(OrderStatus.CONFIRMED, "Reserve stock")
                )
        );

        assertTrue(exception.getMessage().contains("Insufficient stock"));

        Inventory inventoryAfterFailedConfirm = inventoryRepository
                .findByWarehouse_IdAndProduct_Id(warehouse.getId(), product.getId())
                .orElseThrow();
        assertEquals(20, inventoryAfterFailedConfirm.getQuantity());

        SupplyOrder reloadedOrder = orderRepository.findById(createdOrder.getId()).orElseThrow();
        assertEquals(OrderStatus.PENDING, reloadedOrder.getStatus());
    }
}
