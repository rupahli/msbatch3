package com.sl.ms.inventorymanagement.controller;

import com.sl.ms.inventorymanagement.exception.FileNotfoundException;
import com.sl.ms.inventorymanagement.exception.ProductNotfoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductExceptionController {
    @ExceptionHandler(value = ProductNotfoundException.class)
    public ResponseEntity<Object> exception(ProductNotfoundException exception) {
        return new ResponseEntity<>("Product not found in database", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = FileNotfoundException.class)
    public ResponseEntity<Object> exception(FileNotfoundException exception) {
        return new ResponseEntity<>("File not found at path", HttpStatus.NOT_FOUND);
    }
}