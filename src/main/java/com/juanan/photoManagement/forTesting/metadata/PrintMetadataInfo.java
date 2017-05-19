package com.juanan.photoManagement.forTesting.metadata;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
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

	public static void main(String... argv) {
//		List<File> photos = FilesHelper.getFiles("E:/Personal/Seguridad/Multimedia/Fotos");
		List<File> photos = FilesHelper.getFiles("C:/Users/jles/Pictures");

		for(File f : photos) {
			try {
				Metadata metadata = ImageMetadataReader.readMetadata(f);
				
				PrintMetadataInfo.print(f.getAbsolutePath().toString(), metadata);
			} catch (ImageProcessingException e) {
				// handle exception
			} catch (IOException e) {
				// handle exception			
			}
		}		

	}
}