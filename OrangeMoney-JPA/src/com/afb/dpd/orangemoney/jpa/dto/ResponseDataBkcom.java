package com.afb.dpd.orangemoney.jpa.dto;

import java.util.List;

import com.afb.dpd.orangemoney.jpa.tools.Bkcom;


public class ResponseDataBkcom extends ResponseBase{
	
	private static final long serialVersionUID = 1L;	
	private List<Bkcom> data;

	/**
	 * 
	 */
	public ResponseDataBkcom() {
		super();
	}

	/**
	 * @return the data
	 */
	public List<Bkcom> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<Bkcom> data) {
		this.data = data;
	}
	
	

}
