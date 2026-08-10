package com.primebasket.common.dto;

import org.springframework.data.domain.Page;

public final class CommonDtoMapper {

    private CommonDtoMapper(){

    }

    public static <T> PageResponse<T>fromPage(Page<T> page){
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
