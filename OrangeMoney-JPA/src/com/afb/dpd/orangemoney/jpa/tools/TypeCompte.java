/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.tools;

import java.io.Serializable;

/**
 * Classe representant un Type de compte dans Amplitude Pouvant souscrire au module Orange Money
 * @author Francis KONCHOU 
 * @version 1.0
 */
public class TypeCompte implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Code
	 */
	private String code;
	
	/**
	 * Libelle
	 */
	private String nom;
	
	/**
	 * Default COnstructor
	 */
	public TypeCompte() {}

	/**
	 * @param code
	 * @param nom
	 */
	public TypeCompte(String code, String nom) {
		super();
		this.code = code;
		this.nom = nom;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
}
