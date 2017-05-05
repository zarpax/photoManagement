package com.juanan.photoManagement.data.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User extends AbstractEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_USERID_GENERATOR", sequenceName="SEQ_USER")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_USERID_GENERATOR")
	@Column(name="user_id", unique=true, nullable=false)
	private Integer userId;

	@Column(name="last_login", nullable=false)
	private Timestamp lastLogin;

	@Column(name="last_sync", nullable=false)
	private Timestamp lastSync;

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

	public Timestamp getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Timestamp getLastSync() {
		return this.lastSync;
	}

	public void setLastSync(Timestamp lastSync) {
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