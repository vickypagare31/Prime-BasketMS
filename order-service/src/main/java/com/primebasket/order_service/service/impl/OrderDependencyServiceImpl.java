package com.primebasket.order_service.service.impl;

import com.primebasket.order_service.client.CartClient;
import com.primebasket.order_service.client.ProductClient;
import com.primebasket.order_service.dto.CartResponseDto;
import com.primebasket.order_service.dto.ProductResponseDto;
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

    public CartResponseDto cartServiceFallback(Long userId, Exception ex){
        if(ex instanceof FeignException feignException && feignException.status()==404){
            throw new ResourceNotFoundException("Cart not found for user : "+userId);
        }

        throw new ServiceUnavailableException("Cart Service is temporarily unavailable. Please try again shortly.");
    }

    public ProductResponseDto productServiceFallback(Long productId, Exception ex){
        if(ex instanceof FeignException feignException && feignException.status()==404){
            throw new ResourceNotFoundException("Product not found for Id : "+productId);
        }

        throw new ServiceUnavailableException("Product Service is temporarily unavailable. Please try again shortly.");
    }
}
