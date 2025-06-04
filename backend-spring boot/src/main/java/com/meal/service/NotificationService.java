package com.meal.service;

import java.util.List;

import com.meal.model.Notification;
import com.meal.model.Order;
import com.meal.model.Restaurant;
import com.meal.model.User;

public interface NotificationService {
	
	public Notification sendOrderStatusNotification(Order order);
	public void sendRestaurantNotification(Restaurant restaurant, String message);
	public void sendPromotionalNotification(User user, String message);
	
	public List<Notification> findUsersNotification(Long userId);

}
