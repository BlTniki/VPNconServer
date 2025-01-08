package ru.bitniki.cms.domain.exception;

public class UnexpectedStateException extends Exception {
    public UnexpectedStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
