package com.juanan.photoManagement.data.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.juanan.photoManagement.data.entity.User;

@Repository
public class UserDao extends AbstractDAO<User, Integer> {

	public UserDao() {
		super(User.class);
	}

	public User selectByNameAndPassword(String name, String password) {
		User user = null;
		
		TypedQuery<User> q = getEntityManager().createQuery("select u from User u where u.name=:name and u.password=:password", User.class);
		q.setParameter("name", name);
		q.setParameter("password", password);

		List<User> resultList = q.getResultList();
		
		if ((!resultList.isEmpty()) && (resultList.size() == 1)) {
			user = resultList.get(0);
		}
		
		return user;
	}
}
