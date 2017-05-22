package com.juanan.photoManagement.data.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.juanan.photoManagement.data.entity.Device;

@Repository
public class DeviceDao extends AbstractDAO<Device, Integer> {

	public DeviceDao() {
		super(Device.class);
	}

	public Device selectByName(String name) {
		Device device = null;
		
		TypedQuery<Device> q = getEntityManager().createQuery("select d from Device m where m.name=:name", Device.class);
		q.setParameter("name", name);

		List<Device> resultList = q.getResultList();
		
		if ((!resultList.isEmpty()) && (resultList.size() == 1)) {
			device = resultList.get(0);
		}
		
		return device;		
	}	
}
