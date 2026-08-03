package com.primebasket.product_service.specification;

import com.primebasket.product_service.dto.ProductFilterDto;
import com.primebasket.product_service.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product>getProducts(ProductFilterDto filterDto){
        return (root, query, cb) -> {
            List<Predicate>predicates=new ArrayList<>();
            if(filterDto.getBrand()!=null && !filterDto.getBrand().isBlank()){
                predicates.add(cb.equal(root.get("brand"),filterDto.getBrand()));
            }

            if(filterDto.getMinPrice()!=null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"),filterDto.getMinPrice()));
            }

            if(filterDto.getMaxPrice()!=null){
                predicates.add(cb.lessThanOrEqualTo(root.get("price"),filterDto.getMaxPrice()));
            }
            predicates.add(cb.isTrue(root.get("isActive")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
