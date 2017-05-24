package com.juanan.photoManagement.business;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.drew.imaging.ImageProcessingException;
import com.juanan.photoManagement.data.entity.Device;
import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.PhotoMetadata;
import com.juanan.photoManagement.data.exception.PhotoManagementDAOException;

public interface IMetadataManagement {

	public void insertMetadata(List<PhotoMetadata> photoMetadatas) throws PhotoManagementDAOException ;
	public List<PhotoMetadata> getPhotoFileMetadata(Photo p, File photoFile) throws ImageProcessingException, IOException, PhotoManagementDAOException ;
	public PhotoMetadata getPhotoMetadata(BigDecimal photoId);
	public Device getDevice(File photoFile) throws ImageProcessingException, IOException;
	public List<Device> getDevicesFromMetadata(String photoRepositoryPath);
}
