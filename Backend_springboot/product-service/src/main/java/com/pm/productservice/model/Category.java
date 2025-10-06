package com.pm.productservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // 可以添加更多字段
    private Integer sortOrder = 0;
    private Boolean active = true;


}