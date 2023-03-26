/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Classe representant une souscription
 * @author Yves LABO
 * @version 1.0
 */
@Entity
@Table(name = "OgMo_BranchMail")
public class BranchMails implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Default Constructor
	 */
	public BranchMails() {}

	/**
	 * Id auto genere
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
		
	@Column
	private String codeAgence;
	
	@Column
	private String libAgence;
	
	/**
	 * mails des utilisateurs de l'agence
	 */
    @CollectionOfElements(fetch = FetchType.EAGER)
	@JoinTable(
			name = "OGMo_USERMAILS",
			joinColumns = {@JoinColumn(name = "AGENCEID")}
	)
	@Column(name = "MAILS", nullable = false)
    @Fetch(FetchMode.SUBSELECT)
	private List<String> mails = new ArrayList<String>();


    
    
	/**
	 * 
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
	 * @return the codeAgence
	 */
	public String getCodeAgence() {
		return codeAgence;
	}

	/**
	 * @param codeAgence the codeAgence to set
	 */
	public void setCodeAgence(String codeAgence) {
		this.codeAgence = codeAgence;
	}

	/**
	 * @return the libAgence
	 */
	public String getLibAgence() {
		return libAgence;
	}

	/**
	 * @param libAgence the libAgence to set
	 */
	public void setLibAgence(String libAgence) {
		this.libAgence = libAgence;
	}

	/**
	 * @return the mails
	 */
	public List<String> getMails() {
		return mails;
	}

	/**
	 * @param mails the mails to set
	 */
	public void setMails(List<String> mails) {
		this.mails = mails;
	}

	
}
