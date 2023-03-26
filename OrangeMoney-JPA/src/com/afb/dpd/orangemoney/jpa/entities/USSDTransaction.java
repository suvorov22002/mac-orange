/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jpa.tools.OgMoHelper;

/**
 * @author Francis KONCHOU  USSDTransaction
 * @version 1.0
 */
@Entity
@Table(name = "OGMO_USSDTrans")
public class USSDTransaction implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private String lg_Mobile_Money_Trace_ID;
	
	private String lg_Remote_ID;
	
	private Integer int_Amount;
	
	private String str_Phone;
	
	private String str_Step;
	
	private  String str_Status;
	
	private Date dt_Created;
	
	private Date dt_Updated;
	
	private String str_Status_Code;
	
	private String str_Status_Description;
	
	private String str_Type;
	
	@Transient
	private Subscriber subscriber;
	
	@Transient
	private boolean selected; // = isAreconcilier();
	
	/**
	 * Default Constructor
	 */
	public USSDTransaction() {
		super();
	}

	/**
	 * @param lg_Mobile_Money_Trace_ID
	 * @param lg_Remote_ID
	 * @param int_Amount
	 * @param str_Phone
	 * @param str_Step
	 * @param str_Status
	 * @param dt_Created
	 * @param dt_Updated
	 * @param str_Status_Code
	 * @param str_Status_Description
	 * @param str_Type
	 */
	public USSDTransaction(String lg_Mobile_Money_Trace_ID,
			String lg_Remote_ID, Integer int_Amount, String str_Phone,
			String str_Step, String str_Status, Date dt_Created,
			Date dt_Updated, String str_Status_Code,
			String str_Status_Description, String str_Type) {
		super();
		this.lg_Mobile_Money_Trace_ID = lg_Mobile_Money_Trace_ID;
		this.lg_Remote_ID = lg_Remote_ID;
		this.int_Amount = int_Amount;
		this.str_Phone = str_Phone;
		this.str_Step = str_Step;
		this.str_Status = str_Status;
		this.dt_Created = dt_Created;
		this.dt_Updated = dt_Updated;
		this.str_Status_Code = str_Status_Code;
		this.str_Status_Description = str_Status_Description;
		this.str_Type = str_Type;
		this.selected = isAreconcilier();
	}

	public TypeOperation getTypeOperation(){
		return str_Type.equals("DEBIT") ? TypeOperation.Wallet2Account : TypeOperation.Account2Wallet;
	}
	
	public String getFormattedAmount(){
		return OgMoHelper.espacement(String.valueOf(int_Amount) );
	}

	public String getFormattedDate(){
		return new SimpleDateFormat("dd/MM/yyyy HH':'mm':'ss").format(dt_Created);
	}
	
	public boolean isAreconcilier(){
		return str_Step.equalsIgnoreCase("valide") && !str_Status.equalsIgnoreCase("valide") || str_Step.equalsIgnoreCase("CALL SERVICE") && str_Status.equalsIgnoreCase("confirme") ;
	}

	public boolean isOk(){
		return str_Step.equalsIgnoreCase("valide") && str_Status.equalsIgnoreCase("valide") ;
	}
	
	/**
	 * @return the lg_Mobile_Money_Trace_ID
	 */
	public String getLg_Mobile_Money_Trace_ID() {
		return lg_Mobile_Money_Trace_ID;
	}

	/**
	 * @param lg_Mobile_Money_Trace_ID the lg_Mobile_Money_Trace_ID to set
	 */
	public void setLg_Mobile_Money_Trace_ID(String lg_Mobile_Money_Trace_ID) {
		this.lg_Mobile_Money_Trace_ID = lg_Mobile_Money_Trace_ID;
	}

	/**
	 * @return the lg_Remote_ID
	 */
	public String getLg_Remote_ID() {
		return lg_Remote_ID;
	}

	/**
	 * @param lg_Remote_ID the lg_Remote_ID to set
	 */
	public void setLg_Remote_ID(String lg_Remote_ID) {
		this.lg_Remote_ID = lg_Remote_ID;
	}

	/**
	 * @return the int_Amount
	 */
	public Integer getInt_Amount() {
		return int_Amount;
	}

	/**
	 * @param int_Amount the int_Amount to set
	 */
	public void setInt_Amount(Integer int_Amount) {
		this.int_Amount = int_Amount;
	}

	/**
	 * @return the str_Phone
	 */
	public String getStr_Phone() {
		return str_Phone;
	}

	/**
	 * @param str_Phone the str_Phone to set
	 */
	public void setStr_Phone(String str_Phone) {
		this.str_Phone = str_Phone;
	}

	/**
	 * @return the str_Step
	 */
	public String getStr_Step() {
		return str_Step;
	}

	/**
	 * @param str_Step the str_Step to set
	 */
	public void setStr_Step(String str_Step) {
		this.str_Step = str_Step;
	}

	/**
	 * @return the str_Status
	 */
	public String getStr_Status() {
		return str_Status;
	}

	/**
	 * @param str_Status the str_Status to set
	 */
	public void setStr_Status(String str_Status) {
		this.str_Status = str_Status;
	}

	/**
	 * @return the dt_Created
	 */
	public Date getDt_Created() {
		return dt_Created;
	}

	/**
	 * @param dt_Created the dt_Created to set
	 */
	public void setDt_Created(Date dt_Created) {
		this.dt_Created = dt_Created;
	}

	/**
	 * @return the dt_Updated
	 */
	public Date getDt_Updated() {
		return dt_Updated;
	}

	/**
	 * @param dt_Updated the dt_Updated to set
	 */
	public void setDt_Updated(Date dt_Updated) {
		this.dt_Updated = dt_Updated;
	}

	/**
	 * @return the str_Status_Code
	 */
	public String getStr_Status_Code() {
		return str_Status_Code;
	}

	/**
	 * @param str_Status_Code the str_Status_Code to set
	 */
	public void setStr_Status_Code(String str_Status_Code) {
		this.str_Status_Code = str_Status_Code;
	}

	/**
	 * @return the str_Status_Description
	 */
	public String getStr_Status_Description() {
		return str_Status_Description;
	}

	/**
	 * @param str_Status_Description the str_Status_Description to set
	 */
	public void setStr_Status_Description(String str_Status_Description) {
		this.str_Status_Description = str_Status_Description;
	}

	/**
	 * @return the str_Type
	 */
	public String getStr_Type() {
		return str_Type;
	}

	/**
	 * @param str_Type the str_Type to set
	 */
	public void setStr_Type(String str_Type) {
		this.str_Type = str_Type;
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
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
