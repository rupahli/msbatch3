package com.sl.ms.inventorymanagement.repo;

import com.sl.ms.inventorymanagement.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
    Page<Product> findById(Integer invId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Product b WHERE b.inventory.id = ?1")
    void deleteById(Integer invId);

}
