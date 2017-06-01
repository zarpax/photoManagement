package com.juanan.photoManagement.business;

import java.util.List;

import com.juanan.photoManagement.data.entity.User;

public interface IUserManagement {

	public User insert(User user);
	public User getUser(Integer userId);
	
	public List<User> getAll();
	public boolean existsUser(String name, String password);
}
