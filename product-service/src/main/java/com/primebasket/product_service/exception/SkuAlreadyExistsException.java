package com.primebasket.product_service.exception;

public class SkuAlreadyExistsException extends RuntimeException{

    public SkuAlreadyExistsException(String message){
        super(message);

    }
}
