package com.primebasket.product_service.service;

import com.primebasket.common.dto.PageResponse;
import com.primebasket.product_service.dto.*;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

public interface ProductService {

    ProductResponseDto addProducts(ProductRequestDto requestDto);

    ProductResponseDto getProductById(Long productId);

    void deleteProduct(Long productId);

    PageResponse<ProductResponseDto> getAllProducts(int page, int size, Sort sort, ProductFilterDto filterDto);

    ProductUpdateResponseDto updateProduct(Long productId, ProductUpdateRequestDto requestDto);

    List<ProductResponseDto>getProductsByIds(Set<Long>productIds);
}
