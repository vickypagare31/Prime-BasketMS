package com.primebasket.order_service.service.impl;

import com.primebasket.order_service.dto.*;
import com.primebasket.order_service.entity.Order;
import com.primebasket.order_service.entity.OrderItem;
import com.primebasket.order_service.enums.OrderStatus;
import com.primebasket.order_service.exception.ResourceNotFoundException;
import com.primebasket.order_service.exception.ResourceNullException;
import com.primebasket.order_service.mapper.OrderMapper;
import com.primebasket.order_service.service.OrderDependencyService;
import com.primebasket.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

        BigDecimal totalAmount=BigDecimal.ZERO;

        for(CartItemResponseDto cartItem: cart.getCartItems()){

            ProductResponseDto product=orderDependencyService.getProductById(cartItem.getProductId());
            if(!Boolean.TRUE.equals(product.getIsActive())){
                throw new ResourceNotFoundException("Product is not available."
                        +cartItem.getProductId());
            }

            OrderItem orderItem= new OrderItem();

            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());

            //current price from product service
            orderItem.setPrice(product.getPrice());

            orderItem.setOrder(order);
            orderItemsList.add(orderItem);

            //Calculate Items total
            //price * quantity
            BigDecimal itemsTotal=
                    product.getPrice().multiply(
                            BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount=totalAmount.add(itemsTotal);

        }
        order.setOrderItems(orderItemsList);

        List<OrderItem>successfullyReserveItems=new ArrayList<>();

        try {
            //Need to reserve products quantities in inventory
            for(OrderItem item: orderItemsList){

                orderDependencyService.reserveStock(item.getProductId(),
                        new StockQuantityRequestDto(item.getQuantity()));

                successfullyReserveItems.add(item);
            }
        }
        catch (Exception ex){
            for (OrderItem reservedItem: successfullyReserveItems){
                orderDependencyService.releaseReservedStock(reservedItem.getProductId(),
                        new StockQuantityRequestDto(reservedItem.getQuantity()));
            }
            throw  ex;
        }






        return null;
    }
}
