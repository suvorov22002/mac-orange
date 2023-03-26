package com.afb.dpd.orangemoney.jpa.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Type Periodicite
 * @author Francis KONCHOU
 * @version 1.0
 */
public enum Periodicite {
	
	/**
	 * Mensuelle
	 */
	MOIS("Mensuelle"),

	/**
	 * Trimestrielle
	 */
	TRIMESTRE("Trimestrielle"),

	/**
	 * Semestrielle
	 */
	SEMESTRE("Semestrielle"),

	/**
	 * Annuelle
	 */
	ANNUEL("Annuelle");
	
	/**
	 * Valeur
	 */
	private String value;
	
	/**
	 * Constructeur
	 * @param value
	 */
	private Periodicite(String value){
		this.setValue(value);
	}
	
	/**
	 * Retourne la liste des valeus
	 * @return
	 */
	public static List<Periodicite> getValues() {
		
		// Initialisation de la collection a retourner
		List<Periodicite> ops = new ArrayList<Periodicite>();
		
		// Ajout des valeurs
		//ops.add(JOUR);
		//ops.add(SEMAINE);
		ops.add(MOIS);
		ops.add(TRIMESTRE);
		ops.add(SEMESTRE);
		ops.add(ANNUEL);
		
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
