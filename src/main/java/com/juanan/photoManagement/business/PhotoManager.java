package com.juanan.photoManagement.business;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.juanan.photoManagement.data.dao.PhotoDao;
import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;
import com.juanan.photoManagement.data.exception.PhotoManagementDAOException;
import com.juanan.photoManagement.data.exception.PhotoManagementInfraestructureException;

@Service
@Transactional(rollbackFor={Exception.class})
public class PhotoManager implements IPhotoManagement {

	static Log logger = LogFactory.getLog(PhotoManager.class);
	
	@Autowired
	private PhotoDao photoDao;
	
	
	@Override
	public int insert(Photo photo, User user) throws Exception {
		int result = -1;
		
		try {
			if (!exists(photo)) {		
				photo.setName(FilesHelper.getFilenamePathForPhoto(photo, user));
				photo.setPath(FilesHelper.getFilePathForPhoto(photo, user));
				
				Photo inserted = photoDao.insert(photo);
				
				
				
				if ((inserted == null) || (inserted.getId() == null)) {
					result = IPhotoManagement.ERROR;
				} else {
					FilesHelper.writeFile(photo.getBytes(), photo.getPath() + photo.getName());
				}
				
			} else {
				result = IPhotoManagement.EXISTS;
			}
		} catch (PhotoManagementDAOException e) {
			logger.error("Exception when inserting photo.", e);
		} catch (IOException e) {
			logger.error("Exception when writing file", e);
			throw new Exception(e);
		}		
		
		return result;
	}

	
	private boolean exists(Photo photo) {
		boolean exists = false;
		
		if (photo.getMd5() == null) {
			throw new PhotoManagementInfraestructureException("Photo hasn't md5 field");
		}
		
		if (photoDao.selectByMD5(photo.getMd5()) != null) {
			exists = true;
		}
		
		return exists;
	}

	@Override
	public List<Photo> getPhotosFromLastSync(Date lastSync) {
		// TODO Auto-generated method stub
		return null;
	}

}
