package com.juanan.photoManagement.business;

import java.util.Date;
import java.util.List;

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
		
	public List<Photo> getPhotosFromLastSync(Date lastSync);
	
	
}
