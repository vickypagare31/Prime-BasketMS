package com.primebasket.order_service.mapper;

import com.primebasket.order_service.dto.OrderItemRequestDto;
import com.primebasket.order_service.dto.OrderItemResponseDto;
import com.primebasket.order_service.dto.OrderRequestDto;
import com.primebasket.order_service.dto.OrderResponseDto;
import com.primebasket.order_service.entity.Order;
import com.primebasket.order_service.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    public static OrderResponseDto entToDto(Order order){

        OrderResponseDto responseDto=new OrderResponseDto();

        responseDto.setOrderId(order.getOrderId());
        responseDto.setUserId(order.getUserId());
        responseDto.setOrderStatus(order.getOrderStatus());
        responseDto.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponseDto> itemResponseDtos = new ArrayList<>();
        for(OrderItem items: order.getOrderItems()){

            OrderItemResponseDto itemResponseDto = new OrderItemResponseDto();
            itemResponseDto.setOrderItemId(items.getOrderItemId());
            itemResponseDto.setProductId(items.getProductId());
            itemResponseDto.setPrice(items.getPrice());
            itemResponseDto.setQuantity(items.getQuantity());
            itemResponseDtos.add(itemResponseDto);

        }

        responseDto.setItems(itemResponseDtos);
        responseDto.setCreatedAt(order.getCreatedAt());
        responseDto.setUpdatedAt(order.getUpdatedAt());
        return  responseDto;
    }

    public static Order dtoToEnt(OrderRequestDto requestDto){
        Order order= new Order();
        order.setUserId(requestDto.getUserId());

        List<OrderItem>itemsList=new ArrayList<>();
        for(OrderItemRequestDto itemRequestDto: requestDto.getItems()){
            OrderItem items=new OrderItem();

            items.setOrder(order);
            items.setProductId(itemRequestDto.getProductId());
            items.setQuantity(itemRequestDto.getQuantity());
            itemsList.add(items);
        }

        order.setOrderItems(itemsList);

        return order;

    }
}
