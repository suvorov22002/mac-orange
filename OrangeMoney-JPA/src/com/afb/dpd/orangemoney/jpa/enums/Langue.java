package com.afb.dpd.orangemoney.jpa.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Type Langue
 * @author FOKAM/JAZA
 * @version 1.0
 */
public enum Langue {
	
	/**
	 * English
	 */
	EN("English"),
	
	
	/**
	 * Français
	 */
	FR("Français");
	
	/**
	 * Valeur
	 */
	private String value;
	
	/**
	 * Constructeur
	 * @param value
	 */
	private Langue(String value){
		this.setValue(value);
	}
	
	/**
	 * Retourne la liste des valeus
	 * @return
	 */
	public static List<Langue> getValues() {
		
		// Initialisation de la collection a retourner
		List<Langue> ops = new ArrayList<Langue>();
		
		// Ajout des valeurs
		ops.add(EN);
		ops.add(FR);
		
		// Retourne la collection
		return ops;
		
	}

	public static Langue getValue(String shearch) {
		for(Langue t : Langue.getValues()){
			if(t.name().equalsIgnoreCase(shearch)) return t;
		}
		// Retourne la collection
		return null;
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
