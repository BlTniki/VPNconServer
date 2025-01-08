package ru.bitniki.cms.domain.exception;

public class EntityAlreadyExistException extends BadRequestException {
    public EntityAlreadyExistException(String message) {
        super(message);
    }

    public EntityAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
