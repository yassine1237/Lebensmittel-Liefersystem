package com.meal.service;

import com.stripe.exception.StripeException;
import com.meal.model.Order;
import com.meal.model.PaymentResponse;

public interface PaymentService {
	
	public PaymentResponse generatePaymentLink(Order order) throws StripeException;

}
