package com.primebasket.cart_service.mapper;

import com.primebasket.cart_service.dto.CartItemResponseDto;
import com.primebasket.cart_service.dto.CartItemRequestDto;
import com.primebasket.cart_service.dto.CartRequestDto;
import com.primebasket.cart_service.dto.CartResponseDto;
import com.primebasket.cart_service.entity.Cart;
import com.primebasket.cart_service.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartMapper {

    public static CartResponseDto entToDto(Cart cart){
        CartResponseDto responseDto= new CartResponseDto();

        responseDto.setCartId(cart.getCartId());
        responseDto.setUserId(cart.getUserId());

        List<CartItemResponseDto> itemResponseDtoList=new ArrayList<>();
        if (cart.getItems() != null) {
            for(CartItem cartItem: cart.getItems()){
                CartItemResponseDto itemResponseDto= new CartItemResponseDto();

                itemResponseDto.setCartItemId(cartItem.getCartItemId());
                itemResponseDto.setProductId(cartItem.getProductId());
                itemResponseDto.setQuantity(cartItem.getQuantity());
                itemResponseDto.setCreatedAt(cartItem.getCreatedAt());
                itemResponseDto.setUpdateAt(cartItem.getUpdatedAt());
                itemResponseDtoList.add(itemResponseDto);
            }
        }
        responseDto.setResponseDtoList(itemResponseDtoList);
        responseDto.setCreatedAt(cart.getCreatedAt());
        responseDto.setUpdatedAt(cart.getUpdatedAt());
        return responseDto;

    }

    public static Cart dtoToEnt(CartRequestDto requestDto){
        Cart cart= new Cart();
        cart.setUserId(requestDto.getUserId());

        List<CartItem> cartItems = new ArrayList<>();
        if (requestDto.getItems() != null) {
            for (CartItemRequestDto itemRequestDto : requestDto.getItems()) {
                CartItem cartItem = new CartItem();

                cartItem.setProductId(itemRequestDto.getProductId());
                cartItem.setQuantity(itemRequestDto.getQuantity());
                cartItem.setCart(cart);
                cartItems.add(cartItem);
            }
        }

        cart.setItems(cartItems);
        return cart;
    }

}
