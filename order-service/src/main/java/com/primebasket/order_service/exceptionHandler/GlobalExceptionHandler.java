package com.primebasket.order_service.exceptionHandler;

import com.primebasket.order_service.exception.ResourceNotFoundException;
import com.primebasket.order_service.exception.ResourceNullException;
import com.primebasket.order_service.exception.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String>handleResourceNotFoundException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<String>handleServiceUnavailableException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ResourceNullException.class)
    public ResponseEntity<String>handleResourceNullException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
