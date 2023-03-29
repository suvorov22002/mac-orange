package com.afb.dpd.orangemoney.business;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jettison.json.JSONException;

import com.afb.dpd.orangemoney.jpa.entities.BranchMails;
import com.afb.dpd.orangemoney.jpa.entities.Comptabilisation;
import com.afb.dpd.orangemoney.jpa.entities.FactMonth;
import com.afb.dpd.orangemoney.jpa.entities.Parameters;
import com.afb.dpd.orangemoney.jpa.entities.ProfilMarchands;
import com.afb.dpd.orangemoney.jpa.entities.Rapprochement;
import com.afb.dpd.orangemoney.jpa.entities.RequestMessage;
import com.afb.dpd.orangemoney.jpa.entities.Subscriber;
import com.afb.dpd.orangemoney.jpa.entities.TraceRobot;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jpa.entities.TransactionDetail;
import com.afb.dpd.orangemoney.jpa.entities.USSDTransaction;
import com.afb.dpd.orangemoney.jpa.enums.TransactionStatus;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jpa.enums.TypeRapprochement;
import com.afb.dpd.orangemoney.jpa.exception.DAOAPIException;
import com.afb.dpd.orangemoney.jpa.exception.OgMoException;
import com.afb.dpd.orangemoney.jpa.tools.ClientProduit;
import com.afb.dpd.orangemoney.jpa.tools.Doublon;
import com.afb.dpd.orangemoney.jpa.tools.Equilibre;
import com.afb.dpd.orangemoney.jpa.tools.TypeCompte;
import com.afb.dpd.orangemoney.jpa.tools.bkeve;
import com.afb.dpd.orangemoney.jpa.tools.bkmvti;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.PropertyContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

import afb.dsi.dpd.portal.jpa.entities.User;

/**
 * Service Principal de Gestion du Module Orange Money
 * @author Francis KONCHOU
 * @version 1.0
 */
@Remote
public interface IOrangeMoneyManagerRemote {
	
	/**
	 * Nom du service
	 */
	public static final String SERVICE_NAME = "OrangeMoneyManager";
	
	
	public <T> T findByPrimaryKey(Class<T> entityClass, Object entityID,PropertyContainer properties);
	
	/**
	 * Enregistre les parametres generaux
	 * @param param
	 */
	public void saveParameters(Parameters param);
	
	
	
	public BranchMails saveBranchMails(BranchMails branchMails);
	
	public BranchMails updateBranchMails(BranchMails branchMails);
	
	public void deleteBranchMails(Long id);
	
	public List<BranchMails> filterBranchMails(RestrictionsContainer rc);
		
	
	public void saveProfilMarchands(List<ProfilMarchands> profils);
	
	
	public List<ProfilMarchands> filterProfilMarchands();
	
	
	public void profilSubscriptions();
	
	
	public void activerSubscriptions();
	
	/**
	 * Enregistre une souscription
	 * @param subscriber
	 * @return
	 */
	public Subscriber saveSubscriber(Subscriber subscriber) throws Exception, OgMoException;
	
	/**
	 * Modifie une souscription
	 * @param subscriber
	 * @return
	 */
	public Subscriber updateSubscriber(Subscriber subscriber) throws Exception, OgMoException;
	
	/**
	 * Modifie le bank PIN d'un souscripteur
	 * @param subscriber
	 * @return
	 */
	//public Subscriber updateBankPIN(Subscriber subscriber) throws Exception;
	
	/**
	 * Supprime une souscription
	 * @param subscriberId
	 */
	public void deleteSubscriber(Long subscriberId);
	
	
	public void sendMail(List<String> mailsTo, String subject, String messageCorps, Parameters parametre) throws Exception;
	
	
	/**
	 * Determine si le client possede deja une souscription
	 * @param customerId
	 * @return
	 */
	public boolean subscriptionAlreadyExist(String customerId);
	
	/**
	 * Liste les souscriptions
	 * @param rc
	 * @param orders
	 * @return
	 */
	public List<Subscriber> filterSubscriptions( RestrictionsContainer rc, OrderContainer orders );
	
	/**
	 * Annule une souscription
	 * @param sousId
	 */
	public void annulerSouscription(Long sousId,User user) throws Exception , OgMoException;
	
	
	public void updateTransReconcliation(Long id, String reverseTrans, boolean completed, TransactionStatus status, String traceStatus) throws Exception , OgMoException;
	
