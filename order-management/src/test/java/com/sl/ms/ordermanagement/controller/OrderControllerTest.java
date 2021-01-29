package com.sl.ms.ordermanagement.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.ms.ordermanagement.model.Item;
import com.sl.ms.ordermanagement.model.Order;
import com.sl.ms.ordermanagement.services.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Mock
    private OrderServiceImpl mockOrderService;

    @Autowired
    WebApplicationContext context;

    @Autowired
    private OrderController orderControllerUnderTest;

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
        orderControllerUnderTest = new OrderController(mockOrderService);
        headers.add("Authorization", token);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGetByOrderId() {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/dev/orders/61"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\"id\":61,\"name\":\"first order\",\"amount\":400.0,\"items\":[{\"id\":61,\"name\":\"book\",\"quantity\":2,\"price\":100.0,\"amount\":200.0}]}";

//        Assertions.assertEquals(expected, response.getBody());

        try {
           mockMvc.perform(MockMvcRequestBuilders.get("/orders/61"))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            mockMvc.perform(MockMvcRequestBuilders.get("/orders/61").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").isString())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.items").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name").isString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    void testGetById_OrderServiceImplReturnsAbsent() {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        try {

            MvcResult result = mockMvc
                    .perform(MockMvcRequestBuilders.get("/orders/1").contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn();

            Assertions.assertEquals(result.getResponse().getContentAsString(),"Order not found in database");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetAllOrders() {
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);


        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/orders/"))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            MvcResult result = mockMvc
                    .perform(MockMvcRequestBuilders.get("/orders").contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void testCreate() {

        final Order order1 = new Order();
        order1.setAmount(0.0);
        order1.setId(0);
        order1.setName("name");
        final Item item1 = new Item();
        item1.setId(0);
        item1.setName("name");
        item1.setOrder(new Order());
        item1.setQuantity(0);
        item1.setPrice(0.0);
        item1.setAmount(0.0);
        order1.setItems(Set.of(item1));
        when(mockOrderService.save(eq(0), any(Order.class))).thenReturn(order1);

        final ResponseEntity<Order> result = orderControllerUnderTest.create(0, order1);

        Assertions.assertEquals(result.getBody().getId(),0);

    }

    @Test
    void testCreateOrder() {
        // Setup
        final Order order = new Order();
        order.setAmount(0.0);
        order.setId(0);
        order.setName("name");
        final Item item = new Item();
        item.setId(0);
        item.setName("name");
        item.setOrder(new Order());
        item.setQuantity(0);
        item.setPrice(0.0);
        item.setAmount(0.0);
        order.setItems(Set.of(item));

        when(mockOrderService.saveOrder(eq(0), any(Order.class))).thenReturn("Success");

        final Object result = orderControllerUnderTest.createOrder(0, order);

        // Verify the results
    }

    @Test
    void testDelete() {
        // Setup

        // Configure OrderServiceImpl.getProduct(...).
        final Order order1 = new Order();
        order1.setAmount(0.0);
        order1.setId(0);
        order1.setName("name");
        final Item item = new Item();
        item.setId(0);
        item.setName("name");
        item.setOrder(new Order());
        item.setQuantity(0);
        item.setPrice(0.0);
        item.setAmount(0.0);
        order1.setItems(Set.of(item));
        final Optional<Order> order = Optional.of(order1);
        when(mockOrderService.getProduct(0)).thenReturn(order);

        // Run the test
        final ResponseEntity<Order> result = orderControllerUnderTest.delete(0);

        // Verify the results
        verify(mockOrderService).deleteOrder(any(Order.class));
    }

    @Test
    void testDeleteLibraryInTransaction() {
        // Setup
        final Order order = new Order();
        order.setAmount(0.0);
        order.setId(0);
        order.setName("name");
        final Item item = new Item();
        item.setId(0);
        item.setName("name");
        item.setOrder(new Order());
        item.setQuantity(0);
        item.setPrice(0.0);
        item.setAmount(0.0);
        order.setItems(Set.of(item));

        // Run the test
        orderControllerUnderTest.deleteLibraryInTransaction(order);

        // Verify the results
        verify(mockOrderService).deleteOrder(any(Order.class));
    }


}
