package com.bitniki.VPNconServer.exception;

import com.bitniki.VPNconServer.exception.alreadyExistException.EntityAlreadyExistException;
import com.bitniki.VPNconServer.exception.notFoundException.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<String> catchException (Exception e) {
        //somewhere there should be logging
        return ResponseEntity.internalServerError().body("Internal Server Error");
    }

    @ExceptionHandler
    public ResponseEntity<String> catchEntityAlreadyExistException (EntityAlreadyExistException e) {
        //somewhere there should be logging
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler
    public ResponseEntity<String> catchEntityNotFoundException (EntityNotFoundException e) {
        //somewhere there should be logging
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        //return ResponseEntity.badRequest().body(e.getMessage());
    }
}
