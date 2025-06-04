package com.meal.request;

import com.meal.model.Address;
import java.util.List;
import lombok.Data;

@Data
public class CreateOrderRequest {

	private Long restaurantId;

	private Address deliveryAddress;

	private List<AddCartItemRequest> items; // List of items

	private Long totalAmount; // Total amount for the order

	// Method to calculate the total amount
	public Long getTotalAmount() {
		if (items == null || items.isEmpty()) {
			return 0L; // Return 0 or throw an exception if items are mandatory
		}
		long total = 0;
		for (AddCartItemRequest item : items) {
			total += item.getQuantity() * item.getPrice();
		}
		total += 3; // Add the fixed charge
		return total;
	}
}
