package com.primebasket.product_service.dto;

import com.primebasket.product_service.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductResponseDto {

    private Long productId;

    private String productName;

    private String description;

    private BigDecimal price;

    private String brand;

    private String sku;

    private ProductStatus status;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
