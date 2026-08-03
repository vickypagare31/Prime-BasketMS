package com.primebasket.product_service.exception;

public class InvalidSortDirectionException extends RuntimeException{

    public InvalidSortDirectionException(String message){
        super(message);
    }
}
