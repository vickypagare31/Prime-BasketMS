package com.primebasket.category_service.repository;

import com.primebasket.category_service.dto.CategoryRequestDto;
import com.primebasket.category_service.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCategoryNameAndParentCategory(String categoryName, Category parentCategory);

    boolean existsByCategoryNameAndParentCategoryIsNull(String categoryName);

    Page<Category> findAllByIsActiveTrue(Pageable pageable);

    Optional<Category> findByCategoryIdAndIsActiveTrue(Long categoryId);

    boolean existsByCategoryNameAndParentCategoryAndCategoryIdNot(
            String categoryName, Category parentCategory, Long categoryId);
}
