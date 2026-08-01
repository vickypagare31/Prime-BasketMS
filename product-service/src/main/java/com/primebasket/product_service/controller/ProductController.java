package com.primebasket.product_service.controller;

import com.primebasket.product_service.dto.*;
import com.primebasket.product_service.service.ProductService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<ProductResponseDto>addProducts(@RequestBody ProductRequestDto requestDto){
        ProductResponseDto responseDto= productService.addProducts(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto>getProduct(@PathVariable("productId") Long productId){
        ProductResponseDto responseDto=productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String>deleteProduct(@PathVariable("productId") Long productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponseDto>>getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        PageResponse<ProductResponseDto>response=productService.getAllProducts(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductUpdateResponseDto>updateProducts(@PathVariable Long productId, @RequestBody ProductUpdateRequestDto requestDto){
        ProductUpdateResponseDto response = productService.updateProduct(productId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
