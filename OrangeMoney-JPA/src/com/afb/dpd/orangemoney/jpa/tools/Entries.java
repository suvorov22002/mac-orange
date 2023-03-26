/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe
 * @author  
 * @version 1.0
 */
public class Entries implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	
	private List<bkmvtiPush> entries = new ArrayList<bkmvtiPush>();
	
	private List<String> opes = new ArrayList<String>();
	
	private List<String> eves = new ArrayList<String>();
	
	
	
	/**
	 * Default COnstructor
	 */
	public Entries() {}

	
	/**
	 * @param entries
	 * @param opes
	 * @param eves
	 */
	public Entries(List<bkmvtiPush> entries, List<String> opes, List<String> eves) {
		super();
		this.entries = entries;
		this.opes = opes;
		this.eves = eves;
	}


	/**
	 * @return the entries
	 */
	public List<bkmvtiPush> getEntries() {
		return entries;
	}


	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<bkmvtiPush> entries) {
		this.entries = entries;
	}


	/**
	 * @return the opes
	 */
	public List<String> getOpes() {
		return opes;
	}


	/**
	 * @param opes the opes to set
	 */
	public void setOpes(List<String> opes) {
		this.opes = opes;
	}


	/**
	 * @return the eves
	 */
	public List<String> getEves() {
		return eves;
	}


	/**
	 * @param eves the eves to set
	 */
	public void setEves(List<String> eves) {
		this.eves = eves;
	}

}
