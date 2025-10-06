// CategoryResponseDTO.java
package com.pm.productservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * returns a category
 */
@Data
public class CategoryResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Long productCount;
}