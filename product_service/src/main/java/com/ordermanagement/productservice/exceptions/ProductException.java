package com.ordermanagement.productservice.exceptions;
import org.springframework.http.HttpStatus;

public class ProductException extends RuntimeException {

    private final HttpStatus status;
    private final String label;

    public ProductException(String message, HttpStatus status, String label) {
        super(message);
        this.status = status;
        this.label = label;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getLabel() {
        return label;
    }
}