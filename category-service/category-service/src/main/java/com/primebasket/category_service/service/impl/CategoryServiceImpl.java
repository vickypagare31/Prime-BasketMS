package com.primebasket.category_service.service.impl;

import com.primebasket.category_service.dto.CategoryRequestDto;
import com.primebasket.category_service.dto.CategoryResponseDto;
import com.primebasket.category_service.entity.Category;
import com.primebasket.category_service.exception.*;
import com.primebasket.category_service.mapper.CategoryMapper;
import com.primebasket.category_service.repository.CategoryRepository;
import com.primebasket.category_service.service.CategoryService;
import com.primebasket.common.dto.CommonDtoMapper;
import com.primebasket.common.dto.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto addCategories(CategoryRequestDto requestDto) {

        String categoryName= requestDto.getCategoryName();

        if(categoryName==null || categoryName.trim().isEmpty()){
            throw new ResourceNullException("Category name should not be empty");
        }

        categoryName=categoryName.trim();
        Category parentCategory=null;

        if(requestDto.getParentCategoryId()!=null){
            parentCategory=categoryRepository.findById(requestDto.getParentCategoryId())
                    .orElseThrow(()->new ResourceNotFoundException(
                            "Parent category not found: "+requestDto.getParentCategoryId()));
        }


        if(parentCategory==null){
            if(categoryRepository.existsByCategoryNameAndParentCategoryIsNull(categoryName)){
                throw new CategoryAlreadyExistsException(
                        "Category already exists: "+categoryName);
            }
        }
        else {
            if(categoryRepository.existsByCategoryNameAndParentCategory(categoryName,parentCategory)){
                throw new CategoryAlreadyExistsException(
                        "Category already exists under this parent category: "+parentCategory.getCategoryName());
            }
        }

        Category category=CategoryMapper.dtoToEnt(requestDto,parentCategory);
        Category savedCategory=categoryRepository.save(category);
        return CategoryMapper.entToDto(savedCategory);


    }

    @Override
    public CategoryResponseDto getCategoryById(Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException(
                        "Category not found for this ID: "+categoryId));

        return CategoryMapper.entToDto(category);

    }

    @Override
    public PageResponse<CategoryResponseDto> getAllCategory(int pageNo, int pageSize, Sort sort) {

        Pageable pageable= PageRequest.of(pageNo, pageSize,sort);
        if(pageSize>100){
            throw new InvalidPageSizeException("Maximum page size should be 100");
        }

        Page<Category>categoryPage=categoryRepository.findAllByIsActiveTrue(pageable);
        Page<CategoryResponseDto>dtoPage=categoryPage.map(CategoryMapper::entToDto);

        return CommonDtoMapper.fromPage(dtoPage);
    }

    @Override
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto) {

        Category category=categoryRepository.findByCategoryIdAndIsActiveTrue(categoryId)
                        .orElseThrow(()->new ResourceNotFoundException("Category not found for this Id: "+categoryId));

        String categoryName= category.getCategoryName();

        if(requestDto.getCategoryName()!=null){
            categoryName= requestDto.getCategoryName().trim();

            if(categoryName.isEmpty()){
                throw new ResourceNullException("Category name should not be empty");
            }
        }

        Category parentCategory=category.getParentCategory();

        if(requestDto.getParentCategoryId()!=null){
            parentCategory=categoryRepository.findById(requestDto.getParentCategoryId())
                    .orElseThrow(()->new ResourceNotFoundException("Parent category not found: "+requestDto.getParentCategoryId()));

            /*This block of code is for Circular hierarchy problem
            Suppose I have an hierarchy like this
            Electronics-->Mobiles-->Android-->Electronics(This is not acceptable)
            So this code will prevent this
            */
            Category currentParent = parentCategory;

            while (currentParent != null) {
                if (currentParent.getCategoryId().equals(categoryId)) {
                    throw new IllegalCategoryException(
                            "Cannot assign a descendant category as parent");
                }

                currentParent = currentParent.getParentCategory();
            }
        }
        if(categoryRepository.existsByCategoryNameAndParentCategoryAndCategoryIdNot(
                categoryName, parentCategory,categoryId)){

            throw new CategoryAlreadyExistsException("Category already exists under this parent category");

        }
        category.setCategoryName(categoryName);
        category.setParentCategory(parentCategory);
        category=categoryRepository.save(category);

        return CategoryMapper.entToDto(category);


    }

    @Override
    public void deleteCategory(Long categoryId) {

        Category category=categoryRepository.findById(categoryId)
                        .orElseThrow(()->new ResourceNotFoundException("Category not found for this Id: "+categoryId));


        if(!Boolean.TRUE.equals(category.getIsActive())){
            throw new CategoryAlreadyDeactivatedException("Category already deactivated");
        }

        category.setIsActive(false);
        deactivateCategoryHierarchy(category);
        categoryRepository.save(category);

    }

    private void deactivateCategoryHierarchy(Category category) {
        category.setIsActive(false);

        if(category.getSubCategories()!=null){
            for(Category child:category.getSubCategories()){
                deactivateCategoryHierarchy(child);
            }
        }
    }

}

