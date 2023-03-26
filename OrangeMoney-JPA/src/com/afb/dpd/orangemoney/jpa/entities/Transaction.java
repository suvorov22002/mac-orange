/**
 * 
 */
package com.afb.dpd.orangemoney.jpa.entities;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.afb.dpd.orangemoney.jpa.enums.ExceptionCategory;
import com.afb.dpd.orangemoney.jpa.enums.ExceptionCode;
import com.afb.dpd.orangemoney.jpa.enums.TransactionStatus;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jpa.tools.OgMoHelper;

/**
 * Classe representant une transaction Orange Money a executer dans le Core Banking
 * @author Francis KONCHOU
 * @version 1.0
 */
@Entity
@Table(name = "OGMo_TRANS")
@NamedQueries({    
	@NamedQuery(name="Transaction.findAll", query="SELECT t FROM Transaction t"),
	@NamedQuery(name=Transaction.UPDATE_TRANSACTION,query="update Transaction t set t.status=:status, t.posted=:posted, t.dateTraitement=:dateTraitement   where t.id=:id"),
})
public class Transaction implements Serializable {

	/**
	 * Default Serial UID
	 */
	private static final long serialVersionUID = 1L;

	public static final String UPDATE_TRANSACTION = "Transaction.mergesPostingTransaction";

	/**
	 * Default Constructor
	 */
	public Transaction() {}

	/**
	 * Id auto genere
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Operation
	 */
	@Column(name = "OP", nullable = false)
	@Enumerated(EnumType.STRING)
	private TypeOperation typeOperation;