	/**
	 * Supprime une souscription
	 * @param sousId
	 */
	public void deleteSouscription(Long sousId);
	
	/**
	 * Genere une transaction sur la base d'un message recu du SDP
	 * @param message
	 * @return
	 */
	public Transaction generateTransactionFromMessage( RequestMessage message );
	
	/**
	 * Sauvegarde une transaction
	 * @param transaction
	 * @return
	 */
	public Transaction saveTransaction( Transaction transaction );
	
	/**
	 * Met a jour la transaction passee en parametre
	 * @param transaction
	 * @return
	 */
	public Transaction updateTransaction( Transaction transaction );
	
	/**
	 * Filtre les transactions
	 * @param rc
	 * @param orders
	 * @return
	 */
	public List<Transaction> filterTransactions( RestrictionsContainer rc, OrderContainer orders ); 
	
	public void completeTransactions();
		
	public List<TraceRobot> filterTraceRobots(RestrictionsContainer rc, OrderContainer orders);
			
	public List<Transaction> filterTransactionProcessing();
	
	public List<Transaction> filterTransactions(Date deb, Date fin, TypeOperation selectedTypOp, TransactionStatus selectedTypStatus, String nom, String tel, String ncp);
	
	
	/**
	 * Supprime la liste de transactions passees en parametre
	 * @param transactions
	 *
	public void purgeTransactions( List<Transaction> transactions );*/
	
	/**
	 * Consulte la configuration du module
	 * @return
	 */
	public Parameters consulterConfiguration();
	
	/**
	 * Genere la liste des ecritures comptables pour une liste de transactions definies
	 * @param transactions
	 */
	public void generateAccountingEntries( List<Transaction> transactions );
	
	/**
	 * Initialisation des parametres par defaut du module
	 */
	public void initialisations();
	
	/**
	 * Retourne les parametres generaux du module
	 * @return
	 */
	public Parameters findParameters();
	
	/**
	 * Recupere la liste des types de comptes dans Amplitude
	 * @return
	 */
	public List<TypeCompte> filterTypeCompteFromAmplitude();
	
	/**
	 * Recherche un client et ses comptes dans Amplitude
	 * @param customerId
	 * @return
	 */
	public Subscriber findCustomerFromAmplitude(String customerId);
	
	public boolean verifierCompteDormant(String account);
	
	public boolean checkTransactionDay(String account);
	
	public boolean checkNewAccount(String account);
	
	/**
	 * 
	 * @param CustomerId
	 * @return
	 */
	public Subscriber findSubscriber(String  CustomerId, String subkey);
	
	public Subscriber findSubscriberbankPINBankPIN(String  value);
	
	/**
	 * Recherche une transaction a partir de l'id du souscripteur
	 * @param subsId
	 * @return
	 */
	public Transaction findTransactionBySubscriber(Long subsId);
	
	/**
	 * Recherche un souscripteur a partir du numero de telephone
	 * @param phoneNumber
	 * @return
	 */
	public Subscriber findSubscriberFromPhoneNumber(String phoneNumber);
	
	/**
	 * Recherche un souscripteur autre que celui passe en parametre a partir du numero de telephone
	 * @param phoneNumber
	 * @param subsID
	 * @return
	 */
	public Subscriber findSubscriberFromPhoneNumber(String phoneNumber, Long subsID);
	
	/**
	 * Poste l'evenement de la transaction dans Amplitude et met a jour les soldes des comptes
	 * @param transaction
	 * @throws Exception
	 *
	public void posterEvenementDansAmplitude(Transaction transaction) throws Exception;
	*/
	
	/**
	 * Recherche les numeros de comptes du client fourni 
	 * @param customerId
	 * @return
	 */
	public List<String> filterCustomerAccountsFromCBS(String customerId);
	
	/**
	 * Poste l'evenement d'une transaction dans le Core Banking
	 * @param eve
	 * @throws Exception
	 */
	public Transaction posterEvenementDansCoreBanking(Transaction transaction) throws Exception, OgMoException;
	
	/**
	 * Genere l'evenement Delta de la transaction 
	 * @param transaction
	 * @return
	 * @throws Exception
	 */
	public bkeve buildEvenement(Transaction transaction) throws Exception;
	
	/**
	 * Poste les ecritures comptables dans bkmvti
	 * @param mvts
	 * @throws Exception
	 *
	public void posterEcrituresDansCoreBanking(List<bkmvti> mvts) throws Exception;*/
	
