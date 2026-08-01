package com.primebasket.product_service.exceptionHandler;

import com.primebasket.product_service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SkuAlreadyExistsException.class)
    public ResponseEntity<String>handleSkuAlreadyExistsException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String>handleProductNotFoundException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyDeactivatedException.class)
    public ResponseEntity<String>handleProductAlreadyDeactivatedException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PriceConflictException.class)
    public ResponseEntity<String>handlePriceConflictException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNullException.class)
    public ResponseEntity<String>handleResourceNullException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
