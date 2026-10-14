package com.primebasket.order_service.client;

import com.primebasket.order_service.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/v1/products/{productId}")
    ProductResponseDto getProduct(@PathVariable("productId") Long productId);
}