	//public boolean isTfjEnCours() throws Exception;
	
	/**
	 * Envoi du PIN banque par SMS au client
	 * @param subs
	 */
	public void sendCodePINBySMS(Subscriber subs);
	
	/**
	 * Extrait la liste des ecritures comptables d'une liste de transactions selectionnees
	 * @param transactions
	 * @param poster Determine si on veut poster les ecritures dans le Core Banking
	 * @return
	 */
	public List<bkmvti> getECFromTransactions(List<Transaction> transactions, boolean poster);
	
	/**
	 * Exporte la liste des ecritures comptables des transactions passees en parametre dans un fichier
	 * @param transactions
	 * @param fileName
	 * @throws Exception
	 */
	public List<bkmvti> extractECFromSelectedTransactionsIntoFile(List<Transaction> transactions, String fileName) throws Exception;
	
	/**
	 * Poste les ecritures comptables issues des transactions passees en parametre dans Delta
	 * @param transactions
	 */
	public void posterTransactionsDansCoreBanking(List<Transaction> transactions) throws Exception;
	
	/**
	 * Archive la liste des transactions selectionnees
	 * @param transactions
	 * @throws Exception
	 */
	public void archiverTransactions(List<Transaction> transactions) throws Exception;
	
	/**
	 * Supprime la liste des transactions selectionnees
	 * @param transactions
	 * @throws Exception
	 */
	public void purgerTransactions(List<Transaction> transactions) throws Exception;
	
	/**
	 * Envoi un sms au numero de telephone fournit
	 * @param message
	 * @param phoneNumber
	 */
	public void sendSMS(String message, String phoneNumber);
	
	/**
	 * Determine si le numero de cpte passe en parametre est en regle ou non pour des operations
	 * @param numCompte
	 * @return
	 */
	public boolean isCompteFerme(String numCompte);
	
	/**
	 * Verifi si le solde du cpte est suffisant pour le retrait
	 * @param numCompte
	 * @param montant
	 * @return
	 */
	// public boolean isSoldeSuffisant(String numCompte, Double montant) throws Exception;
	
	/**
	 * Traite le message passe en parametre
	 * @param message
	 * @throws Exception
	 */
	//public Transaction processPullPushMessage(RequestMessage message) throws Exception, OgMoException;
	
	/**
	 * Service de Traitement d'une transaction de type PULL
	 * @param phoneNumber
	 * @param bankPIN
	 * @param amount
	 * @throws Exception
	 */
	//public Transaction processPullTransaction(String phoneNumber, String bankPIN, Double amount) throws Exception, OgMoException;
	
	/**
	 * Service de traitement d'une transaction de type PUSH
	 * @param phoneNumber
	 * @param bankPIN
	 * @param amount
	 * @throws Exception
	 */
	//public Transaction processPushTransaction(String phoneNumber, String bankPIN, Double amount) throws Exception, OgMoException;
	
	/**
	 * Service de traitement d'une transaction de type BALANCE
	 * @param phoneNumber
	 * @param bankPIN
	 * @throws Exception
	 */
	//public Double processBalanceTransaction(String phoneNumber, String bankPIN) throws Exception, OgMoException;
	
	/**
	 * Service de traitement d'une transaction de type REVERSAL
	 * @param phoneNumber
	 * @param bankPIN
	 * @param txCode
	 * @throws Exception
	 */
	public Map<String, String> processReversalTransaction(String externalRefNo) throws Exception;
	
	
	public Map<String, String> processStatutTransaction(String externalRefNo) throws Exception;
	
	/**
	 * Determine si le numero de telephone passe en parametre est celui d'un client ayant souscrit au Pusll/Push
	 * @param phoneNumber
	 * @return
	 * @throws Exception
	 */
	public boolean checkSubscriber(String phoneNumber) throws Exception;
	
	public List<TransactionDetail> processGetMiniStament(String accountAlias,String requestId) throws Exception;

	public Map<String,String> processA2W(String bankPIN, Double amount,Double charge, String requestId, String externalRefNo, String requestToken, String operatorCode, String accountAlias) throws Exception;
	
	
	public Map<String,String> processW2A(String bankPIN, Double amount,Double charge, String requestId, String externalRefNo, String requestToken, String operatorCode, String accountAlias) throws Exception;
	
	
	public Map<String,String> processBalance(String bankPin,String requestId) throws Exception;
	
	
	public void processCompleteTransaction(String externalRefNo) throws Exception, OgMoException;
	
	
	
