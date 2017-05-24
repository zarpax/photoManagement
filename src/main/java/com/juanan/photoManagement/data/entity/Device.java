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
 * The persistent class for the device database table.
 * 
 */
@Entity
@Table(name="device")
@NamedQuery(name="Device.findAll", query="SELECT d FROM Device d")
public class Device extends AbstractEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEVICE_DEVICEID_GENERATOR", sequenceName="SEQ_DEVICE", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEVICE_DEVICEID_GENERATOR")
	@Column(name="device_id", unique=true, nullable=false)
	private Integer deviceId;


	@Column(nullable = true)
	private Boolean active;

	@Column(nullable=false, length=256)
	private String name;

	@Column(name="screen_heigth")
	private Integer screenHeigth;

	@Column(name="screen_width")
	private Integer screenWidth;

	@ManyToOne
	@JoinColumn(name="person_id", nullable=true)		
	private User user;

	public Device() {
	}

	public Integer getId() {
		return deviceId;
	}
	public Integer getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getScreenHeigth() {
		return this.screenHeigth;
	}

	public void setScreenHeigth(Integer screenHeigth) {
		this.screenHeigth = screenHeigth;
	}

	public Integer getScreenWidth() {
		return this.screenWidth;
	}

	public void setScreenWidth(Integer screenWidth) {
		this.screenWidth = screenWidth;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User userId) {
		this.user = userId;
	}
}