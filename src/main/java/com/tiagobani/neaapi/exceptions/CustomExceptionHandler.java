package com.tiagobani.neaapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintDeclarationException;
import java.util.Objects;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ConstraintDeclarationException.class)
    public ResponseEntity<ErrorDto> constraintDeclarationExceptionHandling(ConstraintDeclarationException exception, WebRequest webRequest){
        return new ResponseEntity<>(ErrorDto.builder()
                .message(exception.getMessage())
                .path(webRequest.getDescription(false))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> constraintDeclarationExceptionHandling(MethodArgumentTypeMismatchException exception, WebRequest webRequest){

        return new ResponseEntity<>(ErrorDto.builder()
                .message(exception.getMostSpecificCause().getMessage())
                .path(webRequest.getDescription(false))
                .details("[" + exception.getParameter().getParameterName() + "]: " + exception.getValue())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorDto> globalExceptionHandling(BindException exception, WebRequest webRequest) {
        var fieldError = (FieldError) exception.getBindingResult().getAllErrors().stream().findFirst().orElse(null);
        return new ResponseEntity<>(ErrorDto.builder()
                .message("Value with format invalid")
                .details("[" + Objects.requireNonNullElse(fieldError.getField(), "undefined") + "]: " + fieldError.getRejectedValue())
                .path(webRequest.getDescription(false))
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NeaFeedException.class)
    public ResponseEntity<ErrorDto> neaFeedExceptionHandling(NeaFeedException exception, WebRequest webRequest){
        return new ResponseEntity<>(ErrorDto.builder()
                .message(exception.getMessage())
                .path(webRequest.getDescription(false))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> globalExceptionHandling(Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(ErrorDto.builder()
                .message(exception.getMessage())
                .path(webRequest.getDescription(false))
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
