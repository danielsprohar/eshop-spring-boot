package dev.sprohar.eshoporderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.sprohar.eshoporderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
