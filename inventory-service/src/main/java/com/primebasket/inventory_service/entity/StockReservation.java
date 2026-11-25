package com.primebasket.inventory_service.entity;

import com.primebasket.inventory_service.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_reservation",
uniqueConstraints = {
        @UniqueConstraint(name = "uk_checkout_product",
        columnNames = {"checkout_id", "product_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(name="checkout_id", nullable = false)
    private String checkoutId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public void onCreate(){
        this.createdAt=LocalDateTime.now();
    }


}
