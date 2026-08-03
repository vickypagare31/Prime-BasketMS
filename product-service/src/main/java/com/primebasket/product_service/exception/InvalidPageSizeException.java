package com.primebasket.product_service.exception;

public class InvalidPageSizeException extends RuntimeException{

    public InvalidPageSizeException(String message){
        super(message);
    }
}
