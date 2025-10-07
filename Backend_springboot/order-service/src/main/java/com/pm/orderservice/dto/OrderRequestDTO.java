package com.pm.orderservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

public class OrderRequestDTO {
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequest> items;

    private String shippingAddress;
    private String billingAddress;

    @Data
    public static class OrderItemRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        private Integer quantity;
    }
}
