/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jpa.enums.TypeRapprochement;

import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

/**
 * Classe representant un rapprochement des transactions
 * @author 	Yves LABO
 * @version 1.0
 */
public class Rapprochement implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Id auto genere
	 */
	private Long id;

	/**
	 * Operation
	 */
	private TypeOperation typeOperation;

	
	private String libelle;
	
	/**
	 * Date de l'operation
	 */
	private Date dateRappro = new Date();
	
	
	private Date datetimeRappro = new Date();
	
		
	private String ncpRappro;
	
	
	/**
	 * Type rapprochement
	 */
	private TypeRapprochement typeRapprochement;
	
	
	/**
	 * User Rapprochement
	 */
	private String userRapprochement;
	
	
	private Integer totalTransactions = 0;
	
	private Integer totalTransOrange = 0;
	
	
	private Integer totalSuspens = 0;
	
	private Integer totalSuspensOrange = 0;
	
	
	private Double solde = 0d;
	
	
	private String suspens;
	
	private String suspensOrange;
	
		
	
	
	/**
	 * transactions equilibrees
	 */	
	@Transient
	private List<Transaction> transactions = new ArrayList<Transaction>();
	
	@Transient
	private String detailSuspens;
	
	
	
	/**
	 * Default Constructor
	 */
	public Rapprochement() {}


	public Rapprochement(Long id, TypeOperation typeOperation, String libelle, Date dateRappro, Date datetimeRappro, String ncpRappro,
			TypeRapprochement typeRapprochement, String userRapprochement, Integer totalTransactions,
			Integer totalSuspens, Double solde, String suspens) {
		super();
		this.id = id;
		this.typeOperation = typeOperation;
		this.libelle = libelle;
		this.dateRappro = dateRappro;
		this.datetimeRappro = datetimeRappro;
		this.ncpRappro = ncpRappro;
		this.typeRapprochement = typeRapprochement;
		this.userRapprochement = userRapprochement;
		this.totalTransactions = totalTransactions;
		this.totalSuspens = totalSuspens;
		this.solde = solde;
		this.suspens = suspens;
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


	public String getLibelle() {
		return libelle;
	}


	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}


	public Date getDateRappro() {
		return dateRappro;
	}


	public void setDateRappro(Date dateRappro) {
		this.dateRappro = dateRappro;
	}


	public Date getDatetimeRappro() {
		return datetimeRappro;
	}


	public void setDatetimeRappro(Date datetimeRappro) {
		this.datetimeRappro = datetimeRappro;
	}


	public String getNcpRappro() {
		return ncpRappro;
	}


	public void setNcpRappro(String ncpRappro) {
		this.ncpRappro = ncpRappro;
	}


	public TypeRapprochement getTypeRapprochement() {
		return typeRapprochement;
	}


	public void setTypeRapprochement(TypeRapprochement typeRapprochement) {
		this.typeRapprochement = typeRapprochement;
	}


	public String getUserRapprochement() {
		return userRapprochement;
	}


	public void setUserRapprochement(String userRapprochement) {
		this.userRapprochement = userRapprochement;
	}


	public Integer getTotalTransactions() {
		return totalTransactions;
	}


	public void setTotalTransactions(Integer totalTransactions) {
		this.totalTransactions = totalTransactions;
	}


	public Integer getTotalSuspens() {
		return totalSuspens;
	}


	public void setTotalSuspens(Integer totalSuspens) {
		this.totalSuspens = totalSuspens;
	}


	public Double getSolde() {
		return solde;
	}


	public void setSolde(Double solde) {
		this.solde = solde;
	}


	public String getSuspens() {
		return suspens;
	}


	public void setSuspens(String suspens) {
		this.suspens = suspens;
	}


	public List<Transaction> getTransactions() {
		return transactions;
	}


	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	
	public String getDetailSuspens() {
		return detailSuspens;
	}


	public void setDetailSuspens(String detailSuspens) {
		this.detailSuspens = detailSuspens;
	}


	
	
	public Integer getTotalTransOrange() {
		return totalTransOrange;
	}


	public void setTotalTransOrange(Integer totalTransOrange) {
		this.totalTransOrange = totalTransOrange;
	}


	public Integer getTotalSuspensOrange() {
		return totalSuspensOrange;
	}


	public void setTotalSuspensOrange(Integer totalSuspensOrange) {
		this.totalSuspensOrange = totalSuspensOrange;
	}


	public String getSuspensOrange() {
		return suspensOrange;
	}


	public void setSuspensOrange(String suspensOrange) {
		this.suspensOrange = suspensOrange;
	}


	public String getFormattedDate() {
		return PortalHelper.DEFAULT_DATE_FORMAT.format(dateRappro);
	}



}
