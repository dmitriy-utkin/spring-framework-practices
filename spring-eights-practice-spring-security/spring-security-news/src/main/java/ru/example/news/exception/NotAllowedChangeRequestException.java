package ru.example.news.exception;

public class NotAllowedChangeRequestException extends RuntimeException {
    public NotAllowedChangeRequestException(String message) {
        super(message);
    }
}
