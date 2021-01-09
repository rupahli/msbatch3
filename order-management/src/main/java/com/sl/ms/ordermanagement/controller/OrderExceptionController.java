package com.sl.ms.ordermanagement.controller;

import com.sl.ms.ordermanagement.exception.OrderNotfoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OrderExceptionController {
    @ExceptionHandler(value = OrderNotfoundException.class)
    public ResponseEntity<Object> exception(OrderNotfoundException exception) {
        return new ResponseEntity<>("Order not found in database", HttpStatus.NOT_FOUND);
    }
}