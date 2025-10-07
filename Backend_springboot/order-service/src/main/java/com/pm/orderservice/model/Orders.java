package com.pm.orderservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String orderNumber;

    @NotNull
    private Long userId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    @NotNull
    private BigDecimal totalAmount;

    private String shippingAddress;
    private String billingAddress;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        this.orderNumber = generateOrderNumber();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000);
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}