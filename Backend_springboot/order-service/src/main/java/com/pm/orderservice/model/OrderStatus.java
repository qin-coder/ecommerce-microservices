package com.pm.orderservice.model;

public enum OrderStatus {
    CREATED,
    PENDING_PAYMENT,
    PAID,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    COMPLETED,
    CANCELLED,
    REFUNDED
}