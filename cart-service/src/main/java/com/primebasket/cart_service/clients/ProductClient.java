package com.primebasket.cart_service.clients;

import com.primebasket.cart_service.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(name = "product-service")
public interface ProductClient {

    @PostMapping("/api/v1/products/validate")
    List<ProductResponseDto>validateProducts(@RequestBody Set<Long> productIds);

}
