package com.sl.ms.inventorymanagement.repo;

import com.sl.ms.inventorymanagement.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer>{

    @Override
    <S extends Inventory> List<S> saveAll(Iterable<S> iterable);
}
