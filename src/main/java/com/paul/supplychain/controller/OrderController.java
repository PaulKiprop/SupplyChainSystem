package com.paul.supplychain.controller;

import com.paul.supplychain.dto.CreateOrderRequestDto;
import com.paul.supplychain.dto.DtoMapper;
import com.paul.supplychain.dto.OrderResponseDto;
import com.paul.supplychain.dto.UpdateOrderStatusRequestDto;
import com.paul.supplychain.entity.SupplyOrder;
import com.paul.supplychain.service.impl.OrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody CreateOrderRequestDto dto) {
        SupplyOrder order = orderService.createOrder(dto);
        return ResponseEntity.ok(DtoMapper.toOrderDto(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        SupplyOrder order = orderService.getOrderById(id);
        return ResponseEntity.ok(DtoMapper.toOrderDto(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> dtos = orderService.getAllOrders()
                .stream()
                .map(DtoMapper::toOrderDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDto> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequestDto dto
    ) {
        SupplyOrder updated = orderService.updateOrderStatus(id, dto);
        return ResponseEntity.ok(DtoMapper.toOrderDto(updated));
    }
}
