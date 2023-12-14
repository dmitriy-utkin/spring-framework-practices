package com.example.fourth.exception;

public class NotAllowedChangeRequestException extends RuntimeException {
    public NotAllowedChangeRequestException(String message) {
        super(message);
    }
}
