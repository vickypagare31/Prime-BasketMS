package com.primebasket.cart_service.mapper;

import com.primebasket.cart_service.dto.*;
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

                itemResponseDtoList.add(itemResponseDto);
            }
        }
        responseDto.setResponseDtoList(itemResponseDtoList);
        responseDto.setCreatedAt(cart.getCreatedAt());
        //responseDto.setUpdatedAt(cart.getUpdatedAt());
        return responseDto;

    }

    public static CartUpdateResponseDto entToCartUpdateResponseDto(Cart cart){

        CartUpdateResponseDto responseDto=new CartUpdateResponseDto();

        responseDto.setCartId(cart.getCartId());
        responseDto.setUserId(cart.getUserId());

        List<CartItemResponseDto>itemResponseDtoList=new ArrayList<>();
        if(cart.getItems()!=null){
            for(CartItem items: cart.getItems()){

                CartItemResponseDto itemResponseDto= new CartItemResponseDto();

                itemResponseDto.setCartItemId(items.getCartItemId());
                itemResponseDto.setProductId(items.getProductId());
                itemResponseDto.setQuantity(items.getQuantity());
                itemResponseDtoList.add(itemResponseDto);
            }
        }
        responseDto.setResponseDtoList(itemResponseDtoList);
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
