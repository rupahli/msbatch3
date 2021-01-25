package com.sl.ms.ordermanagement.services;

import com.sl.ms.ordermanagement.exception.OrderNotfoundException;
import com.sl.ms.ordermanagement.model.Order;
import com.sl.ms.ordermanagement.repo.ItemsRepository;
import com.sl.ms.ordermanagement.repo.OrderRepository;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final ItemsRepository itemsRepository;
    private final InventoryServiceImpl inventoryServiceImpl;

    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class.getName());

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ItemsRepository itemsRepository,InventoryServiceImpl inventoryServiceImpl) {
        this.orderRepository = orderRepository;
        this.itemsRepository = itemsRepository;
        this.inventoryServiceImpl = inventoryServiceImpl;
    }

    public Order save(Integer order_id,Order order) {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside save :  "+order_id + " Order : "+order);
        Object object = inventoryServiceImpl.checkInvProduct(order_id);
        if (object instanceof Exception){
            LOGGER.error("OrderNotfoundException to be thrown");
            throw new OrderNotfoundException();
        }
        else if(object instanceof Map) {
            LOGGER.info("Product Found saving ->");
            Order returnOrder = orderRepository.save(order);
            LOGGER.info("=========END TIME ============"+new Date());
            return returnOrder;
        }
        LOGGER.info("=========END TIME ============"+new Date());
        return null;
    }

    public Optional<Order> getProduct(Integer order_id) {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside getProduct :  "+order_id );
        Optional<Order> order = orderRepository.findById(order_id);
        LOGGER.info("=========END TIME ============"+new Date());
        return order;
    }

    public Page<Order> getAllProducts(Pageable pageable) {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.log(Level.INFO,"CLOUD Sleuth");
        LOGGER.info("Inside getAllProducts :  "+pageable );
        Page<Order> order = orderRepository.findAll(pageable);
        LOGGER.info("=========END TIME ============"+new Date());
        return order;
    }


    public void deleteOrder(Order order) {
        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside deleteOrder :  "+order);
        itemsRepository.deleteByOrderId(order.getId());
        orderRepository.delete(order);
        LOGGER.info("=========END TIME ============"+new Date());
        LOGGER.info("Records Deleted");
    }

    public Object saveOrder(Integer order_id, Order order) {

        LOGGER.info("=========START TIME ============"+new Date());
        LOGGER.info("Inside saveOrder :  "+order_id + " Order : "+order);
        Object object = inventoryServiceImpl.checkInvProduct(order_id);

        LOGGER.info("=========END TIME ============"+new Date());
        return object;
    }
}
