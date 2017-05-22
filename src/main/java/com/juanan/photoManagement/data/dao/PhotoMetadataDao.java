package com.juanan.photoManagement.data.dao;

import org.springframework.stereotype.Repository;

import com.juanan.photoManagement.data.entity.PhotoMetadata;

@Repository
public class PhotoMetadataDao extends AbstractDAO<PhotoMetadata, Long> {

	public PhotoMetadataDao() {
		super(PhotoMetadata.class);
	}

}
