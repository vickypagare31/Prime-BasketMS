package com.primebasket.order_service.dto;

import com.primebasket.order_service.entity.OrderItem;
import com.primebasket.order_service.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderResponseDto {

    private Long orderId;

    private Long userId;

    private OrderStatus orderStatus;

    private BigDecimal totalAmount;

    private List<OrderItemResponseDto>items;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