	/**
	 * Souscripteur
	 */
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "SUBS_ID")
	private Subscriber subscriber;

	/**
	 * Montant
	 */
	@Column(name = "AMOUNT", nullable = false)
	private Double amount = 0d;

	/**
	 * Numero de compte
	 */
	@Column(name = "ACCOUNT", nullable = false)
	private String account;

	/**
	 * Numero de telephone
	 */
	@Column(name = "PHONE")
	private String phoneNumber;

	/**
	 * Date de l'operation
	 */
	@Column(name = "DATE_OP", nullable = false)
	private Date date = new Date();


	@Column(name = "DATETIME_OP", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetime = new Date();

	/**
	 * intDatetime
	 */
	@Column(name = "INIDATETIME_OP", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date intDatetime = new Date();

	/**
	 * Etat de la transaction
	 */
	/**@Column(name = "INITSTATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private TransactionStatus initStatus = TransactionStatus.PROCESSING;*/

	/**
	 * Etat de la transaction
	 */
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private TransactionStatus status = TransactionStatus.PROCESSING;

	/**
	 * Total des commissions de la transaction
	 */
	@Column(name = "COMMISSIONS", nullable = false)
	private Double commissions = 0d;

	/**
	 * Montant TTC
	 */
	@Column(name = "TTC", nullable = false)
	private Double ttc = 0d;

	/**
	 * Type d'exception levee
	 */
	@Column(name = "ERROR_TYPE", nullable = true)
	@Enumerated(EnumType.STRING)
	private ExceptionCategory exceptionCategory;

	/**
	 * Code de l'exception
	 */
	@Column(name = "ERROR_CODE", nullable = true)
	@Enumerated(EnumType.STRING)
	private ExceptionCode exceptionCode;

	/**
	 * Determine si la transaction a ete postee dans le Core Banking ou non
	 */
	@Column(name = "POSTED")
	private Boolean posted = Boolean.FALSE;

	/**
	 * Completer une transaction 
	 */
	@Column(name = "completed")
	private boolean completed = Boolean.FALSE;

	/**
	 * reverseTrans
	 */
	@Column(name = "reverseTrans")
	private String reverseTrans = "";
	
	/**
	 * traceStatus
	 */
	@Column(name = "trace_status", length=1024)
	private String traceStatus = "";

	/**
	 * Completer une transaction 
	 */
	@Column(name = "completStatus")
	private TransactionStatus completStatus;

	/**
	 * Determine 
	 */
	@Column(name = "compenser")
	private Boolean compenser = Boolean.FALSE;



	@Column
	private String requestId;

	@Column
	private Double solde;

	@Column
	private String externalRefNo;

	@Column
	private String requestToken;

	@Column
	private String operatorCode;

	@Column
	private String accountAlias;

	@Column
	private Double charge = 0d;

	@Column
	private String exceptionCodeName = "";




	/**
	 * Date de comptabilisation/facturation
	 */
	@Column(name = "DATECOMPTA", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date dateCompta;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_TRAITEMENT", nullable = true)
	private Date dateTraitement;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_CONTROLE", nullable = true)
	private Date dateControle;
	
	
	
	/**
	 * TFJO Portal lance???
	 */
	@Column(name = "TFJO_LANCE")
	private Boolean tfjoLance = Boolean.FALSE;

	@Column(name = "MODE_NUIT_AMPLITUDE_ACTIVE")
	private Boolean modeNuitAmplitudeActive = Boolean.FALSE;

	@Column(name = "A_RETRAITER")
	private Boolean aRetraiter = Boolean.FALSE;
	
	


	@Column
	private String lastSolde = "0";

	@Version
	@Column(columnDefinition = "integer DEFAULT 0", nullable = false)
	private Long version;
	
	@Column
	private String tfjoStatus = "";

	

	@Transient
	private boolean selected = true;

	@Transient
	private boolean areconcilier = false;

	@Transient
	private boolean ok = false;

	@Transient
	private boolean success = false;


	@Transient
	private String libelle;

	@Transient
	private String referenceLettrage;

	@Transient
	private String sens;

	@Transient
	private Double montCredit = 0d;

	@Transient
	private Double montDebit = 0d;



	/**
	 * @param typeOperation
	 * @param subscriber
	 * @param amount
	 * @param account
	 * @param phoneNumber
	 */
	public Transaction(TypeOperation typeOperation, Subscriber subscriber, Double amount, String account, String phoneNumber) {
		super();
		this.typeOperation = typeOperation;
		this.subscriber = subscriber;
		this.amount = amount;
		this.account = account;
		this.phoneNumber = phoneNumber;
	}

	public Transaction(TypeOperation typeOperation, Subscriber subscriber, Double amount, String account, String phoneNumber,RequestMessage req) {
		super();
		this.typeOperation = typeOperation;
		this.subscriber = subscriber;
		this.amount = amount;
		this.account = account;
		this.phoneNumber = phoneNumber;

		if(this.typeOperation.equals(TypeOperation.Wallet2Account) || this.typeOperation.equals(TypeOperation.Account2Wallet)){
			this.externalRefNo = req.getExternalRefNo();
			this.charge = req.getCharge();
		}

		this.requestId = req.getRequestId();
		this.requestToken = req.getRequestToken();
		this.operatorCode = req.getOperatorCode();
		this.accountAlias = req.getAccountAlias();

	}

	public Transaction(TypeOperation typeOperation, Subscriber subscriber, Double amount,
			String account, String phoneNumber, TransactionStatus status) {
		super();
		this.typeOperation = typeOperation;
		this.subscriber = subscriber;
		this.amount = amount;
		this.account = account;
		this.phoneNumber = phoneNumber;
		this.status = status;
	}

	/**
	 * @param typeOperation
	 * @param subscriber
	 * @param amount
	 * @param account
	 * @param phoneNumber
	 * @param date
	 * @param commissions
	 * @param ttc
	 */
	public Transaction(TypeOperation typeOperation, Subscriber subscriber,
			Double amount, String account, String phoneNumber, Date date,
			Double commissions, Double ttc) {
		super();
		this.typeOperation = typeOperation;
		this.subscriber = subscriber;
		this.amount = amount;
		this.account = account;
		this.phoneNumber = phoneNumber;
		this.date = date;
		this.commissions = commissions;
		this.ttc = ttc;
	}


	/**
	 * @param typeOperation
	 * @param subscriber
	 * @param amount
	 * @param account
	 * @param phoneNumber
	 * @param date
	 * @param commissions
	 * @param ttc
	 */
	public Transaction(TypeOperation typeOperation, Subscriber subscriber,
			Double amount, String account, String phoneNumber, Date date,
			TransactionStatus status, Double commissions, Double ttc, Date dateCompta) {
		super();
		this.typeOperation = typeOperation;
		this.subscriber = subscriber;
		this.amount = amount;
		this.account = account;
		this.phoneNumber = phoneNumber;
		this.date = date;
		this.status = status;
		this.commissions = commissions;
		this.ttc = ttc;
		this.dateCompta = dateCompta;
	}




	public Transaction(SuspensOrange suspensOrange) {
		super();
		this.date = suspensOrange.getDate();
		this.libelle = suspensOrange.getLibelle();
		this.referenceLettrage = suspensOrange.getReferenceLettrage();
		this.sens = suspensOrange.getSens();
		this.montCredit = suspensOrange.getMontCredit();
		this.montDebit = suspensOrange.getMontDebit();
		this.account = suspensOrange.getAccount();
		this.typeOperation = suspensOrange.getTypeOperation();		
	}



	/**
	 * @return the compenser
	 */
	public Boolean getCompenser() {
		return compenser;
	}

	/**
	 * @param compenser the compenser to set
	 */
	public void setCompenser(Boolean compenser) {
		this.compenser = compenser;
	}

	/**
	 * @return the ok
	 */
	public boolean isOk() {
		return ok;
	}

	/**
	 * @param ok the ok to set
	 */
	public void setOk(boolean ok) {
		this.ok = ok;
	}



	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public TransactionStatus getCompletStatus() {
		return completStatus;
	}

	public void setCompletStatus(TransactionStatus completStatus) {
		this.completStatus = completStatus;
	}

	/**
	 * @return the intDatetime
	 */
	public Date getIntDatetime() {
		return intDatetime;
	}

	/**
	 * @param intDatetime the intDatetime to set
	 */
	public void setIntDatetime(Date intDatetime) {
		this.intDatetime = intDatetime;
	}

	/**
	 * @return the areconcilier
	 */
	public boolean isAreconcilier() {
		return areconcilier;
	}

	/**
	 * @param areconcilier the areconcilier to set
	 */
	public void setAreconcilier(boolean areconcilier) {
		this.areconcilier = areconcilier;
	}

	/**
	 * @return the datetime
	 */
	public Date getDatetime() {
		return datetime;
	}

	public String getExceptionCodeName() {
		if(exceptionCodeName == null) exceptionCodeName = exceptionCode.name();
		return exceptionCodeName;
	}

	public void setExceptionCodeName(String exceptionCodeName) {
		this.exceptionCodeName = exceptionCodeName;
	}

	/**
	 * @param datetime the datetime to set
	 */
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	/**
	 * @return the charge
	 */
	public Double getCharge() {
		return charge;
	}

	/**
	 * @param charge the charge to set
	 */
	public void setCharge(Double charge) {
		this.charge = charge;
	}

	/**
	 * @return the externalRefNo
	 */
	public String getExternalRefNo() {
		return externalRefNo;
	}

	/**
	 * @param externalRefNo the externalRefNo to set
	 */
	public void setExternalRefNo(String externalRefNo) {
		this.externalRefNo = externalRefNo;
	}

	/**
	 * @return the requestToken
	 */
	public String getRequestToken() {
		return requestToken;
	}

	/**
	 * @param requestToken the requestToken to set
	 */
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
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
	 * @return the accountAlias
	 */
	public String getAccountAlias() {
		return accountAlias;
	}

	/**
	 * @param accountAlias the accountAlias to set
	 */
	public void setAccountAlias(String accountAlias) {
		this.accountAlias = accountAlias;
	}

	/**
	 * @return the solde
	 */
	public Double getSolde() {
		return solde;
	}

	/**
	 * @param solde the solde to set
	 */
	public void setSolde(Double solde) {
		this.solde = solde;
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
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
	 * @return the typeOperation
	 */
	public TypeOperation getTypeOperation() {
		return typeOperation;
	}

	/**
	 * @param typeOperation the typeOperation to set
	 */
	public void setTypeOperation(TypeOperation typeOperation) {
		this.typeOperation = typeOperation;
	}

	/**
	 * @return the subscriber
	 */
	public Subscriber getSubscriber() {
		return subscriber;
	}

	/**
	 * @param subscriber the subscriber to set
	 */
	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
	public TransactionStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	/**
	 * @return the exceptionCategory
	 */
	public ExceptionCategory getExceptionCategory() {
		return exceptionCategory;
	}

	/**
	 * @param exceptionCategory the exceptionCategory to set
	 */
	public void setExceptionCategory(ExceptionCategory exceptionCategory) {
		this.exceptionCategory = exceptionCategory;
	}

	/**
	 * @return the exceptionCode
	 */
	public ExceptionCode getExceptionCode() {
		return exceptionCode;
	}

	/**
	 * @param exceptionCode the exceptionCode to set
	 */
	public void setExceptionCode(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
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
	 * @return the ttc
	 */
	public Double getTtc() {
		return Math.ceil( ttc );
	}

	/**
	 * @param ttc the ttc to set
	 */
	public void setTtc(Double ttc) {
		this.ttc = ttc;
	}

	public Double getTaxes() {
		return this.ttc - (this.amount + this.commissions);
	}

	public String getFormattedDate() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);   //  PortalHelper.DEFAULT_DATE_FORMAT.format(date);
	}

	public String getFormattedMontant() {
		return OgMoHelper.espacement(amount);
	}

	public String getHour() {
		return OgMoHelper.sdf_HOUR.format(date);
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @return the posted
	 */
	public Boolean getPosted() {
		return posted;
	}

	/**
	 * @param posted the posted to set
	 */
	public void setPosted(Boolean posted) {
		this.posted = posted;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getRoundedTaxes() {
		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.HALF_UP);
		return df.format(getTaxes());
	}

	public boolean isSuccess() {
		this.success = true;
		if(!this.status.equals(TransactionStatus.SUCCESS)) this.success = false;
		return this.success;
	}


	public void setSuccess(boolean sucess) {
		this.success = sucess;
	}
	
	
	public String getColor() {
		if(this.status.equals(TransactionStatus.SUCCESS) && this.posted.equals(Boolean.TRUE) && this.completed == Boolean.TRUE){
			return "black";
		}
		if(this.status.equals(TransactionStatus.SUCCESS) && this.posted.equals(Boolean.FALSE) && this.completed == Boolean.TRUE){
			return "blue";
		}
		if(this.status.equals(TransactionStatus.SUCCESS) && this.posted.equals(Boolean.FALSE) && this.completed == Boolean.FALSE){
			return "green";
		}
		if(this.status.equals(TransactionStatus.CANCEL)){
			return "orange";
		}
		else{
			return "red";
		}
	}
	
	
	public String getStatusImage() {
		if(this.status.equals(TransactionStatus.SUCCESS)){
			return "ok16x.png";
		}
		if(this.status.equals(TransactionStatus.CANCEL)){
			return "DeleteRed16x.png";
		}
		if(this.status.equals(TransactionStatus.PROCESSING)){
			return "initpwd.png";
		}
		else{
			return "DeleteRed16x.png";
		}
	}


	public boolean getCompletedTrans() {
		if(this.status.equals(TransactionStatus.PROCESSING) && this.isCompleted() == Boolean.FALSE && (this.typeOperation.equals(TypeOperation.Account2Wallet) || this.typeOperation.equals(TypeOperation.Wallet2Account))){
			return Boolean.FALSE;
		}
		else{
			return Boolean.TRUE;
		}
	}

	/**
	 * @return the dateCompta
	 */
	public Date getDateCompta() {
		return dateCompta;
	}

	/**
	 * @param dateCompta the dateCompta to set
	 */
	public void setDateCompta(Date dateCompta) {
		this.dateCompta = dateCompta;
	}

	/**
	 * @return the dateTraitement
	 */
	public Date getDateTraitement() {
		return dateTraitement;
	}

	/**
	 * @param dateTraitement the dateTraitement to set
	 */
	public void setDateTraitement(Date dateTraitement) {
		this.dateTraitement = dateTraitement;
	}

	/**
	 * @return the dateControle
	 */
	public Date getDateControle() {
		return dateControle;
	}

	/**
	 * @param dateControle the dateControle to set
	 */
	public void setDateControle(Date dateControle) {
		this.dateControle = dateControle;
	}

	/**
	 * @return the reverseTrans
	 */
	public String getReverseTrans() {
		return reverseTrans;
	}

	/**
	 * @param reverseTrans the reverseTrans to set
	 */
	public void setReverseTrans(String reverseTrans) {
		this.reverseTrans = reverseTrans;
	}

	/**
	 * @return the traceStatus
	 */
	public String getTraceStatus() {
		return traceStatus;
	}

	/**
	 * @param traceStatus the traceStatus to set
	 */
	public void setTraceStatus(String traceStatus) {
		this.traceStatus = traceStatus;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getReferenceLettrage() {
		return referenceLettrage;
	}

	public void setReferenceLettrage(String referenceLettrage) {
		this.referenceLettrage = referenceLettrage;
	}

	public String getSens() {
		return sens;
	}

	public void setSens(String sens) {
		this.sens = sens;
	}

	public Double getMontCredit() {
		return montCredit;
	}

	public void setMontCredit(Double montCredit) {
		this.montCredit = montCredit;
	}

	public Double getMontDebit() {
		return montDebit;
	}

	public void setMontDebit(Double montDebit) {
		this.montDebit = montDebit;
	}
	
	
	

	/**
	 * @return the tfjoLance
	 */
	public Boolean getTfjoLance() {
		return tfjoLance;
	}

	/**
	 * @param tfjoLance the tfjoLance to set
	 */
	public void setTfjoLance(Boolean tfjoLance) {
		this.tfjoLance = tfjoLance;
	}

	/**
	 * @return the aRetraiter
	 */
	public Boolean getaRetraiter() {
		return aRetraiter;
	}

	/**
	 * @param aRetraiter the aRetraiter to set
	 */
	public void setaRetraiter(Boolean aRetraiter) {
		this.aRetraiter = aRetraiter;
	}

	/**
	 * @return the modeNuitAmplitudeActive
	 */
	public Boolean getModeNuitAmplitudeActive() {
		return modeNuitAmplitudeActive;
	}

	/**
	 * @param modeNuitAmplitudeActive the modeNuitAmplitudeActive to set
	 */
	public void setModeNuitAmplitudeActive(Boolean modeNuitAmplitudeActive) {
		this.modeNuitAmplitudeActive = modeNuitAmplitudeActive;
	}

	
	//A-decommenter
	
	
	public Long getVersion() {
		return version;
	}

	/**
	 * @return the lastSolde
	 */
	public String getLastSolde() {
		return lastSolde;
	}

	/**
	 * @param lastSolde the lastSolde to set
	 */
	public void setLastSolde(String lastSolde) {
		this.lastSolde = lastSolde;
	}

	/**
	 * @return the tfjoStatus
	 */
	public String getTfjoStatus() {
		return tfjoStatus;
	}

	/**
	 * @param tfjoStatus the tfjoStatus to set
	 */
	public void setTfjoStatus(String tfjoStatus) {
		this.tfjoStatus = tfjoStatus;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Transaction [subscriber=" + subscriber.toString() + ", status=" + status + ", ttc=" + ttc + "]";
	}

	
}
