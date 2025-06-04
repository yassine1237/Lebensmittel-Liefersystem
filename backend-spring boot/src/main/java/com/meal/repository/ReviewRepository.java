package com.meal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meal.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
