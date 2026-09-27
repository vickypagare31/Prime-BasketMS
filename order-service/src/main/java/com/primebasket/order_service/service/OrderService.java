package com.primebasket.order_service.service;

import com.primebasket.order_service.dto.OrderRequestDto;
import com.primebasket.order_service.dto.OrderResponseDto;

public interface OrderService {

    OrderResponseDto createOrder(OrderRequestDto requestDto);
}
