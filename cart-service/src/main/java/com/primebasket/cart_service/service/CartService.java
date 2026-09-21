package com.primebasket.cart_service.service;

import com.primebasket.cart_service.dto.CartRequestDto;
import com.primebasket.cart_service.dto.CartResponseDto;
import com.primebasket.cart_service.dto.CartUpdateQuantityRequestDto;
import com.primebasket.cart_service.dto.CartUpdateResponseDto;
import org.springframework.stereotype.Service;


public interface CartService {

    CartResponseDto addToCart(CartRequestDto requestDto);

    CartResponseDto getCartByUserId(Long userId);

    CartUpdateResponseDto updateCartQuantity(Long userId, Long productId, CartUpdateQuantityRequestDto requestDto);

    void removeCartItem(Long userId, Long productId);

    void clearCart(Long userId);

    CartResponseDto validateCart(Long userId);

}