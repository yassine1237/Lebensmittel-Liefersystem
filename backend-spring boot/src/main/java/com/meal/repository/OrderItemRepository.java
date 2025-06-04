package com.meal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meal.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
