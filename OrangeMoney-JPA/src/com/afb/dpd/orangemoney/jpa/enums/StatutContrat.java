package com.afb.dpd.orangemoney.jpa.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Statuts des contrats de souscription
 * @author Francis KONCHOU
 * @version 1.0
 */
public enum StatutContrat {
	
	/**
	 * Type de valeur Fixe
	 */
	ACTIF("Actif"),
	
	WAITING("En Attente"),
	
	/**
	 * Type valeur pourcentage
	 */
	SUSPENDU("Suspendu");
	
	/**
	 * Valeur
	 */
	private String value;
	
	/**
	 * Constructeur
	 * @param value
	 */
	private StatutContrat(String value){
		this.setValue(value);
	}
	
	/**
	 * Retourne la liste des valeus
	 * @return
	 */
	public static List<StatutContrat> getValues() {
		
		// Initialisation de la collection a retourner
		List<StatutContrat> ops = new ArrayList<StatutContrat>();
		
		// Ajout des valeurs
		ops.add(WAITING);
		ops.add(ACTIF);
		ops.add(SUSPENDU);
		
		// Retourne la collection
		return ops;
		
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
	public void setValue(String value) {
		this.value = value;
	}
	
}
