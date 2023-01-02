package com.t6.lockhood.repository;

import com.t6.lockhood.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}
