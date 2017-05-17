package com.juanan.photoManagement.business;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.juanan.photoManagement.data.entity.Photo;

@Service
@Transactional(rollbackFor={Exception.class})
public class CreateActualRepository implements ICreateActualRepository {

	@Autowired
	private PhotoManager pM;
	
	@Autowired
	private UserManager uM;
	
	@Override
	public List<Photo> getPhotosFromDir(String path, Integer defaultUserId) {
		
		List<Photo> existingPhotos = new ArrayList<Photo>();
		
		List<File> photos = FilesHelper.getFiles(path);
		
		Date now = new Date();
		
		for(File f : photos) {
			Date lastModified = new Date(f.lastModified());
			String generateMD5 = PhotoHelper.generateMD5(f);
			
			Photo p = new Photo();
			p.setUserId(defaultUserId);
			p.setCreated(lastModified);
			p.setCreated(lastModified);
			p.setInserted(now);
			p.setName(f.getName());
			p.setPath(f.getAbsolutePath());
			p.setMime("image/jpeg");
			p.setMd5(generateMD5);
			
			if (!pM.exists(p)) {
				pM.insert(p);
			} else {
				existingPhotos.add(p);
			}
		}
		
		return existingPhotos;		
	}

	
}
