/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.afb.dpd.orangemoney.jpa.enums.Periodicite;
import com.afb.dpd.orangemoney.jpa.enums.StatutContrat;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jpa.tools.OgMoHelper;

import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

/**
 * Classe representant une souscription
 * @author Francis KONCHOU
 * @version 1.0
 */
@Entity
@Table(name = "OgMo_Abon")
@NamedQueries({
    @NamedQuery(name="Subscriber.findAll", query="SELECT c FROM Subscriber c where profil is not null"),
    @NamedQuery(name=Subscriber.UPDATE_SUBSCRIBER,query="update Subscriber s set s.dateSaveDernCompta=:dateSaveDernCompta, s.dateDernCompta=:dateDernCompta  where s.id=:id"),
})
public class Subscriber implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String UPDATE_SUBSCRIBER = "Subscriber.mergepostingTransaction";


	/**
	 * Default Constructor
	 */
	public Subscriber() {}

	/**
	 * Id auto genere
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	
	/**
	 * Code du client
	 */
	@Column(name = "client", nullable = false)
	private String customerId;
	
	@Column
	private String submsisdn;
	
	@Column
	private String subkey;
	
	@Column
	private String substatus;
	
	@Column
	private String sublastname;
	
	@Column
	private String subfirstname;
	
	@Column
	private String subdob;
	
	@Column
	private String subcin;
	
	/**
	 * Code PIN du client
	 */
	@Column(name = "PIN", nullable = false, unique=true)
	private String bankPIN;
	
	/**
	 * Code PIN du client
	 */
	@Column(name = "ogMoPIN", nullable = false)
	private String ogMoPIN;

	/**
	 * numeros de telephones du client
	 */
    @CollectionOfElements(fetch = FetchType.EAGER)
	@JoinTable(
			name = "OGMo_SUBSPHONES",
			joinColumns = {@JoinColumn(name = "SUBSID")}
	)
	@Column(name = "PHONES", nullable = false)
    @Fetch(FetchMode.SUBSELECT)
	private List<String> phoneNumbers = new ArrayList<String>();

	/**
	 * Numeros de comptes
	 */
    @CollectionOfElements(fetch = FetchType.EAGER)
	@JoinTable(
			name = "OGMo_SUBSACC",
			joinColumns = {@JoinColumn(name = "SUBSID")}
	)
	@Column(name = "ACCOUNTS")
    @Fetch(FetchMode.SUBSELECT)
	private List<String> accounts = new ArrayList<String>();
    
    /**
     * Nom du client
     */
    @Column(name = "NAME")
    private String customerName;
    
    /**
     * Adresse
     */
    @Column(name = "ADDRESS")
    private String customerAddress;
    
    /**
     * Date de Souscription
     */
    @Column(name = "SUBS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();
    
    /**
     * Etat du contrat
     */
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private StatutContrat status = StatutContrat.WAITING;
    
    /**
     * Periodicite de comptabilisation des commissions
     */
    @Column(name = "PERIOD_COMPTA")
    @Enumerated(EnumType.STRING)
    private Periodicite period = Periodicite.MOIS;
    
    /**
     * Valeur des commissions prelevees
     */
    @Column(name = "COMMISSIONS")
    private Double commissions = 0d;
    
    
    @Column(name = "FACTURER")
    private Boolean facturer = Boolean.TRUE;
    
    /**
     * N° Piece d'identite du client
     */
    @Column(name = "PID")
    private String pid;
    
    /**
     * Compte de Facturation
     */
    @Column(name = "accFact")
    private String accFact;    
    
    @Column(name = "UTIABON")
    private String utiabon;
    
    @Column(name = "NameutiAbon")
    private String nameUtiAbon;
    
    @Column(name = "AgeAbon")
    private String ageAbon;
    
    @Column(name = "libAgeAbon")
    private String libAgeAbone;
    
    @Column(name = "utiMod")
    private String utiMod;
    
    @Column(name = "NameutiMod")
    private String nameUtiMod;
    
    @Column(name = "dateMod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateMod;
    
    @Column(name = "ageMod")
    private String ageMod;
    
    @Column(name = "libAgeMod")
    private String libAgeMod;
    
    @Transient
    private String img = "";
    
    @Transient
    private String title = "";
    
    @Column(name = "utiValid")
	private String utiValid;
	
	@Column(name = "SUBS_DATEVALID")
	@Temporal(TemporalType.DATE)
	private Date dateValid;
	
	
	@Column(name = "SUS_TEMP", length = 1024)
	private String suspensTemp;
	
	@Column(name = "HISTO_ABON", length = 1024)
	private String histoSubscriber;
	
	
	
	/**
	 * ProfilMarchands
	 */
	@ManyToOne
	@JoinColumn(name = "PROFIl_ID")
	private ProfilMarchands profil;
	
	
	@Temporal(TemporalType.DATE)
	@Column
	private Date dateCrtlLimitWA;
	
	@Column
	private Double amountLimitWA;
	
	
	@Temporal(TemporalType.DATE)
	@Column
	private Date dateCrtlLimitAW;
	
	@Column
	private Double amountLimitAW;
	
	
	
	@Temporal(TemporalType.DATE)
	@Column
	private Date dateCrtlLimitWAWeek;
	
	@Column
	private Double amountLimitWAWeek;
	
	
	@Temporal(TemporalType.DATE)
	@Column
	private Date dateCrtlLimitAWWeek;
	
	@Column
	private Double amountLimitAWWeek;
	
	
	
	@Temporal(TemporalType.DATE)
	@Column
	private Date dateCrtlLimitWAMonth;
	
	@Column
	private Double amountLimitWAMonth;
	
	
	@Temporal(TemporalType.DATE)
	@Column
	private Date dateCrtlLimitAWMonth;
	
	@Column
	private Double amountLimitAWMonth;
	
	
	
	/**
	 * Date de la derniere comptabilisation/facturation
	 */
	@Column(name = "LAST_DATE_COMPTA")
	@Temporal(TemporalType.DATE)
	private Date dateDernCompta;
		
	@Column(name = "LASTSAVE_DATE_COMPTA")
	@Temporal(TemporalType.DATE)
	private Date dateSaveDernCompta;
	
	@Column(name = "utiSuspendu")
	private String utiSuspendu;
	
	@Column(name = "dateSuspendu")
	@Temporal(TemporalType.DATE)
	private Date dateSuspendu;
		
	
	@Column(name = "date_resi_cbs")
	private String dateResiliationCBS;
		
	
	
	@Transient
	private String profilId;
	
	@Transient
	private String nomClientOrange;
	
	@Transient
	private String telOrange;
	
	
	@Transient
	private boolean consulSig = false;
	
	
	@Transient
	private String generatedOtp;
	
    
    
	/**
	 * @param customerId
	 * @param customerName
	 * @param customerAddress  
	 */
	public Subscriber(String customerId, String customerName, String customerAddress) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerAddress = customerAddress;
	}
	
	
	public String getDeCryptedBankPIN() {
		return this.bankPIN ; //Encrypter.getInstance().decryptText(this.bankPIN);
	}
	
	public String getFormattedDate(){
		return PortalHelper.DEFAULT_DATE_FORMAT.format(date);
	}

	public String getFormattedDateMod() {
		return dateMod == null ? null : new SimpleDateFormat("dd/MM/yyy", Locale.UK).format(dateMod); 
	}
	
	public String getFormattedDateValid() {
		return dateValid == null ? null : new SimpleDateFormat("dd/MM/yyyy", Locale.UK).format(dateValid); 
	}
	
	public String getFormattedDateSuspendu() {
		return dateSuspendu == null ? null : new SimpleDateFormat("dd/MM/yyyy", Locale.UK).format(dateSuspendu); 
	}
	
	
	//************************** CONTROLE PLAFOND JOURNALIER ************************
	
	private void initLimit(TypeOperation type){
		if(TypeOperation.Account2Wallet.equals(type)){
			this.dateCrtlLimitAW = new Date();
			this.amountLimitAW = 0d; 	
		}else if(TypeOperation.Wallet2Account.equals(type)){
			this.dateCrtlLimitWA = new Date();
			this.amountLimitWA = 0d; 	
		}
	}
	
		
	public Boolean islimit(TypeOperation type,Double amtTrans,Double amtLimit){
		if(amtLimit == null) return Boolean.TRUE;
		if(Boolean.TRUE.equals(crtlLimit(type,amtTrans,amtLimit))){
			if(TypeOperation.Account2Wallet.equals(type)) this.amountLimitAW = this.amountLimitAW + amtTrans ;
			else if(TypeOperation.Wallet2Account.equals(type)) this.amountLimitWA = this.amountLimitWA + amtTrans;
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
		
	private Boolean crtlLimit(TypeOperation type,Double amtTrans , Double amtLimit){
		if(Boolean.FALSE.equals(sameDays(TypeOperation.Account2Wallet.equals(type) ? this.dateCrtlLimitAW : this.dateCrtlLimitWA))){
			initLimit(type);
		}
		return TypeOperation.Account2Wallet.equals(type) ? amtLimit.compareTo(this.amountLimitAW+amtTrans) >= 0 : amtLimit.compareTo(this.amountLimitWA+amtTrans) >= 0;
	}
	
	
	private Boolean sameDays(Date dateCrtlLimit){
		if(dateCrtlLimit == null) return Boolean.FALSE;
		String pattern = "dd/MM/yyyy";
		String dlimit = DateFormatUtils.format(dateCrtlLimit, pattern);
		String dtoday = DateFormatUtils.format(new Date(), pattern);
		return StringUtils.equalsIgnoreCase(dlimit, dtoday);
	}
	
	
	public Date nextDate(Date date, int nbreJour){
		return DateUtils.addDays(date, nbreJour);
	}
		
	
	
	//************************** CONTROLE PLAFOND HEBDOMADAIRE ************************
		
	private void initLimitWeekHebd(TypeOperation type, String periode){
		if("W".equals(periode)){
			if(TypeOperation.Account2Wallet.equals(type)){
				this.dateCrtlLimitAWWeek = nextDate(new Date(), 7);
				this.amountLimitAWWeek = 0d; 	
			}else if(TypeOperation.Wallet2Account.equals(type)){
				this.dateCrtlLimitWAWeek = nextDate(new Date(), 7);
				this.amountLimitWAWeek = 0d; 	
			}
		}
		else if("M".equals(periode)){
			if(TypeOperation.Account2Wallet.equals(type)){
				this.dateCrtlLimitAWMonth = nextDate(new Date(), 30);
				this.amountLimitAWMonth = 0d; 	
			}else if(TypeOperation.Wallet2Account.equals(type)){
				this.dateCrtlLimitWAMonth = nextDate(new Date(), 30);
				this.amountLimitWAMonth = 0d; 	
			}
		}		
	}
	
	
	public Boolean islimitWeekHebd(TypeOperation type,Double amtTrans,Double amtLimit, String periode){
		if(amtLimit == null) return Boolean.TRUE;
		if(Boolean.TRUE.equals(crtlLimitWeekHebd(type,amtTrans,amtLimit,periode))){
			
			if("W".equals(periode)){
				if(TypeOperation.Account2Wallet.equals(type)) this.amountLimitAWWeek = this.amountLimitAWWeek + amtTrans ;
				else if(TypeOperation.Wallet2Account.equals(type)) this.amountLimitWAWeek = this.amountLimitWAWeek + amtTrans;
				return Boolean.TRUE;
			}
			else if("M".equals(periode)){
				if(TypeOperation.Account2Wallet.equals(type)) this.amountLimitAWMonth = this.amountLimitAWMonth + amtTrans ;
				else if(TypeOperation.Wallet2Account.equals(type)) this.amountLimitWAMonth = this.amountLimitWAMonth + amtTrans;
				return Boolean.TRUE;
			}
			
		}
		else{
			if("W".equals(periode)){
				if(TypeOperation.Account2Wallet.equals(type)) this.amountLimitAW = this.amountLimitAW - amtTrans ;
				else if(TypeOperation.Wallet2Account.equals(type)) this.amountLimitWA = this.amountLimitWA - amtTrans;
				return Boolean.FALSE;
			}
			else if("M".equals(periode)){
				if(TypeOperation.Account2Wallet.equals(type)) {
					this.amountLimitAW = this.amountLimitAW - amtTrans ;
					this.amountLimitAWWeek = this.amountLimitAWWeek - amtTrans ;
				}
				else if(TypeOperation.Wallet2Account.equals(type)){
					this.amountLimitWA = this.amountLimitWA - amtTrans;
					this.amountLimitWAWeek = this.amountLimitWAWeek - amtTrans;
				}
				return Boolean.FALSE;
			}
		}
		return Boolean.FALSE;
	}
	
	
	private Boolean crtlLimitWeekHebd(TypeOperation type,Double amtTrans , Double amtLimit, String periode){
		
		boolean result = Boolean.FALSE;
		
		if("W".equals(periode)){
			if(Boolean.TRUE.equals(sameDaysWeekHebd(TypeOperation.Account2Wallet.equals(type) ? this.dateCrtlLimitAWWeek : this.dateCrtlLimitWAWeek))){
				initLimitWeekHebd(type,periode);
			}
			return TypeOperation.Account2Wallet.equals(type) ? amtLimit.compareTo(this.amountLimitAWWeek+amtTrans) >= 0 : amtLimit.compareTo(this.amountLimitWAWeek+amtTrans) >= 0;
		}
		else if("M".equals(periode)){
			if(Boolean.TRUE.equals(sameDaysWeekHebd(TypeOperation.Account2Wallet.equals(type) ? this.dateCrtlLimitAWMonth : this.dateCrtlLimitWAMonth))){
				initLimitWeekHebd(type,periode);
			}
			return TypeOperation.Account2Wallet.equals(type) ? amtLimit.compareTo(this.amountLimitAWMonth+amtTrans) >= 0 : amtLimit.compareTo(this.amountLimitWAMonth+amtTrans) >= 0;
		}
		
		return result;
	}
	
	
	private Boolean sameDaysWeekHebd(Date dateCrtlLimit){
		if(dateCrtlLimit == null) return Boolean.TRUE;
		String pattern = "dd/MM/yyyy";
		String dlimit = DateFormatUtils.format(dateCrtlLimit, pattern);
		String dtoday = DateFormatUtils.format(new Date(), pattern);
		return StringUtils.equalsIgnoreCase(dlimit, dtoday) || OgMoHelper.getNbreJoursBetween(dateCrtlLimit, new Date()) >= 0;
	}
	
	//************************** CONTROLE PLAFOND MENSUEL ************************
	
	

	/**
	 * @return the dateCrtlLimitWA
	 */
	public Date getDateCrtlLimitWA() {
		return dateCrtlLimitWA;
	}


	/**
	 * @param dateCrtlLimitWA the dateCrtlLimitWA to set
	 */
	public void setDateCrtlLimitWA(Date dateCrtlLimitWA) {
		this.dateCrtlLimitWA = dateCrtlLimitWA;
	}


	/**
	 * @return the amountLimitWA
	 */
	public Double getAmountLimitWA() {
		return amountLimitWA;
	}


	/**
	 * @param amountLimitWA the amountLimitWA to set
	 */
	public void setAmountLimitWA(Double amountLimitWA) {
		this.amountLimitWA = amountLimitWA;
	}


	/**
	 * @return the dateCrtlLimitAW
	 */
	public Date getDateCrtlLimitAW() {
		return dateCrtlLimitAW;
	}


	/**
	 * @param dateCrtlLimitAW the dateCrtlLimitAW to set
	 */
	public void setDateCrtlLimitAW(Date dateCrtlLimitAW) {
		this.dateCrtlLimitAW = dateCrtlLimitAW;
	}


	/**
	 * @return the amountLimitAW
	 */
	public Double getAmountLimitAW() {
		return amountLimitAW;
	}


	/**
	 * @param amountLimitAW the amountLimitAW to set
	 */
	public void setAmountLimitAW(Double amountLimitAW) {
		this.amountLimitAW = amountLimitAW;
	}
	

	/**
	 * @return the dateCrtlLimitWAWeek
	 */
	public Date getDateCrtlLimitWAWeek() {
		return dateCrtlLimitWAWeek;
	}


	/**
	 * @param dateCrtlLimitWAWeek the dateCrtlLimitWAWeek to set
	 */
	public void setDateCrtlLimitWAWeek(Date dateCrtlLimitWAWeek) {
		this.dateCrtlLimitWAWeek = dateCrtlLimitWAWeek;
	}


	/**
	 * @return the amountLimitWAWeek
	 */
	public Double getAmountLimitWAWeek() {
		return amountLimitWAWeek;
	}


	/**
	 * @param amountLimitWAWeek the amountLimitWAWeek to set
	 */
	public void setAmountLimitWAWeek(Double amountLimitWAWeek) {
		this.amountLimitWAWeek = amountLimitWAWeek;
	}


	/**
	 * @return the dateCrtlLimitAWWeek
	 */
	public Date getDateCrtlLimitAWWeek() {
		return dateCrtlLimitAWWeek;
	}


	/**
	 * @param dateCrtlLimitAWWeek the dateCrtlLimitAWWeek to set
	 */
	public void setDateCrtlLimitAWWeek(Date dateCrtlLimitAWWeek) {
		this.dateCrtlLimitAWWeek = dateCrtlLimitAWWeek;
	}


	/**
	 * @return the amountLimitAWWeek
	 */
	public Double getAmountLimitAWWeek() {
		return amountLimitAWWeek;
	}


	/**
	 * @param amountLimitAWWeek the amountLimitAWWeek to set
	 */
	public void setAmountLimitAWWeek(Double amountLimitAWWeek) {
		this.amountLimitAWWeek = amountLimitAWWeek;
	}


	/**
	 * @return the dateCrtlLimitWAMonth
	 */
	public Date getDateCrtlLimitWAMonth() {
		return dateCrtlLimitWAMonth;
	}


	/**
	 * @param dateCrtlLimitWAMonth the dateCrtlLimitWAMonth to set
	 */
	public void setDateCrtlLimitWAMonth(Date dateCrtlLimitWAMonth) {
		this.dateCrtlLimitWAMonth = dateCrtlLimitWAMonth;
	}


	/**
	 * @return the amountLimitWAMonth
	 */
	public Double getAmountLimitWAMonth() {
		return amountLimitWAMonth;
	}


	/**
	 * @param amountLimitWAMonth the amountLimitWAMonth to set
	 */
	public void setAmountLimitWAMonth(Double amountLimitWAMonth) {
		this.amountLimitWAMonth = amountLimitWAMonth;
	}


	/**
	 * @return the dateCrtlLimitAWMonth
	 */
	public Date getDateCrtlLimitAWMonth() {
		return dateCrtlLimitAWMonth;
	}


	/**
	 * @param dateCrtlLimitAWMonth the dateCrtlLimitAWMonth to set
	 */
	public void setDateCrtlLimitAWMonth(Date dateCrtlLimitAWMonth) {
		this.dateCrtlLimitAWMonth = dateCrtlLimitAWMonth;
	}


	/**
	 * @return the amountLimitAWMonth
	 */
	public Double getAmountLimitAWMonth() {
		return amountLimitAWMonth;
	}


	/**
	 * @param amountLimitAWMonth the amountLimitAWMonth to set
	 */
	public void setAmountLimitAWMonth(Double amountLimitAWMonth) {
		this.amountLimitAWMonth = amountLimitAWMonth;
	}


	
	public String getFormattedDateCrtlLimitAW() {
		return dateCrtlLimitAW == null ? null : new SimpleDateFormat("dd/MM/yyy", Locale.UK).format(dateCrtlLimitAW);
	}
	
	public String getFormattedDateCrtlLimitWA() {
		return dateCrtlLimitWA == null ? null : new SimpleDateFormat("dd/MM/yyy", Locale.UK).format(dateCrtlLimitWA);
	}
	
	
	public String getFormattedDateCrtlLimitAWWeek() {
		return dateCrtlLimitAWWeek == null ? null : new SimpleDateFormat("dd/MM/yyy", Locale.UK).format(dateCrtlLimitAWWeek);
	}
	
	public String getFormattedDateCrtlLimitWAWeek() {
		return dateCrtlLimitWAWeek == null ? null : new SimpleDateFormat("dd/MM/yyy", Locale.UK).format(dateCrtlLimitWAWeek);
	}
	
	
	public String getFormattedDateCrtlLimitAWMonth() {
		return dateCrtlLimitAWWeek == null ? null : new SimpleDateFormat("dd/MM/yyy", Locale.UK).format(dateCrtlLimitAWMonth);
	}
	
	public String getFormattedDateCrtlLimitWAMonth() {
		return dateCrtlLimitWAMonth == null ? null : new SimpleDateFormat("dd/MM/yyy", Locale.UK).format(dateCrtlLimitWAMonth);
	}
	
	
	/**
	 * @return the ogMoPIN
	 */
	public String getOgMoPIN() {
		return ogMoPIN;
	}


	/**
	 * @param ogMoPIN the ogMoPIN to set
	 */
	public void setOgMoPIN(String ogMoPIN) {
		this.ogMoPIN = ogMoPIN;
	}


	/**
	 * @return the utiValid
	 */
	public String getUtiValid() {
		return utiValid;
	}


	/**
	 * @param utiValid the utiValid to set
	 */
	public void setUtiValid(String utiValid) {
		this.utiValid = utiValid;
	}


	/**
	 * @return the dateValid
	 */
	public Date getDateValid() {
		return dateValid;
	}


	/**
	 * @param dateValid the dateValid to set
	 */
	public void setDateValid(Date dateValid) {
		this.dateValid = dateValid;
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

	
	
	public String getImg() {
		
		img ="del16x.png"; 
		if(this.status.equals(StatutContrat.SUSPENDU)){
			img ="ok16x.png";
		}
		return img;
	}
	
	
	public String getImgWait() {
		
		img ="stop16x.png"; 
		if(this.status.equals(StatutContrat.WAITING)){
			img ="ok16x.png";
		}
		return img;
	}

	
	

	public String getTitle() {
		title ="Annuler le contrat de souscription"; 
		if(this.status.equals(StatutContrat.SUSPENDU)){
			title ="Activer le contrat de souscription";
		}
		return title;
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
	 * @return the subkey
	 */
	public String getSubkey() {
		return subkey;
	}


	/**
	 * @param subkey the subkey to set
	 */
	public void setSubkey(String subkey) {
		this.subkey = subkey;
	}


	/**
	 * @return the substatus
	 */
	public String getSubstatus() {
		return substatus;
	}


	/**
	 * @param substatus the substatus to set
	 */
	public void setSubstatus(String substatus) {
		this.substatus = substatus;
	}


	/**
	 * @return the sublastname
	 */
	public String getSublastname() {
		return sublastname;
	}


	/**
	 * @param sublastname the sublastname to set
	 */
	public void setSublastname(String sublastname) {
		this.sublastname = sublastname;
	}


	/**
	 * @return the subfirstname
	 */
	public String getSubfirstname() {
		return subfirstname;
	}


	/**
	 * @param subfirstname the subfirstname to set
	 */
	public void setSubfirstname(String subfirstname) {
		this.subfirstname = subfirstname;
	}


	/**
	 * @return the subdob
	 */
	public String getSubdob() {
		return subdob;
	}


	/**
	 * @param subdob the subdob to set
	 */
	public void setSubdob(String subdob) {
		this.subdob = subdob;
	}


	/**
	 * @return the subcin
	 */
	public String getSubcin() {
		return subcin;
	}


	/**
	 * @param subcin the subcin to set
	 */
	public void setSubcin(String subcin) {
		this.subcin = subcin;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public void setImg(String img) {
		this.img = img;
	}


	public String getAccFact() {
		return accFact;
	}

	public void setAccFact(String accFact) {
		this.accFact = accFact;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the bankPIN
	 */
	public String getBankPIN() {
		//if(this.bankPIN != null && !this.bankPIN.trim().isEmpty())return Encrypter.getInstance().decryptText(this.bankPIN);
		//else return this.bankPIN;
		return this.bankPIN;
	}
	
	public String getFirstPhone(){
		return this.phoneNumbers != null && !this.phoneNumbers.isEmpty() ? this.phoneNumbers.get(0).trim() : null;
	}
	public String getFirstAccount(){
		return this.accounts!= null && !this.accounts.isEmpty() ? this.accounts.get(0).trim() : null;
	}
	
	public String getFirstAccounts(){
		String acc = "";
		for(String val : accounts){
			if(acc.trim().isEmpty())acc = val;
			else acc = acc +"-"+val;
		}
		return acc;
	}

	/**
	 * @param bankPIN the bankPIN to set
	 */
	public void setBankPIN(String bankPIN) {
		//if(bankPIN != null && !bankPIN.trim().isEmpty()) this.bankPIN = Encrypter.getInstance().encryptText(bankPIN);
		this.bankPIN = bankPIN;
	}
	
	/**
	 * @return the phoneNumbers
	 */
	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	/**
	 * @param phoneNumbers the phoneNumbers to set
	 */
	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	/**
	 * @return the accounts
	 */
	public List<String> getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts the accounts to set
	 */
	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
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
	 * @return the customerAddress
	 */
	public String getCustomerAddress() {
		return customerAddress;
	}

	/**
	 * @param customerAddress the customerAddress to set
	 */
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the status
	 */
	public StatutContrat getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatutContrat status) {
		this.status = status;
	}

	/**
	 * @return the period
	 */
	public Periodicite getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(Periodicite period) {
		this.period = period;
	}

	/**
	 * @return the commissions
	 */
	public Double getCommissions() {
		return commissions;
	}

	/**
	 * @param commissions the commissions to set
	 */
	public void setCommissions(Double commissions) {
		this.commissions = commissions;
	}

	/**
	 * @return the facturer
	 */
	public Boolean getFacturer() {
		return facturer;
	}

	/**
	 * @param facturer the facturer to set
	 */
	public void setFacturer(Boolean facturer) {
		this.facturer = facturer;
	}

	/**
	 * @return the pid
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * @param pid the pid to set
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}

	/**
	 * @return the utiabon
	 */
	public String getUtiabon() {
		return utiabon;
	}

	/**
	 * @param utiabon the utiabon to set
	 */
	public void setUtiabon(String utiabon) {
		this.utiabon = utiabon;
	}

	/**
	 * @return the nameUtiAbon
	 */
	public String getNameUtiAbon() {
		return nameUtiAbon;
	}

	/**
	 * @param nameUtiAbon the nameUtiAbon to set
	 */
	public void setNameUtiAbon(String nameUtiAbon) {
		this.nameUtiAbon = nameUtiAbon;
	}

	/**
	 * @return the ageAbon
	 */
	public String getAgeAbon() {
		return ageAbon;
	}

	/**
	 * @param ageAbon the ageAbon to set
	 */
	public void setAgeAbon(String ageAbon) {
		this.ageAbon = ageAbon;
	}

	/**
	 * @return the libAgeAbone
	 */
	public String getLibAgeAbone() {
		return libAgeAbone;
	}

	/**
	 * @param libAgeAbone the libAgeAbone to set
	 */
	public void setLibAgeAbone(String libAgeAbone) {
		this.libAgeAbone = libAgeAbone;
	}

	/**
	 * @return the utiMod
	 */
	public String getUtiMod() {
		return utiMod;
	}

	/**
	 * @param utiMod the utiMod to set
	 */
	public void setUtiMod(String utiMod) {
		this.utiMod = utiMod;
	}

	/**
	 * @return the nameUtiMod
	 */
	public String getNameUtiMod() {
		return nameUtiMod;
	}

	/**
	 * @param nameUtiMod the nameUtiMod to set
	 */
	public void setNameUtiMod(String nameUtiMod) {
		this.nameUtiMod = nameUtiMod;
	}

	/**
	 * @return the dateMod
	 */
	public Date getDateMod() {
		return dateMod;
	}

	/**
	 * @param dateMod the dateMod to set
	 */
	public void setDateMod(Date dateMod) {
		this.dateMod = dateMod;
	}

	/**
	 * @return the ageMod
	 */
	public String getAgeMod() {
		return ageMod;
	}

	/**
	 * @param ageMod the ageMod to set
	 */
	public void setAgeMod(String ageMod) {
		this.ageMod = ageMod;
	}

	/**
	 * @return the libAgeMod
	 */
	public String getLibAgeMod() {
		return libAgeMod;
	}

	/**
	 * @param libAgeMod the libAgeMod to set
	 */
	public void setLibAgeMod(String libAgeMod) {
		this.libAgeMod = libAgeMod;
	}
	
	
	
	
	
	/**
	 * @return the profil
	 */
	public ProfilMarchands getProfil() {
		return profil;
	}

	/**
	 * @param profil the profil to set
	 */
	public void setProfil(ProfilMarchands profil) {
		this.profil = profil;
	}
	
	
	/**
	 * @return the profilId
	 */
	public String getProfilId() {
		/**if(getProfil() != null){
			this.profilId = getProfil().getId().toString();
		}*/
		return this.profilId;
	}
	
	public void initProfilId() {
		if(getProfil() != null){
			this.profilId = getProfil().getId().toString();
		}
	}

	/**
	 * @param profilId the profilId to set
	 */
	public void setProfilId(String profilId) {
		this.profilId = profilId;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Boolean isMerchant(){
		if(getProfil() == null ) return Boolean.FALSE;
		else{
			if(Boolean.FALSE.equals(getProfil().getActive())) return Boolean.FALSE;
			else{
				if(!getProfil().getId().equals(ProfilMarchands.Default)) return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	
	public String getImageDesac(){
		if(this.getStatus().equals(StatutContrat.ACTIF) ) 
			return "DeleteRed16x.png";
		else
			return "ok16x.png";
	}


	public String getNomClientOrange() {
		return nomClientOrange;
	}


	public void setNomClientOrange(String nomClientOrange) {
		this.nomClientOrange = nomClientOrange;
	}


	public String getTelOrange() {
		return telOrange;
	}


	public void setTelOrange(String telOrange) {
		this.telOrange = telOrange;
	}


	/**
	 * @return the dateDernCompta
	 */
	public Date getDateDernCompta() {
		return dateDernCompta;
	}


	/**
	 * @param dateDernCompta the dateDernCompta to set
	 */
	public void setDateDernCompta(Date dateDernCompta) {
		this.dateDernCompta = dateDernCompta;
	}


	/**
	 * @return the dateSaveDernCompta
	 */
	public Date getDateSaveDernCompta() {
		return dateSaveDernCompta;
	}


	/**
	 * @param dateSaveDernCompta the dateSaveDernCompta to set
	 */
	public void setDateSaveDernCompta(Date dateSaveDernCompta) {
		this.dateSaveDernCompta = dateSaveDernCompta;
	}


	/**
	 * @return the utiSuspendu
	 */
	public String getUtiSuspendu() {
		return utiSuspendu;
	}


	/**
	 * @param utiSuspendu the utiSuspendu to set
	 */
	public void setUtiSuspendu(String utiSuspendu) {
		this.utiSuspendu = utiSuspendu;
	}


	/**
	 * @return the dateSuspendu
	 */
	public Date getDateSuspendu() {
		return dateSuspendu;
	}


	/**
	 * @param dateSuspendu the dateSuspendu to set
	 */
	public void setDateSuspendu(Date dateSuspendu) {
		this.dateSuspendu = dateSuspendu;
	}
	
	public String getFormattedLastFactDate(){
		return PortalHelper.DEFAULT_DATE_FORMAT.format(dateDernCompta);
	}


	/**
	 * @return the consulSig
	 */
	public boolean isConsulSig() {
		return consulSig;
	}


	/**
	 * @param consulSig the consulSig to set
	 */
	public void setConsulSig(boolean consulSig) {
		this.consulSig = consulSig;
	}


	/**
	 * @return the generatedOtp
	 */
	public String getGeneratedOtp() {
		return generatedOtp;
	}


	/**
	 * @param generatedOtp the generatedOtp to set
	 */
	public void setGeneratedOtp(String generatedOtp) {
		this.generatedOtp = generatedOtp;
	}


	/**
	 * @return the dateResiliationCBS
	 */
	public String getDateResiliationCBS() {
		return dateResiliationCBS;
	}


	/**
	 * @param dateResiliationCBS the dateResiliationCBS to set
	 */
	public void setDateResiliationCBS(String dateResiliationCBS) {
		this.dateResiliationCBS = dateResiliationCBS;
	}


	/**
	 * @return the suspensTemp
	 */
	public String getSuspensTemp() {
		return suspensTemp;
	}


	/**
	 * @param suspensTemp the suspensTemp to set
	 */
	public void setSuspensTemp(String suspensTemp) {
		this.suspensTemp = suspensTemp;
	}


	/**
	 * @return the histoSubscriber
	 */
	public String getHistoSubscriber() {
		return histoSubscriber;
	}


	/**
	 * @param histoSubscriber the histoSubscriber to set
	 */
	public void setHistoSubscriber(String histoSubscriber) {
		this.histoSubscriber = histoSubscriber;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Subscriber [customerId=" + customerId + ", submsisdn=" + submsisdn + ", substatus=" + substatus + "]";
	}

	
	
}
