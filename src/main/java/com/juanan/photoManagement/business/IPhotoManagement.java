package com.juanan.photoManagement.business;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.juanan.photoManagement.data.entity.Device;
import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.entity.User;

public interface IPhotoManagement {

	/** 
	 * TODO: No se debe devolver una foto sino un código que diga lo que ha pasado
	 * Por ejemplo 
	 * -1 -> Error al insertar
	 * 0 -> Insert correcto
	 * 1 -> Ya existe la foto
	 * 
	 */
	
	public static int INSERTED = 1;
	public static int EXISTS = -2;
	public static int ERROR = -1;
	
	public int insert(Photo photo, User user) throws Exception; 
	public Photo insertDiskPhoto(File f, byte[] data, Date lastModified, Date now, String generateMD5, User u, Map<String, Device> mapDevices) throws Exception;
	public List<Photo> getPhotosFromDir(String photoRepositoryPath)  throws Exception ;		
	public List<Photo> getPhotosFromLastSync(Date lastSync);		
}
