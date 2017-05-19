package com.juanan.photoManagement.business;

import java.util.List;

import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;

public interface ICreateActualRepository {

	public List<Photo> getPhotosFromDir(String path, Integer defaultUserId) throws Exception ;
	
	public Photo getPhotosFromDir(Photo p, User u) throws Exception ;
}
