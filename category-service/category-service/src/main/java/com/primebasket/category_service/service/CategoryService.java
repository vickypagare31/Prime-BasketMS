package com.primebasket.category_service.service;

import com.primebasket.category_service.dto.CategoryRequestDto;
import com.primebasket.category_service.dto.CategoryResponseDto;
import com.primebasket.common.dto.PageResponse;
import org.springframework.data.domain.Sort;

public interface CategoryService {

    CategoryResponseDto addCategories(CategoryRequestDto requestDto);

    CategoryResponseDto getCategoryById(Long categoryId);

    PageResponse<CategoryResponseDto>getAllCategory(int pageNo, int pageSize, Sort sort);

    CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto);

    void deleteCategory(Long categoryId);
}
