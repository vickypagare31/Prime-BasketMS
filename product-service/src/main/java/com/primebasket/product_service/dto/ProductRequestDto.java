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
public class ProductRequestDto {

    @NotBlank(message = "Product name is mandatory")
    private String productName;

    private String description;

    @NotBlank(message = "Product price is mandatory")
    private BigDecimal price;

    @NotBlank(message = "Product brand is mandatory")
    private String brand;

    @NotBlank(message = "Product sku is mandatory")
    private String sku;

    private ProductStatus status;

}
