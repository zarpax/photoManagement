package com.juanan.photoManagement.business;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.drew.imaging.ImageProcessingException;
import com.juanan.photoManagement.data.entity.Device;
import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.PhotoMetadata;
import com.juanan.photoManagement.data.exception.PhotoManagementDAOException;

public interface IMetadataManagement {

	public static String DATE_TIME_CREATED_PATTERN = "yyyy:MM:dd hh:mm:ss";
	
	public void insertMetadata(List<PhotoMetadata> photoMetadatas) throws PhotoManagementDAOException ;
	public List<PhotoMetadata> getPhotoFileMetadata(Photo p, File photoFile) throws ImageProcessingException, IOException, PhotoManagementDAOException ;
	public PhotoMetadata getPhotoMetadata(BigDecimal photoId);
	public Device getDevice(File photoFile) throws ImageProcessingException, IOException;
	public Date getCreationDate(File photoFile) throws ImageProcessingException, IOException, ParseException;
	public List<Device> getDevicesFromMetadata(String photoRepositoryPath);
}
