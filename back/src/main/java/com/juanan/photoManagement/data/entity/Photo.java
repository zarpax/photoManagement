package com.juanan.photoManagement.data.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the photo database table.
 * 
 */
@Entity
@Table(name="photo")
@NamedQuery(name="Photo.findAll", query="SELECT p FROM Photo p")
public class Photo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=128)
	private String md5;

	@Column(nullable=false, length=12)
	private String mime;

	@Column(nullable=false, length=255)
	private String name;

	@Column(nullable=false, length=255)
	private String path;

	@Column(name="photo_id", nullable=false, precision=32)
	private BigDecimal photoId;

	@Column(name="user_id", nullable=false)
	private Integer userId;

	public Photo() {
	}

	public String getMd5() {
		return this.md5;
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

	public BigDecimal getPhotoId() {
		return this.photoId;
	}

	public void setPhotoId(BigDecimal photoId) {
		this.photoId = photoId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}