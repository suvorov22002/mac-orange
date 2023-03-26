package com.afb.dpd.orangemoney.jpa.tools;

import java.util.ArrayList;
import java.util.List;

public enum StatusAbon {

	ENATTENTE("En attente"),
	FACTURE("Factur�"),
	NONFACTURE("Non factur�"),
	RESILIE("R�sili�");


	/**
	 * Valeur de la constante (Code)
	 */
	private final String value;

	/**
	 * Liste des Types d'entr�es
	 */
	private static final List<StatusAbon> status = new ArrayList<StatusAbon>();


	static {

		status.add(ENATTENTE);
		status.add(FACTURE);
		status.add(NONFACTURE);
		status.add(RESILIE);
	}


	/**
	 * Constructeur avec initialisation des parametres
	 * @param value	Valeur du type d'entr�es
	 */
	private StatusAbon(String value) {

		// Initialisation de la valeur
		this.value = value;
	}

	/**
	 * Obtention de la valeur 
	 * @return	Valeur
	 */
	public String value() {
		return value;
	}

	/**
	 * Obtention de la valeur 
	 * @return	Valeur
	 */
	public String getValue() {
		return value;
	}
	/**
	 * Methode d'obtention des Types d'entr�es
	 * @return	Types d'ent�res
	 */
	public static List<StatusAbon> getValues () {

		// On retourne la liste
		return status;

	}



}
