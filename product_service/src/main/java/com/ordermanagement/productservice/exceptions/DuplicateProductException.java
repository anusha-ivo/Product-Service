package com.ordermanagement.productservice.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateProductException extends ProductException {
    public DuplicateProductException(String message) {
        super(message, HttpStatus.CONFLICT, "DUPLICATE_PRODUCT");
    }
}
