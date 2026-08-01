package com.primebasket.User.exceptionHandler;

import com.primebasket.User.exception.EmailAlreadyExistsException;
import com.primebasket.User.exception.MobileNoAlreadyExistsException;
import com.primebasket.User.exception.UserAlreadyDeactivatedException;
import com.primebasket.User.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String>handleUserNotFound(UserNotFoundException ex){

        return new ResponseEntity<>(
                ex.getMessage(), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String>handleEmailExistsException(EmailAlreadyExistsException ex){
        return new ResponseEntity<>(
                ex.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MobileNoAlreadyExistsException.class)
    public ResponseEntity<String>handleMobileNoException(MobileNoAlreadyExistsException ex){
        return new ResponseEntity<>(
                ex.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UserAlreadyDeactivatedException.class)
    public ResponseEntity<String>handleUserAlreadyDeactivatedException(UserAlreadyDeactivatedException ex){
        return new ResponseEntity<>(
                ex.getMessage(),HttpStatus.CONFLICT);
    }
}
