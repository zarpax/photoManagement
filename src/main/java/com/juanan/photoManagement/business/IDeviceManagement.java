package com.juanan.photoManagement.business;

import java.util.Map;

import com.juanan.photoManagement.data.entity.Device;
import com.juanan.photoManagement.data.exception.PhotoManagementDAOException;

public interface IDeviceManagement {

	public void insertDevice(Device device);
	public Device getPhotoMetadata(Integer deviceId);
	public void populateDeviceTableFromPhotoRepository(String photoRepositoryPath) throws PhotoManagementDAOException ;
	public Map<String, Device> getAllDevices() throws PhotoManagementDAOException;
}
