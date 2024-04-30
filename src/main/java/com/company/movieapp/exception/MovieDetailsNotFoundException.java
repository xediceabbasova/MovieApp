package com.company.movieapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MovieDetailsNotFoundException extends RuntimeException {
    public MovieDetailsNotFoundException(String message) {
        super(message);
    }
}
