package com.sl.ms.inventorymanagement.services;

import com.sl.ms.inventorymanagement.controller.ProductController;
import com.sl.ms.inventorymanagement.exception.FileNotfoundException;
import com.sl.ms.inventorymanagement.exception.ProductNotfoundException;
import com.sl.ms.inventorymanagement.model.Inventory;
import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.repo.InventoryRepository;
import com.sl.ms.inventorymanagement.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class InventoryProductServiceImpl {


    private static final Logger LOGGER = LogManager.getLogger(InventoryProductServiceImpl.class);
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public InventoryProductServiceImpl(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public List<Inventory> save(Inventory inv) {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside save :  "+inv);
        inv.setDate(new Date());
        this.inventoryRepository.save(inv);
        LOGGER.info("Record Saved Successfully");
        LOGGER.info("=========END TIME ============"+new Date());
        return this.inventoryRepository.findAll();
    }

    public List<Inventory> saveFile(Inventory inv) {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside saveFile :  "+inv);
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

            LOGGER.info("File Saved Successfully");
            LOGGER.info("=========END TIME ============"+new Date());
            return inventoryRepository.findAll();
        } catch (IOException e) {
            LOGGER.error("Error occurred : "+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Optional<Product> getProduct(Integer inv_id) {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside getProduct :  "+inv_id);
        Optional<Product> p  = productRepository.findById(inv_id);
        LOGGER.info("=========END TIME ============"+new Date());
        return p;

    }

    public Optional<Inventory> getInv(Integer inv_id) {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside getInv :  "+inv_id);
        Optional<Inventory> inv = inventoryRepository.findById(inv_id);
        LOGGER.info("=========END TIME ============"+new Date());
        return inv;

    }

    public Page<Inventory> getAllProducts(Pageable pageable) {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside getAllProducts :  "+pageable);
        Page<Inventory> inv = inventoryRepository.findAll(pageable);
        LOGGER.info("=========END TIME ============"+new Date());
        return inv;
    }

    public void deleteInv(Inventory inv) {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside deleteInv :  "+inv);
        productRepository.deleteById(inv.getId());
        inventoryRepository.delete(inv);
        LOGGER.info("Records Deleted");
        LOGGER.info("=========END TIME ============"+new Date());
    }

    public List<Inventory> addProductInInv(int productId, Product product) {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside addProductInInv :  "+productId +": Product : "+product);
        Set<Product> products = new HashSet<>();
        products.add(product);

        Inventory inv = new Inventory();
        inv.setId(productId);
        inv.setDate(new Date());
        inv.setFile(null);
        inv.setProducts(products);
        inventoryRepository.save(inv);

        LOGGER.info("Product Saved");
        List<Inventory> invReturn = inventoryRepository.findAll();
        LOGGER.info("=========END TIME ============"+new Date());
        return invReturn;

    }

    public List<Inventory> updateProduct(int productId, Product inputProduct) {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside updateProduct :  "+productId +": Product : "+inputProduct);
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            LOGGER.error("ProductNotfoundException to be thrown");
            throw new ProductNotfoundException();
        }
        product.get().setName(inputProduct.getName());
        product.get().setQuantity(inputProduct.getQuantity());
        product.get().setPrice(inputProduct.getPrice());

        productRepository.save(product.get());
        LOGGER.info("Product Updated");
        List<Inventory> inv = inventoryRepository.findAll();
        LOGGER.info("=========END TIME ============"+new Date());
        return inv;
    }

    @Cacheable("cachedProducts")
    public List<Product> getSupportedProducts() {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside getSupportedProducts :  ");
        List<Product> p = productRepository.findAll();
        LOGGER.info("=========END TIME ============"+new Date());
        return p;
    }
}
