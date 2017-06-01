package com.juanan.photoManagement.business;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.juanan.photoManagement.data.dao.PhotoDao;
import com.juanan.photoManagement.data.entity.Device;
import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.PhotoMetadata;
import com.juanan.photoManagement.data.entity.User;
import com.juanan.photoManagement.data.exception.PhotoManagementDAOException;
import com.juanan.photoManagement.data.exception.PhotoManagementInfraestructureException;

@Service
@Transactional(rollbackFor={Exception.class})
public class PhotoManager implements IPhotoManagement {

	static Log logger = LogFactory.getLog(PhotoManager.class);
	
	@Autowired
	private PhotoDao photoDao;
	
	@Autowired
	private MetadataManager mM;	
	
	@Autowired
	private DeviceManager dM;
	
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
					FilesHelper.writeThumbs(photo, user);
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
		
		String photoMd5 = photo.getMd5();
		
		//logger.info("Buscando si existe MD5[" + photoMd5 + "] para la foto [" + photo.getPath() + "__" + photo.getName() + "]");
		
		if (photoDao.selectByMD5(photoMd5) != null) {
			exists = true;
		}
		
		return exists;
	}

	@Override
	public List<Photo> getPhotosFromLastSync(Date lastSync) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Photo insertDiskPhoto(File f, byte[] data, Date lastModified, Date now, String generateMD5, User u, Map<String, Device> mapDevices) throws Exception {
		Photo result = null;
		
		if ((Files.probeContentType(f.toPath()) != null) && ((Files.probeContentType(f.toPath()).contains("image")) || (Files.probeContentType(f.toPath()).contains("video")))) {
			if ((Files.probeContentType(f.toPath()) != null) && (Files.probeContentType(f.toPath()).contains("image"))) {
				Device fileDevice = mM.getDevice(f);
				
				if ((fileDevice != null) && (fileDevice.getName() != null) && (!fileDevice.getName().isEmpty())) {
					Device deviceName = mapDevices.get(fileDevice.getName());
					User userDevice = deviceName.getUser();

					if (userDevice != null) {
						u.setUserId(userDevice.getId());
						u.setName(userDevice.getName());
					}
				} else {
					u.setUserId(0);
					u.setName("SISTEMA");							
				}
			} else {
				u.setUserId(0);
				u.setName("SISTEMA");						
			}
			
			lastModified = new Date(f.lastModified());
			generateMD5 = PhotoHelper.generateMD5(f);

			Photo p = new Photo();
			p.setUser(u);
			p.setCreated(mM.getCreationDate(f));
			p.setInserted(now);
			p.setName(f.getName());
			p.setPath(f.getAbsolutePath());
			p.setMime(Files.probeContentType(f.toPath()));
			p.setMd5(generateMD5);

			data = FileUtils.readFileToByteArray(f);
			p.setBytes(data);

			Photo photo = null;

			if (insert(p, u) == IPhotoManagement.EXISTS) {
				photo = p;
			}	

			if (photo != null) {
				//existingPhotos.add(photo);
				photo.setPhotoId(new BigDecimal(IPhotoManagement.EXISTS));
				result = photo;
			} else if ((Files.probeContentType(f.toPath()) != null) && (Files.probeContentType(f.toPath()).contains("image"))) {				
				List<PhotoMetadata> metadata = mM.getPhotoFileMetadata(p, f);
				mM.insertMetadata(metadata);
			}

			data = null;
			p.setBytes(null);
			lastModified = null;
			generateMD5 = null;
			p = null;
		} else {
			logger.info("Fichero [" + f.getAbsoluteFile().getAbsolutePath() + "] no procesado porque no tiene metadatos");
		}	
		
		return result;
	}
	
	@Override
	public List<Photo> getPhotosFromDir(String photoRepositoryPath) throws Exception {
		List<Photo> existingPhotos = new ArrayList<Photo>();
		
		try {			
			List<File> photos = FilesHelper.getFiles(photoRepositoryPath);
			
			logger.info("Found " + photos.size() + " photo(s)");
			
			Date now = new Date();
			
			byte[] data = null;
			Date lastModified = null;
			String generateMD5 = null;
			
			Map<String, Device> mapDevices = dM.getAllDevices();
			
			User u = new User();
			u.setUserId(0);
			u.setName("SISTEMA");		
			
			for(File f : photos) {
				Photo p = insertDiskPhoto(f, data, lastModified, now, generateMD5, u, mapDevices);
				
				if (p.getId().intValue() == IPhotoManagement.EXISTS) {
					existingPhotos.add(p);
				}
			}
			
			return existingPhotos;		
			
		} catch (Exception e) {
			logger.error("Ha ocurrido una excepcion", e);
			throw new Exception(e.getMessage());// TODO: Create a specific exception for this
		}
	}


	@Override
	public Photo getPhotoById(Photo photo) {
		Photo p = null;
		
		try {
			p = photoDao.select(photo.getId());
			
			p.setBytes(FilesHelper.getBytesFromPhoto(p, p.getUser()));
		} catch (Exception e) {
			logger.error("Exception when getting photo", e);
		}
		
		return p;
	}


	@Override
	public List<Photo> get100RecentThumbs() { // TODO: Delete ASAP. Is for testing front
		List<Photo> photos = new ArrayList<Photo>();
		
		try {
			photos = photoDao.selectLastest100Photos();
			for (Photo p : photos) {
				p.setBytes(FilesHelper.getBytesFromThumb(p, p.getUser()));
			}
		} catch (Exception e) {
			logger.error("Exception when getting photos from bbdd", e);
		}
		
		return photos;
	}

}
