package com.pm.productservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * return data to the client
 */
@Data
public class ProductResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getFormattedPrice() {
        return "$" + price.toString();
    }

    public boolean isInStock() {
        return stockQuantity != null && stockQuantity > 0;
    }
}