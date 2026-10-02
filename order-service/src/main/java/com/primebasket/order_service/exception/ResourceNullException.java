package com.primebasket.order_service.exception;

public class ResourceNullException extends RuntimeException{

    public ResourceNullException(String message){
        super(message);
    }
}
