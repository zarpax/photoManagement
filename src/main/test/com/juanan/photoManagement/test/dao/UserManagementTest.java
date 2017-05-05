package com.juanan.photoManagement.test.dao;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.juanan.photoManagement.bussiness.IUserManagement;
import com.juanan.photoManagement.data.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
//@Transactional
public class UserManagementTest {

	@Autowired
	private IUserManagement userManager;
	
	@Test
	public void inserUser() throws ParseException {
		User u = new User();
		
		SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = isoFormat.parse("2010-05-23T09:01:02");
		
		u.setName("Juanan4");
		u.setPassword("password");
		u.setLastLogin(new Timestamp(date.getTime()));
		u.setLastSync(new Timestamp(date.getTime()));
		
		User insert = userManager.insert(u);
		
		assert((insert != null) && (insert.getId() != null) && (insert.getId().intValue() > -1));
	}
}
