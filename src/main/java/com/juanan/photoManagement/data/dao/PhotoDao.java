package com.juanan.photoManagement.data.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.juanan.photoManagement.data.entity.Photo;

@Repository
public class PhotoDao extends AbstractDAO<Photo, BigDecimal> {

	public PhotoDao() {
		super(Photo.class);
	}
	
	public Photo selectByMD5(String md5) {
		Photo photo = null;
		
		TypedQuery<Photo> q = getEntityManager().createQuery("select p from Photo p where p.md5=:md5", Photo.class);
		q.setParameter("md5", md5);

		List<Photo> resultList = q.getResultList();
		
		if ((!resultList.isEmpty()) && (resultList.size() == 1)) {
			photo = resultList.get(0);
		}
		
		return photo;
	}
	
	public List<Photo> selectByDateLaterThan(Date d) {
		List<Photo> photos = null;

		//TODO: Pending
		
		return photos;
	}

}
