/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.util.Date;

import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;

import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

/**
 * Classe representant un suspens Orange
 * @version 1.0
 */
//@Entity
//@Table(name = "OGMo_SUSORAN")
public class SuspensOrange implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;
		

	/**
	 * Id auto genere
	 */
	//@Id
	//@Column(name = "ID")
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * Date de l'operation
	 */
	//@Column(name = "DATE_OP")
	private Date date = new Date();
	
	
	//@Column
	private String libelle;
	
	//@Column
	private String referenceLettrage;
	
	//@Column
	private String sens;
	
	//@Column
	private Double montCredit = 0d;
	
	//@Column
	private Double montDebit = 0d;
	
	/**
	 * Numero de compte
	 */
	//@Column(name = "ACCOUNT")
	private String account;
	
	
	/**
	 * Operation
	 */
	//@Column(name = "OP", nullable = false)
	//@Enumerated(EnumType.STRING)
	private TypeOperation typeOperation;
	
	//@Column
	private String statut;
	
	//@Column(name = "DATE_REG")
	private Date dateRegularisation = new Date();
	
	//@Column(name = "MODE_REG")
	private String modeRegularisation = "";
	
	
	
	
	/**
	 * Default Constructor
	 */
	public SuspensOrange() {}
	
	

	public SuspensOrange(Long id, Date date, String libelle, String referenceLettrage, String sens, Double montCredit,
			Double montDebit, String account, TypeOperation typeOperation, String statut, Date dateRegularisation, String modeRegularisation) {
		super();
		this.id = id;
		this.date = date;
		this.libelle = libelle;
		this.referenceLettrage = referenceLettrage;
		this.sens = sens;
		this.montCredit = montCredit;
		this.montDebit = montDebit;
		this.account = account;
		this.typeOperation = typeOperation;
		this.statut = statut;
		this.dateRegularisation = dateRegularisation;
		this.modeRegularisation = modeRegularisation;		
	}

	
	public SuspensOrange(Transaction transaction, String statut, Date dateRegularisation, String modeRegularisation) {
		super();
		this.date = transaction.getDate();
		this.libelle = transaction.getLibelle();
		this.referenceLettrage = transaction.getReferenceLettrage();
		this.sens = transaction.getSens();
		this.montCredit = transaction.getMontCredit();
		this.montDebit = transaction.getMontDebit();
		this.account = transaction.getAccount();
		this.typeOperation = transaction.getTypeOperation();
		this.statut = statut;
		this.dateRegularisation = dateRegularisation;
		this.modeRegularisation = modeRegularisation;
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
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}


	public String getFormattedDate() {
		return PortalHelper.DEFAULT_DATE_FORMAT.format(date);
	}


	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getReferenceLettrage() {
		return referenceLettrage;
	}

	public void setReferenceLettrage(String referenceLettrage) {
		this.referenceLettrage = referenceLettrage;
	}

	public String getSens() {
		return sens;
	}

	public void setSens(String sens) {
		this.sens = sens;
	}

	public Double getMontCredit() {
		return montCredit;
	}

	public void setMontCredit(Double montCredit) {
		this.montCredit = montCredit;
	}

	public Double getMontDebit() {
		return montDebit;
	}

	public void setMontDebit(Double montDebit) {
		this.montDebit = montDebit;
	}


	public String getStatut() {
		return statut;
	}


	public void setStatut(String statut) {
		this.statut = statut;
	}


	public Date getDateRegularisation() {
		return dateRegularisation;
	}


	public void setDateRegularisation(Date dateRegularisation) {
		this.dateRegularisation = dateRegularisation;
	}


	public String getModeRegularisation() {
		return modeRegularisation;
	}


	public void setModeRegularisation(String modeRegularisation) {
		this.modeRegularisation = modeRegularisation;
	}
		


}
