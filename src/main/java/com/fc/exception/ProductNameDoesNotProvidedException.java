package com.fc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductNameDoesNotProvidedException extends RuntimeException {

    public ProductNameDoesNotProvidedException() {
        super("The product name was not provided. Please provide the product name!");
    }
}
