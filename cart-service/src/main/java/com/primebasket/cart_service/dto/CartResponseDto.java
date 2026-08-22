package com.primebasket.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartResponseDto {

    private Long cartId;

    private Long userId;

    private List<CartItemResponseDto> responseDtoList;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
