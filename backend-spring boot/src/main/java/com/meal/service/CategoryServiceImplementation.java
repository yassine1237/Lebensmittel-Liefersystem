package com.meal.service;

import java.util.List;
import java.util.Optional;

import com.meal.repository.foodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meal.Exception.RestaurantException;
import com.meal.model.Category;
import com.meal.model.Restaurant;
import com.meal.repository.CategoryRepository;

@Service
public class CategoryServiceImplementation implements CategoryService {

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private foodRepository foodRepository;

	@Override
	public Category createCategory(String name,Long userId) throws RestaurantException {
		Restaurant restaurant=restaurantService.getRestaurantsByUserId(userId);
		Category createdCategory=new Category();

		createdCategory.setName(name);
		createdCategory.setRestaurant(restaurant);
		return categoryRepository.save(createdCategory);
	}

	@Override
	public List<Category> findCategoryByRestaurantId(Long id) throws RestaurantException {
		Restaurant restaurant=restaurantService.findRestaurantById(id);
		return categoryRepository.findByRestaurantId(id);
	}

	@Override
	public Category findCategoryById(Long id) throws RestaurantException {
		if (id == null) {
			throw new IllegalArgumentException("The given id must not be null");
		}
		Optional<Category> opt = categoryRepository.findById(id);

		if (opt.isEmpty()) {
			throw new RestaurantException("category not exist with id " + id);
		}

		return opt.get();
	}

	@Override
	public void deleteCategory(Long id) throws RestaurantException {
		Category category = findCategoryById(id);
		if (category == null) {
			throw new RestaurantException("category not found with id " + id);
		}
		categoryRepository.delete(category);
	}
}
