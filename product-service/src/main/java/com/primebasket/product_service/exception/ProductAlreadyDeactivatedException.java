package com.primebasket.product_service.exception;

public class ProductAlreadyDeactivatedException extends RuntimeException{

    public ProductAlreadyDeactivatedException(String message){
        super(message);
    }
}
