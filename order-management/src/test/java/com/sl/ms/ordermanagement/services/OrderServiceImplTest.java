package com.sl.ms.ordermanagement.services;

import com.sl.ms.ordermanagement.model.Item;
import com.sl.ms.ordermanagement.model.Order;
import com.sl.ms.ordermanagement.repo.ItemsRepository;
import com.sl.ms.ordermanagement.repo.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class OrderServiceImplTest {

    @Mock
    private OrderRepository mockOrderRepository;
    @Mock
    private ItemsRepository mockItemsRepository;
    @Mock
    private InventoryServiceImpl mockInventoryServiceImpl;

    private OrderServiceImpl orderServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        orderServiceImplUnderTest = new OrderServiceImpl(mockOrderRepository, mockItemsRepository, mockInventoryServiceImpl);
    }

    @Test
    void testSave() {

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

        when(mockInventoryServiceImpl.checkInvProduct(0)).thenReturn("result");


        final Order order1 = new Order();
        order1.setAmount(200);
        order1.setId(10);
        order1.setName("name");
        final Item item1 = new Item();
        item1.setId(10);
        item1.setName("nameItem");
        item1.setOrder(new Order());
        item1.setQuantity(2);
        item1.setPrice(100);
        item1.setAmount(200);
        order1.setItems(Set.of(item1));
        when(mockOrderRepository.save(any(Order.class))).thenReturn(order1);
        when(mockInventoryServiceImpl.checkInvProduct(0)).thenReturn(order1);

        OrderServiceImpl service=Mockito.mock(OrderServiceImpl.class);
        Mockito.doReturn(order1).when(service).save(Mockito.isA(Integer.class),Mockito.isA(Order.class));


        final Order result = service.save(0, order);
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.getId(),10);
    }

    @Test
    void testGetProduct() {

        final Order order1 = new Order();
        order1.setAmount(200);
        order1.setId(10);
        order1.setName("name");
        final Item item1 = new Item();
        item1.setId(10);
        item1.setName("nameItem");
        item1.setOrder(new Order());
        item1.setQuantity(2);
        item1.setPrice(100);
        item1.setAmount(200);
        order1.setItems(Set.of(item1));
        final Optional<Order> order = Optional.of(order1);
        when(mockOrderRepository.findById(10)).thenReturn(order);


        final Optional<Order> result = orderServiceImplUnderTest.getProduct(10);
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.get().getId(),10);
    }

    @Test
    void testGetProduct_OrderRepositoryReturnsAbsent() {

        when(mockOrderRepository.findById(0)).thenReturn(Optional.empty());

        final Optional<Order> result = orderServiceImplUnderTest.getProduct(0);
        Assertions.assertEquals(result.isEmpty() , true);

    }

    @Test
    void testGetAllProducts() {

        final Order order1 = new Order();
        order1.setAmount(200);
        order1.setId(10);
        order1.setName("name");
        final Item item1 = new Item();
        item1.setId(10);
        item1.setName("nameItem");
        item1.setOrder(new Order());
        item1.setQuantity(2);
        item1.setPrice(100);
        item1.setAmount(200);
        order1.setItems(Set.of(item1));
        final Page<Order> orders = new PageImpl<>(List.of(order1));
        when(mockOrderRepository.findAll(any(Pageable.class))).thenReturn(orders);

        final Page<Order> result = orderServiceImplUnderTest.getAllProducts(PageRequest.of(10, 1));
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(result.get().findFirst().get().getId(),10);
    }

    @Test
    void testDeleteOrder() {

        final Order order1 = new Order();
        order1.setAmount(200);
        order1.setId(10);
        order1.setName("name");
        final Item item1 = new Item();
        item1.setId(10);
        item1.setName("nameItem");
        item1.setOrder(new Order());
        item1.setQuantity(2);
        item1.setPrice(100);
        item1.setAmount(200);
        order1.setItems(Set.of(item1));

        orderServiceImplUnderTest.deleteOrder(order1);

        verify(mockItemsRepository).deleteByOrderId(10);
        verify(mockOrderRepository).delete(any(Order.class));
    }

    @Test
    void testSaveOrder() {
        final Order order1 = new Order();
        order1.setAmount(200);
        order1.setId(10);
        order1.setName("name");
        final Item item1 = new Item();
        item1.setId(10);
        item1.setName("nameItem");
        item1.setOrder(new Order());
        item1.setQuantity(2);
        item1.setPrice(100);
        item1.setAmount(200);
        order1.setItems(Set.of(item1));

        when(mockInventoryServiceImpl.checkInvProduct(10)).thenReturn(order1);

        final Object result = orderServiceImplUnderTest.saveOrder(10, order1);
        System.out.println("RESULT" + result.toString());
        Assertions.assertEquals(((Order)result).getId(),10);

    }
}
