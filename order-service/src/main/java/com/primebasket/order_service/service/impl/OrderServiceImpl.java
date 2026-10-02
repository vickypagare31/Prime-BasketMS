package com.primebasket.order_service.service.impl;

import com.primebasket.order_service.dto.*;
import com.primebasket.order_service.entity.Order;
import com.primebasket.order_service.entity.OrderItem;
import com.primebasket.order_service.enums.OrderStatus;
import com.primebasket.order_service.exception.ResourceNullException;
import com.primebasket.order_service.mapper.OrderMapper;
import com.primebasket.order_service.service.OrderDependencyService;
import com.primebasket.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderDependencyService orderDependencyService;
    private final OrderMapper orderMapper;
    @Override
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {

        if(requestDto==null || requestDto.getUserId()==null){

            throw new ResourceNullException("User Id must not be null");

        }

        CartResponseDto cart=orderDependencyService.getCartByUserId(requestDto.getUserId());

        if(cart.getCartItems()==null || cart.getCartItems().isEmpty()){
            throw new ResourceNullException("Cart is empty.");
        }

        Order order=new Order();

        order.setUserId(requestDto.getUserId());
        order.setOrderStatus(OrderStatus.PENDING);

        List<OrderItem> orderItemsList=new ArrayList<>();

        for(CartItemResponseDto cartItem: cart.getCartItems()){

            OrderItem orderItem= new OrderItem();

            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItemsList.add(orderItem);
        }
        order.setOrderItems(orderItemsList);




        return null;
    }
}
