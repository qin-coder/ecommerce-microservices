package com.pm.productservice.repository;

import com.pm.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByName(String name);
    List<Product> findByCategory(String category);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    boolean existsByName(String name);
    boolean existsByCategory(String category);

    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByDescriptionContainingIgnoreCase(String keyword);

    Page<Product> findAll(Pageable pageable);
    Page<Product> findByCategory(String category, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> searchProducts(
            @Param("name") String name,
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );


    List<Product> findByStockQuantityLessThan(Integer quantity);
    List<Product> findByStockQuantityGreaterThanEqual(Integer quantity);


    @Query("SELECT COUNT(p) FROM Product p WHERE p.category = :category")
    Long countByCategory(@Param("category") String category);

    @Query("SELECT p.category, COUNT(p) FROM Product p GROUP BY p.category")
    List<Object[]> countProductsByCategory();


    @Query("SELECT AVG(p.price) FROM Product p WHERE p.category = :category")
    Double findAveragePriceByCategory(@Param("category") String category);
}