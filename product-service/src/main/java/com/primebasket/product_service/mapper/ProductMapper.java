package com.primebasket.product_service.mapper;

import com.primebasket.product_service.dto.ProductRequestDto;
import com.primebasket.product_service.dto.ProductResponseDto;
import com.primebasket.product_service.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static ProductResponseDto entToDto(Product product){
        ProductResponseDto responseDto = new ProductResponseDto();

        responseDto.setProductId(product.getProductId());
        responseDto.setProductName(product.getProductName());
        responseDto.setDescription(product.getDescription());
        responseDto.setPrice(product.getPrice());
        responseDto.setBrand(product.getBrand());
        responseDto.setSku(product.getSku());
        responseDto.setStatus(product.getStatus());
        responseDto.setIsActive(product.getIsActive());
        responseDto.setCreatedAt(product.getCreatedAt());
        responseDto.setUpdatedAt(product.getUpdatedAt());

        return responseDto;
    }

    public static Product dtoToEnt(ProductRequestDto requestDto){

        Product product=new Product();

        product.setProductName(requestDto.getProductName());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setBrand(requestDto.getBrand());
        product.setSku(requestDto.getSku());
        product.setStatus(requestDto.getStatus());

        return product;
    }
}
