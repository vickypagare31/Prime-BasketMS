package com.primebasket.cart_service.service;

import com.primebasket.cart_service.dto.ProductResponseDto;
import com.primebasket.cart_service.dto.UserResponseDto;

import java.util.List;
import java.util.Set;

public interface CartDependencyService {

    UserResponseDto getUser(Long userId);

    List<ProductResponseDto>getActiveProducts(Set<Long> productIds);
}
