package com.meal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meal.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


//    CartItem findByFoodIsContaining

}
