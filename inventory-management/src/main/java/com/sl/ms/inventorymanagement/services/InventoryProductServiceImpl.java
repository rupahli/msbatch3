package com.sl.ms.inventorymanagement.services;

import com.sl.ms.inventorymanagement.exception.FileNotfoundException;
import com.sl.ms.inventorymanagement.exception.ProductNotfoundException;
import com.sl.ms.inventorymanagement.model.Inventory;
import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.repo.InventoryRepository;
import com.sl.ms.inventorymanagement.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InventoryProductServiceImpl {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public InventoryProductServiceImpl(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public List<Inventory> save(Inventory inv) {
        inv.setDate(new Date());
        this.inventoryRepository.save(inv);
        return this.inventoryRepository.findAll();
    }

    public List<Inventory> saveFile(Inventory inv) {

        File file = new File(inv.getFileName());
        byte[] bFile = new byte[(int) file.length()];

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            throw new FileNotfoundException();
        }
        List<String> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(inv.getFileName()))) {

            list = stream
                    .filter(line -> !line.startsWith("line3"))
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());

            Set<Product> products = new HashSet<>();
            list.stream().skip(1).forEach(s -> {

                Product prod = new Product();
                String[] val = s.split(",");

                prod.setName(val[1]);
                prod.setQuantity(Integer.valueOf(val[2]));
                prod.setPrice(Integer.valueOf(val[3]));
                products.add(prod);
            });

            inv.setProducts(products);
            inv.setFile(bFile);
            inv.setDate(new Date());

            inventoryRepository.save(inv);

            return inventoryRepository.findAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<Inventory> getProduct(Integer inv_id) {
        return inventoryRepository.findById(inv_id);
    }

    public Page<Inventory> getAllProducts(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

    public void deleteInv(Inventory inv) {
        productRepository.deleteById(inv.getId());
        inventoryRepository.delete(inv);
    }

    public List<Inventory> addProductInInv(int productId, Product product) {

        Set<Product> products = new HashSet<>();
        products.add(product);

        Inventory inv = new Inventory();
        inv.setId(productId);
        inv.setDate(new Date());
        inv.setFile(null);
        inv.setProducts(products);
        inventoryRepository.save(inv);
        return inventoryRepository.findAll();

    }

    public List<Inventory> updateProduct(int productId, Product inputProduct) {
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            throw new ProductNotfoundException();
        }
        product.get().setName(inputProduct.getName());
        product.get().setQuantity(inputProduct.getQuantity());
        product.get().setPrice(inputProduct.getPrice());

        productRepository.save(product.get());

        return inventoryRepository.findAll();
    }
}
