package com.example.fourth.web.controller.defaults;

import com.example.fourth.exception.EntityNotFoundException;
import com.example.fourth.exception.IllegalArguments;
import com.example.fourth.exception.NotAllowedChangeRequestException;
import com.example.fourth.exception.UserAlreadyExistsException;
import com.example.fourth.web.model.defaults.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NotAllowedChangeRequestException.class)
    public ResponseEntity<ErrorResponse> notAllowedUpdate(NotAllowedChangeRequestException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> userAlreadyExists(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalArguments.class)
    public ResponseEntity<ErrorResponse> illegalArguments(IllegalArguments e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> methodNotSupported(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> internalServerError(HttpServerErrorException e) {
        return ResponseEntity.internalServerError().body(new ErrorResponse("Internal server error"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> notValid(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        String errorMessage = String.join("; ", errorMessages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage));
    }

}
