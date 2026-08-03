package com.primebasket.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductFilterDto {

    private String brand;

    private BigDecimal minPrice;

    private  BigDecimal maxPrice;
}
