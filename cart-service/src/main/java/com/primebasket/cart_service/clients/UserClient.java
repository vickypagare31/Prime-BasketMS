package com.primebasket.cart_service.clients;

import com.primebasket.cart_service.dto.UserAddressResponseDto;
import com.primebasket.cart_service.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {


    @GetMapping("/api/v1/users/{userId}")
    UserResponseDto fetchUserById(@PathVariable Long userId);
}
