package com.pm.orderservice.repository;

import com.pm.orderservice.model.OrderStatus;
import com.pm.orderservice.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findByOrderNumber(String orderNumber);

    List<Orders> findByUserId(Long userId);

    List<Orders> findByStatus(OrderStatus status);

    @Query("SELECT o FROM Orders o WHERE o.userId = :userId AND o" +
            ".status IN :statuses")
    List<Orders> findByUserIdAndStatusIn(@Param("userId") Long userId,
                                         @Param("statuses") List<OrderStatus> statuses);

    boolean existsByOrderNumber(String orderNumber);
}