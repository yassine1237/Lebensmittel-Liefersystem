package com.meal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meal.Exception.RestaurantException;
import com.meal.model.IngredientCategory;
import com.meal.model.IngredientsItem;
import com.meal.model.Food;
import com.meal.model.Restaurant;
import com.meal.repository.IngredientsCategoryRepository;
import com.meal.repository.IngredientsItemRepository;
import com.meal.repository.foodRepository;
@Service
public class IngredientsServiceImplementation implements IngredientsService {

	@Autowired
	private IngredientsCategoryRepository ingredientsCategoryRepo;
	
	@Autowired
	private IngredientsItemRepository ingredientsItemRepository;

	@Autowired
	private foodRepository foodRepository;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Override
	public IngredientCategory createIngredientsCategory(
			String name,Long restaurantId) throws RestaurantException {
		
		IngredientCategory isExist=ingredientsCategoryRepo
				.findByRestaurantIdAndNameIgnoreCase(restaurantId,name);
		
		if(isExist!=null) {
			return isExist;
		}

		Restaurant restaurant=restaurantService.findRestaurantById(restaurantId);
		
		IngredientCategory ingredientCategory=new IngredientCategory();
		ingredientCategory.setRestaurant(restaurant);
		ingredientCategory.setName(name);
		
		return ingredientsCategoryRepo.save(ingredientCategory);
	}
	@Override
	public void deleteIngredientCategory(Long id) throws Exception {
		IngredientCategory category = ingredientsCategoryRepo.findById(id)
				.orElseThrow(() -> new Exception("Ingredient category not found with id " + id));

		for (IngredientsItem item : category.getIngredients()) {
			deleteIngredient(item.getId());
		}

		ingredientsCategoryRepo.delete(category);
	}

	@Override
	public void deleteIngredient(Long id) throws Exception {
		IngredientsItem item = ingredientsItemRepository.findById(id)
				.orElseThrow(() -> new Exception("Ingredient not found with id " + id));

		// Remove references from the join table
		for (Food food : item.getFoods()) {
			food.getIngredients().remove(item);
			foodRepository.save(food);
		}

		ingredientsItemRepository.delete(item);
	}

	@Override
	public IngredientCategory findIngredientsCategoryById(Long id) throws Exception {
		Optional<IngredientCategory> opt=ingredientsCategoryRepo.findById(id);
		if(opt.isEmpty()){
			throw new Exception("ingredient category not found");
		}
		return opt.get();
	}

	@Override
	public List<IngredientCategory> findIngredientsCategoryByRestaurantId(Long id) throws Exception {
		return ingredientsCategoryRepo.findByRestaurantId(id);
	}

	@Override
	public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId) {

		return ingredientsItemRepository.findByRestaurantId(restaurantId);
	}
	

	@Override
	public IngredientsItem createIngredientsItem(Long restaurantId, 
			String ingredientName, Long ingredientCategoryId) throws Exception {
		
		IngredientCategory category = findIngredientsCategoryById(ingredientCategoryId);
		

		
		Restaurant restaurant=restaurantService.findRestaurantById(
				restaurantId);
		IngredientsItem item=new IngredientsItem();
		item.setName(ingredientName);
		item.setRestaurant(restaurant);
		item.setCategory(category);
		
		IngredientsItem savedIngredients = ingredientsItemRepository.save(item);
		category.getIngredients().add(savedIngredients);

		return savedIngredients;
	}


	@Override
	public IngredientsItem updateStoke(Long id) throws Exception {
		Optional<IngredientsItem> item=ingredientsItemRepository.findById(id);
		if(item.isEmpty()) {
			throw new Exception("ingredient not found with id "+item);
		}
		IngredientsItem ingredient=item.get();
		ingredient.setInStoke(!ingredient.isInStoke());
		return ingredientsItemRepository.save(ingredient);
	}


	

	

}
