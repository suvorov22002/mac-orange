package com.afb.dpd.orangemoney.jpa.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Type de Rapprochements
 * @author Yves LABO
 * @version 1.0
 */
public enum TypeRapprochement {

	/**
	 * Compensation
	 */
	COMPENSATION("Compensation"),
	
	/**
	 * Imputation
	 */
	IMPUTATION("Imputation");
	
	/**
	 * Valeur
	 */
	private String value;
	
	/**
	 * Constructeur
	 * @param value
	 */
	private TypeRapprochement(String value){
		this.setValue(value);
	}
	
	/**
	 * Retourne la liste des valeus
	 * @return
	 */
	public static List<TypeRapprochement> getValues() {
		
		// Initialisation de la collection a retourner
		List<TypeRapprochement> typeRapps = new ArrayList<TypeRapprochement>();
		
		// Ajout des valeurs
		typeRapps.add(COMPENSATION);
		typeRapps.add(IMPUTATION);
		
		// Retourne la collection
		return typeRapps;
		
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value){
		this.value = value;
	}
	
}
