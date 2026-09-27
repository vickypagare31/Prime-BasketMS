package com.primebasket.order_service.client;

import com.primebasket.order_service.dto.CartResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service")
public interface CartClient {

    @GetMapping("/api/v1/cart/{userId}")
    CartResponseDto getCartByUserId(@PathVariable Long userId);
}
