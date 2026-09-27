package com.primebasket.cart_service.controller;

import com.primebasket.cart_service.dto.CartRequestDto;
import com.primebasket.cart_service.dto.CartResponseDto;
import com.primebasket.cart_service.dto.CartUpdateQuantityRequestDto;
import com.primebasket.cart_service.dto.CartUpdateResponseDto;
import com.primebasket.cart_service.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock private CartService cartService;
    private CartController cartController;

    @BeforeEach
    void setUp() {
        cartController = new CartController(cartService);
    }

    @Test
    void addToCart_returnsCreatedResponse() {
        CartRequestDto request = new CartRequestDto();
        CartResponseDto serviceResponse = new CartResponseDto();
        when(cartService.addToCart(request)).thenReturn(serviceResponse);

        ResponseEntity<CartResponseDto> response = cartController.addToCart(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(serviceResponse, response.getBody());
    }

    @Test
    void getCartByUserId_returnsOkResponse() {
        CartResponseDto serviceResponse = new CartResponseDto();
        when(cartService.getCartByUserId(7L)).thenReturn(serviceResponse);

        ResponseEntity<CartResponseDto> response = cartController.getCartByUserId(7L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(serviceResponse, response.getBody());
    }

    @Test
    void updateCartQuantity_returnsOkResponse() {
        CartUpdateQuantityRequestDto request = new CartUpdateQuantityRequestDto(4);
        CartUpdateResponseDto serviceResponse = new CartUpdateResponseDto();
        when(cartService.updateCartQuantity(7L, 10L, request)).thenReturn(serviceResponse);

        ResponseEntity<CartUpdateResponseDto> response =
                cartController.updateCartQuantity(7L, 10L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(serviceResponse, response.getBody());
    }

    @Test
    void removeCartItem_returnsNoContent() {
        ResponseEntity<Void> response = cartController.removeCartItem(7L, 10L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(cartService).removeCartItem(7L, 10L);
    }

    @Test
    void clearCart_returnsNoContent() {
        ResponseEntity<Void> response = cartController.clearCart(7L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(cartService).clearCart(7L);
    }

    @Test
    void validateCart_returnsOkResponse() {
        CartResponseDto serviceResponse = new CartResponseDto();
        when(cartService.validateCart(7L)).thenReturn(serviceResponse);

        ResponseEntity<CartResponseDto> response = cartController.validateCart(7L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(serviceResponse, response.getBody());
    }
}
