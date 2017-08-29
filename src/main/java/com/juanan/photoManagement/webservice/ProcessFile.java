package com.juanan.photoManagement.webservice;

import java.io.File;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.juanan.photoManagement.business.IPhotoManagement;
import com.juanan.photoManagement.data.entity.Device;
import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;

@Component
@Scope("prototype")
public class ProcessFile implements Runnable {

	static Log logger = LogFactory.getLog(FileService.class);
	
	@Autowired
	private IPhotoManagement photoManager;
	
	private User u;
	private File f;
	private Date now;
	private Map<String, Device> mapDevices;
	
	public void init(File f, Date now, Map<String, Device> mapDevices) {
		this.mapDevices = mapDevices;
		this.now = now;
		this.f = f;
		u = new User();
		u.setUserId(0);
		u.setName("SISTEMA");		
	}
	
	@Override
	public void run() {
		byte[] data = null;
		Date lastModified = null;
		String generateMD5 = null;
		
		try {
			Photo p = photoManager.insertDiskPhoto(f, data, lastModified, now, generateMD5, u, mapDevices);

			if ((p != null) && (p.getId().intValue() == IPhotoManagement.EXISTS)) {
				//existingPhotos.add(p);
			}
		} catch (Exception e) {
			logger.error("[ERROR] Ha ocurrido un error al procesar la foto[" + f.getAbsolutePath() + "] " + e.getMessage());
			//errores++;
			//erroresFicheros.add(f);
		}
		//procesadas++;
		
//		if (procesadas%200 == 0) {
//			logger.info(String.format("Fotos procesadas [%d] errores[%d] duplicadas[%d]", procesadas, errores, existingPhotos.size()));
//		}
	}

}
