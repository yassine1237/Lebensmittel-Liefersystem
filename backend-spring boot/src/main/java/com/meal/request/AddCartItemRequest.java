package com.meal.request;

import java.util.List;
import lombok.Data;

@Data
public class AddCartItemRequest {

	private Long menuItemId;
	private int quantity;
	private List<String> ingredients;
	private Long price; // Add price field to calculate the total

	// Assuming the price is set when the item is added to the cart
}
