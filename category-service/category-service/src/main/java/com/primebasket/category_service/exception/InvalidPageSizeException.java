package com.primebasket.category_service.exception;

public class InvalidPageSizeException extends RuntimeException{

    public InvalidPageSizeException(String message){
        super(message);
    }
}
