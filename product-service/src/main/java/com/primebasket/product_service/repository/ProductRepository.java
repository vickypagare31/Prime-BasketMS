package com.primebasket.product_service.repository;

import com.primebasket.product_service.dto.PageResponse;
import com.primebasket.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Boolean existsBySku(String sku);

    Optional<Product> findByProductIdAndIsActiveTrue(Long productId);

    Page<Product> findAllByIsActiveTrue(Pageable pageable);
}