	/**
	 * Retourne la liste des transactions USSD
	 * @param rc
	 * @return
	 */
	public List<USSDTransaction> filterUSSDTransactions(RestrictionsContainer rc);
	
	/**
	 * Execute la reconciliation en cas d'ecehec des transactions MoMo
	 * @param trans
	 */
	public void executerReconciliation(List<USSDTransaction> trans) throws Exception, OgMoException;
	
	/**
	 * Comptabilisation periodique des transactions Pull et Push
	 * @return
	 * @throws Exception
	 */
	public List<Comptabilisation> executerTFJO() throws Exception;
	
	/**
	 * Validation de la comptabilisation des operations Pull/Push
	 * @param data
	 * @throws Exception
	 */
	public FactMonth validerTFJO(List<Comptabilisation> data,String user) throws Exception;
	
	/**
	 * Exporte les ecritures generees lors de la comptabilisation ds un fichier excel
	 * @param data
	 * @param fileName
	 * @throws Exception
	 */
	public void exportComptabilisationIntoExcelFile( List<Comptabilisation> data, String fileName ) throws Exception;
	
	/**
	 * Execute la compensation des operations Pull/Push
	 * @param dateDeb
	 * @param dateFin
	 * @throws Exception
	 */
	public FactMonth executerCompensation(Date dateDeb, Date dateFin,String user , String mois) throws Exception;
	
	public List<Transaction> getTransactionTFJOs(Integer limit) throws Exception ;
	
	public List<Transaction> getTransactionTFJOStatusProcess() throws Exception;
	
	public List<bkmvti> genererEcritureTFJOs(List<Transaction> trans) throws Exception;	
	
	public FactMonth visualiserRapportTFJOs(List<bkmvti> mvts, Date dateAbon, String user, String mois, String fileName, Parameters param) throws Exception;
	
	public FactMonth visualiserRapportTFJOTrans(List<Transaction> transactions,  Date dateAbon, String user, String mois, String fileName, Parameters param) throws Exception;
	
	public List<bkmvti> extractECFromTransactions(List<Transaction> transactions) throws Exception;
	
	
	public List<bkmvti> extractECIntoFile(List<bkmvti> mvts, String fileName) throws Exception;
	
	public List<String> annulerEves(List<Transaction> transactions) throws Exception;
	
	
	public List<Transaction> filterTransactionsAreconcilier() throws Exception;
	
	
	public String checkTransactionsNonreconcilie() throws Exception;
	
	public String checkRobotReconciliation() throws Exception;
	
		
	/**
	 * Recupere l'url de la signature
	 * @param age
	 * @param ncp
	 * @param suf
	 * @param cli
	 * @param datec
	 * @param heurec
	 * @param utic
	 * @return
	 * @throws Exception
	 */
	public String getLienSig(String ncp, String utic) throws Exception;
	
	public List<Subscriber> findAllSubscriberNonFactures();
	
	public void facturerListSubscribers(List<Subscriber> list) throws Exception , OgMoException;
	
	public List<Comptabilisation> executerTFJO2() throws Exception;
	
	
	public String lastExecutionRobot();
	
	public String lastExecutionAlerte();
	
	
	public void exportTransactionIntoExcelFile( List<Transaction> data, String fileName ) throws Exception;
	
	
	public void exportSouscriptionIntoExcelFile( List<Subscriber> data, String fileName ) throws Exception;
	
	
	public List<Transaction> chargerDonneesComptabiliserTFJO(Date date) throws Exception;
	
	
	public void executerTFJO2(Date date) throws Exception;
	
	
	public List<Transaction> chargerDonneesComptabiliserRegul() throws Exception;
	
	
	public void exportComptaIntoExcelFile( List<Transaction> data, String fileName ) throws Exception;
	
	
	public void exportRapprochmentBkmvti(String fileName, List<bkmvti> ecritures, List<bkmvti> ecrituresCli, List<bkmvti> ecrituresOrange, List<bkmvti> ecrituresCliRapp, 
			List<bkmvti> ecrituresOrangeRapp, List<bkmvti> ecrituresCliUnijamb, List<bkmvti> ecrituresOrangeUnijamb, List<bkmvti> ecrituresRemove, List<Transaction> transactionsRemove) throws Exception;
	
	
	public void exportRapprochmentBkeve(String fileName, List<Transaction> transactions, List<bkeve> evePortals, List<bkeve> eveCBS, List<bkeve> evePortalRapps, List<bkeve> eveCBSRapps, 
			List<bkeve> evePortalSuspens, List<bkeve> eveCBSSuspens) throws Exception;
	
	
	public void validerTFJO(List<Transaction> data, String user, int year, int month) throws Exception;
	
	
	/**
	 * Recuperer le rapport d'equilibre
	 * @return Liste des equilibres
	 * @throws Exception
	 */
	public List<Equilibre> getRapportEquilibre() throws Exception;
	
