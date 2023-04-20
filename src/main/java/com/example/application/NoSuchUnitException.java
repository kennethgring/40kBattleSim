package com.example.application;

public class NoSuchUnitException extends RuntimeException {
    public NoSuchUnitException() {
        super();
    }
    public NoSuchUnitException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoSuchUnitException(String message) {
        super(message);
    }
    public NoSuchUnitException(Throwable cause) {
        super(cause);
    }
}
