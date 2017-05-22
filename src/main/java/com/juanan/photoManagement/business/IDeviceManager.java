package com.juanan.photoManagement.business;

import com.juanan.photoManagement.data.entity.Device;

public interface IDeviceManager {

	public void insertDevice(Device device);
	public Device getPhotoMetadata(Integer deviceId);
}
