package com.juanan.photoManagement.data.exception;

public class PhotoManagementDAOException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4841539609706000323L;
	
	private String detailedInfo = null;
	
	public PhotoManagementDAOException(String detailedInfo) {
		this.detailedInfo = detailedInfo;
	}

	public PhotoManagementDAOException(String detailedInfo, Exception e) {
		super(e);
		this.detailedInfo = new StringBuffer(detailedInfo).append(" Causa: ").append(e.getMessage()).toString();
	}

	public String getDetailedInfo() {
		return detailedInfo;
	}
	
	@Override
	public String getMessage() {
		return (detailedInfo!=null) ? detailedInfo : super.getMessage();
	}

}
