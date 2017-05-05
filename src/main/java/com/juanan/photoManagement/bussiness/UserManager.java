package com.juanan.photoManagement.bussiness;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.juanan.photoManagement.data.dao.UserDao;
import com.juanan.photoManagement.data.entity.User;
import com.juanan.photoManagement.data.exception.PhotoManagementDAOException;

@Service
@Transactional(rollbackFor={Exception.class})
public class UserManager implements IUserManagement {

	static Log logger = LogFactory.getLog(UserManager.class);
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public User insert(User user) {
		User newUser = null;
		try {
			newUser = userDao.insert(user);
		} catch (PhotoManagementDAOException e) {
			logger.error("Exception when inserting user.", e);
		}
				
		return newUser;
	}

	@Override
	public User getUser(Integer userId) {
		User user = null;
		
		try {
			user = userDao.select(userId);
		} catch (PhotoManagementDAOException e) {
			logger.error("Error when trying to find user with id[" + userId + "]", e);
		}
		
		return user;
	}

}
