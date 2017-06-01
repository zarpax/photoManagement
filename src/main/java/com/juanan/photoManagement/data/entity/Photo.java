package com.juanan.photoManagement.data.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the photo database table.
 * 
 */
@Entity
@Table(name="photo")
@NamedQuery(name="Photo.findAll", query="SELECT p FROM Photo p")
public class Photo extends AbstractEntity<BigDecimal> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PHOTO_PHOTOID_GENERATOR", sequenceName="SEQ_PHOTO", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PHOTO_PHOTOID_GENERATOR")
	@Column(name="photo_id", unique=true, nullable=false, precision=32)
	private BigDecimal photoId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date inserted;

	@Column(nullable=false)
	@JsonIgnore
	private String md5;

	@Column(nullable=false, length=12)
	private String mime;

	@Column(nullable=false, length=255)
	private String name;

	@Column(nullable=false, length=255)
	@JsonIgnore
	private String path;

	//@Column(name="person_id", nullable=false)
	@ManyToOne
	@JoinColumn(name="person_id", nullable=false)	
	@JsonIgnore
	private User user;
	
	@Transient
	private byte[] bytes;
	
	/** TODO: Review of this properties for seen if they are needed
	 * path -> no
	 * 
	 * New metadata info:
	 * size
	 * coords
	 * taken with
	 * tags
	 * valoration
	 * heigh
	 * width
	 * numBits
	 */

	public BigDecimal getId() {
		return photoId;
	}
	
	public BigDecimal getPhotoId() {
		return photoId;
	}
	
	public void setPhotoId(BigDecimal photoId) {
		this.photoId = photoId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getInserted() {
		return inserted;
	}

	public void setInserted(Date inserted) {
		this.inserted = inserted;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getMime() {
		return this.mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}