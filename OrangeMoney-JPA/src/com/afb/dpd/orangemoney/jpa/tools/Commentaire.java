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
public class Commentaire implements Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * libelle
	 */
	private String libelle;
	
	/**
	 * valeur
	 */
	private String valeur;
	
	
	/**
	 * Default COnstructor
	 */
	public Commentaire() {}

	public Commentaire(String libelle, String valeur) {
		super();
		this.libelle = libelle;
		this.valeur = valeur;
	}

	/**
	 * @return the libelle
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * @param libelle the libelle to set
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	/**
	 * @return the valeur
	 */
	public String getValeur() {
		return valeur;
	}

	/**
	 * @param valeur the valeur to set
	 */
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
	

	// <a4j:commandButton style="background-color: #ffffff;color: black;border: 1px solid #A7A37E;border-radius: 8px;"  rendered="#{userHabilitation.menuUSSDTransEnabled}"  value="Simulation transactions" title="Simulation transactions" action="#{frmSimulation.open}" reRender="#{frmSimulation.areaToRender}" immediate="true" styleClass="ui-button" />
	
	// <a4j:commandButton style="background-color: #ffffff;color: black;border: 1px solid #A7A37E;border-radius: 8px;" rendered="#{userHabilitation.menuTFJOEnabled}" value="Comptabilisation des Abonnements" title="Comptabilisation des Abonnements" action="#{frmTFJO.open}" reRender="#{frmTFJO.areaToRender}" immediate="true" styleClass="ui-button" />
	
	// System.out
}
