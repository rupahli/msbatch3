package com.sl.ms.ordermanagement.services;

import com.sl.ms.ordermanagement.exception.OrderNotfoundException;
import com.sl.ms.ordermanagement.model.Order;
import com.sl.ms.ordermanagement.repo.ItemsRepository;
import com.sl.ms.ordermanagement.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final ItemsRepository itemsRepository;
    private final InventoryServiceImpl inventoryServiceImpl;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ItemsRepository itemsRepository,InventoryServiceImpl inventoryServiceImpl) {
        this.orderRepository = orderRepository;
        this.itemsRepository = itemsRepository;
        this.inventoryServiceImpl = inventoryServiceImpl;
    }

    public Order save(Order order) {

        Object object = inventoryServiceImpl.checkInvProduct(order.getId());
        if (object instanceof Exception)
            throw new OrderNotfoundException();
        else if(object instanceof Map) {
            return orderRepository.save(order);
        }
        return null;
    }

    public Optional<Order> getProduct(Integer order_id) {
        return orderRepository.findById(order_id);
    }

    public Page<Order> getAllProducts(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }


    public void deleteOrder(Order order) {
        itemsRepository.deleteByOrderId(order.getId());
        orderRepository.delete(order);
    }
}
