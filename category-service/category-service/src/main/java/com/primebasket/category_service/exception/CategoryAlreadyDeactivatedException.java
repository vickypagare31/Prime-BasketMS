package com.primebasket.category_service.exception;

public class CategoryAlreadyDeactivatedException extends RuntimeException{

    public CategoryAlreadyDeactivatedException(String message){
        super(message);
    }
}
