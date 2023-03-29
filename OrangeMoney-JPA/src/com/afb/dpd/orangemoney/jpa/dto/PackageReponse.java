package com.afb.dpd.orangemoney.jpa.dto;

import java.io.Serializable;

public class PackageReponse implements Serializable{
	
	private String response;

	/**
	 * 
	 */
	public PackageReponse() {
		super();
	}

	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	
	
}
