package com.juanan.photoManagement.business;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;

public class FilesHelper {

	static Log logger = LogFactory.getLog(FilesHelper.class);
	
	private static final String FORMAT_DATE = "yyyymmdd";
	private static final String FORMAT_DATETIME = "yyyymmdd_HHmmss";
	
	private static final String PARAM_CREATED_DATE = "#{CD}";
	private static final String PARAM_CREATED_DATETIME = "#{CDT}";
	private static final String PARAM_USER = "#{US}";
	private static final String PARAM_FILENAME = "#{FI}";
	
	private static final String PHOTO_BASE_PATH = "D:/PhotoRepository";
	
	private static final String PHOTO_PATH = PHOTO_BASE_PATH + "/" + PARAM_CREATED_DATE + "/";
	private static final String FILENAME_FORMAT = PARAM_CREATED_DATETIME + "_" + PARAM_USER + "_" + PARAM_FILENAME;
	
	private static final DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
	private static final DateFormat dateTimeFormat = new SimpleDateFormat(FORMAT_DATETIME);
			
	public static List<File> getFiles(String path) {
		List<File> files = new ArrayList<File>();
		
		files = (List<File>) FileUtils.listFiles(FileUtils.getFile(path), null, true);
		
		return files;
	}
	
	public static void writeFile(byte[] fileInfo, String fullPathToFile) throws IOException {
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(fullPathToFile)));
        stream.write(fileInfo);
        stream.close();		
	}
	
	public static String getFilePathForPhoto(Photo p, User u) {
		String path = PHOTO_PATH.replace(PARAM_CREATED_DATE, dateFormat.format(p.getCreated()));
		
		return path;
	}
	
	public static String getFilenamePathForPhoto(Photo p, User u) {
		String filename = FILENAME_FORMAT.replace(PARAM_CREATED_DATETIME, dateTimeFormat.format(p.getCreated())).replace(PARAM_USER, u.getName()).replace(PARAM_FILENAME, p.getName());
		
		return filename;
	}	

	public static void main(String... argv) {
		Photo p = new Photo();
		User u = new User();
		
		u.setName("Juanan");
		p.setCreated(new Date());
		p.setName("Foto.jpg");
		
		logger.info(FilesHelper.getFilePathForPhoto(p, u));
	}	
}
