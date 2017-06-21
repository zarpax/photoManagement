package com.juanan.photoManagement.business;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;

@Service
@Transactional(rollbackFor={Exception.class})
public class CreateActualRepository implements ICreateActualRepository {
	
	static Log logger = LogFactory.getLog(CreateActualRepository.class);

	@Autowired
	private IPhotoManagement pM;

	@Override
	public Photo getPhotosFromDir(Photo p, User u) throws Exception {
		Photo photo = null;

		if (pM.insert(p, u) == IPhotoManagement.EXISTS) {
			photo = p;
		}	
		
		return photo;
	}
}
