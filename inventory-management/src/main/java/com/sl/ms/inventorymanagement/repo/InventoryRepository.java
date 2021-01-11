package com.sl.ms.inventorymanagement.repo;

import com.sl.ms.inventorymanagement.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer>{

    @Override
    <S extends Inventory> List<S> saveAll(Iterable<S> iterable);
}
