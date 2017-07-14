package com.juanan.photoManagement.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.juanan.photoManagement.business.FilesHelper;
import com.juanan.photoManagement.business.IDeviceManagement;
import com.juanan.photoManagement.business.IPhotoManagement;
import com.juanan.photoManagement.business.IUserManagement;
import com.juanan.photoManagement.business.PhotoHelper;
import com.juanan.photoManagement.data.entity.Device;
import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;
import com.juanan.photoManagement.data.exception.PhotoManagementDAOException;
import com.juanan.photoManagement.webservice.dto.PhotoDTO;

@Controller
@RequestMapping("/fileService")
public class FileService {
	
	static Log logger = LogFactory.getLog(FileService.class);
	
	private final String PHOTO_REPOSITORY_PATH = "E:/Personal/Seguridad/Multimedia/Fotos";// "C:/Users/jles/Pictures"
	
	@Autowired
	private IUserManagement userManagement;
	
	@Autowired
	private IPhotoManagement photoManager;
	
	@Autowired
	private IDeviceManagement dM;	
	
	
	
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
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ResponseEntity<Boolean> login(@RequestBody User userLogin) {
		Boolean status = Boolean.FALSE;
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		try {
			boolean exists = userManagement.existsUser(userLogin.getName(), userLogin.getPassword());
			
			if (exists) {
				status = Boolean.TRUE;
			}
		} catch (Exception e) {
			logger.error("Exception when login", e);
		}
		
		return new ResponseEntity<Boolean>(status, headers, HttpStatus.OK);
	}	
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/getPhoto", method=RequestMethod.POST)
	public void getPhoto(@RequestBody PhotoDTO photo, HttpServletResponse response) {
		sendPhoto((long)photo.getPhotoId(), photo.getWidth(), photo.getHeigth(), response);
	}	
	
