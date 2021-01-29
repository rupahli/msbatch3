package com.sl.ms.ordermanagement.repo;

import com.sl.ms.ordermanagement.model.Item;
import com.sl.ms.ordermanagement.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderRepoTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void contextLoads() {
    }

    @BeforeEach
    public void save() {

        final Order order = new Order();
        order.setAmount(400);
        order.setId(1);
        order.setName("first order");
        final Item item = new Item();
        item.setId(1);
        item.setName("book");
        item.setOrder(order);
        item.setQuantity(2);
        item.setPrice(100);
        item.setAmount(200);
        order.setItems(Set.of(item));

        orderRepository.save(order);

        final Order order1 = new Order();
        order.setAmount(100);
        order.setId(2);
        order.setName("second order");
        final Item item1 = new Item();
        item1.setId(2);
        item1.setName("pen");
        item1.setOrder(order1);
        item1.setQuantity(5);
        item1.setPrice(20);
        item1.setAmount(100);
        order1.setItems(Set.of(item1));

        orderRepository.save(order);
    }

    @Test
    public void findAll() {

        List<Order> result=  orderRepository.findAll();
        Assertions.assertNotNull(result);
    }

    @Test
    public void findById() {
        Optional<Order> result = orderRepository.findById(61);
        System.out.println(result.get().getId());
        Assertions.assertNotNull(result);
    }

}
