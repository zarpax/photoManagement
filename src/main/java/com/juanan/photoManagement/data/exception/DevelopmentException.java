package com.juanan.photoManagement.data.exception;

public class DevelopmentException extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8231155386868333900L;
	
	private String detailedInfo = null;

	public DevelopmentException(String detailedInfo) {
		this.detailedInfo = detailedInfo;
	}

	public DevelopmentException(String detailedInfo, Exception e) {
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
