package com.meal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meal.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
	PasswordResetToken findByToken(String token);
}
