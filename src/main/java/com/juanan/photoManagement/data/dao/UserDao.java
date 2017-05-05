package com.juanan.photoManagement.data.dao;

import org.springframework.stereotype.Repository;

import com.juanan.photoManagement.data.entity.User;

@Repository
public class UserDao extends AbstractDAO<User, Integer> {

	protected UserDao(Class<User> clazz) {
		super(User.class);
	}

}
