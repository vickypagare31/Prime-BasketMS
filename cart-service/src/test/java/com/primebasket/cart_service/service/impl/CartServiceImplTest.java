package com.primebasket.cart_service.service.impl;

import com.primebasket.cart_service.clients.ProductClient;
import com.primebasket.cart_service.clients.UserClient;
import com.primebasket.cart_service.dto.CartItemRequestDto;
import com.primebasket.cart_service.dto.CartRequestDto;
import com.primebasket.cart_service.dto.CartResponseDto;
import com.primebasket.cart_service.dto.ProductResponseDto;
import com.primebasket.cart_service.dto.UserStatusResponseDto;
import com.primebasket.cart_service.entity.Cart;
import com.primebasket.cart_service.entity.CartItem;
import com.primebasket.cart_service.exception.ResourceNotFoundException;
import com.primebasket.cart_service.exception.ResourceNullException;
import com.primebasket.cart_service.mapper.CartMapper;
import com.primebasket.cart_service.repository.CartRepository;
import com.primebasket.cart_service.service.CartDependencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserClient userClient;
    @Mock
    private ProductClient productClient;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private CartDependencyService cartDependencyService;
    @Captor
    private ArgumentCaptor<Cart> cartCaptor;

    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImpl(
                cartRepository, userClient, productClient, cartMapper, cartDependencyService);
    }

    @Test
    void addToCart_createsCartWithRequestedItems() {
        CartRequestDto request = new CartRequestDto(7L, List.of(
                new CartItemRequestDto(10L, 2),
                new CartItemRequestDto(20L, 1)));
        when(cartDependencyService.getUser(7L)).thenReturn(new UserStatusResponseDto(7L, true));
        when(cartDependencyService.getActiveProducts(Set.of(10L, 20L)))
                .thenReturn(List.of(product(10L), product(20L)));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart cart = invocation.getArgument(0);
            cart.setCartId(99L);
            return cart;
        });

        CartResponseDto response = cartService.addToCart(request);

        verify(cartRepository).save(cartCaptor.capture());
        Cart savedCart = cartCaptor.getValue();
        assertEquals(7L, savedCart.getUserId());
        assertEquals(2, savedCart.getItems().size());
        assertEquals(List.of(10L, 20L), savedCart.getItems().stream().map(CartItem::getProductId).toList());
        assertEquals(List.of(2, 1), savedCart.getItems().stream().map(CartItem::getQuantity).toList());
        assertTrue(savedCart.getItems().stream().allMatch(item -> item.getCart() == savedCart));
        assertEquals(99L, response.getCartId());
        assertEquals(7L, response.getUserId());
    }

    @Test
    void addToCart_incrementsQuantityForExistingProduct() {
        Cart existingCart = new Cart();
        existingCart.setCartId(5L);
        existingCart.setUserId(7L);
        CartItem existingItem = new CartItem();
        existingItem.setProductId(10L);
        existingItem.setQuantity(3);
        existingItem.setCart(existingCart);
        existingCart.setItems(new ArrayList<>(List.of(existingItem)));

        when(cartDependencyService.getUser(7L)).thenReturn(new UserStatusResponseDto(7L, true));
        when(cartDependencyService.getActiveProducts(Set.of(10L))).thenReturn(List.of(product(10L)));
        when(cartRepository.findByUserId(7L)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(existingCart)).thenReturn(existingCart);

        cartService.addToCart(new CartRequestDto(7L, List.of(new CartItemRequestDto(10L, 2))));

        verify(cartRepository).save(existingCart);
        assertEquals(1, existingCart.getItems().size());
        assertEquals(5, existingItem.getQuantity());
    }

    @Test
    void addToCart_rejectsMissingUserIdBeforeCallingDependencies() {
        ResourceNullException exception = assertThrows(ResourceNullException.class,
                () -> cartService.addToCart(new CartRequestDto(null, List.of(new CartItemRequestDto(10L, 1)))));

        assertEquals("User Id must not be null", exception.getMessage());
        verify(cartDependencyService, never()).getUser(any());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void addToCart_rejectsInactiveUser() {
        when(cartDependencyService.getUser(7L)).thenReturn(new UserStatusResponseDto(7L, false));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addToCart(new CartRequestDto(7L, List.of(new CartItemRequestDto(10L, 1)))));

        assertEquals("User not found with Id: 7", exception.getMessage());
        verify(cartDependencyService, never()).getActiveProducts(any());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void addToCart_rejectsMissingOrInactiveProducts() {
        when(cartDependencyService.getUser(7L)).thenReturn(new UserStatusResponseDto(7L, true));
        when(cartDependencyService.getActiveProducts(eq(Set.of(10L, 20L))))
                .thenReturn(List.of(product(10L)));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> cartService.addToCart(new CartRequestDto(7L, List.of(
                        new CartItemRequestDto(10L, 1), new CartItemRequestDto(20L, 1)))));

        assertEquals("Products not found or inactive[20]", exception.getMessage());
        verify(cartRepository, never()).save(any());
    }

    private ProductResponseDto product(Long productId) {
        ProductResponseDto product = new ProductResponseDto();
        product.setProductId(productId);
        return product;
    }
}
