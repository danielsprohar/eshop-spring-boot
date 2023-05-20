package dev.sprohar.eshopinventoryservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.sprohar.eshopinventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findBySku(Long sku);

    List<Inventory> findBySkuIn(Long[] skus);

}
