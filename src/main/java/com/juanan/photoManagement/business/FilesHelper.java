package com.juanan.photoManagement.business;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;

import net.coobird.thumbnailator.Thumbnails;

public class FilesHelper {

	static Log logger = LogFactory.getLog(FilesHelper.class);
	
	private static final String FORMAT_DATE = "yyyyMMdd";
	private static final String FORMAT_DATETIME = "yyyyMMdd_HHmmss";
	
	private static final String PARAM_CREATED_DATETIME = "#{CDT}";
	private static final String PARAM_USER = "#{US}";
	private static final String PARAM_FILENAME = "#{FI}";
	private static final String PARAM_CREATED_DATE_YEAR = "#{CDY}";
	private static final String PARAM_CREATED_DATE_MONTH = "#{CDM}";
	private static final String PARAM_WIDTH = "#{WIDTH}";
	private static final String PARAM_HEIGHT = "#{HEIGTH}";
	
	private static String PHOTO_BASE_PATH = "D:/PhotoRepository";
	//private static String PHOTO_BASE_PATH = "/zPendrive2/PhotoRepository";
	private static final String PHOTO_DIR = "Photos";
	private static final String THUMBNAILS_DIR = "Thumbs";
	private static final String CACHE_DIR = "cache";
	
	private static final String PHOTO_PATH = PHOTO_BASE_PATH + "/" + PHOTO_DIR + "/" + PARAM_CREATED_DATE_YEAR + "/" + PARAM_CREATED_DATE_MONTH + "/";
	private static final String THUMBS_PATH = PHOTO_BASE_PATH + "/" + THUMBNAILS_DIR + "/" + PARAM_CREATED_DATE_YEAR + "/" + PARAM_CREATED_DATE_MONTH + "/";
	private static final String CACHE_PATH = PHOTO_BASE_PATH + "/" + CACHE_DIR + "/" + PARAM_CREATED_DATE_YEAR + "/" + PARAM_CREATED_DATE_MONTH + "/";
	
	private static final String FILENAME_FORMAT = PARAM_CREATED_DATETIME + "_" + PARAM_USER + "_" + PARAM_FILENAME;
	private static final String FILENAME_FORMAT_CACHE = PARAM_CREATED_DATETIME + "_" + PARAM_USER + "_" + PARAM_WIDTH + "x" + "PARAM_HEIGTH" + "_" + PARAM_FILENAME;
	
	private static final DateFormat dateTimeFormat = new SimpleDateFormat(FORMAT_DATETIME);
	
  
	public static void SET_PHOTO_BASE_PATH(String photoDir) {
		PHOTO_BASE_PATH = photoDir;
	}
			
	public static List<File> getFiles(String path) {
		List<File> files = new ArrayList<File>();
		
		files = (List<File>) FileUtils.listFiles(FileUtils.getFile(path), null, true);
		
		return files;
	}
	
	public static void writeFile(byte[] fileInfo, String fullPathToFile) throws IOException {
        File file = new File(fullPathToFile);
        file.getParentFile().mkdirs();
        file.createNewFile();
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file, false));
        stream.write(fileInfo);
        stream.close();		
	}
	
	public static void writeThumbs(Photo p, User u) throws IOException {
		String origin = getFilePathForPhoto(p, u).concat(p.getName());
		File destination = new File(getFilePathForThumb(p, u).concat(p.getName()));
		destination.getParentFile().mkdirs();
		destination.createNewFile();
				
		Thumbnails.of(origin).size(50, 50).toFile(destination);		
	}
	
	public static void writeCache(Photo p, User u, int width, int height) throws IOException {
		String origin = getFilePathForPhoto(p, u).concat(p.getName());
		File destination = new File(getFilenamePathForPhotoCache(p, u, width, height).concat(p.getName()));
		destination.getParentFile().mkdirs();
		destination.createNewFile();
				
		Thumbnails.of(origin).size(width, height).toFile(destination);		
	}	
	
	public static byte[] getBytesFromPhoto(Photo p, User u) throws IOException {
		Path path = Paths.get(getFilePathForPhoto(p, u).concat("/").concat(p.getName()));
		
		return Files.readAllBytes(path);
	}
	
	public static byte[] getBytesFromResizedPhoto(Photo p, User u, int width, int height) throws IOException {		
		BufferedImage originalImage = ImageIO.read(new File(getFilePathForPhoto(p, u).concat("/").concat(p.getName())));

		BufferedImage thumbnail = Thumbnails.of(originalImage).size(width, height).asBufferedImage();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( thumbnail, "jpg", baos );
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		
		return imageInByte;
	}
	
	public static String getPathFromResizedPhoto(Photo p, User u, int width, int height) throws IOException {		
		BufferedImage originalImage = ImageIO.read(new File(getFilePathForPhoto(p, u).concat("/").concat(p.getName())));

		String cachePath = getFilePathForCache(p, u).concat("/").concat(p.getName());
		
		Thumbnails.of(originalImage).size(width, height).keepAspectRatio(true).toFile(cachePath);
		
		return cachePath;
	}	
	
	public static byte[] getBytesFromThumb(Photo p, User u) throws IOException {
		Path path = Paths.get(getFilePathForThumb(p, u).concat("/").concat(p.getName()));
		
		return Files.readAllBytes(path);
	}	
	
	private static String getFilePathForPhoto(String dirPath, Photo p, User u) {
		Calendar c = Calendar.getInstance();
		c.setTime(p.getCreated());
		String month = String.format("%02d", (c.get(Calendar.MONTH) + 1));
		String year = String.valueOf(c.get(Calendar.YEAR));
		
		String path = dirPath.replace(PARAM_CREATED_DATE_YEAR, year).replace(PARAM_CREATED_DATE_MONTH, month);
		
		return path;
	}
	
	public static String getFilePathForPhoto(Photo p, User u) {	
		return getFilePathForPhoto(PHOTO_PATH, p, u);
	}
	
	public static String getFilePathForThumb(Photo p, User u) {	
		return getFilePathForPhoto(THUMBS_PATH, p, u);
	}
	
	public static String getFilePathForCache(Photo p, User u) {	
		return getFilePathForPhoto(CACHE_PATH, p, u);
	}	
	
	public static String getFilenamePathForPhoto(Photo p, User u) {
		String filename = FILENAME_FORMAT.replace(PARAM_CREATED_DATETIME, dateTimeFormat.format(p.getCreated())).replace(PARAM_USER, u.getName()).replace(PARAM_FILENAME, p.getName());
		
		return filename;
	}
	
	public static String getFilenamePathForPhotoCache(Photo p, User u, int width, int height) {
		String filename = FILENAME_FORMAT.replace(PARAM_CREATED_DATETIME, dateTimeFormat.format(p.getCreated())).replace(PARAM_USER, u.getName()).replace(PARAM_WIDTH, String.valueOf(width)).replace(PARAM_HEIGHT, String.valueOf(height)).replace(PARAM_FILENAME, p.getName());
		
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
