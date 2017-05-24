package com.juanan.photoManagement.business;

import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;

public interface ICreateActualRepository {
<<<<<<< HEAD
=======

//	public List<Photo> getPhotosFromDir(String path, Integer defaultUserId) throws Exception ;
>>>>>>> branch 'master' of https://github.com/zarpax/photoManagement.git
	
	public Photo getPhotosFromDir(Photo p, User u) throws Exception ;
}
