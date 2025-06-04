package com.meal.service;

import java.util.List;

import com.stripe.exception.StripeException;
import com.meal.Exception.CartException;
import com.meal.Exception.OrderException;
import com.meal.Exception.RestaurantException;
import com.meal.Exception.UserException;
import com.meal.model.Order;
import com.meal.model.PaymentResponse;
import com.meal.model.User;
import com.meal.request.CreateOrderRequest;

public interface OrderService {
	
	 public PaymentResponse createOrder(CreateOrderRequest order, User user) throws UserException, RestaurantException, CartException, StripeException;
	 
	 public Order updateOrder(Long orderId, String orderStatus) throws OrderException;
	 
	 public void cancelOrder(Long orderId) throws OrderException;
	 
	 public List<Order> getUserOrders(Long userId) throws OrderException;
	 
	 public List<Order> getOrdersOfRestaurant(Long restaurantId,String orderStatus) throws OrderException, RestaurantException;

	public List<Order> getRecentOrdersOfRestaurant(Long restaurantId) throws OrderException, RestaurantException;
}
