package com.sl.ms.inventorymanagement.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.sl.ms.inventorymanagement.exception.ProductNotfoundException;
import com.sl.ms.inventorymanagement.model.Inventory;
import com.sl.ms.inventorymanagement.model.Product;
import com.sl.ms.inventorymanagement.services.InventoryProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Mock
    private InventoryProductServiceImpl mockService;

    @Autowired
    WebApplicationContext context;

    @Autowired
    private ProductController productControllerUnderTest;

    TestRestTemplate restTemplate = new TestRestTemplate();

    private int port = 8889;

    HttpHeaders headers = new HttpHeaders();

    private MockMvc mockMvc;

    private String token="Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJzb2Z0dGVrSldUIiwic3ViIjoicnVwYWhsaSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MTE5Mjg0MDQsImV4cCI6MTYxMTkyOTAwNH0.jzfQZKwPIid_GP53ep_Lk5cjyCw519DpzlWQbeZs9T2j957vLP1vXQe8f46DXHJBu_PNV_Byu0fTuSed2tMRdw";

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        initMocks(this);
        productControllerUnderTest = new ProductController(mockService);
        headers.add("Authorization", token);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testGetById() throws Exception {

        try {
            final MockHttpServletResponse response = mockMvc.perform(get("/products/{product_id}", 61))
                    .andReturn().getResponse();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSupportedProducts() throws Exception {
        final MockHttpServletResponse response = mockMvc.perform(get("/supported")
               )
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testGetAll() throws Exception {
        final MockHttpServletResponse response = mockMvc.perform(get("/products"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void testDeleteLibraryInTransaction() {
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

       productControllerUnderTest.deleteLibraryInTransaction(inventory2);

       verify(mockService).deleteInv(any(Inventory.class));
    }

    @Test
    public void testDelete() {
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
        when(mockService.getInv(3)).thenReturn(inventory);
        final ResponseEntity<Inventory> result = productControllerUnderTest.delete(3);
        verify(mockService).deleteInv(inventory2);
    }

    @Test
    public void testDelete_InventoryProductServiceImplGetInvReturnsAbsent() {

        when(mockService.getInv(99)).thenReturn(Optional.empty());

        try{
            final ResponseEntity<Inventory> result = productControllerUnderTest.delete(99);
        }catch (Exception ex){
            Assertions.assertEquals(true,ex instanceof ProductNotfoundException);
        }


    }

    @Test
    public void testUpdateProduct() {
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
        when(mockService.updateProduct(3, product2)).thenReturn(inventories);


        final ResponseEntity<List<Inventory>> result = productControllerUnderTest.updateProduct(3, product2);
        System.out.println("RESULT" + result.toString());
       Assertions.assertEquals(result.getBody().get(0).getId(),inventory2.getId());
    }


    @Test
    public void testAddProductInInv() {
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
        when(mockService.addProductInInv(3, product2)).thenReturn(inventories);


        final ResponseEntity<List<Inventory>> result = productControllerUnderTest.addProductInInv(3, product2);
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.getBody().get(0).getId(),inventory2.getId());
    }

    @Test
    public void testCreateProduct() {
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
        when(mockService.save(inventory2)).thenReturn(inventories);


        final ResponseEntity<List<Inventory>> result = productControllerUnderTest.createProduct(inventory2);
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.getBody().get(0).getId(),inventory2.getId());
    }

}
