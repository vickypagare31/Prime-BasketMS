package com.primebasket.order_service.service.impl;

import com.primebasket.order_service.dto.OrderRequestDto;
import com.primebasket.order_service.dto.OrderResponseDto;
import com.primebasket.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Override
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {


        return null;
    }
}
