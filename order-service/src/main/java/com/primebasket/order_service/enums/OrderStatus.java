package com.primebasket.order_service.enums;

public enum OrderStatus {
    PENDING,
    CONFIRMED,           //Payment successful and order confirmed
    PROCESSING,         //Order is being prepared
    SHIPPED,
    DELIVERED,
    CANCELLED,
    FAILED
}
