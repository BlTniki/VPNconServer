package com.bitniki.VPNconServer.exception;

import com.bitniki.VPNconServer.exception.alreadyExistException.EntityAlreadyExistException;
import com.bitniki.VPNconServer.exception.notFoundException.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.validationFailedException.EntityValidationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    }

    @ExceptionHandler
    public ResponseEntity<String> catchUsernameNotFoundException (UsernameNotFoundException e) {
        //somewhere there should be logging
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> catchEntityValidationFailedException (EntityValidationFailedException e) {
        //somewhere there should be logging
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> catchAccessDeniedException(AccessDeniedException e) {
        //somewhere there should be logging
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> catchBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials");
    }
}
