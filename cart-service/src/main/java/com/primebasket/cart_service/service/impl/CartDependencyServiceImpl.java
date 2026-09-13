package com.primebasket.cart_service.service.impl;

import com.primebasket.cart_service.dto.ProductResponseDto;
import com.primebasket.cart_service.dto.UserResponseDto;
import com.primebasket.cart_service.service.CartDependencyService;

import java.util.List;
import java.util.Set;

public class CartDependencyServiceImpl implements CartDependencyService {
    @Override
    public UserResponseDto getUser(Long userId) {
        return null;
    }

    @Override
    public List<ProductResponseDto> getActiveProducts(Set<Long> productIds) {
        return List.of();
    }
}
