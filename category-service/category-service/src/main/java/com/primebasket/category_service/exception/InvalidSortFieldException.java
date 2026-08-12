package com.primebasket.category_service.exception;

public class InvalidSortFieldException extends RuntimeException{
    public InvalidSortFieldException(String message){
        super(message);
    }
}
