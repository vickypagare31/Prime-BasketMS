package com.primebasket.cart_service.exceptionHandling;

import com.primebasket.cart_service.exception.CartEmptyException;
import com.primebasket.cart_service.exception.ResourceNotFoundException;
import com.primebasket.cart_service.exception.ResourceNullException;
import com.primebasket.cart_service.exception.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String>handleResourceNotFoundException(Exception ex){
        return  new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNullException.class)
    public ResponseEntity<String>handleResourceNullException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<String>handleServiceUnavailableException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<String>handleCartEmptyException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NO_CONTENT);
    }
}
