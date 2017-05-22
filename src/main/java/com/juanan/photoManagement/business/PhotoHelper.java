package com.juanan.photoManagement.business;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.DigestUtils;

public class PhotoHelper {
	
	/**
	 * TODO: Take a look to Commons-image for image operations
	 * 
	 * Take a look to http://im4java.sourceforge.net/ too. Appears to be very interesting
	 * 
	 * Take a look to http://www.javainuse.com/java/ImageEquals
	 * 
	 * Cool.... http://openimaj.org/
	 */
	
	static Log logger = LogFactory.getLog(PhotoHelper.class);
	
	public static String generateMD5(File file) {
		String md5String = null;
		FileInputStream fI = null;
		
		try {
			fI = new FileInputStream(file);
			md5String = DigestUtils.md5DigestAsHex(fI);
			fI.close();
		} catch (IOException e) {
			logger.error("Exception when generating md5", e);
		}
		
		return md5String;
	}
	
	
	/**
	 * This method is from internet and I'll test it
	 * http://stackoverflow.com/questions/8567905/how-to-compare-images-for-similarity-using-java
	 */	
	public static float compareImage(File fileA, File fileB) {

	    float percentage = 0;
	    try {
	        // take buffer data from both image files //
	        BufferedImage biA = ImageIO.read(fileA);
	        DataBuffer dbA = biA.getData().getDataBuffer();
	        int sizeA = dbA.getSize();
	        BufferedImage biB = ImageIO.read(fileB);
	        DataBuffer dbB = biB.getData().getDataBuffer();
	        int sizeB = dbB.getSize();
	        int count = 0;
	        // compare data-buffer objects //
	        if (sizeA == sizeB) {

	            for (int i = 0; i < sizeA; i++) {

	                if (dbA.getElem(i) == dbB.getElem(i)) {
	                    count = count + 1;
	                }

	            }
	            percentage = (count * 100) / sizeA;
	        } else {
	            System.out.println("Both the images are not of same size");
	        }

	    } catch (Exception e) {
	        System.out.println("Failed to compare image files ...");
	    }
	    return percentage;
	}	
	
}
