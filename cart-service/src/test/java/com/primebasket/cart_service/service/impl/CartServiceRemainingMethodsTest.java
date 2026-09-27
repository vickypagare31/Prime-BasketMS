package com.primebasket.cart_service.service.impl;

import com.primebasket.cart_service.clients.ProductClient;
import com.primebasket.cart_service.clients.UserClient;
import com.primebasket.cart_service.dto.*;
import com.primebasket.cart_service.entity.Cart;
import com.primebasket.cart_service.entity.CartItem;
import com.primebasket.cart_service.exception.CartEmptyException;
import com.primebasket.cart_service.exception.ResourceNotFoundException;
import com.primebasket.cart_service.exception.ResourceNullException;
import com.primebasket.cart_service.mapper.CartMapper;
import com.primebasket.cart_service.repository.CartRepository;
import com.primebasket.cart_service.service.CartDependencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceRemainingMethodsTest {

    @Mock private CartRepository cartRepository;
    @Mock private UserClient userClient;
    @Mock private ProductClient productClient;
    @Mock private CartMapper cartMapper;
    @Mock private CartDependencyService cartDependencyService;

    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImpl(
                cartRepository, userClient, productClient, cartMapper, cartDependencyService);
    }

    @Test
    void getCartByUserId_returnsMappedCart() {
        Cart cart = cart(5L, 7L, item(10L, 2));
        when(cartDependencyService.getUser(7L)).thenReturn(activeUser(7L));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.of(cart));

        CartResponseDto response = cartService.getCartByUserId(7L);

        assertEquals(5L, response.getCartId());
        assertEquals(7L, response.getUserId());
        assertEquals(10L, response.getResponseDtoList().get(0).getProductId());
    }

    @Test
    void getCartByUserId_rejectsInactiveUser() {
        when(cartDependencyService.getUser(7L)).thenReturn(new UserStatusResponseDto(7L, false));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.getCartByUserId(7L));

        assertEquals("User not found with Id: 7", exception.getMessage());
        verify(cartRepository, never()).findByUserId(any());
    }

    @Test
    void getCartByUserId_rejectsMissingCart() {
        when(cartDependencyService.getUser(7L)).thenReturn(activeUser(7L));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.getCartByUserId(7L));

        assertEquals("Cart not found for this Id: 7", exception.getMessage());
    }

    @Test
    void updateCartQuantity_updatesAndSavesItem() {
        CartItem cartItem = item(10L, 2);
        Cart cart = cart(5L, 7L, cartItem);
        cartItem.setCart(cart);
        when(cartDependencyService.getUser(7L)).thenReturn(activeUser(7L));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.of(cart));
        when(cartDependencyService.getProduct(10L)).thenReturn(activeProduct(10L));
        when(cartRepository.save(cart)).thenReturn(cart);

        CartUpdateResponseDto response = cartService.updateCartQuantity(
                7L, 10L, new CartUpdateQuantityRequestDto(4));

        assertEquals(4, cartItem.getQuantity());
        assertEquals(4, response.getResponseDtoList().get(0).getQuantity());
        verify(cartRepository).save(cart);
    }

    @Test
    void updateCartQuantity_rejectsInvalidQuantityBeforeCallingDependencies() {
        ResourceNullException exception = assertThrows(ResourceNullException.class,
                () -> cartService.updateCartQuantity(7L, 10L, new CartUpdateQuantityRequestDto(0)));

        assertEquals("Quantity not be null or must be greater than 0", exception.getMessage());
        verifyNoInteractions(cartDependencyService, cartRepository);
    }

    @Test
    void updateCartQuantity_rejectsProductMissingFromCart() {
        Cart cart = cart(5L, 7L, item(20L, 1));
        when(cartDependencyService.getUser(7L)).thenReturn(activeUser(7L));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.of(cart));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.updateCartQuantity(7L, 10L, new CartUpdateQuantityRequestDto(4)));

        assertEquals("Product not found in cart.", exception.getMessage());
        verify(cartDependencyService, never()).getProduct(any());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void updateCartQuantity_rejectsInactiveProduct() {
        Cart cart = cart(5L, 7L, item(10L, 2));
        when(cartDependencyService.getUser(7L)).thenReturn(activeUser(7L));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.of(cart));
        when(cartDependencyService.getProduct(10L)).thenReturn(new ProductResponseDto());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.updateCartQuantity(7L, 10L, new CartUpdateQuantityRequestDto(4)));

        assertEquals("Product not found for this Id: 10", exception.getMessage());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void removeCartItem_removesAndSavesItem() {
        Cart cart = cart(5L, 7L, item(10L, 2));
        when(cartDependencyService.getUser(7L)).thenReturn(activeUser(7L));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.of(cart));
        when(cartDependencyService.getProduct(10L)).thenReturn(activeProduct(10L));

        cartService.removeCartItem(7L, 10L);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }

    @Test
    void removeCartItem_rejectsProductMissingFromCart() {
        Cart cart = cart(5L, 7L, item(20L, 1));
        when(cartDependencyService.getUser(7L)).thenReturn(activeUser(7L));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.of(cart));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.removeCartItem(7L, 10L));

        assertEquals("Product not found in cart.", exception.getMessage());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void clearCart_removesAllItemsAndSavesCart() {
        Cart cart = cart(5L, 7L, item(10L, 2), item(20L, 1));
        when(cartDependencyService.getUser(7L)).thenReturn(activeUser(7L));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.of(cart));

        cartService.clearCart(7L);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }

    @Test
    void clearCart_rejectsNullUserIdBeforeCallingDependencies() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.clearCart(null));

        assertEquals("User id must not be null.", exception.getMessage());
        verifyNoInteractions(cartDependencyService, cartRepository);
    }

    @Test
    void validateCart_returnsCartWhenEveryItemIsValid() {
        Cart cart = cart(5L, 7L, item(10L, 2), item(20L, 1));
        when(cartDependencyService.getUser(7L)).thenReturn(activeUser(7L));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.of(cart));
        when(cartDependencyService.getProduct(10L)).thenReturn(activeProduct(10L));
        when(cartDependencyService.getProduct(20L)).thenReturn(activeProduct(20L));

        CartResponseDto response = cartService.validateCart(7L);

        assertEquals(5L, response.getCartId());
        assertEquals(2, response.getResponseDtoList().size());
        verify(cartDependencyService).getProduct(10L);
        verify(cartDependencyService).getProduct(20L);
    }

    @Test
    void validateCart_rejectsEmptyCart() {
        Cart cart = cart(5L, 7L);
        when(cartDependencyService.getUser(7L)).thenReturn(activeUser(7L));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.of(cart));

        CartEmptyException exception = assertThrows(CartEmptyException.class,
                () -> cartService.validateCart(7L));

        assertEquals("Cart is empty.", exception.getMessage());
        verify(cartDependencyService, never()).getProduct(any());
    }

    @Test
    void validateCart_rejectsInvalidItemQuantity() {
        Cart cart = cart(5L, 7L, item(10L, 0));
        when(cartDependencyService.getUser(7L)).thenReturn(activeUser(7L));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.of(cart));

        ResourceNullException exception = assertThrows(ResourceNullException.class,
                () -> cartService.validateCart(7L));

        assertEquals("Invalid quantity for product: 10", exception.getMessage());
        verify(cartDependencyService, never()).getProduct(any());
    }

    @Test
    void validateCart_rejectsInactiveProduct() {
        Cart cart = cart(5L, 7L, item(10L, 1));
        when(cartDependencyService.getUser(7L)).thenReturn(activeUser(7L));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.of(cart));
        when(cartDependencyService.getProduct(10L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.validateCart(7L));

        assertEquals("Product not found for this Id: 10", exception.getMessage());
    }

    private ProductResponseDto activeProduct(Long productId) {
        ProductResponseDto product = new ProductResponseDto();
        product.setProductId(productId);
        product.setIsActive(true);
        return product;
    }

    private UserStatusResponseDto activeUser(Long userId) {
        return new UserStatusResponseDto(userId, true);
    }

    private Cart cart(Long cartId, Long userId, CartItem... items) {
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>(List.of(items)));
        return cart;
    }

    private CartItem item(Long productId, Integer quantity) {
        CartItem item = new CartItem();
        item.setProductId(productId);
        item.setQuantity(quantity);
        return item;
    }
}
