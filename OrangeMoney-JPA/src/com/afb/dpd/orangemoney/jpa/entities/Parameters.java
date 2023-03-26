/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.afb.dpd.orangemoney.jpa.tools.OgMoHelper;
import com.yashiro.persistence.utils.dao.tools.encrypter.Encrypter;


/**
 * Parametres Generaux du Module Orange 
 * @author Francis KONCHOU / Yves FOKAM
 * @version 1.0
 */
@Entity
@Table(name = "OGMo_PRMTRS")
public class Parameters implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor
	 */
	public Parameters() {}

	/**
	 * Code par defaut
	 */
	public static final String CODE_PARAM = "ORMo_PARAMS";

	/**
	 * Code du parametre
	 */
	@Id
	@Column(name = "CODE")
	private String code = Encrypter.getInstance().hashText(CODE_PARAM);

	/**
	 * Longueur du PIN banque
	 */
	@Column(name = "bankPINLength")
	private Integer bankPINLength = 22;
	
	
	private Double tva = 19.25d;
	
	/**
	 * bankBIC
	 */
	@Column(name = "bankBIC")
	private String bankBIC = "BNKSXGWE";
	
	@Column(name = "registerService")
	private String registerService = "https://b2w-sb.orange-money.com/register/?bic=";
	
	@Column(name = "inquiryService")
	private String inquiryService = "https://b2w-sb.orange-money.com/status-inquiry/?bic=";
	
	@Column(name = "idlService")
	private String idlService = "https://b2w-sb.orange-money.com/idle/?bic=";
	
	
	@Column(name = "operatorCode")
	private String operatorCode = "ORANGEMONEYSX";
	
	@Column(name = "affiliateCode")
	private String affiliateCode = "OML";
	
	@Column(name = "Allias")
	private String allias = "CCEICMCX";
	
	@Column(name = "currency")
	private String currency = "952";

	/**
	 * PIN minimal
	 */
	@Column(name = "minPIN")
	private Integer minPIN = 1240;

	/**
	 * PIN maximal
	 */
	@Column(name = "maxPIN")
	private Integer maxPIN = 99999;

	/**
	 * Nbre de comptes autorises
	 */
	@Column(name = "maxAccounts")
	private Integer maxAccounts = 1;

	/**
	 * Nbre de numeros de telephones autorises
	 */
	@Column(name = "maxPhoneNumbers")
	private Integer maxPhoneNumbers = 1;

	/**
	 * Duree de traitement d'une transaction
	 */
	@Column(name = "trnsactionTimeOut")
	private Integer trnsactionTimeOut = 60;

	/**
	 * Montant max du Acount to Walet
	 */
	@Column(name = "maxAWAmount")
	private Double maxAWAmount = 0d;

	/**
	 * Montant max du Walet to Acount
	 */
	@Column(name = "maxWAAmount")
	private Double maxWAAmount = 0d;
	
	/**
	 * Montant max du Acount to Walet Day
	 */
	@Column(name = "maxAWAmountDays")
	private Double maxAWAmountDays = 0d;

	/**
	 * Montant max du Walet to Acount Day
	 */
	@Column(name = "maxWAAmountDays")
	private Double maxWAAmountDays = 0d;
	
	
	/**
	 * Montant max du Acount to Walet Week
	 */
	@Column(name = "maxAWAmountWeeks")
	private Double maxAWAmountWeeks = 0d;

	/**
	 * Montant max du Walet to Acount Week
	 */
	@Column(name = "maxWAAmountWeeks")
	private Double maxWAAmountWeeks = 0d;
	
	
	
	/**
	 * Montant max du Acount to Walet Month
	 */
	@Column(name = "maxAWAmountMonths")
	private Double maxAWAmountMonths = 0d;

	/**
	 * Montant max du Walet to Acount Month
	 */
	@Column(name = "maxWAAmountMonths")
	private Double maxWAAmountMonths = 0d;
	
	
	
	@Column(name = "idleAuto")
	private Boolean idleAuto = Boolean.TRUE;
	
	@Column(name = "modeNuit")
	private Boolean modeNuit = Boolean.TRUE;

	/**
	 * Adresse de Test de la plateforme SDP
	 */
	@Column(name = "ipSDPTest")
	private String ipSDPTest = "41.206.4.219";

	/**
	 * Adresse de production de la plateforme SDP
	 */
	@Column(name = "ipSDPProd")
	private String addressSDPProd = "41.206.4.162";

	/**
	 * Code operation a utiliser dans le core banking
	 */
	@Column(name = "codeOpe")
	private String codeOpe = "000";
	
	/**
	 * Code operation a utiliser pour effectuer les transactions
	 */
	@Column(name = "codeOpeCompt")
	private String codeOpeCompt = "000";
	

	/**
	 * Code de l'utilisateur dans Delta
	 */
	@Column(name = "uti")
	private String uti = "OGMO";

	/**
	 * DAP des Operations WA
	 */
	@Column(name = "NCPDAPWA")
	private String ncpDAPWaletAcount = "00001-02523481001-82";

	/**
	 * DAP des operations AW
	 */
	@Column(name = "NCPDAPAW")
	private String ncpDAPAcountWalet = "00001-02523481001-82";

	/**
	 * Numero de compte de Orange
	 */
	@Column(name = "ncpOrange")
	private String ncpOrange = "00001-02523481001-82";
	
	
	/**
	 * Numero de compte clientelise de Orange
	 */
	@Column(name = "ncpCliOrange")
	private String ncpCliOrange = "00002-04996661003-52";
	

	/**
	 * Numero de compte des Commissions
	 */
	@Column(name = "ncpCom")
	private String ncpCom = "00001-72900090301-90";

	/**
	 * Numero de compte TVA
	 */
	@Column(name = "ncpTVA")
	private String ncpTVA = "00001-43400090035-17";
	
	/**
	 * Numero de compte TVA
	 */
	@Column(name = "ncpTVAORG")
	private String ncpTVAOrange = "00001-43400090035-17";

	/**
	 * Numero de compte de liaison
	 */
	@Column(name = "ncpLiaison")
	private String ncpLiaison = "45920090100";

	/**
	 * Activation/Desactivation du Service
	 */
	@Column(name = "ACTIF")
	private Boolean active = Boolean.TRUE;

	/**
	 * Date d'execution de la compense
	 */
	@Column(name = "DATE_TFJO")
	private String dateTfjo;

	/**
	 * Heure de Remise en service du module (apres TFJO)
	 */
	@Column(name = "HEURE_REPRISE")
	private String heureReprise = "00:00";
	
	@Column(name = "soldeMin")
	private Double soldeMin = 0d;
	
	@Column(name = "plageCompte")
	private String plageCompte = " ";
	
	/**
	 * Types de comptes autorises
	 */
    @CollectionOfElements(fetch = FetchType.EAGER)
	@JoinTable(
			name = "OGMo_PARAMACCTYPS",
			joinColumns = {@JoinColumn(name = "PRM_CODE")}
	)
	@Column(name = "ACC_TYPS")
    @Fetch(FetchMode.SUBSELECT)
	private List<String> accountTypes = new ArrayList<String>();
    
    /**
     * Liste des commissions
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
			name = "OGMo_PARAMCOMS",
			joinColumns = {@JoinColumn(name = "PRM_CODE")}
	)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Commissions> commissions = new ArrayList<Commissions>();
    
    @Column
    private Double tauxComOrange = 0d;
    
    @Column(name = "oppositions")
	private String oppositions = "06";
    
    
    
    @Column(name = "executionRobot")
	private String executionRobot = " ";
    
    @Column(name = "lancementRobot")
	private String lancementRobot = " ";
    
    
    @Column(name = "executionAlerte")
	private String executionAlerte = " ";
    
    @Column(name = "lancementAlerte")
	private String lancementAlerte = " ";
    
    
    
    /**
	 * Adresse mail du SAV
	 */
	@Column(name = "EMAIL_ENV")
	//*** private String email_sav = "controlecompte@afrilandfirstbank.com";
	private String email_sav = "serviceapresvente@afrilandfirstbank.com";
	
	/**
	 * Password du SAV
	 */
	@Column(name = "PWD_ENV")
	private String pwd_sav = Encrypter.getInstance().encryptText("sav00001"); // sav00001
	
	/**
	 * Serveur SMTP
	 */
	@Column(name = "SMTP_SRV_NAME")
	private String smtpServerName = "172.21.10.91";
	
	/**
	 * Longueur de la reference du dossier
	 */
	@Column(name = "PORT_ENV")
	private String portEnvoiMail = "25";
	
	
	@Column(name = "periodeLancementRobot")
	private int periodeLancementRobot = 5;
	
	@Column(name = "doReconci")
	private Boolean doReconciliation = Boolean.TRUE;
	
	
	@Column(name = "periodeAlerte")
	private int periodeAlerte = 10;
	
	@Column(name = "delaiRobot")
	private int delaiRobot = 10;
	
	@Column(name = "delaiReconci")
	private int delaiReconciliation = 15;
	
	@Column(name = "numeroAlerteSMS")
	private String numeroAlerteSMS = "694700692-695673175";
	
	
	@Column(name = "changeReconci")
	private Boolean changeReconci = Boolean.FALSE;
		
	
	
	/**
	 * ip adress du server Amplitude
	 */
	@Column(name = "IP_ADRESS_AMPLI")
	private String ipAdressAmpli;
	
	/**
	 * port du serveur Amplitude
	 */
	@Column(name = "PORT_SERV_AMPLI")
	private String portServerAmpli;
	
	/**
	 * login serveur Amplitude
	 */
	@Column(name = "USER_LOGIN_SERV_AMPLI")
	private String userLoginServerAmpli=Encrypter.getInstance().encryptText("user");

	/**
	 * mdp serveur Amplitude
	 */
	@Column(name = "USER_PASSWORD_AMPLI")
	private String userPasswordServerAmpli=Encrypter.getInstance().encryptText("motpasse");
	
	/**
	 * repertoire fichier Amplitude
	 */
	@Column(name = "FILE_PATH_AMPLI")
	private String filePathAmpli;
	
	/**
	 * nom fichier Amplitude
	 */
	@Column(name = "FILE_NAME_AMPLI")
	private String fileNameAmpli;
	
	/**
	 * extension fichier Amplitude
	 */
	@Column(name = "FILE_EXT_AMPLI")
	private String fileExtensionAmpli;
	
	/**
	 * chemin sauvegarde fichiers TFJOs
	 */
	@Column(name = "CHEMIN_SAVE")
	private String cheminSaveFile = "E:\\SAVE\\monetique\\ARCH_GENERAL";
	
	
	/**
     * TFJ lances?
     */
    @Column(name = "TFJ_EN_COURS")
    private Boolean tfjoEnCours = Boolean.FALSE;
    
    
    
    @Column(name = "delaiDormant")
	private String delaiDormant = "15";
    
    @Column(name = "delai_new_acc")
	private String delaiNewAccount = "10";
	
	@Column(name = "type_Opes_Dormant")
	private String typeOpesDormant = "";
    
	@Column(name = "host_Api")
	private String hostApi = "";
    
	@Column(name = "port_Api")
	private String portApi = "";
    
	@Column(name = "protocole_api")
	private String protocoleApi = "";
    
	@Column(name = "url_sms")
	private String urlSms = "";
    
	@Column(name = "url_mail")
	private String urlMail = "";
	
	@Column(name = "url_pack")
	private String urlPackage = "";
    
	/**
	 * Url du service du core banking
	 */
	@Column(name = "URL_API_CBS")
	private String urlCbsApi = "http://172.21.88.115/cbsservices/rest/cbs/transactions/process";
	
	private String tokenCbsApi = "";
	
	
	@Column(name = "heure_resil")
	private String heureVerifResiliation = "8-13";
	
	@Column(name = "seuilReconci")
	private String seuilReconciliation = "20";
	
	@Column(name = "lot_trans")
	private String lotTransactions = "3000";
	
	
	/*
	@Version
	@Column(columnDefinition = "integer DEFAULT 0", nullable = false)
	private Long version;
	*/
	
	
	@Transient
	private String maxAWAmountTrans;

	@Transient
	private String maxWAAmountTrans;
	
	@Transient
	private String maxAWAmountDaysTrans;

	@Transient
	private String maxWAAmountDaysTrans;
		
	@Transient
	private String maxAWAmountWeeksTrans;

	@Transient
	private String maxWAAmountWeeksTrans;
	
	@Transient
	private String maxAWAmountMonthsTrans;

	@Transient
	private String maxWAAmountMonthsTrans;
	
    
    
   

    /**
     * Initialisation des commissions par defaut
     */
    public void initCommissions() {
    	commissions = OgMoHelper.getDefaultCommissions();
    }
    
	public Double getMaxAWAmountDays() {
		return maxAWAmountDays;
	}

	public void setMaxAWAmountDays(Double maxAWAmountDays) {
		this.maxAWAmountDays = maxAWAmountDays;
	}

	public Double getMaxWAAmountDays() {
		return maxWAAmountDays;
	}

	public void setMaxWAAmountDays(Double maxWAAmountDays) {
		this.maxWAAmountDays = maxWAAmountDays;
	}
	
	/**
	 * @return the maxAWAmountWeeks
	 */
	public Double getMaxAWAmountWeeks() {
		return maxAWAmountWeeks;
	}

	/**
	 * @param maxAWAmountWeeks the maxAWAmountWeeks to set
	 */
	public void setMaxAWAmountWeeks(Double maxAWAmountWeeks) {
		this.maxAWAmountWeeks = maxAWAmountWeeks;
	}

	/**
	 * @return the maxWAAmountWeeks
	 */
	public Double getMaxWAAmountWeeks() {
		return maxWAAmountWeeks;
	}

	/**
	 * @param maxWAAmountWeeks the maxWAAmountWeeks to set
	 */
	public void setMaxWAAmountWeeks(Double maxWAAmountWeeks) {
		this.maxWAAmountWeeks = maxWAAmountWeeks;
	}

	/**
	 * @return the maxAWAmountMonths
	 */
	public Double getMaxAWAmountMonths() {
		return maxAWAmountMonths;
	}

	/**
	 * @param maxAWAmountMonths the maxAWAmountMonths to set
	 */
	public void setMaxAWAmountMonths(Double maxAWAmountMonths) {
		this.maxAWAmountMonths = maxAWAmountMonths;
	}

	/**
	 * @return the maxWAAmountMonths
	 */
	public Double getMaxWAAmountMonths() {
		return maxWAAmountMonths;
	}

	/**
	 * @param maxWAAmountMonths the maxWAAmountMonths to set
	 */
	public void setMaxWAAmountMonths(Double maxWAAmountMonths) {
		this.maxWAAmountMonths = maxWAAmountMonths;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the ncpTVAOrange
	 */
	public String getNcpTVAOrange() {
		return ncpTVAOrange;
	}
	
	/**
	 * @return the ncpCliOrange
	 */
	public String getNcpCliOrange() {
		return ncpCliOrange;
	}

	/**
	 * @param ncpCliOrange the ncpCliOrange to set
	 */
	public void setNcpCliOrange(String ncpCliOrange) {
		this.ncpCliOrange = ncpCliOrange;
	}

	/**
	 * @return the soldeMin
	 */
	public Double getSoldeMin() {
		return soldeMin;
	}

	/**
	 * @param soldeMin the soldeMin to set
	 */
	public void setSoldeMin(Double soldeMin) {
		this.soldeMin = soldeMin;
	}

	/**
	 * @return the plageCompte
	 */
	public String getPlageCompte() {
		return plageCompte;
	}

	/**
	 * @return the modeNuit
	 */
	public Boolean getModeNuit() {
		return modeNuit;
	}

	/**
	 * @param modeNuit the modeNuit to set
	 */
	public void setModeNuit(Boolean modeNuit) {
		this.modeNuit = modeNuit;
	}

	/**
	 * @param plageCompte the plageCompte to set
	 */
	public void setPlageCompte(String plageCompte) {
		this.plageCompte = plageCompte;
	}

	/**
	 * @param ncpTVAOrange the ncpTVAOrange to set
	 */
	public void setNcpTVAOrange(String ncpTVAOrange) {
		this.ncpTVAOrange = ncpTVAOrange;
	}

	/**
	 * @return the tva
	 */
	public Double getTva() {
		return tva;
	}

	/**
	 * @param tva the tva to set
	 */
	public void setTva(Double tva) {
		this.tva = tva;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the bankPINLength
	 */
	public Integer getBankPINLength() {
		return bankPINLength;
	}

	
	
	/**
	 * @return the idleAuto
	 */
	public Boolean getIdleAuto() {
		return idleAuto;
	}

	/**
	 * @param idleAuto the idleAuto to set
	 */
	public void setIdleAuto(Boolean idleAuto) {
		this.idleAuto = idleAuto;
	}

	/**
	 * @param bankPINLength the bankPINLength to set
	 */
	public void setBankPINLength(Integer bankPINLength) {
		this.bankPINLength = bankPINLength;
	}

	/**
	 * @return the minPIN
	 */
	public Integer getMinPIN() {
		return minPIN;
	}

	/**
	 * @param minPIN the minPIN to set
	 */
	public void setMinPIN(Integer minPIN) {
		this.minPIN = minPIN;
	}

	/**
	 * @return the maxPIN
	 */
	public Integer getMaxPIN() {
		return maxPIN;
	}

	/**
	 * @param maxPIN the maxPIN to set
	 */
	public void setMaxPIN(Integer maxPIN) {
		this.maxPIN = maxPIN;
	}

	/**
	 * @return the maxAccounts
	 */
	public Integer getMaxAccounts() {
		return maxAccounts;
	}

	/**
	 * @param maxAccounts the maxAccounts to set
	 */
	public void setMaxAccounts(Integer maxAccounts) {
		this.maxAccounts = maxAccounts;
	}

	/**
	 * @return the maxPhoneNumbers
	 */
	public Integer getMaxPhoneNumbers() {
		return maxPhoneNumbers;
	}

	/**
	 * @param maxPhoneNumbers the maxPhoneNumbers to set
	 */
	public void setMaxPhoneNumbers(Integer maxPhoneNumbers) {
		this.maxPhoneNumbers = maxPhoneNumbers;
	}

	/**
	 * @return the trnsactionTimeOut
	 */
	public Integer getTrnsactionTimeOut() {
		return trnsactionTimeOut;
	}

	/**
	 * @param trnsactionTimeOut the trnsactionTimeOut to set
	 */
	public void setTrnsactionTimeOut(Integer trnsactionTimeOut) {
		this.trnsactionTimeOut = trnsactionTimeOut;
	}

	/**
	 * @return the maxAWAmount
	 */
	public Double getMaxAWAmount() {
		return maxAWAmount;
	}

	/**
	 * @param maxAWAmount the maxAWAmount to set
	 */
	public void setMaxAWAmount(Double maxAWAmount) {
		this.maxAWAmount = maxAWAmount;
	}

	/**
	 * @return the maxWAAmount
	 */
	public Double getMaxWAAmount() {
		return maxWAAmount;
	}

	/**
	 * @param maxWAAmount the maxWAAmount to set
	 */
	public void setMaxWAAmount(Double maxWAAmount) {
		this.maxWAAmount = maxWAAmount;
	}


	
	/**
	 * @return the accountTypes
	 */
	public List<String> getAccountTypes() {
		return accountTypes;
	}

	/**
	 * @param accountTypes the accountTypes to set
	 */
	public void setAccountTypes(List<String> accountTypes) {
		this.accountTypes = accountTypes;
	}

	/**
	 * @return the commissions
	 */
	public List<Commissions> getCommissions() {
		return commissions;
	}

	/**
	 * @param commissions the commissions to set
	 */
	public void setCommissions(List<Commissions> commissions) {
		this.commissions = commissions;
	}

	/**
	 * @return the ipSDPTest
	 */
	public String getIpSDPTest() {
		return ipSDPTest;
	}

	/**
	 * @param ipSDPTest the ipSDPTest to set
	 */
	public void setIpSDPTest(String ipSDPTest) {
		this.ipSDPTest = ipSDPTest;
	}

	/**
	 * @return the addressSDPProd
	 */
	public String getAddressSDPProd() {
		return addressSDPProd;
	}

	/**
	 * @param addressSDPProd the addressSDPProd to set
	 */
	public void setAddressSDPProd(String addressSDPProd) {
		this.addressSDPProd = addressSDPProd;
	}

	/**
	 * @return the codeOpe
	 */
	public String getCodeOpe() {
		return codeOpe;
	}

	/**
	 * @param codeOpe the codeOpe to set
	 */
	public void setCodeOpe(String codeOpe) {
		this.codeOpe = codeOpe;
	}
	
	
	public String getCodeOpeCompt() {
		return codeOpeCompt;
	}

	public void setCodeOpeCompt(String codeOpeCompt) {
		this.codeOpeCompt = codeOpeCompt;
	}
	
	

	/**
	 * @return the uti
	 */
	public String getUti() {
		return uti;
	}

	/**
	 * @param uti the uti to set
	 */
	public void setUti(String uti) {
		this.uti = uti;
	}

	/**
	 * @return the ncpDAPWaletAcount
	 */
	public String getNcpDAPWaletAcount() {
		return ncpDAPWaletAcount;
	}

	/**
	 * @param ncpDAPWaletAcount the ncpDAPWaletAcount to set
	 */
	public void setNcpDAPWaletAcount(String ncpDAPWaletAcount) {
		this.ncpDAPWaletAcount = ncpDAPWaletAcount;
	}

	/**
	 * @return the ncpDAPAcountWalet
	 */
	public String getNcpDAPAcountWalet() {
		return ncpDAPAcountWalet;
	}

	/**
	 * @param ncpDAPAcountWalet the ncpDAPAcountWalet to set
	 */
	public void setNcpDAPAcountWalet(String ncpDAPAcountWalet) {
		this.ncpDAPAcountWalet = ncpDAPAcountWalet;
	}

	/**
	 * @return the ncpOrange
	 */
	public String getNcpOrange() {
		return ncpOrange;
	}

	/**
	 * @param ncpOrange the ncpOrange to set
	 */
	public void setNcpOrange(String ncpOrange) {
		this.ncpOrange = ncpOrange;
	}

	/**
	 * @return the ncpCom
	 */
	public String getNcpCom() {
		return ncpCom;
	}

	/**
	 * @param ncpCom the ncpCom to set
	 */
	public void setNcpCom(String ncpCom) {
		this.ncpCom = ncpCom;
	}

	/**
	 * @return the ncpTVA
	 */
	public String getNcpTVA() {
		return ncpTVA;
	}

	/**
	 * @param ncpTVA the ncpTVA to set
	 */
	public void setNcpTVA(String ncpTVA) {
		this.ncpTVA = ncpTVA;
	}

	/**
	 * @return the ncpLiaison
	 */
	public String getNcpLiaison() {
		return ncpLiaison;
	}

	/**
	 * @param ncpLiaison the ncpLiaison to set
	 */
	public void setNcpLiaison(String ncpLiaison) {
		this.ncpLiaison = ncpLiaison;
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
	 * @return the dateTfjo
	 */
	public String getDateTfjo() {
		return dateTfjo;
	}

	/**
	 * @param dateTfjo the dateTfjo to set
	 */
	public void setDateTfjo(String dateTfjo) {
		this.dateTfjo = dateTfjo;
	}

	/**
	 * @return the heureReprise
	 */
	public String getHeureReprise() {
		return heureReprise;
	}

	/**
	 * @param heureReprise the heureReprise to set
	 */
	public void setHeureReprise(String heureReprise) {
		this.heureReprise = heureReprise;
	}

		
	/**------------------------------------------------   */
	public String getCodeOperation(){
		return this.codeOpe;
	}
	
	public String getNumCompteCommissions(){
		return this.ncpCom;
	}
	
	public String getNumCompteTVA(){
		return this.ncpTVA;
	}
	
	public String getCodeUtil(){
		return this.uti;
	}
	
	public String getNumCompteLiaison(){
		return this.ncpLiaison;
	}

	/**
	 * @return the bankBIC
	 */
	public String getBankBIC() {
		return bankBIC;
	}

	/**
	 * @param bankBIC the bankBIC to set
	 */
	public void setBankBIC(String bankBIC) {
		this.bankBIC = bankBIC;
	}

	/**
	 * @return the registerService
	 */
	public String getRegisterService() {
		return registerService;
	}

	/**
	 * @param registerService the registerService to set
	 */
	public void setRegisterService(String registerService) {
		this.registerService = registerService;
	}

	/**
	 * @return the inquiryService
	 */
	public String getInquiryService() {
		return inquiryService;
	}

	/**
	 * @param inquiryService the inquiryService to set
	 */
	public void setInquiryService(String inquiryService) {
		this.inquiryService = inquiryService;
	}

	/**
	 * @return the idlService
	 */
	public String getIdlService() {
		return idlService;
	}

	/**
	 * @param idlService the idlService to set
	 */
	public void setIdlService(String idlService) {
		this.idlService = idlService;
	}

	/**
	 * @return the operatorCode
	 */
	public String getOperatorCode() {
		return operatorCode;
	}

	/**
	 * @param operatorCode the operatorCode to set
	 */
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	/**
	 * @return the affiliateCode
	 */
	public String getAffiliateCode() {
		return affiliateCode;
	}

	/**
	 * @param affiliateCode the affiliateCode to set
	 */
	public void setAffiliateCode(String affiliateCode) {
		this.affiliateCode = affiliateCode;
	}

	/**
	 * @return the allias
	 */
	public String getAllias() {
		return allias;
	}

	/**
	 * @param allias the allias to set
	 */
	public void setAllias(String allias) {
		this.allias = allias;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the tauxComOrange
	 */
	public Double getTauxComOrange() {
		return tauxComOrange;
	}

	/**
	 * @param tauxComOrange the tauxComOrange to set
	 */
	public void setTauxComOrange(Double tauxComOrange) {
		this.tauxComOrange = tauxComOrange;
	}

	public String getExecutionRobot() {
		return executionRobot;
	}

	public void setExecutionRobot(String executionRobot) {
		this.executionRobot = executionRobot;
	}

	public String getLancementRobot() {
		return lancementRobot;
	}

	public void setLancementRobot(String lancementRobot) {
		this.lancementRobot = lancementRobot;
	}

	/**
	 * @return the executionAlerte
	 */
	public String getExecutionAlerte() {
		return executionAlerte;
	}

	/**
	 * @param executionAlerte the executionAlerte to set
	 */
	public void setExecutionAlerte(String executionAlerte) {
		this.executionAlerte = executionAlerte;
	}

	/**
	 * @return the lancementAlerte
	 */
	public String getLancementAlerte() {
		return lancementAlerte;
	}

	/**
	 * @param lancementAlerte the lancementAlerte to set
	 */
	public void setLancementAlerte(String lancementAlerte) {
		this.lancementAlerte = lancementAlerte;
	}

	/**
	 * @return the oppositions
	 */
	public String getOppositions() {
		return oppositions;
	}

	/**
	 * @param oppositions the oppositions to set
	 */
	public void setOppositions(String oppositions) {
		this.oppositions = oppositions;
	}
	
	
	
	/**
	 * @return the email_sav
	 */
	public String getEmail_sav() {
		return email_sav;
	}

	/**
	 * @param email_sav the email_sav to set
	 */
	public void setEmail_sav(String email_sav) {
		this.email_sav = email_sav;
	}

	/**
	 * @return the pwd_sav
	 */
	public String getPwd_sav() {
		return Encrypter.getInstance().decryptText(pwd_sav);
	}

	/**
	 * @param pwd_sav the pwd_sav to set
	 */
	public void setPwd_sav(String pwd_sav) {
		this.pwd_sav = Encrypter.getInstance().encryptText( pwd_sav );
	}


	/**
	 * @return the smtpServerName
	 */
	public String getSmtpServerName() {
		return smtpServerName;
	}

	/**
	 * @param smtpServerName the smtpServerName to set
	 */
	public void setSmtpServerName(String smtpServerName) {
		this.smtpServerName = smtpServerName;
	}

	public String getPortEnvoiMail() {
		return portEnvoiMail;
	}

	public void setPortEnvoiMail(String portEnvoiMail) {
		this.portEnvoiMail = portEnvoiMail;
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Parameters [code=" + code + ", bankPINLength=" + bankPINLength + ", tva=" + tva + ", bankBIC=" + bankBIC
				+ ", registerService=" + registerService + ", inquiryService=" + inquiryService + ", idlService="
				+ idlService + ", operatorCode=" + operatorCode + ", affiliateCode=" + affiliateCode + ", allias="
				+ allias + ", currency=" + currency + ", minPIN=" + minPIN + ", maxPIN=" + maxPIN + ", maxAccounts="
				+ maxAccounts + ", maxPhoneNumbers=" + maxPhoneNumbers + ", trnsactionTimeOut=" + trnsactionTimeOut
				+ ", maxAWAmount=" + maxAWAmount + ", maxWAAmount=" + maxWAAmount + ", maxAWAmountDays="
				+ maxAWAmountDays + ", maxWAAmountDays=" + maxWAAmountDays + ", idleAuto=" + idleAuto + ", modeNuit="
				+ modeNuit + ", ipSDPTest=" + ipSDPTest + ", addressSDPProd=" + addressSDPProd + ", codeOpe=" + codeOpe
				+ ", uti=" + uti + ", ncpDAPWaletAcount=" + ncpDAPWaletAcount + ", ncpDAPAcountWalet="
				+ ncpDAPAcountWalet + ", ncpOrange=" + ncpOrange + ", ncpCom=" + ncpCom + ", ncpTVA=" + ncpTVA
				+ ", ncpTVAOrange=" + ncpTVAOrange + ", ncpLiaison=" + ncpLiaison + ", active=" + active + ", dateTfjo="
				+ dateTfjo + ", heureReprise=" + heureReprise + ", soldeMin=" + soldeMin + ", plageCompte="
				+ plageCompte + ", accountTypes=" + accountTypes + ", commissions=" + commissions + ", tauxComOrange="
				+ tauxComOrange + ", oppositions=" + oppositions + ", executionRobot=" + executionRobot
				+ ", lancementRobot=" + lancementRobot + "]";
	}

	
	
	
	/**
	 * @return the maxAWAmountTrans
	 */
	public String getMaxAWAmountTrans() {
		return maxAWAmountTrans != null ? maxAWAmountTrans : String.valueOf(maxAWAmount.intValue());
	}

	/**
	 * @param maxAWAmountTrans the maxAWAmountTrans to set
	 */
	public void setMaxAWAmountTrans(String maxAWAmountTrans) {
		this.maxAWAmountTrans = maxAWAmountTrans;
	}

	/**
	 * @return the maxWAAmountTrans
	 */
	public String getMaxWAAmountTrans() {
		return maxWAAmountTrans != null ? maxWAAmountTrans : String.valueOf(maxWAAmount.intValue());
	}

	/**
	 * @param maxWAAmountTrans the maxWAAmountTrans to set
	 */
	public void setMaxWAAmountTrans(String maxWAAmountTrans) {
		this.maxWAAmountTrans = maxWAAmountTrans;
	}

	/**
	 * @return the maxAWAmountDaysTrans
	 */
	public String getMaxAWAmountDaysTrans() {
		return maxAWAmountDaysTrans != null ? maxAWAmountDaysTrans : String.valueOf(maxAWAmountDays.intValue());
	}

	/**
	 * @param maxAWAmountDaysTrans the maxAWAmountDaysTrans to set
	 */
	public void setMaxAWAmountDaysTrans(String maxAWAmountDaysTrans) {
		this.maxAWAmountDaysTrans = maxAWAmountDaysTrans;
	}

	/**
	 * @return the maxWAAmountDaysTrans
	 */
	public String getMaxWAAmountDaysTrans() {
		return maxWAAmountDaysTrans != null ? maxWAAmountDaysTrans : String.valueOf(maxWAAmountDays.intValue());
	}

	/**
	 * @param maxWAAmountDaysTrans the maxWAAmountDaysTrans to set
	 */
	public void setMaxWAAmountDaysTrans(String maxWAAmountDaysTrans) {
		this.maxWAAmountDaysTrans = maxWAAmountDaysTrans;
	}

	/**
	 * @return the maxAWAmountWeeksTrans
	 */
	public String getMaxAWAmountWeeksTrans() {
		return maxAWAmountWeeksTrans != null ? maxAWAmountWeeksTrans : String.valueOf(maxAWAmountWeeks.intValue());
	}

	/**
	 * @param maxAWAmountWeeksTrans the maxAWAmountWeeksTrans to set
	 */
	public void setMaxAWAmountWeeksTrans(String maxAWAmountWeeksTrans) {
		this.maxAWAmountWeeksTrans = maxAWAmountWeeksTrans;
	}

	/**
	 * @return the maxWAAmountWeeksTrans
	 */
	public String getMaxWAAmountWeeksTrans() {
		return maxWAAmountWeeksTrans != null ? maxWAAmountWeeksTrans : String.valueOf(maxWAAmountWeeks.intValue());
	}

	/**
	 * @param maxWAAmountWeeksTrans the maxWAAmountWeeksTrans to set
	 */
	public void setMaxWAAmountWeeksTrans(String maxWAAmountWeeksTrans) {
		this.maxWAAmountWeeksTrans = maxWAAmountWeeksTrans;
	}

	/**
	 * @return the maxAWAmountMonthsTrans
	 */
	public String getMaxAWAmountMonthsTrans() {
		return maxAWAmountMonthsTrans != null ? maxAWAmountMonthsTrans : String.valueOf(maxAWAmountMonths.intValue());
	}

	/**
	 * @param maxAWAmountMonthsTrans the maxAWAmountMonthsTrans to set
	 */
	public void setMaxAWAmountMonthsTrans(String maxAWAmountMonthsTrans) {
		this.maxAWAmountMonthsTrans = maxAWAmountMonthsTrans;
	}

	/**
	 * @return the maxWAAmountMonthsTrans
	 */
	public String getMaxWAAmountMonthsTrans() {
		return maxWAAmountMonthsTrans != null ? maxWAAmountMonthsTrans : String.valueOf(maxWAAmountMonths.intValue());
	}

	/**
	 * @param maxWAAmountMonthsTrans the maxWAAmountMonthsTrans to set
	 */
	public void setMaxWAAmountMonthsTrans(String maxWAAmountMonthsTrans) {
		this.maxWAAmountMonthsTrans = maxWAAmountMonthsTrans;
	}
	
	



	public int getPeriodeLancementRobot() {
		return periodeLancementRobot;
	}


	public void setPeriodeLancementRobot(int periodeLancementRobot) {
		this.periodeLancementRobot = periodeLancementRobot;
	}

	/**
	 * @return the doReconciliation
	 */
	public Boolean getDoReconciliation() {
		return doReconciliation;
	}

	/**
	 * @param doReconciliation the doReconciliation to set
	 */
	public void setDoReconciliation(Boolean doReconciliation) {
		this.doReconciliation = doReconciliation;
	}

	/**
	 * @return the periodetAlerte
	 */
	public int getPeriodeAlerte() {
		return periodeAlerte;
	}

	/**
	 * @param periodetAlerte the periodetAlerte to set
	 */
	public void setPeriodeAlerte(int periodeAlerte) {
		this.periodeAlerte = periodeAlerte;
	}

	/**
	 * @return the delaiReconciliation
	 */
	public int getDelaiReconciliation() {
		return delaiReconciliation;
	}

	/**
	 * @param delaiReconciliation the delaiReconciliation to set
	 */
	public void setDelaiReconciliation(int delaiReconciliation) {
		this.delaiReconciliation = delaiReconciliation;
	}

	/**
	 * @return the delaiRobot
	 */
	public int getDelaiRobot() {
		return delaiRobot;
	}

	/**
	 * @param delaiRobot the delaiRobot to set
	 */
	public void setDelaiRobot(int delaiRobot) {
		this.delaiRobot = delaiRobot;
	}

	/**
	 * @return the numeroAlerteSMS
	 */
	public String getNumeroAlerteSMS() {
		return numeroAlerteSMS;
	}

	/**
	 * @param numeroAlerteSMS the numeroAlerteSMS to set
	 */
	public void setNumeroAlerteSMS(String numeroAlerteSMS) {
		this.numeroAlerteSMS = numeroAlerteSMS;
	}

	
	public Boolean getChangeReconci() {
		return changeReconci;
	}

	public void setChangeReconci(Boolean changeReconci) {
		this.changeReconci = changeReconci;
	}
	
	public String getIpAdressAmpli() {
		return ipAdressAmpli;
	}

	public void setIpAdressAmpli(String ipAdressAmpli) {
		this.ipAdressAmpli = ipAdressAmpli;
	}

	public String getPortServerAmpli() {
		return portServerAmpli;
	}

	public void setPortServerAmpli(String portServerAmpli) {
		this.portServerAmpli = portServerAmpli;
	}

	public String getUserLoginServerAmpli() {
		return Encrypter.getInstance().decryptText(userLoginServerAmpli);
	}

	public void setUserLoginServerAmpli(String userLoginServerAmpli) {
		this.userLoginServerAmpli = Encrypter.getInstance().encryptText(userLoginServerAmpli);
	}

	public String getUserPasswordServerAmpli() {
		return Encrypter.getInstance().decryptText(userPasswordServerAmpli);
	}

	public void setUserPasswordServerAmpli(String userPasswordServerAmpli) {
		this.userPasswordServerAmpli = Encrypter.getInstance().encryptText(userPasswordServerAmpli);
	}

	public String getFilePathAmpli() {
		return filePathAmpli;
	}

	public void setFilePathAmpli(String filePathAmpli) {
		this.filePathAmpli = filePathAmpli;
	}

	public String getFileNameAmpli() {
		return fileNameAmpli;
	}

	public void setFileNameAmpli(String fileNameAmpli) {
		this.fileNameAmpli = fileNameAmpli;
	}

	public String getFileExtensionAmpli() {
		return fileExtensionAmpli;
	}

	public void setFileExtensionAmpli(String fileExtensionAmpli) {
		this.fileExtensionAmpli = fileExtensionAmpli;
	}
		
	
	public String getCheminSaveFile() {
		return StringUtils.isNotBlank(cheminSaveFile) ? cheminSaveFile : "E:\\ACQUISITION\\ARCHIVES";
	}

	public void setCheminSaveFile(String cheminSaveFile) {
		this.cheminSaveFile = cheminSaveFile;
	}
	
	
	
	
	public Boolean getTfjoEnCours() {
		return tfjoEnCours = (null==tfjoEnCours ? false : tfjoEnCours);
	}

	public void setTfjoEnCours(Boolean tfjoEnCours) {
		this.tfjoEnCours = tfjoEnCours;
	}

	/**
	 * @return the delaiDormant
	 */
	public String getDelaiDormant() {
		return delaiDormant;
	}

	/**
	 * @param delaiDormant the delaiDormant to set
	 */
	public void setDelaiDormant(String delaiDormant) {
		this.delaiDormant = delaiDormant;
	}

	/**
	 * @return the delaiNewAccount
	 */
	public String getDelaiNewAccount() {
		return delaiNewAccount;
	}

	/**
	 * @param delaiNewAccount the delaiNewAccount to set
	 */
	public void setDelaiNewAccount(String delaiNewAccount) {
		this.delaiNewAccount = delaiNewAccount;
	}

	/**
	 * @return the typeOpesDormant
	 */
	public String getTypeOpesDormant() {
		return typeOpesDormant;
	}

	/**
	 * @param typeOpesDormant the typeOpesDormant to set
	 */
	public void setTypeOpesDormant(String typeOpesDormant) {
		this.typeOpesDormant = typeOpesDormant;
	}

	/**
	 * @return the hostApi
	 */
	public String getHostApi() {
		return hostApi;
	}

	/**
	 * @param hostApi the hostApi to set
	 */
	public void setHostApi(String hostApi) {
		this.hostApi = hostApi;
	}

	/**
	 * @return the portApi
	 */
	public String getPortApi() {
		return portApi;
	}

	/**
	 * @param portApi the portApi to set
	 */
	public void setPortApi(String portApi) {
		this.portApi = portApi;
	}

	/**
	 * @return the protocoleApi
	 */
	public String getProtocoleApi() {
		return protocoleApi;
	}

	/**
	 * @param protocoleApi the protocoleApi to set
	 */
	public void setProtocoleApi(String protocoleApi) {
		this.protocoleApi = protocoleApi;
	}

	/**
	 * @return the urlSms
	 */
	public String getUrlSms() {
		return urlSms;
	}

	/**
	 * @param urlSms the urlSms to set
	 */
	public void setUrlSms(String urlSms) {
		this.urlSms = urlSms;
	}

	/**
	 * @return the urlMail
	 */
	public String getUrlMail() {
		return urlMail;
	}

	/**
	 * @param urlMail the urlMail to set
	 */
	public void setUrlMail(String urlMail) {
		this.urlMail = urlMail;
	}

	/**
	 * @return the urlPackage
	 */
	public String getUrlPackage() {
		return urlPackage;
	}

	/**
	 * @param urlPackage the urlPackage to set
	 */
	public void setUrlPackage(String urlPackage) {
		this.urlPackage = urlPackage;
	}

	/**
	 * @return the urlCbsApi
	 */
	public String getUrlCbsApi() {
		return urlCbsApi;
	}

	/**
	 * @param urlCbsApi the urlCbsApi to set
	 */
	public void setUrlCbsApi(String urlCbsApi) {
		this.urlCbsApi = urlCbsApi;
	}

	/**
	 * @return the tokenCbsApi
	 */
	public String getTokenCbsApi() {
		return tokenCbsApi;
	}

	/**
	 * @param tokenCbsApi the tokenCbsApi to set
	 */
	public void setTokenCbsApi(String tokenCbsApi) {
		this.tokenCbsApi = tokenCbsApi;
	}

	/**
	 * @return the heureVerifResiliation
	 */
	public String getHeureVerifResiliation() {
		return heureVerifResiliation;
	}

	/**
	 * @param heureVerifResiliation the heureVerifResiliation to set
	 */
	public void setHeureVerifResiliation(String heureVerifResiliation) {
		this.heureVerifResiliation = heureVerifResiliation;
	}

	/**
	 * @return the seuilReconciliation
	 */
	public String getSeuilReconciliation() {
		return seuilReconciliation;
	}

	/**
	 * @param seuilReconciliation the seuilReconciliation to set
	 */
	public void setSeuilReconciliation(String seuilReconciliation) {
		this.seuilReconciliation = seuilReconciliation;
	}

	/**
	 * @return the lotTransactions
	 */
	public String getLotTransactions() {
		return lotTransactions;
	}

	/**
	 * @param lotTransactions the lotTransactions to set
	 */
	public void setLotTransactions(String lotTransactions) {
		this.lotTransactions = lotTransactions;
	}

	/*
	public Long getVersion() {
		return version;
	}
	*/
		

}
