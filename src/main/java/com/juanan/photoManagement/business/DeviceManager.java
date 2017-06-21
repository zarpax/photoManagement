package com.juanan.photoManagement.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.juanan.photoManagement.data.dao.DeviceDao;
import com.juanan.photoManagement.data.entity.Device;
import com.juanan.photoManagement.data.exception.PhotoManagementDAOException;

@Service
@Transactional(rollbackFor={Exception.class, PhotoManagementDAOException.class})
public class DeviceManager implements IDeviceManagement {

	@Autowired
	private IMetadataManagement mM;
	
	@Autowired
	private DeviceDao deviceDao;
	
	public void populateDeviceTableFromPhotoRepository(String photoRepositoryPath) throws PhotoManagementDAOException {
		List<Device> devices = mM.getDevicesFromMetadata(photoRepositoryPath);
		
		for(Device d : devices) {
			if (deviceDao.selectByName(d.getName()) == null) {
				deviceDao.insert(d);
			}
		}
	}

	@Override
	public void insertDevice(Device device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Device getPhotoMetadata(Integer deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Device> getAllDevices() throws PhotoManagementDAOException {
		Map<String, Device> mapDevices = new HashMap<String, Device>();
		List<Device> listDevices = deviceDao.selectAll();
		
		for(Device d : listDevices) {
			mapDevices.put(d.getName(), d);
		}
		
		return mapDevices;
		
	}
}
