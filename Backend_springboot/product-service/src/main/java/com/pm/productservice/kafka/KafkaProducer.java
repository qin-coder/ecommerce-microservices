package com.pm.productservice.kafka;

import com.pm.productservice.events.ProductEvents;
import com.pm.productservice.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void sendProductCreated(Product product) {
        String createdAt = product.getCreatedAt() != null ?
                product.getCreatedAt().format(formatter) :
                LocalDateTime.now().format(formatter);

        ProductEvents.ProductCreatedEvent event = ProductEvents.ProductCreatedEvent.newBuilder()
                .setProductId(product.getId().toString())
                .setName(product.getName())
                .setDescription(product.getDescription() != null ? product.getDescription() : "")
                .setPrice(product.getPrice().toString())
                .setCategory(product.getCategory())
                .setStockQuantity(product.getStockQuantity())
                .setCreatedAt(createdAt)
                .setEventType("PRODUCT_CREATED")
                .build();

        try {
            kafkaTemplate.send("product.events", product.getId().toString(), event.toByteArray());
            log.info("Product created event sent: {}", product.getId());
        } catch (Exception e) {
            log.error("Error sending product created event: {}", e.getMessage());
        }
    }

    public void sendProductUpdated(Product oldProduct, Product newProduct) {

        String newUpdatedAt = newProduct.getUpdatedAt() != null ?
                newProduct.getUpdatedAt().format(formatter) :
                LocalDateTime.now().format(formatter);

        ProductEvents.ProductUpdatedEvent event = ProductEvents.ProductUpdatedEvent.newBuilder()
                .setProductId(newProduct.getId().toString())
                .setName(newProduct.getName())
                .setDescription(newProduct.getDescription() != null ? newProduct.getDescription() : "")
                .setOldPrice(oldProduct.getPrice().toString())
                .setNewPrice(newProduct.getPrice().toString())
                .setCategory(newProduct.getCategory())
                .setStockQuantity(newProduct.getStockQuantity())
                .setUpdatedAt(newUpdatedAt)
                .setEventType("PRODUCT_UPDATED")
                .build();

        try {
            kafkaTemplate.send("product.events", newProduct.getId().toString(), event.toByteArray());
            log.info("Product updated event sent: {}", newProduct.getId());
        } catch (Exception e) {
            log.error("Error sending product updated event: {}", e.getMessage());
        }
    }

    public void sendProductDeleted(String productId) {
        ProductEvents.ProductDeletedEvent event = ProductEvents.ProductDeletedEvent.newBuilder()
                .setProductId(productId)
                .setDeletedAt(java.time.LocalDateTime.now().format(formatter))
                .setEventType("PRODUCT_DELETED")
                .build();

        try {
            kafkaTemplate.send("product.events", productId, event.toByteArray());
            log.info("Product deleted event sent: {}", productId);
        } catch (Exception e) {
            log.error("Error sending product deleted event: {}", e.getMessage());
        }
    }
}