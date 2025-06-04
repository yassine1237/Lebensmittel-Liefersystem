package com.meal.controller;

import java.util.List;

import com.meal.model.Category;
import com.meal.model.Restaurant;
import com.meal.service.CategoryService;
import com.meal.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meal.Exception.FoodException;
import com.meal.Exception.RestaurantException;
import com.meal.Exception.UserException;
import com.meal.model.Food;
import com.meal.model.User;
import com.meal.request.CreateFoodRequest;
import com.meal.service.FoodService;
import com.meal.service.UserService;

@RestController
@RequestMapping("/api/admin/food")
public class AdminMenuItemController {

	@Autowired
	private FoodService menuItemService;

	@Autowired
	private UserService userService;

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private CategoryService categoryService;

	@PostMapping()
	public ResponseEntity<Food> createItem(
			@RequestHeader("Authorization") String jwt,
			@RequestBody CreateFoodRequest createFoodRequest) throws RestaurantException, UserException, FoodException {

		User user = userService.findUserProfileByJwt(jwt);
		Restaurant restaurant = restaurantService.findRestaurantById(createFoodRequest.getRestaurantId());
		Category category = categoryService.findCategoryById(createFoodRequest.getCategoryId());

		Food createdFood = menuItemService.createFood(createFoodRequest, category, restaurant);
		return new ResponseEntity<>(createdFood, HttpStatus.CREATED);
	}
	@GetMapping("/restaurant/{id}")
	public ResponseEntity<List<Food>> getRestaurantsFood(
			@PathVariable Long id,
			@RequestParam(required = false, defaultValue = "false") boolean isVegetarian,
			@RequestParam(required = false, defaultValue = "false") boolean isNonveg,
			@RequestParam(required = false, defaultValue = "false") boolean isSeasonal,
			@RequestParam(required = false) String foodCategory) throws FoodException {

		List<Food> foods = menuItemService.getRestaurantsFood(id, isVegetarian, isNonveg, isSeasonal, foodCategory);
		return new ResponseEntity<>(foods, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteItem(@PathVariable Long id, @RequestHeader("Authorization") String jwt)
			throws UserException, FoodException {
		User user = userService.findUserProfileByJwt(jwt);
		menuItemService.deleteFood(id);
		return ResponseEntity.ok("Menu item deleted");
	}

	@GetMapping("/search")
	public ResponseEntity<List<Food>> getMenuItemByName(@RequestParam String name) {
		List<Food> menuItem = menuItemService.searchFood(name);
		return ResponseEntity.ok(menuItem);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Food> updateAvailibilityStatus(@PathVariable Long id) throws FoodException {
		Food menuItems = menuItemService.updateAvailibilityStatus(id);
		return ResponseEntity.ok(menuItems);
	}
}
