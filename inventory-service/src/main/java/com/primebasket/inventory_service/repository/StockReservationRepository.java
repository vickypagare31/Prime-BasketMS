package com.primebasket.inventory_service.repository;

import com.primebasket.inventory_service.entity.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockReservationRepository extends JpaRepository<StockReservation, Long> {

    Optional<StockReservation> findByCheckoutIdAndProductId(String checkoutId, Long productId);
}
