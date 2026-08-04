package com.primebasket.product_service.service.impl;

import com.primebasket.product_service.dto.ProductRequestDto;
import com.primebasket.product_service.dto.ProductResponseDto;
import com.primebasket.product_service.entity.Product;
import com.primebasket.product_service.enums.ProductStatus;
import com.primebasket.product_service.exception.SkuAlreadyExistsException;
import com.primebasket.product_service.repository.ProductRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(null, null, null, productRepository);
    }

    @Test
    void addProducts_whenSkuIsAvailable_savesAnActiveProductAndReturnsResponse() {
        ProductRequestDto request = new ProductRequestDto(
                "Wireless Keyboard", "Compact mechanical keyboard", new BigDecimal("2499.00"),
                "PrimeTech", "KEY-001");
        Product savedProduct = new Product();
        savedProduct.setProductId(1L);
        savedProduct.setProductName(request.getProductName());
        savedProduct.setDescription(request.getDescription());
        savedProduct.setPrice(request.getPrice());
        savedProduct.setBrand(request.getBrand());
        savedProduct.setSku(request.getSku());
        savedProduct.setStatus(ProductStatus.ACTIVE);
        savedProduct.setIsActive(true);

        when(productRepository.existsBySku("KEY-001")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponseDto response = productService.addProducts(request);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product productToSave = productCaptor.getValue();
        assertThat(productToSave.getSku()).isEqualTo("KEY-001");
        assertThat(productToSave.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(productToSave.getIsActive()).isTrue();
        assertThat(response.getProductId()).isEqualTo(1L);
        assertThat(response.getProductName()).isEqualTo("Wireless Keyboard");
        assertThat(response.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(response.getIsActive()).isTrue();
    }

    @Test
    void addProducts_whenSkuAlreadyExists_throwsExceptionAndDoesNotSave() {
        ProductRequestDto request = new ProductRequestDto(
                "Wireless Keyboard", "Compact mechanical keyboard", new BigDecimal("2499.00"),
                "PrimeTech", "KEY-001");
        when(productRepository.existsBySku("KEY-001")).thenReturn(true);

        assertThatThrownBy(() -> productService.addProducts(request))
                .isInstanceOf(SkuAlreadyExistsException.class)
                .hasMessage("SKU already exists");

        verify(productRepository, never()).save(any(Product.class));
    }
}
