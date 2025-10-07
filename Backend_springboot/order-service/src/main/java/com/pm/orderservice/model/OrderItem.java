package com.pm.orderservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private String productName;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;

    private BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders order;

    @PrePersist
    @PreUpdate
    protected void calculateSubtotal() {
        this.subtotal =
                unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}