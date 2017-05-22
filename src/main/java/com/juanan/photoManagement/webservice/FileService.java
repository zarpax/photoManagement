package com.juanan.photoManagement.webservice;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.juanan.photoManagement.business.CreateActualRepository;
import com.juanan.photoManagement.business.FilesHelper;
import com.juanan.photoManagement.business.MetadataManager;
import com.juanan.photoManagement.business.PhotoHelper;
import com.juanan.photoManagement.business.PhotoManager;
import com.juanan.photoManagement.business.UserManager;
import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.PhotoMetadata;
import com.juanan.photoManagement.data.entity.User;

@RestController
@RequestMapping("/fileService")
public class FileService {
	
	static Log logger = LogFactory.getLog(FileService.class);
	
	@Autowired
	private UserManager userManagement;
	
	@Autowired
	private PhotoManager photoManager;
	
	@Autowired
	private CreateActualRepository aR;
	
	@Autowired
	private MetadataManager mM;	
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload( 
            @RequestParam("file") MultipartFile file,
            @RequestParam("filename") String name,
            @RequestParam("mime") String mime,
            @RequestParam("userId") Integer userId,
            @RequestParam("creationDate") String creationDate) {

		/** TODO: Validation params
		 * file is not empty
		 * filename is a correct name
		 * correct and supported mimetype
		 * userId is a valid user in system
		 * creationDate a valid date
		 */
		
        if (!file.isEmpty()) {
            try {
            	User user = new User();
            	user.setUserId(userId);
            	
            	Photo p = new Photo();
            	p.setBytes(file.getBytes());
            	p.setName(name);
            	p.setMime(mime);
            	p.setCreated(new Date()); // TODO: Convert param to Date
            	p.setUser(user);
            	            	
            	byte[] bytes = file.getBytes();
            	p.setBytes(bytes);
            	
            	int result = photoManager.insert(p, user);
            	
                
                //BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
                //stream.write(bytes);
                //stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }	
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public @ResponseBody String test() {
		logger.debug("Testing running");
		
		return "hecho";		
	}
	
	@RequestMapping(value="/allUsers", method=RequestMethod.GET)
	public @ResponseBody List<User> getAllUsers() {
		return userManagement.getAll();
	}
	
//	@RequestMapping(value="/startFromDisk", method=RequestMethod.GET)
//	public @ResponseBody List<Photo> startFromDisk() {
//		try {
//			return aR.getPhotosFromDir("E:/Personal/Seguridad/Multimedia/Fotos", 0);
//		} catch (Exception e) {
//			return null;
//		}
//	}
	
	@RequestMapping(value="/startFromDisk", method=RequestMethod.GET)
	public @ResponseBody List<Photo> startFromDisk() {
		List<Photo> existingPhotos = new ArrayList<Photo>();
		
		try {			
			
			List<File> photos = FilesHelper.getFiles("C:/Users/jles/Pictures");
			//List<File> photos = FilesHelper.getFiles("E:/Personal/Seguridad/Multimedia/Fotos");
			
			logger.info("Found " + photos.size() + " photo(s)");
			
			Date now = new Date();
			
			byte[] data = null;
			Date lastModified = null;
			String generateMD5 = null;
			
			User u = new User();
			u.setUserId(0);
			u.setName("SISTEMA");		
			
			for(File f : photos) { 
				logger.info("Procesando fichero [" + f.getAbsoluteFile().getAbsolutePath() + "] size[" +f.length() + "]");
				if ((Files.probeContentType(f.toPath()) != null) && (Files.probeContentType(f.toPath()).contains("image"))) {
					lastModified = new Date(f.lastModified());
					generateMD5 = PhotoHelper.generateMD5(f);

					Photo p = new Photo();
					p.setUser(u);
					p.setCreated(lastModified);
					p.setCreated(lastModified);
					p.setInserted(now);
					p.setName(f.getName());
					p.setPath(f.getAbsolutePath());
					p.setMime(Files.probeContentType(f.toPath()));
					p.setMd5(generateMD5);

					//logger.info("Fichero [" + f.getAbsoluteFile().getAbsolutePath() + "] size[" +f.length() + "]");
					data = FileUtils.readFileToByteArray(f);
					p.setBytes(data);

					Photo photo = aR.getPhotosFromDir(p, u);

					if (photo != null) {
						existingPhotos.add(photo);
					} else {				
						List<PhotoMetadata> metadata = mM.getPhotoFileMetadata(p, f);
						mM.insertMetadata(metadata);
					}

					data = null;
					p.setBytes(null);
					lastModified = null;
					generateMD5 = null;
					p = null;
				} else {
					logger.info("No es imagen [" + f.getAbsoluteFile().getAbsolutePath() + "] " + Files.probeContentType(f.toPath()));
				}
			}
			
			return existingPhotos;		
			
		} catch (Exception e) {
			logger.error("Ha ocurrido una excepcion", e);
			return existingPhotos;
		}
	}	
}
