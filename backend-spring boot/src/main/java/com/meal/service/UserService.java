package com.meal.service;

import java.util.List;

import com.meal.Exception.UserException;
import com.meal.model.User;

public interface UserService {

	public User findUserProfileByJwt(String jwt) throws UserException;
	
	public User findUserByEmail(String email) throws UserException;

	public List<User> findAllUsers();

	public List<User> getPenddingRestaurantOwner();

	void updatePassword(User user, String newPassword);

	void sendPasswordResetEmail(User user);
	public void deleteAddress(Long addressId);

}
