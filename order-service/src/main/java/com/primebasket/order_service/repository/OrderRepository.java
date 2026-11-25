package com.primebasket.order_service.repository;

import com.primebasket.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long > {

}
