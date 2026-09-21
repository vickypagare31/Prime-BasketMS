package com.primebasket.cart_service.controller;

import com.primebasket.cart_service.dto.CartRequestDto;
import com.primebasket.cart_service.dto.CartResponseDto;
import com.primebasket.cart_service.dto.CartUpdateQuantityRequestDto;
import com.primebasket.cart_service.dto.CartUpdateResponseDto;
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

    @PatchMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartUpdateResponseDto>updateCartQuantity(@PathVariable Long userId,
                                                                   @PathVariable Long productId,
                                                                   @RequestBody CartUpdateQuantityRequestDto requestDto){

        return ResponseEntity.ok(cartService.updateCartQuantity(userId, productId, requestDto));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Void>removeCartItem(@PathVariable Long userId, @PathVariable Long productId){

        cartService.removeCartItem(userId, productId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{userId}/items")
    public ResponseEntity<Void>clearCart(@PathVariable Long userId){
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validate/{userId}")
    public ResponseEntity<CartResponseDto>validateCart(@PathVariable Long userId){
        return ResponseEntity.ok(cartService.validateCart(userId));
    }
}
