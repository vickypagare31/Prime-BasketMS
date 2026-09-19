package com.primebasket.cart_service.controller;

import com.primebasket.cart_service.dto.CartRequestDto;
import com.primebasket.cart_service.dto.CartResponseDto;
import com.primebasket.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponseDto>addToCart( @RequestBody CartRequestDto requestDto){
        CartResponseDto responseDto=cartService.addToCart(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDto>getCartByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

}
