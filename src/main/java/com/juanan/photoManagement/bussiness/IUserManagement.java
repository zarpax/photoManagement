package com.juanan.photoManagement.bussiness;

import com.juanan.photoManagement.data.entity.User;

public interface IUserManagement {

	public User insert(User user);
	public User getUser(Integer userId);
	
}
