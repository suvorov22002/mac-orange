/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.afb.dpd.orangemoney.jpa.enums.TransactionStatus;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;

/**
 * Classe representant une transaction Orange Money a executer dans le Core Banking
 * @author Francis KONCHOU
 * @version 1.0
 */
@Entity
@Table(name = "OGMo_TRANS_ORAN")
public class TransactionOrange implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor
	 */
	public TransactionOrange() {}

	/**
	 * Id auto genere
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Operation
	 */
	@Column(name = "OP", nullable = false)
	@Enumerated(EnumType.STRING)
	private TypeOperation typeOperation;

	
	/**
	 * Montant
	 */
	@Column(name = "AMOUNT", nullable = false)
	private Double amount = 0d;

	
	/**
	 * b2wid
	 */
	@Column(name = "b2wid")
	private String b2wid;
	
	
	/**
	 * alias
	 */
	@Column(name = "alias")
	private String alias;
	
	
	@Column
	private String requestCode;
	
	
	@Column
	private String responseCode;
	
	
	@Column
	private String externalRefNo;
	
	
	@Column(name = "DATETIME_OP", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetime = new Date();
	
		
	/**
	 * Etat de la transaction
	 */
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private TransactionStatus status = TransactionStatus.PROCESSING;

		
	
	
	public TransactionOrange(Long id, TypeOperation typeOperation, Double amount, String b2wid, String alias,
			String requestCode, String responseCode, String externalRefNo, Date datetime, TransactionStatus status) {
		super();
		this.id = id;
		this.typeOperation = typeOperation;
		this.amount = amount;
		this.b2wid = b2wid;
		this.alias = alias;
		this.requestCode = requestCode;
		this.responseCode = responseCode;
		this.externalRefNo = externalRefNo;
		this.datetime = datetime;
		this.status = status;
	}
	


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public TypeOperation getTypeOperation() {
		return typeOperation;
	}


	public void setTypeOperation(TypeOperation typeOperation) {
		this.typeOperation = typeOperation;
	}


	public Double getAmount() {
		return amount;
	}
	

	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public String getB2wid() {
		return b2wid;
	}


	public void setB2wid(String b2wid) {
		this.b2wid = b2wid;
	}


	public String getAlias() {
		return alias;
	}


	public void setAlias(String alias) {
		this.alias = alias;
	}

	
	public String getRequestCode() {
		return requestCode;
	}


	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}


	public String getResponseCode() {
		return responseCode;
	}


	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}


	public String getExternalRefNo() {
		return externalRefNo;
	}


	public void setExternalRefNo(String externalRefNo) {
		this.externalRefNo = externalRefNo;
	}

	
	public Date getDatetime() {
		return datetime;
	}


	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}


	public TransactionStatus getStatus() {
		return status;
	}


	public void setStatus(TransactionStatus status) {
		this.status = status;
	}



	public String getColor() {
		if(this.status.equals(TransactionStatus.SUCCESS)){
			return "green";
		}
		if(this.status.equals(TransactionStatus.PROCESSING)){
			return "blue";
		}
		else{
			return "red";
		}
	}


}
