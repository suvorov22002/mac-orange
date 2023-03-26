package com.afb.dpd.orangemoney.jpa.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;

public class TransactionDetail  {
	
    protected String tranRefNo;
    
    protected Date tranDate;
    
    protected String tranType;
    
    protected String ccy;
    
    protected String crDr;
    
    protected double amount;

    protected String narration;
    
    protected Map<String, String> map = new HashMap<String, String>();
    
    

	/**
	 * 
	 */
	public TransactionDetail() {
		super();
	}


	/**
	 * @param tranRefNo
	 * @param tranDate
	 * @param tranType
	 * @param ccy
	 * @param crDr
	 * @param amount
	 * @param narration
	 */
	public TransactionDetail(String tranRefNo, Date tranDate, String tranType,
			String ccy, String crDr, double amount, String narration) {
		super();
		this.tranRefNo = tranRefNo;
		this.tranDate = tranDate;
		this.tranType = tranType;
		this.ccy = ccy;
		this.crDr = crDr;
		this.amount = amount;
		this.narration = narration;
	}
	
	
	public TransactionDetail(Transaction t) {
		super();
		this.tranRefNo = t.getId().toString();
		this.tranDate = t.getDate();
		this.tranType = "";
		this.ccy = "";
		this.crDr = t.getTypeOperation().equals(TypeOperation.Account2Wallet)? "C" : "D";
		this.amount = t.getAmount();
		this.narration = "";
	}
	
	

	/**
	 * @return the map
	 */
	public Map<String, String> getMap() {
		return map;
	}


	/**
	 * @param map the map to set
	 */
	public void setMap(Map<String, String> map) {
		this.map = map;
	}


	/**
	 * @return the tranRefNo
	 */
	public String getTranRefNo() {
		return tranRefNo;
	}

	/**
	 * @param tranRefNo the tranRefNo to set
	 */
	public void setTranRefNo(String tranRefNo) {
		this.tranRefNo = tranRefNo;
	}

	/**
	 * @return the tranDate
	 */
	public Date getTranDate() {
		return tranDate;
	}

	/**
	 * @param tranDate the tranDate to set
	 */
	public void setTranDate(Date tranDate) {
		this.tranDate = tranDate;
	}

	/**
	 * @return the tranType
	 */
	public String getTranType() {
		return tranType;
	}

	/**
	 * @param tranType the tranType to set
	 */
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	/**
	 * @return the ccy
	 */
	public String getCcy() {
		return ccy;
	}

	/**
	 * @param ccy the ccy to set
	 */
	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	/**
	 * @return the crDr
	 */
	public String getCrDr() {
		return crDr;
	}

	/**
	 * @param crDr the crDr to set
	 */
	public void setCrDr(String crDr) {
		this.crDr = crDr;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the narration
	 */
	public String getNarration() {
		return narration;
	}

	/**
	 * @param narration the narration to set
	 */
	public void setNarration(String narration) {
		this.narration = narration;
	}

    
    
}
