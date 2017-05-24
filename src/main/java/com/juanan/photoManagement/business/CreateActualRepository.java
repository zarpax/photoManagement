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
	private PhotoManager pM;
	
	@Autowired
	private UserManager uM;
	
	@Autowired
	private MetadataManager mM;
	
//	@Override
//	public List<Photo> getPhotosFromDir(String path, Integer defaultUserId) throws Exception {
//		
//		List<Photo> existingPhotos = new ArrayList<Photo>();
//		
//		List<File> photos = FilesHelper.getFiles(path);
//		
//		logger.info("Found " + photos.size() + " photo(s)");
//		
//		Date now = new Date();
//		
//		byte[] data = null;
//		Date lastModified = null;
//		String generateMD5 = null;
//		
//		User u = new User();
//		u.setUserId(0);
//		u.setName("SISTEMA");		
//		
//		for(File f : photos) {
//			lastModified = new Date(f.lastModified());
//			generateMD5 = PhotoHelper.generateMD5(f);
//			
//			Photo p = new Photo();
//			p.setCreated(lastModified);
//			p.setCreated(lastModified);
//			p.setInserted(now);
//			p.setName(f.getName());
//			p.setPath(f.getAbsolutePath());
//			p.setMime(Files.probeContentType(f.toPath()));
//			p.setMd5(generateMD5);
//			
//			p.setUser(u);
//			
//			logger.info("Fichero [" + f.getAbsoluteFile().getAbsolutePath() + "] size[" +f.length() + "]");
//			data = FilesHelper.extractBytes(f.getAbsoluteFile().getAbsolutePath());
//			p.setBytes(data);
//			
//			
//			if (pM.insert(p, u) == IPhotoManagement.EXISTS) {
//				existingPhotos.add(p);
//			} else {
//				List<PhotoMetadata> metadata = mM.getPhotoFileMetadata(p, f);
//				mM.insertMetadata(metadata);				
//			}
//						
//			data = null;
//			p.setBytes(null);
//			lastModified = null;
//			generateMD5 = null;
//			p = null;
//		}
//		
//		return existingPhotos;		
//	}

	@Override
	public Photo getPhotosFromDir(Photo p, User u) throws Exception {
		Photo photo = null;

		if (pM.insert(p, u) == IPhotoManagement.EXISTS) {
			photo = p;
		}	
		
		return photo;
	}
}
