package com.primebasket.product_service.exception;

public class PriceConflictException extends RuntimeException{
    public PriceConflictException(String message){
        super(message);
    }
}
