package com.primebasket.order_service.service.impl;

import com.primebasket.order_service.dto.CartItemResponseDto;
import com.primebasket.order_service.dto.CartResponseDto;
import com.primebasket.order_service.dto.OrderRequestDto;
import com.primebasket.order_service.dto.OrderResponseDto;
import com.primebasket.order_service.dto.ProductResponseDto;
import com.primebasket.order_service.dto.StockQuantityRequestDto;
import com.primebasket.order_service.entity.Order;
import com.primebasket.order_service.enums.OrderStatus;
import com.primebasket.order_service.enums.ProductStatus;
import com.primebasket.order_service.exception.ResourceNotFoundException;
import com.primebasket.order_service.exception.ResourceNullException;
import com.primebasket.order_service.mapper.OrderMapper;
import com.primebasket.order_service.repository.OrderRepository;
import com.primebasket.order_service.service.OrderDependencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderDependencyService orderDependencyService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderRepository orderRepository;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderDependencyService, orderMapper, orderRepository);
    }

    @Test
    void createOrder_whenRequestIsNull_throwsResourceNullException() {
        assertThatThrownBy(() -> orderService.createOrder(null))
                .isInstanceOf(ResourceNullException.class)
                .hasMessage("User Id must not be null");

        verifyNoInteractions(orderDependencyService, orderRepository);
    }

    @Test
    void createOrder_whenUserIdIsNull_throwsResourceNullException() {
        OrderRequestDto request = new OrderRequestDto();

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(ResourceNullException.class)
                .hasMessage("User Id must not be null");

        verifyNoInteractions(orderDependencyService, orderRepository);
    }

    @Test
    void createOrder_whenCartIsNull_throwsResourceNullException() {
        OrderRequestDto request = requestForUser(10L);
        when(orderDependencyService.getCartByUserId(10L)).thenReturn(null);

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(ResourceNullException.class)
                .hasMessage("Cart is empty.");

        verify(orderDependencyService).getCartByUserId(10L);
        verify(orderRepository, never()).saveAndFlush(any(Order.class));
    }

    @Test
    void createOrder_whenCartItemsAreNullOrEmpty_throwsResourceNullException() {
        OrderRequestDto request = requestForUser(10L);
        CartResponseDto cartWithNullItems = cartForUser(10L, null);
        CartResponseDto emptyCart = cartForUser(10L, Collections.emptyList());
        when(orderDependencyService.getCartByUserId(10L))
                .thenReturn(cartWithNullItems, emptyCart);

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(ResourceNullException.class)
                .hasMessage("Cart is empty.");
        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(ResourceNullException.class)
                .hasMessage("Cart is empty.");

        verify(orderRepository, never()).saveAndFlush(any(Order.class));
    }

    @Test
    void createOrder_whenProductIsInactive_throwsResourceNotFoundAndDoesNotReserveOrSave() {
        OrderRequestDto request = requestForUser(10L);
        when(orderDependencyService.getCartByUserId(10L))
                .thenReturn(cartForUser(10L, List.of(cartItem(1L, 101L, 2))));
        when(orderDependencyService.getProductById(101L))
                .thenReturn(product(101L, new BigDecimal("25.00"), false));

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product is not available.101");

        verify(orderDependencyService, never())
                .reserveStock(any(Long.class), any(StockQuantityRequestDto.class));
        verify(orderRepository, never()).saveAndFlush(any(Order.class));
    }

    @Test
    void createOrder_whenCartIsValid_reservesStockSavesOrderAndReturnsCalculatedResponse() {
        OrderRequestDto request = requestForUser(10L);
        when(orderDependencyService.getCartByUserId(10L)).thenReturn(cartForUser(10L, List.of(
                cartItem(1L, 101L, 2),
                cartItem(2L, 202L, 3))));
        when(orderDependencyService.getProductById(101L))
                .thenReturn(product(101L, new BigDecimal("25.50"), true));
        when(orderDependencyService.getProductById(202L))
                .thenReturn(product(202L, new BigDecimal("10.00"), true));
        when(orderRepository.saveAndFlush(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(500L);
            order.getOrderItems().get(0).setOrderItemId(501L);
            order.getOrderItems().get(1).setOrderItemId(502L);
            return order;
        });

        OrderResponseDto response = orderService.createOrder(request);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).saveAndFlush(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();

        assertThat(savedOrder.getUserId()).isEqualTo(10L);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(savedOrder.getTotalAmount()).isEqualByComparingTo("81.00");
        assertThat(savedOrder.getOrderItems()).hasSize(2);
        assertThat(savedOrder.getOrderItems())
                .allSatisfy(item -> assertThat(item.getOrder()).isSameAs(savedOrder));
        assertThat(savedOrder.getOrderItems().get(0).getPrice()).isEqualByComparingTo("25.50");
        assertThat(savedOrder.getOrderItems().get(1).getPrice()).isEqualByComparingTo("10.00");

        assertThat(response.getOrderId()).isEqualTo(500L);
        assertThat(response.getUserId()).isEqualTo(10L);
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(response.getTotalAmount()).isEqualByComparingTo("81.00");
        assertThat(response.getItems()).hasSize(2);

        InOrder stockThenSave = inOrder(orderDependencyService, orderRepository);
        stockThenSave.verify(orderDependencyService).reserveStock(
                eq(101L), argThat(quantityRequest(2)));
        stockThenSave.verify(orderDependencyService).reserveStock(
                eq(202L), argThat(quantityRequest(3)));
        stockThenSave.verify(orderRepository).saveAndFlush(savedOrder);
        verify(orderDependencyService, never())
                .releaseReservedStock(any(Long.class), any(StockQuantityRequestDto.class));
    }

    @Test
    void createOrder_whenSecondReservationFails_releasesOnlyPreviouslyReservedStock() {
        OrderRequestDto request = requestForUser(10L);
        when(orderDependencyService.getCartByUserId(10L)).thenReturn(cartForUser(10L, List.of(
                cartItem(1L, 101L, 2),
                cartItem(2L, 202L, 3))));
        when(orderDependencyService.getProductById(101L))
                .thenReturn(product(101L, new BigDecimal("25.50"), true));
        when(orderDependencyService.getProductById(202L))
                .thenReturn(product(202L, new BigDecimal("10.00"), true));
        RuntimeException reservationFailure = new RuntimeException("Insufficient stock");
        doThrow(reservationFailure).when(orderDependencyService)
                .reserveStock(eq(202L), any(StockQuantityRequestDto.class));

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isSameAs(reservationFailure);

        verify(orderDependencyService).releaseReservedStock(
                eq(101L), argThat(quantityRequest(2)));
        verify(orderDependencyService, never()).releaseReservedStock(
                eq(202L), any(StockQuantityRequestDto.class));
        verify(orderRepository, never()).saveAndFlush(any(Order.class));
    }

    @Test
    void createOrder_whenSavingFails_releasesEveryReservedItemAndRethrowsFailure() {
        OrderRequestDto request = requestForUser(10L);
        when(orderDependencyService.getCartByUserId(10L)).thenReturn(cartForUser(10L, List.of(
                cartItem(1L, 101L, 2),
                cartItem(2L, 202L, 3))));
        when(orderDependencyService.getProductById(101L))
                .thenReturn(product(101L, new BigDecimal("25.50"), true));
        when(orderDependencyService.getProductById(202L))
                .thenReturn(product(202L, new BigDecimal("10.00"), true));
        RuntimeException databaseFailure = new RuntimeException("Database unavailable");
        when(orderRepository.saveAndFlush(any(Order.class))).thenThrow(databaseFailure);

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isSameAs(databaseFailure);

        verify(orderDependencyService).releaseReservedStock(
                eq(101L), argThat(quantityRequest(2)));
        verify(orderDependencyService).releaseReservedStock(
                eq(202L), argThat(quantityRequest(3)));
    }

    @Test
    void createOrder_whenRollbackReleaseFails_addsFailureAsSuppressedAndContinuesRollback() {
        OrderRequestDto request = requestForUser(10L);
        when(orderDependencyService.getCartByUserId(10L)).thenReturn(cartForUser(10L, List.of(
                cartItem(1L, 101L, 2),
                cartItem(2L, 202L, 3))));
        when(orderDependencyService.getProductById(101L))
                .thenReturn(product(101L, new BigDecimal("25.50"), true));
        when(orderDependencyService.getProductById(202L))
                .thenReturn(product(202L, new BigDecimal("10.00"), true));
        RuntimeException databaseFailure = new RuntimeException("Database unavailable");
        RuntimeException releaseFailure = new RuntimeException("Release failed");
        when(orderRepository.saveAndFlush(any(Order.class))).thenThrow(databaseFailure);
        doThrow(releaseFailure).when(orderDependencyService)
                .releaseReservedStock(eq(101L), any(StockQuantityRequestDto.class));

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isSameAs(databaseFailure)
                .satisfies(thrown -> assertThat(thrown.getSuppressed())
                        .containsExactly(releaseFailure));

        verify(orderDependencyService).releaseReservedStock(
                eq(202L), argThat(quantityRequest(3)));
    }

    private static OrderRequestDto requestForUser(Long userId) {
        OrderRequestDto request = new OrderRequestDto();
        request.setUserId(userId);
        return request;
    }

    private static CartResponseDto cartForUser(Long userId, List<CartItemResponseDto> items) {
        CartResponseDto cart = new CartResponseDto();
        cart.setCartId(1L);
        cart.setUserId(userId);
        cart.setCartItems(items);
        return cart;
    }

    private static CartItemResponseDto cartItem(Long cartItemId, Long productId, Integer quantity) {
        return new CartItemResponseDto(cartItemId, productId, quantity);
    }

    private static ProductResponseDto product(Long productId, BigDecimal price, boolean active) {
        ProductResponseDto product = new ProductResponseDto();
        product.setProductId(productId);
        product.setPrice(price);
        product.setStatus(active ? ProductStatus.ACTIVE : ProductStatus.INACTIVE);
        product.setIsActive(active);
        return product;
    }

    private static ArgumentMatcher<StockQuantityRequestDto> quantityRequest(
            Integer expectedQuantity) {
        return request -> request != null && expectedQuantity.equals(request.getQuantity());
    }
}
