package com.primebasket.product_service.mapper;

import com.primebasket.common.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

    @Component
    public class PageResponseMapper {

        public static <T>PageResponse<T>fromPage(Page<T> page){
            PageResponse<T>response=new PageResponse<>();

            response.setContent(page.getContent());
            response.setPage(page.getNumber());
            response.setSize(page.getSize());
            response.setTotalElements(page.getTotalElements());
            response.setTotalPages(page.getTotalPages());
            response.setLast(page.isLast());

            return response;
        }
}