	/**
	 * Recuperer le rapport des doublons
	 * @return liste des doublons
	 * @throws Exception
	 */
	public List<Doublon> getRapportDoublon() throws Exception;
	
	/**
	 * Recuperer le nombre total des abonnes comptabilises
	 * @return nombre total des abonnes comptabilises
	 * @throws Exception
	 */
	public int getTotalAbonComptabilises() throws Exception;
	
	/**
	 * Recuperer le montant total des commisions des abonnements comptabilises
	 * @return montant total des commisions des abonnements comptabilises
	 * @throws Exception
	 */
	public Double getTotalComsAbonComptabilises() throws Exception;
	
	/**
	 * Recuperer le montant total des taxes des abonnements comptabilises
	 * @return montant total des taxes des abonnements comptabilises
	 * @throws Exception
	 */
	public Double getTotalTaxAbonComptabilises() throws Exception;
	
	
	/**
	 * Recuperer le rapport d'equilibre
	 * @return Liste des equilibres
	 * @throws Exception
	 */
	public List<Equilibre> getRapportEquilibre(String util, String typeOp) throws Exception;
	
	/**
	 * Recuperer le rapport des doublons
	 * @return liste des doublons
	 * @throws Exception
	 */
	public List<Doublon> getRapportDoublon(String util, String typeOp) throws Exception;
	
	/**
	 * Recuperer le nombre total des abonnes comptabilises
	 * @return nombre total des abonnes comptabilises
	 * @throws Exception
	 */
	public int getTotalAbonComptabilises(String util, String typeOp) throws Exception;
	
	/**
	 * Recuperer le montant total des commisions des abonnements comptabilises
	 * @return montant total des commisions des abonnements comptabilises
	 * @throws Exception
	 */
	public Double getTotalComsAbonComptabilises(String util, String typeOp) throws Exception;
	
	/**
	 * Recuperer le montant total des taxes des abonnements comptabilises
	 * @return montant total des taxes des abonnements comptabilises
	 * @throws Exception
	 */
	public Double getTotalTaxAbonComptabilises(String util, String typeOp) throws Exception;
	
	
	
	
	public Rapprochement executeRapprochement(TypeOperation typeOpe, String user, String ncp, Date debut, Date fin) throws Exception;
	
	
	public Rapprochement executeImputation(TypeOperation typeOpe, String sens, String user, String comptes, Date debut, Date fin) throws Exception;
	
	
	public Date getLastRapprochement(TypeOperation typeOpe, TypeRapprochement typeRapprochement, String ncpRappro) throws Exception;
	
	
	public List<Rapprochement> filterRapprochements(RestrictionsContainer rc, OrderContainer orders);
	
	
	public Date getLastTransaction() throws Exception;
	
	
	
	
	public List<Transaction> filterTransactionARetraiter();
	
	public List<Transaction> rePosterEvenementDansCoreBanking(List<Transaction> transactions) throws Exception;
	
	
	public boolean isTFJOPortalEnCours() throws Exception;
		
	public void suiviRobot();
		
	public void suiviReservation(String fileName, String heureDebut, String heureFin, boolean afterTFJO) throws Exception;
	
	
	public int alerteCompteDormant(User user, String selectedAccount, String message) throws Exception;
	
	public int alerteSMS(String message, String phone) throws Exception;
	
	public String genererOTP(String phone) throws Exception;
	
	
	public List<ClientProduit> resiliationsCBS() throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException;
	
	
	
	public void ignoirerEvenements(List<Transaction> transactions) throws JsonGenerationException, JsonMappingException, UnsupportedEncodingException, IOException,
	JSONException, DAOAPIException, URISyntaxException;
	
}
