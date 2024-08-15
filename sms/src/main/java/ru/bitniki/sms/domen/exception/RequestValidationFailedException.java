package ru.bitniki.sms.domen.exception;

public class RequestValidationFailedException extends BadRequestException {
    public RequestValidationFailedException(String message) {
        super(message);
    }

    public RequestValidationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
