package com.meal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meal.Exception.FoodException;
import com.meal.Exception.RestaurantException;
import com.meal.model.Category;
import com.meal.model.IngredientsItem;
import com.meal.model.Food;
import com.meal.model.Restaurant;
import com.meal.repository.IngredientsItemRepository;
import com.meal.repository.foodRepository;
import com.meal.request.CreateFoodRequest;

@Service
public class FoodServiceImplementation implements FoodService {

	@Autowired
	private foodRepository foodRepository;

	@Autowired
	private IngredientsItemRepository ingredientsItemRepository;

	@Autowired
	private IngredientsService ingredientService;

	@Override
	public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant)
			throws FoodException, RestaurantException {

		Food food = new Food();
		food.setFoodCategory(category);
		food.setCreationDate(new Date());
		food.setDescription(req.getDescription());
		food.setImages(req.getImages());
		food.setName(req.getName());
		food.setPrice(req.getPrice());
		food.setSeasonal(req.isSeasonal());
		food.setVegetarian(req.isVegetarian());
		food.setRestaurant(restaurant);

		// Fetch ingredients by IDs
		List<IngredientsItem> ingredients = ingredientsItemRepository.findAllById(req.getIngredientIds());
		food.setIngredients(ingredients);

		food = foodRepository.save(food);

		restaurant.getFoods().add(food);
		return food;
	}


	@Override
	public void deleteFood(Long foodId) throws FoodException {
		Food food = findFoodById(foodId);
		food.setRestaurant(null);
		foodRepository.delete(food);
	}

	@Override
	public List<Food> getRestaurantsFood(Long restaurantId, boolean isVegetarian, boolean isNonveg, boolean isSeasonal, String foodCategory) throws FoodException {
		List<Food> foods = foodRepository.findByRestaurantId(restaurantId);

		if (isVegetarian) {
			foods = filterByVegetarian(foods, isVegetarian);
		}
		if (isNonveg) {
			foods = filterByNonveg(foods, isNonveg);
		}
		if (isSeasonal) {
			foods = filterBySeasonal(foods, isSeasonal);
		}
		if (foodCategory != null && !foodCategory.equals("")) {
			foods = filterByFoodCategory(foods, foodCategory);
		}

		return foods;
	}

	private List<Food> filterByVegetarian(List<Food> foods, boolean isVegetarian) {
		return foods.stream()
				.filter(food -> food.isVegetarian() == isVegetarian)
				.collect(Collectors.toList());
	}

	private List<Food> filterByNonveg(List<Food> foods, boolean isNonveg) {
		return foods.stream()
				.filter(food -> !food.isVegetarian())
				.collect(Collectors.toList());
	}

	private List<Food> filterBySeasonal(List<Food> foods, boolean isSeasonal) {
		return foods.stream()
				.filter(food -> food.isSeasonal() == isSeasonal)
				.collect(Collectors.toList());
	}

	private List<Food> filterByFoodCategory(List<Food> foods, String foodCategory) {
		return foods.stream()
				.filter(food -> food.getFoodCategory() != null && food.getFoodCategory().getName().equals(foodCategory))
				.collect(Collectors.toList());
	}

	@Override
	public List<Food> searchFood(String keyword) {
		List<Food> items = new ArrayList<>();

		if (!keyword.isEmpty()) {
			System.out.println("keyword -- " + keyword);
			items = foodRepository.searchByNameOrCategory(keyword);
		}

		return items;
	}

	@Override
	public Food updateAvailibilityStatus(Long id) throws FoodException {
		Food food = findFoodById(id);

		food.setAvailable(!food.isAvailable());
		foodRepository.save(food);
		return food;
	}

	@Override
	public Food findFoodById(Long foodId) throws FoodException {
		Optional<Food> food = foodRepository.findById(foodId);
		if (food.isPresent()) {
			return food.get();
		}
		throw new FoodException("Food with id " + foodId + " not found");
	}
}
