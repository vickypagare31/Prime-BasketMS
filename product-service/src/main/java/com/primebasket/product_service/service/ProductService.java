package com.primebasket.product_service.service;

import com.primebasket.product_service.dto.*;
import com.primebasket.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ProductService {

    ProductResponseDto addProducts(ProductRequestDto requestDto);

    ProductResponseDto getProductById(Long productId);

    void deleteProduct(Long productId);

    PageResponse<ProductResponseDto> getAllProducts(int page, int size, Sort sort, ProductFilterDto filterDto);

    ProductUpdateResponseDto updateProduct(Long productId, ProductUpdateRequestDto requestDto);
}
