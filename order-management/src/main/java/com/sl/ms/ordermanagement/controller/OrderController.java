package com.sl.ms.ordermanagement.controller;

import com.sl.ms.ordermanagement.exception.OrderNotfoundException;
import com.sl.ms.ordermanagement.model.Order;
import com.sl.ms.ordermanagement.services.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.Optional;

@RestController
public class OrderController {

    private final OrderServiceImpl orderService;

    @Autowired
    public OrderController(OrderServiceImpl orderService) {
       this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> create(@RequestBody Order order) {
        Order savedOrder = orderService.save(order);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(savedOrder.getId()).toUri();

        return ResponseEntity.created(location).body(savedOrder);
    }

    /*@PutMapping("/orders/{order_id}")
    public ResponseEntity<Order> update(@PathVariable Integer order_id, @RequestBody Order order) {
        Optional<Order> optionalOrder = orderService.getProduct(order_id);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        order.setId(optionalOrder.get().getId());
        orderService.save(order);

        return ResponseEntity.noContent().build();
    }*/

    @DeleteMapping("/orders/{order_id}")
    public ResponseEntity<Order> delete(@PathVariable Integer order_id) {
        Optional<Order> optionalOrder = orderService.getProduct(order_id);
        if (!optionalOrder.isPresent()) {
            throw new OrderNotfoundException();//ResponseEntity.unprocessableEntity().build();
        }

        deleteLibraryInTransaction(optionalOrder.get());

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public void deleteLibraryInTransaction(Order order) {
        orderService.deleteOrder(order);
    }

    @GetMapping("/orders/{order_id}")
    public ResponseEntity<Order> getById(@PathVariable Integer order_id) {
        Optional<Order> optionalOrder = orderService.getProduct(order_id);
        if (!optionalOrder.isPresent()) {
            throw new OrderNotfoundException();
            //return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok(optionalOrder.get());
    }

    @GetMapping("/orders")
    public ResponseEntity<Page<Order>> getAll(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllProducts(pageable));
    }
}
