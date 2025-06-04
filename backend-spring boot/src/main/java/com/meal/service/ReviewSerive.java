package com.meal.service;

import java.util.List;

import com.meal.Exception.ReviewException;
import com.meal.model.Review;
import com.meal.model.User;
import com.meal.request.ReviewRequest;

public interface ReviewSerive {
	
    public Review submitReview(ReviewRequest review,User user);
    public void deleteReview(Long reviewId) throws ReviewException;
    public double calculateAverageRating(List<Review> reviews);
}
