package com.aruiz.microservices.order_services.repository;

import com.aruiz.microservices.order_services.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
