package com.juanan.photoManagement.data.exception;

public class PhotoManagementInfraestructureException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3693087490830919312L;
	
	private String detailedInfo = null;

	public PhotoManagementInfraestructureException(String detailedInfo) {
		this.detailedInfo = detailedInfo;
	}

	public PhotoManagementInfraestructureException(String detailedInfo, Exception e) {
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
