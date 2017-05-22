package com.juanan.photoManagement.data.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the metadata_type database table.
 * 
 */
@Entity
@Table(name="metadata_type")
@NamedQuery(name="MetadataType.findAll", query="SELECT m FROM MetadataType m")
public class MetadataType extends AbstractEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="METADATA_TYPE_METADATATYPEID_GENERATOR", sequenceName="SEQ_METADATA_TYPE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="METADATA_TYPE_METADATATYPEID_GENERATOR")
	@Column(name="metadata_type_id", unique=true, nullable=false)
	private Integer metadataTypeId;

	@Column(name="metadata_type_name", nullable=false, length=20)
	private String metadataTypeName;

	//bi-directional many-to-one association to PhotoMetadata
	@OneToMany(mappedBy="metadataTypeBean")
	private List<PhotoMetadata> photoMetadata;

	public MetadataType() {
	}

	public Integer getId() {
		return metadataTypeId;		
	}
	
	public Integer getMetadataTypeId() {
		return this.metadataTypeId;
	}

	public void setMetadataTypeId(Integer metadataTypeId) {
		this.metadataTypeId = metadataTypeId;
	}

	public String getMetadataTypeName() {
		return this.metadataTypeName;
	}

	public void setMetadataTypeName(String metadataTypeName) {
		this.metadataTypeName = metadataTypeName;
	}

	public List<PhotoMetadata> getPhotoMetadata() {
		return this.photoMetadata;
	}

	public void setPhotoMetadata(List<PhotoMetadata> photoMetadata) {
		this.photoMetadata = photoMetadata;
	}

	public PhotoMetadata addPhotoMetadata(PhotoMetadata photoMetadata) {
		getPhotoMetadata().add(photoMetadata);
		photoMetadata.setMetadataTypeBean(this);

		return photoMetadata;
	}

	public PhotoMetadata removePhotoMetadata(PhotoMetadata photoMetadata) {
		getPhotoMetadata().remove(photoMetadata);
		photoMetadata.setMetadataTypeBean(null);

		return photoMetadata;
	}

}