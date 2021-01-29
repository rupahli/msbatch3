package com.sl.ms.inventorymanagement.repo;


import com.sl.ms.inventorymanagement.model.Inventory;
import com.sl.ms.inventorymanagement.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InventoryRepoTest {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void contextLoads() {
    }

    @BeforeEach
    public void save() {

        final Inventory inventory = new Inventory();
        final Product product1 = new Product();
        product1.setId(1);
        product1.setName("Pro name");
        product1.setQuantity(2);
        product1.setPrice(400);
        product1.setInventory(inventory);
        inventory.setProducts(Set.of(product1));
        inventory.setId(1);
        inventory.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory.setFileName("fileName");
        inventory.setFile("content".getBytes());

        productRepository.save(product1);

        final Inventory inventory1 = new Inventory();
        final Product product = new Product();
        product.setId(22);
        product.setName("Pro name 22");
        product.setQuantity(4);
        product.setPrice(600);
        product.setInventory(inventory1);
        inventory1.setProducts(Set.of(product));
        inventory1.setId(22);
        inventory1.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory1.setFileName("fileName1");
        inventory1.setFile("content1".getBytes());

        productRepository.save(product);
    }

    @Test
    public void findAll() {

        List<Inventory> result=  inventoryRepository.findAll();
        Assertions.assertNotNull(result);
    }

    @Test
    public void findById() {
        Optional<Inventory> result = inventoryRepository.findById(22);
        System.out.println(result.get().getId());
        Assertions.assertNotNull(result);
    }

}
