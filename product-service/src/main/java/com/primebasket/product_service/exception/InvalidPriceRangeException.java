package com.primebasket.product_service.exception;

public class InvalidPriceRangeException extends RuntimeException{

    public InvalidPriceRangeException(String message){
        super(message);
    }
}
