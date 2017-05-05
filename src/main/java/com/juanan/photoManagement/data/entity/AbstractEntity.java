package com.juanan.photoManagement.data.entity;

import java.io.Serializable;

public abstract class AbstractEntity<ID> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6475981304907426399L;

	public abstract ID getId();
	
}
