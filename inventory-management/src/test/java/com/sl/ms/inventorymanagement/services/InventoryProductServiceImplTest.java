package com.sl.ms.inventorymanagement.services;

import com.sl.ms.inventorymanagement.exception.FileNotfoundException;
import com.sl.ms.inventorymanagement.exception.ProductNotfoundException;
import com.sl.ms.inventorymanagement.model.Inventory;
import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.repo.InventoryRepository;
import com.sl.ms.inventorymanagement.repo.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class InventoryProductServiceImplTest {

    @Mock
    private InventoryRepository mockInventoryRepository;
    @Mock
    private ProductRepository mockProductRepository;

    private InventoryProductServiceImpl inventoryProductServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        inventoryProductServiceImplUnderTest = new InventoryProductServiceImpl(mockInventoryRepository, mockProductRepository);
    }

    @Test
    void testSave() {

        final Inventory inventory = new Inventory();
        final Product product = new Product();
        product.setId(1);
        product.setName("Pro name");
        product.setQuantity(2);
        product.setPrice(400);
        product.setInventory(inventory);
        inventory.setProducts(Set.of(product));
        inventory.setId(1);
        inventory.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory.setFileName("fileName");
        inventory.setFile("content".getBytes());


        final Inventory inventory1 = new Inventory();
        final Product product1 = new Product();
        product1.setId(2);
        product1.setName("Pro name2");
        product1.setQuantity(22);
        product1.setPrice(4020);
        product1.setInventory(inventory1);
        inventory1.setProducts(Set.of(product1));
        inventory1.setId(2);
        inventory1.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory1.setFileName("fileName2");
        inventory1.setFile("content2".getBytes());



        final Inventory inventory2 = new Inventory();
        final Product product2 = new Product();
        product2.setId(3);
        product2.setName("Pro name2");
        product2.setQuantity(322);
        product2.setPrice(403);
        product2.setInventory(inventory2);
        inventory2.setProducts(Set.of(product2));
        inventory2.setId(3);
        inventory2.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory2.setFileName("fileName3");
        inventory2.setFile("content3".getBytes());
        final List<Inventory> inventories = List.of(inventory2);
        when(mockInventoryRepository.save(any(Inventory.class))).thenReturn(inventory2);
        when(mockInventoryRepository.findAll()).thenReturn(inventories);


        final List<Inventory> result = inventoryProductServiceImplUnderTest.save(inventory2);
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.get(0).getId(),3);

    }

    @Test
    void testSaveFile() {

        final Inventory inventory = new Inventory();
        final Product product = new Product();
        product.setId(1);
        product.setName("Pro name");
        product.setQuantity(2);
        product.setPrice(400);
        product.setInventory(inventory);
        inventory.setProducts(Set.of(product));
        inventory.setId(1);
        inventory.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory.setFileName("fileName");
        inventory.setFile("content".getBytes());


        final Inventory inventory1 = new Inventory();
        final Product product1 = new Product();
        product1.setId(2);
        product1.setName("Pro name2");
        product1.setQuantity(22);
        product1.setPrice(4020);
        product1.setInventory(inventory1);
        inventory1.setProducts(Set.of(product1));
        inventory1.setId(2);
        inventory1.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory1.setFileName("fileName2");
        inventory1.setFile("content2".getBytes());



        final Inventory inventory2 = new Inventory();
        final Product product2 = new Product();
        product2.setId(3);
        product2.setName("Pro name2");
        product2.setQuantity(322);
        product2.setPrice(403);
        product2.setInventory(inventory2);
        inventory2.setProducts(Set.of(product2));
        inventory2.setId(3);
        inventory2.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory2.setFileName("fileName3");
        inventory2.setFile("content3".getBytes());
        final List<Inventory> inventories = List.of(inventory2);
        when(mockInventoryRepository.save(any(Inventory.class))).thenReturn(inventory2);
        when(mockInventoryRepository.findAll()).thenReturn(inventories);

        try{
            final List<Inventory> result = inventoryProductServiceImplUnderTest.saveFile(inventory2);
        }catch (Exception ex){
            Assertions.assertEquals(true,ex instanceof FileNotfoundException);
        }

    }

    @Test
    void testGetProduct() {

        final Inventory inventory2 = new Inventory();
        final Product product2 = new Product();
        product2.setId(3);
        product2.setName("Pro name2");
        product2.setQuantity(322);
        product2.setPrice(403);
        product2.setInventory(inventory2);
        inventory2.setProducts(Set.of(product2));
        inventory2.setId(3);
        inventory2.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory2.setFileName("fileName3");
        inventory2.setFile("content3".getBytes());
        final Optional<Product> product = Optional.of(product2);
        when(mockProductRepository.findById(3)).thenReturn(product);


        final Optional<Product> result = inventoryProductServiceImplUnderTest.getProduct(3);
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.get().getName(),product2.getName());

    }

    @Test
    void testGetProduct_ProductRepositoryReturnsAbsent() {

        when(mockProductRepository.findById(77)).thenReturn(Optional.empty());


        final Optional<Product> result = inventoryProductServiceImplUnderTest.getProduct(0);
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.isEmpty(),true);
    }

    @Test
    void testGetInv() {

        final Inventory inventory2 = new Inventory();
        final Product product2 = new Product();
        product2.setId(3);
        product2.setName("Pro name2");
        product2.setQuantity(322);
        product2.setPrice(403);
        product2.setInventory(inventory2);
        inventory2.setProducts(Set.of(product2));
        inventory2.setId(3);
        inventory2.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory2.setFileName("fileName3");
        inventory2.setFile("content3".getBytes());
        final Optional<Inventory> inventory = Optional.of(inventory2);
        when(mockInventoryRepository.findById(3)).thenReturn(inventory);


        final Optional<Inventory> result = inventoryProductServiceImplUnderTest.getInv(3);
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.get().getId(),product2.getId());

    }

    @Test
    void testGetInv_InventoryRepositoryReturnsAbsent() {

        when(mockInventoryRepository.findById(99)).thenReturn(Optional.empty());


        final Optional<Inventory> result = inventoryProductServiceImplUnderTest.getInv(99);

        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.isEmpty(),true);
    }

    @Test
    void testGetAllProducts() {

        final Inventory inventory = new Inventory();
        final Product product = new Product();
        product.setId(1);
        product.setName("Pro name");
        product.setQuantity(2);
        product.setPrice(400);
        product.setInventory(inventory);
        inventory.setProducts(Set.of(product));
        inventory.setId(1);
        inventory.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory.setFileName("fileName");
        inventory.setFile("content".getBytes());

        List<Inventory> inventories = new ArrayList<Inventory>();
        inventories.add(inventory);

        final Inventory inventory1 = new Inventory();
        final Product product1 = new Product();
        product1.setId(2);
        product1.setName("Pro name2");
        product1.setQuantity(22);
        product1.setPrice(4020);
        product1.setInventory(inventory1);
        inventory1.setProducts(Set.of(product1));
        inventory1.setId(2);
        inventory1.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory1.setFileName("fileName2");
        inventory1.setFile("content2".getBytes());
        inventories.add(inventory1);


        final Inventory inventory2 = new Inventory();
        final Product product2 = new Product();
        product2.setId(3);
        product2.setName("Pro name2");
        product2.setQuantity(322);
        product2.setPrice(403);
        product2.setInventory(inventory2);
        inventory2.setProducts(Set.of(product2));
        inventory2.setId(3);
        inventory2.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory2.setFileName("fileName3");
        inventory2.setFile("content3".getBytes());
        inventories.add(inventory2);

        final Page<Inventory> inventoriesP = new PageImpl<>(inventories);
        when(mockInventoryRepository.findAll(any(Pageable.class))).thenReturn(inventoriesP);


        final Page<Inventory> result = inventoryProductServiceImplUnderTest.getAllProducts(PageRequest.of(0, 1));
        System.out.println("RESULT" + result.toString());

        Assertions.assertEquals(result.getSize(),3);
    }

    @Test
    void testDeleteInv() {

        final Inventory inventory2 = new Inventory();
        final Product product2 = new Product();
        product2.setId(3);
        product2.setName("Pro name2");
        product2.setQuantity(322);
        product2.setPrice(403);
        product2.setInventory(inventory2);
        inventory2.setProducts(Set.of(product2));
        inventory2.setId(3);
        inventory2.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory2.setFileName("fileName3");
        inventory2.setFile("content3".getBytes());

        inventoryProductServiceImplUnderTest.deleteInv(inventory2);
        verify(mockProductRepository).deleteById(3);
        verify(mockInventoryRepository).delete(any(Inventory.class));
    }

    @Test
    void testAddProductInInv() {

        final Inventory inventory2 = new Inventory();
        final Product product2 = new Product();
        product2.setId(3);
        product2.setName("Pro name2");
        product2.setQuantity(322);
        product2.setPrice(403);
        product2.setInventory(inventory2);
        inventory2.setProducts(Set.of(product2));
        inventory2.setId(3);
        inventory2.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory2.setFileName("fileName3");
        inventory2.setFile("content3".getBytes());

        when(mockInventoryRepository.save(any(Inventory.class))).thenReturn(inventory2);
        final List<Inventory> inventories = List.of(inventory2);
        when(mockInventoryRepository.findAll()).thenReturn(inventories);

        final List<Inventory> result = inventoryProductServiceImplUnderTest.addProductInInv(3, product2);
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.get(0).getId(),inventory2.getId());

    }

    @Test
    void testUpdateProduct() {

        final Inventory inventory2 = new Inventory();
        final Product product2 = new Product();
        product2.setId(3);
        product2.setName("Pro name2");
        product2.setQuantity(322);
        product2.setPrice(403);
        product2.setInventory(inventory2);
        inventory2.setProducts(Set.of(product2));
        inventory2.setId(3);
        inventory2.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory2.setFileName("fileName3");
        inventory2.setFile("content3".getBytes());
        final Optional<Product> product = Optional.of(product2);
        when(mockProductRepository.findById(3)).thenReturn(product);

        when(mockProductRepository.save(any(Product.class))).thenReturn(product2);

       final List<Inventory> inventories = List.of(inventory2);
        when(mockInventoryRepository.findAll()).thenReturn(inventories);


        final List<Inventory> result = inventoryProductServiceImplUnderTest.updateProduct(3, product2);
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.get(0).getId(),inventory2.getId());

    }

    @Test
    void testUpdateProduct_ProductRepositoryFindByIdReturnsAbsent() {

        final Inventory inventory2 = new Inventory();
        final Product product2 = new Product();
        product2.setId(3);
        product2.setName("Pro name2");
        product2.setQuantity(322);
        product2.setPrice(403);
        product2.setInventory(inventory2);
        inventory2.setProducts(Set.of(product2));
        inventory2.setId(3);
        inventory2.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory2.setFileName("fileName3");
        inventory2.setFile("content3".getBytes());

        when(mockProductRepository.findById(3)).thenReturn(Optional.empty());
        when(mockProductRepository.save(any(Product.class))).thenReturn(product2);
        final List<Inventory> inventories = List.of(inventory2);
        when(mockInventoryRepository.findAll()).thenReturn(inventories);
        try{
            final List<Inventory> result = inventoryProductServiceImplUnderTest.updateProduct(3, product2);
        }catch (Exception ex){
            Assertions.assertEquals(true,ex instanceof ProductNotfoundException);
        }

    }

    @Test
    void testGetSupportedProducts() {

        final Inventory inventory2 = new Inventory();
        final Product product2 = new Product();
        product2.setId(3);
        product2.setName("Pro name2");
        product2.setQuantity(322);
        product2.setPrice(403);
        product2.setInventory(inventory2);
        inventory2.setProducts(Set.of(product2));
        inventory2.setId(3);
        inventory2.setDate(new GregorianCalendar(2021, Calendar.JANUARY, 29).getTime());
        inventory2.setFileName("fileName3");
        inventory2.setFile("content3".getBytes());
        final List<Product> products = List.of(product2);
        when(mockProductRepository.findAll()).thenReturn(products);


        final List<Product> result = inventoryProductServiceImplUnderTest.getSupportedProducts();
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.get(0).getName(),product2.getName());

    }
}
