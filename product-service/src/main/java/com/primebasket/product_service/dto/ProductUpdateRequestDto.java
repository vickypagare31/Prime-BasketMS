package com.primebasket.product_service.dto;

import com.primebasket.product_service.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductUpdateRequestDto {

    private String productName;

    private String description;

    private BigDecimal price;

    private String sku;

}
