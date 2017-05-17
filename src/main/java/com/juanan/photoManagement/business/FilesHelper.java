package com.juanan.photoManagement.business;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FilesHelper {

	static Log logger = LogFactory.getLog(FilesHelper.class);
		
	public static List<File> getFiles(String path) {
		List<File> files = new ArrayList<File>();
		
		files = (List<File>) FileUtils.listFiles(FileUtils.getFile(path), null, true);
		
		return files;
	}

	public static void main(String... argv) {
		List<File> files = FilesHelper.getFiles("D:/pruebas");
		
		for(File f : files) {
			logger.info(f.getAbsolutePath());
		}
	}
}
