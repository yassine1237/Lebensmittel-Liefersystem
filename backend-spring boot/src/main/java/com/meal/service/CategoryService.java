package com.meal.service;

import java.util.List;

import com.meal.Exception.RestaurantException;
import com.meal.model.Category;

public interface CategoryService {
	
	public Category createCategory (String name,Long userId) throws RestaurantException;
	public List<Category> findCategoryByRestaurantId(Long restaurantId) throws RestaurantException;
	public Category findCategoryById(Long id) throws RestaurantException;
	void deleteCategory(Long id) throws RestaurantException;

}
