package com.juanan.photoManagement.data.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.juanan.photoManagement.data.entity.MetadataType;

@Repository
public class MetadataTypeDao extends AbstractDAO<MetadataType, Integer> {

	public MetadataTypeDao() {
		super(MetadataType.class);
	}

	public MetadataType selectByName(String name) {
		MetadataType metadataType = null;
		
		TypedQuery<MetadataType> q = getEntityManager().createQuery("select m from MetadataType m where m.metadataTypeName=:name", MetadataType.class);
		q.setParameter("name", name);

		List<MetadataType> resultList = q.getResultList();
		
		if ((!resultList.isEmpty()) && (resultList.size() == 1)) {
			metadataType = resultList.get(0);
		}
		
		return metadataType;		
	}
}
