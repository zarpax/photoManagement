package com.juanan.photoManagement.data.dao;

import java.math.BigDecimal;

import com.juanan.photoManagement.data.entity.Photo;

public class PhotoDao extends AbstractDAO<Photo, BigDecimal> {

	public PhotoDao() {
		super(Photo.class);
	}

}
