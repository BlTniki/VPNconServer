package ru.bitniki.sms.domen.exception;

public class EntityAlreadyExistException extends BadRequestException {
    public EntityAlreadyExistException(String message) {
        super(message);
    }

    public EntityAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
