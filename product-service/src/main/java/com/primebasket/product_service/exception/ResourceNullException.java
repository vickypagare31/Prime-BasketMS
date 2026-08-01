package com.primebasket.product_service.exception;

public class ResourceNullException extends RuntimeException{
    public ResourceNullException(String message){
        super(message);
    }
}
