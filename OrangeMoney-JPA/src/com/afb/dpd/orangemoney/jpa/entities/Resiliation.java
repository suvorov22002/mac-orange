/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe representant une resiliation dans le CBS
 * @author Yves FOKAM
 * @version 1.0
 */
@Entity
@Table(name = "OgMo_resil")
public class Resiliation implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Default Constructor
	 */
	public Resiliation() {}

	/**
	 * Id auto genere
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "id_subs")
	private Long idSubscriber;
	
	@Column(name = "client")
	private String client;
	
	@Column
	private String submsisdn;
	
    @Column(name = "NAME")
    private String customerName;
    	
	@Column(name = "date_resi")
	private String dateResiliation;
	
	@Column(name = "heure_resi")
	private String heureResiliation;
	
	@Column(name = "type_resil")
	private String typeResiliation;


	/**
	 * @param id
	 * @param idSubscriber
	 * @param client
	 * @param submsisdn
	 * @param customerName
	 * @param dateResiliation
	 * @param typeResiliation
	 */
	public Resiliation(Long id, Long idSubscriber, String client, String submsisdn, String customerName,
			String dateResiliation, String heureResiliation, String typeResiliation) {
		super();
		this.id = id;
		this.idSubscriber = idSubscriber;
		this.client = client;
		this.submsisdn = submsisdn;
		this.customerName = customerName;
		this.dateResiliation = dateResiliation;
		this.heureResiliation = heureResiliation;
		this.typeResiliation = typeResiliation;
	}


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the idSubscriber
	 */
	public Long getIdSubscriber() {
		return idSubscriber;
	}


	/**
	 * @param idSubscriber the idSubscriber to set
	 */
	public void setIdSubscriber(Long idSubscriber) {
		this.idSubscriber = idSubscriber;
	}


	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}


	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}


	/**
	 * @return the submsisdn
	 */
	public String getSubmsisdn() {
		return submsisdn;
	}


	/**
	 * @param submsisdn the submsisdn to set
	 */
	public void setSubmsisdn(String submsisdn) {
		this.submsisdn = submsisdn;
	}


	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}


	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	/**
	 * @return the dateResiliation
	 */
	public String getDateResiliation() {
		return dateResiliation;
	}


	/**
	 * @param dateResiliation the dateResiliation to set
	 */
	public void setDateResiliation(String dateResiliation) {
		this.dateResiliation = dateResiliation;
	}


	/**
	 * @return the heureResiliation
	 */
	public String getHeureResiliation() {
		return heureResiliation;
	}


	/**
	 * @param heureResiliation the heureResiliation to set
	 */
	public void setHeureResiliation(String heureResiliation) {
		this.heureResiliation = heureResiliation;
	}


	/**
	 * @return the typeResiliation
	 */
	public String getTypeResiliation() {
		return typeResiliation;
	}


	/**
	 * @param typeResiliation the typeResiliation to set
	 */
	public void setTypeResiliation(String typeResiliation) {
		this.typeResiliation = typeResiliation;
	}
	
	
	
	
}
