package com.primebasket.order_service.service;

import com.primebasket.order_service.dto.CartResponseDto;
import com.primebasket.order_service.dto.ProductResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

public interface OrderDependencyService {

    CartResponseDto getCartByUserId(@PathVariable Long userId);

    ProductResponseDto getProductById(@PathVariable Long productId);
}
