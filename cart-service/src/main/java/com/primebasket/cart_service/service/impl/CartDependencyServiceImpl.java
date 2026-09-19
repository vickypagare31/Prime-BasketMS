package com.primebasket.cart_service.service.impl;

import com.primebasket.cart_service.clients.ProductClient;
import com.primebasket.cart_service.clients.UserClient;
import com.primebasket.cart_service.dto.ProductResponseDto;
import com.primebasket.cart_service.dto.UserStatusResponseDto;
import com.primebasket.cart_service.exception.ResourceNotFoundException;
import com.primebasket.cart_service.exception.ServiceUnavailableException;
import com.primebasket.cart_service.service.CartDependencyService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class CartDependencyServiceImpl implements CartDependencyService {

    private final UserClient userClient;
    private final ProductClient productClient;

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "userServiceFallback")
    public UserStatusResponseDto getUser(Long userId) {
        return userClient.getUserStatus(userId);
    }

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "productServiceFallback")
    public List<ProductResponseDto> getActiveProducts(Set<Long> productIds) {
        return productClient.validateProducts(productIds);
    }

    @Override
    @CircuitBreaker(name = "productServiceGet", fallbackMethod = "getProductFallBack")
    public ProductResponseDto getProduct(Long productId) {
        return productClient.getProduct(productId);
    }

    public UserStatusResponseDto userServiceFallback(Long userId, Throwable error){
        if(error instanceof FeignException feignException && feignException.status()==404){
            throw new ResourceNotFoundException("User not found with Id: "+userId);
        }

        throw new ServiceUnavailableException("User Service is temporarily unavailable. Please try again shortly.");
    }

    public List<ProductResponseDto>productServiceFallback(Set<Long> productIds, Throwable error){
        throw new ServiceUnavailableException("Product Service is temporarily unavailable. Please try again shortly.");
    }

    public ProductResponseDto getProductFallBack(Long productId, Exception ex){
        throw new ServiceUnavailableException("Product Service unavailable. Please try again shortly.");
    }
}
