package com.afb.dpd.orangemoney.jpa.exception;

@SuppressWarnings("serial")
public class DAOAPIException extends Exception{

	public DAOAPIException(String string) {
		// TODO Auto-generated constructor stub
		super(string);
	}

	public DAOAPIException(Exception e) {
		// TODO Auto-generated constructor stub
		super(e);
	}

}
