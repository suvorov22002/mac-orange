package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * PlageTransactions
 * @author Francis K 
 * @version 1.0
 */
@Entity
@Table(name = "OGMO_ProfMarch")
public class ProfilMarchands implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static final Long Default = 01l;

	/**
	 * PlageTransactions
	 */
	public ProfilMarchands() {}
	
	
	public ProfilMarchands(Long id) {
		super();
		this.id = id;
	}
	
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * profilName
	 */
	@Column
	private String profilName;
	
	@Column(name = "commissions")
	private Double commissions = 0d;
	
	/**
	 * Montant max du Pull
	 */
	@Column(name = "maxPullAmount")
	private Double maxPullAmount = 0d;
	
	/**
	 * Montant max du Push
	 */
	@Column(name = "maxPushAmount")
	private Double maxPushAmount = 0d;
	
	/**
	 * Montant max du Pull Day
	 */
	@Column(name = "maxPullAmountDay")
	private Double maxPullAmountDay = 0d;
	
	/**
	 * Montant max du Push Day
	 */
	@Column(name = "maxPushAmountDay")
	private Double maxPushAmountDay = 0d;
	
	
	
	/**
	 * Montant max du Push Week
	 */
	@Column(name = "maxPushAmountWeek")
	private Double maxPushAmountWeek = 0d;
	
	
	/**
	 * Montant max du Pull Week
	 */
	@Column(name = "maxPullAmountWeek")
	private Double maxPullAmountWeek = 0d;
	
	
	
	/**
	 * Montant max du Push Month
	 */
	@Column(name = "maxPushAmountMonth")
	private Double maxPushAmountMonth = 0d;
	
	
	/**
	 * Montant max du Pull Month
	 */
	@Column(name = "maxPullAmountMonth")
	private Double maxPullAmountMonth = 0d;
	
	
	
	/**
     * DAP des Operations de Push
     */
    @Column(name = "NCPDAPPUSH")
    private String ncpDAPPush = "00001-02523481001-82";

    /**
     * DAP des operations de Pull
     */
    @Column(name = "NCPDAPPULL")
    private String ncpDAPPull = "00001-02523481001-82";

    /**
     * Numero de compte de MTN
     */
    @Column(name = "ncpMTN")
    private String numCompteMTN = "00001-02523481001-82";

    /**
     * Numero de compte des Commissions
     */
    @Column(name = "numCompteCommissions")
    private String numCompteCommissions = "00001-72900090301-90";

    /**
     * Numero de compte TVA
     */
    @Column(name = "numCompteTVA")
    private String numCompteTVA = "00001-43400090035-17";

    /**
     * Numero de compte de liaison
     */
    @Column(name = "numCompteLiaison")
    private String numCompteLiaison = "45920090100";
    
    /**
     * Activation/Desactivation du Service
     */
    @Column(name = "ACTIF")
    private Boolean active = Boolean.TRUE;

    /**
     * Authorise les transactions pendant les TFJ
     */
    @Column(name = "ALLOW_TRANS_IN_TFJ")
    private Boolean allowTransDuringTFJO = Boolean.TRUE;
    
    
    
    @Transient
	private boolean validerDesact = false;
    
    
    @Transient
	private String commissionsTrans;
	
    @Transient
	private String maxPullAmountTrans;
	
    @Transient
	private String maxPushAmountTrans;
	
    @Transient
	private String maxPullAmountDayTrans;
	
    @Transient
	private String maxPushAmountDayTrans;
	
    @Transient
	private String maxPushAmountWeekTrans;
	
    @Transient
	private String maxPullAmountWeekTrans;
	
    @Transient
	private String maxPushAmountMonthTrans;
		
    @Transient
	private String maxPullAmountMonthTrans;
    
    


	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the commissions
	 */
	public Double getCommissions() {
		//*** return commissions;
		return commissions != null ? commissions : 0d;
	}


	/**
	 * @param commissions the commissions to set
	 */
	public void setCommissions(Double commissions) {
		this.commissions = commissions;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the profilName
	 */
	public String getProfilName() {
		return profilName;
	}

	/**
	 * @param profilName the profilName to set
	 */
	public void setProfilName(String profilName) {
		this.profilName = profilName;
	}

	/**
	 * @return the maxPullAmount
	 */
	public Double getMaxPullAmount() {
		//*** return maxPullAmount;
		return maxPullAmount != null ? maxPullAmount : 0d;
	}

	/**
	 * @param maxPullAmount the maxPullAmount to set
	 */
	public void setMaxPullAmount(Double maxPullAmount) {
		this.maxPullAmount = maxPullAmount;		
	}

	/**
	 * @return the maxPushAmount
	 */
	public Double getMaxPushAmount() {
		//*** return maxPushAmount;
		return maxPushAmount != null ? maxPushAmount : 0d;
	}

	/**
	 * @param maxPushAmount the maxPushAmount to set
	 */
	public void setMaxPushAmount(Double maxPushAmount) {
		this.maxPushAmount = maxPushAmount;
	}

	/**
	 * @return the maxPullAmountDay
	 */
	public Double getMaxPullAmountDay() {
		//*** return maxPullAmountDay;
		return maxPullAmountDay != null ? maxPullAmountDay : 0d;
	}

	/**
	 * @param maxPullAmountDay the maxPullAmountDay to set
	 */
	public void setMaxPullAmountDay(Double maxPullAmountDay) {
		this.maxPullAmountDay = maxPullAmountDay;
	}

	/**
	 * @return the maxPushAmountDay
	 */
	public Double getMaxPushAmountDay() {
		//*** return maxPushAmountDay;
		return maxPushAmountDay != null ? maxPushAmountDay : 0d;
	}

	/**
	 * @param maxPushAmountDay the maxPushAmountDay to set
	 */
	public void setMaxPushAmountDay(Double maxPushAmountDay) {
		this.maxPushAmountDay = maxPushAmountDay;
	}
	
	/**
	 * @return the maxPushAmountWeek
	 */
	public Double getMaxPushAmountWeek() {
		//*** return maxPushAmountWeek;
		return maxPushAmountWeek != null ? maxPushAmountWeek : 0d;
	}


	/**
	 * @param maxPushAmountWeek the maxPushAmountWeek to set
	 */
	public void setMaxPushAmountWeek(Double maxPushAmountWeek) {
		this.maxPushAmountWeek = maxPushAmountWeek;
	}


	/**
	 * @return the maxPullAmountWeek
	 */
	public Double getMaxPullAmountWeek() {
		//**** return maxPullAmountWeek;
		return maxPullAmountWeek != null ? maxPullAmountWeek : 0d;
	}


	/**
	 * @param maxPullAmountWeek the maxPullAmountWeek to set
	 */
	public void setMaxPullAmountWeek(Double maxPullAmountWeek) {
		this.maxPullAmountWeek = maxPullAmountWeek;
	}


	/**
	 * @return the maxPushAmountMonth
	 */
	public Double getMaxPushAmountMonth() {
		//*** return maxPushAmountMonth;
		return maxPushAmountMonth != null ? maxPushAmountMonth : 0d;
	}


	/**
	 * @param maxPushAmountMonth the maxPushAmountMonth to set
	 */
	public void setMaxPushAmountMonth(Double maxPushAmountMonth) {
		this.maxPushAmountMonth = maxPushAmountMonth;
	}


	/**
	 * @return the maxPullAmountMonth
	 */
	public Double getMaxPullAmountMonth() {
		//*** return maxPullAmountMonth;
		return maxPullAmountMonth != null ? maxPullAmountMonth : 0d;
	}


	/**
	 * @param maxPullAmountMonth the maxPullAmountMonth to set
	 */
	public void setMaxPullAmountMonth(Double maxPullAmountMonth) {
		this.maxPullAmountMonth = maxPullAmountMonth;
	}


	/**
	 * @return the ncpDAPPush
	 */
	public String getNcpDAPPush() {
		return ncpDAPPush;
	}

	/**
	 * @param ncpDAPPush the ncpDAPPush to set
	 */
	public void setNcpDAPPush(String ncpDAPPush) {
		this.ncpDAPPush = ncpDAPPush;
	}

	/**
	 * @return the ncpDAPPull
	 */
	public String getNcpDAPPull() {
		return ncpDAPPull;
	}

	/**
	 * @param ncpDAPPull the ncpDAPPull to set
	 */
	public void setNcpDAPPull(String ncpDAPPull) {
		this.ncpDAPPull = ncpDAPPull;
	}

	/**
	 * @return the numCompteMTN
	 */
	public String getNumCompteMTN() {
		return numCompteMTN;
	}

	/**
	 * @param numCompteMTN the numCompteMTN to set
	 */
	public void setNumCompteMTN(String numCompteMTN) {
		this.numCompteMTN = numCompteMTN;
	}

	/**
	 * @return the numCompteCommissions
	 */
	public String getNumCompteCommissions() {
		return numCompteCommissions;
	}

	/**
	 * @param numCompteCommissions the numCompteCommissions to set
	 */
	public void setNumCompteCommissions(String numCompteCommissions) {
		this.numCompteCommissions = numCompteCommissions;
	}

	/**
	 * @return the numCompteTVA
	 */
	public String getNumCompteTVA() {
		return numCompteTVA;
	}

	/**
	 * @param numCompteTVA the numCompteTVA to set
	 */
	public void setNumCompteTVA(String numCompteTVA) {
		this.numCompteTVA = numCompteTVA;
	}

	/**
	 * @return the numCompteLiaison
	 */
	public String getNumCompteLiaison() {
		return numCompteLiaison;
	}

	/**
	 * @param numCompteLiaison the numCompteLiaison to set
	 */
	public void setNumCompteLiaison(String numCompteLiaison) {
		this.numCompteLiaison = numCompteLiaison;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the allowTransDuringTFJO
	 */
	public Boolean getAllowTransDuringTFJO() {
		return allowTransDuringTFJO;
	}

	/**
	 * @param allowTransDuringTFJO the allowTransDuringTFJO to set
	 */
	public void setAllowTransDuringTFJO(Boolean allowTransDuringTFJO) {
		this.allowTransDuringTFJO = allowTransDuringTFJO;
	}
	
	
	
	public boolean isValiderDesact() {
		return validerDesact;
	}
	
	public void setValiderDesact(boolean validerDesact) {
		this.validerDesact = validerDesact;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PlageTransactions [maxPullAmount=" + maxPullAmount
				+ ", maxPullAmountDay=" + maxPullAmountDay + ", profilName="
				+ profilName + "]";
	}


	/**
	 * @return the commissionsTrans
	 */
	public String getCommissionsTrans() {
		return commissionsTrans != null ? commissionsTrans : String.valueOf(commissions != null ? commissions.intValue() : 0);
	}


	/**
	 * @param commissionsTrans the commissionsTrans to set
	 */
	public void setCommissionsTrans(String commissionsTrans) {
		this.commissionsTrans = commissionsTrans;
	}


	/**
	 * @return the maxPullAmountTrans
	 */
	public String getMaxPullAmountTrans() {
		return maxPullAmountTrans != null ? maxPullAmountTrans : String.valueOf(maxPullAmount != null ? maxPullAmount.intValue() : 0);
	}


	/**
	 * @param maxPullAmountTrans the maxPullAmountTrans to set
	 */
	public void setMaxPullAmountTrans(String maxPullAmountTrans) {
		this.maxPullAmountTrans = maxPullAmountTrans;
	}


	/**
	 * @return the maxPushAmountTrans
	 */
	public String getMaxPushAmountTrans() {
		return maxPushAmountTrans != null ? maxPushAmountTrans : String.valueOf(maxPushAmount != null ? maxPushAmount.intValue() : 0);
	}


	/**
	 * @param maxPushAmountTrans the maxPushAmountTrans to set
	 */
	public void setMaxPushAmountTrans(String maxPushAmountTrans) {
		this.maxPushAmountTrans = maxPushAmountTrans;
	}


	/**
	 * @return the maxPullAmountDayTrans
	 */
	public String getMaxPullAmountDayTrans() {
		return maxPullAmountDayTrans != null ? maxPullAmountDayTrans : String.valueOf(maxPullAmountDay != null ? maxPullAmountDay.intValue() : 0); 
	}


	/**
	 * @param maxPullAmountDayTrans the maxPullAmountDayTrans to set
	 */
	public void setMaxPullAmountDayTrans(String maxPullAmountDayTrans) {
		this.maxPullAmountDayTrans = maxPullAmountDayTrans;
	}


	/**
	 * @return the maxPushAmountDayTrans
	 */
	public String getMaxPushAmountDayTrans() {
		return maxPushAmountDayTrans != null ? maxPushAmountDayTrans : String.valueOf(maxPushAmountDay != null ? maxPushAmountDay.intValue() : 0);
	}


	/**
	 * @param maxPushAmountDayTrans the maxPushAmountDayTrans to set
	 */
	public void setMaxPushAmountDayTrans(String maxPushAmountDayTrans) {
		this.maxPushAmountDayTrans = maxPushAmountDayTrans;
	}


	/**
	 * @return the maxPushAmountWeekTrans
	 */
	public String getMaxPushAmountWeekTrans() {
		return maxPushAmountWeekTrans != null ? maxPushAmountWeekTrans : String.valueOf(maxPushAmountWeek != null ? maxPushAmountWeek.intValue() : 0);
	}


	/**
	 * @param maxPushAmountWeekTrans the maxPushAmountWeekTrans to set
	 */
	public void setMaxPushAmountWeekTrans(String maxPushAmountWeekTrans) {
		this.maxPushAmountWeekTrans = maxPushAmountWeekTrans;
	}


	/**
	 * @return the maxPullAmountWeekTrans
	 */
	public String getMaxPullAmountWeekTrans() {
		return maxPullAmountWeekTrans != null ? maxPullAmountWeekTrans : String.valueOf(maxPullAmountWeek != null ? maxPullAmountWeek.intValue() : 0); 
	}


	/**
	 * @param maxPullAmountWeekTrans the maxPullAmountWeekTrans to set
	 */
	public void setMaxPullAmountWeekTrans(String maxPullAmountWeekTrans) {
		this.maxPullAmountWeekTrans = maxPullAmountWeekTrans;
	}


	/**
	 * @return the maxPushAmountMonthTrans
	 */
	public String getMaxPushAmountMonthTrans() {
		return maxPushAmountMonthTrans != null ? maxPushAmountMonthTrans : String.valueOf(maxPushAmountMonth != null ? maxPushAmountMonth.intValue() : 0);
	}


	/**
	 * @param maxPushAmountMonthTrans the maxPushAmountMonthTrans to set
	 */
	public void setMaxPushAmountMonthTrans(String maxPushAmountMonthTrans) {
		this.maxPushAmountMonthTrans = maxPushAmountMonthTrans;
	}


	/**
	 * @return the maxPullAmountMonthTrans
	 */
	public String getMaxPullAmountMonthTrans() {
		return maxPullAmountMonthTrans != null ? maxPullAmountMonthTrans : String.valueOf(maxPullAmountMonth != null ? maxPullAmountMonth.intValue() : 0);
	}


	/**
	 * @param maxPullAmountMonthTrans the maxPullAmountMonthTrans to set
	 */
	public void setMaxPullAmountMonthTrans(String maxPullAmountMonthTrans) {
		this.maxPullAmountMonthTrans = maxPullAmountMonthTrans;
	}   
	
	
    
}
