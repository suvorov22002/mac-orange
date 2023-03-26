/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.tools;

import java.io.Serializable;

/**
 * Classe
 * @author  
 * @version 1.0
 */
public class ClientProduit implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	
	private String matricule;
	
	private String produit;
	
	private StatusAbon statut;
	
	
	/**
	 * Default COnstructor
	 */
	public ClientProduit() {}

	
	public ClientProduit(String matricule, String produit, StatusAbon statut) {
		super();
		this.matricule = matricule;
		this.produit = produit;
		this.statut = statut;
	}

	

	/**
	 * @return the matricule
	 */
	public String getMatricule() {
		return matricule;
	}


	/**
	 * @param matricule the matricule to set
	 */
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}


	/**
	 * @return the produit
	 */
	public String getProduit() {
		return produit;
	}


	/**
	 * @param produit the produit to set
	 */
	public void setProduit(String produit) {
		this.produit = produit;
	}


	/**
	 * @return the statut
	 */
	public StatusAbon getStatut() {
		return statut;
	}


	/**
	 * @param statut the statut to set
	 */
	public void setStatut(StatusAbon statut) {
		this.statut = statut;
	}

}
