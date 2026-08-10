package com.primebasket.product_service.controller;

import com.primebasket.common.dto.PageResponse;
import com.primebasket.product_service.dto.*;
import com.primebasket.product_service.exception.InvalidPriceRangeException;
import com.primebasket.product_service.exception.InvalidSortFieldException;
import com.primebasket.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //For Validation
    private static final Set<String> ALLOWED_SORT_FIELDS=Set.of(
            "productName",
            "price",
            "brand",
            "createdAt"
    );

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
    public ResponseEntity<PageResponse<ProductResponseDto>>getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
                                                                          @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
                                                                          @RequestParam(required = false, defaultValue = "DESC") String sortDir,
                                                                          @RequestParam(required = false) String brand,
                                                                          @RequestParam(required = false)BigDecimal minPrice,
                                                                          @RequestParam(required = false)BigDecimal maxPrice){
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)){
            throw new InvalidSortFieldException("Invalid sort filed:"+sortBy);
        }
        Sort sort;
        if(!sortDir.equalsIgnoreCase("ASC") && !sortDir.equalsIgnoreCase("DESC")){
            throw new InvalidSortFieldException("Sort Direction must be ASC or DESC:");
        }
        if(sortDir.equalsIgnoreCase("ASC")){
            sort=Sort.by(sortBy).ascending();
        }else {
            sort= Sort.by(sortBy).descending();
        }

        ProductFilterDto filterDto = new ProductFilterDto();
        filterDto.setBrand(brand);
        filterDto.setMinPrice(minPrice);
        filterDto.setMaxPrice(maxPrice);
        /*
            for BigDecimal java provides features like compareTo method where
            -1 left smaller than right
             0 Both are equal
             1 left greater than right
             So below code minPrice.compareTo(maxprice)>0 means it check for is minPrice greater than maxPrice
             if yes it return true and loop will be executed.
         */
        if(minPrice!=null && maxPrice!=null && minPrice.compareTo(maxPrice)>0){
            throw new InvalidPriceRangeException("Minimum price should not greater than Maximum price");
        }

        PageResponse<ProductResponseDto>response=productService.getAllProducts(page, size,sort,filterDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductUpdateResponseDto>updateProducts(@PathVariable Long productId, @RequestBody ProductUpdateRequestDto requestDto){
        ProductUpdateResponseDto response = productService.updateProduct(productId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
