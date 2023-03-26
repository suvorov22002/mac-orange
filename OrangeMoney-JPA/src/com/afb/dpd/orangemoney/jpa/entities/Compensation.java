/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Classe Representant
 * @author Francis KONCHOU
 * @version 1.0
 */
@Entity
@Table(name = "OGMO_COMP")
public class Compensation extends AbstractCompensation implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor
	 */
	public Compensation() {}

	/**
	 * Liste des ecritures de la transaction
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy="parent" , fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<CompensationDetails> compenses = new ArrayList<CompensationDetails>();

	/**
	 * @return the compenses
	 */
	public List<CompensationDetails> getCompenses(){
		return compenses;
	}

	/**
	 * @param compenses the compenses to set
	 */
	public void setCompenses(List<CompensationDetails> compenses) {
		this.compenses = compenses;
	}
	
}
