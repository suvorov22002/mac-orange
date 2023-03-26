/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;

import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;

/**
 * Classe representant un message
 * @author Francis KONCHOU
 * @version 1.0
 */
public class RequestMessage implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor
	 */
	public RequestMessage() {}

	private TypeOperation operation;
	
	private String bankPIN;
	
	private String phoneNumber = "237";
	
	private Double amount = 0d;
	
	private String account;
	
	private Subscriber subscriber;
	
	private String externalRefNo;
	
	private String requestToken;
	
	private String operatorCode;
	
	private String accountAlias;
	
	private String requestId;
	
	private Double charge = 0d;

	/**
	 * @param operation
	 * @param bankPIN
	 * @param phoneNumber
	 * @param amount
	 * @param account
	 */
	public RequestMessage(TypeOperation operation, String bankPIN,
			String phoneNumber, Double amount, String account) {
		super();
		this.operation = operation;
		this.bankPIN = bankPIN;
		this.phoneNumber = phoneNumber;
		this.amount = amount;
		this.account = account;
	}
	
	
	public RequestMessage(TypeOperation operation, String bankPIN,
			String phoneNumber, Double amount, String account,Subscriber subscriber) {
		super();
		this.operation = operation;
		this.bankPIN = bankPIN;
		this.phoneNumber = phoneNumber;
		this.amount = amount;
		this.account = account;
		this.subscriber = subscriber;
	}
	
	public RequestMessage(TypeOperation operation, String bankPIN,
			String phoneNumber, Double amount, String account,Subscriber subscriber,Double charge,String requestId,String externalRefNo,String requestToken,
			String operatorCode,String accountAlias) {
		super();
		this.operation = operation;
		this.bankPIN = bankPIN;
		this.phoneNumber = phoneNumber;
		this.amount = amount;
		this.account = account;
		this.subscriber = subscriber;
		this.requestId = requestId;
		this.charge = charge;
		this.externalRefNo = externalRefNo;
		this.requestToken = requestToken;
		this.operatorCode = operatorCode;
		this.accountAlias = accountAlias;
	}

	
	
	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}


	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}


	/**
	 * @return the charge
	 */
	public Double getCharge() {
		return charge;
	}


	/**
	 * @param charge the charge to set
	 */
	public void setCharge(Double charge) {
		this.charge = charge;
	}


	/**
	 * @return the operation
	 */
	public TypeOperation getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(TypeOperation operation) {
		this.operation = operation;
	}

	/**
	 * @return the bankPIN
	 */
	public String getBankPIN() {
		return bankPIN;
	}

	/**
	 * @param bankPIN the bankPIN to set
	 */
	public void setBankPIN(String bankPIN) {
		this.bankPIN = bankPIN;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the subscriber
	 */
	public Subscriber getSubscriber() {
		return subscriber;
	}

	/**
	 * @param subscriber the subscriber to set
	 */
	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}


	/**
	 * @return the externalRefNo
	 */
	public String getExternalRefNo() {
		return externalRefNo;
	}


	/**
	 * @param externalRefNo the externalRefNo to set
	 */
	public void setExternalRefNo(String externalRefNo) {
		this.externalRefNo = externalRefNo;
	}


	/**
	 * @return the requestToken
	 */
	public String getRequestToken() {
		return requestToken;
	}


	/**
	 * @param requestToken the requestToken to set
	 */
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}


	/**
	 * @return the operatorCode
	 */
	public String getOperatorCode() {
		return operatorCode;
	}


	/**
	 * @param operatorCode the operatorCode to set
	 */
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}


	/**
	 * @return the accountAlias
	 */
	public String getAccountAlias() {
		return accountAlias;
	}


	/**
	 * @param accountAlias the accountAlias to set
	 */
	public void setAccountAlias(String accountAlias) {
		this.accountAlias = accountAlias;
	}
	
	

}
