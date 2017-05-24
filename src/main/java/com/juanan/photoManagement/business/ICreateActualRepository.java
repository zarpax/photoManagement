package com.juanan.photoManagement.business;

import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;

public interface ICreateActualRepository {
	
	public Photo getPhotosFromDir(Photo p, User u) throws Exception ;
}
