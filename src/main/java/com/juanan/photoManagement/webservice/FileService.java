package com.juanan.photoManagement.webservice;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

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
import com.juanan.photoManagement.business.UserManager;
import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;

@RestController
@RequestMapping("/fileService")
public class FileService {
	
	static Log logger = LogFactory.getLog(FileService.class);
	
	@Autowired
	private UserManager userManagement;
	
	@Autowired
	private CreateActualRepository aR;
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload( 
            @RequestParam("file") MultipartFile file){
            String name = "test11";
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = 
                        new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
                stream.write(bytes);
                stream.close();
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
	
	@RequestMapping(value="/startFromDisk", method=RequestMethod.GET)
	public @ResponseBody List<Photo> startFromDisk() {
		return aR.getPhotosFromDir("E:/Personal/Seguridad/Multimedia/Fotos", 0);
	}
}
