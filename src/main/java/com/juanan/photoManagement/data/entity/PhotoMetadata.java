package com.juanan.photoManagement.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the photo_metadata database table.
 * 
 */
@Entity
@Table(name="photo_metadata")
@NamedQuery(name="PhotoMetadata.findAll", query="SELECT p FROM PhotoMetadata p")
public class PhotoMetadata extends AbstractEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PHOTO_METADATA_METADATAID_GENERATOR", sequenceName="SEQ_PHOTO_METADATA", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PHOTO_METADATA_METADATAID_GENERATOR")
	@Column(name="metadata_id", unique=true, nullable=false, precision=32)
	private Long metadataId;

	@Column(nullable=false, length=100)
	private String name;

	//@Column(name="photo_id", nullable=false, precision=32)
	@ManyToOne
	@JoinColumn(name="photo_id", nullable=false)	
	private Photo photo;

	@Column(nullable=false)
	private String value;

	//bi-directional many-to-one association to MetadataType
	@ManyToOne
	@JoinColumn(name="metadata_type", nullable=false)
	private MetadataType metadataTypeBean;

	public PhotoMetadata() {
	}
	
	public Long getId() {
		return this.metadataId;
	}

	public Long getMetadataId() {
		return this.metadataId;
	}

	public void setMetadataId(Long metadataId) {
		this.metadataId = metadataId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MetadataType getMetadataTypeBean() {
		return this.metadataTypeBean;
	}

	public void setMetadataTypeBean(MetadataType metadataTypeBean) {
		this.metadataTypeBean = metadataTypeBean;
	}

}