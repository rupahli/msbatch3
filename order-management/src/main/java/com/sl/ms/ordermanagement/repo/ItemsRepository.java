package com.sl.ms.ordermanagement.repo;

import com.sl.ms.ordermanagement.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ItemsRepository extends JpaRepository<Item, Integer>{
    Page<Item> findByOrderId(Integer orderId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Item b WHERE b.order.id = ?1")
    void deleteByOrderId(Integer orderId);
}
