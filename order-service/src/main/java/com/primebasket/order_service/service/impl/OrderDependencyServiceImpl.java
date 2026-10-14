package com.primebasket.order_service.service.impl;

import com.primebasket.order_service.client.CartClient;
import com.primebasket.order_service.client.InventoryClient;
import com.primebasket.order_service.client.ProductClient;
import com.primebasket.order_service.dto.*;
import com.primebasket.order_service.exception.InvalidQuantityException;
import com.primebasket.order_service.exception.ResourceNotFoundException;
import com.primebasket.order_service.exception.ServiceUnavailableException;
import com.primebasket.order_service.service.OrderDependencyService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDependencyServiceImpl implements OrderDependencyService {

    private final CartClient cartClient;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    @Override
    @CircuitBreaker(name = "cartService", fallbackMethod = "cartServiceFallback")
    public CartResponseDto getCartByUserId(Long userId) {
        return cartClient.getCartByUserId(userId);
    }

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "productServiceFallback")
    public ProductResponseDto getProductById(Long productId) {
        return productClient.getProduct(productId);
    }

    @Override
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "reserveStockFallback")
    public InventoryUpdateResponseDto reserveStock(Long productId, StockQuantityRequestDto requestDto) {
        return inventoryClient.reserveStock(productId, requestDto);
    }

    @Override
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "releaseReservedStockFallback")
    public InventoryUpdateResponseDto releaseReservedStock(Long productId, StockQuantityRequestDto requestDto) {
        return inventoryClient.releaseReservedStock(productId, requestDto);
    }


    public CartResponseDto cartServiceFallback(Long userId, Exception ex) {
        if (ex instanceof FeignException feignException && feignException.status() == 404) {
            throw new ResourceNotFoundException("Cart not found for user : " + userId);
        }

        throw new ServiceUnavailableException("Cart Service is temporarily unavailable. Please try again shortly.");
    }

    public ProductResponseDto productServiceFallback(Long productId, Exception ex) {
        if (ex instanceof FeignException feignException && feignException.status() == 404) {
            throw new ResourceNotFoundException("Product not found for Id : " + productId);
        }

        throw new ServiceUnavailableException("Product Service is temporarily unavailable. Please try again shortly.");
    }

    public InventoryUpdateResponseDto reserveStockFallback(
            Long productId,
            StockQuantityRequestDto requestDto,
            Throwable throwable) {
        throw mapInventoryException(
                productId,
                throwable,
                "Inventory Service is temporarily unavailable while reserving stock. Please try again shortly.");
    }

    public InventoryUpdateResponseDto releaseReservedStockFallback(
            Long productId,
            StockQuantityRequestDto requestDto,
            Throwable throwable) {
        throw mapInventoryException(
                productId,
                throwable,
                "Inventory Service is temporarily unavailable while releasing stock. Please try again shortly.");
    }

    private RuntimeException mapInventoryException(
            Long productId,
            Throwable throwable,
            String unavailableMessage) {

        if (throwable instanceof FeignException.NotFound) {
            return new ResourceNotFoundException("Inventory not found for product: " + productId);
        }
        if (throwable instanceof FeignException.BadRequest
                || throwable instanceof FeignException.Conflict) {
            String message = ((FeignException) throwable).contentUTF8();

            return new InvalidQuantityException(
                    message.isBlank() ? "Invalid stock quantity." : message);
        }
        return new ServiceUnavailableException(unavailableMessage);
    }
}
