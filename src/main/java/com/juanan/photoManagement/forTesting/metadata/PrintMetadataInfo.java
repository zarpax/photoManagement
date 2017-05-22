package com.juanan.photoManagement.forTesting.metadata;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.juanan.photoManagement.business.FilesHelper;

public class PrintMetadataInfo {

	// Used library: https://github.com/drewnoakes/metadata-extractor
	static Log logger = LogFactory.getLog(PrintMetadataInfo.class);

	public static void print(String filename, Metadata metadata)
	{
		logger.info("********************** Metadata for file[" + filename + "] **************************");

		// Iterate over the data and print to System.out

		//
		// A Metadata object contains multiple Directory objects
		//
		for (Directory directory : metadata.getDirectories()) {

			//
			// Each Directory stores values in Tag objects
			//
			for (Tag tag : directory.getTags()) {
				logger.info(tag);
				
			}

			//
			// Each Directory may also contain error messages
			//
			if (directory.hasErrors()) {
				for (String error : directory.getErrors()) {
					logger.error("ERROR: " + error);
				}
			}
		}
		
		logger.info("*************************************************************************************");
	}	
	
	public static String getDevice(Metadata metadata) {
		ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
		
		StringBuilder sB = new StringBuilder();
		
		if (directory != null) {
			if (directory.getDescription(ExifIFD0Directory.TAG_MAKE) != null) {
				sB.append(directory.getDescription(ExifIFD0Directory.TAG_MAKE).trim());
			}

			if (directory.getDescription(ExifIFD0Directory.TAG_MODEL) != null) {
				if (sB.length() > 0) {
					sB.append("-");
				}

				sB.append(directory.getDescription(ExifIFD0Directory.TAG_MODEL).trim());
			}
		}
		
		return sB.toString();

	}

	public static void main(String... argv) {
		List<File> photos = FilesHelper.getFiles("E:/Personal/Seguridad/Multimedia/Fotos");
//		List<File> photos = FilesHelper.getFiles("C:/Users/jles/Pictures");

		Map<String, String> devices = new HashMap<String, String>();
		
		for(File f : photos) {
			try {
				Metadata metadata = ImageMetadataReader.readMetadata(f);				
//				PrintMetadataInfo.print(f.getAbsolutePath().toString(), metadata);				
				String device = PrintMetadataInfo.getDevice(metadata);
				
				if ((!device.isEmpty()) && (!devices.containsKey(device))) {
					logger.info("Dispisitivo nuevo [" + device + "]");
					devices.put(device, device);
				}
								
			} catch (ImageProcessingException e) {
				// handle exception
			} catch (IOException e) {
				// handle exception			
			}
		}		

	}
}