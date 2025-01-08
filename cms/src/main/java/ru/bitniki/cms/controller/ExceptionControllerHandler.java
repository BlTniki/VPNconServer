package ru.bitniki.cms.controller;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ru.bitniki.cms.controller.model.ErrorResponse;
import ru.bitniki.cms.domain.exception.BadRequestException;
import ru.bitniki.cms.domain.exception.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionControllerHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ResponseEntity<ErrorResponse>> entityNotFound(EntityNotFoundException e) {
        LOGGER.warn(e);
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        e.getMessage(),
                        EntityNotFoundException.class.getName(),
                        e.getMessage(),
                        Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
                )));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ErrorResponse>> entityNotFound(ResponseStatusException e) {
        if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
            throw e;
        }
        LOGGER.warn(e);
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "Resource is not exists",
                        EntityNotFoundException.class.getName(),
                        e.getMessage(),
                        Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
                )));
    }

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<ErrorResponse>> badRequest(BadRequestException e) {
        LOGGER.warn(e);
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(),
                        e.getClass().getName(),
                        e.getMessage(),
                        Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
                )));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> internalException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        e.getMessage(),
                        e.getClass().getName(),
                        e.getMessage(),
                        Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
                )));
    }
}
