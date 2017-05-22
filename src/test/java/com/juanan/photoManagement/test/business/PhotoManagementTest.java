package com.juanan.photoManagement.test.business;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.juanan.photoManagement.business.IPhotoManagement;
import com.juanan.photoManagement.business.PhotoHelper;
import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-photo.xml" })

public class PhotoManagementTest {

	@Autowired
	private IPhotoManagement photoManager;
	
	@Test
	public void insertPhoto() throws Exception {
		Photo p = new Photo();
		
		SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = isoFormat.parse("2010-05-23T09:01:02");

		User u = new User();
		u.setUserId(0);
		
		p.setCreated(date);
		p.setInserted(date);
		p.setMime("mime");
		p.setName("name");
		p.setPath("path");
		p.setUser(u);
		

		
		File f = new File("D:/Amigasw.jpg");
		p.setMd5(PhotoHelper.generateMD5(f));		
		
		photoManager.insert(p, u);
	}
}
