package com.meal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.meal.Exception.CartException;
import com.meal.Exception.OrderException;
import com.meal.Exception.RestaurantException;
import com.meal.Exception.UserException;
import com.meal.model.Address;
import com.meal.model.Cart;
import com.meal.model.CartItem;
import com.meal.model.Notification;
import com.meal.model.Order;
import com.meal.model.OrderItem;
import com.meal.model.PaymentResponse;
import com.meal.model.Restaurant;
import com.meal.model.User;
import com.meal.repository.AddressRepository;
import com.meal.repository.OrderItemRepository;
import com.meal.repository.OrderRepository;
import com.meal.repository.RestaurantRepository;
import com.meal.repository.UserRepository;
import com.meal.request.CreateOrderRequest;
@Service
public class OrderServiceImplementation implements OrderService {
	
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private CartSerive cartService;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PaymentService paymentSerive;
	
	@Autowired
	private NotificationService notificationService;




	@Override
	public PaymentResponse createOrder(CreateOrderRequest order, User user) throws UserException, RestaurantException, CartException, StripeException {

		Address shippingAddress = order.getDeliveryAddress();

		// Find the address in the user's existing addresses, if it exists
		Optional<Address> existingAddressOpt = user.getAddresses().stream()
				.filter(existingAddress -> existingAddress.equals(shippingAddress))
				.findFirst();

		Address finalAddress;

		if (existingAddressOpt.isPresent()) {
			// If the address exists, use the existing one
			finalAddress = existingAddressOpt.get();
		} else {
			// Otherwise, save the new address and add it to the user's addresses
			finalAddress = addressRepository.save(shippingAddress);
			user.getAddresses().add(finalAddress);
		}

		System.out.println("user addresses --------------  " + user.getAddresses());

		userRepository.save(user);

		Optional<Restaurant> restaurant = restaurantRepository.findById(order.getRestaurantId());
		if (restaurant.isEmpty()) {
			throw new RestaurantException("Restaurant not found with id " + order.getRestaurantId());
		}

		Order createdOrder = new Order();

		createdOrder.setCustomer(user);
		createdOrder.setDeliveryAddress(finalAddress); // Use the final address
		createdOrder.setCreatedAt(new Date());
		createdOrder.setOrderStatus("PENDING");
		createdOrder.setRestaurant(restaurant.get());

		Cart cart = cartService.findCartByUserId(user.getId());

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem cartItem : cart.getItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setFood(cartItem.getFood());
			orderItem.setIngredients(cartItem.getIngredients());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setTotalPrice(cartItem.getFood().getPrice() * cartItem.getQuantity());

			OrderItem savedOrderItem = orderItemRepository.save(orderItem);
			orderItems.add(savedOrderItem);
		}

		Long totalAmount = order.getTotalAmount();
		createdOrder.setTotalAmount(totalAmount);
		createdOrder.setRestaurant(restaurant.get());

		createdOrder.setItems(orderItems);
		Order savedOrder = orderRepository.save(createdOrder);

		restaurant.get().getOrders().add(savedOrder);

		restaurantRepository.save(restaurant.get());

		PaymentResponse res = paymentSerive.generatePaymentLink(savedOrder);
		return res;
	}


	@Override
	public void cancelOrder(Long orderId) throws OrderException {
           Order order =findOrderById(orderId);
           if(order==null) {
        	   throw new OrderException("Order not found with the id "+orderId);
           }
		
		    orderRepository.deleteById(orderId);
		
	}
	
	public Order findOrderById(Long orderId) throws OrderException {
		Optional<Order> order = orderRepository.findById(orderId);
		if(order.isPresent()) return order.get();
		
		throw new OrderException("Order not found with the id "+orderId);
	}

	@Override
	public List<Order> getUserOrders(Long userId) throws OrderException {
		List<Order> orders=orderRepository.findAllUserOrders(userId);
		return orders;
	} 

	@Override
	public List<Order> getOrdersOfRestaurant(Long restaurantId,String orderStatus) throws OrderException, RestaurantException {
		
			List<Order> orders = orderRepository.findOrdersByRestaurantId(restaurantId);
			
			if(orderStatus!=null) {
				orders = orders.stream()
						.filter(order->order.getOrderStatus().equals(orderStatus))
						.collect(Collectors.toList());
			}
			
			return orders;
	}
//    private List<MenuItem> filterByVegetarian(List<MenuItem> menuItems, boolean isVegetarian) {
//    return menuItems.stream()
//            .filter(menuItem -> menuItem.isVegetarian() == isVegetarian)
//            .collect(Collectors.toList());
//}
	
	

	@Override
	public Order updateOrder(Long orderId, String orderStatus) throws OrderException {
		Order order=findOrderById(orderId);
		
		System.out.println("--------- "+orderStatus);
		
		if(orderStatus.equals("OUT_FOR_DELIVERY") || orderStatus.equals("DELIVERED") 
				|| orderStatus.equals("COMPLETED") || orderStatus.equals("PENDING")) {
			order.setOrderStatus(orderStatus);
			Notification notification=notificationService.sendOrderStatusNotification(order);
			return orderRepository.save(order);
		}
		else throw new OrderException("Please Select A Valid Order Status");
		
		
	}
	@Override
	public List<Order> getRecentOrdersOfRestaurant(Long restaurantId) throws OrderException, RestaurantException {
		// PageRequest.of(0, 4) ensures that only 4 orders are returned.
		return orderRepository.findRecentOrdersByRestaurantId(restaurantId, PageRequest.of(0, 4));
	}
}
