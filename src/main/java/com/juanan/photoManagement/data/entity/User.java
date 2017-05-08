package com.juanan.photoManagement.data.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="person")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User extends AbstractEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_USERID_GENERATOR", sequenceName="SEQ_USER", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_USERID_GENERATOR")
	@Column(name="user_id", unique=true, nullable=false)
	private Integer userId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_login", nullable=false)
	private Date lastLogin;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_sync", nullable=false)
	private Date lastSync;

	@Column(nullable=false, length=16)
	private String name;

	@Column(length=256)
	private String password;

	public Integer getId() {
		return this.userId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getLastSync() {
		return lastSync;
	}

	public void setLastSync(Date lastSync) {
		this.lastSync = lastSync;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}