package ru.bitniki.sms.domen.exception;

public class UnexpectedStateException extends Exception {
    public UnexpectedStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
