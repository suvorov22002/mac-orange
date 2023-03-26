package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.afb.dpd.orangemoney.jpa.enums.TransactionStatus;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;

/**
 * CompensationDetails
 * @author Francis K
 * @version 1.0
 */
@Entity
@Table(name = "OGMO_COMP_DET")
public class CompensationDetails extends AbstractKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Souscripteur
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "COMPE_ID")
	private Compensation parent;
	
	/**
	 * Souscripteur
	 */
	@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn(name = "TRANS_ID")
	private Transaction transaction;	
	
	/**
	 * Etat de la transaction
	 */
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private TransactionStatus status = TransactionStatus.PROCESSING;
	
	/**
	 * Operation
	 */
	@Column(name = "OP", nullable = false)
	@Enumerated(EnumType.STRING)
	private TypeOperation typeOperation;
	
	@Column
	private String requestCode;
		
	@Column
	private String b2wid;
		
	@Column
	private String alias;
	
	/**
	 * Montant
	 */
	@Column(name = "AMOUNT", nullable = false)
	private Double amount = 0d;
	
	@Column
	private String type;
	
	@Column
	private String responseCode;
	
	@Column
	private String referenceNumber;
		
	@Column
	private String requestDateTxt;
		
	@Column(name = "requestDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestDate = new Date();
	
	/**
	 * Determine 
	 */
	@Column(name = "compenser")
	private Boolean compenser = Boolean.FALSE;

	
	/**
	 * CompensationDetails
	 */
	public CompensationDetails() {
		super();
	}
	
	/**
	 * @return the parent
	 */
	public Compensation getParent() {
		return parent;
	}



	/**
	 * @param parent the parent to set
	 */
	public void setParent(Compensation parent) {
		this.parent = parent;
	}



	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * @return the status
	 */
	public TransactionStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	/**
	 * @return the typeOperation
	 */
	public TypeOperation getTypeOperation() {
		return typeOperation;
	}

	/**
	 * @param typeOperation the typeOperation to set
	 */
	public void setTypeOperation(TypeOperation typeOperation) {
		this.typeOperation = typeOperation;
	}

	/**
	 * @return the requestCode
	 */
	public String getRequestCode() {
		return requestCode;
	}

	/**
	 * @param requestCode the requestCode to set
	 */
	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	/**
	 * @return the b2wid
	 */
	public String getB2wid() {
		return b2wid;
	}

	/**
	 * @param b2wid the b2wid to set
	 */
	public void setB2wid(String b2wid) {
		this.b2wid = b2wid;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the referenceNumber
	 */
	public String getReferenceNumber() {
		return referenceNumber;
	}

	/**
	 * @param referenceNumber the referenceNumber to set
	 */
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	/**
	 * @return the requestDateTxt
	 */
	public String getRequestDateTxt() {
		return requestDateTxt;
	}

	/**
	 * @param requestDateTxt the requestDateTxt to set
	 */
	public void setRequestDateTxt(String requestDateTxt) {
		this.requestDateTxt = requestDateTxt;
	}

	/**
	 * @return the requestDate
	 */
	public Date getRequestDate() {
		return requestDate;
	}

	/**
	 * @param requestDate the requestDate to set
	 */
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	/**
	 * @return the compenser
	 */
	public Boolean getCompenser() {
		return compenser;
	}

	/**
	 * @param compenser the compenser to set
	 */
	public void setCompenser(Boolean compenser) {
		this.compenser = compenser;
	}
	
}
