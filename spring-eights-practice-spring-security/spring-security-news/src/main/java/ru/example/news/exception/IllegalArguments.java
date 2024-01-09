package ru.example.news.exception;

public class IllegalArguments extends RuntimeException{
    public IllegalArguments(String message) {
        super(message);
    }
}
