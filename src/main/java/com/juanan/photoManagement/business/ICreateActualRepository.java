package com.juanan.photoManagement.business;

import java.util.List;

import com.juanan.photoManagement.data.entity.Photo;

public interface ICreateActualRepository {

	public List<Photo> getPhotosFromDir(String path, Integer defaultUserId) throws Exception ;
}
