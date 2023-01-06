package com.fc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductNameAlreadyExistsException extends RuntimeException {

    public ProductNameAlreadyExistsException(String name) {
        super("The product name already exists. Please change the name: " + name);
    }
}
