package com.juanan.photoManagement.business;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import com.juanan.photoManagement.data.dao.MetadataTypeDao;
import com.juanan.photoManagement.data.dao.PhotoMetadataDao;
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
				
				
				
				//
				// Each Directory stores values in Tag objects
				//
				for (Tag tag : directory.getTags()) {
					PhotoMetadata m = new PhotoMetadata();
					m.setMetadataTypeBean(metadataType);
					m.setName(tag.getTagName());
					m.setValue(tag.getDescription());
					m.setPhoto(p);
					list.add(m);
				}

				//
				// Each Directory may also contain error messages
				//
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

}
