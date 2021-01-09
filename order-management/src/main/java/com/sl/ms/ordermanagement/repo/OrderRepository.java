package com.sl.ms.ordermanagement.repo;

import com.sl.ms.ordermanagement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer>{
}
