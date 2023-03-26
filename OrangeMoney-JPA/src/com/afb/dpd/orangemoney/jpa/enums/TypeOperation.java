package com.afb.dpd.orangemoney.jpa.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Type d'operations
 * @author Francis KONCHOU
 * @version 1.0
 */
public enum TypeOperation {
	
	/**
	 * OMViewHelper
	 */
	Account2Wallet("Account to Wallet"),
	
	TRANSACTION("TRANSACTION"),
	
	/**
	 * 
	 */
	Wallet2Account("Wallet to Account"),
	
	/**
	 * 
	 */
	TransferStatusInquiry("Check the status of a transfer"),
	
	
	
	MiniStatement("Mini-statement of a bank Account"),

	/**
	 * 
	 */
	BALANCE("Balance"),

	/**
	 * 
	 */
	REVERSAL("Reversal"),

	/**
	 * 
	 */
	SUBSCRIPTION("Souscription"),

	/**
	 * 
	 */
	MODIFY("Modification de contrat"),

	/**
	 * 
	 */
	COMPTABILISATION("Comptabilisation"),
	
	/**
	 * 
	 */
	CANCEL("Annulation de contrat");
	
	/**
	 * Valeur
	 */
	private String value;
	
	/**
	 * Constructeur
	 * @param value
	 */
	private TypeOperation(String value){
		this.setValue(value);
	}
	
	/**
	 * Retourne la liste des valeus
	 * @return
	 */
	public static List<TypeOperation> getValues() {
		
		// Initialisation de la collection a retourner
		List<TypeOperation> ops = new ArrayList<TypeOperation>();
		
		// Ajout des valeurs
		ops.add(Account2Wallet);
		ops.add(Wallet2Account);
		ops.add(SUBSCRIPTION);
		ops.add(BALANCE);
		ops.add(MiniStatement);
		ops.add(CANCEL);
		
		// Retourne la collection
		return ops;
		
	}
	
	
	
	/**
	 * Liste des Types non reconciliables 
	 * @return
	 */
	public static List<TypeOperation> getTypeNonReconciliable() {
		
		// Initialisation de la collection a retourner
		List<TypeOperation> ops = new ArrayList<TypeOperation>();
		
		// Ajout des valeurs
		ops.add(BALANCE);
		ops.add(MiniStatement);
		ops.add(SUBSCRIPTION);
		ops.add(COMPTABILISATION);
		
		// Retourne la collection
		return ops;
		
	}
	
	
	/**
	 * Liste des Types non reconciliables 
	 * @return
	 */
	public static List<TypeOperation> getTypeReconciliable() {
		
		// Initialisation de la collection a retourner
		List<TypeOperation> ops = new ArrayList<TypeOperation>();
		
		// Ajout des valeurs
		ops.add(Account2Wallet);
		ops.add(Wallet2Account);
		
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
