package com.primebasket.inventory_service.exceptionHandler;

import com.primebasket.inventory_service.exception.InventoryAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InventoryAlreadyExistsException.class)
    public ResponseEntity<String> handleInventoryAlreadyExists(InventoryAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}
