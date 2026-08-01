package com.primebasket.product_service.mapper;

import com.primebasket.product_service.dto.ProductRequestDto;
import com.primebasket.product_service.dto.ProductUpdateRequestDto;
import com.primebasket.product_service.dto.ProductUpdateResponseDto;
import com.primebasket.product_service.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductUpdateMapper {

    public static Product dtoToEnt(ProductUpdateRequestDto requestDto){
        Product product= new Product();

        product.setProductName(requestDto.getProductName());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setSku(requestDto.getSku());

        return product;
    }

    public static ProductUpdateResponseDto entToDto(Product product){

        ProductUpdateResponseDto responseDto=new ProductUpdateResponseDto();

        responseDto.setProductName(product.getProductName());
        responseDto.setDescription(product.getDescription());
        responseDto.setBrand(product.getBrand());
        responseDto.setPrice(product.getPrice());
        responseDto.setSku(product.getSku());
        responseDto.setStatus(product.getStatus());
        responseDto.setIsActive(product.getIsActive());
        responseDto.setUpdatedAt(product.getUpdatedAt());
        return responseDto;
    }
}
