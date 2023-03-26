package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractCompensation
 * @author Francis K
 * @version 1.0
 */
@MappedSuperclass
public class AbstractCompensation extends AbstractKey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Column
	private String fileName = "";
	
	/**
	 * 
	 */
	@Column
	private String uti = "";
	
	/**
	 * intDatetime
	 */
	@Column(name = "INIDATETIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date intDate = new Date();
	
	/**
	 * 
	 */
	public AbstractCompensation() {
		super();
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the uti
	 */
	public String getUti() {
		return uti;
	}

	/**
	 * @param uti the uti to set
	 */
	public void setUti(String uti) {
		this.uti = uti;
	}

	/**
	 * @return the intDate
	 */
	public Date getIntDate() {
		return intDate;
	}

	/**
	 * @param intDate the intDate to set
	 */
	public void setIntDate(Date intDate) {
		this.intDate = intDate;
	}
	
}
