package com.juanan.photoManagement.webservice;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.juanan.photoManagement.business.DeviceManager;
import com.juanan.photoManagement.business.FilesHelper;
import com.juanan.photoManagement.business.IPhotoManagement;
import com.juanan.photoManagement.business.PhotoHelper;
import com.juanan.photoManagement.business.PhotoManager;
import com.juanan.photoManagement.business.UserManager;
import com.juanan.photoManagement.data.entity.Device;
import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;
import com.juanan.photoManagement.data.exception.PhotoManagementDAOException;

@RestController
@RequestMapping("/fileService")
public class FileService {
	
	static Log logger = LogFactory.getLog(FileService.class);
	
	private final String PHOTO_REPOSITORY_PATH = "E:/Personal/Seguridad/Multimedia/Fotos";// "C:/Users/jles/Pictures"
	
	@Autowired
	private UserManager userManagement;
	
	@Autowired
	private PhotoManager photoManager;
	
	@Autowired
	private CreateActualRepository aR;
	
	@Autowired
	private DeviceManager dM;	
	
	
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload( 
            @RequestParam("file") MultipartFile file,
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
            	user.setName("SISTEMA");
            	
            	Photo p = new Photo();
            	p.setBytes(file.getBytes());
            	p.setName(file.getOriginalFilename());
            	p.setMime(file.getContentType());
            	p.setCreated(new Date()); // TODO: Convert param to Date
            	p.setInserted(new Date());
            	p.setUser(user);
            	p.setMd5(PhotoHelper.generateMD5(file));
            	byte[] bytes = file.getBytes();
            	p.setBytes(bytes);
            	
            	int result = photoManager.insert(p, user);
            	
                
                //BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
                //stream.write(bytes);
                //stream.close();
                return "You successfully uploaded " + file.getOriginalFilename() + " into " + file.getOriginalFilename() + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + file.getOriginalFilename() + " because the file was empty.";
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

	@RequestMapping(value="/generateDevices", method=RequestMethod.GET)
	public void generateDevices() {
		try {
			dM.populateDeviceTableFromPhotoRepository(PHOTO_REPOSITORY_PATH);
		} catch (PhotoManagementDAOException e) {
			logger.error("Exception on extracting devices from photo files", e);
		}
	}
	
//	@RequestMapping(value="/startFromDisk", method=RequestMethod.GET)
//	public @ResponseBody List<Photo> startFromDisk() {
//		List<Photo> existingPhotos = new ArrayList<Photo>();
//		
//		try {
//			existingPhotos =  photoManager.getPhotosFromDir(PHOTO_REPOSITORY_PATH);
//		} catch (Exception e) {
//			logger.error(e);
//		}
//		
//		return existingPhotos;
//	}
	
	@RequestMapping(value="/startFromDisk", method=RequestMethod.GET)
	public @ResponseBody List<Photo> startFromDisk() {
		List<Photo> existingPhotos = new ArrayList<Photo>();

		int errores = 0;
		int procesadas = 0;
		
		try {			
			List<File> photos = FilesHelper.getFiles(PHOTO_REPOSITORY_PATH);

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
				try {
					Photo p = photoManager.insertDiskPhoto(f, data, lastModified, now, generateMD5, u, mapDevices);

					if ((p != null) && (p.getId().intValue() == IPhotoManagement.EXISTS)) {
						existingPhotos.add(p);
					}
				} catch (Exception e) {
					logger.error("[ERROR] Ha ocurrido un error al procesar la foto[" + f.getAbsolutePath() + "] " + e.getMessage());
					errores++;
				}
				procesadas++;
				
				logger.info(String.format("Fotos procesadas [%d] errores[%d] duplicadas[%d]", procesadas, errores, existingPhotos.size()));
			}

		} catch (Exception e) {
			logger.error("Ha ocurrido una excepcion al subir las fotos", e);
		}

		return existingPhotos;		
	}	
}
