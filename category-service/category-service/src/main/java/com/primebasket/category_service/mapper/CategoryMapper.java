package com.primebasket.category_service.mapper;

import com.primebasket.category_service.dto.CategoryRequestDto;
import com.primebasket.category_service.dto.CategoryResponseDto;
import com.primebasket.category_service.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public static CategoryResponseDto entToDto(Category category){

        CategoryResponseDto responseDto=new CategoryResponseDto();

        responseDto.setCategoryId(category.getCategoryId());
        responseDto.setCategoryName(category.getCategoryName());
        responseDto.setParentCategoryId(
                category.getParentCategory() != null ? category.getParentCategory().getCategoryId() : null
        );
        responseDto.setParentCategoryName(
                category.getParentCategory()!=null ? category.getParentCategory().getCategoryName() : null
        );
        responseDto.setIsActive(category.getIsActive());
        responseDto.setCreatedAt(category.getCreatedAt());

        return responseDto;
    }

    public static Category dtoToEnt(CategoryRequestDto requestDto, Category parentCategory){

        Category category=new Category();

        category.setCategoryName(requestDto.getCategoryName());
        category.setParentCategory(parentCategory);
        category.setIsActive(true);

        return category;
    }
}
