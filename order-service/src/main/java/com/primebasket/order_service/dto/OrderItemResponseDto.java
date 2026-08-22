package com.primebasket.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemResponseDto {

    private Long orderItemId;

    private Long productId;

    private Integer quantity;

    private BigDecimal price;
}
