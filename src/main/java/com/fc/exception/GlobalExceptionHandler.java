package com.fc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ProductNotFoundException.class)
    public ResponseEntity productNotFoundException(ProductNotFoundException productNotFoundException) {
        return new ResponseEntity(productNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ProductNameAlreadyExistsException.class)
    public ResponseEntity productNameAlreadyExistsException(ProductNameAlreadyExistsException productNameAlreadyExistsException) {
        return new ResponseEntity(productNameAlreadyExistsException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ProductNameDoesNotProvidedException.class)
    public ResponseEntity productNameDoesNotProvidedException(ProductNameDoesNotProvidedException productNameDoesNotProvidedException) {
        return new ResponseEntity(productNameDoesNotProvidedException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MissingMandatoryFieldException.class)
    public ResponseEntity missingMandatoryFieldException(MissingMandatoryFieldException missingMandatoryFieldException) {
        return new ResponseEntity(missingMandatoryFieldException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PriceIsNotGreaterThanZeroException.class)
    public ResponseEntity priceIsNotGreaterThanZeroException(PriceIsNotGreaterThanZeroException priceIsNotGreaterThanZeroException) {
        return new ResponseEntity(priceIsNotGreaterThanZeroException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}