	private void sendPhoto(Long photoId, int width, int height, HttpServletResponse response) {
		Photo p = null;
	    
		try {
			logger.debug("Solicitando foto con id[" + photoId + "]");
			Photo photoParam = new Photo();
			photoParam.setPhotoId(new BigDecimal(photoId));
			p = photoManager.getPhotoByIdWithoutFile(photoParam);

			response.addHeader("Content-disposition", "attachment;filename=" + p.getName());
			response.setContentType(p.getMime());
			int fileLength = -1;
			InputStream iS = null;
			
			// TODO: Improve this if/else
			if ((width > 0) && (height > 0)) {
				iS = photoManager.getCachePhotoForDevice(p, p.getUser(), width, height);
			} else {			
				File f = new File(FilesHelper.getDirPathForPhoto(p, p.getUser()) + "/" + p.getName());
				iS = new FileInputStream(f);				
			}
			
			fileLength = IOUtils.copy(iS, response.getOutputStream());
			response.setContentLength(fileLength);
			response.flushBuffer();			
			iS.close();
			response.getOutputStream().close();
		} catch (Exception e) {
			logger.error("Exception when sending a photo", e);
		}		
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/getPhoto/{photoId}", method=RequestMethod.GET)
	public void getPhoto(@PathVariable Long photoId, HttpServletResponse response) {
		sendPhoto(photoId, 0, 0, response);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/getPhoto/{photoId}/{width}/{heigth}", method=RequestMethod.GET)
	public void getPhoto(@PathVariable Long photoId, @PathVariable int width, @PathVariable int heigth, HttpServletResponse response) {
		sendPhoto(photoId, width, heigth, response);
	}	
	
//	@CrossOrigin(origins = "*")
//	@RequestMapping(value="/getThumb", method=RequestMethod.POST)
//	public ResponseEntity<Photo> getThumb(@RequestBody PhotoDTO photo) {
//		Photo p = null;
//	    final HttpHeaders headers = new HttpHeaders();
//	    
//		try {
//			logger.debug("Solicitando foto con id[" + photo.getPhotoId() + "]");
//			Photo photoParam = new Photo();
//			photoParam.setPhotoId(new BigDecimal(photo.getPhotoId()));
//			p = photoManager.getPhotoById(photoParam, photo.getWidth(), photo.getHeigth());
//			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//		} catch (Exception e) {
//			logger.error("Exception when getting thumb", e);
//		}
//		
//		return new ResponseEntity<Photo>(p, headers, HttpStatus.CREATED);
//	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value="/downloadThumb", method=RequestMethod.POST)
	public void downloadThumb(@RequestBody PhotoDTO photo, HttpServletResponse response) {
		Photo p = null;
	    
		try {
			logger.debug("Solicitando foto con id[" + photo.getPhotoId() + "]");
			Photo photoParam = new Photo();
			photoParam.setPhotoId(new BigDecimal(photo.getPhotoId()));
			p = photoManager.getPhotoByIdWithoutFile(photoParam);
		
			File f = new File(FilesHelper.getDirPathForThumb(p, p.getUser()) + "/" + p.getName());
			InputStream iS = new FileInputStream(f);
			response.addHeader("Content-disposition", "attachment;filename=" + p.getName());
			response.setContentType(p.getMime());
			response.setContentLength((int)f.length());
			
			ServletOutputStream out = response.getOutputStream();
			byte[] bytes = new byte[512];
			int bytesRead;

			while ((bytesRead = iS.read(bytes)) != -1) {
			    out.write(bytes, 0, bytesRead);
			}

			// do the following in a finally block:
			iS.close();
			out.close();
		} catch (Exception e) {
			logger.error("Exception when login", e);
		}
	}	
	

	@CrossOrigin(origins = "*")
	@RequestMapping(value="/getThumb/{photoId}/{width}/{height}", method=RequestMethod.GET)
	public void downloadThumbGet(@PathVariable Long photoId, @PathVariable int width, @PathVariable int height, HttpServletResponse response) {
		Photo p = null;
	    
		try {
			logger.debug("Solicitando foto con id[" + photoId + "]");
			Photo photoParam = new Photo();
			photoParam.setPhotoId(new BigDecimal(photoId));
			p = photoManager.getPhotoByIdWithoutFile(photoParam);
		
			response.addHeader("Content-disposition", "attachment;filename=" + p.getName());
			response.setContentType(p.getMime());
			int fileLength = 0;

			InputStream iS = photoManager.getThumbPhotoForDevice(p, p.getUser(), width, height);
			//fileLength = IOUtils.copy(iS, response.getOutputStream());			
			
			ServletOutputStream out = response.getOutputStream();
			byte[] bytes = new byte[512];
			int bytesRead;

			while ((bytesRead = iS.read(bytes)) != -1) {
			    out.write(bytes, 0, bytesRead);
			    fileLength += bytesRead;
			}
						
			response.setContentLength(fileLength);
			response.flushBuffer();
			

			// do the following in a finally block:
			iS.close();
			out.close();			
		} catch (Exception e) {
			logger.error("Exception when getting one thumb", e);
		}
	}	
	
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/getThumbs", method=RequestMethod.POST)
	public ResponseEntity<List<Photo>> getThumbs() {
		List<Photo> photos = null;
	    final HttpHeaders headers = new HttpHeaders();
	    
		try {
			photos = photoManager.getRandomPhotos(25);
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		} catch (Exception e) {
			logger.error("Exception when login", e);
		}
		
		return new ResponseEntity<List<Photo>>(photos, headers, HttpStatus.CREATED);
	}		

	@RequestMapping(value="/generateDevices", method=RequestMethod.GET)
	public void generateDevices() {
		try {
			dM.populateDeviceTableFromPhotoRepository(PHOTO_REPOSITORY_PATH);
		} catch (PhotoManagementDAOException e) {
			logger.error("Exception on extracting devices from photo files", e);
		}
	}
	
	@RequestMapping(value="/startFromDisk", method=RequestMethod.GET)
	public @ResponseBody List<Photo> startFromDisk() {
		List<Photo> existingPhotos = new ArrayList<Photo>();
		List<File> erroresFicheros = new ArrayList<File>();
		
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
					erroresFicheros.add(f);
				}
				procesadas++;
				
				if (procesadas%200 == 0) {
					logger.info(String.format("Fotos procesadas [%d] errores[%d] duplicadas[%d]", procesadas, errores, existingPhotos.size()));
				}
			}

		} catch (Exception e) {
			logger.error("Ha ocurrido una excepcion al subir las fotos", e);
		}
		
		

		return existingPhotos;		
	}	
}
