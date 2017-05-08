package com.juanan.photoManagement.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.DigestUtils;

public class PhotoHelper {
	
	static Log logger = LogFactory.getLog(PhotoHelper.class);
	
	public static String generateMD5(File file) {
		String md5String = null;
		FileInputStream fI = null;
		
		try {
			fI = new FileInputStream(file);
			md5String = DigestUtils.md5DigestAsHex(fI);
		} catch (IOException e) {
			logger.error("Exception when generating md5", e);
		}
		
		return md5String;
	}
	
	
}
