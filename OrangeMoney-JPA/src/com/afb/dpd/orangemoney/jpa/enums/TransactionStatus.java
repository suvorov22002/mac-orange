package com.afb.dpd.orangemoney.jpa.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Type d'operations
 * @author Francis KONCHOU
 * @version 1.0
 */
public enum TransactionStatus {

	/**
	 * Transaction en cours de traitement
	 */
	PROCESSING("En Attente"),
	
	/**
	 * Echec de la transaction suite a une erreur
	 */
	FAILED("Echec"),

	/**
	 * Transaction annulee
	 */
	CANCEL("Annulée"),
	
	/**
	 * Regul
	 */
	REGUL("Regul"),
	
	
	/**
	 * Close
	 */
	CLOSE("Close"),
	
	
	/**
	 * Transaction correctement executee de bout en bout
	 */
	SUCCESS("Validée");
	
	/**
	 * Valeur
	 */
	private String value;
	
	/**
	 * Constructeur
	 * @param value
	 */
	private TransactionStatus(String value){
		this.setValue(value);
	}
	
	/**
	 * Retourne la liste des valeus
	 * @return
	 */
	public static List<TransactionStatus> getValues() {
		
		// Initialisation de la collection a retourner
		List<TransactionStatus> ops = new ArrayList<TransactionStatus>();
		
		// Ajout des valeurs
		ops.add(PROCESSING);
		ops.add(FAILED);
		ops.add(CANCEL);
		ops.add(SUCCESS);
		
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
	public void setValue(String value){
		this.value = value;
	}
	
}
