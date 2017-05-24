package com.juanan.photoManagement.business;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.juanan.photoManagement.data.dao.MetadataTypeDao;
import com.juanan.photoManagement.data.dao.PhotoMetadataDao;
import com.juanan.photoManagement.data.entity.Device;
import com.juanan.photoManagement.data.entity.MetadataType;
import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.PhotoMetadata;
import com.juanan.photoManagement.data.exception.PhotoManagementDAOException;

@Service
@Transactional(rollbackFor={Exception.class})
public class MetadataManager implements IMetadataManagement {
	
	static Log logger = LogFactory.getLog(MetadataManager.class);
	
	@Autowired
	private PhotoMetadataDao photoMetadataDao;
	
	@Autowired
	private MetadataTypeDao metadataTypeDao;
	
	@Override
	public void insertMetadata(List<PhotoMetadata> photoMetadatas) throws PhotoManagementDAOException {
		for(PhotoMetadata m : photoMetadatas) {
			try {
				photoMetadataDao.insert(m);
			} catch (Exception e) {
				logger.error("Ha ocurrido una excepcion al intentar introducir los metadatos [" + m.getName() + "] de la foto [" + m.getPhoto().getName() + "]", e);
			}
		}		
	}

	@Override
	public List<PhotoMetadata> getPhotoFileMetadata(Photo p, File photoFile) throws ImageProcessingException, IOException, PhotoManagementDAOException {
		List<PhotoMetadata> list = new ArrayList<PhotoMetadata>();
		Metadata metadata = ImageMetadataReader.readMetadata(photoFile);	
		
			for (Directory directory : metadata.getDirectories()) {
				MetadataType metadataType = metadataTypeDao.selectByName(directory.getName());
				
				if (metadataType == null) {
					metadataType = new MetadataType();
					metadataType.setMetadataTypeName(directory.getName());
					metadataType = metadataTypeDao.insert(metadataType);
				}

				for (Tag tag : directory.getTags()) {
					if ((tag != null) && (tag.getTagName() != null) && (tag.getDescription() != null) && (tag.getTagName().trim().length() > 0) && (tag.getDescription().trim().length() > 0)) {
						PhotoMetadata m = new PhotoMetadata();
						m.setMetadataTypeBean(metadataType);
						m.setName(tag.getTagName().replaceAll("[^\\x00-\\x7F]", "").trim());
						m.setValue(tag.getDescription().replaceAll("[^\\x00-\\x7F]", "").trim());
						m.setPhoto(p);
						list.add(m);
					}
				}

				if (directory.hasErrors()) {
					for (String error : directory.getErrors()) {
						logger.error("ERROR: " + error);
					}
				}
			}
			
			return list;
	}

	@Override
	public PhotoMetadata getPhotoMetadata(BigDecimal photoId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Device getDevice(File photoFile) throws ImageProcessingException, IOException {
		Device d = null;
		Metadata metadata = ImageMetadataReader.readMetadata(photoFile);
		ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
		
		StringBuilder sB = new StringBuilder();
		
		if (directory != null) {
			if (directory.getDescription(ExifIFD0Directory.TAG_MAKE) != null) {
				sB.append(directory.getDescription(ExifIFD0Directory.TAG_MAKE).replaceAll("[^\\x00-\\x7F]", "").trim());
			}

			if (directory.getDescription(ExifIFD0Directory.TAG_MODEL) != null) {
				if (sB.length() > 0) {
					sB.append("-");
				}

				sB.append(directory.getDescription(ExifIFD0Directory.TAG_MODEL).replaceAll("[^\\x00-\\x7F]", "").trim());
			}
		}
		
		if (sB.toString().trim().length() > 0) {
			d = new Device();
			d.setActive(null);
			d.setName(sB.toString());
		}
		
		return d;

	}	

	@Override
	public List<Device> getDevicesFromMetadata(String photoRepositoryPath) {
		Map<String, Device> mapDevices = new HashMap<String, Device>();
		List<File> photos = FilesHelper.getFiles(photoRepositoryPath);
		
		for(File f : photos) {
			try {
			if ((Files.probeContentType(f.toPath()) != null) && ((Files.probeContentType(f.toPath()).contains("image")))) {
				try {
					Device d = getDevice(f);

					if ((d != null) && (d.getName() != null) && (!mapDevices.containsKey(d.getName()))) {
						logger.info("Device[" + d.getName() + "] Fichero [" + f.getAbsoluteFile().getAbsolutePath() + "]");
						mapDevices.put(d.getName(), d);
					}				
				} catch (ImageProcessingException e) {
					logger.error("ImageProcessingException when reading metadata of file[" + f.getAbsoluteFile().getAbsolutePath() + "]", e);
				} catch (IOException e) {
					logger.error("IOException when reading metadata of file[" + f.getAbsoluteFile().getAbsolutePath() + "]", e);
				}
			}
			} catch (IOException e) {
				logger.error("Exception when reading mimetype of file[" + f.getAbsoluteFile().getAbsolutePath() + "]", e);
			}
		}
		
		return new ArrayList<Device>(mapDevices.values());
	}
	
	

}
