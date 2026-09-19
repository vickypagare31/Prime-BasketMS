package com.primebasket.cart_service.service;

import com.primebasket.cart_service.dto.ProductResponseDto;
import com.primebasket.cart_service.dto.UserStatusResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

public interface CartDependencyService {

    UserStatusResponseDto getUser(Long userId);

    List<ProductResponseDto>getActiveProducts(Set<Long> productIds);

    ProductResponseDto getProduct(@PathVariable("productId") Long productId);
}
