package com.tiagobani.neaapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NeaFeedException extends RuntimeException {

    public NeaFeedException(String message) {
        super(message);
    }
}
