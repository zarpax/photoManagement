package com.juanan.photoManagement.business;

import java.util.Date;
import java.util.List;

import com.juanan.photoManagement.data.entity.Photo;
import com.juanan.photoManagement.data.exception.PhotoManagementInfraestructureException;

public interface IPhotoManagement {

	/** 
	 * TODO: No se debe devolver una foto sino un código que diga lo que ha pasado
	 * Por ejemplo 
	 * -1 -> Error al insertar
	 * 0 -> Insert correcto
	 * 1 -> Ya existe la foto
	 * 
	 */
	
	public Photo insert(Photo photo); 
	
	public boolean exists(Photo photo) throws PhotoManagementInfraestructureException;
	
	public List<Photo> getPhotosFromLastSync(Date lastSync);
	
	
}
