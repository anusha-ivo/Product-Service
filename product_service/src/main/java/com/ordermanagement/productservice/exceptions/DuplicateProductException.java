package com.ordermanagement.productservice.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateProductException extends AppException{
    public DuplicateProductException(String message) {
        super(message, HttpStatus.CONFLICT, "DUPLICATE_PRODUCT");
    }
}
