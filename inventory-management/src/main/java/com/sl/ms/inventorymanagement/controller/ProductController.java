package com.sl.ms.inventorymanagement.controller;


import com.sl.ms.inventorymanagement.exception.ProductNotfoundException;
import com.sl.ms.inventorymanagement.model.Inventory;
import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.services.InventoryProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    private final InventoryProductServiceImpl service;

    @Autowired
    public ProductController(InventoryProductServiceImpl service) {
       this.service = service;
    }

    @PostMapping("/products/file")
    public ResponseEntity<List<Inventory>> create(@RequestBody Inventory inv) {
        List<Inventory> savedProduct = service.saveFile(inv);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(savedProduct.get(0).getId()).toUri();

        return ResponseEntity.created(location).body(savedProduct);
    }

    @PostMapping("/products/")
    public ResponseEntity<List<Inventory>> createProduct(@RequestBody Inventory inv) {
        List<Inventory> savedProduct = service.save(inv);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProduct.get(0).getId()).toUri();

        return ResponseEntity.created(location).body(savedProduct);
    }

    @PostMapping("/products/{product_id}")
    public ResponseEntity<List<Inventory>> addProductInInv(@PathVariable(name = "product_id") int productId,@RequestBody Product product) {
        List<Inventory> savedProduct = service.addProductInInv(productId,product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProduct.get(0).getId()).toUri();

        return ResponseEntity.created(location).body(savedProduct);
    }

    @PutMapping("/products/{product_id}")
    public ResponseEntity<List<Inventory>> updateProduct(@PathVariable(name = "product_id") int productId,@RequestBody Product product) {
        List<Inventory> savedProduct = service.updateProduct(productId,product);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProduct.get(0).getId()).toUri();

        return ResponseEntity.created(location).body(savedProduct);
    }


    @DeleteMapping("/products/{product_id}")
    public ResponseEntity<Inventory> delete(@PathVariable Integer product_id) {
        Optional<Inventory> optionalOrder = service.getInv(product_id);
        if (!optionalOrder.isPresent()) {
            throw new ProductNotfoundException();
        }

        deleteLibraryInTransaction(optionalOrder.get());

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public void deleteLibraryInTransaction(Inventory inv) {
        service.deleteInv(inv);
    }

    @GetMapping("/products/{product_id}")
    public ResponseEntity<Product> getById(@PathVariable Integer product_id) {
        Optional<Product> optionalOrder = service.getProduct(product_id);
        if (!optionalOrder.isPresent()) {
            throw new ProductNotfoundException();
        }

        return ResponseEntity.ok(optionalOrder.get());
    }

    @GetMapping("/products")
    public ResponseEntity<Page<Inventory>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAllProducts(pageable));
    }
}
