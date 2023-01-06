package com.fc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PriceIsNotGreaterThanZeroException extends RuntimeException {

    public PriceIsNotGreaterThanZeroException(Long id) {
        super("New price should be greater than zero. Product id is " + id);
    }
}
