package ru.bitniki.sms.domain.exception;

public class EntityNotFoundException extends BadRequestException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
