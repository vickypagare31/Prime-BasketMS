package com.primebasket.product_service.service.impl;

import com.primebasket.common.dto.PageResponse;
import com.primebasket.product_service.dto.*;
import com.primebasket.product_service.entity.Product;
import com.primebasket.product_service.enums.ProductStatus;
import com.primebasket.product_service.exception.*;
import com.primebasket.product_service.mapper.PageResponseMapper;
import com.primebasket.product_service.mapper.ProductMapper;
import com.primebasket.product_service.mapper.ProductUpdateMapper;
import com.primebasket.product_service.repository.ProductRepository;
import com.primebasket.product_service.service.ProductService;
import com.primebasket.product_service.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    private final ProductUpdateMapper productUpdateMapper;

    private final PageResponseMapper pageResponseMapper;

    private static final Logger logger=LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    @Override
    public ProductResponseDto addProducts(ProductRequestDto requestDto) {

    if(productRepository.existsBySku(requestDto.getSku())){
        logger.warn("Product creation failed. SKU already exists : {}", requestDto.getSku());
        throw new SkuAlreadyExistsException("SKU already exists");
    }
        Product product=ProductMapper.dtoToEnt(requestDto);
        product.setStatus(ProductStatus.valueOf("ACTIVE"));
        product.setIsActive(true);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.entToDto(savedProduct);
    }

    @Override
    public ProductResponseDto getProductById(Long productId) {
        Product product=productRepository.findById(productId).orElseThrow(()->
            new ProductNotFoundException("Product not found with ProductId :"+productId));

        return ProductMapper.entToDto(product);
    }

    @Override
    public void deleteProduct(Long productId) {

        Product product=productRepository.findByProductIdAndIsActiveTrue(productId)
                .orElseThrow(()->new ProductAlreadyDeactivatedException(
                        "Product Already Deactivated"));
        product.setIsActive(false);
        productRepository.save(product);
    }

    @Override
    public PageResponse<ProductResponseDto> getAllProducts(int page, int size, Sort sort, ProductFilterDto filterDto) {
        Pageable pageable= PageRequest.of(page, size, sort);
        if(size>100){
            throw new InvalidPageSizeException("Maximum page size is 100");
        }
        Specification<Product>specification= ProductSpecification.getProducts(filterDto);

        Page<Product>pageList=productRepository.findAll(specification,pageable);
        Page<ProductResponseDto>dtoPage= pageList.map(ProductMapper::entToDto);
        return PageResponseMapper.fromPage(dtoPage);
    }



    @Override
    public ProductUpdateResponseDto updateProduct(Long productId, ProductUpdateRequestDto requestDto) {
        //Product updateProduct;
        Product product = productRepository.findByProductIdAndIsActiveTrue(productId)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found for this Id: " + productId));

        boolean hasUpdates=requestDto.getProductName()!=null ||
                requestDto.getDescription()!=null ||
                requestDto.getPrice()!=null ||
                requestDto.getSku()!=null;

        if(!hasUpdates){
            throw new ResourceNullException("Atleast one field required for update!");
        }



            if(requestDto.getProductName()!=null){
                if(requestDto.getProductName().trim().isEmpty()){
                    throw new ResourceNullException("Product name should not be empty:");
                }
                product.setProductName(requestDto.getProductName().trim());
            }

            if (requestDto.getDescription() != null) {
                if(requestDto.getDescription().trim().isEmpty()){
                    throw new ResourceNullException("Description should not be empty:");
                }
                product.setDescription(requestDto.getDescription().trim());
            }


            if (requestDto.getPrice() != null) {
                if (requestDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new PriceConflictException("Price should be greater than 0");
                }
                product.setPrice(requestDto.getPrice());
            }


            if (requestDto.getSku() != null ) {

                String newSku=requestDto.getSku().trim();
                if(newSku.isEmpty()){
                    throw new ResourceNullException("SKU should not be empty:");
                }
                //only validate if the sku is actually changing
                if(!newSku.equals(product.getSku())){
                    if(productRepository.existsBySku(newSku)){
                        throw new SkuAlreadyExistsException("SKU already exists:");
                    }
                }
                product.setSku(requestDto.getSku());
            }
            product = productRepository.save(product);


        return ProductUpdateMapper.entToDto(product);
    }
}
