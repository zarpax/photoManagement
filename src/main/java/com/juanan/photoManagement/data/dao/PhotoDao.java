package com.juanan.photoManagement.data.dao;

import java.math.BigDecimal;

import com.juanan.photoManagement.data.entity.Photo;

public class PhotoDao extends AbstractDAO<Photo, BigDecimal> {

	protected PhotoDao(Class<Photo> clazz) {
		super(Photo.class);
	}

}
