package com.primebasket.category_service.controller;

import com.primebasket.category_service.dto.CategoryRequestDto;
import com.primebasket.category_service.dto.CategoryResponseDto;
import com.primebasket.category_service.exception.CategoryAlreadyExistsException;
import com.primebasket.category_service.exception.InvalidSortFieldException;
import com.primebasket.category_service.service.CategoryService;
import com.primebasket.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<CategoryResponseDto>addCategories(@RequestBody CategoryRequestDto requestDto){

        CategoryResponseDto responseDto=categoryService.addCategories(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto>getCategory(@PathVariable Long categoryId){
        CategoryResponseDto responseDto=categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponseDto>>getAllCategories(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                             @RequestParam(defaultValue = "10",required = false) int pageSize,
                                                                             @RequestParam(defaultValue = "createdAt", required = false) String sortBy,
                                                                             @RequestParam(defaultValue = "DESC", required = false) String sortDir){
        Sort sort;
        if(!sortDir.equalsIgnoreCase("ASC") && !sortDir.equalsIgnoreCase("DESC")){
            throw  new InvalidSortFieldException("Sort Direction must be ASC or DESC");
        }

        if(sortDir.equalsIgnoreCase("ASC")){
            sort=Sort.by(sortBy).ascending();
        }else {
            sort= Sort.by(sortBy).descending();
        }

        PageResponse<CategoryResponseDto>response= categoryService.getAllCategory(pageNo, pageSize,sort);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto>updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequestDto requestDto){
        CategoryResponseDto responseDto=categoryService.updateCategory(categoryId,requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void>deleteCategory(@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
