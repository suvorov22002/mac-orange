package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "OGMo_IDLE")
public class IdleOM implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Id auto genere
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date intiDate = new Date();
	
	@Column
	private Boolean responseCode;
	
	@Column
	private Boolean tfjo;
	
	@Column
	private String idlesend;

	/**
	 * @param intiDate
	 * @param responseCode
	 * @param tfjo
	 * @param idlesend
	 */
	public IdleOM(Boolean responseCode, Boolean tfjo, String idlesend) {
		super();
		this.responseCode = responseCode;
		this.tfjo = tfjo;
		this.idlesend = idlesend;
	}

	/**
	 * 
	 */
	public IdleOM() {
		super();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the intiDate
	 */
	public Date getIntiDate() {
		return intiDate;
	}

	/**
	 * @param intiDate the intiDate to set
	 */
	public void setIntiDate(Date intiDate) {
		this.intiDate = intiDate;
	}

	/**
	 * @return the responseCode
	 */
	public Boolean getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(Boolean responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the tfjo
	 */
	public Boolean getTfjo() {
		return tfjo;
	}

	/**
	 * @param tfjo the tfjo to set
	 */
	public void setTfjo(Boolean tfjo) {
		this.tfjo = tfjo;
	}

	/**
	 * @return the idlesend
	 */
	public String getIdlesend() {
		return idlesend;
	}

	/**
	 * @param idlesend the idlesend to set
	 */
	public void setIdlesend(String idlesend) {
		this.idlesend = idlesend;
	}
	
	
	
	
	
}
