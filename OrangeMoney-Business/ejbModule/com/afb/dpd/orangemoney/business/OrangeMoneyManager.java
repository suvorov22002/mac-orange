package com.afb.dpd.orangemoney.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.afb.dpd.mobilemoney.dao.IOrangeMoneyDAOAPILocal;
import com.afb.dpd.mobilemoney.dao.IOrangeMoneyDAOLocal;
import com.afb.dpd.orangemoney.jpa.dto.Account;
import com.afb.dpd.orangemoney.jpa.dto.Client;
import com.afb.dpd.orangemoney.jpa.dto.OperationDto;
import com.afb.dpd.orangemoney.jpa.dto.ResponseDataAccount;
import com.afb.dpd.orangemoney.jpa.dto.ResponseDataBkcom;
import com.afb.dpd.orangemoney.jpa.dto.ResponseDataBkeve;
import com.afb.dpd.orangemoney.jpa.dto.ResponseDataClient;
import com.afb.dpd.orangemoney.jpa.dto.ResponseDataHis;
import com.afb.dpd.orangemoney.jpa.dto.Shared;
import com.afb.dpd.orangemoney.jpa.entities.BranchMails;
import com.afb.dpd.orangemoney.jpa.entities.Commissions;
import com.afb.dpd.orangemoney.jpa.entities.Comptabilisation;
import com.afb.dpd.orangemoney.jpa.entities.FactMonth;
import com.afb.dpd.orangemoney.jpa.entities.FactMonthDetails;
import com.afb.dpd.orangemoney.jpa.entities.Parameters;
import com.afb.dpd.orangemoney.jpa.entities.ProfilMarchands;
import com.afb.dpd.orangemoney.jpa.entities.Rapprochement;
import com.afb.dpd.orangemoney.jpa.entities.RequestMessage;
import com.afb.dpd.orangemoney.jpa.entities.Subscriber;
import com.afb.dpd.orangemoney.jpa.entities.SuspensOrange;
import com.afb.dpd.orangemoney.jpa.entities.TraceRobot;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jpa.entities.TransactionDetail;
import com.afb.dpd.orangemoney.jpa.entities.USSDTransaction;
import com.afb.dpd.orangemoney.jpa.enums.ExceptionCategory;
import com.afb.dpd.orangemoney.jpa.enums.ExceptionCode;
import com.afb.dpd.orangemoney.jpa.enums.ModeFacturation;
import com.afb.dpd.orangemoney.jpa.enums.Periodicite;
import com.afb.dpd.orangemoney.jpa.enums.StatutContrat;
import com.afb.dpd.orangemoney.jpa.enums.TransactionStatus;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jpa.enums.TypeRapprochement;
import com.afb.dpd.orangemoney.jpa.enums.TypeValeurFrais;
import com.afb.dpd.orangemoney.jpa.exception.DAOAPIException;
import com.afb.dpd.orangemoney.jpa.exception.OgMoException;
import com.afb.dpd.orangemoney.jpa.tools.Bkcom;
import com.afb.dpd.orangemoney.jpa.tools.Bkhis;
import com.afb.dpd.orangemoney.jpa.tools.ClientProduit;
import com.afb.dpd.orangemoney.jpa.tools.CloseStream;
import com.afb.dpd.orangemoney.jpa.tools.Commentaire;
import com.afb.dpd.orangemoney.jpa.tools.ConnexionDB;
import com.afb.dpd.orangemoney.jpa.tools.DateComptable;
import com.afb.dpd.orangemoney.jpa.tools.DateUtil;
import com.afb.dpd.orangemoney.jpa.tools.Doublon;
import com.afb.dpd.orangemoney.jpa.tools.Entries;
import com.afb.dpd.orangemoney.jpa.tools.Equilibre;
import com.afb.dpd.orangemoney.jpa.tools.OgMoHelper;
import com.afb.dpd.orangemoney.jpa.tools.Queries;
import com.afb.dpd.orangemoney.jpa.tools.SendMail;
import com.afb.dpd.orangemoney.jpa.tools.TypeCompte;
import com.afb.dpd.orangemoney.jpa.tools.bkeve;
import com.afb.dpd.orangemoney.jpa.tools.bkmvti;
import com.afb.dpd.orangemoney.jpa.tools.bkmvtiPush;
import com.afb.portal.datamodel.entities.Parameter;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.yashiro.persistence.utils.annotations.AllowedRole;
import com.yashiro.persistence.utils.annotations.ProtectedClass;
import com.yashiro.persistence.utils.collections.converters.ConverterUtil;
import com.yashiro.persistence.utils.dao.tools.AliasesContainer;
import com.yashiro.persistence.utils.dao.tools.LoaderModeContainer;
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.PropertyContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;
import com.yashiro.persistence.utils.dao.tools.encrypter.Encrypter;

import afb.dsi.dpd.portal.business.facade.IFacadeManagerRemote;
import afb.dsi.dpd.portal.jpa.entities.Branch;
import afb.dsi.dpd.portal.jpa.entities.DataSystem;
import afb.dsi.dpd.portal.jpa.entities.SMSWeb;
import afb.dsi.dpd.portal.jpa.entities.User;
import afb.dsi.dpd.portal.jpa.tools.PortalHelper;
import bsh.StringUtil;
import entreeRela.jpa.entities.ProcessUser;
import entreeRela.jpa.enums.Etape;
import entreeRela.jpa.enums.TypeClient;

/**
 * Implementation du service metier de Gestion du Module Orange Money
 * @author Francis KONCHOU
 * @version 2.0
 */
@ProtectedClass(system = "Orange Money", methods = {} )
//@Interceptors(value = {AuditModuleInterceptor.class})
@Stateless(name = IOrangeMoneyManagerRemote.SERVICE_NAME, mappedName = IOrangeMoneyManagerRemote.SERVICE_NAME, description = "")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OrangeMoneyManager implements IOrangeMoneyManagerRemote {

	/**
	 * Default constructor. 
	 */
	public OrangeMoneyManager() {}

	/**
	 * Le Logger
	 */
	private static Log logger = LogFactory.getLog(OrangeMoneyManager.class);

	/**
	 * Injection de l'EJB Facade du Portail
	 */
	@EJB
	private IOrangeMoneyDAOLocal dao;

	@EJB
	private IOrangeMoneyDAOAPILocal  orangeMoneyAPI;


	private Connection conCBS = null;
	private Statement stmCBS;
	private DataSystem dsCBS = null;
	private DataSystem dsAIF = null;
	private DataSystem dsCbs = null;

	/**
	 * Nbre de fois qu'il faut tester (dans le Timer) la fermeture de l'agence centrale (Demarrage des TFJ)
	 */
	private int delaiCtrlTFJDelta = 1;

	/**
	 * Frequence d'execution du Timer (15min)
	 */
	private long nbMinutesCtrlFinTFJ = 900;

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#activerSubscriptions()
	 */
	@Override
	@AllowedRole(name = "activerSubscriptions", displayName = "OgMo.ActiverSubscriptions")
	public void activerSubscriptions() {

	}


	public <T> T findByPrimaryKey(Class<T> entityClass, Object entityID,PropertyContainer properties) {

		// Appel de la DAO
		return dao.findByPrimaryKey(entityClass, entityID, properties);

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#saveParameters(com.afb.dpd.mobilemoney.jpa.entities.Parameters)
	 */
	@Override
	@AllowedRole(name = "saveParameters", displayName = "OgMo.SaveParameters")
	public void saveParameters(Parameters param) {

		// Delete all Comission

		// TODO Auto-generated method stub
		/**List<Commissions> commissions = param.getCommissions();
		for(Commissions c : commissions){
			c = dao.update(c);
		}
		param.setCommissions(commissions);*/
		dao.update(param);
	}



	@Override
	@AllowedRole(name = "OgMo-saveBranchMails", displayName = "OgMo.Creer.BranchMails")
	public BranchMails saveBranchMails(BranchMails branchMails) {
		return dao.save(branchMails);
	}

	@Override
	@AllowedRole(name = "OgMo-updateBranchMails",displayName = "OgMo.Modifier.BranchMails")
	public BranchMails updateBranchMails(BranchMails branchMails) {
		return dao.update(branchMails);
	}

	@Override
	@AllowedRole(name = "OgMo-deleteBranchMails", displayName = "OgMo.Supprimer.BranchMails")
	public void deleteBranchMails(Long id) {
		dao.delete( dao.findByPrimaryKey(BranchMails.class, id, null) );
	}

	@Override
	public List<BranchMails> filterBranchMails(RestrictionsContainer rc) {
		return dao.filter(BranchMails.class, null, rc, OrderContainer.getInstance().add(Order.asc("libAgence")), null, 0, -1);
	}




	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.orangemoney.business.IOrangeMoneyManagerRemote#saveProfilMarchands(java.util.List)
	 */
	@Override
	@AllowedRole(name = "saveProfilMarchands", displayName = "OgMo.saveProfilMarchands")
	public void saveProfilMarchands(List<ProfilMarchands> profils){
		// TODO Auto-generated method stub
		for(ProfilMarchands prof : profils) dao.update(prof);
	}	


	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#profilSubscriptions()
	 */
	@Override
	@AllowedRole(name = "profilSubscriptions", displayName = "OgMo.ProfilSubscriptions")
	public void profilSubscriptions() {
		dao.filter(ProfilMarchands.class,null,null,null,null,0,-1);
	}



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.orangemoney.business.IOrangeMoneyManagerRemote#filterProfilMarchands(com.yashiro.persistence.utils.dao.tools.RestrictionsContainer, com.yashiro.persistence.utils.dao.tools.OrderContainer)
	 */
	@Override
	@AllowedRole(name = "filterProfilMarchands", displayName = "OgMo.FilterProfilMarchands")
	public List<ProfilMarchands> filterProfilMarchands() {
		// TODO Auto-generated method stub
		return dao.filter(ProfilMarchands.class, null, null, null, null, 0, -1);
	}



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#saveSubscriber(com.afb.dpd.mobilemoney.jpa.entities.Subscriber)
	 */
	@Override
	@AllowedRole(name = "saveSubscriber", displayName = "OgMo.CreateSubscriber")
	public Subscriber saveSubscriber(Subscriber subscriber) throws Exception, OgMoException {

		// Lecture de la date de soscription
		subscriber.setDate(new Date());

		// Date de derniére comptabilisation
		if(subscriber.getProfil() != null) subscriber.setDateDernCompta(new Date());

		// Log
		//logger.info("Generation du Code PIN du client");

		// Generation du PIN banque
		//subscriber.setBankPIN( generateBankPIN() );

		// Log
		//logger.info("Recherche des parametres");

		Parameters param = findParameters();

		// Log
		//logger.info("recherche des commissions sur la souscription");

		Map<TypeOperation, Commissions> mapComs = ConverterUtil.convertCollectionToMap(param.getCommissions(), "operation");

		// MAJ des infos de facturation

		//*** logger.info("********** subscriber.getCommissions() ********" + subscriber.getCommissions());
		//*** logger.info("**********  mapComs.get(TypeOperation.Account2Wallet).getValeur() ********" +  mapComs.get(TypeOperation.Account2Wallet).getValeur());
		subscriber.setCommissions( subscriber.getCommissions() + mapComs.get(TypeOperation.Account2Wallet).getValeur() );
		subscriber.setCommissions( subscriber.getCommissions() + mapComs.get(TypeOperation.Wallet2Account).getValeur() );
		subscriber.setPeriod( mapComs.get(TypeOperation.Wallet2Account).getPeriodFacturation() );
		subscriber.setFacturer( isClientEmploye(subscriber.getCustomerId()) ? Boolean.FALSE : Boolean.TRUE );


		// Lecture de la commission du type operation
		Commissions coms = mapComs.get(TypeOperation.SUBSCRIPTION);

		// Si des commissions ont ete parametres sur l'operation
		Transaction transaction = null;
		ProfilMarchands plm = subscriber.getProfil();
		if((coms != null && coms.getValeur() > 0) || plm != null ) {

			// Log
			logger.info("Il existe des commissions sur la souscription! On initialise la transaction a poster dans Delta");

			boolean finish = false;
			Iterator<String> els = subscriber.getAccounts().iterator();
			while(els.hasNext() && !finish ){
				String acc = els.next();

				// Creation d'une transaction
				transaction = new Transaction(TypeOperation.SUBSCRIPTION, subscriber, 0d, acc, subscriber.getPhoneNumbers().get(0)) ;

				// Log
				logger.info("Postage de l'evenement dans le CBS");

				// Postage de l'evenement dans le CBS
				transaction = posterEvenementDansCoreBanking(transaction);
				if(transaction != null) finish = true;
			}

			if(transaction == null){
				return null;			
			}

		} else {

			// Creation de la souscription
			subscriber.setAccFact(subscriber.getFirstAccount());
			subscriber = dao.save(subscriber);

			// Log
			logger.info("Enregistrement du souscripteur");

		}

		// Log
		//logger.info("Envoi du code PIN au client par SMS");

		// Envoi du code PIN Banque par SMS
		// sendCodePINBySMS(subscriber);

		coms = null; param = null;

		return subscriber;

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#updateSubscriber(com.afb.dpd.mobilemoney.jpa.entities.Subscriber)
	 */
	@Override
	@AllowedRole(name = "updateSubscriber", displayName = "OgMo.UpdateSubscriber")
	public Subscriber updateSubscriber(Subscriber subscriber) throws Exception, OgMoException {

		// TODO Auto-generated method stub
		//return mobileMoneyDAO.update(subscriber);

		Parameters param = findParameters();

		// Lecture de la commission du type operation
		Commissions coms = ConverterUtil.convertCollectionToMap(param.getCommissions(), "operation").get(TypeOperation.MODIFY);

		// Si des commissions ont ete parametres sur l'operation
		if(coms != null && coms.getValeur() > 0) {

			// Creation d'une transaction
			Transaction transaction = new Transaction(TypeOperation.MODIFY, subscriber, 0d, subscriber.getAccounts().get(0), subscriber.getPhoneNumbers().get(0)) ;

			// Postage de l'evenement dans le CBS
			posterEvenementDansCoreBanking(transaction);

		}		

		return dao.update(subscriber);

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#deleteSubscriber(java.lang.Long)
	 */
	@Override
	@AllowedRole(name = "deleteSubscriber", displayName = "OgMo.DeleteSubscriber")
	public void deleteSubscriber(Long subscriberId) {
		// TODO Auto-generated method stub
		dao.delete( dao.findByPrimaryKey(Subscriber.class, subscriberId, null) );
	}




	@SuppressWarnings("static-access")
	public void sendMail(List<String> mailsTo, String subject, String messageCorps, Parameters parametre) throws Exception {

		SendMail sendMail = new SendMail();
		sendMail.sendMail(null, null, mailsTo, subject, messageCorps, parametre);
	}


	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#filterSubscriptions(com.yashiro.persistence.utils.dao.tools.RestrictionsContainer, com.yashiro.persistence.utils.dao.tools.OrderContainer)
	 */
	@Override
	@AllowedRole(name = "filterSubscriptions", displayName = "OgMo.FilterSubscriptions")
	public List<Subscriber> filterSubscriptions(RestrictionsContainer rc, OrderContainer orders) {
		// TODO Auto-generated method stub
		return dao.filter(Subscriber.class, null, rc, orders, null, 0, -1);
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#annulerSouscription(java.lang.Long)
	 */
	@Override
	@AllowedRole(name = "annulerSouscription", displayName = "OgMo.AnnulerSubscriptions")
	public void annulerSouscription(Long sousId,User user) throws Exception, OgMoException {

		// Recuperation du souscripteur
		Subscriber subscriber = dao.findByPrimaryKey(Subscriber.class, sousId, null);

		if(subscriber.getStatus().equals(StatutContrat.SUSPENDU)){
			// MAJ du statut de la souscription
			subscriber.setStatus(StatutContrat.ACTIF);
		}else{
			// MAJ du statut de la souscription
			subscriber.setStatus(StatutContrat.SUSPENDU);
		}

		// Recherche des parametres
		Parameters param = findParameters();

		// Lecture de la commission du type operation
		Commissions coms = ConverterUtil.convertCollectionToMap(param.getCommissions(), "operation").get(TypeOperation.CANCEL);

		// Si des commissions ont ete parametres sur l'operation
		if(coms != null && coms.getValeur() > 0) {

			// Creation d'une transaction
			Transaction transaction = new Transaction(TypeOperation.SUBSCRIPTION, subscriber, 0d, subscriber.getAccounts().get(0), subscriber.getPhoneNumbers().get(0)) ;

			// Postage de l'evenement dans le CBS
			posterEvenementDansCoreBanking(transaction);

		}

		subscriber.setUtiMod(user.getLogin());
		subscriber.setNameUtiMod(user.getName());
		subscriber.setDateMod(new Date());
		dao.update(subscriber);
		//dao.getEntityManager().createQuery("Update Subscriber s set s.status = :status where s.id = :id").setParameter("status", StatutContrat.SUSPENDU).setParameter("id", sousId).executeUpdate();
		param = null;

	}
	
	
	
	@Override
	public void updateTransReconcliation(Long id, String reverseTrans, boolean completed, TransactionStatus status, String traceStatus) throws Exception , OgMoException {   
		
		try {
			if(status != null) {
				dao.getEntityManager().createQuery("update Transaction t set t.reverseTrans= :reverseTrans, t.completed= :completed, t.status= :status, t.traceStatus= :traceStatus where t.id= :id").setParameter("reverseTrans", reverseTrans).setParameter("completed", completed).setParameter("status", status).setParameter("traceStatus", traceStatus).setParameter("id", id).executeUpdate();
			}
			else {
				dao.getEntityManager().createQuery("update Transaction t set t.reverseTrans= :reverseTrans, t.completed= :completed, t.traceStatus= :traceStatus where t.id= :id").setParameter("reverseTrans", reverseTrans).setParameter("completed", completed).setParameter("traceStatus", traceStatus).setParameter("id", id).executeUpdate();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#deleteSouscription(java.lang.Long)
	 */
	@Override
	@AllowedRole(name = "deleteSouscription", displayName = "OgMo.SupprimerSubscriptions")
	public void deleteSouscription(Long sousId) {
		dao.delete( dao.findByPrimaryKey(Subscriber.class, sousId, null) );
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#subscriptionAlreadyExist(java.lang.String)
	 */
	public boolean subscriptionAlreadyExist(String customerId) {

		// Recherche des souscriptions du client 
		List<Subscriber> liste = dao.filter(Subscriber.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("customerId", customerId)).add(Restrictions.eq("status", StatutContrat.ACTIF)), null, null, 0, -1);

		// Calcul du resultat a retourner
		boolean resultat = liste != null && !liste.isEmpty();

		// Libere la variable
		liste.clear(); liste = null;

		// Retourne le resultat
		return resultat;
	}


	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#findSubscriberFromPhoneNumber(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Subscriber findSubscriberFromPhoneNumber(String phoneNumber) {

		// Recherche du souscripteur possedant le numero de Tel du message 
		List<Subscriber> subs = dao.getEntityManager().createQuery("From Subscriber s left join fetch s.phoneNumbers phones where phones in ('"+ phoneNumber +"') and s.status=:status  ").setParameter("status", StatutContrat.ACTIF).getResultList(); // mobileMoneyDAO.filter(Subscriber.class, AliasesContainer.getInstance().add("phoneNumbers", "phones"), RestrictionsContainer.getInstance().add(Restrictions.in("phones", new Object[]{phoneNumber}  )), null, null, 0, -1);
		
		if( subs != null && !subs.isEmpty() ) {
			return isCompteFerme(subs.get(0).getAccounts().get(0)) ? null : subs.get(0);
		}
		else return null;

		// Retourne le souscripteur trouve
		// return subs != null && !subs.isEmpty() && !isCompteFerme(subs.get(0).getAccounts().get(0)) ? subs.get(0) : null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#findSubscriberFromPhoneNumber(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Subscriber findSubscriberFromPhoneNumber(String phoneNumber, Long subsID) {

		// Recherche du souscripteur possedant le numero de Tel du message 
		List<Subscriber> subs = dao.getEntityManager().createQuery("From Subscriber s left join fetch s.phoneNumbers phones where s.id <> "+ subsID +" and phones in ('"+ phoneNumber +"') and s.status=:status ").setParameter("status", StatutContrat.ACTIF).getResultList();

		// Retourne le souscripteur trouve
		return subs != null && !subs.isEmpty() ? subs.get(0) : null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Subscriber findSubscriber(String  CustomerId , String subkey){

		// Recherche du souscripteur possedant le numero de Tel du message  status = StatutContrat.ACTIF;
		Query querry = dao.getEntityManager().createQuery("select s From Subscriber s where s.customerId=:customerId and subkey=:subkey and status=:status ");
		querry.setParameter("customerId", CustomerId);
		querry.setParameter("subkey", subkey);
		querry.setParameter("status", StatutContrat.ACTIF);
		List<Subscriber> subs = querry.getResultList();

		if(subs != null && !subs.isEmpty()){
			return subs.get(0);
		}
		// Retourne le souscripteur trouveer
		return null;
	}


	public Subscriber findSubscriberbankPINBankPIN(String  value){

		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("bankPIN",value));

		List<Subscriber> subs = dao.filter(Subscriber.class, null, rc, null, null, 0, -1);

		// Recherche du souscripteur possedant le numero de Tel du message 
		/* ***
		Query querry = dao.getEntityManager().createQuery("select s From Subscriber s where s.bankPIN=:bankPIN ");
		querry.setParameter("bankPIN", value);
		List<Subscriber> subs = querry.getResultList();
		 */
		if(subs != null && !subs.isEmpty()){
			return subs.get(0);
		}
		// Retourne le souscripteur trouve
		return null;
	}




	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#generateTransactionFromMessage(com.afb.dpd.mobilemoney.jpa.entities.RequestMessage)
	 */
	@Override
	public Transaction generateTransactionFromMessage(RequestMessage message) {

		// Recherche du souscripteur possedant le numero de Tel du message 
		Subscriber sub = findSubscriberFromPhoneNumber( message.getPhoneNumber() );

		// Retourne la Transaction
		return sub != null ? new Transaction(message.getOperation(), sub, message.getAmount(), (message.getAccount() != null ? message.getAccount() : sub.getAccounts().get(0) ), message.getPhoneNumber()) : null;

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#saveTransaction(com.afb.dpd.mobilemoney.jpa.entities.Transaction)
	 */
	@Override
	public Transaction saveTransaction(Transaction transaction) {
		// TODO Auto-generated method stub
		return dao.save(transaction);
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#updateTransaction(com.afb.dpd.mobilemoney.jpa.entities.Transaction)
	 */
	@Override
	public Transaction updateTransaction(Transaction transaction) {
		// TODO Auto-generated method stub
		return dao.update(transaction);
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#filterTransactions(com.yashiro.persistence.utils.dao.tools.RestrictionsContainer, com.yashiro.persistence.utils.dao.tools.OrderContainer)
	 */
	@Override
	@AllowedRole(name = "filterTransactions", displayName = "OgMo.FilterTransactions")
	public List<Transaction> filterTransactions(RestrictionsContainer rc, OrderContainer orders) {
		// TODO Auto-generated method stub
		AliasesContainer all = AliasesContainer.getInstance();
		all.add("subscriber","subscriber");
		return dao.filter(Transaction.class, all, rc, orders, null, 0, -1);
	}


	@Override
	@AllowedRole(name = "completeTransactions", displayName = "OgMo.completer.Transactions")
	public void completeTransactions() {

	}

	@Override
	@AllowedRole(name = "suiviRobot", displayName = "OgMo.Suivi.Robot")
	public void suiviRobot() {

	}



	@SuppressWarnings("unchecked")
	public List<Transaction> filterTransactionProcessing() {
		// TODO Auto-generated method stub
	
		try {
			Query querry = dao.getEntityManager().createQuery("select t from Transaction t where datetime<= :datetime and completed= :completed and posted= :posted and status= :status and (typeOperation= :typeOperation1 or typeOperation= :typeOperation2) order by datetime asc");
			querry.setParameter("datetime",DateUtils.addMinutes(new Date(),-15));
			querry.setParameter("completed",Boolean.FALSE);
			querry.setParameter("posted",Boolean.FALSE);
			querry.setParameter("status",TransactionStatus.SUCCESS);
			querry.setParameter("typeOperation1",TypeOperation.Account2Wallet);
			querry.setParameter("typeOperation2",TypeOperation.Wallet2Account);
			return querry.getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return new ArrayList<Transaction>();

	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> filterTransactions(Date deb, Date fin, TypeOperation selectedTypOp, TransactionStatus selectedTypStatus, String nom, String tel, String ncp) {
		// TODO Auto-generated method stub 
	
		try {
			String sql = "select t from Transaction t where id is not null ";
			if(deb != null && fin != null) {
				sql = sql + " and datetime between :deb and :fin";
			}
			if(selectedTypOp != null) {
				sql = sql + " and typeOperation= :typeOperation";
			}
			if(selectedTypStatus != null) {
				sql = sql + " and status= : status";
			}
			if(StringUtils.isNotBlank(nom)) {
				sql = sql + " and subscriber.customerName like :nom";
			}
			if(StringUtils.isNotBlank(tel)) {
				sql = sql + " and phoneNumber like :tel";
			}
			if(StringUtils.isNotBlank(ncp)) {
				sql = sql + " and account like :ncp";
			}
			sql = sql + " order by datetime desc";
			
			Query querry = dao.getEntityManager().createQuery(sql);
			
			
			if(deb != null && fin != null) {
				querry.setParameter("deb",deb);
				querry.setParameter("fin",fin);
			}
			if(selectedTypOp != null) {
				querry.setParameter("typeOperation",selectedTypOp);
			}
			if(selectedTypStatus != null) {
				querry.setParameter("status",selectedTypStatus);
			}
			if(StringUtils.isNotBlank(nom)) {
				querry.setParameter("nom",nom);
			}
			if(StringUtils.isNotBlank(tel)) {
				querry.setParameter("tel",tel);
			}
			if(StringUtils.isNotBlank(ncp)) {
				querry.setParameter("ncp",ncp);
			}
			
			return querry.getResultList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return new ArrayList<Transaction>();

	}

	
	/*

	Date deb = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(txtDateDeb) + " " + (StringUtils.isNotBlank(txtHeureDebut) ? txtHeureDebut : "00") + ":00:00");
			Date fin = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(txtDateFin) + " " + (StringUtils.isNotBlank(txtHeureFin) ? txtHeureFin : "23") + ":59:59");
			
			// Initialisation d'un conteneur de restrictions
			RestrictionsContainer rc = RestrictionsContainer.getInstance().add(Restrictions.between("datetime", deb, fin));

			// Ajout des restrictions au conteneur
			if(selectedTypOp != null ) rc.add(Restrictions.eq("typeOperation", selectedTypOp));
			if(selectedTypStatus != null ) rc.add(Restrictions.eq("status", selectedTypStatus));
			if(nom != null  && !nom.trim().isEmpty()) rc.add(Restrictions.ilike("subscriber.customerName","%"+nom+"%",MatchMode.ANYWHERE));
			if(tel != null  && !tel.trim().isEmpty()) rc.add(Restrictions.ilike("phoneNumber","%"+tel+"%",MatchMode.ANYWHERE));
			if(ncp != null  && !ncp.trim().isEmpty()) rc.add(Restrictions.ilike("account","%"+ncp+"%",MatchMode.ANYWHERE));
			//*** rc.add(Restrictions.le("datetime",DateUtils.addMinutes(new Date(),-2)));
			OrderContainer orders = OrderContainer.getInstance().add(Order.desc("datetime"));

	*/

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#purgeTransactions(java.util.List)
	 *
	@Override
	@AllowedRole(name = "purgeTransactions", displayName = "OgMo.PurgeTransactions")
	public void purgeTransactions(List<Transaction> transactions) {
		mobileMoneyDAO.delete(transactions);
	}*/

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#generateAccountingEntries(java.util.List)
	 */
	@Override
	@AllowedRole(name = "generateAccountingEntries", displayName = "OgMo.GenerateAccountingEntries")
	public void generateAccountingEntries(List<Transaction> transactions) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#initialisations()
	 */
	@Override
	public void initialisations() {

		// Recherche des parametres par defaut
		Parameters params = dao.findByPrimaryKey(Parameters.class, Encrypter.getInstance().hashText(Parameters.CODE_PARAM), null);

		// Si les parametres n'existent pas
		if(params == null) dao.save( new Parameters() );
		else{
			params.setLancementRobot("ON");
			params.setExecutionRobot("ON");

			params.setLancementAlerte("ON");
			params.setExecutionAlerte("ON");

			dao.update(params);
		}

		/*
		params.setPeriodeLancementRobot(5);
		dao.update(params);
		 */

	}



	@Override
	public List<TraceRobot> filterTraceRobots(RestrictionsContainer rc, OrderContainer orders) {
		// TODO Auto-generated method stub
		return dao.filter(TraceRobot.class, null, rc, orders, null, 0, -1);
	}



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#findParameters()
	 */
	@Override
	public Parameters findParameters() {
		// TODO Auto-generated method stub
		return dao.findByPrimaryKey(Parameters.class, Encrypter.getInstance().hashText(Parameters.CODE_PARAM), null);
	}

	@Override
	@AllowedRole(name = "consulterConfiguration", displayName = "OgMo.Consulter.Configuration")
	public Parameters consulterConfiguration() {
		return findParameters();
	}

	private void findCBSDataSystem() throws NamingException {

		// Demarrage du service Facade du portail
		IFacadeManagerRemote portalFacadeManager = (IFacadeManagerRemote) new InitialContext().lookup( PortalHelper.APPLICATION_EAR.concat("/").concat( IFacadeManagerRemote.SERVICE_NAME ).concat("/remote") );

		// Recuperation de la DS de cnx au CBS
		dsCBS = (DataSystem) portalFacadeManager.findByProperty(DataSystem.class, "code", "DELTA-V10");
	}
	
	private void findCBSServicesDataSystem() {

		try {

			// Demarrage du service Facade du portail
			IFacadeManagerRemote portalFacadeManager = (IFacadeManagerRemote) new InitialContext().lookup( PortalHelper.APPLICATION_EAR.concat("/").concat( IFacadeManagerRemote.SERVICE_NAME ).concat("/remote") );

			// Recuperation de la DS de cnx au CBS
			dsCbs = (DataSystem) portalFacadeManager.findByProperty(DataSystem.class, "code", "AFB-SERVICE-CBS");

		}catch(Exception e){}
	}
	
	private void findAIFDataSystem() {

		try {

			// Demarrage du service Facade du portail
			IFacadeManagerRemote portalFacadeManager = (IFacadeManagerRemote) new InitialContext().lookup( PortalHelper.APPLICATION_EAR.concat("/").concat( IFacadeManagerRemote.SERVICE_NAME ).concat("/remote") );

			// Recuperation de la DS de cnx au CBS
			dsAIF = (DataSystem) portalFacadeManager.findByProperty(DataSystem.class, "code", "AIF");

		}catch(Exception e){}
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#filterTypeCompteFromAmplitude()
	 */
	@Override
	public List<TypeCompte> filterTypeCompteFromAmplitude() {

		// Initialisation de la collection a retourner
		List<TypeCompte> types = new ArrayList<TypeCompte>();

		try{

			// Initialisation de DataStore d'Amplitude
			if(dsCBS == null) findCBSDataSystem();

			// Recherche de la liste des types de comptes dans le CBS
			ResultSet rs = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(0).getQuery(), null);

			// Parcours du resultat
			while(rs != null && rs.next()){

				// Ajout de l'element trouve a la collection
				types.add( new TypeCompte(rs.getString("code").trim(), rs.getString("nom").trim()) );
			}
			
			// Fermeture des connexions
			CloseStream.fermeturesStream(rs);

			////if(conCBS != null ) conCBS.close();

		} catch(Exception e){e.printStackTrace();}


		// Retourne les types de compte
		return types;
	}

	/*
	 * 
	 */
	@Override
	public List<String> filterCustomerAccountsFromCBS(String customerId) {

		// Initialisation de la liste de comptes a retourner
		List<String> accounts = new ArrayList<String>();

		// Recherche des parametres
		Parameters param = findParameters();

		String in = "(";
		for(String typ : param.getAccountTypes()) in += "'"+ typ +"', ";
		if(in.length() > 1) in = in.substring(0, in.length()-2) + ")";


		try {

			// Initialisation de DataStore d'Amplitude
			if(dsCBS == null) findCBSDataSystem();

			// Recherche de la liste des types de comptes dans le CBS
			ResultSet rs = executeFilterSystemQuery(dsCBS, "select age, ncp, clc from bkcom where cli='"+ customerId +"' "+ (in.length() > 1 ? " and cpro in "+in+" " : "") , null);

			// S'il existe au moins un resultat
			while(rs != null && rs.next()) {
				accounts.add( rs.getString("age") + "-" + rs.getString("ncp") + "-" + rs.getString("clc") );
			}

			// Fermeture des connexions
			CloseStream.fermeturesStream(rs);

			//if(conCBS != null ) conCBS.close();

			// Suppression des variables
			param = null; 

		} catch(Exception e){}

		// Retourne la liste de comptes
		return accounts;

	}





	/*
	 * 
	 */
	@Override
	public boolean verifierCompteDormant(String ncp) {

		boolean result = Boolean.TRUE;

		if(StringUtils.isBlank(ncp)) return result;

		// Recherche des parametres
		Parameters params = findParameters();

		String in = "";
		if(params != null && StringUtils.isNotBlank(params.getTypeOpesDormant())) {
			for(String s : params.getTypeOpesDormant().split("-")) in += "'" + s + "'" + ", ";
		}
		if(!in.isEmpty()) in = "(".concat( in.substring(0, in.length()-2) ).concat(")");

		try {

			// Initialisation de DataStore d'Amplitude
			if(dsCBS == null) findCBSDataSystem();

			Date debut = DateUtils.addMonths(new Date(), -(StringUtils.isNotBlank(params.getDelaiDormant()) ? Integer.parseInt(params.getDelaiDormant()) : 5));
			Date fin = new Date();

			// Recherche de la liste des types de comptes dans le CBS
			//String sql = "select ncp from bkhis where ncp = ? and ope in LISTOPES and dco between ? and ? ";
			//ResultSet rs = executeFilterSystemQuery(dsCBS, sql.replace("LISTOPES", in), new Object[]{ncp.split("-")[1],debut,fin});
			
			OperationDto opeDto = new OperationDto();
			opeDto.setAgences(ncp.split("-")[0]);
			opeDto.setComptes(ncp.split("-")[1]);
			opeDto.setDebut(DateUtil.format(debut , DateUtil.DATE_MINUS_FORMAT_SINGLE));
			opeDto.setFin(DateUtil.format(fin , DateUtil.DATE_MINUS_FORMAT_SINGLE));
			opeDto.setTypeOperations(in);
			
			String playload = null;
			
			playload = Shared.mapToJsonString(opeDto);
			if(!Shared.isJSONValid(playload)) {
				playload =  Shared.mapToJsonString(new OperationDto());
			}
			
			if (dsCbs == null) findCBSServicesDataSystem();
			if (dsCbs != null && StringUtils.isNotBlank(dsCbs.getDbConnectionString())) {
				
				HttpPost post = new HttpPost(dsCbs.getDbConnectionString()+"/transactions/process/gethistorydetail");
				post.setHeader("content-type", "application/json");
				post.setEntity(new StringEntity(playload , Consts.UTF_8));
				
			    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(post);
			    HttpEntity entity = null;
			    entity = response.getEntity();
			    
			    if(entity != null) {
			    	
			    	 List<Bkhis> listHist = new ArrayList<>();
			    	 String content = EntityUtils.toString(entity);
					 JSONObject json = new JSONObject(content);
					
					 ResponseDataHis responseDataHis = Shared.mapToResponseDataHis(json);
					 String responseCode = responseDataHis.getCode();
						 
					  if ("200".equals(responseCode)) {
						
						  listHist = responseDataHis.getData();
						  if(!listHist.isEmpty()) {
							  logger.info("ACTIVITY OK!!! ");
							  result = Boolean.FALSE;
						  }
						  
					  }
			    } 
			}

		} catch(Exception e){}

		// Suppression des variables
		params = null; 

		// Retourne 
		return result;

	}



	/*
	 * 
	 */
	@Override
	public boolean checkTransactionDay(String ncp) {

		boolean result = Boolean.FALSE;

		if(StringUtils.isBlank(ncp)) return result;

		// Recherche des parametres
		Parameters params = findParameters();

		String in = "";
		if(params != null && StringUtils.isNotBlank(params.getTypeOpesDormant())) {
			for(String s : params.getTypeOpesDormant().split("-")) in += "'" + s + "'" + ", ";
		}
		if(!in.isEmpty()) in = "(".concat( in.substring(0, in.length()-2) ).concat(")");

		try {
			//ResultSet rs = executeFilterSystemQuery(dsCBS, sql.replace("LISTOPES", in), new Object[]{account.split("-")[1],account.split("-")[1]});
			
			logger.info("CHECKING NEW TRX!!! " + in);
			
			OperationDto opeDto = new OperationDto();
			opeDto.setAgences(ncp.split("-")[0]);
			opeDto.setComptes(ncp.split("-")[1]);
			opeDto.setTypeOperations(in);
		
			String playload = null;
			
			playload = Shared.mapToJsonString(opeDto);
			if(!Shared.isJSONValid(playload)) {
				playload =  Shared.mapToJsonString(new OperationDto());
			}
			
			if (dsCbs == null) findCBSServicesDataSystem();
			if (dsCbs != null && StringUtils.isNotBlank(dsCbs.getDbConnectionString())) {
				
				HttpPost post = new HttpPost(dsCbs.getDbConnectionString()+"/transactions/process/getevesdetail");
				post.setHeader("content-type", "application/json");
				post.setEntity(new StringEntity(playload , Consts.UTF_8));
				
			    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(post);
			    HttpEntity entity = null;
			    entity = response.getEntity();
			    
			    if(entity != null) {
			    	
			    	 List<bkeve> listBkeve = new ArrayList<>();
			    	 String content = EntityUtils.toString(entity);
					 JSONObject json = new JSONObject(content);
					
					 ResponseDataBkeve responseDataBkeve = Shared.mapToResponseDataBkeve(json);
					 String responseCode = responseDataBkeve.getCode();
						 
					  if ("200".equals(responseCode)) {
						 
						  listBkeve = responseDataBkeve.getData();
						  if(!listBkeve.isEmpty()) {
							  logger.info("New Transaction jour OK!!! ");
							  result = Boolean.TRUE;
						  }
						  
					  }
			    } 
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}

		// Suppression des variables
		params = null; 
		// Retourne 
		return result;

	}

	/*
	 * 
	 */
	@Override
	public boolean checkNewAccount(String account) {

		boolean result = Boolean.FALSE;

		if(StringUtils.isBlank(account)) return result;

		// Recherche des parametres
		Parameters params = findParameters();

		try {

			// Initialisation de DataStore d'Amplitude
			//if(dsCBS == null) findCBSDataSystem();

			Date dateJour = DateUtils.addDays(new Date(), -(StringUtils.isNotBlank(params.getDelaiNewAccount()) ? Integer.parseInt(params.getDelaiNewAccount()) : 10));

			// Verification de la date d'ouverture du compte dans le CBS
			//String sql = "select ncp from bkcom where ncp = ? and dou >= ? ";
			//ResultSet rs = executeFilterSystemQuery(dsCBS, sql, new Object[]{account.split("-")[1],dateJour});
			
			if(dsAIF == null) findAIFDataSystem();
			if(dsAIF != null && StringUtils.isNotBlank(dsAIF.getDbConnectionString())) {
				
				String numCompte = account.split("-")[1];
				
				HttpGet getRequest = new HttpGet(dsAIF.getDbConnectionString()+"/account/getlistecompte/"+numCompte.substring(0, 7));
			    getRequest.setHeader("content-type", "application/json");
			    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(getRequest);
			    HttpEntity entity = null;
			    entity = response.getEntity();
			 
			    if(entity != null) {
			    	 
			    	 List<Account> listComptes = new ArrayList<>();
			    	 String content = EntityUtils.toString(entity);
					 JSONObject json = new JSONObject(content);
					
					 ResponseDataAccount responseDataAcc = Shared.mapToResponseDataAccount(json);
					 String responseCode = responseDataAcc.getCode();
					
					 
					 if ("200".equals(responseCode)) {
						 
						 listComptes = responseDataAcc.getDatas();
						 for(Account acct : listComptes) {
							 
							 if (numCompte.equalsIgnoreCase(acct.getNcp()) && (acct.getDou().getTime() >= dateJour.getTime())) {
								 result = Boolean.TRUE;
							 }
						 }	 
					 } 
			     }
			}

		} catch(Exception e){}

		// Suppression des variables
		params = null; 

		// Retourne 
		return result;

	}





	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#findCustomerFromAmplitude(java.lang.String)
	 */
	@Override
	public Subscriber findCustomerFromAmplitude(String customerId) {

		// Initialisation
		////System.out.println("-----findCustomerFromAmplitude----"+customerId);
		Subscriber subscriber = null;
		//String in = "";
		List<String> accountTypes = new ArrayList<>();

		// Recherche des parametres generaux
		Parameters params = findParameters();

		// Si les types de cptes autorises ont ete parametres
		if( params.getAccountTypes() != null && !params.getAccountTypes().isEmpty()) {
			accountTypes = params.getAccountTypes();
			//for(String s : params.getAccountTypes()) in += "'" + s + "'" + ", ";

		}

		//if(!in.isEmpty()) in = "(".concat( in.substring(0, in.length()-2) ).concat(")");


		try {

			// Initialisation de DataStore d'Amplitude
			if(dsCBS == null) findCBSDataSystem();

			// Recherche de la liste des types de comptes dans le CBS
			// //System.out.println("--SQL---   : "+"select distinct bkcom.cha,bkcom.age, bkcom.ncp, bkcom.clc, bkcli.nom, nvl(bkcli.pre, ' ') as pre, bkadcli.adr1, bkadcli.ville from bkcom, bkcli LEFT JOIN bkadcli ON bkcli.cli = bkadcli.cli where bkcom.cli = bkcli.cli and bkadcli.cli = bkcli.cli and bkcom.cfe='N' and bkcom.ife='N' and bkcom.dev='001' and bkcom.cli='"+ customerId +"'" + (in.isEmpty() ? "" : " and bkcom.cpro in " + in));
			//ResultSet rs = executeFilterSystemQuery(dsCBS, "select distinct bkcom.cha,bkcom.age, bkcom.ncp, bkcom.clc, bkcli.nom, nvl(bkcli.pre, ' ') as pre, bkadcli.adr1, bkadcli.ville from bkcom, bkcli LEFT JOIN bkadcli ON bkcli.cli = bkadcli.cli where bkcom.cli = bkcli.cli and bkadcli.cli = bkcli.cli and bkcom.cfe='N' and bkcom.ife='N' and bkcom.dev='001' and bkcom.cli='"+ customerId +"'" + (in.isEmpty() ? "" : " and bkcom.cpro in " + in), null);
			
			if(dsAIF == null) findAIFDataSystem();
			
			if(dsAIF != null && StringUtils.isNotBlank(dsAIF.getDbConnectionString())) {
				
				List<Client> listClient = new ArrayList<>();
				HttpGet getRequest = new HttpGet(dsAIF.getDbConnectionString()+"/customer/customerdetails/"+customerId);
			    getRequest.setHeader("content-type", "application/json");
			    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(getRequest);
			    HttpEntity entity = null;
			    entity = response.getEntity();
			   
			    if(entity != null) {
			    	String content = EntityUtils.toString(entity);
					JSONObject json = new JSONObject(content);
					
					 ResponseDataClient responseData = Shared.mapToResponseDataClient(json);
					 String responseCode = responseData.getCode();
					 
					 if ("200".equals(responseCode)) {
						 
						 listClient = responseData.getDatas();
						 for(Client client : listClient) {
							 
							 subscriber = new Subscriber(client.getMatricule(), client.getCustomerName(), client.getAdresse1() + " " + client.getVille());
							 subscriber.setPid(client.getNumeroPiece());
							 
							 if (StringUtils.isNotBlank(client.getNumTelephone1())) {
								 subscriber.getPhoneNumbers().add(client.getNumTelephone1());
							 }
							 if (StringUtils.isNotBlank(client.getNumTelephone2())) {
								 subscriber.getPhoneNumbers().add(client.getNumTelephone2());
							 }
							 
	    				 }
						 
						 getRequest = new HttpGet(dsAIF.getDbConnectionString()+"/account/getlistecompte/"+customerId);
    				     getRequest.setHeader("content-type", "application/json");
    				     response = Shared.getClosableHttpClient().execute(getRequest);
    				     entity = null;
    				     entity = response.getEntity();
    				    
    				     if(entity != null) {
    				    	 
    				    	 List<Account> listComptes = new ArrayList<>();
    				    	 content = EntityUtils.toString(entity);
							 json = new JSONObject(content);
							
							 ResponseDataAccount responseDataAcc = Shared.mapToResponseDataAccount(json);
							 responseCode = responseDataAcc.getCode();
							 
							 if ("200".equals(responseCode)) {
								 
								 listComptes = responseDataAcc.getDatas();
								 
								 for(Account account : listComptes) {

    								 if(!account.isCfe() && !account.isIfe() && accountTypes.contains(account.getCodeProduit())) {
    									 if(CheckChapiter(account.getChapitre().trim()).equals(Boolean.TRUE)){
    										 subscriber.getAccounts().add( account.getAgence().concat("-").concat(account.getNcp()).concat("-").concat(account.getCle()) );
    									 }
    								 }
    								 
    							 }	 
							 }
							 else {
								 return null;
							 }	 
    				     } 
					 }
					 else {
						 return subscriber;
					 } 
			    }
			    else {
			    	return null;
			    }
			   
			}
			
			params = null;
			//if(conCBS != null ) conCBS.close();

		} catch(Exception e){}

		// Retourne le client trouve
		return subscriber;
	}

	public Transaction findTransactionBySubscriber(Long subsId) {

		List<Transaction> trans = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("subscriber.id", subsId)), null, null, 0, -1);

		return trans != null && !trans.isEmpty() ? trans.get(0) : null;
	}


	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#posterEvenementDansAmplitude(com.afb.dpd.mobilemoney.jpa.entities.Transaction)
	 */
	public void posterEvenementDansAmplitude(Transaction transaction) throws Exception {

		// S'il n'ya aucune transaction on sort
		if(transaction == null) return;

		// Lecture des parametres generaux
		Parameters params = findParameters();

		// Log
		logger.info("");

		// Tests
		if(params == null) throw new Exception("Parametres inexistants");
		if(params.getNcpDAPWaletAcount() == null || params.getNcpDAPAcountWalet() == null) throw new Exception("Comptes de liaisons pour operations de Push & Pull non parametres !!!");

		// Initialisation du code de l'evenement a generer
		String codeOpe = params.getCodeOperation();  //.getNumCompteFloat().split("-")[0].equals(transaction.getAccount().split("-")[0]) ? OgMoHelper.CODE_OPE_VIREMENT_SIMPLE : OgMoHelper.CODE_OPE_VIREMENT_INTERAG;

		// Log
		logger.info("Demarrage du service Facade du portail OK!");

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();

		// Log
		logger.info("Lecture de la DS du CBS OK!");

		// Recuperation du dernier numero evenement du type operation
		ResultSet rs = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(2).getQuery(), new Object[]{codeOpe});

		// Log
		logger.info("Lecture du dernier numero d'evenement genere OK!");

		// Calcul du numero d'evenement
		Long numEve = rs != null && rs.next() ? numEve = rs.getLong("num") + 1 : 0l;

		// Log
		logger.info("Calcul du prochain numero d'evenement OK!");

		// Fermeture des connexions
		CloseStream.fermeturesStream(rs);

		// Recuperation du cpte DAP a utiliser
		String ncpDAP = transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? params.getNcpDAPAcountWalet(): params.getNcpDAPWaletAcount();

		// Recuperation du compte Float MTN
		ResultSet rsCpteOrange = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(6).getQuery(), new Object[]{ ncpDAP.split("-")[0], ncpDAP.split("-")[1], ncpDAP.split("-")[2] });

		// Log
		logger.info("Recuperation des infos sur le cpte de MTN OK!");

		ResultSet rsCpteAbonne = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(6).getQuery(), new Object[]{ transaction.getAccount().split("-")[0], transaction.getAccount().split("-")[1], transaction.getAccount().split("-")[2] });

		// Log
		logger.info("Recuperation des infos sur le compte de l'abonne OK!");

		// Initialisation de l'evenement a poster dans Amplitude
		bkeve eve = new bkeve();

		// Initialisation de l'evenement de la commission
		//bkevec evec = new bkevec(codeOpe, OgMoHelper.padText(String.valueOf(numEve), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? params.getCommissionsPull() : params.getCommissionsPush() , "O");

		// Si on a trouve les 2 comptes a mouvementer
		if(rsCpteOrange != null && rsCpteOrange.next() && rsCpteAbonne != null && rsCpteAbonne.next()) {

			// Lecture des infos du cpte Debiteur
			eve.setDebiteur( (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("age") : rsCpteOrange.getString("age")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("dev") : rsCpteOrange.getString("dev")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("ncp") : rsCpteOrange.getString("ncp")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("suf") : rsCpteOrange.getString("suf")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("clc") : rsCpteOrange.getString("clc")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("cli") : rsCpteOrange.getString("cli")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("nom") : rsCpteOrange.getString("nom")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("ges") : rsCpteOrange.getString("ges")), transaction.getAmount(), transaction.getAmount(), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getDate("dva") : rsCpteOrange.getDate("dva")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getDouble("sde") : rsCpteOrange.getDouble("sde")));

			// Lecture des infos du cpte crediteur
			eve.setCrediteur( (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("age") : rsCpteOrange.getString("age")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("dev") : rsCpteOrange.getString("dev")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("ncp") : rsCpteOrange.getString("ncp")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("suf") : rsCpteOrange.getString("suf")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("clc") : rsCpteOrange.getString("clc")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("cli") : rsCpteOrange.getString("cli")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("nom") : rsCpteOrange.getString("nom")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getString("ges") : rsCpteOrange.getString("ges")), transaction.getAmount(), transaction.getAmount(), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getDate("dva") : rsCpteOrange.getDate("dva")), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? rsCpteAbonne.getDouble("sde") : rsCpteOrange.getDouble("sde")));

			// Log
			logger.info("Generation de l'evenement");
			
			// Fermeture des connexions
			CloseStream.fermeturesStream(rsCpteOrange);
			CloseStream.fermeturesStream(rsCpteAbonne);


			// Enregistrement de l'evenement
			executeUpdateSystemQuery(dsCBS, eve.getSaveQuery(), eve.getQueryValues());

			// Enregistrement de la commission evenement
			//executeUpdateSystemQuery(dsCBS, evec.getSaveQuery(), evec.getQueryValues());

			// Log
			logger.info("Creation de l'evenement OK!");

			// MAJ du solde indicatif debiteur
			executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(4).getQuery(), new Object[]{transaction.getAmount(), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? transaction.getAccount().split("-")[0] : ncpDAP.split("-")[0] ), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? transaction.getAccount().split("-")[1] : ncpDAP.split("-")[1] ), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? transaction.getAccount().split("-")[2] : ncpDAP.split("-")[2] )  });

			// Log
			logger.info("MAJ du solde indicatif du compte debiteur OK!");

			// MAJ du solde indicatif crediteur
			executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(5).getQuery(), new Object[]{transaction.getAmount(), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? transaction.getAccount().split("-")[0] : ncpDAP.split("-")[0] ), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? transaction.getAccount().split("-")[1] : ncpDAP.split("-")[1] ), (transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? transaction.getAccount().split("-")[2] : ncpDAP.split("-")[2] )  });

			// Log
			logger.info("MAJ du solde indicatif du compte crediteur OK!");

			// MAJ du dernier numero d'evenement utilise pour le type operation
			executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(3).getQuery(), new Object[]{numEve, codeOpe});

			// Log
			logger.info("MAJ du dernier numero d'evenement genere pour le type operation OK!");

		}

		CloseStream.fermeturesStream(rsCpteOrange); rsCpteOrange = null;
		CloseStream.fermeturesStream(rsCpteAbonne); rsCpteAbonne = null;

		//if(conCBS != null ) conCBS.close();

	}


	/**
	 * Construit l'evenement (accompagne des ecritures) a poster dans Delta 
	 * @param operation
	 * @param transaction
	 * @return evenement
	 */
	public bkeve buildEvenement(Transaction transaction) throws Exception {

		/***********************/
		/*** INITIALISATIONS ***/
		/***********************/
		
		Account custAcc = customerAccount(transaction.getAccount());
		Client customer = customerInfos(transaction.getSubscriber().getCustomerId());

		// Lecture des parametres generaux
		Parameters params = findParameters();

		if(params == null) throw new Exception("Parametres non initialises");

		
		// Initialisations
		ResultSet /*rsCpteOrange = null, rsCpteAbonne = null, rsCpteComsOrange = null,rsCpteComsBank = null, rsCpteTVAOrange = null, rsCpteTVABank = null, rsCpteLiaison = null, */ rsLiaison = null , rsLiaisonCentral = null;
		Bkcom bkCpteOrange = null, bkCpteComsOrange = null, bkCpteComsBank = null, bkCpteTVAOrange = null, bkCpteTVABank = null, bkLiaisonHippo = null, bkCpteLiaison = null;
		Map<TypeOperation, Commissions> mapComs = ConverterUtil.convertCollectionToMap(params.getCommissions(), "operation");

		Long numEc = 1l;
		Date dco = getDateComptable();
		Date dva1 = dco; //getDateValeurDebit(dco,dsCBS);
		Date dva2 = dco; //getDateValeurCredit(dco,dsCBS);
		Double tva = params.getTva() == null ? 19.25d : params.getTva();
		Double frais = 0d;
		Double tauxComOrange = 0d;
		Double tauxComBank = 0d;
		Double valeurComBank = 0d;
		Double valeurComOrange = 0d;
		Double valeurTaxBank = 0d;
		Double valeurTaxOrange = 0d;
		Double valeurCom = transaction.getCommissions();
		Double valeurTax = transaction.getTtc() - transaction.getCommissions();
		Date dateTransaction = transaction.getDate() != null ? transaction.getDate() : new Date();

		String natMag = "VIRORMO";
		String natIag = "VIRORMO";
		String datop = new SimpleDateFormat("ddMMyy").format(transaction.getDate() != null ? transaction.getDate() : new Date());

		ProfilMarchands plm = transaction.getSubscriber().getProfil();
		String ncpDAP = transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? params.getNcpDAPAcountWalet() : (transaction.getTypeOperation().equals(TypeOperation.Wallet2Account) ? params.getNcpDAPWaletAcount() : null);
		if(plm != null){
			ncpDAP = transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ? plm.getNcpDAPPull() : (transaction.getTypeOperation().equals(TypeOperation.Wallet2Account) ? plm.getNcpDAPPush() : null);
		} 
		/**if(ncpDAP == null && (transaction.getTypeOperation().equals(TypeOperation.BALANCE) || transaction.getTypeOperation().equals(TypeOperation.MiniStatement))){
			ncpDAP = params.getNcpCom();
		}*/

		/***************************************************************/
		/*** LECTURE DU DERNIER NUMERO D'EVENEMENT DU TYPE OPERATION ***/
		/***************************************************************/


		
		// Calcul du numero d'evenement
		Long res = lastNumEveOpe(params.getCodeOperation());
		Long numEve = res != null ? res : 1l;
		System.out.print("********* numEve ********** : " + numEve);

		// Log
		//executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(3).getQuery(), new Object[]{numEve, params.getCodeOperation() });
		//CloseStream.fermeturesStream(rs);

		// Recuperation de la commission correspondant a l'operation
		Commissions com = mapComs.get(transaction.getTypeOperation());

		if(com != null && (com.getModeFacturation() == null || !com.getModeFacturation().equals(ModeFacturation.PERIODIQUE) || plm != null  )){

			// Calcul du taux et des frais
			if(plm != null){
				if(transaction.getTypeOperation().equals(TypeOperation.COMPTABILISATION)){
					valeurCom = plm.getCommissions();
					tva = com.getTauxTVA();
					Double valeurComHT = Double.valueOf(Math.round(valeurCom / (1+(tva / 100.0))));
					valeurTax  = valeurCom - valeurComHT;
					//tauxComOrange = params.getTauxComOrange();
					//if(tauxComOrange == null)tauxComOrange = 60d;
					valeurComOrange = 0d;
					valeurComBank = valeurComHT;
					valeurTaxOrange = 0d;
					valeurTaxBank = valeurTax;
				} 

			}else{
				if( com.getTypeValeur().equals(TypeValeurFrais.FIXE) ) valeurCom = com.getValeur(); else valeurCom = com.getValeur();

				tva = com.getTauxTVA();
				Double valeurComHT = Double.valueOf(Math.round(valeurCom / (1+(tva / 100.0))));
				valeurTax  = valeurCom - valeurComHT;
				tauxComOrange = params.getTauxComOrange();
				if(tauxComOrange == null)tauxComOrange = 60d;
				valeurComOrange = Double.valueOf(Math.round(valeurComHT * (tauxComOrange/100)));
				valeurComBank = valeurComHT - valeurComOrange;
				valeurTaxOrange = Double.valueOf(Math.round(valeurTax * (tauxComOrange/100)));
				valeurTaxBank = valeurTax - valeurTaxOrange;

			}

		}

		// Si le client est un employe de la banque, on annule ses commissions
		if(isClientEmploye(transaction.getSubscriber().getCustomerId())){
			//valeurCom = 0d; valeurTax = 0d;
		}

		// Recherche du cpte de l'abonne
		//rsCpteAbonne = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(6).getQuery(), new Object[]{ transaction.getAccount().split("-")[0], transaction.getAccount().split("-")[1], transaction.getAccount().split("-")[2] });

		// Si on ne trouve le cpte du client on leve une exception
		if( custAcc == null ) {
			throw new Exception("Impossible de trouver le compte du client");
		}

		if(ncpDAP != null){
			bkCpteOrange = accountInfos(ncpDAP);
			// Si on ne trouve le cpte du client on leve une exception
			if( bkCpteOrange == null ) {
				throw new Exception("Impossible de trouver le compte Orange");
			}
		}		

		// Si une commission a ete parametre pour l'operation
		if( valeurCom > 0 ){

			// Si le compte des commissions Bank a ete parametre
			if(params.getNumCompteCommissions() != null && !params.getNumCompteCommissions().isEmpty()) 
				//rsCpteComsBank = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(7).getQuery(), new Object[]{params.getNumCompteCommissions().split("-")[0], params.getNumCompteCommissions().split("-")[1], params.getNumCompteCommissions().split("-")[2] });
			    bkCpteComsBank = accountInfos(params.getNumCompteCommissions());
			if(bkCpteComsBank == null) throw new Exception("Impossible de trouver le compte de commission banque");

			// Si le compte des commissions Orange a ete parametre
			if(params.getNcpOrange() != null && !params.getNcpOrange().isEmpty()) 
				//rsCpteComsOrange = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(7).getQuery(), new Object[]{params.getNcpOrange().split("-")[0], params.getNcpOrange().split("-")[1], params.getNcpOrange().split("-")[2] });
			    bkCpteComsOrange = accountInfos(params.getNcpOrange());
			if(bkCpteComsOrange == null) throw new Exception("Impossible de trouver le compte de commission");

			rsLiaisonCentral = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+params.getNumCompteCommissions().split("-")[0]+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
			if(rsLiaisonCentral == null || !rsLiaisonCentral.next()) throw new Exception("Impossible de trouver le compte de liaison de l'agence "+params.getNumCompteCommissions().split("-")[0]);
			if(rsLiaisonCentral != null) rsLiaisonCentral.next();

			// Si le numero de cpte TVA a ete parametre
			if(params.getNumCompteTVA() != null && !params.getNumCompteTVA().isEmpty())
				//rsCpteTVABank = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(7).getQuery(), new Object[]{params.getNumCompteTVA().split("-")[0],params.getNumCompteTVA().split("-")[1], params.getNumCompteTVA().split("-")[2] });
				bkCpteTVABank = accountInfos(params.getNumCompteTVA());
			//if(rsCpteTVABank != null) rsCpteTVABank.next();

			if(params.getNcpTVAOrange() != null && !params.getNcpTVAOrange().isEmpty())
				//rsCpteTVAOrange = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(7).getQuery(), new Object[]{params.getNcpTVAOrange().split("-")[0],params.getNcpTVAOrange().split("-")[1], params.getNcpTVAOrange().split("-")[2] });
			    bkCpteTVAOrange = accountInfos(params.getNcpTVAOrange());
			//if(bkCpteTVAOrange == null) throw new Exception("Impossible de trouver le compte TVA orange");;

		}

		transaction.setCommissions(valeurCom);
		transaction.setTtc(transaction.getAmount() + valeurCom); // valeurTax

		// Annulation de la Generation de l'evenement s'il nya aucun montant a debiter
		if(transaction.getTtc() == 0d && plm == null) {
			transaction = null;

			return null;
		}

		// Initialisation de l'evenement a retourner
		// ncpDAP == null ? (rsCpteComs != null && !rsCpteComs.getString("age").equals(rsCpteAbonne.getString("age")) ? natIag : natMag) : (rsCpteAbonne.getString("age").equals(ncpDAP.split("-")[0]) ? natMag : natIag)
		//*** bkeve eve = new bkeve(transaction, params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEve), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), rsCpteAbonne.getString("dev"), transaction.getAmount() + valeurCom,natMag , dco, params.getCodeUtil(), tauxComOrange, frais, tva, transaction.getAmount() + valeurCom,dva1,dva2);
		bkeve eve = new bkeve(transaction, params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEve), 
				OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), custAcc.getCodeDevise(), transaction.getAmount() + valeurCom,
				natMag , dco, params.getCodeUtil(), tauxComOrange, frais, tva, transaction.getAmount() + valeurCom, 
				transaction.getAmount() + valeurCom,dva1,dva2);
				eve.setRefdos(transaction.getRequestId());
		
		// S'il s'agit d'une operation inter-agence
		if(eve.getNat().equals(natIag) || eve.getNat().equals(natMag)){

			// Recuperation du compte de liaison de l'agence du client
			rsCpteLiaison = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+ custAcc.getAgence() +"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
			
			rsLiaisonHippo = null
			// Recuperation du compte de liaison d'hippodrome
			String age ="00001";
			if(bkCpteOrange != null){
				age = bkCpteOrange.getAge();
			}
			rsLiaisonHippo = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom  where dev='001' and age='"+age+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);

			// Si le compte de liaison existe
			if(rsCpteLiaison == null || !rsCpteLiaison.next()) {
				throw new Exception("Impossible de trouver le compte de liaison de l'agence " + custAcc.getAgence());
			}

			// Si le compte de liaison d'hippodrome n'existe pas
			if(rsLiaisonHippo == null || !rsLiaisonHippo.next()) {
				throw new Exception("Impossible de trouver le compte de liaison de l'agence d'hippodrome " );
			}

		}

		// S'il s'agit de la souscription
		if( transaction.getTypeOperation().equals(TypeOperation.SUBSCRIPTION) || transaction.getTypeOperation().equals(TypeOperation.MODIFY) || transaction.getTypeOperation().equals(TypeOperation.COMPTABILISATION) || transaction.getTypeOperation().equals(TypeOperation.MiniStatement) || transaction.getTypeOperation().equals(TypeOperation.BALANCE) ) {

			// Si on a parametre les frais sur la souscription
			if ( valeurCom > 0 ){

				// Ajout du debiteur
				eve.setDebiteur(custAcc.getAgence(), custAcc.getCodeDevise(), custAcc.getNcp(), custAcc.getSuffixe(), custAcc.getCle(), 
						custAcc.getCodeClient(), customer.getNom(), customer.getCodeGes(), transaction.getAmount() + valeurCom, 
						transaction.getAmount() + valeurCom, null, custAcc.getSde());

				// Ajout du debiteur
				if(bkCpteComsBank != null ) eve.setCrediteur(bkCpteComsBank.getAge(), bkCpteComsBank.getDev(), bkCpteComsBank.getNcp(), bkCpteComsBank.getSuf(),
						bkCpteComsBank.getClc(), bkCpteComsBank.getCli(), bkCpteComsBank.getInti(), "   ", valeurCom, valeurCom, null, bkCpteComsBank.getSde());

				// Libelle de l'evenement 
				if( transaction.getTypeOperation().equals(TypeOperation.SUBSCRIPTION))
					eve.setLib1(transaction.getPhoneNumber()+"/"+datop+"/SUBSCRIPTION");

				else if( transaction.getTypeOperation().equals(TypeOperation.BALANCE))
					eve.setLib1(transaction.getPhoneNumber()+"/"+datop+"/BALANCE"+"/"+transaction.getRequestId());

				else if( transaction.getTypeOperation().equals(TypeOperation.MiniStatement))
					eve.setLib1(transaction.getPhoneNumber()+"/"+datop+"/MiniStatement"+"/"+transaction.getRequestId());
				else eve.setLib1(custAcc.getNcp()+"/"+datop+"/"+transaction.getTypeOperation().toString()+"/"+transaction.getRequestId());

			}

			// S'il s'agit du Pull
		} else if( transaction.getTypeOperation().equals(TypeOperation.Account2Wallet) ){
			
			bkCpteOrange = accountInfos(ncpDAP);
			// Recuperation du compte Float Orange
			//rsCpteOrange = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(8).getQuery(), new Object[]{ ncpDAP.split("-")[0], ncpDAP.split("-")[1], ncpDAP.split("-")[2] });

			// Ajout du debiteur
			eve.setDebiteur(custAcc.getAgence(), custAcc.getCodeDevise(), custAcc.getNcp(), custAcc.getSuffixe(), custAcc.getCle(), custAcc.getCodeClient(),
					customer.getNom(), customer.getCodeGes(), transaction.getAmount(), transaction.getAmount(), null, custAcc.getSde());

			// Ajout du crediteur
			if(bkCpteOrange != null) eve.setCrediteur(bkCpteOrange.getAge(), bkCpteOrange.getDev(), bkCpteOrange.getNcp(), bkCpteOrange.getSuf(), 
					bkCpteOrange.getClc(), bkCpteOrange.getCli(), bkCpteOrange.getInti(), "", transaction.getAmount(), transaction.getAmount(), null, bkCpteOrange.getSde());

			// Libelle de l'evenement
			//MODIF*** eve.setLib1(transaction.getPhoneNumber()+"/"+datop+"/Bank2Wallet");
			eve.setLib1(transaction.getPhoneNumber()+"/"+new SimpleDateFormat("ddMMyyHHmm").format(dateTransaction)+"A2W"+"/"+transaction.getRequestId());

			// S'il s'agit du Push
		}else if(transaction.getTypeOperation().equals(TypeOperation.Wallet2Account)){

			bkCpteOrange = accountInfos(ncpDAP);
			// Recuperation du compte Float Orange
			//rsCpteOrange = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(8).getQuery(), new Object[]{ ncpDAP.split("-")[0], ncpDAP.split("-")[1], ncpDAP.split("-")[2] });

			// Ajout du crediteur
			eve.setCrediteur(custAcc.getAgence(), custAcc.getCodeDevise(), custAcc.getNcp(), custAcc.getSuffixe(), custAcc.getCle(), custAcc.getCodeClient(), 
					customer.getNom(), customer.getCodeGes(), transaction.getAmount(), transaction.getAmount(), null, custAcc.getSde());

			// Ajout du debiteur
			if(bkCpteOrange != null) eve.setDebiteur(bkCpteOrange.getAge(), bkCpteOrange.getDev(), bkCpteOrange.getNcp(), bkCpteOrange.getSuf(), 
					bkCpteOrange.getClc(), bkCpteOrange.getCli(), bkCpteOrange.getInti(), "", transaction.getAmount(), transaction.getAmount(), null, bkCpteOrange.getSde());

			// Libelle de l'evenement
			//MODIF*** eve.setLib1(transaction.getPhoneNumber()+"/"+datop+"/Wallet2Bank");
			eve.setLib1(transaction.getPhoneNumber()+"/"+new SimpleDateFormat("ddMMyyHHmm").format(dateTransaction)+"/W2A"+"/"+transaction.getRequestId());
		}

		/***********************************************************/
		/** GENERATION DES ECRITURES COMPTABLES DE LA TRANSACTION **/
		/***********************************************************/
		String pie = RandomStringUtils.randomNumeric(6);

		// Si c'est un Account2Wallet
		if(transaction.getTypeOperation().equals(TypeOperation.Account2Wallet)){ // && mapComs.get(TypeOperation.Account2Wallet).getModeFacturation().equals(ModeFacturation.TRANSACTION) ) {

			// Debit du client du TTC
			eve.getEcritures().add( new bkmvti(custAcc.getAgence(), custAcc.getCodeDevise(), custAcc.getChapitre(), custAcc.getNcp(), custAcc.getSuffixe(), 
					params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null,
					params.getCodeUtil(), eve.getEve(), custAcc.getCle(), dco, null, null, transaction.getAmount() + valeurCom + valeurTax, "D",
					transaction.getPhoneNumber()+"/"+new SimpleDateFormat("ddMMyyHHmm").format(dateTransaction)+"/"+"A2W", "N", pie, null, null, null, null, 
					null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, custAcc.getAgence(), custAcc.getCodeDevise(), 
					transaction.getAmount() + valeurCom + valeurTax, null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;
			
			if(!custAcc.getAgence().equalsIgnoreCase(bkCpteOrange.getAge())){
				// Credit de la liaison du client du TTC
				if(rsCpteLiaison != null) eve.getEcritures().add( new bkmvti(rsCpteLiaison.getString("age"), rsCpteLiaison.getString("dev"), rsCpteLiaison.getString("cha"), rsCpteLiaison.getString("ncp"), rsCpteLiaison.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaison.getString("clc"), dco, null, rsCpteLiaison.getDate("dva"), transaction.getAmount() + valeurCom + valeurTax, "C",transaction.getPhoneNumber()+"/"+new SimpleDateFormat("ddMMyyHHmm").format(dateTransaction)+"/"+"A2W", "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaison.getString("age"), rsCpteLiaison.getString("dev"), transaction.getAmount() + valeurCom + valeurTax, null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;

				// Debit de la liaison d'hippodromme du TTC
				if(rsLiaisonHippo != null) eve.getEcritures().add( new bkmvti(rsLiaisonHippo.getString("age"), rsLiaisonHippo.getString("dev"), rsLiaisonHippo.getString("cha"), rsLiaisonHippo.getString("ncp"), rsLiaisonHippo.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonHippo.getString("clc"), dco, null, rsLiaisonHippo.getDate("dva"), transaction.getAmount() + valeurCom + valeurTax, "D",transaction.getPhoneNumber()+"/"+new SimpleDateFormat("ddMMyyHHmm").format(dateTransaction)+"/"+"A2W", "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonHippo.getString("age"), rsLiaisonHippo.getString("dev"), transaction.getAmount() + valeurCom + valeurTax, null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;
			}
			// Credit du cpte MTN du HT
			eve.getEcritures().add(new bkmvti(bkCpteOrange.getAge(), bkCpteOrange.getDev(), bkCpteOrange.getCha(), bkCpteOrange.getNcp(), bkCpteOrange.getSuf(),
					params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, 
					params.getCodeUtil(), eve.getEve(), bkCpteOrange.getClc(), dco, null, null, transaction.getAmount(), "C",
					transaction.getPhoneNumber()+"/"+new SimpleDateFormat("ddMMyyHHmm").format(dateTransaction)+"/"+"A2W", "N", pie, null, null, null, null, 
					null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, bkCpteOrange.getAge(), bkCpteOrange.getDev(),
					transaction.getAmount(), null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;

			// Si c'est un Wallet2Account
		}else if(transaction.getTypeOperation().equals(TypeOperation.Wallet2Account)){   // && mapComs.get(TypeOperation.Wallet2Account).getModeFacturation().equals(ModeFacturation.TRANSACTION)) {

			// Debit du cpte MTN du montant HT
			eve.getEcritures().add( new bkmvti(bkCpteOrange.getAge(), bkCpteOrange.getDev(), bkCpteOrange.getCha(), bkCpteOrange.getNcp(), bkCpteOrange.getSuf(), 
					params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, 
					params.getCodeUtil(), eve.getEve(), bkCpteOrange.getClc(), dco, null, null, transaction.getAmount(), "D",
					transaction.getPhoneNumber()+"/"+new SimpleDateFormat("ddMMyyHHmm").format(dateTransaction)+"/"+"W2A", "N", pie, null, null, null, null,
					null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, bkCpteOrange.getAge(), bkCpteOrange.getDev(),
					transaction.getAmount(), null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;
			
			if(!custAcc.getAgence().equalsIgnoreCase(bkCpteOrange.getAge())){
				// Credit de la liaison d'hippodrome du HT
				if(rsLiaisonHippo != null) eve.getEcritures().add( new bkmvti(rsLiaisonHippo.getString("age"), rsLiaisonHippo.getString("dev"), rsLiaisonHippo.getString("cha"), rsLiaisonHippo.getString("ncp"), rsLiaisonHippo.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonHippo.getString("clc"), dco, null, rsLiaisonHippo.getDate("dva"), transaction.getAmount(), "C",transaction.getPhoneNumber()+"/"+new SimpleDateFormat("ddMMyyHHmm").format(dateTransaction)+"/"+"W2A", "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonHippo.getString("age"), rsLiaisonHippo.getString("dev"), transaction.getAmount(), null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;

				// Debit de la liaison du client du HT
				if(rsCpteLiaison != null) eve.getEcritures().add( new bkmvti(rsCpteLiaison.getString("age"), rsCpteLiaison.getString("dev"), rsCpteLiaison.getString("cha"), rsCpteLiaison.getString("ncp"), rsCpteLiaison.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaison.getString("clc"), dco, null, rsCpteLiaison.getDate("dva"), transaction.getAmount(), "D",transaction.getPhoneNumber()+"/"+new SimpleDateFormat("ddMMyyHHmm").format(dateTransaction)+"/"+"W2A", "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaison.getString("age"), rsCpteLiaison.getString("dev"), transaction.getAmount(), null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;
			}
			// Credit du cpte client du HT
			eve.getEcritures().add( new bkmvti(custAcc.getAgence(), custAcc.getCodeDevise(), custAcc.getChapitre(), custAcc.getNcp(), custAcc.getSuffixe(), 
					params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, 
					params.getCodeUtil(), eve.getEve(), custAcc.getCle(), dco, null, null, transaction.getAmount(), "C",
					transaction.getPhoneNumber()+"/"+new SimpleDateFormat("ddMMyyHHmm").format(dateTransaction)+"/"+"W2A", "N", pie, null, null, null, null, 
					null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, custAcc.getAgence(), custAcc.getCodeDevise(),
					transaction.getAmount(), null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;

		}
		//else{

		if( valeurCom > 0){

			String lib = eve.getLib1();

			// Ecriture Comptable Commission Afriland 
			eve.getEcritures().add( new bkmvti(custAcc.getAgence(), custAcc.getCodeDevise(), custAcc.getChapitre(), custAcc.getNcp(), custAcc.getSuffixe(), 
					params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, 
					params.getCodeUtil(), eve.getEve(), custAcc.getCle(), dco, null, null, valeurCom, "D", lib, "N", pie, null, null, null, null, null, null, 
					null, null, null, null, null, null, null, null, null, null, null, null, null, null, custAcc.getAgence(), custAcc.getCodeDevise(), 
					valeurCom + valeurTax, null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;
			
			if(!custAcc.getAgence().equalsIgnoreCase(bkCpteComsBank.getAge())){
				
				// Debit de la liaison du client du HT
				rsCpteLiaison = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+custAcc.getAgence()+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
				if(rsCpteLiaison != null){
					rsCpteLiaison.next();
					eve.getEcritures().add( new bkmvti(rsCpteLiaison.getString("age"), rsCpteLiaison.getString("dev"), rsCpteLiaison.getString("cha"), rsCpteLiaison.getString("ncp"), rsCpteLiaison.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaison.getString("clc"), dco, null, rsCpteLiaison.getDate("dva"), valeurCom, "C",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaison.getString("age"), rsCpteLiaison.getString("dev"), valeurCom, null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;
				}
				
				CloseStream.fermeturesStream(rsLiaisonCentral);
				// Credit de la liaison d'hippodrome du HT
				rsLiaisonCentral = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+bkCpteComsBank.getAge()+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
				if(rsLiaisonCentral != null){ 
					rsLiaisonCentral.next();
					eve.getEcritures().add( new bkmvti(rsLiaisonCentral.getString("age"), rsLiaisonCentral.getString("dev"), rsLiaisonCentral.getString("cha"), rsLiaisonCentral.getString("ncp"), rsLiaisonCentral.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonCentral.getString("clc"), dco, null, rsLiaisonCentral.getDate("dva"), valeurCom, "D",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonCentral.getString("age"), rsLiaisonCentral.getString("dev"), valeurCom, null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;
				}
			}
			if(!transaction.getTypeOperation().equals(TypeOperation.SUBSCRIPTION)){
				// Ecriture Comission Bank

				eve.getEcritures().add( new bkmvti(bkCpteComsBank.getAge(), bkCpteComsBank.getDev(), bkCpteComsBank.getCha(), bkCpteComsBank.getNcp(),
						bkCpteComsBank.getSuf(), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), 
						null, params.getCodeUtil(), eve.getEve(), bkCpteComsBank.getClc(), dco, null, null, valeurCom, "C",lib, "N", pie, null, null, null, null,
						null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, bkCpteComsBank.getAge(), bkCpteComsBank.getDev(), 
						valeurComBank, null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;

			}else{
				// Ecriture Comission Bank
				eve.getEcritures().add( new bkmvti(bkCpteComsBank.getAge(), bkCpteComsBank.getDev(), bkCpteComsBank.getCha(), bkCpteComsBank.getNcp(), 
						bkCpteComsBank.getSuf(), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), 
						null, params.getCodeUtil(), eve.getEve(), bkCpteComsBank.getClc(), dco, null, null, valeurCom, "C",lib, "N", pie, null, null, null, null,
						null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, bkCpteComsBank.getAge(), bkCpteComsBank.getDev(), 
						valeurComBank, null, null, null, null, null, transaction.getRequestId(), null, null, eve.getNat(), "VA", null, null) );  numEc++;
			}

		}

		// On libere les variables
		CloseStream.fermeturesStream(rsLiaisonCentral); rsLiaisonCentral = null;
		CloseStream.fermeturesStream(rsLiaisonHippo); rsLiaisonHippo = null;
		CloseStream.fermeturesStream(rsCpteLiaison); rsCpteLiaison = null;
		
		mapComs.clear();

		eve.setDco(dco);
		eve.setDva1(dva1);
		eve.setDva2(dva2);
		eve.setDou(dco);

		//if(conCBS != null ) conCBS.close();

		// Retourne l'evenement
		return eve;

	}



	private  Parameters params = null;

	/**
	 * Determine si on a lance les TFJ
	 * @return
	 * @throws Exception
	 * @throws OgMoException 
	 */
	private boolean isModeNuit() throws Exception, OgMoException {

		boolean res = false;
		
		try {
			
			if (dsCbs == null) findCBSServicesDataSystem();
			if(dsCbs != null && StringUtils.isNotBlank(dsCbs.getDbConnectionString())) {
				
				HttpGet getRequest = new HttpGet(dsCbs.getDbConnectionString()+"/kyc/process/modenuit");
			    getRequest.setHeader("content-type", "application/json");
			    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(getRequest);
			    HttpEntity entity = null;
			    entity = response.getEntity();
			    
			    if(entity != null) {
			    	
			    	 if(entity != null) {
			    		 
			    		 String content = EntityUtils.toString(entity);
						 JSONObject json = new JSONObject(content);
						 
						 String responseCode = json.getString("code");
						 
						 if ("200".equals(responseCode)) {
							 res = true;
						 }
			    		 
			    	 }
			    } 
			}
		}
		catch(Exception e) {
			return false;
		}
		
		// Retourne le resultat 
		return res;
	}

	/**
	 * Retourne le solde du compte passe en parametre. 
	 * Le numero de compte est sous le format : 00000-00000000000-00
	 * @param numCompte
	 * @return
	 * @throws Exception

	private Double getSolde(String numCompte) throws Exception {

		// Initialisation de la valeur du solde a retourne
		Double solde = 0d;

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();

		// Execution de la requete de selection du solde indicatif du compte en mode nuit comme en mode jour
		ResultSet rs = executeFilterSystemQuery(dsCBS,  isModeNuit() ? "select sin + (select nvl(sum(mon),0) from bksin where age='"+ numCompte.split("-")[0] +"' and ncp='"+ numCompte.split("-")[1] +"' ) as sin from bkcom where age = ? and ncp = ? and clc = ? " : "select sin from bkcom where age = ? and ncp = ? and clc = ? ", new Object[]{ numCompte.split("-")[0], numCompte.split("-")[1], numCompte.split("-")[2] }) ; //: executeFilterSystemQuery(dsCBS, "", new Object[]{ numCompte.split("-")[0], numCompte.split("-")[1], numCompte.split("-")[2] }) ;

		// Recuperation du solde du compte
		if(rs != null && rs.next()) solde = rs.getDouble("sin");

		// Recherche de la disponibilité d'une ligne de decouvert dans le compte du client
		rs = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(11).getQuery(), new Object[]{ numCompte.split("-")[1], new Date() });

		// Si le decouvert existe (on ajoute le montant de l'autorisation au solde du client)
		if(rs != null && rs.next()) solde += rs.getDouble("maut");

		// Fermeture de la cnx
		rs.getStatement().close(); rs.close();

		// Retourne le solde du cpte
		return solde;

	}*/


	/**
	 * 
	 * @param numCompte
	 * @param montant
	 * @param type
	 * @return
	 * @throws Exception
	 * @throws OgMoException
	 */
	private Double getSolde(String numCompte,Double montant,TypeOperation type) throws Exception, OgMoException {

		Double solde = 0d;
		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();

		params = findParameters();

		solde =  getSoldeInCoreBanking(numCompte);
		if(solde == null) solde = 0d;

		Double decouvert = 0d;


		if(isBlocageDebit(numCompte,params.getCodeOpe(),null)){
			// Message d'avertissement
			throw new OgMoException(ExceptionCode.BankBlockingDebitAccount, ExceptionCode.BankBlockingDebitAccount.getValue(), ExceptionCategory.METIER);
		}


		// Controle Solde Minimun du Compte (compte )  02979551051
		// Lecture des parametres generaux
		Parameters params = findParameters();
		Double soldMin = params.getSoldeMin();
		String plageCompte = params.getPlageCompte();
		Boolean trouve = Boolean.FALSE;
		if(soldMin != null && plageCompte != null ){
			if(soldMin > 0d && !plageCompte.trim().isEmpty()){
				for(String val : plageCompte.split(",")){
					if(numCompte.split("-")[1].substring(7,10).equalsIgnoreCase(val)){
						trouve = Boolean.TRUE;
					}
				}
			}
		}

		if(!type.equals(TypeOperation.Wallet2Account)) {
			if(trouve.equals(Boolean.TRUE)){

				if((solde - soldMin - montant + decouvert ) < 0  && !type.equals(TypeOperation.BALANCE)){
					throw new OgMoException(ExceptionCode.BankInsufficientBalance, ExceptionCode.BankInsufficientBalance.getValue(), ExceptionCategory.SUBSCRIBER);
				}
			}else{
				if((solde - montant + decouvert ) < 0  && !type.equals(TypeOperation.BALANCE)){
					throw new OgMoException(ExceptionCode.BankInsufficientBalance, ExceptionCode.BankInsufficientBalance.getValue(), ExceptionCategory.SUBSCRIBER);
				}
			}

			// Contorle Chapitre 
			/*
			if(CheckChapiter(cha).equals(Boolean.FALSE)){
				throw new OgMoException(ExceptionCode.BankBlockingDebitAccount, ExceptionCode.BankBlockingDebitAccount.getValue(), ExceptionCategory.SUBSCRIBER);
			}
			 */
		}


		if(!type.equals(TypeOperation.BALANCE)){
			solde = solde + decouvert;	
		}

		return solde;

	}



	private Double getSoldeCompte(String numCompte) throws Exception, OgMoException {

		return getSoldeInCoreBanking(numCompte);

	}



	public Boolean CheckChapiter(String cha){

		// Controle chapitre du Compte 
		Parameter parameter = filterParameter("CHAPITRE_MFRAIS");
		String txtchapitre = "";
		Map<String,String> MapChapitre = new HashMap<String,String>();
		if(parameter != null ){
			txtchapitre = parameter.getParameter().trim();
			for(String rs : txtchapitre.split(",")){
				MapChapitre.put(rs,rs);
			}				
		}



		List<String> chapitres = new ArrayList<String>(MapChapitre.keySet());
		for(String rs : chapitres){
			if(!rs.contains("-")){
				if(rs.trim().length() == 8){
					if(rs.trim().equalsIgnoreCase(cha)) return Boolean.FALSE;
				}else if(rs.trim().length() < 8){
					if(cha.startsWith(rs.trim())) return Boolean.FALSE;
				}
			}
		}

		// Controle des Plages 
		for(String rs : chapitres){
			if(rs.contains("-")){
				String debut  = rs.split("-")[0];
				String fin  = rs.split("-")[1];
				if(debut.trim().length() == 8  && fin.trim().length() == 8 ){
					Double valdebut = Double.valueOf(debut);
					Double valfin = Double.valueOf(fin);
					Double valcha = Double.valueOf(cha);
					if(valdebut >= valcha && valcha >= valfin) return Boolean.FALSE;
				}
			}
		}

		return Boolean.TRUE;

	}




	public Parameter filterParameter(String txtCode){
		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		rc.add(Restrictions.eq("code",txtCode));	
		List<Parameter> params = dao.filter(Parameter.class, null, rc, null, null, 0, -1);
		if(params.isEmpty()) return null;
		return params.get(0);
	}




	/**
	 * Retourne la table des comptes a utiliser (Gestion du cas des TFJ)
	 * @return
	 * @throws Exception
	 *
	private String getTableComptesAUtiliser() throws Exception {

		// Retourne la table des comptes a utiliser
		return "bkcom"; //getStatutAgenceCentrale().equalsIgnoreCase("OU") ? "bkcom" : "bkcom";
	}*/

	/**
	 * Timer qui teste la fin des TFJ (toutes les heures)
	private void TFJScheduler() {

		// Initialisation du Timer
		Timer timer = new Timer();

		// Initialisation de la tache a executer
		timer.schedule(new TimerTask() {

			// Initialisation du compteur du delai de controle du lancement des tfj dans Amplitude
			int n = 0; boolean aEteFerme = false;

			@Override
			public void run() {

				try{

					//logger.info("[Traitement TFJ MAC]. Etape " + n);

					if(getStatutAgenceCentrale().equalsIgnoreCase("OU")) {

						logger.info("[Traitement TFJ MAC]. Agence Ouverte");

						if( n >= delaiCtrlTFJDelta && aEteFerme ){

							logger.info("[Traitement TFJ MAC]. Nbre de passage=" + n + " et Agence tjrs ouverte donc, execution de la fin des TFJ");

							// Suspension du Timer
							this.cancel();

							// Alors les TFJ sont finis et on execute les operations PST-TFJ
							endTFJ();

						} else if(n>=40){
							this.cancel(); endTFJ();
						}

					} else aEteFerme = true;

					// On incremente le compteur de controle de delai
					n++;

				}catch(Exception ex){logger.error("Erreur lors de la cloture des TFJ"); ex.printStackTrace();}

			}

			// Delai d'execution du Timer (1h)
		}, 0, nbMinutesCtrlFinTFJ * 1000);

	}*/


	public boolean isSoldeSuffisant(String numCompte, Double montant) throws Exception {

		boolean res = false;

		Double solde = 0d;

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();

		// Recherche du Solde du compte client
		ResultSet rs = executeFilterSystemQuery(dsCBS, "select sin from bkcom where age = ? and ncp = ? and clc = ? ", new Object[]{ numCompte.split("-")[0], numCompte.split("-")[1], numCompte.split("-")[2] });

		// Recuperation du solde du cpte
		if(rs != null && rs.next()) solde = rs.getDouble("sin");

		// Si on est en periode de TFJ, on recupere le total des operation effectuees dans e-First
		CloseStream.fermeturesStream(rs);
		if(getStatutAgenceCentrale().equals("FE")) {
			rs = executeFilterSystemQuery(dsCBS, "select nvl(sum(mon),0) as mon from bksin where age = ? and ncp = ? ", new Object[]{ numCompte.split("-")[0], numCompte.split("-")[1] });
		}
		else {
			rs = null;
		}
		
		// Reduction du solde indicatif par le total des transactions e-First
		if(rs != null && rs.next()) solde -= rs.getDouble("mon");

		// Calcul du resultat
		res = solde < montant;

		// Fermeture de la cnx
		CloseStream.fermeturesStream(rs);

		//if(conCBS != null ) conCBS.close();

		return res;

	}

	public boolean isCompteFerme(String numCompte) {

		boolean res = false;

		try {

			// Initialisation de DataStore d'Amplitude
			if(dsCBS == null) findCBSDataSystem();

			ResultSet rs = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(7).getQuery(), new Object[]{ numCompte.split("-")[0], numCompte.split("-")[1], numCompte.split("-")[2] });

			if(rs == null || !rs.next()) res = true;

			CloseStream.fermeturesStream(rs);

			//if(conCBS != null ) conCBS.close();

		} catch(Exception e){}

		return res;

	}



	public boolean isBlocageCredit(String numCompte) throws Exception{

		boolean res = false;

		try {

			params = findParameters();

			// Initialisation de DataStore d'Amplitude
			if(dsCBS == null) findCBSDataSystem();

			String sql = "select age, ncp, opp from bkoppcom where eta='V' and (dfin>=today or dfin is null) and opp in LISTOPP and age = ? and ncp = ? ";

			String in = "";
			if(params.getOppositions() != null && !params.getOppositions().trim().isEmpty()) {
				for(String s : params.getOppositions().split("-")) in += "'" + s + "'" + ", ";
			}
			if(!in.isEmpty()) in = "(".concat( in.substring(0, in.length()-2) ).concat(")");


			ResultSet rs = executeFilterSystemQuery(dsCBS, sql.replace("LISTOPP", in), new Object[]{ numCompte.split("-")[0], numCompte.split("-")[1]});

			if(rs != null && rs.next()){
				res = true;
			}

			CloseStream.fermeturesStream(rs);

			//if(conCBS != null ) conCBS.close();

		} catch(Exception e){}

		return res;

	}




	private String CODE_OPE  =  "select opp from bkoppcom where age=? and ncp=? and eta='V'  and (dfin is null or dfin>today)";   // select opp from bkoppcom where age=? and ncp=? and eta='V'  and (dfin is null or dfin>today);
	//*** private String CODE_OPE  =  "select opp from bkoppcom where age=? and ncp=? and eta='V' ";   // select opp from bkoppcom where age=? and ncp=? and eta='V'  and (dfin is null or dfin>today);

	private String CODE_OPE_CLI  =  "select opp from bkoppcli where eta='V' and (dfin>=today or dfin is null) and cli=? ";   // select opp from bkoppcom where age=? and ncp=? and eta='V'  and (dfin is null or dfin>today);
	//private String CODE_OPE_CLI  =  "select opp from bkoppcli where eta='V' and cli=? ";   // select opp from bkoppcom where age=? and ncp=? and eta='V'  and (dfin is null or dfin>today);


	public boolean isBlocageDebit(String numCompte,String ope,String opp) throws Exception{

		// Controle Opposition Debit sur le Compte    
		System.out.println("********** getOppositionOperation numCompte ***********" + numCompte);

		try {
			
				if (dsCbs == null) findCBSServicesDataSystem();
				if(dsCbs != null && StringUtils.isNotBlank(dsCbs.getDbConnectionString())) {
				
				HttpGet getRequest = new HttpGet(dsCbs.getDbConnectionString()+"/transactions/process/oppos-compte/" + numCompte.split("-")[0] +"/" + numCompte.split("-")[1]);
			    getRequest.setHeader("content-type", "application/json");
			    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(getRequest);
			    HttpEntity entity = null;
			    entity = response.getEntity();
			    
			    if(entity != null) {
			    	
			    	 if(entity != null) {
			    		 
			    		 String content = EntityUtils.toString(entity);
						 JSONObject json = new JSONObject(content);
						 
						 String responseCode = json.getString("code");
						
						 
						 if ("200".equals(responseCode)) {
							 String jsonData = json.getString("data").replace(" ", "");
							
					        JSONArray jsonArray = new JSONArray(jsonData);  
					        if (jsonArray != null) {   
					            JSONObject opps = new JSONObject();
					            //Iterating JSON array  
					            for (int i=0;i<jsonArray.length();i++){   
					            	opps = (JSONObject) jsonArray.get(i);
					            	if (opps.getString("opp").equals(ope)) {
					            		return Boolean.TRUE;
					            	}
					            }   
					        }  
						 }
			    	 }
			    } 
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return Boolean.FALSE;

	}
	
	public Bkcom accountInfos(String compte) {
		
	   Bkcom bkcom = null;
		
		try {
			//"select * from bkcom where age = ? and ncp = ? and clc = ? and cfe='N' and ife='N'"
			if (dsCbs == null) findCBSServicesDataSystem();
			if(dsCbs != null && StringUtils.isNotBlank(dsCbs.getDbConnectionString())) {
				
				HttpGet getRequest = new HttpGet(dsCbs.getDbConnectionString()+"/kyc/process/getaccountinfo/"+compte.split("-")[0]+"/"+compte.split("-")[1]);
			    getRequest.setHeader("content-type", "application/json");
			    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(getRequest);
			    HttpEntity entity = null;
			    entity = response.getEntity();
			    
			    if(entity != null) {
			    	
			    	 if(entity != null) {
			    		 
			    		 List<Bkcom> listCompte = new ArrayList<>();
			    		 String content = EntityUtils.toString(entity);
						 JSONObject json = new JSONObject(content);
						 
						 ResponseDataBkcom responseDataCpte = Shared.mapToResponseDataBkcom(json);
						 String responseCode = responseDataCpte.getCode();
						
						 
						 if ("200".equals(responseCode)) {
							 listCompte = responseDataCpte.getData();
							 if(!listCompte.isEmpty()) {
								 bkcom = listCompte.get(0);
								 if("N".equals(bkcom.getIfe()) && "N".equals(bkcom.getCfe())) {
									 return bkcom;
								 }
							 }
						 }
			    		 
			    	 }
			    	
			    } 
			}
		}
		catch(Exception e) {
			return null;
		}
		
		return bkcom;
	}
	   
    private Account customerAccount(String numCompte) {
    	Account acc = null;
    	
    	try {
			// Initialisation de DataStore d'Amplitude
			if(dsAIF == null) findAIFDataSystem();
			if(dsAIF != null && StringUtils.isNotBlank(dsAIF.getDbConnectionString())) {
				
				String age = numCompte.split("-")[0];
				String ncp = numCompte.split("-")[1];
				String cle = numCompte.split("-")[2];
				
				HttpGet getRequest = new HttpGet(dsAIF.getDbConnectionString()+"/account/getlistecompte/"+ncp.substring(0, 7));
			    getRequest.setHeader("content-type", "application/json");
			    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(getRequest);
			    HttpEntity entity = null;
			    entity = response.getEntity();
			    
			    if(entity != null) {
			    	 
			    	 List<Account> listComptes = new ArrayList<>();
			    	 String content = EntityUtils.toString(entity);
					 JSONObject json = new JSONObject(content);
					
					 ResponseDataAccount responseDataAcc = Shared.mapToResponseDataAccount(json);
					 String responseCode = responseDataAcc.getCode();
					 
					 if ("200".equals(responseCode)) {
						 
						 listComptes = responseDataAcc.getDatas();
						 
						 for(Account account : listComptes) {
							 
							 if (age.equalsIgnoreCase(account.getAgence()) && ncp.equalsIgnoreCase(account.getNcp()) 
									 && cle.equalsIgnoreCase(account.getCle()) ) {
								 
								 acc = account;
								 break;
							 
							 }
							 
						 }	 
					 } 
			     }
			}

		} catch(Exception e){
			return null;
		}
    	
    	return acc;
    }
    
    private Client customerInfos(String custId) {
    	
    	Client cust = null;
    	
    	try {

			// Initialisation de DataStore d'Amplitude
			//if(dsCBS == null) findCBSDataSystem();
			//ResultSet rs = executeFilterSystemQuery(dsCBS, "select pro from bkprocli where cli='"+ numClient +"' order by dpro desc", null);
			
			if(dsAIF == null) findAIFDataSystem();
			if(dsAIF != null && StringUtils.isNotBlank(dsAIF.getDbConnectionString())) {
				
				HttpGet getRequest = new HttpGet(dsAIF.getDbConnectionString()+"/customer/customerdetails/"+custId);
			    getRequest.setHeader("content-type", "application/json");
			    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(getRequest);
			    HttpEntity entity = null;
			    entity = response.getEntity();
			 
			    if(entity != null) {
			    	 
			    	 List<Client> listClient = new ArrayList<>();
			    	 String content = EntityUtils.toString(entity);
					 JSONObject json = new JSONObject(content);
					
					 ResponseDataClient responseDataClt = Shared.mapToResponseDataClient(json);
					 String responseCode = responseDataClt.getCode();
					
					 
					 if ("200".equals(responseCode)) {
						 
						 listClient = responseDataClt.getDatas();
						 if(!listClient.isEmpty()) {
							 cust = listClient.get(0);
						 }
					 } 
			     }
			}

		} catch(Exception e){
			return null;
		}
    	
    	return cust;
    }

    public Long lastNumEveOpe(String ope) {
		
		Long numEve = null;
		
		try {
			
			if (dsCbs == null) findCBSServicesDataSystem();
			if(dsCbs != null && StringUtils.isNotBlank(dsCbs.getDbConnectionString())) {
				System.out.println("NUMEVE: " + dsCbs.getDbConnectionString()+"/transactions/process/lastnumeroeveope/" + ope);
				HttpGet getRequest = new HttpGet(dsCbs.getDbConnectionString()+"/transactions/process/lastnumeroeveope/" + ope);
			    getRequest.setHeader("content-type", "application/json");
			    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(getRequest);
			    HttpEntity entity = null;
			    entity = response.getEntity();
			    
			    if(entity != null) {
			    	
			    	 if(entity != null) {
			    		 
			    		 String content = EntityUtils.toString(entity);
						 JSONObject json = new JSONObject(content);
						 //System.out.println("JSONObject: " + json);
						 String responseCode = json.getString("code");
						 //System.out.println("responseCode: " + responseCode);
						 if ("200".equalsIgnoreCase(responseCode)) {
							 String lienSig = json.getString("data");
							 numEve = lienSig != null && !StringUtils.isBlank(lienSig) ? Long.valueOf(lienSig) + 1 : 1l;
							 //System.out.println("numEve: " + numEve);
						 }
			    		 
			    	 }
			    	
			    } 
			}
		}
		catch(Exception e) {
			return null;
		}
		
		return numEve;
	}

	public boolean isBlocageDebitClient(String client,String ope) throws Exception{

		// Controle Opposition Debit sur le Compte    
		ResultSet rs = executeFilterSystemQuery(dsCBS, CODE_OPE_CLI, new Object[]{client});
		List<String> opps = new ArrayList<String>();
		while(rs.next()){
			opps.add(rs.getString("opp"));
		}
		//System.out.println("********** getOppositionOperation opps.size() ***********" + opps.size());
		//System.out.println("********** getOppositionOperation opps.toString() ***********" + opps.toString());
		CloseStream.fermeturesStream(rs);

		//if(conCBS != null ) conCBS.close();

		return getOppositionOperation(ope, client, opps);

	}


	public Boolean getOppositionOperation(String ope,String age,List<String> opps)throws Exception{
		for(String oppp : opps){
			ResultSet rs = executeFilterSystemQuery(dsCBS, " select copp from bkopl where (age='00099' or age = ? ) and copp='O' and ope=? and opp= ?  ", new Object[]{age,ope,oppp});
			if(rs != null && rs.next()){
				//System.out.println("********** getOppositionOperation Boolean.TRUE ***********");
				// Fermeture de la cnx
				CloseStream.fermeturesStream(rs);
				//if(conCBS != null ) conCBS.close();
				return Boolean.TRUE;
			}
		}

		//System.out.println("********** getOppositionOperation Boolean.FALSE ***********");
		return Boolean.FALSE;
	}



	public boolean isClientEmploye(String numClient) {

		boolean res = false;

		try {

			// Initialisation de DataStore d'Amplitude
			//if(dsCBS == null) findCBSDataSystem();
			//ResultSet rs = executeFilterSystemQuery(dsCBS, "select pro from bkprocli where cli='"+ numClient +"' order by dpro desc", null);
			
			if(dsAIF == null) findAIFDataSystem();
			if(dsAIF != null && StringUtils.isNotBlank(dsAIF.getDbConnectionString())) {
				
				HttpGet getRequest = new HttpGet(dsAIF.getDbConnectionString()+"/customer/customerdetails/"+numClient);
			    getRequest.setHeader("content-type", "application/json");
			    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(getRequest);
			    HttpEntity entity = null;
			    entity = response.getEntity();
			 
			    if(entity != null) {
			    	 
			    	 List<Client> listClient = new ArrayList<>();
			    	 String content = EntityUtils.toString(entity);
					 JSONObject json = new JSONObject(content);
					
					 ResponseDataClient responseDataClt = Shared.mapToResponseDataClient(json);
					 String responseCode = responseDataClt.getCode();
					
					 
					 if ("200".equals(responseCode)) {
						 
						 listClient = responseDataClt.getDatas();
						 if(!listClient.isEmpty()) {
							 Client client = listClient.get(0);
							 
							 if("110".equals(client.getCodeProfil())) {
								 res = true;
							 }
						 }
					 } 
			     }
			}

		} catch(Exception e){}

		return res;

	}



	//@SuppressWarnings("unused")  
	private Transaction getTransactionFromRequestMessage(RequestMessage message) throws Exception, OgMoException {

		// Test du montant
		//System.out.println("**********  new Date() ***********" + new Date());
		System.out.println("********** TR TEST 1 ***********");
		if( (message.getOperation().equals(TypeOperation.Account2Wallet) || message.getOperation().equals(TypeOperation.Wallet2Account)) &&  ( message.getAmount() == null || message.getAmount() <= 0 ) ) 
			throw new OgMoException(ExceptionCode.SubscriberInvalidAmount, ExceptionCode.SubscriberInvalidAmount.getValue(), ExceptionCategory.SUBSCRIBER);

		//Subscriber subs = findSubscriberFromPhoneNumber(message.getPhoneNumber());
		Subscriber subs = message.getSubscriber();

		// test de l'abonne
		System.out.println("********** TR TEST 2 ***********");
		if(subs == null){
			// Message d'avertissement
			throw new OgMoException(ExceptionCode.SubscriberInvalidPIN, ExceptionCode.SubscriberInvalidPIN.getValue(), ExceptionCategory.SUBSCRIBER);
		}

		// Test du numero de telephone
		if( subs.getStatus().equals(StatutContrat.WAITING) ) {

			// Message d'avertissement
			throw new OgMoException(ExceptionCode.SubscriberSuspended, ExceptionCode.SubscriberSuspended.getValue(), ExceptionCategory.SUBSCRIBER);
		}

		// Test du numero de telephone
		if(subs.getStatus().equals(StatutContrat.SUSPENDU)){
			// Message d'avertissement
			throw new OgMoException(ExceptionCode.SubscriberSuspended, ExceptionCode.SubscriberSuspended.getValue(), ExceptionCategory.SUBSCRIBER);
		}

		// test de l'abonne
		if(subs.getFirstPhone() == null || subs.getFirstPhone().isEmpty()){
			// Message d'avertissement
			throw new OgMoException(ExceptionCode.SubscriberInvalidPhone, ExceptionCode.SubscriberInvalidPhone.getValue(), ExceptionCategory.SUBSCRIBER);
		}

		// test du PIN   Encrypter.getInstance().encryptText(  )
		if(!subs.getBankPIN().equals(message.getBankPIN())){
			// Message d'avertissement
			throw new OgMoException(ExceptionCode.SubscriberInvalidPIN, ExceptionCode.SubscriberInvalidPIN.getValue(), ExceptionCategory.SUBSCRIBER);
		}

		// Lecture du compte
		if(message.getAccount() == null || message.getAccount().trim().isEmpty()) message.setAccount( subs.getAccounts().get(0).trim() );

		// Lecture du TElephone
		if(message.getPhoneNumber() == null || message.getPhoneNumber().trim().isEmpty()) message.setPhoneNumber(subs.getPhoneNumbers().get(0).trim());

		// Test de l'existence du compte 
		if( isCompteFerme(message.getAccount())){
			// Message d'avertissement
			throw new OgMoException(ExceptionCode.BankClosingAccount, ExceptionCode.BankClosingAccount.getValue(), ExceptionCategory.METIER);
		}

		Parameters params = findParameters();

		// Si c'est un retrait (verification du solde)	
		if(message.getOperation().equals(TypeOperation.Account2Wallet)){

			// Test de vérification du blocage en débit
			System.out.println("********** isBlocageDebit(message.getAccount(),params.getCodeOpe(),null) ***********");
			if( isBlocageDebit(message.getAccount(),params.getCodeOpe(),null)){
				// Message d'avertissement
				throw new OgMoException(ExceptionCode.BankBlockingDebitAccount, ExceptionCode.BankBlockingDebitAccount.getValue(), ExceptionCategory.METIER);
			}

			// Test du solde du compte  if(isSoldeSuffisant(message.getAccount(), message.getAmount())){
			if(getSolde(message.getAccount(), message.getAmount(),TypeOperation.TRANSACTION) < 0){
				// Message d'avertissement
				throw new OgMoException(ExceptionCode.BankInsufficientBalance, ExceptionCode.BankInsufficientBalance.getValue(), ExceptionCategory.METIER);
			}

			if(isTFJOPortalEnCours() || isModeNuit()) {
				List<Transaction> trans = checkTransactionSubscriber(subs);
				if(!trans.isEmpty() && trans.size() > 0){
					try {
						if(StringUtils.isNotBlank(trans.get(0).getLastSolde()) && message.getAmount().compareTo(Double.valueOf(trans.get(0).getLastSolde())) < 0){
							// Message d'avertissement
							throw new OgMoException(ExceptionCode.BankInsufficientBalance, ExceptionCode.BankInsufficientBalance.getValue(), ExceptionCategory.METIER);
						}
					} catch(Exception ex) {
					}
				}				
			}


			/*
			Double solde = getSoldeCompte(message.getAccount());
			if(message.getAmount() > solde) throw new OgMoException(ExceptionCode.BankInsufficientBalance, ExceptionCode.BankInsufficientBalance.getValue(), ExceptionCategory.METIER);
			 */



			// Si le montant de la transaction est superieur au montant max on leve une exception
			//if(param.getMaxAWAmount() > 0 && message.getAmount() > param.getMaxAWAmount()) throw new OgMoException(ExceptionCode.BankExceededAmount, ExceptionCode.BankExceededAmount.getValue(), ExceptionCategory.SUBSCRIBER);

			System.out.println("********** TEST 3 ***********");
			if(Boolean.TRUE.equals(subs.isMerchant())){
				ProfilMarchands plg = subs.getProfil();
				System.out.println("*******ProfilMarchands***********"+plg);
				if(plg != null){
					System.out.println("********** TEST 4 ***********");
					if(message.getAmount() > plg.getMaxPullAmount()) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountTransaction, ExceptionCode.SubscriberInvalidAmountTransaction.getValue(), ExceptionCategory.SUBSCRIBER);

					// plafond Journalier
					System.out.println("********** TEST 5 ***********");
					if(Boolean.FALSE.equals(subs.islimit(message.getOperation(),message.getAmount(),plg.getMaxPullAmountDay()))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountDay, ExceptionCode.SubscriberInvalidAmountDay.getValue(), ExceptionCategory.SUBSCRIBER);

					// plafond Hebdomadaire
					System.out.println("********** TEST 6 ***********");
					if(Boolean.FALSE.equals(subs.islimitWeekHebd(message.getOperation(),message.getAmount(),plg.getMaxPullAmountWeek(), "W"))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountWeek, ExceptionCode.SubscriberInvalidAmountWeek.getValue(), ExceptionCategory.SUBSCRIBER);

					// plafond Mensuel
					System.out.println("********** TEST 7 ***********");
					if(Boolean.FALSE.equals(subs.islimitWeekHebd(message.getOperation(),message.getAmount(),plg.getMaxPullAmountMonth(), "M"))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountMonth, ExceptionCode.SubscriberInvalidAmountMonth.getValue(), ExceptionCategory.SUBSCRIBER);

					System.out.println("********** OK AFTER TEST 7 ***********");

				}else{

					System.out.println("********** TEST 8 ***********");
					if(message.getAmount() > params.getMaxAWAmount()) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountTransaction, ExceptionCode.SubscriberInvalidAmountTransaction.getValue(), ExceptionCategory.SUBSCRIBER);

					// plafond Journalier
					System.out.println("********** TEST 9 ***********");
					if(Boolean.FALSE.equals(subs.islimit(message.getOperation(),message.getAmount(),params.getMaxAWAmountDays()))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountDay, ExceptionCode.SubscriberInvalidAmountDay.getValue(), ExceptionCategory.SUBSCRIBER);

					// plafond Hebdomadaire
					System.out.println("********** TEST 10 ***********");
					if(Boolean.FALSE.equals(subs.islimitWeekHebd(message.getOperation(),message.getAmount(),params.getMaxAWAmountWeeks(), "W"))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountWeek, ExceptionCode.SubscriberInvalidAmountWeek.getValue(), ExceptionCategory.SUBSCRIBER);

					// plafond Mensuel
					System.out.println("********** TEST 11 ***********");
					if(Boolean.FALSE.equals(subs.islimitWeekHebd(message.getOperation(),message.getAmount(),params.getMaxAWAmountMonths(), "M"))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountMonth, ExceptionCode.SubscriberInvalidAmountMonth.getValue(), ExceptionCategory.SUBSCRIBER);

					System.out.println("********** OK AFTER TEST 11 ***********");

				}
			}else{
				System.out.println("********** TEST 12 ***********");
				if( message.getAmount() > params.getMaxAWAmount()) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountTransaction, ExceptionCode.SubscriberInvalidAmountTransaction.getValue(), ExceptionCategory.SUBSCRIBER);

				// plafond Journalier
				System.out.println("********** TEST 13 ***********");
				if(Boolean.FALSE.equals(subs.islimit(message.getOperation(),message.getAmount(),params.getMaxAWAmountDays()))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountDay, ExceptionCode.SubscriberInvalidAmountDay.getValue(), ExceptionCategory.SUBSCRIBER);

				// plafond Hebdomadaire
				System.out.println("********** TEST 14 ***********");
				if(Boolean.FALSE.equals(subs.islimitWeekHebd(message.getOperation(),message.getAmount(),params.getMaxAWAmountWeeks(), "W"))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountWeek, ExceptionCode.SubscriberInvalidAmountWeek.getValue(), ExceptionCategory.SUBSCRIBER);

				// plafond Mensuel
				System.out.println("********** TEST 15 ***********");
				if(Boolean.FALSE.equals(subs.islimitWeekHebd(message.getOperation(),message.getAmount(),params.getMaxAWAmountMonths(), "M"))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountMonth, ExceptionCode.SubscriberInvalidAmountMonth.getValue(), ExceptionCategory.SUBSCRIBER);

				System.out.println("********** OK AFTER TEST 15 ***********");

			}

		}else if(message.getOperation().equals(TypeOperation.Wallet2Account)){

			// Test de vérification du blocage en crédit
			System.out.println("********** isBlocageCredit(message.getAccount()) *********** : " + isBlocageCredit(message.getAccount()));
			if( isBlocageCredit(message.getAccount())){
				// Message d'avertissement
				throw new OgMoException(ExceptionCode.BankBlockingCreditAccount, ExceptionCode.BankBlockingCreditAccount.getValue(), ExceptionCategory.METIER);
			}


			//Parameters params = findParameters();
			// Si le montant de la transaction est superieur au montant max on leve une exception
			//if(param.getMaxWAAmount() > 0 && message.getAmount() > param.getMaxWAAmount()) throw new OgMoException(ExceptionCode.BankExceededAmount, ExceptionCode.BankExceededAmount.getValue(), ExceptionCategory.SUBSCRIBER);

			if(Boolean.TRUE.equals(subs.isMerchant())){
				ProfilMarchands plg = subs.getProfil();
				System.out.println("*******ProfilMarchands***********"+plg);
				System.out.println("********** TEST 16 ***********");
				if(plg != null){

					//AJOUT***** Test du solde du compte Orange  if(isSoldeSuffisant(message.getAccount(), message.getAmount())){
					/* ***
					if(getSolde(params.getNcpCliOrange(), message.getAmount(),TypeOperation.TRANSACTION) < 0){
						// Message d'avertissement
						throw new OgMoException(ExceptionCode.BankInsufficientBalance, ExceptionCode.BankInsufficientBalance.getValue(), ExceptionCategory.METIER);
					}
					 */

					// plafond par transaction
					System.out.println("********** TEST 17 ***********");
					if( message.getAmount() > plg.getMaxPushAmount()) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountTransaction, ExceptionCode.SubscriberInvalidAmountTransaction.getValue(), ExceptionCategory.SUBSCRIBER);

					// plafond Journalier
					System.out.println("********** TEST 18 ***********");
					if(Boolean.FALSE.equals(subs.islimit(message.getOperation(),message.getAmount(), plg.getMaxPushAmountDay()))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountDay, ExceptionCode.SubscriberInvalidAmountDay.getValue(), ExceptionCategory.SUBSCRIBER);

					// plafond Hebdomadaire
					System.out.println("********** TEST 19 ***********");
					if(Boolean.FALSE.equals(subs.islimitWeekHebd(message.getOperation(),message.getAmount(), plg.getMaxPushAmountWeek(), "W"))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountWeek, ExceptionCode.SubscriberInvalidAmountWeek.getValue(), ExceptionCategory.SUBSCRIBER);

					// plafond Mensuel
					System.out.println("********** TEST 20 ***********");
					if(Boolean.FALSE.equals(subs.islimitWeekHebd(message.getOperation(),message.getAmount(), plg.getMaxPushAmountMonth(), "M"))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountMonth, ExceptionCode.SubscriberInvalidAmountMonth.getValue(), ExceptionCategory.SUBSCRIBER);

					System.out.println("********** OK AFTER TEST 20 ***********");

				}else{

					// plafond par transaction
					System.out.println("********** TEST 21 ***********");
					if( message.getAmount() > params.getMaxWAAmount()) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountTransaction, ExceptionCode.SubscriberInvalidAmountTransaction.getValue(), ExceptionCategory.SUBSCRIBER);

					// plafond Journalier
					System.out.println("********** TEST 22 ***********");
					if(Boolean.FALSE.equals(subs.islimit(message.getOperation(),message.getAmount(),params.getMaxWAAmountDays()))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountDay, ExceptionCode.SubscriberInvalidAmountDay.getValue(), ExceptionCategory.SUBSCRIBER);

					// plafond Hebdomadaire
					System.out.println("********** TEST 23 ***********");
					if(Boolean.FALSE.equals(subs.islimitWeekHebd(message.getOperation(),message.getAmount(),params.getMaxWAAmountWeeks(), "W"))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountWeek, ExceptionCode.SubscriberInvalidAmountWeek.getValue(), ExceptionCategory.SUBSCRIBER);

					// plafond Mensuel
					System.out.println("********** TEST 24 ***********");
					if(Boolean.FALSE.equals(subs.islimitWeekHebd(message.getOperation(),message.getAmount(),params.getMaxWAAmountMonths(), "M"))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountMonth, ExceptionCode.SubscriberInvalidAmountMonth.getValue(), ExceptionCategory.SUBSCRIBER);

					System.out.println("********** OK AFTER TEST 24 ***********");

				}
			}else{

				// plafond par transaction
				System.out.println("********** TEST 25 ***********");
				if(message.getAmount() > params.getMaxWAAmount()) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountTransaction, ExceptionCode.SubscriberInvalidAmountTransaction.getValue(), ExceptionCategory.SUBSCRIBER);

				// plafond Journalier
				System.out.println("********** TEST 26 ***********");
				if(Boolean.FALSE.equals(subs.islimit(message.getOperation(),message.getAmount(),params.getMaxWAAmountDays()))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountDay, ExceptionCode.SubscriberInvalidAmountDay.getValue(), ExceptionCategory.SUBSCRIBER);

				// plafond Hebdomadaire
				System.out.println("********** TEST 27 ***********");
				if(Boolean.FALSE.equals(subs.islimitWeekHebd(message.getOperation(),message.getAmount(),params.getMaxWAAmountWeeks(), "W"))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountWeek, ExceptionCode.SubscriberInvalidAmountWeek.getValue(), ExceptionCategory.SUBSCRIBER);

				// plafond Mensuel
				System.out.println("********** TEST 28 ***********");
				if(Boolean.FALSE.equals(subs.islimitWeekHebd(message.getOperation(),message.getAmount(),params.getMaxWAAmountMonths(), "M"))) throw new OgMoException(ExceptionCode.SubscriberInvalidAmountMonth, ExceptionCode.SubscriberInvalidAmountMonth.getValue(), ExceptionCategory.SUBSCRIBER);

				System.out.println("********** OK AFTER TEST 28 ***********");

			}

		}


		/*
		// Initialisation de la transaction
		Transaction trx = new Transaction(message.getOperation(), subs, message.getAmount(), message.getAccount(), message.getPhoneNumber(),message);

		// Tout est OK
		return trx;
		 */


		//logger.info("[MoMo] : TEST PARAM POUR RETRAITEMENT");
		// Si le mode nuit est active et l'etat des tfjo active



		/* Bloc commenté le 16-09-2020
		if(isTFJOPortalEnCours() && isModeNuit()) {
			//logger.info("[MoMo] : PARAM RETRAITEMENT OK!");
			// Desactivation du verrou des tfjo 
			setTFJOPortalEnCours(Boolean.FALSE);

			// Retraiter les transactions executes entre le lancement des TFJO Portal et l'activation du mode nuit Amplitude
			//RetraiterTransactionWorker.runChecking();
			processRetraiterTransactions();
		}

		// Initialisation de la transaction
		Transaction trx = new Transaction(message.getOperation(), subs, message.getAmount(), message.getAccount(), message.getPhoneNumber(),message);


		// Log
		//logger.info("NEW TRANSACTION ");

		// Sauvegarde du contexte de la transaction
		trx.setTfjoLance(isTFJOPortalEnCours());
		trx.setModeNuitAmplitudeActive(isModeNuit());

		// Marquer la transaction a retraiter si les TFJO Portal sont en cours et le mode nuit d'Amplitude non active
		if(isTFJOPortalEnCours() && !isModeNuit()) trx.setaRetraiter(Boolean.TRUE);

		 */

		// Log
		//logger.info("Tfjo Lance : "+trx.getTfjoLance());
		// Log
		//logger.info("Mode Nuit Amplitude Active : "+trx.getModeNuitAmplitudeActive());
		// Log
		//logger.info("A Retraiter : "+trx.getARetraiter());


		// Initialisation de la transaction
		Transaction trx = new Transaction(message.getOperation(), subs, message.getAmount(), message.getAccount(), message.getPhoneNumber(),message);


		// Tout est OK
		return trx;


	}


	public  void processComplet(List<Transaction> trans) throws Exception{

		// Traite le message 
		for(Transaction tran : trans){
			if(TransactionStatus.FAILED.equals(tran.getCompletStatus())){
				// Annulation de la Transactin cote Bank
				// Initialisation de la map a retourner
				Map<String, String> map = new HashMap<String, String>();
				map = processReversalTransaction(tran.getId().toString());
				if(map.get("RespoonceCode").equalsIgnoreCase("000")){
					tran.setCompleted(Boolean.TRUE);
				}
			}else if(TransactionStatus.SUCCESS.equals(tran.getCompletStatus())){
				// posting dans le compte de Orange 

				tran.setCompleted(Boolean.TRUE);
			}

		}

	}


	/**
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 * @throws OgMoException
	 */
	//@Override
	public synchronized Transaction processA2WW2AMessage(RequestMessage message) throws Exception, OgMoException {

		// Traite le message 
		System.out.println("********************* processA2WW2AMessage(RequestMessage message) **********************");
		return posterEvenementDansCoreBanking( getTransactionFromRequestMessage(message) );

	}


	public boolean annulerEvenement(DataSystem dsCBS,String querry) throws Exception {
		// MAJ de l'etat des evenements dans le Core Banking
		executeUpdateSystemQuery(dsCBS, querry,null);
		return true;		
	}


	public boolean annulerMAJSoldeIndicatif(bkeve eve) throws Exception {
		// MAJ des soldes indicatifs des cptes debiteurs et crediteurs

		List<Queries> query = new ArrayList();

		try {
			if(isModeNuit()){

				// MAJ du solde indicatif du Compte Debiteur  orig='WEB' , 'MON' ;  flag=''
				query.add(new Queries("update bksin set mon=0 where age= ? and  dev=? and  ncp = ? and  suf=? and  mon=? and  orig=? and  flag=? ", new Object[]{ eve.getAge1(), eve.getDev1(), eve.getNcp1(), eve.getSuf1(), -eve.getMon1(), "WEB", "O" }));
				//*** MAJ executeUpdateSystemQuery(dsCBS, "update bksin set mon=0 where age= ? and  dev=? and  ncp = ? and  suf=? and  mon=? and  orig=? and  flag=? ", new Object[]{ eve.getAge1(), eve.getDev1(), eve.getNcp1(), eve.getSuf1(), -eve.getMon1(), "WEB", "O" });

				// MAJ du solde indicatif du compte Crediteur
				query.add(new Queries("update bksin set mon=0 where age= ? and  dev=? and  ncp = ? and  suf=? and  mon=? and  orig=? and  flag=? ", new Object[]{ eve.getAge2(), eve.getDev2(), eve.getNcp2(), eve.getSuf2(), eve.getMon2(), "WEB", "O" }));
				//*** MAJ executeUpdateSystemQuery(dsCBS, "update bksin set mon=0 where age= ? and  dev=? and  ncp = ? and  suf=? and  mon=? and  orig=? and  flag=? ", new Object[]{ eve.getAge2(), eve.getDev2(), eve.getNcp2(), eve.getSuf2(), eve.getMon2(), "WEB", "O" });

				executeUpdateSystemQuery(dsCBS, query);

				return true;
			} 
			else {
				query.add(new Queries("update bkcom set sin=sin-"+ eve.getMnt2() +" where age='"+ eve.getAge2() +"' and ncp='"+ eve.getNcp2() +"' and clc='"+ eve.getClc2() +"' ", null));
				//*** MAJ executeUpdateSystemQuery(dsCBS, "update bkcom set sin=sin-"+ eve.getMnt2() +" where age='"+ eve.getAge2() +"' and ncp='"+ eve.getNcp2() +"' and clc='"+ eve.getClc2() +"' ", null);

				query.add(new Queries("update bkcom set sin=sin+"+ eve.getMnt1() +" where age='"+ eve.getAge1() +"' and ncp='"+ eve.getNcp1() +"' and clc='"+ eve.getClc1() +"' ", null));
				//*** MAJ executeUpdateSystemQuery(dsCBS, "update bkcom set sin=sin+"+ eve.getMnt1() +" where age='"+ eve.getAge1() +"' and ncp='"+ eve.getNcp1() +"' and clc='"+ eve.getClc1() +"' ", null);
				executeUpdateSystemQuery(dsCBS, query);

				return true;
			}

		} catch (Exception e) {

			return false;
		}

	}




	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#posterEvenementDansCoreBanking(com.afb.dpd.mobilemoney.jpa.tools.bkeve)
	 */
	@Override
	public synchronized Transaction posterEvenementDansCoreBanking(Transaction transaction) throws Exception, OgMoException {

		if(transaction == null){
			throw new OgMoException(ExceptionCode.BankException, ExceptionCode.BankException.getValue(), ExceptionCategory.METIER);
		}

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();

		// Si les TFJ sont en cours, 
		/** if(isTfjEnCours()){
			try{
				throw new OgMoException(ExceptionCode.BankEndOfDay, ExceptionCode.BankEndOfDay.getValue(), ExceptionCategory.METIER);
			}catch(OgMoException e){
				return null;
			}
		}*/

		params = findParameters();

		// Contruction de l'evenement liee a la transaction
		bkeve eve = buildEvenement(transaction);

		if(eve == null){
			throw new OgMoException(ExceptionCode.BankException, ExceptionCode.BankException.getValue(), ExceptionCategory.METIER);
		}
		if(eve != null){

			if(!transaction.getTypeOperation().equals(TypeOperation.Wallet2Account)){

				Double sous = transaction.getAmount(); 
				if(!transaction.getTypeOperation().equals(TypeOperation.Account2Wallet)){
					sous = transaction.getCommissions(); 
				}

				Double amount = getSolde(transaction.getAccount(), sous,TypeOperation.TRANSACTION)-sous;

				if(amount < 0 ){
					if(transaction.getTypeOperation().equals(TypeOperation.CANCEL) || transaction.getTypeOperation().equals(TypeOperation.Account2Wallet)){
						throw new OgMoException(ExceptionCode.BankInsufficientBalance, ExceptionCode.BankInsufficientBalance.getValue(), ExceptionCategory.METIER);
					}else if( transaction.getTypeOperation().equals(TypeOperation.BALANCE)){
						throw new OgMoException(ExceptionCode.BankInsufficientBalance, ExceptionCode.BankInsufficientBalance.getValue(), ExceptionCategory.METIER);
					}else if( transaction.getTypeOperation().equals(TypeOperation.MiniStatement)){
						throw new OgMoException(ExceptionCode.BankInsufficientBalance, ExceptionCode.BankInsufficientBalance.getValue(), ExceptionCategory.METIER);
					}
				}

			}else{

				String cha = "";
				String numCompte = transaction.getAccount();
				//ResultSet rs = executeFilterSystemQuery(dsCBS, "select cha from bkcom where dev='001' and age = ? and ncp = ? and clc = ? ", new Object[]{ numCompte.split("-")[0], numCompte.split("-")[1], numCompte.split("-")[2] });
				
				Bkcom compte = accountInfos(numCompte);
				if(compte != null){
					cha = compte.getCha();
				}
				
				// Contorle Chapitre 
				if(CheckChapiter(cha).equals(Boolean.FALSE)){
					//return 0d;
					throw new OgMoException(ExceptionCode.BankBlockingDebitAccount, ExceptionCode.BankBlockingDebitAccount.getValue(), ExceptionCategory.SUBSCRIBER);
				}

			}


			logger.info("EVE NON NULL");

			//Envoi de l'evenement vers l'API
			bkeve _eve = registerEventToCoreBanking(eve);

			if(_eve != null) {
				logger.info("[OGMO - Register Event Response] : "+_eve.getEve()+" ID: "+_eve.getE_id());

				try {
					eve.setEve( OgMoHelper.padText(String.valueOf(_eve.getEve()), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0") );
					eve.setE_id(_eve.getE_id());
					eve.setId(Long.parseLong(eve.getE_id()));
					eve.getTransaction().setStatus(TransactionStatus.SUCCESS);
					eve = dao.save(eve);

				}catch (Exception e){
					logger.info("[OGMO CONTROLE RESERVATION ERROR] : SAVE TRX EXCEPTION --> CANCEL EVE & MAJ SOLDE");
					// MAJ de l'etat des evenements dans le Core Banking (annulation)
					// Annulation de la maj du solde indicatif
					logger.info("[OGMO - Register] : "+eve.getEve());
					bkeve ev = reversalEventsCoreBanking(""+eve.getId());
					logger.info("[OGMO - Reversal - ETA] : "+ev.getEta()+" ETAP: "+ev.getEtap());
					e.printStackTrace();
					
					ev.setEve( OgMoHelper.padText(String.valueOf(_eve.getEve()), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0") );
					ev.setE_id(_eve.getE_id());
					ev.setId(Long.parseLong(ev.getE_id()));
					ev.getTransaction().setStatus(TransactionStatus.FAILED);
					ev = dao.save(ev);
				}

			}
			else {
				//*** return transaction;
				return null;
			}


		}

		return eve != null ? eve.getTransaction() : transaction;

	}



	public synchronized void processCompleteTransaction(String id) throws Exception, OgMoException {

		if(dsCBS == null) findCBSDataSystem();

		Long txID = Long.valueOf(id);
		List<bkeve> eves = dao.filter(bkeve.class, AliasesContainer.getInstance().add("transaction", "transaction"), RestrictionsContainer.getInstance().add(Restrictions.eq("transaction.id", txID)), null, null, 0, -1);
		if(eves == null || eves.isEmpty()){
			return ;
		}

		// Recuperation de l'evenement genere par la transaction
		bkeve eve = eves.get(0);


		if(eve != null){

			Transaction transaction = eve.getTransaction();

			// Calcul de la table des evenements a utiliser
			String majEve = "update BKEVE set eta='IG', etap='VA' where eve = "+ eve.getEve() +" and ope= "+eve.getOpe()+" ";
			boolean modeNuit = isModeNuit();
			boolean evePosted = false;
			String tableEvt = "BKEVE";
			String tableOpe = "bkope";  
			if(modeNuit){
				//tableEvt = "SYN_BKEVE";
				tableEvt = "bkeve_eod";
				tableOpe = "syn_bkope";
			}			

			// Si les TFJ ne sont pas lances, on poste l'evenement dans Amplitude 
			// Enregistrement de l'evenement dans Amplitude
			if(transaction.getTypeOperation().equals(TypeOperation.Wallet2Account)){
				if(modeNuit == false){

					// MAJ du solde indicatif crediteur
					executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(5).getQuery(), new Object[]{transaction.getAmount(), eve.getAge2(), eve.getNcp2(), eve.getClc2()  });

					// MAJ du solde indicatif du compte debiteur
					executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(4).getQuery(), new Object[]{ transaction.getAmount() , eve.getAge1(), eve.getNcp1(), eve.getClc1() } );

				}else{

					// MAJ du solde indicatif du Compte Debiteur  orig='WEB' , 'MON' ;  flag=''
					executeUpdateSystemQuery(dsCBS, "insert into bksin (age, dev, ncp, suf, mon, orig, flag) values (?, ?, ?, ?, ?, ?, ?) ", new Object[]{ eve.getAge1(), eve.getDev1(), eve.getNcp1(), eve.getSuf1(), -eve.getMon1(), "WEB", "O" });

					// MAJ du solde indicatif du compte Crediteur
					executeUpdateSystemQuery(dsCBS, "insert into bksin (age, dev, ncp, suf, mon, orig, flag) values (?, ?, ?, ?, ?, ?, ?) ", new Object[]{ eve.getAge2(), eve.getDev2(), eve.getNcp2(), eve.getSuf2(), eve.getMon2(), "WEB", "O" });

					// MAJ du dernier numero d'evenement utilise pour le type operation
					executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(3).getQuery().replaceAll("bkope", tableOpe), new Object[]{ Long.valueOf(eve.getEve()), eve.getOpe() });

				}
			}

			// Enregistrement de l'evenement dans Amplitude
			try{
				executeUpdateSystemQuery(dsCBS, eve.getSaveQuery().replaceAll("BKEVE", tableEvt), eve.getQueryValues());
			}catch (Exception e){
				annulerEvenement(dsCBS,majEve.replaceAll("BKEVE", tableEvt));
				e.printStackTrace();
			}

			// Check Bkeve
			try{
				ResultSet rsa = executeFilterSystemQuery(dsCBS, eve.getCheckQuery().replaceAll("BKEVE", tableEvt), eve.getQueryCheckValues());
				if(rsa.next()){
					evePosted = true;
				}else{
					throw new OgMoException(ExceptionCode.BankException, ExceptionCode.BankException.getValue(), ExceptionCategory.METIER);
				}
				// Fermeture de la cnx
				CloseStream.fermeturesStream(rsa);
			}catch (Exception e){
				e.printStackTrace();
				if(evePosted)annulerEvenement(dsCBS,majEve.replaceAll("BKEVE", tableEvt));
				throw new OgMoException(ExceptionCode.BankException, ExceptionCode.BankException.getValue(), ExceptionCategory.METIER);
			}

			//if(conCBS != null ) conCBS.close();

			// MAJ du statut de la transaction
			eve.getTransaction().setStatus(TransactionStatus.SUCCESS);
			eve.getTransaction().setCompleted(Boolean.TRUE);

			// MAJ du Compte de Facturation
			eve.getTransaction().getSubscriber().setAccFact(transaction.getAccount());

			// Sauvegarde l'evenement
			eve = dao.save(eve);

			transaction = eve.getTransaction();

		} 

		//else transaction.setStatus(TransactionStatus.SUCCESS);
		//return eve != null ? eve.getTransaction() : dao.save(transaction);
		//return transaction;

	}

	/**
	 * Envoi du PIN banque du souscripteur par SMS
	 * @param subs
	 * @throws Exception
	 */
	@Override
	@AllowedRole(name = "sendCodePINBySMS", displayName = "OgMo.Send.CodePIN.By.SMS")
	public void sendCodePINBySMS(Subscriber subs) {

		// Si le souscripteur est null on sort
		if(subs == null) return;

		// Envoi du sms
		// sendSMS("Cher Mr/Mme "+subs.getCustomerName()+" Votre Votre souscription aux opérations Orange Money est affective ", subs.getPhoneNumbers().get(0));

	}

	public void sendSMS(String message, String phoneNumber) {

		try{

			// Demarrage du service Facade du portail
			//IFacadeManagerRemote portalFacadeManager = (IFacadeManagerRemote) new InitialContext().lookup( PortalHelper.APPLICATION_EAR.concat("/").concat( IFacadeManagerRemote.SERVICE_NAME ).concat("/remote") );
			//portalFacadeManager.sendSMSViaLMT( new SMSWeb("OGMo-01", "Orange Money", "AUTO", message, phoneNumber) );
			dao.save(new SMSWeb("OGMo-01", "Orange Money", "AUTO", message, phoneNumber) );

		}catch(Exception e){e.printStackTrace();}

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#getECFromTransactions(java.util.List)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@AllowedRole(name = "getECFromTransactions", displayName = "OgMo.Consulter.Ecritures.Comptables")
	public List<bkmvti> getECFromTransactions(List<Transaction> transactions, boolean poster) {

		//System.out.println("********** getECFromTransactions 1 ***********");
		List<bkmvti> values = new ArrayList<bkmvti>();


		// S'il n'ya aucune transaction on sort
		if(transactions == null || transactions.isEmpty()) return null;

		// Initialisation du filtre
		String in = "";

		// Recuperation  de l'id de chaque transaction selectionnee
		for(Transaction t : transactions) if(t.isSelected() && t.getStatus().equals(TransactionStatus.SUCCESS) && (poster ? !t.getPosted().booleanValue() : true) ) { in += t.getId() + ", "; }

		// Construction du filtre sur les transactions
		if(!in.isEmpty()) in = "(" + in.substring(0, in.length()-2) + ")";

		// Si aucune transaction n'a ete selectionnee on sort
		if(in.isEmpty()) return null;

		// Execute et retourne la liste des ecritures comptables des transactions selectionnees
		values.addAll(dao.getEntityManager().createQuery("Select e.ecritures from bkeve e where e.transaction.id in "+ in +"").getResultList());

		return values;
	}



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#extractECFromSelectedTransactionsIntoFile(java.util.List)
	 */
	@Override
	@AllowedRole(name = "extractECFromSelectedTransactionsIntoFile", displayName = "OgMo.Export.Ecritures.Comptable.Into.File")
	public List<bkmvti> extractECFromSelectedTransactionsIntoFile(List<Transaction> transactions, String fileName) throws Exception {

		// Recuperation de la liste des ecritures comptables des transactions selectionnees
		List<bkmvti> mvts = getECFromTransactions(transactions, false);


		List<bkmvti> ecritures = genererEcritureTFJOs(transactions);
		mvts.addAll(ecritures);

		// S'il n'existe aucune ecriture on sort
		if(mvts == null || mvts.isEmpty()) return null;

		// Initialisation du fichier a generer
		FileWriter fw = new FileWriter(fileName);

		// Parcours des ecritures
		for(bkmvti mvt : mvts) {

			// Ecriture de la ligne dans le fichier
			fw.write(mvt.getFileLine().concat("\n"));

		}

		// Fermeture du fichier
		fw.flush(); fw.close();

		return ecritures;
	}





	//@SuppressWarnings("rawtypes")
	public void annulerEvenements(List<Transaction> transactions, DataSystem dsCBS) throws Exception {

		if(transactions == null || transactions.isEmpty()) return;

		// Initialisation du filtre des transactions
		String in = "";

		// Recuperation  de l'id de chaque transaction selectionnee dans le filtre
		for(Transaction t : transactions) if(t.isSelected()) { in += t.getId() + ", "; }

		// Construction du filtre sur les transactions
		if(!in.isEmpty()) in = "(" + in.substring(0, in.length()-2) + ")";

		// Si aucune transaction n'a ete selectionnee on sort
		if(in.isEmpty()) return;

		// Recherche des code evenements et codes operations des transactions selectionnees
		List eves = dao.getEntityManager().createQuery("Select e.eve, e.ope From bkeve e where e.transaction.id in "+ in +"").getResultList();

		// Si aucun evenement trouve on sort
		if(eves == null || eves.isEmpty()) return;

		// Initialisation du filtre des evenements et du code operation
		String inEve = ""; String ope = "";

		for(Object o : eves) {
			inEve += "'".concat( ((Object[])o)[0].toString()  ).concat("', ");
			if(ope.isEmpty()) ope = ((Object[])o)[1].toString();
		}

		// Construction du filtre des evenements a mettre a jour dans Delta
		if(!inEve.isEmpty()) inEve = "(".concat(inEve.substring(0, inEve.length()-2)).concat(")");

		// MAJ de l'etat des evenements dans le Core Banking
		executeUpdateSystemQuery(dsCBS, "update bkeve set eta='IG', etap='VA' where eve in "+ inEve +" and ope=? ", new Object[]{ ope  } );

		// MAJ de l'etat des evenements dans le Core Banking
		// executeUpdateSystemQuery(dsCBS, "update bkeve set eta='IG', etap='VA' where eta != 'IG' and ope=? ", new Object[]{ ope  } );

		// MAJ des transactions
		dao.getEntityManager().createQuery("Update Transaction t set t.posted=:posted where t.id in "+ in +"").setParameter("posted", Boolean.TRUE).executeUpdate();
	}



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#posterTransactionsDansCoreBanking(java.util.List)
	 */
	@Override
	@AllowedRole(name = "posterTransactionsDansCoreBanking", displayName = "OgMo.Poster.Ecritures.Comptable.Into.Delta")
	public void posterTransactionsDansCoreBanking(List<Transaction> transactions) throws Exception {

		System.out.println("*************** posterTransactionsDansCoreBanking***************");
		Subscriber subs = null;
		if(!transactions.isEmpty()){
			//System.out.println("********** posterTransactionsDansCoreBanking 2 ***********");
			subs = transactions.get(0).getSubscriber();
		}

		// Recuperation de la liste des ecritures comptables des transactions selectionnees
		List<bkmvti> mvts = getECFromTransactions(transactions, true);

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();
		Date dco = getDateComptable();


		// Parcours des ecritures
		if(conCBS == null || conCBS.isClosed()) conCBS = getSystemConnection(dsCBS);

		// Suspension temporaire du mode blocage dans la BD du Core Banking
		if(dsCBS.getDbConnectionString().indexOf("informix") > 0) conCBS.createStatement().executeUpdate("SET ISOLATION TO DIRTY READ");

		stmCBS = conCBS.createStatement();
		Map<String,String> mapCompte = new HashMap<String, String>();

		ResultSet rs = null;
		for(bkmvti mvt : mvts){

			mvt.setDco(dco);
			//mvt.setDva(dva);
			// Ecriture de la ligne dans le fichier
			if(!mvt.getDev().equalsIgnoreCase("001")){
				mvt.setDev("001");
			}

			// Recuperation 
			FactMonthDetails det = new FactMonthDetails(Integer.valueOf(mvt.getAge()), mvt.getNcp()+"-"+mvt.getClc(),mvt.getIntitule(), mvt.getLib(),mvt.getInitDate(),mvt.getDateFact() ,mvt.getDatedebutFact(),mvt.getSen(),mvt.getMon());
			det.setLibage(mvt.getLibage());
			det.setTxtage(mvt.getAge());
			det.setDateAbon(subs.getDate());
			det.setDev(mvt.getDev());
			String cle = mvt.getAge().trim()+mvt.getNcp().trim()+mvt.getClc().trim();
			if(mapCompte.containsKey(cle)){
				det.setIntitule(mapCompte.get(cle));
			}else{
				rs = executeFilterSystemQuery(dsCBS,OgMoHelper.getDefaultCBSQueries().get(6).getQuery(),new Object[]{mvt.getAge(),mvt.getNcp(),mvt.getClc()});
				if(rs.next()){
					det.setIntitule(rs.getString("nom"));
					if(mapCompte.isEmpty()){
						mapCompte.put(cle, rs.getString("nom"));
					}else if(!mapCompte.containsKey(cle)){
						mapCompte.put(cle, rs.getString("nom"));
					}
				}else {
					CloseStream.fermeturesStream(rs);
					rs = executeFilterSystemQuery(dsCBS,OgMoHelper.getDefaultCBSQueries().get(7).getQuery(),new Object[]{mvt.getAge(),mvt.getNcp(),mvt.getClc()});
					if(rs.next()){
						det.setIntitule(rs.getString("inti"));
						if(mapCompte.isEmpty()){
							mapCompte.put(cle, rs.getString("inti"));
						}else if(!mapCompte.containsKey(cle)){
							mapCompte.put(cle, rs.getString("inti"));
						}
					}
				}
			}
			details.add(det);
			if("C".equalsIgnoreCase(mvt.getSen())){
				mntC = mntC+mvt.getMon();
				nbrC++;
			}else{
				mntD = mntD+mvt.getMon();
				nbrD++;
			}
			dev = mvt.getDev();

		}


		if(mvts.size() == 0){
			throw new RuntimeException("Aucune ecriture comptable à intégrer. Rassurez-vous d'avoir effectuer la reconciliation globale !");
		}

		// Insertion 
		Boolean insert = executeUpdateSystemQuery(dsCBS, mvts , dco);

		if(insert.equals(Boolean.FALSE)){
			throw new RuntimeException("Erreur lors de l'intégration des ecritures comptables");
		}

		// Annulation des evenements
		if(transactions.size() > 900 ){
			int i = 0;
			int cpt = 900;
			int size = transactions.size();
			int reste = size % cpt;
			System.out.println("*************** reste *************** : " + reste);
			int nb = size / cpt;
			int j = 0;
			List<Transaction> trans = new ArrayList<Transaction>();
			while(nb > 0){
				System.out.println("*************** cpt *************** : " + cpt);
				j = j + cpt;
				System.out.println("*************** i *************** : " + i);
				System.out.println("*************** j *************** : " + j);
				trans = transactions.subList(i,j);
				System.out.println("*************** trans.size()  *************** : " + trans.size());
				annulerEvenements(trans, dsCBS);
				nb = nb - 1;
				i = j;
			}

			trans = transactions.subList(i,j+reste);
			System.out.println("*************** trans.size() reste *************** : " + trans.size());
			annulerEvenements(trans, dsCBS);

		}else{

			annulerEvenements(transactions, dsCBS);

		}

		// Vidage des variables
		mvts.clear(); mvts = null; 

		// Fermeture de la cnx
		CloseStream.fermeturesStream(rs);
		//if(conCBS != null ) conCBS.close();

	}	



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#archiverTransactions(java.util.List)
	 */
	@Override
	@AllowedRole(name = "archiverTransactions", displayName = "OgMo.Archiver.Transactions")
	public void archiverTransactions(List<Transaction> transactions) throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#purgerTransactions(java.util.List)
	 */
	@Override
	@AllowedRole(name = "purgerTransactions", displayName = "OgMo.Purger.Transactions")
	public void purgerTransactions(List<Transaction> transactions) throws Exception {
		// TODO Auto-generated method stub

	}






	public Connection getSystemConnection(DataSystem system) throws Exception {
		Class.forName(system.getProviderClassName());
		return DriverManager.getConnection( system.getDbConnectionString(), system.getDbUserName(), system.getDbPassword() );
	}


	public ResultSet executeFilterSystemQuery(DataSystem ds, String query) throws Exception {

		ResultSet rs = null;

		if(conCBS == null || conCBS.isClosed()) conCBS = getSystemConnection(ds);

		if(conCBS != null){
			if(ds.getDbConnectionString().indexOf("informix") > 0) conCBS.createStatement().executeUpdate("SET ISOLATION TO DIRTY READ");
			PreparedStatement ps = conCBS.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
		}
		return rs;

	}
	
	
	
	public ResultSet executeFilterSystemQuery(PreparedStatement ps, Object[] parameters) throws Exception {
		if (parameters != null && parameters.length > 0) {
			int i = 1;
			for (Object o : parameters) {
				if (o instanceof java.util.Date)
					ps.setDate(i, new java.sql.Date(((java.util.Date) o).getTime()));
				else if (o instanceof java.sql.Date)
					ps.setDate(i, (java.sql.Date) o);
				else if (o instanceof Double)
					ps.setDouble(i, Double.valueOf(o.toString()));
				else if (o instanceof Long)
					ps.setLong(i, Long.valueOf(o.toString()));
				else if (o instanceof Integer)
					ps.setInt(i, Integer.valueOf(o.toString()));
				else
					ps.setString(i, o.toString());
				i++;
			}
		}
		return ps.executeQuery();
	}
	


	/*
	public ResultSet executeFilterSystemQuery(DataSystem ds, String query, Object[] parameters) throws Exception {

		ResultSet rs = null;

		if(conCBS == null || conCBS.isClosed()) conCBS = getSystemConnection(ds);

		if(conCBS != null){

			if(ds.getDbConnectionString().indexOf("informix") > 0 ){
				stmCBS = conCBS.createStatement();
				stmCBS.executeUpdate("SET ISOLATION TO DIRTY READ");
			}
			PreparedStatement ps = conCBS.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			if(parameters != null && parameters.length > 0){

				int i = 1;
				for(Object o : parameters){
					if(o instanceof java.util.Date) ps.setDate(i, new java.sql.Date( ((java.util.Date)o).getTime() ) );
					else if(o instanceof java.sql.Date) ps.setDate(i, (java.sql.Date)o );
					else if(o instanceof Double) ps.setDouble(i, Double.valueOf( o.toString()) );
					else if(o instanceof Long) ps.setLong(i, Long.valueOf( o.toString()) );
					else if(o instanceof Integer) ps.setInt(i, Integer.valueOf( o.toString()) );
					else ps.setString(i, o.toString());
					i++;

				}

			}

			rs = ps.executeQuery();
			
			stmCBS.close();
		}

		return rs;

	}
	*/
	
	
	public ResultSet executeFilterSystemQuery(DataSystem ds, String query, Object[] parameters) throws Exception {
		ResultSet rs = null;
		Connection conCBS = getSystemConnection(ds);
		Statement stmCBS = null;
		PreparedStatement ps = null;
		
		if(conCBS != null){
			if(ds.getDbConnectionString().indexOf("informix") > 0 ){
				stmCBS = conCBS.createStatement();
				stmCBS.executeUpdate("SET ISOLATION TO DIRTY READ");
				stmCBS.close();
			}
			ps = conCBS.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if(parameters != null && parameters.length > 0){
				int i = 1;
				for(Object o : parameters){
					if(o instanceof java.util.Date) ps.setDate(i, new java.sql.Date( ((java.util.Date)o).getTime() ) );
					else if(o instanceof java.sql.Date) ps.setDate(i, (java.sql.Date)o );
					else if(o instanceof Double) ps.setDouble(i, Double.valueOf( o.toString()) );
					else if(o instanceof Long) ps.setLong(i, Long.valueOf( o.toString()) );
					else if(o instanceof Integer) ps.setInt(i, Integer.valueOf( o.toString()) );
					else ps.setString(i, o.toString());
					i++;
				}
			}
			rs = ps.executeQuery();
		}		
		
		if(rs == null) {
			if ( ps != null ) {
				try {
					ps.close();
				} catch ( SQLException e ) {
				}
			}
			
			if ( conCBS != null ) {
				try {
					conCBS.close();
				} catch ( SQLException e ) {
				}
			}
		}
		
		return rs;
	}
	


	public Boolean executeUpdateSystemQuery(DataSystem ds,List<bkmvti> mvts, Date dco) throws Exception {

		if(conCBS == null || conCBS.isClosed()) conCBS = getSystemConnection(ds);

		if(conCBS != null){

			PreparedStatement ps = null;

			try{

				conCBS.setAutoCommit(false);
				Statement statement = 	conCBS.createStatement(); 
				if(ds.getDbConnectionString().indexOf("informix") > 0) statement.execute("SET ISOLATION TO DIRTY READ");
				ps = conCBS.prepareStatement(mvts.get(0).getSaveQuery()); 

				// Parcours des ecritures String query, Object[] parameters
				for(bkmvti mvt : mvts){
					mvt.setDco(dco);
					if(!mvt.getDev().equalsIgnoreCase("001")) mvt.setDev("001");
					if(mvt.getQueryValues() != null && mvt.getQueryValues().length > 0){
						int i = 1;
						for(Object o : mvt.getQueryValues()){
							if(o == null) ps.setNull(i, java.sql.Types.NULL);
							else if(o instanceof java.util.Date) ps.setDate(i, new java.sql.Date( ((java.util.Date)o).getTime() ) );
							else if(o instanceof java.sql.Date) ps.setDate(i, (java.sql.Date)o );
							else if(o instanceof Double) ps.setDouble(i, Double.valueOf( o.toString()) );
							else if(o instanceof Long) ps.setLong(i, Long.valueOf( o.toString()) );
							else if(o instanceof Integer) ps.setInt(i, Integer.valueOf( o.toString()) );
							else ps.setString(i, o.toString());
							i++;
						}
					}
					ps.addBatch();
					//ps.executeUpdate();
				}	

				ps.executeBatch();
				statement.close();
				conCBS.commit();

			}catch(SQLException e){
				conCBS.rollback();
				return Boolean.FALSE;
			}catch(Exception ex){
				conCBS.rollback();
				return Boolean.FALSE;
			}finally{
				if (ps != null) {
					ps.close();
				}
				if (conCBS != null) {
					conCBS.close();
				}
			}

		}

		return Boolean.TRUE;

	}


	public void executeUpdateSystemQuery(DataSystem ds, String query, Object[] parameters) throws Exception {

		if(conCBS == null || conCBS.isClosed()) conCBS = getSystemConnection(ds);

		if(conCBS != null){

			////System.out.println("----query-----"+query);

			if(ds.getDbConnectionString().indexOf("informix") > 0){
				Statement statement = 	conCBS.createStatement(); //.executeUpdate("SET ISOLATION TO DIRTY READ");
				//statement.execute("SET LOCK MODE TO NOT WAIT ");
				statement.execute("SET ISOLATION TO DIRTY READ");
			}
			//PreparedStatement ps = conCBS.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			PreparedStatement ps = conCBS.prepareStatement(query);

			if(parameters != null && parameters.length > 0){

				int i = 1;
				for(Object o : parameters){

					if(o == null) ps.setNull(i, java.sql.Types.NULL);
					else if(o instanceof java.util.Date) ps.setDate(i, new java.sql.Date( ((java.util.Date)o).getTime() ) );
					else if(o instanceof java.sql.Date) ps.setDate(i, (java.sql.Date)o );
					else if(o instanceof Double) ps.setDouble(i, Double.valueOf( o.toString()) );
					else if(o instanceof Long) ps.setLong(i, Long.valueOf( o.toString()) );
					else if(o instanceof Integer) ps.setInt(i, Integer.valueOf( o.toString()) );
					else ps.setString(i, o.toString());
					i++;

				}

			}
			
			ps.executeUpdate();
			
			if ( ps != null ) {
				try {
					ps.close();
				} catch ( SQLException e ) {
				}
			}
			
			if ( conCBS != null ) {
				try {
					conCBS.close();
				} catch ( SQLException e ) {
				}
			}
		}

	}



	public void executeUpdateSystemQuery(DataSystem ds, List<Queries> queries) throws Exception {

		if(conCBS == null || conCBS.isClosed()) conCBS = getSystemConnection(ds);



		if(conCBS != null){
			//Set autocommit as false
			conCBS.setAutoCommit(Boolean.FALSE);

			if(ds.getDbConnectionString().indexOf("informix") > 0) {
				conCBS.createStatement().executeUpdate("SET ISOLATION TO DIRTY READ");
				//conCBS.createStatement().executeUpdate("SET LOCK MODE TO WAIT");
			}

			for(Queries q : queries) {
				String query = q.getQuery();
				Object[] parameters = q.getParams();

				System.out.println("*********** query *********** " + query);


				PreparedStatement ps = conCBS.prepareStatement(query);

				if(parameters != null && parameters.length > 0){

					int i = 1;
					for(Object o : parameters){

						if(o == null) ps.setNull(i, java.sql.Types.NULL);
						else if(o instanceof java.util.Date) ps.setDate(i, new java.sql.Date( ((java.util.Date)o).getTime() ) );
						else if(o instanceof java.sql.Date) ps.setDate(i, (java.sql.Date)o );
						else if(o instanceof Double) ps.setDouble(i, Double.valueOf( o.toString()) );
						else if(o instanceof Long) ps.setLong(i, Long.valueOf( o.toString()) );
						else if(o instanceof Integer) ps.setInt(i, Integer.valueOf( o.toString()) );
						else ps.setString(i, o.toString());
						i++;

					}

				}

				ps.executeUpdate();
				if (ps != null) ps.close();
			}

			//Commit 
			conCBS.commit();

			// Fermeture des cnx
			// CBS_CNX_OPTI
			//if(conCBS != null ) conCBS.close();
		}

	}



	/**
	 * Lit et retourne la date comptable dans le CBS
	 * @param dsCBS
	 * @return
	 * @throws Exception 
	 */
	public Date getDateComptable() throws Exception{

		Date d = null;
		
		if(dsAIF == null) findAIFDataSystem();
		
		if(dsAIF != null && StringUtils.isNotBlank(dsAIF.getDbConnectionString())) {
			
			HttpGet getRequest = new HttpGet(dsAIF.getDbConnectionString()+"/nomenclature/getdatecomptable");
		    getRequest.setHeader("content-type", "application/json");
		    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(getRequest);
		    HttpEntity entity = null;
		    entity = response.getEntity();
		 
		    if(entity != null) {
		    	 
		    	 String content = EntityUtils.toString(entity);
				 JSONObject json = new JSONObject(content);
	
				 String responseCode = json.getString("code");
				
				 if ("200".equals(responseCode)) {
					 String dat = json.getString("data");
					 d = DateUtil.parse(dat, DateUtil.DATE_MINUS_FORMAT_SINGLE);
					 
					 return d;
				 } 
		     }
		}
		
		return d;

	}


	public Date getDateValeurCredit(Date dco,DataSystem dsCBS) throws Exception{

		Date dva = new Date();

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		// recuperation des jours feriers
		Map<String,String> feriers = new HashMap<String, String>();
		String years = new SimpleDateFormat("yyyy").format(new Date());
		ResultSet rs = executeFilterSystemQuery(dsCBS, "select jourfer from bkfer where TO_CHAR(jourfer,'%Y')='"+years+"'" , null);
		while(rs.next()){
			feriers.put(format.format(rs.getDate(1)),rs.getString(1));				
		}

		if(dco != null){

			// recherche du jour suivant ouvrable
			Date suivant = DateUtils.addDays(dco, 1);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(suivant);
			boolean trouv = false;
			while(!trouv){
				if(feriers.containsKey(format.format(cal.getTime()))){
					cal.add(Calendar.DAY_OF_MONTH, 1);
					trouv = false;
				}else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ){
					cal.add(Calendar.DAY_OF_MONTH, 1);
					trouv = false;
				}else{
					trouv = true;
					suivant = cal.getTime();
					dva = suivant;
				}

			}

		}
		CloseStream.fermeturesStream(rs);

		//if(conCBS != null ) conCBS.close();

		return dva;

	}

	public Date getDateValeurDebit(Date dco,DataSystem dsCBS) throws Exception{
		Date dva = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		// recuperation des jours feriers
		Map<String,String> feriers = new HashMap<String, String>();
		String years = new SimpleDateFormat("yyyy").format(new Date());
		ResultSet rs = executeFilterSystemQuery(dsCBS, "select jourfer from bkfer where TO_CHAR(jourfer,'%Y')='"+years+"'" , null);
		while(rs.next()){
			feriers.put(format.format(rs.getDate(1)),rs.getString(1));				
		}
		if(dco != null){
			// recherche du jour suivant ouvrable
			Date suivant = DateUtils.addDays(dco, -1);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(suivant);
			boolean trouv = false;
			while(!trouv){
				if(feriers.containsKey(format.format(cal.getTime()))){
					cal.add(Calendar.DAY_OF_MONTH, -1);
					trouv = false;
				}else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ){
					cal.add(Calendar.DAY_OF_MONTH, -1);
					trouv = false;
				}else{
					trouv = true;
					suivant = cal.getTime();
					dva = suivant;
				}
			}
		}
		CloseStream.fermeturesStream(rs);
		//if(conCBS != null ) conCBS.close();

		return dva;
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#processPullTransaction(java.lang.String, java.lang.String, java.lang.Double)
	 */
	public synchronized Transaction processA2WTransaction(String bankPIN, Double amount,Double charge,String requestId,String externalRefNo,String requestToken,
			String operatorCode,String accountAlias) throws Exception, OgMoException {

		System.out.println("*************** findSubscriberbankPINBankPIN(bankPIN) *************** : " + bankPIN);
		Subscriber subscriber = findSubscriberbankPINBankPIN(bankPIN);

		// En cas d'erreur on annule tout
		if(subscriber == null) throw new OgMoException(ExceptionCode.SubscriberInvalidPIN, ExceptionCode.SubscriberInvalidPIN.getValue(), ExceptionCategory.METIER);

		String phoneNumber  = subscriber.getSubmsisdn();

		return processA2WW2AMessage( new RequestMessage(TypeOperation.Account2Wallet, bankPIN, phoneNumber, amount,subscriber.getFirstAccount(),subscriber,charge,requestId,externalRefNo,requestToken,operatorCode,accountAlias) );

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#processPushTransaction(java.lang.String, java.lang.String, java.lang.Double)
	 */
	public synchronized Transaction processW2ATransaction(String bankPIN, Double amount,Double charge,String requestId,String externalRefNo,String requestToken,
			String operatorCode,String accountAlias) throws Exception, OgMoException {

		Subscriber subscriber = findSubscriberbankPINBankPIN(bankPIN);

		// En cas d'erreur on annule tout
		if(subscriber == null) throw new OgMoException(ExceptionCode.SubscriberInvalidPIN, ExceptionCode.SubscriberInvalidPIN.getValue(), ExceptionCategory.METIER);

		String phoneNumber  = subscriber.getSubmsisdn();

		return processA2WW2AMessage( new RequestMessage(TypeOperation.Wallet2Account, bankPIN, phoneNumber, amount,null,subscriber,charge,requestId,externalRefNo,requestToken,operatorCode,accountAlias) );

	}

	
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#processBalanceTransaction(java.lang.String, java.lang.String)
	 */
	public synchronized Double processBalanceTransaction(String bankPIN,String requestId) throws Exception, OgMoException {

		////System.out.println("-----bankPIN-----"+bankPIN);

		Subscriber subscriber = findSubscriberbankPINBankPIN(bankPIN);

		// En cas d'erreur on annule tout
		if(subscriber == null) return null;

		String phoneNumber  = subscriber.getSubmsisdn();


		// Initialisation de la transaction a executer
		Transaction tx = getTransactionFromRequestMessage( new RequestMessage(TypeOperation.BALANCE, bankPIN, phoneNumber, 0d, null,subscriber) );

		// En cas d'erreur on annule tout
		if(tx == null) return null;

		tx.setRequestId(requestId);

		tx.setAccountAlias(bankPIN);

		// Recuperation du solde du cpte
		Double solde = getSolde(tx.getAccount(),0d,TypeOperation.BALANCE);


		if(solde == null) throw new OgMoException(ExceptionCode.SystemCoreBankingSystemAcces, "Erreur lors de la lecture du solde du compte", ExceptionCategory.SYSTEM);

		// MAJ du statut de la transaction
		tx.setStatus(TransactionStatus.SUCCESS);

		tx.setSolde(solde);

		// Sauvegarde de la transaction
		//saveTransaction(tx);

		tx = posterEvenementDansCoreBanking(tx);

		// En cas d'erreur on annule tout
		if(tx == null || tx.getStatus() == TransactionStatus.FAILED) return null;
				
		// Retourne le solde du cpte
		return solde;
	}

	

	public synchronized List<TransactionDetail> processMiniStatementTransaction(String bankPIN,String requestId) throws Exception, OgMoException {

		////System.out.println("-----bankPIN-----"+bankPIN);

		List<TransactionDetail> details = new ArrayList<TransactionDetail>();
		TransactionDetail elem = new TransactionDetail();

		Subscriber subscriber = findSubscriberbankPINBankPIN(bankPIN);

		// En cas d'erreur on annule tout
		if(subscriber == null) return null;

		String phoneNumber  = subscriber.getSubmsisdn();
		// Initialisation de la transaction a executer
		Transaction tx = getTransactionFromRequestMessage( new RequestMessage(TypeOperation.MiniStatement, bankPIN, phoneNumber, 0d, null,subscriber) );

		////System.out.println("-----Transaction-----"+tx);

		// En cas d'erreur on annule tout
		if(tx == null) return null;

		tx.setRequestId(requestId);

		tx.setAccountAlias(bankPIN);

		// Recuperation du solde du cpte
		// Double solde = getSolde(tx.getAccount());

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();
		String numCompte = tx.getAccount();
		ResultSet rs = executeFilterSystemQuery(dsCBS, "select first 5 * from bkhis where age = ? and ncp = ? order by dco desc ", new Object[]{numCompte.split("-")[0],numCompte.split("-")[1]});
		while(rs.next()){
			//elem = new TransactionDetail(rs.getString("eve"),rs.getDate("dco"), rs.getString("sen"),rs.getString("dev"), rs.getString("sen"), rs.getDouble("mon"), rs.getString("lib"));
			String dev = rs.getString("dev");
			if(dev.equalsIgnoreCase("950")){
				dev= "XAF";
			}else dev= "EUR";
			elem = new TransactionDetail(rs.getString("eve"),rs.getDate("dco"), rs.getString("sen"),"XAF", rs.getString("sen"), rs.getDouble("mon"), rs.getString("lib"));
			details.add(elem);
		}
		CloseStream.fermeturesStream(rs);
		//if(solde == null) throw new OgMoException(ExceptionCode.SystemCoreBankingSystemAcces, "Erreur lors de la lecture du solde du compte", ExceptionCategory.SYSTEM);

		// MAJ du statut de la transaction
		tx.setStatus(TransactionStatus.SUCCESS);

		//tx.setSolde(solde);
		////System.out.println("-----posterEvenementDansCoreBanking-----");

		// Sauvegarde de la transaction
		//saveTransaction(tx);
		tx = posterEvenementDansCoreBanking(tx);
		
		// En cas d'erreur on annule tout
		if(tx == null || tx.getStatus() == TransactionStatus.FAILED) return null;

		//if(conCBS != null ) conCBS.close();

		// Retourne le solde du cpte
		return details;

	}


	@AllowedRole(name = "executerSimulation", displayName = "OgMo.Executer.Simulation")
	public void executerSimulation() {

	}



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#processReversalTransaction(java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unused")
	@Override
	public synchronized Map<String, String> processReversalTransaction(String idTrans) throws Exception {

		logger.info("*********************************processReversalTransaction**************************idTrans**"+idTrans);

		//Liste des requêtes
		List<Queries> query = new ArrayList();

		// Initialisation de la map a retourner
		Map<String, String> map = new HashMap<String, String>();

		// Chargement des proprietes ds la map
		map.put("idTrans", idTrans);

		Long txID = Long.valueOf(idTrans);
		List<bkeve> eves = dao.filter(bkeve.class, AliasesContainer.getInstance().add("transaction", "transaction"), RestrictionsContainer.getInstance().add(Restrictions.eq("transaction.id", txID)), null, null, 0, -1);

		if(eves == null || eves.isEmpty()) {
			logger.info("*********************************processReversalTransaction************************** eve not found **");
			map.put("statusCode", "500");
			map.put("error", "");
			return map;
		}

		String externalRefNo = "";

		/* Methode commentée le 16-09-2020

		// Initialisation de la map a retourner
		Map<String, String> map = new HashMap<String, String>();

		// Chargement des proprietes ds la map
		map.put("externalRefNo", externalRefNo);

		//*** Long txID = Long.valueOf(externalRefNo);
		Long txID = 0L;

		List<Transaction> transacs = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("externalRefNo", externalRefNo)), OrderContainer.getInstance().add(Order.desc("id")), null, 0, -1);
		if(transacs == null || transacs.isEmpty()){
			map.put("responseCode", "E01");
			return map;
		}
		if(!transacs.isEmpty() &&  transacs.size() > 0) {
			txID = transacs.get(0).getId();
		}

		List<bkeve> eves = dao.filter(bkeve.class, AliasesContainer.getInstance().add("transaction", "transaction"), RestrictionsContainer.getInstance().add(Restrictions.eq("transaction.id", txID)), null, null, 0, -1);
		if(eves == null || eves.isEmpty()){
			map.put("responseCode", "E01");
			return map;
		}
		 */


		try{

			// Recuperation de l'evenement genere par la transaction
			bkeve eve = eves.get(0);

			//envoi de l'evenement a l'API pour annulation
			logger.info("IDEVE: "+eve.getId());
			if(reversalEventsCoreBanking(""+eve.getId()) == null) return map;

			Transaction trans = eve.getTransaction();
			externalRefNo = trans.getExternalRefNo();


			if(trans == null ){

				map.put("CBAReferenceNo",externalRefNo);
				map.put("responseCode", "E01");
				return map;

			}else if(trans.getStatus().equals(TransactionStatus.SUCCESS) ){

				//MAJ de la transaction
				trans  = dao.findByPrimaryKey(Transaction.class, txID, null);
				trans.setCompleted(Boolean.TRUE);
				trans.setStatus(TransactionStatus.CANCEL);
				dao.update(trans);

			}else if(trans.getStatus().equals(TransactionStatus.CANCEL)){
				// MAJ du statut de l'operation (executee avec succes) 
				map.put("responseCode", "E01");
				map.put("CBAReferenceNo",externalRefNo);
			}else if(trans.getStatus().equals(TransactionStatus.FAILED)){
				// MAJ du statut de l'operation (executee avec succes) 
				map.put("responseCode", "E34");
				map.put("CBAReferenceNo",externalRefNo);
			}else{
				// MAJ du statut de l'operation (executee avec succes) 
				map.put("responseCode", "E01");
				map.put("CBAReferenceNo",externalRefNo);
			}

			try {
				String message = "Your " + eve.getTransaction().getTypeOperation().getValue() + " transaction of XAF "+ eve.getTransaction().getAmount() +" has been cancelled!";
				String phone = eve.getTransaction().getPhoneNumber();
				int resultSMS = alerteSMS(message, phone);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch(Exception e){
			// MAJ du statut de l'operation (executee avec succes)
			map.put("responseCode", "E01");
			map.put("CBAReferenceNo",externalRefNo);
			e.printStackTrace();
		}

		// Retourne la map
		return map;

	}

	public synchronized Map<String, String> processReversalTransactionTrans(String externalRefNo) throws Exception {

		//Liste des requêtes
		List<Queries> query = new ArrayList();

		// Initialisation de la map a retourner
		Map<String, String> map = new HashMap<String, String>();

		// Chargement des proprietes ds la map
		map.put("externalRefNo", externalRefNo);

		//*** Long txID = Long.valueOf(externalRefNo);
		Long txID = 0L;

		List<Transaction> transacs = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("externalRefNo", externalRefNo)), OrderContainer.getInstance().add(Order.desc("id")), null, 0, -1);
		if(transacs == null || transacs.isEmpty()){
			map.put("responseCode", "E01");
			return map;
		}


		if(!transacs.isEmpty() &&  transacs.size() > 0) {
			txID = transacs.get(0).getId();
		}
		List<bkeve> eves = dao.filter(bkeve.class, AliasesContainer.getInstance().add("transaction", "transaction"), RestrictionsContainer.getInstance().add(Restrictions.eq("transaction.id", txID)), null, null, 0, -1);

		if(eves == null || eves.isEmpty()) {
			map.put("responseCode", "E01");
			return map;
		}

		try {

			// Recuperation de l'evenement genere par la transaction
			bkeve eve = eves.get(0);

			// Demarrage du service Facade du portail
			// IFacadeManagerRemote portalFacadeManager = (IFacadeManagerRemote) new InitialContext().lookup( PortalHelper.APPLICATION_EAR.concat("/").concat( IFacadeManagerRemote.SERVICE_NAME ).concat("/remote") );

			// Recuperation de la DS de cnx au CBS
			// DataSystem dsCBS = (DataSystem) portalFacadeManager.findByProperty(DataSystem.class, "code", "DELTA-V10");

			// MAJ des soldes indicatifs des cptes debiteurs et crediteurs
			query.add(new Queries("update bkcom set sin=sin-"+ eve.getMnt2() +" where age='"+ eve.getAge2() +"' and ncp='"+ eve.getNcp2() +"' and clc='"+ eve.getClc2() +"' ", null));
			//*** MAJ executeUpdateSystemQuery(dsCBS, "update bkcom set sin=sin-"+ eve.getMnt2() +" where age='"+ eve.getAge2() +"' and ncp='"+ eve.getNcp2() +"' and clc='"+ eve.getClc2() +"' ", null);

			query.add(new Queries("update bkcom set sin=sin+"+ eve.getMnt1() +" where age='"+ eve.getAge1() +"' and ncp='"+ eve.getNcp1() +"' and clc='"+ eve.getClc1() +"' ", null));
			//*** MAJ executeUpdateSystemQuery(dsCBS, "update bkcom set sin=sin+"+ eve.getMnt1() +" where age='"+ eve.getAge1() +"' and ncp='"+ eve.getNcp1() +"' and clc='"+ eve.getClc1() +"' ", null);

			// MAJ de l'evenement dans le CBS
			query.add(new Queries("update bkeve set eta='IG', etap='VA' where eve='"+eve.getEve()+"' and age ='"+ eve.getAge()+"' and age1='"+ eve.getAge1() +"' and ncp1='"+ eve.getNcp1() +"' and clc1='"+ eve.getClc1() +"' ", null));
			//*** MAJ executeUpdateSystemQuery(dsCBS, "update bkeve set eta='IG', etap='VA' where eve='"+eve.getEve()+"' and age ='"+ eve.getAge()+"' and age1='"+ eve.getAge1() +"' and ncp1='"+ eve.getNcp1() +"' and clc1='"+ eve.getClc1() +"' ", null);

			// Modification des infos de l'evenement
			eve.setEta("IG"); eve.setEtap("VA");
			eve.getTransaction().setStatus(TransactionStatus.CANCEL);

			// MAJ de l'evenement
			eve = dao.update(eve);

			// Message d'information du client
			// sendSMS("Your " + eve.getTransaction().getTypeOperation().getValue() + " transaction of XAF "+ eve.getTransaction().getAmount() +" has been cancelled!", eve.getTransaction().getPhoneNumber());

			// MAJ du statut de l'operation (executee avec succes) 
			map.put("responseCode", "000");
			map.put("CBAReferenceNo", eve.getTransaction().getId().toString());

			executeUpdateSystemQuery(dsCBS,query);

		}catch(Exception e){

			// MAJ du statut de l'operation (executee avec succes)
			map.put("responseCode", "E01");
			e.printStackTrace();
		}

		// Retourne la map
		return map;

	}



	public synchronized Map<String, String> processReversalTransactionTransNuit(String externalRefNo) throws Exception {

		//Liste des requêtes
		List<Queries> query = new ArrayList();			

		// Initialisation de la map a retourner
		Map<String, String> map = new HashMap<String, String>();

		// Chargement des proprietes ds la map
		map.put("externalRefNo", externalRefNo);

		//*** Long txID = Long.valueOf(externalRefNo);
		Long txID = 0L;

		List<Transaction> transacs = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("externalRefNo", externalRefNo)), OrderContainer.getInstance().add(Order.desc("id")), null, 0, -1);
		if(transacs == null || transacs.isEmpty()){
			map.put("responseCode", "E01");
			return map;
		}
		if(!transacs.isEmpty() &&  transacs.size() > 0) {
			txID = transacs.get(0).getId();
		}
		List<bkeve> eves = dao.filter(bkeve.class, AliasesContainer.getInstance().add("transaction", "transaction"), RestrictionsContainer.getInstance().add(Restrictions.eq("transaction.id", txID)), null, null, 0, -1);

		if(eves == null || eves.isEmpty()) {
			map.put("responseCode", "E01");
			return map;
		}

		try {

			// Recuperation de l'evenement genere par la transaction
			bkeve eve = eves.get(0);

			// Demarrage du service Facade du portail
			//IFacadeManagerRemote portalFacadeManager = (IFacadeManagerRemote) new InitialContext().lookup( PortalHelper.APPLICATION_EAR.concat("/").concat( IFacadeManagerRemote.SERVICE_NAME ).concat("/remote") );

			// Recuperation de la DS de cnx au CBS
			//DataSystem dsCBS = (DataSystem) portalFacadeManager.findByProperty(DataSystem.class, "code", "DELTA-V10");


			// MAJ des soldes indicatifs des cptes debiteurs et crediteurs
			//executeUpdateSystemQuery(dsCBS, "update bkcom set sin=sin-"+ eve.getMnt2() +" where age='"+ eve.getAge2() +"' and ncp='"+ eve.getNcp2() +"' and clc='"+ eve.getClc2() +"' ", null);
			//executeUpdateSystemQuery(dsCBS, "update bkcom set sin=sin+"+ eve.getMnt1() +" where age='"+ eve.getAge1() +"' and ncp='"+ eve.getNcp1() +"' and clc='"+ eve.getClc1() +"' ", null);


			// MAJ du solde indicatif du Compte Debiteur  orig='WEB' , 'MON' ;  flag=''
			query.add(new Queries("update bksin set mon=0 where age= ? and  dev=? and  ncp = ? and  suf=? and  mon=? and  orig=? and  flag=? ", new Object[]{ eve.getAge1(), eve.getDev1(), eve.getNcp1(), eve.getSuf1(), -eve.getMon1(), "WEB", "O" }));
			//*** MAJ executeUpdateSystemQuery(dsCBS, "update bksin set mon=0 where age= ? and  dev=? and  ncp = ? and  suf=? and  mon=? and  orig=? and  flag=? ", new Object[]{ eve.getAge1(), eve.getDev1(), eve.getNcp1(), eve.getSuf1(), -eve.getMon1(), "WEB", "O" });

			// MAJ du solde indicatif du compte Crediteur
			query.add(new Queries("update bksin set mon=0 where age= ? and  dev=? and  ncp = ? and  suf=? and  mon=? and  orig=? and  flag=? ", new Object[]{ eve.getAge2(), eve.getDev2(), eve.getNcp2(), eve.getSuf2(), eve.getMon2(), "WEB", "O" }));
			//*** MAJ executeUpdateSystemQuery(dsCBS, "update bksin set mon=0 where age= ? and  dev=? and  ncp = ? and  suf=? and  mon=? and  orig=? and  flag=? ", new Object[]{ eve.getAge2(), eve.getDev2(), eve.getNcp2(), eve.getSuf2(), eve.getMon2(), "WEB", "O" });


			// MAJ de l'evenement dans le CBS
			query.add(new Queries("update syn_bkeve set eta='IG', etap='VA' where eve='"+eve.getEve()+"' and age ='"+ eve.getAge()+"' and age1='"+ eve.getAge1() +"' and ncp1='"+ eve.getNcp1() +"' and clc1='"+ eve.getClc1() +"' ", null));
			//*** executeUpdateSystemQuery(dsCBS, "update syn_bkeve set eta='IG', etap='VA' where eve='"+eve.getEve()+"' and age ='"+ eve.getAge()+"' and age1='"+ eve.getAge1() +"' and ncp1='"+ eve.getNcp1() +"' and clc1='"+ eve.getClc1() +"' ", null);

			// Modification des infos de l'evenement
			eve.setEta("IG"); eve.setEtap("VA");
			eve.getTransaction().setStatus(TransactionStatus.CANCEL);

			// MAJ de l'evenement
			eve = dao.update(eve);

			// Message d'information du client
			// sendSMS("Your " + eve.getTransaction().getTypeOperation().getValue() + " transaction of XAF "+ eve.getTransaction().getAmount() +" has been cancelled!", eve.getTransaction().getPhoneNumber());

			// MAJ du statut de l'operation (executee avec succes) 
			map.put("responseCode", "000");
			map.put("CBAReferenceNo", eve.getTransaction().getId().toString());

			executeUpdateSystemQuery(dsCBS,query);

		}catch(Exception e){

			// MAJ du statut de l'operation (executee avec succes)
			map.put("responseCode", "E01");
			e.printStackTrace();
		}

		// Retourne la map
		return map;

	}


	public synchronized List<TransactionDetail> processGetMiniStament(String accountAlias,String requestId) throws Exception {

		// Initialisation de la map a retourner
		Map<String, String> map = new HashMap<String, String>();

		List<TransactionDetail> details = new ArrayList<TransactionDetail>();
		TransactionDetail elem = new TransactionDetail();

		if(accountAlias == null ) {
			map.put("responseCode", "E01");
			elem.setMap(map);
			details.add(elem);
			return details;
		}

		try{

			details = processMiniStatementTransaction(accountAlias,requestId);
			
			if(details == null){
				map.put("responseCode", "E01");
			}else{
				map.put("responseCode", "000");
				for(TransactionDetail t : details){
					t.setMap(map);
				}
			}

			

		}catch(OgMoException me){
			// TODO Auto-generated catch block

			////System.out.println("--processGetMiniStament------OgMoException---------------me-----------"+me.getCode());

			// TODO Auto-generated catch block
			// Erreur Momo
			if(me.getCode().equals(ExceptionCode.BankInsufficientBalance)){
				map.put("responseCode", "E16");
				map.put("amount", "0");
			}else if(me.getCode().equals(ExceptionCode.BankExceededAmount)){
				map.put("responseCode", "E22");
				map.put("amount", "0");
			}else if(me.getCode().equals(ExceptionCode.BankInsufficientBalance)){
				map.put("responseCode", "E16");
			} else if(me.getCode().equals(ExceptionCode.BankBlockingDebitAccount)) {
				map.put("responseCode", "E25");
			}else if(me.getCode().equals(ExceptionCode.SubscriberSuspended)) {
				map.put("responseCode", "E15");
			}else if(me.getCode().equals(ExceptionCode.SubscriberInvalidPIN)) {
				map.put("responseCode", "E13");
			}else if(me.getCode().equals(ExceptionCode.BankBlockingCreditAccount) || me.getCode().equals(ExceptionCode.BankBlockingDebitAccount)) {
				map.put("responseCode", "E26");
			}else {
				map.put("responseCode", "E01");
			}

			//map.put("responseCode", "E01");
			elem.setMap(map);
			details.add(elem);
			me.printStackTrace();

			// En cas d'erreur on annule tout
			Subscriber subs = findSubscriberbankPINBankPIN(accountAlias);
			if(subs != null){
				String phoneNumber  = subs.getSubmsisdn();
				RequestMessage message = new RequestMessage(TypeOperation.MiniStatement,accountAlias, phoneNumber, 0d, null,subs) ;
				// Formation du messafe Transaction 
				Transaction trx = new Transaction(message.getOperation(), subs, message.getAmount(), message.getAccount(), message.getPhoneNumber(),message);
				trx.setRequestId(requestId);
				trx.setStatus(TransactionStatus.FAILED);
				trx.setExceptionCategory(ExceptionCategory.SYSTEM);
				trx.setExceptionCode(me.getCode());
				trx.setAccount(subs.getFirstAccount());
				trx.setDate(new Date());
				trx.setDatetime(new Date());
				trx = dao.save(trx);
				////System.out.println("--processGetMiniStament------OgMoException---------------me-----------"+me.getCode());
			}

		}catch(Exception e){

			////System.out.println("--processGetMiniStament------OgMoException---------------me-----------"+e.getMessage());

			// MAJ du statut de l'operation (executee avec succes)
			map.put("responseCode", "E01");
			elem.setMap(map);
			details.add(elem);
			e.printStackTrace();

			// En cas d'erreur on annule tout
			Subscriber subs = findSubscriberbankPINBankPIN(accountAlias);
			if(subs != null){
				String phoneNumber  = subs.getSubmsisdn();
				RequestMessage message = new RequestMessage(TypeOperation.MiniStatement,accountAlias, phoneNumber, 0d, null,subs) ;
				// Formation du messafe Transaction 
				Transaction trx = new Transaction(message.getOperation(), subs, message.getAmount(), message.getAccount(), message.getPhoneNumber(),message);
				trx.setRequestId(requestId);
				trx.setAccount(subs.getFirstAccount());
				trx.setStatus(TransactionStatus.FAILED);
				trx.setExceptionCategory(ExceptionCategory.SYSTEM);
				trx.setExceptionCode(ExceptionCode.BankException);
				trx.setDate(new Date());
				trx.setDatetime(new Date());
				trx = dao.save(trx);
			}

		}

		// Retourne la map
		return details;

	}



	@Override
	public synchronized Map<String, String> processStatutTransaction(String externalRefNo) throws Exception {

		// Initialisation de la map a retourner
		Map<String, String> map = new HashMap<String, String>();
		Transaction trans  = null;

		try {

			List<Transaction> transacs = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("externalRefNo", externalRefNo)), OrderContainer.getInstance().add(Order.desc("id")), null, 0, -1);
			if(!transacs.isEmpty() &&  transacs.size() > 0) {
				trans = transacs.get(0);
			}

			/*
			Long txID = Long.valueOf(externalRefNo);
			trans  = dao.findByPrimaryKey(Transaction.class, txID, null);
			 */

			if(trans == null ){
				try {
					Query querry = dao.getEntityManager().createQuery("select t from Transaction t where externalRefNo= :externalRefNo ");
					querry.setParameter("externalRefNo",externalRefNo);
					//querry.setParameter("externalRefNo","");
					trans = (Transaction) querry.getSingleResult();
				} catch (Exception e) {
					trans  = null;
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if(trans == null ){

				map.put("CBAReferenceNo",externalRefNo);
				map.put("responseCode", "E01");
				return map;

			}else if(trans.getStatus().equals(TransactionStatus.SUCCESS) ){
				// MAJ du statut de l'operation (executee avec succes) || trans.getStatus().equals(TransactionStatus.PROCESSING)
				map.put("responseCode", "000");
				map.put("CBAReferenceNo",trans.getId().toString());

			}else if(trans.getStatus().equals(TransactionStatus.FAILED)){
				// MAJ du statut de l'operation (executee avec succes) 
				map.put("responseCode", "E34");
				map.put("CBAReferenceNo",externalRefNo);
			}else{
				// MAJ du statut de l'operation (executee avec succes) 
				map.put("responseCode", "E01");
				map.put("CBAReferenceNo",externalRefNo);
			}

		}catch(Exception e){

			// MAJ du statut de l'operation (executee avec succes)
			map.put("responseCode", "E01");
			map.put("CBAReferenceNo",externalRefNo);
			//map.put("responseCode", "E13");
			e.printStackTrace();

		}

		// control Final
		if(map.get("responseCode").equalsIgnoreCase("E01") && trans != null){
			if(trans.getStatus().equals(TransactionStatus.SUCCESS)){
				processReversalTransaction(externalRefNo);
			}
		}

		// Retourne la map
		return map;

	}



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#checkSubscriber(java.lang.String)
	 */
	@Override
	public synchronized boolean checkSubscriber(String phoneNumber) throws Exception {

		// Si le module est desactive on sort
		//if(!isModuleActive()) return false;

		// Verifie si l'abonne existe
		return findSubscriberFromPhoneNumber(phoneNumber) != null;

	}



	@Override
	public synchronized Map<String, String>  processA2W(String bankPIN, Double amount,Double charge, String requestId, String externalRefNo, String requestToken, String operatorCode, String accountAlias) throws Exception {

		// Initialisation de la map a retourner
		Map<String, String> map = new HashMap<String, String>();

		try{

			// Execution de la transaction de Pull
			Transaction tx = processA2WTransaction(bankPIN, amount, charge, requestId, externalRefNo, requestToken, operatorCode, accountAlias);

			if(tx == null){
				map.put("responseCode", "E01");
				map.put("CBAReferenceNo",externalRefNo);
			}else if(tx.getStatus().equals(TransactionStatus.SUCCESS)){
				// Operation executee avec succes
				map.put("responseCode", "000");
				map.put("CBAReferenceNo", tx.getId().toString());
			}else{
				map.put("responseCode", "E01");
				map.put("CBAReferenceNo",externalRefNo);
			}

		} catch(OgMoException me){

			////System.out.println("--processA2W------OgMoException---------------me-----------"+me.getCode());

			// Erreur Momo
			// TODO Auto-generated catch block
			// Erreur Momo
			if(me.getCode().equals(ExceptionCode.BankInsufficientBalance)){
				map.put("responseCode", "E16");
				map.put("amount", "0");
			}else if(me.getCode().equals(ExceptionCode.BankExceededAmount)){
				map.put("responseCode", "E22");
				map.put("amount", "0");
			}else if(me.getCode().equals(ExceptionCode.BankInsufficientBalance)){
				map.put("responseCode", "E16");
			} else if(me.getCode().equals(ExceptionCode.BankBlockingDebitAccount)) {
				map.put("responseCode", "E25");
			}else if(me.getCode().equals(ExceptionCode.SubscriberSuspended)) {
				map.put("responseCode", "E15");
			}else if(me.getCode().equals(ExceptionCode.SubscriberInvalidPIN)) {
				map.put("responseCode", "E13");
			}else if(me.getCode().equals(ExceptionCode.BankBlockingCreditAccount) || me.getCode().equals(ExceptionCode.BankBlockingDebitAccount)) {
				map.put("responseCode", "E26");
			}else {
				map.put("responseCode", "E01");
			}

			map.put("CBAReferenceNo",externalRefNo);

			// En cas d'erreur on annule tout
			Subscriber subs = findSubscriberbankPINBankPIN(bankPIN);
			if(subs != null){
				String phoneNumber  = subs.getSubmsisdn();
				RequestMessage message = new RequestMessage(TypeOperation.Account2Wallet, bankPIN, phoneNumber, amount,subs.getFirstAccount(),subs,charge,requestId,externalRefNo,requestToken,operatorCode,accountAlias) ;
				// Formation du messafe Transaction 
				Transaction trx = new Transaction(message.getOperation(), subs, message.getAmount(), message.getAccount(), message.getPhoneNumber(),message);
				trx.setRequestId(requestId);
				trx.setTtc(amount+charge);
				trx.setStatus(TransactionStatus.FAILED);
				trx.setExceptionCode(me.getCode());
				trx.setAccount(subs.getFirstAccount());
				trx.setDate(new Date());
				trx.setDatetime(new Date());
				trx.setSuccess(false);
				trx = dao.save(trx);
				////System.out.println("--processA2W------Exception-----Transaction---------"+trx.getStatus());
			}

		} catch (Exception e){

			////System.out.println("--processA2W------Exception---------------me-----------"+e.getMessage());

			// Erreur Systeme
			map.put("responseCode", "E01");
			map.put("CBAReferenceNo",externalRefNo);
			e.printStackTrace();

			// En cas d'erreur on annule tout
			Subscriber subs = findSubscriberbankPINBankPIN(bankPIN);
			if(subs != null){
				String phoneNumber  = subs.getSubmsisdn();
				RequestMessage message = new RequestMessage(TypeOperation.Account2Wallet, bankPIN, phoneNumber, amount,subs.getFirstAccount(),subs,charge,requestId,externalRefNo,requestToken,operatorCode,accountAlias) ;
				// Formation du messafe Transaction 
				Transaction trx = new Transaction(message.getOperation(), subs, message.getAmount(), message.getAccount(), message.getPhoneNumber(),message);
				trx.setRequestId(requestId);
				trx.setStatus(TransactionStatus.FAILED);
				trx.setExceptionCategory(ExceptionCategory.SYSTEM);
				trx.setExceptionCode(ExceptionCode.BankException);
				trx.setAccount(subs.getFirstAccount());
				trx.setTtc(amount+charge);
				trx.setDate(new Date());
				trx.setDatetime(new Date());
				trx.setSuccess(false);
				trx = dao.save(trx);
			}

		}

		////System.out.println("--processA2W------OK------------------------"+map.toString());

		return map;

	}



	@Override
	public synchronized Map<String, String> processW2A(String bankPIN, Double amount,Double charge, String requestId, String externalRefNo, String requestToken, String operatorCode, String accountAlias) throws Exception {

		// Initialisation de la map a retourner
		Map<String, String> map = new HashMap<String, String>();

		try{

			// Execution de la transaction de Push
			Transaction tx = processW2ATransaction(bankPIN, amount, charge, requestId, externalRefNo, requestToken, operatorCode, accountAlias);

			if(tx == null){
				map.put("responseCode", "E01");
				map.put("CBAReferenceNo",externalRefNo);
			}else if(tx.getStatus().equals(TransactionStatus.SUCCESS)){
				// Operation executee avec succes
				map.put("responseCode", "000");
				map.put("CBAReferenceNo", tx.getId().toString());
			}else{
				map.put("responseCode", "E01");
				map.put("CBAReferenceNo",externalRefNo);
			}

		}catch(OgMoException me){

			////System.out.println("--processW2A------OgMoException---------------me-----------"+me.getCode());
			// Erreur Momo
			if(me.getCode().equals(ExceptionCode.BankInsufficientBalance)){
				map.put("responseCode", "E16");
				map.put("amount", "0");
			}else if(me.getCode().equals(ExceptionCode.BankExceededAmount)){
				map.put("responseCode", "E22");
				map.put("amount", "0");
			}else if(me.getCode().equals(ExceptionCode.BankInsufficientBalance)){
				map.put("responseCode", "E16");
			} else if(me.getCode().equals(ExceptionCode.BankBlockingDebitAccount)) {
				map.put("responseCode", "E25");
			}else if(me.getCode().equals(ExceptionCode.SubscriberSuspended)) {
				map.put("responseCode", "E15");
			}else if(me.getCode().equals(ExceptionCode.SubscriberInvalidPIN)) {
				map.put("responseCode", "E13");
			}else if(me.getCode().equals(ExceptionCode.BankBlockingCreditAccount) || me.getCode().equals(ExceptionCode.BankBlockingDebitAccount)) {
				map.put("responseCode", "E26");
			}else {
				map.put("responseCode", "E01");
			}

			map.put("CBAReferenceNo", externalRefNo);

			// En cas d'erreur on annule tout
			Subscriber subs = findSubscriberbankPINBankPIN(bankPIN);
			if(subs != null){
				String phoneNumber  = subs.getSubmsisdn();
				RequestMessage message = new RequestMessage(TypeOperation.Wallet2Account, bankPIN, phoneNumber, amount,null,subs,charge,requestId,externalRefNo,requestToken,operatorCode,accountAlias);
				// Formation du messafe Transaction 
				Transaction trx = new Transaction(message.getOperation(), subs, message.getAmount(), message.getAccount(), message.getPhoneNumber(),message);
				trx.setRequestId(requestId);
				trx.setStatus(TransactionStatus.FAILED);
				trx.setExceptionCategory(me.getCategory());
				trx.setExceptionCode(me.getCode());
				trx.setAccount(subs.getFirstAccount());
				trx.setDate(new Date());
				trx.setTtc(amount+charge);
				trx.setDatetime(new Date());
				trx.setSuccess(false);
				trx = dao.save(trx);
				//System.out.println("--processW2A------Exception-----Transaction---------"+trx.getStatus());
			}

		} catch (Exception e){

			//System.out.println("--processW2A------Exception---------------me-----------"+e.getMessage());

			// Erreur Systeme
			map.put("responseCode", "E01");
			map.put("CBAReferenceNo", externalRefNo);
			e.printStackTrace();

			// En cas d'erreur on annule tout
			Subscriber subs = findSubscriberbankPINBankPIN(bankPIN);
			if(subs != null){
				String phoneNumber  = subs.getSubmsisdn();
				RequestMessage message = new RequestMessage(TypeOperation.Wallet2Account, bankPIN, phoneNumber, amount,null,subs,charge,requestId,externalRefNo,requestToken,operatorCode,accountAlias);
				// Formation du messafe Transaction 
				Transaction trx = new Transaction(message.getOperation(), subs, message.getAmount(), message.getAccount(), message.getPhoneNumber(),message);
				trx.setRequestId(requestId);
				trx.setStatus(TransactionStatus.FAILED);
				trx.setExceptionCategory(ExceptionCategory.SYSTEM);
				trx.setExceptionCode(ExceptionCode.BankException);
				trx.setAccount(subs.getFirstAccount());
				trx.setDate(new Date());
				trx.setTtc(amount+charge);
				trx.setDatetime(new Date());
				trx.setSuccess(false);
				trx = dao.save(trx);
			}

		}

		//System.out.println("--processW2A------OK------------------------"+map.toString());

		return map;

	}



	@Override
	public synchronized Map<String, String> processBalance(String bankPin,String requestId) throws Exception {

		// Initialisation de la map a retourner
		Map<String, String> map = new HashMap<String, String>();

		// Chargement des proprietes ds la map
		map.put("bankPin", bankPin);

		// Si les TFJ sont en cours
		/**if(isTfjEnCours()) {
			// On annule l'operation de Demande de Solde
			map.put("amount", "0");
			map.put("responseCode", "E13");
			return map;
		}*/

		try{

			// Execution de la transaction de Balance
			Double amount = processBalanceTransaction(bankPin,requestId);
			
			if(amount == null){
				map.put("responseCode", "E01");
			}else{
				// Recuperation du solde
				map.put("amount", String.valueOf(amount.longValue()) );
				// Operation executee avec succes
				map.put("responseCode", "000");
			}

			

		}catch(OgMoException me){

			//System.out.println("--processBalance------OgMoException---------------me-----------"+me.getCode());

			// Erreur Momo
			if(me.getCode().equals(ExceptionCode.BankInsufficientBalance)){
				map.put("responseCode", "E16");
				map.put("amount", "0");
			}else if(me.getCode().equals(ExceptionCode.BankExceededAmount)){
				map.put("responseCode", "E22");
				map.put("amount", "0");
			}else if(me.getCode().equals(ExceptionCode.BankInsufficientBalance)){
				map.put("responseCode", "E16");
			} else if(me.getCode().equals(ExceptionCode.BankBlockingDebitAccount)) {
				map.put("responseCode", "E25");
			}else if(me.getCode().equals(ExceptionCode.SubscriberSuspended)) {
				map.put("responseCode", "E15");
			}else if(me.getCode().equals(ExceptionCode.SubscriberInvalidPIN)) {
				map.put("responseCode", "E13");
			}else if(me.getCode().equals(ExceptionCode.BankBlockingCreditAccount) || me.getCode().equals(ExceptionCode.BankBlockingDebitAccount)) {
				map.put("responseCode", "E26");
			}else {
				map.put("responseCode", "E01");
			}

			map.put("amount", "0");

			// En cas d'erreur on annule tout
			Subscriber subs = findSubscriberbankPINBankPIN(bankPin);
			if(subs != null){
				String phoneNumber  = subs.getSubmsisdn();
				RequestMessage message = new RequestMessage(TypeOperation.BALANCE, bankPin, phoneNumber, 0d, null,subs);
				// Formation du messafe Transaction 
				Transaction trx = new Transaction(message.getOperation(), subs, message.getAmount(), message.getAccount(), message.getPhoneNumber(),message);
				trx.setRequestId(requestId);
				trx.setAccount(subs.getFirstAccount());
				trx.setStatus(TransactionStatus.FAILED);
				trx.setExceptionCategory(me.getCategory());
				trx.setExceptionCode(me.getCode());
				trx.setDate(new Date());
				trx.setDatetime(new Date());
				trx.setSuccess(false);
				trx = dao.save(trx);
				//System.out.println("--processBalance------Exception-----Transaction---------"+trx.getStatus());
			}

		}catch(Exception e){

			//System.out.println("--processBalance------Exception---------------me-----------"+e.getMessage());

			// Erreur Systeme
			map.put("responseCode", "E01");
			map.put("amount", "0");
			e.printStackTrace();

			// En cas d'erreur on annule tout
			Subscriber subs = findSubscriberbankPINBankPIN(bankPin);
			if(subs != null){
				String phoneNumber  = subs.getSubmsisdn();
				RequestMessage message = new RequestMessage(TypeOperation.BALANCE, bankPin, phoneNumber, 0d, null,subs);
				// Formation du messafe Transaction 
				Transaction trx = new Transaction(message.getOperation(), subs, message.getAmount(), message.getAccount(), message.getPhoneNumber(),message);
				trx.setRequestId(requestId);
				trx.setAccount(subs.getFirstAccount());
				trx.setStatus(TransactionStatus.FAILED);
				trx.setExceptionCategory(ExceptionCategory.SYSTEM);
				trx.setExceptionCode(ExceptionCode.BankException);
				trx.setDate(new Date());
				trx.setDatetime(new Date());
				trx.setSuccess(false);
				trx = dao.save(trx);
			}

		}

		return map;

	}

	/**
	 * Determine si l'agence centrale est fermee ou non
	 * @return
	 * @throws Exception
	 */
	private String getStatutAgenceCentrale() throws Exception {

		String statutTfj = "FE";

		try{
			// Lecture des Prmtrs de cnx a Amplitude
			if(dsCBS == null) findCBSDataSystem();
			// Recuperation du l'etat de l'agence centrale
			ResultSet rs = executeFilterSystemQuery(dsCBS, "select lib2 from bknom where cacc='00099'", null);
			statutTfj = rs != null && rs.next() ? rs.getString("lib2").trim() : "FE";
			CloseStream.fermeturesStream(rs);
			//if(conCBS != null ) conCBS.close();

		}catch (Exception e) {
			// TODO: handle exception
			statutTfj = "FE";
		}
		return statutTfj;
	}

	/**
	 * Determine si on a lance les TFJ
	 * @return
	 * @throws Exception
	 */
	public boolean isTfjEnCours() throws Exception {

		// Recuperation des parametres generaux
		if(params == null ) params = findParameters();

		// Recuperation du statut de l'agence centrale
		String statutAgCentral = getStatutAgenceCentrale();
		if(params == null || statutAgCentral == null || params.getDateTfjo() == null ) return false;

		// On calcule la date 10h avec le lancement des TFJ
		//Calendar cal = new GregorianCalendar(); cal.setTime( new SimpleDateFormat("dd/MM/yyyy HH':'mm").parse(param.getDateTfjo()) ); cal.add(Calendar.HOUR, 10);
		// Calcul du resultat a retourner
		// boolean resultat = statutAgCentral.equalsIgnoreCase("FE") || ( !param.getActive().booleanValue() && new Date().before(cal.getTime()) );
		boolean resultat = statutAgCentral.equalsIgnoreCase("FE");

		// Si l'agence est ouverte et on a depasse les 10h apres le lancement des TFJ et que le service n'est tjr pas actif alors on l'active
		if(statutAgCentral.equalsIgnoreCase("OU")){
			// Activation de la Fin du TFJ
			endTFJ();
		}
		// Retourne le resultat 
		return resultat;
	}

	private void endTFJ() throws Exception {

		// On Reactivation du module
		Parameters p = findParameters(); 
		p.setActive(Boolean.TRUE);
		dao.update(p);
		//logger.info("Module Reactive. OK!");
		//mobileMoneyDAO.getEntityManager().createQuery("Update Parameters p set p.active=:actif").setParameter("actif", Boolean.TRUE).executeUpdate();

		// On Poste dans Amplitude des evts mis en attente pendant les TFJ
		posterEvenementsEnSuspendPendantTFJ();
		//logger.info("Evenements en Attente Postes. OK!");

	}

	/**
	 * Calcule et retourne le total des Pull qu'un client a effectue pendant les TFJ
	 * @param phoneNumber
	 * @return
	 * @throws Exception
	 */
	private Double getTotalAmountBank2WalletPendantTFJ(String phoneNumber) throws Exception {

		// Initialisation du mnt a retourner
		Double mnt = 0d;

		// Recherche de la liste des evenements concernes 
		List<bkeve> eves = dao.filter(bkeve.class, AliasesContainer.getInstance().add("transaction", "tx"), RestrictionsContainer.getInstance().add(Restrictions.eq("tx.typeOperation", TypeOperation.Account2Wallet)).add(Restrictions.eq("tx.phoneNumber", phoneNumber)).add(Restrictions.eq("suspendInTFJ", Boolean.TRUE)), null, null, 0, -1);

		// Calcul du mnt total des Pull
		if(eves != null && !eves.isEmpty()) for(bkeve eve : eves) mnt += eve.getTransaction().getAmount();

		// Retourne le mnt total des Pull effectues
		return mnt;
	}

	/**
	 * Poste les evenements 
	 * @throws Exception
	 */
	public void posterEvenementsEnSuspendPendantTFJ() throws Exception {

		// Recherche des evenements non postes dans Delta pour avoir ete faits pendant les TFJ
		List<bkeve> eves = dao.filter(bkeve.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("suspendInTFJ", Boolean.TRUE)), null, null, 0, -1);

		// DataStore vers Amplitude
		if(dsCBS == null) findCBSDataSystem();

		Date dco = getDateComptable();

		Parameters params = findParameters();

		// Parcours des Evenements trouves
		for(bkeve eve : eves){

			try{

				/***************************************************************/
				/*** LECTURE DU DERNIER NUMERO D'EVENEMENT DU TYPE OPERATION ***/
				/***************************************************************/

				// Recuperation du dernier numero evenement du type operation
				ResultSet rs = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(2).getQuery(), new Object[]{ params.getCodeOperation() });

				// Log
				logger.info("Lecture du dernier numero d'evenement genere OK!");

				// Calcul du numero d'evenement
				Long numEve = rs != null && rs.next() ? numEve = rs.getLong("num") + 1 : 1l;

				// Log
				logger.info("Calcul du prochain numero d'evenement OK!");

				// Log
				logger.info("Lecture du dernier numero d'evenement genere OK!");
				executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(3).getQuery(), new Object[]{numEve, params.getCodeOperation() });

				// Fermeture de cnx
				CloseStream.fermeturesStream(rs);

				// Affectation de la date comptable actuelle a l'evenement
				eve.setDco(dco);
				String codeEve = OgMoHelper.padText(String.valueOf(numEve), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0");
				eve.setEve(codeEve);
				eve.setEveo(codeEve);
				for(bkmvti b : eve.getEcritures()){
					b.setEve(codeEve);
					b.getQueryValues()[3] = codeEve;
				}

				// Enregistrement de l'evenement dans Amplitude
				executeUpdateSystemQuery(dsCBS, eve.getSaveQuery(), eve.getQueryValues());

				// MAJ du solde indicatif du compte debiteur
				executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(4).getQuery(), new Object[]{ (eve.getTransaction().getTypeOperation().equals(TypeOperation.Wallet2Account) ? eve.getTransaction().getAmount() : eve.getMnt1()) , eve.getAge1(), eve.getNcp1(), eve.getClc1() } );

				// MAJ du solde indicatif crediteur
				if(eve.getTransaction().getTypeOperation().equals(TypeOperation.Wallet2Account) || eve.getTransaction().getTypeOperation().equals(TypeOperation.Account2Wallet)) executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(5).getQuery(), new Object[]{eve.getTransaction().getAmount(), eve.getAge2(), eve.getNcp2(), eve.getClc2()  });

				// MAJ du dernier numero d'evenement utilise pour le type operation
				executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(3).getQuery(), new Object[]{ Long.valueOf(eve.getEve()), eve.getOpe() });

			} catch(Exception e){e.printStackTrace();}

			// MAJ du Statut de l'evenement
			eve.setSuspendInTFJ(Boolean.FALSE);

			//if(conCBS != null ) conCBS.close();

			// MAJ de l'evenement
			dao.update(eve);
		}

	}

	/*
	private boolean isModuleActive(){

		// Initialisation du resultat a retourner
		boolean result = true;

		// Recuperation des parametres Generaux
		Parameters param = findParameters();

		// Si
		if(param.getActive().booleanValue() || param.getHeureReprise() == null || param.getDateTfjo() == null) {

			return result;

		} else {

			try{

				// Initialisation du calendrier Gregorien
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(new Date());

				// Recuperation de la date courante
				Date now = OgMoHelper.sdf.parse( OgMoHelper.sdf.format(new Date()) ) ;

				// Calcul de la Date de remise en service du module
				Date dateReprise = OgMoHelper.sdf.parse( OgMoHelper.sdf_DATE.format(cal.getTime()) + " " + param.getHeureReprise()  );

				// Recuperation de la date du Traitement (TFJO)
				Date dateTfjo = OgMoHelper.sdf.parse(param.getDateTfjo());

				// Si la Date de reprise est inferieure a la date du Traitement (alors il s'agit +to du jour suivant)
				if(dateTfjo.after(dateReprise)) {
					cal.add(Calendar.DATE, 1);
					dateReprise = OgMoHelper.sdf.parse( OgMoHelper.sdf_DATE.format(cal.getTime()) + " " + param.getHeureReprise()  );
				}

				// Si la date de remise en service du module est depassee
				if(now.after(dateReprise)) {

					// Reactivation du module
					mobileMoneyDAO.getEntityManager().createQuery("Update Parameters p set p.active = :actif").setParameter("actif", Boolean.TRUE).executeUpdate();

				} else {

					// Le module est désactive
					result = false;
				}

			}catch(Exception ex){}
		}

		return result;
	}
	 */


	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#filterUSSDTransactions(com.yashiro.persistence.utils.dao.tools.RestrictionsContainer)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@AllowedRole(name = "filterUSSDTransactions", displayName = "OgMo.Consulter.USSDTransactions")
	public List<USSDTransaction> filterUSSDTransactions(RestrictionsContainer rc) {

		// Recherche la liste des trx USSD ds la BD du serveur Pull/Push
		//List<USSDTransaction> data = pullpushDAO.filter(USSDTransaction.class, null, rc, OrderContainer.getInstance().add(Order.desc("dt_Created")), null, 0, -1);
		List<USSDTransaction> data = new ArrayList<USSDTransaction>();

		if( !data.isEmpty() ) {

			// Affectation du souscripteur a chaque enregistrement trouve
			for(USSDTransaction t : data) {

				try {

					// Recherche du souscripteur possedant le numero de Tel du message 
					List<Subscriber> subs = dao.getEntityManager().createQuery("From Subscriber s left join fetch s.phoneNumbers phones where phones in ('"+ t.getStr_Phone() +"')  ").getResultList();
					t.setSubscriber( subs != null && !subs.isEmpty() ? subs.get(0) : null );

				} catch(Exception e){}
			}
		}

		return data;
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#executerReconciliation(java.util.List)
	 */
	@Override
	@AllowedRole(name = "executerReconciliation", displayName = "OgMo.Executer.Reconciliation")
	public void executerReconciliation(List<USSDTransaction> trans) throws Exception, OgMoException {

		if(trans == null || trans.isEmpty()) return;

		// Excution des transactions Pull/Push de la liste
		for(USSDTransaction t : trans) {

			// Re-Execution de la transaction
			// processPullPushMessage( new RequestMessage(t.getTypeOperation(), Encrypter.getInstance().decryptText( t.getSubscriber().getBankPIN() ), t.getStr_Phone(), Double.valueOf(t.getInt_Amount()), t.getSubscriber().getAccounts().get(0)) ) ;

			// MAJ des statuts de la transaction
			t.setStr_Status("valide"); t.setStr_Step("valide");

			// MAJ des statuts de la transaction dans l'api ussd
			// pullpushDAO.update(t);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#executerTFJO()
	 */
	@Override
	@SuppressWarnings("unchecked")
	//@TransactionTimeout(value = 15000)
	@AllowedRole(name = "executerTFJO", displayName = "OgMo.Executer.TFJO")
	public List<Comptabilisation> executerTFJO() throws Exception {

		// Initialisation de la liste a retourner
		List<Comptabilisation> compta = new ArrayList<Comptabilisation>();

		// Lecture des parametres generaux
		Parameters param = findParameters();

		// Recuperation de la liste des commissions
		Map<TypeOperation, Commissions> mapComs = ConverterUtil.convertCollectionToMap(param.getCommissions(), "operation");
		Double valeurComs = 0d, tva = 0d; Periodicite p = Periodicite.MOIS;

		// Construction d'une Map de periodicites
		Map<Periodicite, Integer> mapPeriods = new HashMap<Periodicite, Integer>();
		mapPeriods.put(Periodicite.MOIS, 30); mapPeriods.put(Periodicite.TRIMESTRE, 90); mapPeriods.put(Periodicite.SEMESTRE, 180); mapPeriods.put(Periodicite.ANNUEL, 365);

		// Calcul des commissions a prelever periodiquement
		valeurComs += mapComs.get(TypeOperation.Account2Wallet).getPeriodFacturation() != null ? mapComs.get(TypeOperation.Account2Wallet).getValeur() : 0d;
		valeurComs += mapComs.get(TypeOperation.Wallet2Account).getPeriodFacturation() != null ? mapComs.get(TypeOperation.Wallet2Account).getValeur() : 0d;

		// Recuperation de la periodicite
		p = mapComs.get(TypeOperation.Account2Wallet).getPeriodFacturation() != null ? mapComs.get(TypeOperation.Account2Wallet).getPeriodFacturation() : mapComs.get(TypeOperation.Wallet2Account).getPeriodFacturation() ;

		// Recuperation de la tva
		tva = Math.max( mapComs.get(TypeOperation.Account2Wallet).getPeriodFacturation() != null ? mapComs.get(TypeOperation.Account2Wallet).getTauxTVA() : 0d, mapComs.get(TypeOperation.Wallet2Account).getPeriodFacturation() != null ? mapComs.get(TypeOperation.Wallet2Account).getTauxTVA() : 0d);

		// Si la transaction de Account2Wallet est comptabilisee periodiquement
		if( valeurComs > 0 ) { 

			// Lecture de la periodicite de facturation
			Integer nbJrs = mapPeriods.get(p); 

			// Initialisation de la requete de chargement des abonnes a comptabiliser
			String req = "Select distinct s from Subscriber s where s.status ='"+ StatutContrat.ACTIF +"' and ( s.id in (select distinct c.subscriber.id from Comptabilisation c where CURRENT_DATE - c.dateCompta >= "+ nbJrs +" and c.dateCompta = (select max(c3.dateCompta) from Comptabilisation c3 ) ) or (s.id not in (select distinct c2.subscriber.id from Comptabilisation c2) and CURRENT_DATE - s.date >= "+ nbJrs +" )  ) order by s.customerName ";

			// Chargement de la liste des abonnes a comptabiliser
			List<Subscriber> subs = new ArrayList<Subscriber>( new HashSet<Subscriber>( dao.getEntityManager().createQuery(req).getResultList() ) );

			// Parcours de la liste des abonnes trouves
			for(Subscriber s : subs) {

				// Generation des evenements a comptabiliser pour chaque abonne trouve
				compta.add( new Comptabilisation(null, new Date(), s, p, buildEvenement( new Transaction(TypeOperation.COMPTABILISATION, s, 0d, s.getAccounts().get(0), s.getPhoneNumbers().get(0) , new Date(), valeurComs, valeurComs * (1 + tva/100)  ) ) ) );

			}


		}


		// Liberation des ressources
		mapComs.clear(); mapPeriods.clear(); param = null; //com = null; com2 = null;

		if(compta != null && !compta.isEmpty()) for(int i=compta.size()-1; i>=0; i--){
			if(compta.get(i).getEve() == null) compta.remove(i);
		}

		// Retourne la liste des comptabilisations
		return compta;

	}

	@Override
	@SuppressWarnings("unchecked")
	//@TransactionTimeout(value = 15000)
	@AllowedRole(name = "executerTFJO", displayName = "OgMo.Executer.TFJO")
	public List<Comptabilisation> executerTFJO2() throws Exception {

		// Initialisation de la liste a retourner
		List<Comptabilisation> compta = new ArrayList<Comptabilisation>();

		// Initialisation de la liste des abonnes a comptabiliser
		List<Subscriber> subs = new ArrayList<Subscriber>();

		String req = ""; 
		int nbJrs = 0;

		// Lecture des parametres generaux
		Parameters param = findParameters();

		// Recuperation de la liste des commissions
		Map<TypeOperation, Commissions> mapComs = ConverterUtil.convertCollectionToMap(param.getCommissions(), "operation");
		Double tva = 19.25; 

		// Construction d'une Map de periodicites
		Map<Periodicite, Integer> mapPeriods = new HashMap<Periodicite, Integer>();
		mapPeriods.put(Periodicite.MOIS, 30); mapPeriods.put(Periodicite.TRIMESTRE, 90); mapPeriods.put(Periodicite.SEMESTRE, 180); mapPeriods.put(Periodicite.ANNUEL, 365);

		// Recuperation de la tva
		tva = Math.max( mapComs.get(TypeOperation.Account2Wallet).getPeriodFacturation() != null ? mapComs.get(TypeOperation.Account2Wallet).getTauxTVA() : 0d, mapComs.get(TypeOperation.Wallet2Account).getPeriodFacturation() != null ? mapComs.get(TypeOperation.Wallet2Account).getTauxTVA() : 0d);


		/**
		 * **********
		 * **********
		 * **********
		 */

		// Initialisation de la requete des abonnes n'ayant pas encore fait l'objet d'une comptabilisation
		req = "From Subscriber s where s.status = :statut and s.facturer = :facturer and s.commissions>0 and s.id not in (select distinct c.subscriber.id from Comptabilisation c)";

		// Recherche des abonnes qui n'ont pas encore fait l'objet d'un comptabilisation
		subs = dao.getEntityManager().createQuery(req).setParameter("statut", StatutContrat.ACTIF).setParameter("facturer", Boolean.TRUE).getResultList();

		// Parcours de la liste des abonnes
		if(subs != null && !subs.isEmpty()){
			for(int i=subs.size()-1; i>=0; i--){
				// Suppression des abonnes n'ayant pas encore atteint le delai de facturation
				nbJrs = (int) OgMoHelper.getNbreJoursBetween(subs.get(i).getDate(), new Date());
				if(nbJrs < mapPeriods.get(subs.get(i).getPeriod())) subs.remove(i);
			}
		}

		/**
		 * **********
		 * **********
		 * **********
		 */

		// Initialisation de la requete des abonnes ayant deja fait l'objet d'une comptabilisation
		//req = "From Comptabilisation c where c.subscriber.status = :statut and c.subscriber.facturer = :facturer and c.subscriber.commissions>0 and c.dateCompta = (select max(c1.dateCompta) from Comptabilisation c1 where c1.subscriber.id = c.subscriber.id ) ";

		req = "From Subscriber s where s.status = :statut and s.facturer = :facturer and s.commissions>0 and s.id in (select distinct c.subscriber.id from Comptabilisation c)";

		// Recherche des abonnes qui n'ont pas encore fait l'objet d'un comptabilisation
		List<Subscriber> tmp = dao.getEntityManager().createQuery(req).setParameter("statut", StatutContrat.ACTIF).setParameter("facturer", Boolean.TRUE).getResultList();

		for(Subscriber s : tmp) {

			String req2 = "From Comptabilisation c where c.subscriber.id=:id and c.dateCompta = (select max(c1.dateCompta) from Comptabilisation c1 where c1.subscriber.id = :id )";
			List<Comptabilisation> tmpCompta = dao.getEntityManager().createQuery(req2).setParameter("id", s.getId()).getResultList();
			if(tmpCompta != null && tmpCompta.isEmpty()) {
				nbJrs = (int) OgMoHelper.getNbreJoursBetween(tmpCompta.get(0).getDateCompta(), new Date());
				if(nbJrs >= mapPeriods.get(s.getPeriod()) ) subs.add(s);
			}

		}

		// Suppression des doublons
		subs = new ArrayList<Subscriber>( new HashSet<Subscriber>(subs) );

		// Parcours de la liste des abonnes trouves
		for(Subscriber s : subs) {

			Double com = 0d;
			if(s.getProfil() != null)
				com = s.getProfil().getCommissions();
			else
				com = s.getCommissions();

			// Generation des evenements a comptabiliser pour chaque abonne trouve
			//compta.add( new Comptabilisation(null, new Date(), s, s.getPeriod(), buildEvenement( new Transaction(TypeOperation.COMPTABILISATION, s, 0d, s.getFirstAccount(), s.getFirstPhone() , new Date(), s.getCommissions(), s.getCommissions() * (1 + tva/100)  ) ) ) );
			compta.add( new Comptabilisation(null, new Date(), s, s.getPeriod(), buildEvenement( new Transaction(TypeOperation.COMPTABILISATION, s, 0d, s.getFirstAccount(), s.getFirstPhone() , new Date(), com, com * (1 + tva/100)  ) ) ) );

		}

		// Liberation des ressources
		mapComs.clear(); mapPeriods.clear(); param = null; tmp.clear();  //com = null; com2 = null;

		if(compta != null && !compta.isEmpty()) for(int i=compta.size()-1; i>=0; i--){
			if(compta.get(i).getEve() == null) compta.remove(i);
		}

		// Retourne la liste des comptabilisations
		return compta;

	}


	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#vaiderTFJO(java.util.List)
	 */
	@Override
	@AllowedRole(name = "validerTFJO", displayName = "OgMo.Valider.TFJO")
	public FactMonth validerTFJO(List<Comptabilisation> data,String user) throws Exception {

		if(dsCBS == null) findCBSDataSystem();

		details = new ArrayList<FactMonthDetails>();
		mntD = 0d; mntC = 0d; nbrC = 0; nbrD = 0;

		Date dco = getDateComptable();

		// Parcours de la liste des comptabilisations a valider
		for(Comptabilisation c : data) {

			// Si l'objet courant a ete selectionne par l'utilisateur et ses ecritures sont equilibrees
			if(c.getEve() != null) if(c.isSelected() && c.getEve().isEquilibre()) {

				if(isSoldeSuffisant(c.getSubscriber().getFirstAccount(), c.getEve().getMon1())){

					// Parcours des ecritures
					for(bkmvti mvt : c.getEve().getEcritures()) {
						// Ecriture de la ligne dans le fichier
						executeUpdateSystemQuery(dsCBS, mvt.getSaveQuery(), mvt.getQueryValues());

						FactMonthDetails det = new FactMonthDetails(Integer.valueOf(mvt.getAge()), mvt.getNcp()+"-"+mvt.getClc(),mvt.getIntitule(), mvt.getLib(),mvt.getInitDate(),mvt.getDateFact() ,mvt.getDatedebutFact(),mvt.getSen(),mvt.getMon());
						det.setLibage(mvt.getLibage());
						det.setTxtage(mvt.getAge());
						details.add(det);
						if("C".equalsIgnoreCase(mvt.getSen())){
							mntC = mntC+mvt.getMon();
							nbrC++;
						}else{
							mntD = mntD+mvt.getMon();
							nbrD++;
						}

					}

					// MAJ du statut de la transaction
					c.getEve().getTransaction().setStatus(TransactionStatus.SUCCESS);
					c.setStatus(TransactionStatus.SUCCESS);

					// Sauvegarde l'evenement
					dao.save(c);
				}

			}
		}

		String mois = DateFormatUtils.format(new Date(),"MMMM");
		Collections.sort(details);
		FactMonth fac = new FactMonth(dco, "D", "C", mntD, mntC, nbrD, nbrC, user, new Date(), mois);
		fac = dao.save(fac);
		for(FactMonthDetails det : details) det.setParent(fac);
		dao.saveList(details,true);
		fac.setDetails(details);

		return fac;
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#exportComptabilisationIntoExcelFile(java.util.List, java.lang.String)
	 */
	@Override
	@AllowedRole(name = "exportComptabilisationIntoExcelFile", displayName = "OgMo.Export.TFJO.To.Excel")
	public void exportComptabilisationIntoExcelFile( List<Comptabilisation> data, String fileName ) throws Exception {

		// Initialisation d'un document Excel
		SXSSFWorkbook wb = new SXSSFWorkbook();

		// Initialisation de la Feuille courante
		Sheet sheet  = wb.createSheet("Ecritures Comptables");

		// Creation d'une ligne
		Row row = sheet.createRow(0);

		// Affichage des entetes de colonnes du fichier excel
		row.createCell(0).setCellValue( "N°" );
		row.createCell(1).setCellValue( "N° Mvt" );
		row.createCell(2).setCellValue( "Agence" );
		row.createCell(3).setCellValue( "Date Comptable" );
		row.createCell(4).setCellValue( "Libellé" );
		row.createCell(5).setCellValue( "Sens" );
		row.createCell(6).setCellValue( "N° de Cpte" );
		row.createCell(7).setCellValue( "Intitulé" );
		row.createCell(8).setCellValue( "Montant" );
		row.createCell(9).setCellValue( "Opération" );
		row.createCell(10).setCellValue( "N° de Pièce" );
		row.createCell(11).setCellValue( "Réf. de Lettrage" );

		// Initialisation du compteur
		int i = 1;

		// Parcours des transaction
		for(Comptabilisation c : data){


			// Parcours des ecritures comptables de chaque transaction
			if(c.isSelected()) for(bkmvti ec : c.getEve().getEcritures()){

				// Initialisation d'une ligne
				row = sheet.createRow(i);

				// Affichage des colonnes dans la fichier excel
				row.createCell(0).setCellValue( i++ );
				row.createCell(1).setCellValue( ec.getMvti() );
				row.createCell(2).setCellValue( ec.getAge() );
				row.createCell(3).setCellValue( ec.getDco() );
				row.createCell(4).setCellValue( ec.getLib() );
				row.createCell(5).setCellValue( ec.getSen() );
				row.createCell(6).setCellValue( ec.getNcp() );
				row.createCell(7).setCellValue( ec.getLabel() );
				row.createCell(8).setCellValue( ec.getMon() );
				row.createCell(9).setCellValue( ec.getOpe() );
				row.createCell(10).setCellValue( ec.getPie() );
				row.createCell(11).setCellValue( ec.getRlet() );

			}

		}

		/**
		 * DEUXIEME FEUILLE
		 */

		// Initialisation de la Feuille courante
		Sheet sheet2  = wb.createSheet("Synthese");

		// Creation d'une ligne
		row = sheet2.createRow(0);

		// Affichage des entetes de colonnes du fichier excel
		row.createCell(0).setCellValue( "N°" );
		row.createCell(1).setCellValue( "N° de compte" );
		row.createCell(2).setCellValue( "Client" );
		row.createCell(3).setCellValue( "Date Abonnement" );
		row.createCell(4).setCellValue( "Date Facturation" );
		row.createCell(5).setCellValue( "Sens" );
		row.createCell(6).setCellValue( "Montant" );
		row.createCell(7).setCellValue( "Taxes" );
		row.createCell(8).setCellValue( "Agence" );

		// Initialisation du compteur
		i = 1;

		// Parcours des transaction
		for(Comptabilisation c : data) {


			// Parcours des ecritures comptables de chaque transaction
			if(c.isSelected()) {

				// Initialisation d'une ligne
				row = sheet2.createRow(i);

				// Affichage des colonnes dans la fichier excel
				row.createCell(0).setCellValue( i++ );
				row.createCell(1).setCellValue( c.getSubscriber().getFirstAccount() );
				row.createCell(2).setCellValue( c.getSubscriber().getCustomerName() );
				row.createCell(3).setCellValue( c.getSubscriber().getDate() );
				row.createCell(4).setCellValue( c.getDateCompta() );
				row.createCell(5).setCellValue( "D" );
				row.createCell(6).setCellValue( c.getEve().getTransaction().getAmount() + c.getEve().getTransaction().getCommissions() );
				row.createCell(7).setCellValue( c.getEve().getTransaction().getTaxes() );
				row.createCell(8).setCellValue( c.getSubscriber().getFirstAccount().substring(0, 5) );
			}
		}


		// Sauvegarde du fichier
		FileOutputStream fileOut = new FileOutputStream(PortalHelper.JBOSS_DATA_DIR + File.separator + PortalHelper.PORTAL_RESOURCES_DATA_DIR + File.separator + PortalHelper.PORTAL_DOWNLOAD_DATA_DIR + File.separator + fileName);
		wb.write(fileOut);
		fileOut.close();

	}


	private  Double mntD = 0d;
	private  Double mntC = 0d;
	private  Integer nbrC = 0;
	private  Integer nbrD = 0;
	private  String dev = "";
	private  List<FactMonthDetails> details = new ArrayList<FactMonthDetails>();

	private List<BranchMails> ecritures;




	public List<Transaction> filterTransactionsAreconcilier() throws Exception{
		Parameters params = findParameters();
		return dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("status", TransactionStatus.SUCCESS)).add(Restrictions.eq("posted", Boolean.FALSE)).add(Restrictions.eq("completed", Boolean.FALSE)).add(Restrictions.or(Restrictions.eq("typeOperation",TypeOperation.Account2Wallet),Restrictions.eq("typeOperation",TypeOperation.Wallet2Account))).add(Restrictions.le("datetime",DateUtils.addMinutes(new Date(),-params.getPeriodeLancementRobot()))), OrderContainer.getInstance().add(Order.asc("date")), null, 0, -1);
	}



	@SuppressWarnings("unchecked")
	public String checkTransactionsNonreconcilie() throws Exception{

		List<Transaction> trans = new ArrayList<Transaction>();
		Parameters params = findParameters();
		String alerte = "";

		if(params == null) return alerte;

		if(StringUtils.isBlank(params.getNumeroAlerteSMS()) || params.getNumeroAlerteSMS().trim().length() < 9) return alerte;

		if(StringUtils.isBlank(Integer.toString(params.getPeriodeLancementRobot()))) return alerte;

		if(StringUtils.isBlank(params.getSeuilReconciliation())) return alerte;

		//*** List<Transaction> trans = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("status", TransactionStatus.SUCCESS)).add(Restrictions.eq("posted", Boolean.FALSE)).add(Restrictions.eq("completed", Boolean.FALSE)).add(Restrictions.or(Restrictions.eq("typeOperation",TypeOperation.Account2Wallet),Restrictions.eq("typeOperation",TypeOperation.Wallet2Account))).add(Restrictions.le("datetime",DateUtils.addMinutes(new Date(),-params.getPeriodeLancementRobot()))), OrderContainer.getInstance().add(Order.asc("date")), null, 0, -1);
				
		/*
		int copt = 0;
		for(Transaction t : trans){
			Long delai = OgMoHelper.getNbreMinutesBetween(t.getDatetime(), new Date());
			if(params.getDelaiReconciliation() < delai){
				alerte = alerte + " **** " + t.getPhoneNumber() + " - " + t.getExternalRefNo();
				copt++;
			}
		}
		 */

		try {
			
			Query querry = dao.getEntityManager().createQuery("select t from Transaction t where datetime<= :datetime and completed= :completed and posted= :posted and status= :status and (typeOperation= :typeOperation1 or typeOperation= :typeOperation2) order by datetime asc");
			querry.setParameter("datetime",DateUtils.addMinutes(new Date(),-15));
			querry.setParameter("completed",Boolean.FALSE);
			querry.setParameter("posted",Boolean.FALSE);
			querry.setParameter("status",TransactionStatus.SUCCESS);
			querry.setParameter("typeOperation1",TypeOperation.Account2Wallet);
			querry.setParameter("typeOperation2",TypeOperation.Wallet2Account);
			
			trans = querry.getResultList();
			
			if(trans.size() > Integer.parseInt(params.getSeuilReconciliation())){
				//Alerte des utilisateurs par SMS
				for(String s : params.getNumeroAlerteSMS().split("-")){
					SMSWeb sMSWeb = new SMSWeb("OGMo-01", "Orange Money", "AUTO", trans.size() + " TRANSACTIONS NON RECONCILIEES, DEPUIS PLUS DE " + params.getDelaiReconciliation() + " min", "237" + s);
					dao.save(sMSWeb);
				}
			}

		} catch(Exception ex) {
			alerte = "";
		}

		return Integer.toString(trans.size());
	}



	public String checkRobotReconciliation() throws Exception{

		Parameters params = findParameters();
		String alerte = "";

		if(params == null) return alerte;

		if(StringUtils.isBlank(params.getNumeroAlerteSMS()) || params.getNumeroAlerteSMS().trim().length() < 9) return alerte;

		if(StringUtils.isBlank(Integer.toString(params.getDelaiRobot()))) return alerte;

		String req = "From TraceRobot t where t.typeExecution = :type and t.id = (select max(t2.id) From TraceRobot t2) ";
		TraceRobot obj = (TraceRobot) dao.getEntityManager().createQuery(req).setParameter("type", "RECONCILIATION-TRANSACTION").getSingleResult();

		try {

			if(obj != null){
				Long delai = OgMoHelper.getNbreMinutesBetween(obj.getDatetimeTrace(), new Date());
				if(params.getPeriodeLancementRobot() < delai){

					for(String s : params.getNumeroAlerteSMS().split("-")){
						alerte =  "MAC ORANGE :  LE ROBOT DE RECONCILIATION S'EST ARRETE DEPUIS PLUS DE " + delai + " min";
						SMSWeb sMSWeb = new SMSWeb("OGMo-01", "Orange Money", "AUTO", alerte, "237" + s);
						dao.save(sMSWeb);
					}
				}
			}

		} catch(Exception ex) {
			alerte = "";
		}

		return alerte;
	}



	public List<Transaction> checkTransactionSubscriber(Subscriber subscriber) throws Exception{

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH':'mm':'ss");
		RestrictionsContainer rc = RestrictionsContainer.getInstance().add(Restrictions.between("datetime", sdf.parseObject(new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + " 00:00:00"), sdf.parseObject(new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + " 23:59:00")));
		rc.add(Restrictions.eq("subscriber", subscriber));
		rc.add(Restrictions.eq("status", TransactionStatus.SUCCESS));

		return dao.filter(Transaction.class, null, rc, OrderContainer.getInstance().add(Order.desc("datetime")), null, 0, -1);

	}


	public List<Transaction> getTransactionTFJOs(Integer limit) throws Exception {

		setTFJOPortalEnCours(Boolean.TRUE);

		Parameters params = findParameters();

		/*
		List<Transaction> trans = new ArrayList<Transaction>();
		if(Boolean.TRUE.equals(params.getDoReconciliation())){
			trans = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("status", TransactionStatus.SUCCESS)).add(Restrictions.eq("posted", Boolean.FALSE)).add(Restrictions.eq("completed", Boolean.TRUE)).add(Restrictions.or(Restrictions.eq("typeOperation",TypeOperation.Account2Wallet),Restrictions.eq("typeOperation",TypeOperation.Wallet2Account))), OrderContainer.getInstance().add(Order.asc("date")), null, 0, limit);
		}
		else{
			trans = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("status", TransactionStatus.SUCCESS)).add(Restrictions.eq("posted", Boolean.FALSE)).add(Restrictions.or(Restrictions.eq("typeOperation",TypeOperation.Account2Wallet),Restrictions.eq("typeOperation",TypeOperation.Wallet2Account))), OrderContainer.getInstance().add(Order.asc("date")), null, 0, limit);
		}

		List<Transaction> trans2 = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("status", TransactionStatus.SUCCESS)).add(Restrictions.eq("posted", Boolean.FALSE)).add(Restrictions.in("typeOperation", TypeOperation.getTypeNonReconciliable())), OrderContainer.getInstance().add(Order.asc("date")), null, 0, limit);
		trans.addAll(trans2);
		if(trans.isEmpty()) 
			return new ArrayList<Transaction>();	
		*/
		
		// Initialisation d'un conteneur de restrictions
		System.out.println("*************** getTransactionTFJOs 1 *************** : ");
		RestrictionsContainer rc = RestrictionsContainer.getInstance().add(Restrictions.eq("status", TransactionStatus.SUCCESS)).add(Restrictions.eq("posted", Boolean.FALSE));

		OrderContainer orders = OrderContainer.getInstance().add(Order.asc("date"));
		// Ajout des restrictions au conteneur
		if(Boolean.TRUE.equals(params.getDoReconciliation())){
			System.out.println("*************** getTransactionTFJOs 2 *************** : ");
			rc.add(Restrictions.or(Restrictions.and(Restrictions.in("typeOperation", TypeOperation.getTypeReconciliable()), Restrictions.eq("completed", Boolean.TRUE)), Restrictions.in("typeOperation", TypeOperation.getTypeNonReconciliable())));
		}
		else{
			System.out.println("*************** getTransactionTFJOs 3 *************** : ");
			rc.add(Restrictions.or(Restrictions.in("typeOperation", TypeOperation.getTypeReconciliable()), Restrictions.in("typeOperation", TypeOperation.getTypeNonReconciliable())));
		}

		System.out.println("*************** getTransactionTFJOs 4 *************** : ");
		return dao.filter(Transaction.class, null, rc, orders, null, 0, limit);
	}
	
	
	
	public List<Transaction> getTransactionTFJOStatusProcess() throws Exception {

		return dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("tfjoStatus", TransactionStatus.PROCESSING.name())), OrderContainer.getInstance().add(Order.asc("date")), null, 0, -1);
		
	}



	@SuppressWarnings("unchecked")
	public List<bkmvti> genererEcritureTFJOs(List<Transaction> trans) throws Exception {

		List<bkmvti> resultats = new ArrayList<bkmvti>();

		details = new ArrayList<FactMonthDetails>();
		mntD = 0d; mntC = 0d; nbrC = 0; nbrD = 0;

		//SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
		Parameters params = findParameters();
		int numEc = 1; 
		String datop = new SimpleDateFormat("ddMMyy").format(new Date());

		if(dsCBS == null) findCBSDataSystem();

		Date dco = getDateComptable();


		// Recuperation du compte Orange
		////System.out.println("-----ORG------"+params.getNcpOrange());
		////System.out.println("-----SQL------"+OgMoHelper.getDefaultCBSQueries().get(8).getQuery());
		ResultSet rsCpteOrange = executeFilterSystemQuery(dsCBS, "select bkcom.age, bkcom.dev, bkcom.cha, bkcom.ncp, bkcom.suf, bkcom.clc, bkcom.dva, bkcom.sde, bkcom.inti, bkcom.utic, bkcli.nom, bkcli.ges from bkcom, bkcli where bkcli.cli = bkcom.cli and bkcom.age = ? and bkcom.ncp = ? and bkcom.clc = ?", new Object[]{ params.getNcpCliOrange().split("-")[0], params.getNcpCliOrange().split("-")[1], params.getNcpCliOrange().split("-")[2] });

		// Recuperation du compte Orange
		////System.out.println("-----A2W------"+params.getNcpDAPAcountWalet());
		////System.out.println("-----SQL------"+OgMoHelper.getDefaultCBSQueries().get(8).getQuery());
		ResultSet rsCpteDAPA2W = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(8).getQuery(), new Object[]{ params.getNcpDAPAcountWalet().split("-")[0], params.getNcpDAPAcountWalet().split("-")[1], params.getNcpDAPAcountWalet().split("-")[2] });

		// Recuperation du compte Orange
		////System.out.println("-----ORG------"+params.getNcpDAPWaletAcount());
		////System.out.println("-----SQL------"+OgMoHelper.getDefaultCBSQueries().get(8).getQuery());
		ResultSet rsCpteDAPW2A = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(8).getQuery(), new Object[]{ params.getNcpDAPWaletAcount().split("-")[0], params.getNcpDAPWaletAcount().split("-")[1], params.getNcpDAPWaletAcount().split("-")[2] });

		if(!rsCpteOrange.next()) {
			throw new Exception("Comptes de Orange inexistants");
		}
		if(!rsCpteDAPA2W.next()) {
			throw new Exception("Comptes de DAP A2W inexistants");
		}
		if(!rsCpteDAPW2A.next()) {
			throw new Exception("Comptes de DAP W2A inexistants");
		}


		/*************************************************************************/
		/**-------GENERATION DES EC DE COMPENSATION DANS LE COMPTE DE ORANGE ---**/
		/*************************************************************************/


		boolean poster = false;


		// S'il n'ya aucune transaction on sort
		if(trans == null || trans.isEmpty()) return null;

		// Initialisation du filtre
		String in = "";



		//*************** DEBIT DU CREDIT DU COMPTE ORANGE ***************

		// Recuperation du dernier numero evenement du type operation
		ResultSet rs = executeFilterSystemQuery(dsCBS, "select max(eve) as num from bkeve where ope=?", new Object[]{ params.getCodeOperation() });
		Long numEve = rs != null && rs.next() ? numEve = Long.valueOf( rs.getString("num") != null ? rs.getString("num").trim() : "1" )  + 1 : 1l;
		CloseStream.fermeturesStream(rs);

		bkeve eve = new bkeve(null, params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEve), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), "001", 0d, "VIORMO", new Date(), params.getCodeUtil(), 0d, 0d, 0d, 0d );
		eve.setEta("IG");


		System.out.println("*************** genererEcritureTFJOs Transaction tx : trans ***************");
		for(Transaction tx : trans) {

			// Recuperation  de l'id de chaque transaction selectionnee
			if(tx.isSelected() && tx.getStatus().equals(TransactionStatus.SUCCESS) && (poster ? !tx.getPosted().booleanValue() : true) ) { in += tx.getId() + ", "; }


			if(tx.getSubscriber().getProfil() != null){

				if(tx.getTypeOperation().equals(TypeOperation.Account2Wallet)){
					// Debit du Cpte DAP des A2W pour les cas de transactions Marchands
					String lib = "MRCH/" + tx.getPhoneNumber() + "/" + new SimpleDateFormat("ddMMyyHHmm").format(tx.getDate()) + "/A2W";

					// Recuperation du compte 
					ProfilMarchands plg = tx.getSubscriber().getProfil();
					ResultSet rsCpteDAPA2WMarch = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(8).getQuery(), new Object[]{ plg.getNcpDAPPull().split("-")[0], plg.getNcpDAPPull().split("-")[1], plg.getNcpDAPPull().split("-")[2] });

					if(!rsCpteDAPA2WMarch.next() ) {
						throw new Exception("Comptes de Liaisons inexistants");
					}

					String pie = RandomStringUtils.randomNumeric(6);
					eve.getEcritures().add( new bkmvti(rsCpteDAPA2WMarch.getString("age"), rsCpteDAPA2WMarch.getString("dev"), rsCpteDAPA2WMarch.getString("cha"), rsCpteDAPA2WMarch.getString("ncp"), rsCpteDAPA2WMarch.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteDAPA2WMarch.getString("clc"), dco, null, rsCpteDAPA2WMarch.getDate("dva"), tx.getAmount(), "D", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteDAPA2WMarch.getString("age"), rsCpteDAPA2WMarch.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;
					eve.getEcritures().add( new bkmvti(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("cha"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteOrange.getString("clc"), dco, null, rsCpteOrange.getDate("dva"), tx.getAmount(), "C", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;

					//Ecritures comptables pour les comptes de liaison
					if(!rsCpteDAPA2WMarch.getString("age").equalsIgnoreCase(rsCpteOrange.getString("age"))){
						// Debit de la liaison du client du HT
						ResultSet rsCpteLiaisonDAP = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteDAPA2WMarch.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsCpteLiaisonDAP != null){
							rsCpteLiaisonDAP.next();
							eve.getEcritures().add( new bkmvti(rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), rsCpteLiaisonDAP.getString("cha"), rsCpteLiaisonDAP.getString("ncp"), rsCpteLiaisonDAP.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaisonDAP.getString("clc"), dco, null, rsCpteLiaisonDAP.getDate("dva"), tx.getAmount(), "C",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}
						// Credit de la liaison d'hippodrome du HT
						ResultSet rsLiaisonOrange = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteOrange.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsLiaisonOrange != null){ 
							rsLiaisonOrange.next();
							eve.getEcritures().add( new bkmvti(rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), rsLiaisonOrange.getString("cha"), rsLiaisonOrange.getString("ncp"), rsLiaisonOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonOrange.getString("clc"), dco, null, rsLiaisonOrange.getDate("dva"), tx.getAmount(), "D",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}
												
						CloseStream.fermeturesStream(rsCpteLiaisonDAP); rsCpteLiaisonDAP = null;
						CloseStream.fermeturesStream(rsLiaisonOrange); rsLiaisonOrange = null;
					}

					CloseStream.fermeturesStream(rsCpteDAPA2WMarch); rsCpteDAPA2WMarch = null;
				}
				else if(tx.getTypeOperation().equals(TypeOperation.Wallet2Account)){
					// Credit du DAP des W2A pour les cas de transactions Marchands
					String lib = "MRCH/" + tx.getPhoneNumber() + "/" + new SimpleDateFormat("ddMMyyHHmm").format(tx.getDate()) + "/W2A";

					// Recuperation du compte 
					ProfilMarchands plg = tx.getSubscriber().getProfil();
					ResultSet rsCpteDAPW2AMarch = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(8).getQuery(), new Object[]{ plg.getNcpDAPPush().split("-")[0], plg.getNcpDAPPush().split("-")[1], plg.getNcpDAPPush().split("-")[2] });
					
					if(!rsCpteDAPW2AMarch.next() ) {
						throw new Exception("Comptes de Liaisons inexistants");
					}

					String pie = RandomStringUtils.randomNumeric(6);
					eve.getEcritures().add( new bkmvti(rsCpteDAPW2AMarch.getString("age"), rsCpteDAPW2AMarch.getString("dev"), rsCpteDAPW2AMarch.getString("cha"), rsCpteDAPW2AMarch.getString("ncp"), rsCpteDAPW2AMarch.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteDAPW2AMarch.getString("clc"), dco, null, rsCpteDAPW2AMarch.getDate("dva"), tx.getAmount(), "C", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteDAPW2AMarch.getString("age"), rsCpteDAPW2AMarch.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;					
					eve.getEcritures().add( new bkmvti(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("cha"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteOrange.getString("clc"), dco, null, rsCpteOrange.getDate("dva"), tx.getAmount(), "D", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;

					//Ecritures comptables pour les comptes de liaison
					if(!rsCpteDAPW2AMarch.getString("age").equalsIgnoreCase(rsCpteOrange.getString("age"))){
						// Debit de la liaison du client du HT
						ResultSet rsCpteLiaisonDAP = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteDAPW2AMarch.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsCpteLiaisonDAP != null){
							rsCpteLiaisonDAP.next();
							eve.getEcritures().add( new bkmvti(rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), rsCpteLiaisonDAP.getString("cha"), rsCpteLiaisonDAP.getString("ncp"), rsCpteLiaisonDAP.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaisonDAP.getString("clc"), dco, null, rsCpteLiaisonDAP.getDate("dva"), tx.getAmount(), "D",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}
						// Credit de la liaison d'hippodrome du HT
						ResultSet rsLiaisonOrange = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteOrange.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsLiaisonOrange != null){ 
							rsLiaisonOrange.next();
							eve.getEcritures().add( new bkmvti(rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), rsLiaisonOrange.getString("cha"), rsLiaisonOrange.getString("ncp"), rsLiaisonOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonOrange.getString("clc"), dco, null, rsLiaisonOrange.getDate("dva"), tx.getAmount(), "C",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}
												
						CloseStream.fermeturesStream(rsCpteLiaisonDAP); rsCpteLiaisonDAP = null;
						CloseStream.fermeturesStream(rsLiaisonOrange); rsLiaisonOrange = null;
					}

					CloseStream.fermeturesStream(rsCpteDAPW2AMarch); rsCpteDAPW2AMarch = null;
				}

			}
			else{

				if(tx.getTypeOperation().equals(TypeOperation.Account2Wallet)){
					// Debit du Cpte DAP des A2W pour les cas de transactions Ends-users
					String lib = tx.getPhoneNumber() + "/" + new SimpleDateFormat("ddMMyyHHmm").format(tx.getDate()) + "/A2W";

					String pie = RandomStringUtils.randomNumeric(6);
					eve.getEcritures().add( new bkmvti(rsCpteDAPA2W.getString("age"), rsCpteDAPA2W.getString("dev"), rsCpteDAPA2W.getString("cha"), rsCpteDAPA2W.getString("ncp"), rsCpteDAPA2W.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteDAPA2W.getString("clc"), dco, null, rsCpteDAPA2W.getDate("dva"), tx.getAmount(), "D", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteDAPA2W.getString("age"), rsCpteDAPA2W.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;
					eve.getEcritures().add( new bkmvti(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("cha"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteOrange.getString("clc"), dco, null, rsCpteOrange.getDate("dva"), tx.getAmount(), "C", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;

					//Ecritures comptables pour les comptes de liaison
					if(!rsCpteDAPA2W.getString("age").equalsIgnoreCase(rsCpteOrange.getString("age"))){
						// Debit de la liaison du client du HT
						ResultSet rsCpteLiaisonDAP = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteDAPA2W.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsCpteLiaisonDAP != null){
							rsCpteLiaisonDAP.next();
							eve.getEcritures().add( new bkmvti(rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), rsCpteLiaisonDAP.getString("cha"), rsCpteLiaisonDAP.getString("ncp"), rsCpteLiaisonDAP.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaisonDAP.getString("clc"), dco, null, rsCpteLiaisonDAP.getDate("dva"), tx.getAmount(), "C",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}
						// Credit de la liaison d'hippodrome du HT
						ResultSet rsLiaisonOrange = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteOrange.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsLiaisonOrange != null){ 
							rsLiaisonOrange.next();
							eve.getEcritures().add( new bkmvti(rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), rsLiaisonOrange.getString("cha"), rsLiaisonOrange.getString("ncp"), rsLiaisonOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonOrange.getString("clc"), dco, null, rsLiaisonOrange.getDate("dva"), tx.getAmount(), "D",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}
						
						CloseStream.fermeturesStream(rsCpteLiaisonDAP); rsCpteLiaisonDAP = null;
						CloseStream.fermeturesStream(rsLiaisonOrange); rsLiaisonOrange = null;
					}
				}
				else if(tx.getTypeOperation().equals(TypeOperation.Wallet2Account)){
					// Credit du DAP des W2A pour les cas de transactions Ends-users
					String lib = tx.getPhoneNumber() + "/" + new SimpleDateFormat("ddMMyyHHmm").format(tx.getDate()) + "/W2A";

					String pie = RandomStringUtils.randomNumeric(6);
					eve.getEcritures().add( new bkmvti(rsCpteDAPW2A.getString("age"), rsCpteDAPW2A.getString("dev"), rsCpteDAPW2A.getString("cha"), rsCpteDAPW2A.getString("ncp"), rsCpteDAPW2A.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteDAPW2A.getString("clc"), dco, null, rsCpteDAPW2A.getDate("dva"), tx.getAmount(), "C", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteDAPW2A.getString("age"), rsCpteDAPW2A.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;					
					eve.getEcritures().add( new bkmvti(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("cha"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteOrange.getString("clc"), dco, null, rsCpteOrange.getDate("dva"), tx.getAmount(), "D", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;

					//Ecritures comptables pour les comptes de liaison
					if(!rsCpteDAPW2A.getString("age").equalsIgnoreCase(rsCpteOrange.getString("age"))){
						// Debit de la liaison du client du HT
						ResultSet rsCpteLiaisonDAP = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteDAPW2A.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsCpteLiaisonDAP != null){
							rsCpteLiaisonDAP.next();
							eve.getEcritures().add( new bkmvti(rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), rsCpteLiaisonDAP.getString("cha"), rsCpteLiaisonDAP.getString("ncp"), rsCpteLiaisonDAP.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaisonDAP.getString("clc"), dco, null, rsCpteLiaisonDAP.getDate("dva"), tx.getAmount(), "D",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}
						// Credit de la liaison d'hippodrome du HT
						ResultSet rsLiaisonOrange = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteOrange.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsLiaisonOrange != null){ 
							rsLiaisonOrange.next();
							eve.getEcritures().add( new bkmvti(rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), rsLiaisonOrange.getString("cha"), rsLiaisonOrange.getString("ncp"), rsLiaisonOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonOrange.getString("clc"), dco, null, rsLiaisonOrange.getDate("dva"), tx.getAmount(), "C",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}

						CloseStream.fermeturesStream(rsCpteLiaisonDAP); rsCpteLiaisonDAP = null;
						CloseStream.fermeturesStream(rsLiaisonOrange); rsLiaisonOrange = null;
					}
				}

			}

		}

		// Construction du filtre sur les transactions
		if(!in.isEmpty()) in = "(" + in.substring(0, in.length()-2) + ")";

		// Si aucune transaction n'a ete selectionnee on sort
		if(in.isEmpty()) return null;

		// Execute et retourne la liste des ecritures comptables des transactions selectionnees
		System.out.println("*************** genererEcritureTFJOs resultats.addAll ***************");
		List<bkmvti> resultats1 = new ArrayList<bkmvti>();;
		resultats1 = (List<bkmvti>) dao.getEntityManager().createQuery("Select e.ecritures from bkeve e where e.transaction.id in "+ in +"").getResultList();
		resultats.addAll(resultats1);


		//*************** FIN DU CREDIT DU COMPTE ORANGE ***************

		System.out.println("*************** FIN DU CREDIT DU COMPTE ORANGE *************** : " + resultats.size());
		resultats.addAll(eve.getEcritures());

		// Sauvegarde l'evenement (Dans le Portal)
		//dao.save(eve);
		// On libere tous les ResultSet 
		CloseStream.fermeturesStream(rsCpteOrange); rsCpteOrange = null;
		CloseStream.fermeturesStream(rsCpteDAPA2W); rsCpteDAPA2W = null;
		CloseStream.fermeturesStream(rsCpteDAPW2A); rsCpteDAPW2A = null;
		
		//if(conCBS != null ) conCBS.close();
		resultats1.clear();
		System.out.println("*************** genererEcritureTFJOs resultats.size() *************** : " + resultats.size());


		return resultats;

	}



	public List<bkmvti> extractECFromTransactions(List<Transaction> transactions) throws Exception {

		/*
		// Recuperation de la liste des ecritures comptables des transactions selectionnees
		List<bkmvti> mvts = getECFromTransactions(transactions, false);
		System.out.println("*************** mvts.size() *************** : " + mvts.size());
		 */

		List<bkmvti> mvts = genererEcritureTFJOs(transactions);
		//*** System.out.println("*************** mvts.size() *************** : " + mvts.size());
		//mvts.addAll(ecritures);

		// S'il n'existe aucune ecriture on sort
		if(mvts == null || mvts.isEmpty()) return null;

		return mvts;
	}



	public List<bkmvti> extractECIntoFile(List<bkmvti> mvts, String fileName) throws Exception {

		// S'il n'existe aucune ecriture on sort
		if(mvts == null || mvts.isEmpty()) return null;

		// Initialisation du fichier a generer
		FileWriter fw = new FileWriter(fileName);

		// Parcours des ecritures
		//*** System.out.println("*************** extractECIntoFile fileName *************** : " + fileName);
		for(bkmvti mvt : mvts) {

			// Ecriture de la ligne dans le fichier
			fw.write(mvt.getFileLine().concat("\n"));

		}

		// Fermeture du fichier
		fw.flush(); fw.close();

		return mvts;
	}



	public FactMonth visualiserRapportTFJOTrans(List<Transaction> transactions,  Date dateAbon, String user, String mois, String fileName, Parameters param) throws Exception {

		System.out.println("*************** visualiserRapportTFJOTrans transactions.size()*************** : " + transactions.size());
		// S'il n'existe aucune transaction on sort
		if(transactions == null || transactions.isEmpty()) return null;
		List<bkmvti> mvts = new ArrayList<bkmvti>();
		if(transactions.size() > 3000 ){
			System.out.println("*************** visualiserRapportTFJOTrans transactions.size() > 1000 transactions.size() *************** : " + transactions.size());
			int i = 0;
			int cpt = 3000;
			int size = transactions.size();
			int reste = size % cpt;
			System.out.println("*************** reste *************** : " + reste);
			int nb = size / cpt;
			int j = 0;
			List<Transaction> trans = new ArrayList<Transaction>();
			while(nb > 0){
				System.out.println("*************** cpt *************** : " + cpt);
				j = j + cpt;
				System.out.println("*************** i *************** : " + i);
				System.out.println("*************** j *************** : " + j);
				trans = new ArrayList<Transaction>(transactions.subList(i,j));
				System.out.println("*************** trans.size() *************** : " + trans.size());
				mvts.addAll(genererEcritureTFJOs(trans));
				nb = nb - 1;
				i = j;
			}

			System.out.println("*************** i - j+reste *************** : " + i + " - " + (j+reste));				
			trans = new ArrayList<Transaction>(transactions.subList(i,j+reste));
			System.out.println("*************** trans.size() reste *************** : " + trans.size());
			mvts.addAll(genererEcritureTFJOs(trans));

		}else{
			System.out.println("*************** visualiserRapportTFJOTrans transactions.size() <= 1000 transactions.size() *************** : " + transactions.size());
			mvts.addAll(genererEcritureTFJOs(transactions));				
		}


		if(mvts.isEmpty() || mvts.size() == 0){
			return null;
		}


		List<FactMonthDetails> resultats = new ArrayList<FactMonthDetails>();

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();
		Date dco = getDateComptable();


		// Parcours des ecritures
		if(conCBS == null || conCBS.isClosed()) conCBS = getSystemConnection(dsCBS);

		// Suspension temporaire du mode blocage dans la BD du Core Banking
		if(dsCBS.getDbConnectionString().indexOf("informix") > 0) conCBS.createStatement().executeUpdate("SET ISOLATION TO DIRTY READ");

		stmCBS = conCBS.createStatement();
		Map<String,String> mapCompte = new HashMap<String, String>();


		// S'il n'existe aucune ecriture on sort
		if(mvts == null || mvts.isEmpty()) return null;

		// Initialisation du fichier a generer
		FileWriter fw = new FileWriter(fileName);


		ResultSet rs = null;
		System.out.println("*************** Initialisation du fichier a generer ***************");
		for(bkmvti mvt : mvts){

			mvt.setDco(dco);
			mvt.setOpe(param.getCodeOpeCompt());
			mvt.setUti(user);

			//mvt.setDva(dva);
			// Ecriture de la ligne dans le fichier
			if(!mvt.getDev().equalsIgnoreCase("001")){
				mvt.setDev("001");
			}

			// Recuperation 
			FactMonthDetails det = new FactMonthDetails(Integer.valueOf(mvt.getAge()), mvt.getNcp()+"-"+mvt.getClc(),mvt.getIntitule(), mvt.getLib(),mvt.getInitDate(),mvt.getDateFact() ,mvt.getDatedebutFact(),mvt.getSen(),mvt.getMon());
			det.setLibage(mvt.getLibage());
			det.setTxtage(mvt.getAge());
			det.setDateAbon(dateAbon);
			det.setDev(mvt.getDev());
			String cle = mvt.getAge().trim()+mvt.getNcp().trim()+mvt.getClc().trim();
			if(mapCompte.containsKey(cle)){
				det.setIntitule(mapCompte.get(cle));
			}else{
				rs = executeFilterSystemQuery(dsCBS,OgMoHelper.getDefaultCBSQueries().get(6).getQuery(),new Object[]{mvt.getAge(),mvt.getNcp(),mvt.getClc()});
				if(rs.next()){
					det.setIntitule(rs.getString("nom"));
					if(mapCompte.isEmpty()){
						mapCompte.put(cle, rs.getString("nom"));
					}else if(!mapCompte.containsKey(cle)){
						mapCompte.put(cle, rs.getString("nom"));
					}
				}else {
					CloseStream.fermeturesStream(rs);
					rs = executeFilterSystemQuery(dsCBS,OgMoHelper.getDefaultCBSQueries().get(7).getQuery(),new Object[]{mvt.getAge(),mvt.getNcp(),mvt.getClc()});
					if(rs.next()){
						det.setIntitule(rs.getString("inti"));
						if(mapCompte.isEmpty()){
							mapCompte.put(cle, rs.getString("inti"));
						}else if(!mapCompte.containsKey(cle)){
							mapCompte.put(cle, rs.getString("inti"));
						}
					}
				}
			}
			resultats.add(det);
			if("C".equalsIgnoreCase(mvt.getSen())){
				mntC = mntC+mvt.getMon();
				nbrC++;
			}else{
				mntD = mntD+mvt.getMon();
				nbrD++;
			}
			dev = mvt.getDev();

			// Ecriture de la ligne dans le fichier
			fw.write(mvt.getFileLine().concat("\n"));

		}


		// Fermeture du fichier
		fw.flush(); fw.close();

		CloseStream.fermeturesStream(rs);

		//if(conCBS != null ) conCBS.close();


		Collections.sort(resultats);
		FactMonth fac = new FactMonth(dco, "D", "C", mntD, mntC, nbrD, nbrC, user, new Date(), mois);
		fac.setDev(dev);
		fac = dao.save(fac);
		for(FactMonthDetails det : resultats) det.setParent(fac);


		if(resultats.size() > 500 ){
			System.out.println("*************** resultats.size() > 500 resultats.size() *************** : " + resultats.size());
			int i = 0;
			int cpt = 500;
			int size = resultats.size();
			int reste = size % cpt;
			System.out.println("*************** reste *************** : " + reste);
			int nb = size / cpt;
			int j = 0;
			List<FactMonthDetails> results = new ArrayList<FactMonthDetails>();
			while(nb > 0){
				System.out.println("*************** cpt *************** : " + cpt);
				j = j + cpt;
				System.out.println("*************** i *************** : " + i);
				System.out.println("*************** j *************** : " + j);
				results = new ArrayList<FactMonthDetails>(resultats.subList(i,j));
				System.out.println("*************** results.size() *************** : " + results.size());
				dao.saveList(results,true);
				nb = nb - 1;
				i = j;
				results = new ArrayList<FactMonthDetails>();
			}

			results = new ArrayList<FactMonthDetails>(resultats.subList(i,j+reste));
			System.out.println("*************** results.size() reste *************** : " + results.size());
			dao.saveList(results,true);

		}else{
			System.out.println("*************** resultats.size() <= 500 resultats.size() *************** : " + resultats.size());
			dao.saveList(resultats,true);			
		}



		fac.setDetails(resultats);

		// Execution du Timer de Contrôle de la fin des TFJ
		// TFJScheduler();

		return fac;

	}



	public FactMonth visualiserRapportTFJOs(List<bkmvti> mvts, Date dateAbon, String user, String mois, String fileName, Parameters param) throws Exception {

		List<FactMonthDetails> resultats = new ArrayList<FactMonthDetails>();
		List<bkmvtiPush> entries = new ArrayList<bkmvtiPush>();
		List<String> codeOpes = new ArrayList<String>();


		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();
		Date dco = getDateComptable();


		// Parcours des ecritures
		if(conCBS == null || conCBS.isClosed()) conCBS = getSystemConnection(dsCBS);

		// Suspension temporaire du mode blocage dans la BD du Core Banking
		if(dsCBS.getDbConnectionString().indexOf("informix") > 0) conCBS.createStatement().executeUpdate("SET ISOLATION TO DIRTY READ");

		stmCBS = conCBS.createStatement();
		Map<String,String> mapCompte = new HashMap<String, String>();


		// S'il n'existe aucune ecriture on sort
		if(mvts == null || mvts.isEmpty()) return null;

		// Initialisation du fichier a generer
		//*** FileWriter fw = new FileWriter(fileName);

		String ope = param.getCodeOpeCompt();
		codeOpes.add(ope);

		ResultSet rs = null;
		System.out.println("*************** Initialisation du fichier a generer ***************");
		for(bkmvti mvt : mvts){

			mvt.setDco(dco);
			mvt.setOpe(ope);
			mvt.setUti(user);

			//mvt.setDva(dva);
			// Ecriture de la ligne dans le fichier
			if(!mvt.getDev().equalsIgnoreCase("001")){
				mvt.setDev("001");
			}

			// Recuperation 
			FactMonthDetails det = new FactMonthDetails(Integer.valueOf(mvt.getAge()), mvt.getNcp()+"-"+mvt.getClc(),mvt.getIntitule(), mvt.getLib(),mvt.getInitDate(),mvt.getDateFact() ,mvt.getDatedebutFact(),mvt.getSen(),mvt.getMon());
			det.setLibage(mvt.getLibage());
			det.setTxtage(mvt.getAge());
			det.setDateAbon(dateAbon);
			det.setDev(mvt.getDev());
			String cle = mvt.getAge().trim()+mvt.getNcp().trim()+mvt.getClc().trim();
			if(mapCompte.containsKey(cle)){
				det.setIntitule(mapCompte.get(cle));
			}else{
				rs = executeFilterSystemQuery(dsCBS,OgMoHelper.getDefaultCBSQueries().get(6).getQuery(),new Object[]{mvt.getAge(),mvt.getNcp(),mvt.getClc()});
				if(rs.next()){
					det.setIntitule(rs.getString("nom"));
					if(mapCompte.isEmpty()){
						mapCompte.put(cle, rs.getString("nom"));
					}else if(!mapCompte.containsKey(cle)){
						mapCompte.put(cle, rs.getString("nom"));
					}
				}else {
					CloseStream.fermeturesStream(rs);
					rs = executeFilterSystemQuery(dsCBS,OgMoHelper.getDefaultCBSQueries().get(7).getQuery(),new Object[]{mvt.getAge(),mvt.getNcp(),mvt.getClc()});
					if(rs.next()){
						det.setIntitule(rs.getString("inti"));
						if(mapCompte.isEmpty()){
							mapCompte.put(cle, rs.getString("inti"));
						}else if(!mapCompte.containsKey(cle)){
							mapCompte.put(cle, rs.getString("inti"));
						}
					}
				}
			}
			resultats.add(det);
			if("C".equalsIgnoreCase(mvt.getSen())){
				mntC = mntC+mvt.getMon();
				nbrC++;
			}else{
				mntD = mntD+mvt.getMon();
				nbrD++;
			}
			dev = mvt.getDev();

			entries.add(new bkmvtiPush(mvt));

			// Ecriture de la ligne dans le fichier
			//*** fw.write(mvt.getFileLine().concat("\n"));

		}

		// Fermeture du fichier
		//*** fw.flush(); fw.close();

		CloseStream.fermeturesStream(rs);
		
		try {
				boolean b = Boolean.FALSE;
				b = sendAccountsEntriesToCoreBanking(mvts);
				if(!b) {
					return null;
				}
				else {
					
					//Construction du rapport
					FactMonth fac = new FactMonth(dco, "D", "C", mntD, mntC, nbrD, nbrC, user, new Date(), mois);
					Collections.sort(resultats);
					fac.setDev(dev);
					fac = dao.save(fac);
					for(FactMonthDetails det : resultats) det.setParent(fac);

					if(resultats.size() > 1000 ){
						int i = 0;
						int cpt = 1000;
						int size = resultats.size();
						int reste = size % cpt;
						int nb = size / cpt;
						int j = 0;
						List<FactMonthDetails> results = new ArrayList<FactMonthDetails>();
						while(nb > 0){
							j = j + cpt;
							results = new ArrayList<FactMonthDetails>(resultats.subList(i,j));
							dao.saveList(results,true);
							nb = nb - 1;
							i = j;
						}

						results = new ArrayList<FactMonthDetails>(resultats.subList(i,j+reste));
						dao.saveList(results,true);

					}else{
						dao.saveList(resultats,true);			
					}

					fac.setDetails(resultats);
					return fac;
				}
				
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}



	public List<String> annulerEves(List<Transaction> transactions) throws Exception{

		return cancelEvenements(transactions, dsCBS);
	}



	//@SuppressWarnings("rawtypes")
	@SuppressWarnings("unchecked")
	public List<String> cancelEvenements(List<Transaction> transactions, DataSystem dsCBS) throws Exception {

		if(transactions == null || transactions.isEmpty()) return null;
		List<String> __eves = new ArrayList();
		List eves = new ArrayList();
		// Initialisation du filtre des evenements et du code operation
		String inEve = ""; String ope = "";

		// Initialisation de la restriction in
		String in = ""; int n = 0;


		for( int i=0; i<transactions.size(); i++ ){
			
			// Maj de la transaction
			/*
			transactions.get(i).setPosted(Boolean.TRUE);
			transactions.get(i).setDateTraitement(new Date());
			transactions.get(i).setaRetraiter(Boolean.FALSE);
			transactions.get(i).setTfjoStatus(TransactionStatus.SUCCESS.name());	
			*/

			in += transactions.get(i).getId() + ", ";
			inEve += transactions.get(i).getId() + ", ";
			n++;

			// Si on a deja atteind 1000 transactions selectionnees
			if(n>0 && n%1000==0) {

				// Construction de la liste des criteres
				in = "(".concat(in.substring(0, in.length()-2)).concat(")");

				// Recuperation de la liste des evenements associes aux transactions selectionnees
				//				eves.addAll(mobileMoneyDAO.getEntityManager().createQuery("Select e.eve, e.ope From bkeve e where e.transaction.id in "+ in +"").getResultList() ) ;
				//to API 09-2020
				eves.addAll(dao.getEntityManager().createQuery("Select e.id From bkeve e where e.transaction.id in "+ in +"").getResultList() ) ;

				// Reinitialisation de la variable des criteres
				in = ""; n = 0;
			}
			
		}

		if(!in.isEmpty() && in.length()>2) {
			in = "(".concat(in.substring(0, in.length()-2)).concat(")");

			// Recuperation de la liste des evenements associes aux transactions selectionnees
			//			eves.addAll(mobileMoneyDAO.getEntityManager().createQuery("Select e.eve, e.ope From bkeve e where e.transaction.id in "+ in +"").getResultList() ) ;
			//to API 09-2020
			eves.addAll(dao.getEntityManager().createQuery("Select e.id From bkeve e where e.transaction.id in "+ in +"").getResultList() ) ;
		
		}
		
		//MAJ du statut des transactions en BD
		if(!inEve.isEmpty() && inEve.length()>2) {
			inEve = "(".concat(inEve.substring(0, inEve.length()-2)).concat(")");
			dao.getEntityManager().createQuery("Update Transaction t set t.posted=:posted, t.aRetraiter=:aRetraiter, t.tfjoStatus=:tfjoStatus, dateTraitement=:dateTraitement where t.id in "+ inEve +"").setParameter("posted", Boolean.TRUE).setParameter("aRetraiter", Boolean.FALSE).setParameter("tfjoStatus", "SUCCESS").setParameter("dateTraitement", new Date()).executeUpdate();
		}


		// Si aucun evenement trouve on sort
		if(eves == null || eves.isEmpty()) return null;

		
		/*
		if(transactions.size() > 3000 ){
			int i = 0;
			int cpt = 3000;
			int size = transactions.size();
			int reste = size % cpt;
			int nb = size / cpt;
			int j = 0;
			List<Transaction> trans = new ArrayList<Transaction>();
			while(nb > 0){
				j = j + cpt;
				trans = transactions.subList(i,j);
				dao.saveList(trans, Boolean.TRUE);
				nb = nb - 1;
				i = j;
			}

			trans = transactions.subList(i,j+reste);
			dao.saveList(trans, Boolean.TRUE);

		}else{
			dao.saveList(transactions, Boolean.TRUE);
		}
		*/

		setTFJOPortalEnCours(Boolean.FALSE);

		for(Object str : eves) {
			__eves.add(String.valueOf(str));
		}
		
		 try {
			boolean b = registerIgnoreEventsToCoreBanking(__eves);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return __eves;

	}






	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.orangemoney.business.IOrangeMoneyManagerRemote#executerCompensation(java.util.Date, java.util.Date, java.lang.String, java.lang.String)
	 */
	@Override
	@AllowedRole(name = "executerCompensation", displayName = "OgMo.ExecuterCompensation")
	public FactMonth executerCompensation(Date deb, Date fin,String user , String mois) throws Exception {

		details = new ArrayList<FactMonthDetails>();
		mntD = 0d; mntC = 0d; nbrC = 0; nbrD = 0;

		//SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
		Parameters params = findParameters();
		Double totalA2W = 0d, totalW2A = 0d;
		Double totalA2WMarch = 0d, totalW2AMarch = 0d;
		int numEc = 1; 
		String datop = new SimpleDateFormat("ddMMyy").format(new Date());

		if(dsCBS == null) findCBSDataSystem();

		Date dco = getDateComptable();

		List<Transaction> trans = null;
		if(Boolean.TRUE.equals(params.getDoReconciliation())){
			trans = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("status", TransactionStatus.SUCCESS)).add(Restrictions.eq("posted", Boolean.FALSE)).add(Restrictions.eq("completed", Boolean.TRUE)).add(Restrictions.or(Restrictions.eq("typeOperation",TypeOperation.Account2Wallet),Restrictions.eq("typeOperation",TypeOperation.Wallet2Account))), OrderContainer.getInstance().add(Order.asc("date")), null, 0, -1);
		}
		else{
			trans = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("status", TransactionStatus.SUCCESS)).add(Restrictions.eq("posted", Boolean.FALSE)).add(Restrictions.or(Restrictions.eq("typeOperation",TypeOperation.Account2Wallet),Restrictions.eq("typeOperation",TypeOperation.Wallet2Account))), OrderContainer.getInstance().add(Order.asc("date")), null, 0, -1);
		}

		List<Transaction> trans2 = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("status", TransactionStatus.SUCCESS)).add(Restrictions.eq("posted", Boolean.FALSE)).add(Restrictions.in("typeOperation", TypeOperation.getTypeNonReconciliable())), OrderContainer.getInstance().add(Order.asc("date")), null, 0, -1);
		trans.addAll(trans2);
		if(trans.isEmpty()) return null;

		// Calcul des montants totaux des transactions

		int i=0;
		String message = "";
		for(Transaction tx : trans){
			if(Boolean.TRUE.equals(tx.isCompleted())){
				tx.setSelected(true);
				if(tx.getSubscriber().getProfil() != null){
					totalA2WMarch += tx.getSubscriber().getProfil() != null && tx.getTypeOperation().equals(TypeOperation.Account2Wallet) ? tx.getAmount() : 0d;
					totalW2AMarch += tx.getSubscriber().getProfil() != null && tx.getTypeOperation().equals(TypeOperation.Wallet2Account) ? tx.getAmount() : 0d;
				}
				else{
					totalA2W += tx.getTypeOperation().equals(TypeOperation.Account2Wallet) ? tx.getAmount() : 0d;
					totalW2A += tx.getTypeOperation().equals(TypeOperation.Wallet2Account) ? tx.getAmount() : 0d;
				}
			}
			else{
				if(tx.getTypeOperation().equals(TypeOperation.Account2Wallet) || tx.getTypeOperation().equals(TypeOperation.Wallet2Account)){
					i++;
					message = message + " **** " + tx.getPhoneNumber() + "-" + tx.getAmount();
				}
			}
		}

		/* A decommenter
		if(i > 0){
			throw new RuntimeException("Bien vouloir reconcilier les transactions suivantes afin de poursuivre avec le TFJO : " + message);
		}
		 */

		// Recuperation du compte Orange
		////System.out.println("-----ORG------"+params.getNcpOrange());
		////System.out.println("-----SQL------"+OgMoHelper.getDefaultCBSQueries().get(8).getQuery());
		ResultSet rsCpteOrange = executeFilterSystemQuery(dsCBS, "select bkcom.age, bkcom.dev, bkcom.cha, bkcom.ncp, bkcom.suf, bkcom.clc, bkcom.dva, bkcom.sde, bkcom.inti, bkcom.utic, bkcli.nom, bkcli.ges from bkcom, bkcli where bkcli.cli = bkcom.cli and bkcom.age = ? and bkcom.ncp = ? and bkcom.clc = ?", new Object[]{ params.getNcpCliOrange().split("-")[0], params.getNcpCliOrange().split("-")[1], params.getNcpCliOrange().split("-")[2] });

		// Recuperation du compte Orange
		////System.out.println("-----A2W------"+params.getNcpDAPAcountWalet());
		////System.out.println("-----SQL------"+OgMoHelper.getDefaultCBSQueries().get(8).getQuery());
		ResultSet rsCpteDAPA2W = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(8).getQuery(), new Object[]{ params.getNcpDAPAcountWalet().split("-")[0], params.getNcpDAPAcountWalet().split("-")[1], params.getNcpDAPAcountWalet().split("-")[2] });

		// Recuperation du compte Orange
		////System.out.println("-----ORG------"+params.getNcpDAPWaletAcount());
		////System.out.println("-----SQL------"+OgMoHelper.getDefaultCBSQueries().get(8).getQuery());
		ResultSet rsCpteDAPW2A = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(8).getQuery(), new Object[]{ params.getNcpDAPWaletAcount().split("-")[0], params.getNcpDAPWaletAcount().split("-")[1], params.getNcpDAPWaletAcount().split("-")[2] });
		
		if(!rsCpteOrange.next()) {
			throw new Exception("Comptes de Orange inexistants");
		}
		if(!rsCpteDAPA2W.next()) {
			throw new Exception("Comptes de DAP A2W inexistants");
		}
		if(!rsCpteDAPW2A.next()) {
			throw new Exception("Comptes de DAP W2A inexistants");
		}

		// Poste les EC des transactions dans le CoreBanking
		posterTransactionsDansCoreBanking(trans);

		/*************************************************************************/
		/**-------GENERATION DES EC DE COMPENSATION DANS LE COMPTE DE ORANGE ---**/
		/*************************************************************************/



		//*************** DEBIT DU CREDIT DU COMPTE ORANGE ***************

		// Recuperation du dernier numero evenement du type operation
		ResultSet rs = executeFilterSystemQuery(dsCBS, "select max(eve) as num from bkeve where ope=?", new Object[]{ params.getCodeOperation() });
		Long numEve = rs != null && rs.next() ? numEve = Long.valueOf( rs.getString("num") != null ? rs.getString("num").trim() : "1" )  + 1 : 1l;
		CloseStream.fermeturesStream(rs);

		bkeve eve = new bkeve(null, params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEve), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), "001", Math.abs(totalA2W - totalW2A), "VIORMO", new Date(), params.getCodeUtil(), 0d, 0d, 0d, Math.abs(totalA2W - totalW2A) );
		eve.setEta("IG");


		/*
		if(totalA2W - totalW2A < 0){
			eve.setDebiteur( rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), rsCpteOrange.getString("clc"), rsCpteOrange.getString("cli"), rsCpteOrange.getString("nom"), rsCpteOrange.getString("ges"),  Math.abs(totalA2W - totalW2A), Math.abs(totalA2W - totalW2A), rsCpteOrange.getDate("dva"), rsCpteOrange.getDouble("sde"));
			eve.setCrediteur(rsCpteDAPA2W.getString("age"), rsCpteDAPA2W.getString("dev"), rsCpteDAPA2W.getString("ncp"), rsCpteDAPA2W.getString("suf"), rsCpteDAPA2W.getString("clc"), rsCpteDAPA2W.getString("cli"), rsCpteDAPA2W.getString("inti"), rsCpteDAPA2W.getString("utic"), Math.abs(totalA2W - totalW2A), Math.abs(totalA2W - totalW2A), rsCpteDAPA2W.getDate("dva"), rsCpteDAPA2W.getDouble("sde"));
		}else{
			eve.setCrediteur(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), rsCpteOrange.getString("clc"), rsCpteOrange.getString("cli"), rsCpteOrange.getString("nom"), rsCpteOrange.getString("ges"),  Math.abs(totalA2W - totalW2A), Math.abs(totalA2W - totalW2A), rsCpteOrange.getDate("dva"), rsCpteOrange.getDouble("sde"));
			eve.setDebiteur(rsCpteDAPW2A.getString("age"), rsCpteDAPW2A.getString("dev"), rsCpteDAPW2A.getString("ncp"), rsCpteDAPW2A.getString("suf"), rsCpteDAPW2A.getString("clc"), rsCpteDAPW2A.getString("cli"), rsCpteDAPW2A.getString("inti"), rsCpteDAPW2A.getString("utic"), Math.abs(totalA2W - totalW2A), Math.abs(totalA2W - totalW2A), rsCpteDAPW2A.getDate("dva"), rsCpteDAPW2A.getDouble("sde"));
		}

		if(totalA2WMarch - totalA2WMarch < 0) {
			eve.setDebiteur( rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), rsCpteOrange.getString("clc"), rsCpteOrange.getString("cli"), rsCpteOrange.getString("nom"), rsCpteOrange.getString("ges"),  Math.abs(totalA2WMarch - totalW2AMarch), Math.abs(totalA2WMarch - totalW2AMarch), rsCpteOrange.getDate("dva"), rsCpteOrange.getDouble("sde"));
			eve.setCrediteur(rsCpteDAPA2W.getString("age"), rsCpteDAPA2W.getString("dev"), rsCpteDAPA2W.getString("ncp"), rsCpteDAPA2W.getString("suf"), rsCpteDAPA2W.getString("clc"), rsCpteDAPA2W.getString("cli"), rsCpteDAPA2W.getString("inti"), rsCpteDAPA2W.getString("utic"), Math.abs(totalA2WMarch - totalW2AMarch), Math.abs(totalA2WMarch - totalW2AMarch), rsCpteDAPA2W.getDate("dva"), rsCpteDAPA2W.getDouble("sde"));
		} else {
			eve.setCrediteur(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), rsCpteOrange.getString("clc"), rsCpteOrange.getString("cli"), rsCpteOrange.getString("nom"), rsCpteOrange.getString("ges"),  Math.abs(totalA2WMarch - totalW2AMarch), Math.abs(totalA2WMarch - totalW2AMarch), rsCpteOrange.getDate("dva"), rsCpteOrange.getDouble("sde"));
			eve.setDebiteur(rsCpteDAPW2A.getString("age"), rsCpteDAPW2A.getString("dev"), rsCpteDAPW2A.getString("ncp"), rsCpteDAPW2A.getString("suf"), rsCpteDAPW2A.getString("clc"), rsCpteDAPW2A.getString("cli"), rsCpteDAPW2A.getString("inti"), rsCpteDAPW2A.getString("utic"), Math.abs(totalA2WMarch - totalW2AMarch), Math.abs(totalA2WMarch - totalW2AMarch), rsCpteDAPW2A.getDate("dva"), rsCpteDAPW2A.getDouble("sde"));
		}
		 */


		// Debit du Cpte DAP des Pull
		//****** Methode commentee
		/*
		for(Transaction tx : trans) {
			if(tx.getTypeOperation().equals(TypeOperation.Account2Wallet)){
				bkmvti elem = new bkmvti(rsCpteDAPA2W.getString("age"), rsCpteDAPA2W.getString("dev"), rsCpteDAPA2W.getString("cha"), rsCpteDAPA2W.getString("ncp"), rsCpteDAPA2W.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteDAPA2W.getString("clc"), dco, null, rsCpteDAPA2W.getDate("dva"), tx.getAmount(), "D", "Account2Wallet /" + datop + "/" + tx.getPhoneNumber(), "N", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteDAPA2W.getString("age"), rsCpteDAPA2W.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null);
				eve.getEcritures().add(elem); numEc++;
			}
		}
		 */


		/*
		for(Transaction tx : trans) {
			if(tx.getTypeOperation().equals(TypeOperation.Account2Wallet)){
				if(tx.getSubscriber().getProfil() == null){
					eve.getEcritures().add( new bkmvti(rsCpteDAPA2W.getString("age"), rsCpteDAPA2W.getString("dev"), rsCpteDAPA2W.getString("cha"), rsCpteDAPA2W.getString("ncp"), rsCpteDAPA2W.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteDAPA2W.getString("clc"), dco, null, rsCpteDAPA2W.getDate("dva"), tx.getAmount(), "D", "Account2Wallet/" + new SimpleDateFormat("ddMMyyHHmm").format(tx.getDate()) + "/" + tx.getPhoneNumber(), "N", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteDAPA2W.getString("age"), rsCpteDAPA2W.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;					
				}else{
					// Recuperation du compte MTN Marchand
					ProfilMarchands plg = tx.getSubscriber().getProfil();
					ResultSet rsCpteDAPA2WMarch = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(8).getQuery(), new Object[]{ plg.getNcpDAPPull().split("-")[0], plg.getNcpDAPPull().split("-")[1], plg.getNcpDAPPull().split("-")[2] });

					if(!rsCpteDAPA2WMarch.next() ) throw new Exception("Comptes de Liaisons inexistants");

					eve.getEcritures().add( new bkmvti(rsCpteDAPA2WMarch.getString("age"), rsCpteDAPA2WMarch.getString("dev"), rsCpteDAPA2WMarch.getString("cha"), rsCpteDAPA2WMarch.getString("ncp"), rsCpteDAPA2WMarch.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteDAPA2WMarch.getString("clc"), dco, null, rsCpteDAPA2WMarch.getDate("dva"), tx.getAmount(), "D", "PULL/" + new SimpleDateFormat("ddMMyyHHmm").format(tx.getDate()) + "/" + tx.getPhoneNumber(), "N", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteDAPA2WMarch.getString("age"), rsCpteDAPA2WMarch.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;
				}
			}
		}


		// Crédit du Cpte Orange du total des A2W
		if(totalA2W > 0){
			eve.getEcritures().add( new bkmvti(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("cha"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteOrange.getString("clc"), dco, null, rsCpteOrange.getDate("dva"), totalA2W, "C", "COMPENS/" + TypeOperation.Account2Wallet.getValue().toUpperCase() + "/" + datop, "N", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), totalA2W, null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;
		}

		// Debit du Cpte Orange du total des W2A
		if(totalW2A > 0){
			eve.getEcritures().add( new bkmvti(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("cha"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteOrange.getString("clc"), dco, null, rsCpteOrange.getDate("dva"), totalW2A, "D", "COMPENS/" + TypeOperation.Wallet2Account.getValue().toUpperCase() + "/" + datop, "N", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), totalW2A, null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;
		}

		// CrÃ©dit du Cpte MTN du total des A2W
		if(totalA2WMarch > 0) eve.getEcritures().add( new bkmvti(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("cha"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteOrange.getString("clc"), dco, null, rsCpteOrange.getDate("dva"), totalA2WMarch, "C", "COMPENS MARCH/" + TypeOperation.Account2Wallet.toString().toUpperCase() + "/" + datop, "N", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), totalA2WMarch, null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;

		// Debit du Cpte MTN du total des W2A
		if(totalW2AMarch > 0) eve.getEcritures().add( new bkmvti(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("cha"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteOrange.getString("clc"), dco, null, rsCpteOrange.getDate("dva"), totalW2AMarch, "D", "COMPENS MARCH/" + TypeOperation.Wallet2Account.toString().toUpperCase() + "/" + datop, "N", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), totalW2AMarch, null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;
		 */



		// Credit du DAP des W2A
		//****** Methode commentee
		/*
		for(Transaction tx : trans){
			if(tx.getTypeOperation().equals(TypeOperation.Wallet2Account)){
				eve.getEcritures().add( new bkmvti(rsCpteDAPW2A.getString("age"), rsCpteDAPW2A.getString("dev"), rsCpteDAPW2A.getString("cha"), rsCpteDAPW2A.getString("ncp"), rsCpteDAPW2A.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteDAPW2A.getString("clc"), dco, null, rsCpteDAPW2A.getDate("dva"), tx.getAmount(), "C", "Wallet2Account /" + datop + "/" + tx.getPhoneNumber(), "N", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteDAPW2A.getString("age"), rsCpteDAPW2A.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;
			}
		}
		 */


		for(Transaction tx : trans) {

			if(tx.getSubscriber().getProfil() != null){

				if(tx.getTypeOperation().equals(TypeOperation.Account2Wallet)){
					// Debit du Cpte DAP des A2W pour les cas de transactions Marchands
					String lib = "MRCH/" + tx.getPhoneNumber() + "/" + new SimpleDateFormat("ddMMyyHHmm").format(tx.getDate()) + "/A2W";

					// Recuperation du compte 
					ProfilMarchands plg = tx.getSubscriber().getProfil();
					ResultSet rsCpteDAPA2WMarch = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(8).getQuery(), new Object[]{ plg.getNcpDAPPull().split("-")[0], plg.getNcpDAPPull().split("-")[1], plg.getNcpDAPPull().split("-")[2] });

					if(!rsCpteDAPA2WMarch.next() ) {
						throw new Exception("Comptes de Liaisons inexistants");
					}
					
					String pie = RandomStringUtils.randomNumeric(6);
					eve.getEcritures().add( new bkmvti(rsCpteDAPA2WMarch.getString("age"), rsCpteDAPA2WMarch.getString("dev"), rsCpteDAPA2WMarch.getString("cha"), rsCpteDAPA2WMarch.getString("ncp"), rsCpteDAPA2WMarch.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteDAPA2WMarch.getString("clc"), dco, null, rsCpteDAPA2WMarch.getDate("dva"), tx.getAmount(), "D", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteDAPA2WMarch.getString("age"), rsCpteDAPA2WMarch.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;
					eve.getEcritures().add( new bkmvti(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("cha"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteOrange.getString("clc"), dco, null, rsCpteOrange.getDate("dva"), tx.getAmount(), "C", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;

					//Ecritures comptables pour les comptes de liaison
					if(!rsCpteDAPA2WMarch.getString("age").equalsIgnoreCase(rsCpteOrange.getString("age"))){
						// Debit de la liaison du client du HT
						ResultSet rsCpteLiaisonDAP = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteDAPA2WMarch.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsCpteLiaisonDAP != null){
							rsCpteLiaisonDAP.next();
							eve.getEcritures().add( new bkmvti(rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), rsCpteLiaisonDAP.getString("cha"), rsCpteLiaisonDAP.getString("ncp"), rsCpteLiaisonDAP.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaisonDAP.getString("clc"), dco, null, rsCpteLiaisonDAP.getDate("dva"), tx.getAmount(), "C",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}
						// Credit de la liaison d'hippodrome du HT
						ResultSet rsLiaisonOrange = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteOrange.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsLiaisonOrange != null){ 
							rsLiaisonOrange.next();
							eve.getEcritures().add( new bkmvti(rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), rsLiaisonOrange.getString("cha"), rsLiaisonOrange.getString("ncp"), rsLiaisonOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonOrange.getString("clc"), dco, null, rsLiaisonOrange.getDate("dva"), tx.getAmount(), "D",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}
						
						CloseStream.fermeturesStream(rsCpteLiaisonDAP); rsCpteLiaisonDAP = null;
						CloseStream.fermeturesStream(rsLiaisonOrange); rsLiaisonOrange = null;
					}

					CloseStream.fermeturesStream(rsCpteDAPA2WMarch); rsCpteDAPA2WMarch = null;
				}
				else if(tx.getTypeOperation().equals(TypeOperation.Wallet2Account)){
					// Credit du DAP des W2A pour les cas de transactions Marchands
					String lib = "MRCH/" + tx.getPhoneNumber() + "/" + new SimpleDateFormat("ddMMyyHHmm").format(tx.getDate()) + "/W2A";

					// Recuperation du compte 
					ProfilMarchands plg = tx.getSubscriber().getProfil();
					ResultSet rsCpteDAPW2AMarch = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(8).getQuery(), new Object[]{ plg.getNcpDAPPush().split("-")[0], plg.getNcpDAPPush().split("-")[1], plg.getNcpDAPPush().split("-")[2] });

					if(!rsCpteDAPW2AMarch.next() ) throw new Exception("Comptes de Liaisons inexistants");

					String pie = RandomStringUtils.randomNumeric(6);
					eve.getEcritures().add( new bkmvti(rsCpteDAPW2AMarch.getString("age"), rsCpteDAPW2AMarch.getString("dev"), rsCpteDAPW2AMarch.getString("cha"), rsCpteDAPW2AMarch.getString("ncp"), rsCpteDAPW2AMarch.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteDAPW2AMarch.getString("clc"), dco, null, rsCpteDAPW2AMarch.getDate("dva"), tx.getAmount(), "C", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteDAPW2AMarch.getString("age"), rsCpteDAPW2AMarch.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;					
					eve.getEcritures().add( new bkmvti(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("cha"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteOrange.getString("clc"), dco, null, rsCpteOrange.getDate("dva"), tx.getAmount(), "D", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;

					//Ecritures comptables pour les comptes de liaison
					if(!rsCpteDAPW2AMarch.getString("age").equalsIgnoreCase(rsCpteOrange.getString("age"))){
						// Debit de la liaison du client du HT
						ResultSet rsCpteLiaisonDAP = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteDAPW2AMarch.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsCpteLiaisonDAP != null){
							rsCpteLiaisonDAP.next();
							eve.getEcritures().add( new bkmvti(rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), rsCpteLiaisonDAP.getString("cha"), rsCpteLiaisonDAP.getString("ncp"), rsCpteLiaisonDAP.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaisonDAP.getString("clc"), dco, null, rsCpteLiaisonDAP.getDate("dva"), tx.getAmount(), "D",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}
						// Credit de la liaison d'hippodrome du HT
						ResultSet rsLiaisonOrange = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteOrange.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsLiaisonOrange != null){ 
							rsLiaisonOrange.next();
							eve.getEcritures().add( new bkmvti(rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), rsLiaisonOrange.getString("cha"), rsLiaisonOrange.getString("ncp"), rsLiaisonOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonOrange.getString("clc"), dco, null, rsLiaisonOrange.getDate("dva"), tx.getAmount(), "C",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}

						CloseStream.fermeturesStream(rsCpteLiaisonDAP); rsCpteLiaisonDAP = null;
						CloseStream.fermeturesStream(rsLiaisonOrange); rsLiaisonOrange = null;
					}

					CloseStream.fermeturesStream(rsCpteDAPW2AMarch); rsCpteDAPW2AMarch = null;
				}

			}
			else{

				if(tx.getTypeOperation().equals(TypeOperation.Account2Wallet)){
					// Debit du Cpte DAP des A2W pour les cas de transactions Ends-users
					String lib = tx.getPhoneNumber() + "/" + new SimpleDateFormat("ddMMyyHHmm").format(tx.getDate()) + "/A2W";

					String pie = RandomStringUtils.randomNumeric(6);
					eve.getEcritures().add( new bkmvti(rsCpteDAPA2W.getString("age"), rsCpteDAPA2W.getString("dev"), rsCpteDAPA2W.getString("cha"), rsCpteDAPA2W.getString("ncp"), rsCpteDAPA2W.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteDAPA2W.getString("clc"), dco, null, rsCpteDAPA2W.getDate("dva"), tx.getAmount(), "D", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteDAPA2W.getString("age"), rsCpteDAPA2W.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;
					eve.getEcritures().add( new bkmvti(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("cha"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteOrange.getString("clc"), dco, null, rsCpteOrange.getDate("dva"), tx.getAmount(), "C", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;

					//Ecritures comptables pour les comptes de liaison
					if(!rsCpteDAPA2W.getString("age").equalsIgnoreCase(rsCpteOrange.getString("age"))){
						// Debit de la liaison du client du HT
						ResultSet rsCpteLiaisonDAP = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteDAPA2W.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsCpteLiaisonDAP != null){
							rsCpteLiaisonDAP.next();
							eve.getEcritures().add( new bkmvti(rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), rsCpteLiaisonDAP.getString("cha"), rsCpteLiaisonDAP.getString("ncp"), rsCpteLiaisonDAP.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaisonDAP.getString("clc"), dco, null, rsCpteLiaisonDAP.getDate("dva"), tx.getAmount(), "C",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}
						// Credit de la liaison d'hippodrome du HT
						ResultSet rsLiaisonOrange = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteOrange.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsLiaisonOrange != null){ 
							rsLiaisonOrange.next();
							eve.getEcritures().add( new bkmvti(rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), rsLiaisonOrange.getString("cha"), rsLiaisonOrange.getString("ncp"), rsLiaisonOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonOrange.getString("clc"), dco, null, rsLiaisonOrange.getDate("dva"), tx.getAmount(), "D",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}

						CloseStream.fermeturesStream(rsCpteLiaisonDAP); rsCpteLiaisonDAP = null;
						CloseStream.fermeturesStream(rsLiaisonOrange); rsLiaisonOrange = null;
					}

				}
				else if(tx.getTypeOperation().equals(TypeOperation.Wallet2Account)){
					// Credit du DAP des W2A pour les cas de transactions Ends-users
					String lib = tx.getPhoneNumber() + "/" + new SimpleDateFormat("ddMMyyHHmm").format(tx.getDate()) + "/W2A";

					String pie = RandomStringUtils.randomNumeric(6);
					eve.getEcritures().add( new bkmvti(rsCpteDAPW2A.getString("age"), rsCpteDAPW2A.getString("dev"), rsCpteDAPW2A.getString("cha"), rsCpteDAPW2A.getString("ncp"), rsCpteDAPW2A.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteDAPW2A.getString("clc"), dco, null, rsCpteDAPW2A.getDate("dva"), tx.getAmount(), "C", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteDAPW2A.getString("age"), rsCpteDAPW2A.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;					
					eve.getEcritures().add( new bkmvti(rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), rsCpteOrange.getString("cha"), rsCpteOrange.getString("ncp"), rsCpteOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteOrange.getString("clc"), dco, null, rsCpteOrange.getDate("dva"), tx.getAmount(), "D", lib, "N", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteOrange.getString("age"), rsCpteOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) ); numEc++;

					//Ecritures comptables pour les comptes de liaison
					if(!rsCpteDAPW2A.getString("age").equalsIgnoreCase(rsCpteOrange.getString("age"))){
						// Debit de la liaison du client du HT
						ResultSet rsCpteLiaisonDAP = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteDAPW2A.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsCpteLiaisonDAP != null){
							rsCpteLiaisonDAP.next();
							eve.getEcritures().add( new bkmvti(rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), rsCpteLiaisonDAP.getString("cha"), rsCpteLiaisonDAP.getString("ncp"), rsCpteLiaisonDAP.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaisonDAP.getString("clc"), dco, null, rsCpteLiaisonDAP.getDate("dva"), tx.getAmount(), "D",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaisonDAP.getString("age"), rsCpteLiaisonDAP.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}
						// Credit de la liaison d'hippodrome du HT
						ResultSet rsLiaisonOrange = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where dev='001' and age='"+rsCpteOrange.getString("age")+"' and ncp='"+ params.getNumCompteLiaison() +"'", null);
						if(rsLiaisonOrange != null){ 
							rsLiaisonOrange.next();
							eve.getEcritures().add( new bkmvti(rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), rsLiaisonOrange.getString("cha"), rsLiaisonOrange.getString("ncp"), rsLiaisonOrange.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonOrange.getString("clc"), dco, null, rsLiaisonOrange.getDate("dva"), tx.getAmount(), "C",lib, "O", pie, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonOrange.getString("age"), rsLiaisonOrange.getString("dev"), tx.getAmount(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
						}

						CloseStream.fermeturesStream(rsCpteLiaisonDAP); rsCpteLiaisonDAP = null;
						CloseStream.fermeturesStream(rsLiaisonOrange); rsLiaisonOrange = null;
					}
				}

			}





			//********************************************************************


			/*
			// Debit du Cpte DAP des A2W pour les cas de transactions Marchands
			if(tx.getTypeOperation().equals(TypeOperation.Account2Wallet) && tx.getSubscriber().getProfil() != null){
				//--
			}
			// Debit du Cpte DAP des A2W pour les cas de transactions Ends-users
			if(tx.getTypeOperation().equals(TypeOperation.Account2Wallet) && tx.getSubscriber().getProfil() == null){
				//--
			}
			 */
		}


		/*
		for(Transaction tx : trans) {
			// Credit du DAP des W2A pour les cas de transactions Marchands
			if(tx.getTypeOperation().equals(TypeOperation.Wallet2Account) && tx.getSubscriber().getProfil() != null){
				//--
			}
			// Credit du DAP des W2A pour les cas de transactions Ends-users
			if(tx.getTypeOperation().equals(TypeOperation.Wallet2Account) && tx.getSubscriber().getProfil() == null){
				//--
			}
		}
		 */

		//*************** FIN DU CREDIT DU COMPTE ORANGE ***************


		details.addAll(detailsTransVersNcpOrange(eve.getEcritures(), trans));



		// Insertion 
		Boolean insert = executeUpdateSystemQuery(dsCBS, eve.getEcritures() , dco);

		/*
		// Enregistrement de l'evenement Global dans Amplitude
		System.out.println("***************** eve.getQueryValues().toString() ***************** : " + eve.getQueryValues().toString());
		executeUpdateSystemQuery(dsCBS, eve.getSaveQuery(), eve.getQueryValues());

		// MAJ du dernier numero d'evenement utilise pour le type operation
		executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(3).getQuery(), new Object[]{ Long.valueOf(eve.getEve()), eve.getOpe() });
		 */

		// Sauvegarde l'evenement (Dans le Portal)
		//*** dao.save(eve);


		/*************************************************************************/
		/**------------MISE A JOUR DES TRANSACTIONS DANS LE SYSTEME-------------**/
		/*************************************************************************/
		// Enregistrement de l'evenement Global dans Amplitude
		//executeUpdateSystemQuery(dsCBS, eve.getSaveQuery(), eve.getQueryValues());

		// Sauvegarde des ecritures comptables dans Amplitude
		//for(bkmvti mvt : eve.getEcritures()) executeUpdateSystemQuery(dsCBS, mvt.getSaveQuery(), mvt.getQueryValues());

		// MAJ du solde indicatif des comptes DAP
		//executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(4).getQuery(), new Object[]{ totalPush, rsCpteDAPW2A.getString("age"), rsCpteDAPW2A.getString("ncp"), rsCpteDAPW2A.getString("clc") } );
		//executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(5).getQuery(), new Object[]{ totalPull, rsCpteDAPA2W.getString("age"), rsCpteDAPA2W.getString("ncp"), rsCpteDAPA2W.getString("clc") } );

		// MAJ du dernier numero d'evenement utilise pour le type operation
		//executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(3).getQuery(), new Object[]{ Long.valueOf(eve.getEve()), eve.getOpe() });

		// Sauvegarde l'evenement (Dans le Portal)
		//dao.save(eve);

		// MAJ de la date d'execution du TFJO
		//dao.getEntityManager().createQuery("Update Parameters p set p.active=:actif, p.dateTfjo='"+ OgMoHelper.sdf.format(new Date()) +"'").setParameter("actif", Boolean.FALSE).executeUpdate();

		// On libere tous les ResultSet
		CloseStream.fermeturesStream(rsCpteOrange); rsCpteOrange = null;
		CloseStream.fermeturesStream(rsCpteDAPA2W); rsCpteDAPA2W = null;
		CloseStream.fermeturesStream(rsCpteDAPW2A); rsCpteDAPW2A = null;
		
		//if(conCBS != null ) conCBS.close();

		//rs = null;
		params = null;


		Collections.sort(details);
		FactMonth fac = new FactMonth(dco, "D", "C", mntD, mntC, nbrD, nbrC, user, new Date(), mois);
		fac.setDev(dev);
		fac = dao.save(fac);
		for(FactMonthDetails det : details) det.setParent(fac);
		dao.saveList(details,true);


		fac.setDetails(details);

		// Execution du Timer de Contrôle de la fin des TFJ
		// TFJScheduler();

		return fac;

	}


	public  List<Date> getJourFerier(Date date) throws SQLException, Exception {

		// recuperation des jours feriers
		List<Date> feriers = new ArrayList<Date>();
		String years = new SimpleDateFormat("yyyy").format(date);

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();

		String req_ferie = "select jourfer from bkfer where TO_CHAR(jourfer,'%Y')='" + years + "'";
		ResultSet resultSet = executeFilterSystemQuery(dsCBS, req_ferie, new Object[]{});

		while(resultSet != null && resultSet.next()){
			feriers.add(resultSet.getDate(1));
		}

		CloseStream.fermeturesStream(resultSet);
		//if(conCBS != null ) conCBS.close();

		return feriers;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> getInfoCompte(String ncp, String age) throws SQLException, Exception {

		Map infoCompte = new HashMap<String, String>();

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();

		String req_infosCompte = "select suf, dev, cha, clc from bkcom where ncp = '"+ ncp +"' and age = '"+ age +"' ";
		ResultSet resultSet = executeFilterSystemQuery(dsCBS, req_infosCompte, new Object[]{});

		if(resultSet.next()) {
			infoCompte.put("dev",resultSet.getString("dev"));
			infoCompte.put("cha",resultSet.getString("cha") != null ? resultSet.getString("cha").trim() : "");
			infoCompte.put("clc",resultSet.getString("clc"));
		}
		else{
			throw new Exception("Le compte "+ncp+" n'existe pas dans l'agence "+age);
		}
		CloseStream.fermeturesStream(resultSet);
		//if(conCBS != null ) conCBS.close();

		return infoCompte;
	}



	public List<FactMonthDetails> detailsTransVersNcpOrange(List<bkmvti> mvts, List<Transaction> transactions) throws Exception{

		Subscriber subs = null;
		if(!transactions.isEmpty()){
			subs = transactions.get(0).getSubscriber();
		}

		List<FactMonthDetails> dets = new ArrayList<FactMonthDetails>();

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();
		Date dco = getDateComptable();


		// Parcours des ecritures
		if(conCBS == null || conCBS.isClosed()) conCBS = getSystemConnection(dsCBS);

		// Suspension temporaire du mode blocage dans la BD du Core Banking
		if(dsCBS.getDbConnectionString().indexOf("informix") > 0) conCBS.createStatement().executeUpdate("SET ISOLATION TO DIRTY READ");

		stmCBS = conCBS.createStatement();
		Map<String,String> mapCompte = new HashMap<String, String>();

		for(bkmvti mvt : mvts){

			mvt.setDco(dco);
			//mvt.setDva(dva);
			// Ecriture de la ligne dans le fichier
			if(!mvt.getDev().equalsIgnoreCase("001")){
				mvt.setDev("001");
			}

			// Recuperation 
			FactMonthDetails det = new FactMonthDetails(Integer.valueOf(mvt.getAge()), mvt.getNcp()+"-"+mvt.getClc(),mvt.getIntitule(), mvt.getLib(),mvt.getInitDate(),mvt.getDateFact() ,mvt.getDatedebutFact(),mvt.getSen(),mvt.getMon());
			det.setLibage(mvt.getLibage());
			det.setTxtage(mvt.getAge());
			det.setDateAbon(subs.getDate());
			det.setDev(mvt.getDev());
			String cle = mvt.getAge().trim()+mvt.getNcp().trim()+mvt.getClc().trim();
			ResultSet rs = null;
			if(mapCompte.containsKey(cle)){
				det.setIntitule(mapCompte.get(cle));
			}else{
				rs = executeFilterSystemQuery(dsCBS,OgMoHelper.getDefaultCBSQueries().get(6).getQuery(),new Object[]{mvt.getAge(),mvt.getNcp(),mvt.getClc()});
				if(rs.next()){
					det.setIntitule(rs.getString("nom"));
					if(mapCompte.isEmpty()){
						mapCompte.put(cle, rs.getString("nom"));
					}else if(!mapCompte.containsKey(cle)){
						mapCompte.put(cle, rs.getString("nom"));
					}
				}else {
					CloseStream.fermeturesStream(rs);
					rs = executeFilterSystemQuery(dsCBS,OgMoHelper.getDefaultCBSQueries().get(7).getQuery(),new Object[]{mvt.getAge(),mvt.getNcp(),mvt.getClc()});
					if(rs.next()){
						det.setIntitule(rs.getString("inti"));
						if(mapCompte.isEmpty()){
							mapCompte.put(cle, rs.getString("inti"));
						}else if(!mapCompte.containsKey(cle)){
							mapCompte.put(cle, rs.getString("inti"));
						}
					}
				}
			}

			CloseStream.fermeturesStream(rs);

			dets.add(det);
			if("C".equalsIgnoreCase(mvt.getSen())){
				mntC = mntC+mvt.getMon();
				nbrC++;
			}else{
				mntD = mntD+mvt.getMon();
				nbrD++;
			}
			dev = mvt.getDev();

		}

		//if(conCBS != null ) conCBS.close();

		return dets;

	}






	/**
		Les tables impliquées sont :

		    - bksig_audit : pour sauvegarder les traces de consultation
			- bksigc : Pour consulter les signatures
			- table 098 avec cacc ='SIGNATURES' :  pour récupérer l'url vers le serveur des signatures
			(Tu peux consulter le dictionnaire de données pour en savoir plus sur ces tables)

		Le process est le suivant:
		   1 - Génération d'une clé unique de consultation
		   2 - Génération du délai de validité de la signature au format yyMMddHHmmss
		   3 - Insertion dans bksig_audit
		   4 - insertion dans bksigc
		   5 - fabication de l'url de consultation à partir de la clé unique générée plus haut
		   6 - Affichage de la signature à partir de l'url générée  (je le fais dans un popup du navigateur)
		   -------
		   7- Si la signature est OK (valider par l'utilisateur) le champ conf de bksig_audit est update à 'O', sinon on update à 'N'
		   la requête est : UPDATE bksig_audit SET conf =? WHERE age = ? AND ncp = ? AND dev = '001' AND suf = ? AND cli = ? AND datec = ? AND heurec = ? AND utic = ?;

	 **/

	/**
		1- Génération d'une clé unique de consultation
		  Génération de la clé unique
		  clé = clé max en BD + 1;
	 */
	private double genererCle() throws Exception{

		if(dsCBS == null) findCBSDataSystem();

		ResultSet rs = executeFilterSystemQuery(dsCBS, "select max (cle) as maxCle from bksigc", null) ;
		double max= rs != null && rs.next() ? rs.getDouble("maxCle") : 0;
		CloseStream.fermeturesStream(rs);

		//if(conCBS != null ) conCBS.close();

		return  max + 1;
	}

	/**
		2- Génération du délai de validité de la signature au format yyMMddHHmmss
		Pour ce qui est du délai de validiter je récupère l'heure du serveur, j'ajoute une minute et je format en String au format yyMMddHHmmss
	 */

	/**
		3- Insertion dans bksig_audit
		Requête pour insertion dans bksig_audit: INSERT_INTO_BKSIG_AUDIT = "INSERT INTO bksig_audit VALUES (?,'001',?,?,?,?,?,?,'E','cbconssig')"
	 */
	public void insertIntoBksigAudi(String age, String ncp, String suf, String cli, Date datec, String heurec, String utic) throws Exception{

		if(dsCBS == null) findCBSDataSystem();

		executeUpdateSystemQuery(dsCBS, "INSERT INTO bksig_audit VALUES (?,'001',?,?,?,?,?,?,'E','cbconssig')", new Object[]{age, ncp, suf, cli, new java.sql.Date(datec.getTime()), heurec, utic}) ;

	}

	/**
		4- insertion dans bksigc
		Requête pour insertion dans bksigc: INSERT_INTO_BKSIGC = "INSERT INTO bksigc VALUES (?,?,?,?,?,?,?,'001','O')"
	 */

	public void insertIntoBksigC(double cle, String valid, String cuti, String cli, String ncp, String suf, String age) throws Exception{

		if(dsCBS == null) findCBSDataSystem();

		executeUpdateSystemQuery(dsCBS, "INSERT INTO bksigc VALUES (?,?,?,?,?,?,?,'001','O')", new Object[]{cle, valid, cuti, cli, ncp, suf, age}) ;
	}

	/**
		5 - fabication de l'url de consultation à partir de la clé unique générée plus haut
		urlfinal  = lien+cle;
		requête récupération lien vers serveur signature :  SELECT_LIEN_SIG = "SELECT lib3,lib4,lib5 FROM bknom WHERE age ='00099' AND ctab ='098' AND cacc ='SIGNATURES'";
	 */
	public String getLienSig() throws Exception{

		if(dsCBS == null) findCBSDataSystem();

		String lien = null;
		ResultSet rs = executeFilterSystemQuery(dsCBS, "SELECT lib3,lib4,lib5 FROM bknom WHERE age ='00099' AND ctab ='098' AND cacc ='SIGNATURES'", null) ;

		while (rs.next()) {
			lien = rs.getString("lib3").trim();
			if(lien.charAt(lien.length()-1) != '/')  lien+='/';
			String str = rs.getString("lib4").trim();
			if(str.charAt(0)== '/') str = str.substring(0);
			lien += str; 
			if(lien.charAt(lien.length()-1) != '/') lien+='/';
			str = rs.getString("lib5").trim();
			if(str.charAt(0)== '/') str = str.substring(0);
			lien += str;
		}
		CloseStream.fermeturesStream(rs);
		//if(conCBS != null ) conCBS.close();

		return lien;
	}

	/**
	 *	Pour avoir le lien finall la fonction finale est :
	 */

	@Override
	public String getLienSig(String ncp, String utic) throws Exception {
		
	    System.out.println("Signature: " + ncp + " Uti: " + utic);
	    String lienSig = null;
		double cle = genererCle();
		if (dsCbs == null) findCBSServicesDataSystem();
		if(dsCbs != null && StringUtils.isNotBlank(dsCbs.getDbConnectionString())) {
			
			HttpGet getRequest = new HttpGet(dsCbs.getDbConnectionString()+"/kyc/process/geturlsignature/" + ncp + "/" + utic);
		    getRequest.setHeader("content-type", "application/json");
		    CloseableHttpResponse response = Shared.getClosableHttpClient().execute(getRequest);
		    HttpEntity entity = null;
		    entity = response.getEntity();
		    
		    if(entity != null) {
		    	
		    	 if(entity != null) {
		    		 
		    		 String content = EntityUtils.toString(entity);
					 JSONObject json = new JSONObject(content);
					 
					 String responseCode = json.getString("code");
					 
					 if ("200".equals(responseCode)) {
						 lienSig = json.getString("data");
						 System.out.println("Lien: " + lienSig);
					 }
		    		 
		    	 }
		    	
		    } 
		}
		
		// return du lien final 
		return lienSig;
	}

	/**
	 *	requête pour se rassurer qu'une clé générée n'existe pas : SELECT_INTO_BKSIGC = "SELECT * FROM bksigc WHERE cle = ?"
	 */
	private boolean cleSigExiste(double cle) throws Exception{

		if(dsCBS == null) findCBSDataSystem();

		ResultSet rs = executeFilterSystemQuery(dsCBS, "SELECT * FROM bksigc WHERE cle = " + cle, null) ;

		boolean rep = rs != null && rs.next();
		CloseStream.fermeturesStream(rs);
		//if(conCBS != null ) conCBS.close();

		return rep;
	}


	@SuppressWarnings("unchecked")
	public List<Subscriber> findAllSubscriberNonFactures(){

		// Initialisation de la liste a retourner
		List<Subscriber> result = new ArrayList<Subscriber>();

		// Recherche des abonnes non factures a la souscription
		result = dao.getEntityManager().createQuery("Select distinct t.subscriber from Transaction t where t.typeOperation=:typeOp and t.ttc=0 and t.subscriber.status=:actif order by t.subscriber.customerName").setParameter("typeOp", TypeOperation.SUBSCRIPTION).setParameter("actif", StatutContrat.ACTIF).getResultList(); // .filter(Subscriber.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("", 0)), null, null, 0, -1);

		// Suppression des clients Employes
		//for(int i=result.size()-1; i>=0; i--) if(isClientEmploye(result.get(i).getCustomerId()) || result.get(i).getFirstPhone().equals("237672275402") ) result.remove(i);

		// Retourne le resultat
		return result;

	}


	public void facturerListSubscribers(List<Subscriber> list) throws Exception, OgMoException {

		if(list == null || list.isEmpty()) return;

		// Recuperation des parametres generaux
		Parameters param = findParameters();

		// Lecture de la commission du type operation
		Commissions coms = ConverterUtil.convertCollectionToMap(param.getCommissions(), "operation").get(TypeOperation.SUBSCRIPTION);

		// Si des commissions ont ete parametres sur l'operation
		if(coms != null && coms.getValeur() > 0) {

			// Parcours de la liste des abonnes
			for(Subscriber s : list){

				// Annulation de la precedente transaction
				dao.getEntityManager().createQuery("Update Transaction t set t.status=:status where t.typeOperation=:typeOp and t.subscriber.id=:id").setParameter("status", TransactionStatus.FAILED).setParameter("typeOp", TypeOperation.SUBSCRIPTION).setParameter("id", s.getId()).executeUpdate();

				// Postage de l'evenement dans le CBS
				posterEvenementDansCoreBanking( new Transaction(TypeOperation.SUBSCRIPTION, s, 0d, s.getFirstAccount(), s.getFirstPhone(), TransactionStatus.SUCCESS) );

			}

		}
	}




	public String lastExecutionRobot(){
		String req = "From TraceRobot t where t.typeExecution = :type and t.id = (select max(t2.id) From TraceRobot t2) ";
		TraceRobot obj = (TraceRobot) dao.getEntityManager().createQuery(req).setParameter("type", "RECONCILIATION-TRANSACTION").getSingleResult();
		return obj != null ?  obj.getFormattedDatetimeTrace() : "";
	}


	public String lastExecutionAlerte(){
		String req = "From TraceRobot t where t.typeExecution = :type and t.id = (select max(t2.id) From TraceRobot t2) ";
		TraceRobot obj = (TraceRobot) dao.getEntityManager().createQuery(req).setParameter("type", "ALERTE-RECONCILIATION").getSingleResult();
		return obj != null ?  obj.getFormattedDatetimeTrace() : "";
	}



	@Override
	public void exportTransactionIntoExcelFile( List<Transaction> data, String fileName ) throws Exception {

		// Initialisation d'un document Excel
		SXSSFWorkbook wb = new SXSSFWorkbook();

		// Initialisation de la Feuille courante
		Sheet sheet  = wb.createSheet("Liste des transactions");

		// Creation d'une ligne
		Row row = sheet.createRow(0);

		// Affichage des entetes de colonnes du fichier excel
		row.createCell(0).setCellValue( "N°" );
		row.createCell(1).setCellValue( "Num Téléphone" );
		row.createCell(2).setCellValue( "Client" );
		row.createCell(3).setCellValue( "Opération" );
		row.createCell(4).setCellValue( "Date" );
		row.createCell(5).setCellValue( "Montant" );
		row.createCell(6).setCellValue( "N° de Cpte" );
		row.createCell(7).setCellValue( "Référence Orange" );
		row.createCell(8).setCellValue( "Code Erreur" );
		row.createCell(9).setCellValue( "Statut" );
		row.createCell(10).setCellValue( "Statut Orange" );
		
		// Initialisation du compteur
		int i = 1;

		// Parcours des transaction
		for(Transaction t : data){


			// Initialisation d'une ligne
			row = sheet.createRow(i);

			// Affichage des colonnes dans la fichier excel
			row.createCell(0).setCellValue( i++ );
			row.createCell(1).setCellValue( t.getPhoneNumber() );
			row.createCell(2).setCellValue( t.getSubscriber().getCustomerName());
			row.createCell(3).setCellValue( t.getTypeOperation().getValue() );
			row.createCell(4).setCellValue( t.getFormattedDate() );
			row.createCell(5).setCellValue( t.getTtc());
			row.createCell(6).setCellValue( t.getAccount() );
			row.createCell(7).setCellValue( t.getExternalRefNo());
			row.createCell(8).setCellValue( t.getRequestId() );
			row.createCell(9).setCellValue( t.getStatus().getValue() );
			row.createCell(10).setCellValue( t.getReverseTrans() );

		}

		// Sauvegarde du fichier
		FileOutputStream fileOut = new FileOutputStream(PortalHelper.JBOSS_DATA_DIR + File.separator + PortalHelper.PORTAL_RESOURCES_DATA_DIR + File.separator + PortalHelper.PORTAL_DOWNLOAD_DATA_DIR + File.separator + fileName);
		wb.write(fileOut);
		fileOut.close();

	}



	@Override
	public void exportSouscriptionIntoExcelFile( List<Subscriber> data, String fileName ) throws Exception {

		// Initialisation d'un document Excel
		SXSSFWorkbook wb = new SXSSFWorkbook();

		// Initialisation de la Feuille courante
		Sheet sheet  = wb.createSheet("Liste des souscriptions");

		// Creation d'une ligne
		Row row = sheet.createRow(0);

		// Affichage des entetes de colonnes du fichier excel
		row.createCell(0).setCellValue( "N°" );
		row.createCell(1).setCellValue( "Code" );
		row.createCell(2).setCellValue( "N° de Cpte" );
		row.createCell(3).setCellValue( "Téléphone" );
		row.createCell(4).setCellValue( "Client" );
		row.createCell(5).setCellValue( "Date" );
		row.createCell(6).setCellValue( "Agence Abonnement" );
		row.createCell(7).setCellValue( "Utilisateur Abonnement" );		
		row.createCell(8).setCellValue( "Date Modification" );
		row.createCell(9).setCellValue( "Agence Modification" );
		row.createCell(10).setCellValue( "Utilisateur Modification" );		
		row.createCell(11).setCellValue( "Utilisateur Validation" );
		row.createCell(12).setCellValue( "Profil" );
		row.createCell(13).setCellValue( "Client OM" );
		row.createCell(14).setCellValue( "Téléphone OM" );
		row.createCell(15).setCellValue( "N° CNI OM" );
		row.createCell(16).setCellValue( "Statut" );

		// Initialisation du compteur
		int i = 1;

		// Parcours des transaction
		for(Subscriber t : data){

			// Initialisation d'une ligne
			row = sheet.createRow(i);

			// Affichage des colonnes dans la fichier excel
			row.createCell(0).setCellValue( i++ );
			row.createCell(1).setCellValue( t.getCustomerId() );
			row.createCell(2).setCellValue( t.getFirstAccount());
			row.createCell(3).setCellValue( t.getFirstPhone() );
			row.createCell(4).setCellValue( t.getCustomerName());
			row.createCell(5).setCellValue( t.getFormattedDate() );
			row.createCell(6).setCellValue( t.getAgeAbon() );
			row.createCell(7).setCellValue( t.getUtiabon() + " -- " + t.getNameUtiAbon() );			
			row.createCell(8).setCellValue( t.getFormattedDateMod() );
			row.createCell(9).setCellValue( t.getAgeMod() );
			row.createCell(10).setCellValue( t.getUtiMod() + " -- " + t.getNameUtiMod() );
			row.createCell(11).setCellValue( t.getUtiValid() );
			row.createCell(12).setCellValue(  t.getProfil() == null ? "DEFAULT" : t.getProfil().getProfilName() );
			row.createCell(13).setCellValue( t.getSublastname() + " " + t.getSubfirstname());
			row.createCell(14).setCellValue( t.getSubmsisdn());
			row.createCell(15).setCellValue( t.getSubcin());
			row.createCell(16).setCellValue( t.getStatus().getValue() );

		}

		// Sauvegarde du fichier
		FileOutputStream fileOut = new FileOutputStream(PortalHelper.JBOSS_DATA_DIR + File.separator + PortalHelper.PORTAL_RESOURCES_DATA_DIR + File.separator + PortalHelper.PORTAL_DOWNLOAD_DATA_DIR + File.separator + fileName);
		wb.write(fileOut);
		fileOut.close();

	}

	/**
	 * chargerDonneesComptabiliserTFJO
	 * @return liste des transactions a comptabiliser
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Transaction> chargerDonneesComptabiliserTFJO(Date date) throws Exception{
		return dao.filter(Transaction.class, AliasesContainer.getInstance().add("subscriber", "s"), RestrictionsContainer.getInstance().add(Restrictions.eq("typeOperation",TypeOperation.COMPTABILISATION)).add(Restrictions.eq("status",TransactionStatus.PROCESSING)), OrderContainer.getInstance().add(Order.asc("s.dateDernCompta")), null, 0, -1);
	}


	@Override
	@SuppressWarnings("unchecked")
	//@TransactionTimeout(value = 15000)
	@AllowedRole(name = "executerTFJO", displayName = "OgMo.Executer.TFJO")
	public void executerTFJO2(Date date) throws Exception {

		// Initialisation de la liste des abonnes a comptabiliser
		List<Subscriber> subs = new ArrayList<Subscriber>();

		// Lecture des parametres generaux
		params = findParameters();

		// Recuperation de la liste des commissions
		Map<TypeOperation, Commissions> mapComs = ConverterUtil.convertCollectionToMap(params.getCommissions(), "operation");

		// Initialisation de la TVA
		Double tva = 19.25d;

		// Construction d'une Map de periodicites
		Map<Periodicite, Integer> mapPeriods = new HashMap<Periodicite, Integer>();
		mapPeriods.put(Periodicite.MOIS, 30); mapPeriods.put(Periodicite.TRIMESTRE, 90); mapPeriods.put(Periodicite.SEMESTRE, 180); mapPeriods.put(Periodicite.ANNUEL, 365);

		// Recuperation de la tva parametree sur les commissions PULL et PUSH
		//tva = Math.max( mapComs.get(TypeOperation.PULL).getPeriodFacturation() != null ? mapComs.get(TypeOperation.PULL).getTauxTVA() : 0d, mapComs.get(TypeOperation.PUSH).getPeriodFacturation() != null ? mapComs.get(TypeOperation.PUSH).getTauxTVA() : 0d);
		if(params.getTva() != null) tva = params.getTva();

		// Initialisation de la requete de selection des abonnements a comptabiliser (date d'anniversaire)
		//String req = "From Subscriber s where s.status = :statut and s.facturer = :facturer and s.commissions>0"; //  and (current_date - s.dateDernCompta) >= (case when s.period='MOIS' then 30 else ( case when s.period='TRIMESTRE' then 90 else ( case when s.period='SEMESTRE' then 180 else ( case when s.period='ANNUEL' then 365 else ( case when s.period='SEMAINE' then 7 else 1 end ) end ) end ) end ) end ) ";
		//String req = "From Subscriber s where s.status = :statut and s.facturer = :facturer  and s.commissions > 0  and s.dateDernCompta <=  :dateDernCompta  and profil is not null "; // and s.date < :date
		String req = "From Subscriber s where s.status = :statut and s.facturer = :facturer  and (s.dateDernCompta <= :dateDernCompta or s.dateDernCompta is null)  and s.profil is not null "; // and s.date < :date

		// Recuperation de la liste des abonnements a comptabiliser
		//subs = mobileMoneyDAO.getEntityManager().createQuery(req).setParameter("statut", StatutContrat.ACTIF).setParameter("facturer", Boolean.TRUE).getResultList(); // .setMaxResults(1500)
		subs = dao.getEntityManager().createQuery(req).setParameter("statut", StatutContrat.ACTIF).setParameter("facturer", Boolean.TRUE).setParameter("dateDernCompta", getLastFacturationDate(date)).getResultList();

		//System.out.println("**************** subs.size() **************** : " + subs.size());

		// Generation des evenements, transactions et ecritures a comptabiliser et enregistrement
		//mobileMoneyDAO.saveList(buildEventsForComptabilisation(subs, tva, date), true);
		dao.saveList(buildEventsForComptabilisation(subs, tva, date), true);

		// Liberation des ressources
		mapComs.clear(); mapPeriods.clear(); subs.clear();

	}



	/**
	 * chargerDonneesComptabiliserRegul
	 * @return liste des transactions a regulariser
	 * @throws Exception
	 */
	public List<Transaction> chargerDonneesComptabiliserRegul() throws Exception{

		Map<Long,Transaction> mapTrans = new HashMap<Long,Transaction>();

		// Initialisation de DataStore d'Amplitude
		//if(dsCBS == null) findCBSDataSystem();

		// Marqueur du mode nuit
		boolean nuit = isModeNuit();

		List<Transaction> list = new ArrayList<Transaction>(); 
		List<Transaction> values = dao.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("typeOperation",TypeOperation.COMPTABILISATION)).add(Restrictions.eq("status", TransactionStatus.REGUL)).add(Restrictions.or(Restrictions.isNull("dateControle"), Restrictions.lt("dateControle", new Date()))), OrderContainer.getInstance().add(Order.desc("dateCompta")), null, 0, 5000);

		//System.out.println("**************** values.size() ***************** : " + values.size());
		if(values.isEmpty()) return list;
		for(Transaction t : values) mapTrans.put(t.getId(),t);

		// Parcours de la liste des abonnements a comptabiliser 
		List<bkeve> eves = dao.filter(bkeve.class, null, RestrictionsContainer.getInstance().add(Restrictions.in("transaction",values)), null, null, 0, -1);

		//Long numEve = getLastEveNum(dsCBS);

		// Parcours de la liste des abonnements a comptabiliser
		for(bkeve eve : eves) {
			// MAJ des numeros d'evenements de numero 000000
			/*if(eve.getEve().equals("000000")){
				eve.setEve(OgMoHelper.padText(String.valueOf(numEve), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"));
				for(bkmvti mvt : eve.getEcritures()){
					if(mvt.getLib().contains("HIP")) mvt.getLib().replaceFirst(" HIP", " LS");
					if(mvt.getEve().equals("000000")) mvt.setEve(eve.getEve());
				}
				// MAJ de l'evenement modifie et de ses ecritures
				mobileMoneyDAO.getEntityManager().merge(eve);
			}*/

			Transaction tx = mapTrans.get(eve.getTransaction().getId());
			tx.setDateControle(new Date());
			if(tx.getSubscriber().getFirstAccount() != null){

				if(!isCompteFerme(tx.getSubscriber().getFirstAccount())){

					// Si l'objet courant a ete selectionne par l'utilisateur et ses ecritures sont equilibrees
					if(eve.isEquilibre()){ //if(c.isSelected() && c.getEve().isEquilibre()){

						// Si le solde du compte est suffisant
						if(getSolde(tx.getSubscriber().getFirstAccount(), nuit) > tx.getTtc()){
							list.add(tx);
						}
					}

				}else{
					tx.setStatus(TransactionStatus.CLOSE);
					Subscriber s = tx.getSubscriber();
					s.setStatus(StatutContrat.SUSPENDU);
					s.setUtiSuspendu("AUTO");
					s.setDateSuspendu(new Date());
					dao.getEntityManager().merge(s);
					dao.getEntityManager().merge(tx);
				}

			} //else tx.setDateControle(new Date());

		}
		values.clear();
		eves.clear();
		return list;
	}



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#exportComptabilisationIntoExcelFile(java.util.List, java.lang.String)
	 */
	@Override
	@AllowedRole(name = "exportComptabilisationIntoExcelFile", displayName = "OgMo.Export.TFJO.To.Excel")
	public void exportComptaIntoExcelFile( List<Transaction> data, String fileName ) throws Exception {

		// Initialisation d'un document Excel
		SXSSFWorkbook wb = new SXSSFWorkbook();

		// Initialisation de la Feuille courante
		Sheet sheet  = wb.createSheet("Ecritures Comptables");

		// Creation d'une ligne
		Row row = sheet.createRow(0);

		// Affichage des entetes de colonnes du fichier excel
		row.createCell(0).setCellValue( "N°" );
		row.createCell(1).setCellValue( "N° Mvt" );
		row.createCell(2).setCellValue( "Agence" );
		row.createCell(3).setCellValue( "Date Comptable" );
		row.createCell(4).setCellValue( "Libellé" );
		row.createCell(5).setCellValue( "Sens" );
		row.createCell(6).setCellValue( "N° de Cpte" );
		row.createCell(7).setCellValue( "Intitulé" );
		row.createCell(8).setCellValue( "Montant" );
		row.createCell(9).setCellValue( "Opération" );
		row.createCell(10).setCellValue( "N° de Pièce" );
		row.createCell(11).setCellValue( "Réf. de Lettrage" );

		// Initialisation du compteur
		int i = 1;

		// Recuperation de la liste des evenements
		List<bkeve> eves = dao.filter(bkeve.class, null, RestrictionsContainer.getInstance().add(Restrictions.in("transaction",data)), null, null, 0, -1);

		// Parcours de la liste des evenements
		for(bkeve eve : eves) {

			for(bkmvti ec : eve.getEcritures()){

				// Initialisation d'une ligne
				row = sheet.createRow(i);

				// Affichage des colonnes dans la fichier excel
				row.createCell(0).setCellValue( i++ );
				row.createCell(1).setCellValue( ec.getMvti() );
				row.createCell(2).setCellValue( ec.getAge() );
				row.createCell(3).setCellValue( ec.getDco() );
				row.createCell(4).setCellValue( ec.getLib() );
				row.createCell(5).setCellValue( ec.getSen() );
				row.createCell(6).setCellValue( ec.getNcp() );
				row.createCell(7).setCellValue( ec.getLabel() );
				row.createCell(8).setCellValue( ec.getMon() );
				row.createCell(9).setCellValue( ec.getOpe() );
				row.createCell(10).setCellValue( ec.getPie() );
				row.createCell(11).setCellValue( ec.getRlet() );

			}

		}

		/**
		 * DEUXIEME FEUILLE
		 */

		// Initialisation de la Feuille courante
		Sheet sheet2  = wb.createSheet("Synthese");

		// Creation d'une ligne
		row = sheet2.createRow(0);

		// Affichage des entetes de colonnes du fichier excel
		row.createCell(0).setCellValue( "N°" );
		row.createCell(1).setCellValue( "N° de compte" );
		row.createCell(2).setCellValue( "Client" );
		row.createCell(3).setCellValue( "Date Abonnement" );
		row.createCell(4).setCellValue( "Date Facturation" );
		row.createCell(5).setCellValue( "Sens" );
		row.createCell(6).setCellValue( "Montant" );
		row.createCell(7).setCellValue( "Taxes" );
		row.createCell(8).setCellValue( "Agence" );

		// Initialisation du compteur
		i = 1;

		// Parcours des transactions
		for(Transaction tx : data) {

			// Initialisation d'une ligne
			row = sheet2.createRow(i);

			// Affichage des colonnes dans la fichier excel
			row.createCell(0).setCellValue( i++ );
			row.createCell(1).setCellValue( tx.getSubscriber().getFirstAccount() );
			row.createCell(2).setCellValue( tx.getSubscriber().getCustomerName() );
			row.createCell(3).setCellValue( tx.getSubscriber().getDate() );
			row.createCell(4).setCellValue( tx.getDateCompta() );
			row.createCell(5).setCellValue( "D" );
			row.createCell(6).setCellValue( tx.getAmount() + tx.getCommissions() );
			row.createCell(7).setCellValue( tx.getTaxes() );
			row.createCell(8).setCellValue( tx.getSubscriber().getFirstAccount().substring(0, 5) );
		}


		// Sauvegarde du fichier
		FileOutputStream fileOut = new FileOutputStream(PortalHelper.JBOSS_DATA_DIR + File.separator + PortalHelper.PORTAL_RESOURCES_DATA_DIR + File.separator + PortalHelper.PORTAL_DOWNLOAD_DATA_DIR + File.separator + fileName);
		wb.write(fileOut);
		fileOut.close();

	}




	public void exportRapprochmentBkmvti(String fileName, List<bkmvti> ecritures, List<bkmvti> ecrituresCli, List<bkmvti> ecrituresOrange, List<bkmvti> ecrituresCliRapp, 
			List<bkmvti> ecrituresOrangeRapp, List<bkmvti> ecrituresCliUnijamb, List<bkmvti> ecrituresOrangeUnijamb, List<bkmvti> ecrituresRemove, List<Transaction> transactionsRemove) throws Exception {

		// Initialisation d'un document Excel
		SXSSFWorkbook wb = new SXSSFWorkbook();

		System.out.println("*************** ecritures.size() *************** : " + ecritures.size());
		if(ecritures.size() > 0 ) exportECBkmvti(ecritures, wb, "ALL_ECRITURES");
		System.out.println("*************** ecrituresCli.size() *************** : " + ecrituresCli.size());
		if(ecrituresCli.size() > 0 ) exportECBkmvti(ecrituresCli, wb, "ECRITURES_NCP_CLIENT");
		System.out.println("*************** ecrituresOrange.size() *************** : " + ecrituresOrange.size());
		if(ecrituresOrange.size() > 0 ) exportECBkmvti(ecrituresOrange, wb, "ECRITURES_NCP_ORANGE");
		System.out.println("*************** ecrituresCliRapp.size() *************** : " + ecrituresCliRapp.size());
		if(ecrituresCliRapp.size() > 0 ) exportECBkmvti(ecrituresCliRapp, wb, "ECRITURES_RAPPROCHEES_NCP_CLIENT");
		System.out.println("*************** ecrituresOrangeRapp.size() *************** : " + ecrituresOrangeRapp.size());
		if(ecrituresOrangeRapp.size() > 0 ) exportECBkmvti(ecrituresOrangeRapp, wb, "ECRITURES_RAPPROCHEES_NCP_ORANGE");
		System.out.println("*************** ecrituresCliUnijamb.size() *************** : " + ecrituresCliUnijamb.size());
		if(ecrituresCliUnijamb.size() > 0 ) exportECBkmvti(ecrituresCliUnijamb, wb, "ECRITURES_UNIJAMBISTES_NCP_CLIENT");
		System.out.println("*************** ecrituresOrangeUnijamb.size() *************** : " + ecrituresOrangeUnijamb.size());
		if(ecrituresOrangeUnijamb.size() > 0 ) exportECBkmvti(ecrituresOrangeUnijamb, wb, "ECRITURES_UNIJAMBISTES_NCP_ORANGE");
		System.out.println("*************** ecrituresRemove.size() *************** : " + ecrituresRemove.size());
		if(ecrituresRemove.size() > 0 ) exportECBkmvti(ecrituresRemove, wb, "ECRITURES_SUPPRIMEES");

		System.out.println("*************** transactionsRemove.size() *************** : " + transactionsRemove.size());
		if(transactionsRemove.size() > 0 ) exportTransactions(transactionsRemove, wb, "TRANSACTIONS_SUPPRIMEES");


		// Sauvegarde du fichier
		FileOutputStream fileOut = new FileOutputStream(PortalHelper.JBOSS_DATA_DIR + File.separator + PortalHelper.PORTAL_RESOURCES_DATA_DIR + File.separator + PortalHelper.PORTAL_DOWNLOAD_DATA_DIR + File.separator + fileName);
		wb.write(fileOut);
		fileOut.close();
	}



	public void exportRapprochmentBkeve(String fileName, List<Transaction> transactions, List<bkeve> evePortals, List<bkeve> eveCBS, List<bkeve> evePortalRapps, List<bkeve> eveCBSRapps, 
			List<bkeve> evePortalSuspens, List<bkeve> eveCBSSuspens) throws Exception {

		// Initialisation d'un document Excel
		SXSSFWorkbook wb = new SXSSFWorkbook();

		List<Commentaire> recaps = new ArrayList<Commentaire>(); // Ajoute
		recaps.add(new Commentaire("RECAPITULATIF SUR LE CONTROLE DE RESERVATION : ", ""));
		recaps.add(new Commentaire("--------------------------------------------", ""));
		recaps.add(new Commentaire("", ""));


		recaps.add(new Commentaire("TRANSACTIONS SUR LA PERIODE : ", Integer.toString(transactions.size())));
		recaps.add(new Commentaire("EVENEMENTS PORTAL : ", Integer.toString(evePortals.size())));
		recaps.add(new Commentaire("EVENEMENTS CORE BANKING : ", Integer.toString(eveCBS.size())));
		recaps.add(new Commentaire("EVENEMENTS PORTAL RAPPROCHÉS : ", Integer.toString(evePortalRapps.size())));
		recaps.add(new Commentaire("EVENEMENTS CORE BANKING RAPPROCHÉS : ", Integer.toString(eveCBSRapps.size())));
		recaps.add(new Commentaire("EVENEMENTS SUSPENS PORTAL : ", Integer.toString(evePortalSuspens.size())));
		recaps.add(new Commentaire("EVENEMENTS SUSPENS CORE BANKING : ", Integer.toString(eveCBSSuspens.size())));


		exportRecaps(recaps, wb, "RECAPITULATIF");

		//*** System.out.println("*************** transactions.size() *************** : " + transactions.size());
		if(transactions.size() > 0 ) exportTransactions(transactions, wb, "TRANSACTIONS");


		//*** System.out.println("*************** evePortals.size() *************** : " + evePortals.size());
		if(evePortals.size() > 0 ) exportBkeve(evePortals, wb, "EVE_PORTALS");
		//*** System.out.println("*************** eveCBS.size() *************** : " + eveCBS.size());
		if(eveCBS.size() > 0 ) exportBkeve(eveCBS, wb, "EVE_CBS");
		//*** System.out.println("*************** evePortalRapps.size() *************** : " + evePortalRapps.size());
		if(evePortalRapps.size() > 0 ) exportBkeve(evePortalRapps, wb, "EVE_PORTALS_RAPPROCHES");
		//*** System.out.println("*************** eveCBSRapps.size() *************** : " + ecrituresCliRapp.size());
		if(eveCBSRapps.size() > 0 ) exportBkeve(eveCBSRapps, wb, "EVE_CBS_RAPPROCHES");
		//*** System.out.println("*************** evePortalSuspens.size() *************** : " + evePortalSuspens.size());
		if(evePortalSuspens.size() > 0 ) exportBkeve(evePortalSuspens, wb, "EVE_PORTALS_SUSPENS");
		//*** System.out.println("*************** eveCBSSuspens.size() *************** : " + eveCBSSuspens.size());
		if(eveCBSSuspens.size() > 0 ) exportBkeve(eveCBSSuspens, wb, "EVE_CBS_SUSPENS");
		//*** System.out.println("*************** ecrituresOrangeUnijamb.size() *************** : " + ecrituresOrangeUnijamb.size());

		// Sauvegarde du fichier
		FileOutputStream fileOut = new FileOutputStream(PortalHelper.JBOSS_DATA_DIR + File.separator + PortalHelper.PORTAL_RESOURCES_DATA_DIR + File.separator + PortalHelper.PORTAL_DOWNLOAD_DATA_DIR + File.separator + fileName);
		wb.write(fileOut);
		fileOut.close();
	}



	public void exportECBkmvti(List<bkmvti> ecritures, Object file, String sheetName) throws Exception {
		if (ecritures == null || ecritures.isEmpty()) {
			return;
		}

		SXSSFWorkbook wb;

		if (file instanceof SXSSFWorkbook) {
			wb = (SXSSFWorkbook) file;
		} else {
			wb = new SXSSFWorkbook();
		}

		// Initialisation de la Feuille courante
		Sheet sheet = wb.createSheet(sheetName);

		// Creation d'une ligne
		Row row = sheet.createRow(0);

		// Affichage des entetes de colonnes du fichier excel
		row.createCell(0).setCellValue( "N°" );
		row.createCell(1).setCellValue( "Utilisateur" );
		row.createCell(2).setCellValue( "Agence" );
		row.createCell(3).setCellValue( "Date Comptable" );
		row.createCell(4).setCellValue( "Libellé" );
		row.createCell(5).setCellValue( "Sens" );
		row.createCell(6).setCellValue( "N° de Cpte" );
		row.createCell(7).setCellValue( "Nature opération" );
		row.createCell(8).setCellValue( "Montant" );
		row.createCell(9).setCellValue( "Opération" );
		row.createCell(10).setCellValue( "N° de Pièce" );

		//Initialisation du compteur
		int i = 1;

		for (bkmvti ec : ecritures) {
			// Initialisation d'une ligne
			row = sheet.createRow(i);

			// Affichage des colonnes dans la fichier excel
			row.createCell(0).setCellValue( i++ );
			row.createCell(1).setCellValue( ec.getUti() );
			row.createCell(2).setCellValue( ec.getAge() );
			row.createCell(3).setCellValue( new SimpleDateFormat("dd/MM/yyyy").format(ec.getDco()) );
			row.createCell(4).setCellValue( ec.getLib() );
			row.createCell(5).setCellValue( ec.getSen() );
			row.createCell(6).setCellValue( ec.getNcp() );
			row.createCell(7).setCellValue( ec.getNat() );
			row.createCell(8).setCellValue( ec.getMon() );
			row.createCell(9).setCellValue( ec.getOpe() );
			row.createCell(10).setCellValue( ec.getPie() );

		}

	}


	public void exportBkeve(List<bkeve> eves, Object file, String sheetName) throws Exception {
		if (eves == null || eves.isEmpty()) {
			return;
		}

		SXSSFWorkbook wb;

		if (file instanceof SXSSFWorkbook) {
			wb = (SXSSFWorkbook) file;
		} else {
			wb = new SXSSFWorkbook();
		}

		// Initialisation de la Feuille courante
		Sheet sheet = wb.createSheet(sheetName);

		// Creation d'une ligne
		Row row = sheet.createRow(0);

		// Affichage des entetes de colonnes du fichier excel
		row.createCell(0).setCellValue( "N°" );
		row.createCell(1).setCellValue( "N° EVE" );
		row.createCell(2).setCellValue( "DCO" );
		row.createCell(3).setCellValue( "AGE1" );
		row.createCell(4).setCellValue( "NCP1" );
		row.createCell(5).setCellValue( "CLC1" );
		row.createCell(6).setCellValue( "NOM1" );
		row.createCell(7).setCellValue( "SEN1" );
		row.createCell(8).setCellValue( "MON1" );
		row.createCell(9).setCellValue( "AGE2" );
		row.createCell(10).setCellValue( "NCP2" );
		row.createCell(11).setCellValue( "CLC2" );
		row.createCell(12).setCellValue( "NOM2" );
		row.createCell(13).setCellValue( "SEN2" );
		row.createCell(14).setCellValue( "MON2" );
		row.createCell(15).setCellValue( "ETA" );
		row.createCell(16).setCellValue( "DSAI" );
		row.createCell(17).setCellValue( "HSAI" );

		//Initialisation du compteur
		int i = 1;

		for (bkeve eve : eves) {
			// Initialisation d'une ligne
			row = sheet.createRow(i);

			// Affichage des colonnes dans la fichier excel
			row.createCell(0).setCellValue( i++ );
			row.createCell(1).setCellValue( eve.getEve() );
			row.createCell(2).setCellValue( new SimpleDateFormat("dd/MM/yyyy").format(eve.getDco()) );
			row.createCell(3).setCellValue( eve.getAge1());
			row.createCell(4).setCellValue( eve.getNcp1() );
			row.createCell(5).setCellValue( eve.getClc1() );
			row.createCell(6).setCellValue( eve.getNom1() );
			row.createCell(7).setCellValue( eve.getSen1() );
			row.createCell(8).setCellValue( eve.getMon1() );
			row.createCell(9).setCellValue( eve.getAge2() );
			row.createCell(10).setCellValue( eve.getNcp2() );
			row.createCell(11).setCellValue( eve.getClc2() );
			row.createCell(12).setCellValue( eve.getNom2() );
			row.createCell(13).setCellValue( eve.getSen2() );
			row.createCell(14).setCellValue( eve.getMon2());
			row.createCell(15).setCellValue( eve.getEta());
			row.createCell(16).setCellValue( new SimpleDateFormat("dd/MM/yyyy").format(eve.getDsai()) );
			row.createCell(17).setCellValue( eve.getHsai() );

		}

	}


	public void exportTransactions(List<Transaction> transactions, Object file, String sheetName) throws Exception {
		if (transactions == null || transactions.isEmpty()) {
			return;
		}

		SXSSFWorkbook wb;

		if (file instanceof SXSSFWorkbook) {
			wb = (SXSSFWorkbook) file;
		} else {
			wb = new SXSSFWorkbook();
		}

		// Initialisation de la Feuille courante
		Sheet sheet = wb.createSheet(sheetName);

		// Creation d'une ligne
		Row row = sheet.createRow(0);

		// Affichage des entetes de colonnes du fichier excel
		row.createCell(0).setCellValue( "N°" );
		row.createCell(1).setCellValue( "N° de compte" );
		row.createCell(2).setCellValue( "Client" );
		row.createCell(3).setCellValue( "Type Opération" );
		row.createCell(4).setCellValue( "Date Transaction" );
		row.createCell(5).setCellValue( "Montant" );
		row.createCell(6).setCellValue( "Téléphone" );
		row.createCell(7).setCellValue( "Statut" );

		//Initialisation du compteur
		int i = 1;

		for (Transaction tx : transactions) {
			// Initialisation d'une ligne
			row = sheet.createRow(i);

			// Affichage des colonnes dans la fichier excel
			row.createCell(0).setCellValue( i++ );
			row.createCell(1).setCellValue( tx.getAccount() );
			row.createCell(2).setCellValue( tx.getSubscriber().getCustomerName() );
			row.createCell(3).setCellValue( tx.getTypeOperation().getValue() );
			row.createCell(4).setCellValue( new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK).format(tx.getDate()));
			row.createCell(5).setCellValue( tx.getAmount());
			row.createCell(6).setCellValue( tx.getPhoneNumber() );
			row.createCell(7).setCellValue( tx.getStatus().getValue());

		}

	}


	public void exportRecaps(List<Commentaire> recaps, Object file, String sheetName) throws Exception {
		if (recaps == null || recaps.isEmpty()) {
			return;
		}

		SXSSFWorkbook wb;

		if (file instanceof SXSSFWorkbook) {
			wb = (SXSSFWorkbook) file;
		} else {
			wb = new SXSSFWorkbook();
		}

		// Initialisation de la Feuille courante
		Sheet sheet = wb.createSheet(sheetName);

		// Creation d'une ligne
		Row row = sheet.createRow(0);

		// Affichage des entetes de colonnes du fichier excel
		row.createCell(0).setCellValue( "N°" );
		row.createCell(1).setCellValue( "LIBELLÉ" );
		row.createCell(2).setCellValue( "VALEUR" );

		//Initialisation du compteur
		int i = 1;

		for (Commentaire re : recaps) {
			// Initialisation d'une ligne
			row = sheet.createRow(i);

			// Affichage des colonnes dans la fichier excel
			row.createCell(0).setCellValue( i++ );
			row.createCell(1).setCellValue( re.getLibelle());
			row.createCell(2).setCellValue( re.getValeur());

		}

	}



	public void checkEveByTransaction(String fileName, List<Transaction> transactions, DataSystem dsCBS, Parameters param) throws Exception {

		// Initialisation d'un document Excel
		SXSSFWorkbook wb = new SXSSFWorkbook();


		if(transactions == null || transactions.isEmpty()) return;

		// Initialisation du filtre des transactions
		String in = "";

		// Recuperation  de l'id de chaque transaction selectionnee dans le filtre
		for(Transaction t : transactions) if(t.isSelected()) { in += t.getId() + ", "; }

		// Construction du filtre sur les transactions
		if(!in.isEmpty()) in = "(" + in.substring(0, in.length()-2) + ")";

		// Si aucune transaction n'a ete selectionnee on sort
		if(in.isEmpty()) return;

		// Recherche des code evenements et codes operations des transactions selectionnees
		List<bkeve> eves = dao.getEntityManager().createQuery("Select e.eve, e.ope From bkeve e where e.transaction.id in "+ in +"").getResultList();

		// Si aucun evenement trouve on sort
		if(eves == null || eves.isEmpty())  throw new Exception("aucun evenement trouve dans Portal");



		//Recherche des eves dans le CBS
		String req_infosCompte = "select ope, eve, dev, mht, nat, dco, uti, tcom1, frai1, ttax1, mnt1 from bkeve where eta='VA and ope = '"+ param.getCodeOpe() +"' ";
		ResultSet resultSet = executeFilterSystemQuery(dsCBS, req_infosCompte, new Object[]{});


		/*
		 List<bkeve> evesCBS = = new ArrayList<bkeve>(); 
		 if(resultSet.next()) {
			 evesCBS.add(new bkeve(null, String ope, String eve, String dev, Double mht, String nat, Date dco, String uti, Double tcom1, Double frai1, Double ttax1, Double mnt1));
		 }
		 else{
			 throw new Exception("aucun evenement trouve dans CBS");
		 }
		 */





		// Initialisation du filtre des evenements et du code operation
		String inEve = ""; String ope = "";

		for(Object o : eves) {
			inEve += "'".concat( ((Object[])o)[0].toString()  ).concat("', ");
			if(ope.isEmpty()) ope = ((Object[])o)[1].toString();
		}

		// Construction du filtre des evenements a mettre a jour dans Delta
		if(!inEve.isEmpty()) inEve = "(".concat(inEve.substring(0, inEve.length()-2)).concat(")");

		// MAJ de l'etat des evenements dans le Core Banking
		executeUpdateSystemQuery(dsCBS, "update bkeve set eta='IG', etap='VA' where eve in "+ inEve +" and ope=? ", new Object[]{ ope  } );

		// MAJ de l'etat des evenements dans le Core Banking
		// executeUpdateSystemQuery(dsCBS, "update bkeve set eta='IG', etap='VA' where eta != 'IG' and ope=? ", new Object[]{ ope  } );

		// MAJ des transactions
		dao.getEntityManager().createQuery("Update Transaction t set t.posted=:posted where t.id in "+ in +"").setParameter("posted", Boolean.TRUE).executeUpdate();






		if(transactions.size() > 0 ) exportTransactions(transactions, wb, "TRANSACTIONS_MAC_ORANGE");


		// Sauvegarde du fichier
		FileOutputStream fileOut = new FileOutputStream(PortalHelper.JBOSS_DATA_DIR + File.separator + PortalHelper.PORTAL_RESOURCES_DATA_DIR + File.separator + PortalHelper.PORTAL_DOWNLOAD_DATA_DIR + File.separator + fileName);
		wb.write(fileOut);
		fileOut.close();

		CloseStream.fermeturesStream(resultSet); 

		//if(conCBS != null ) conCBS.close();

	}





	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.business.IMobileMoneyManagerRemote#vaiderTFJO(java.util.List)
	 */
	@Override
	@AllowedRole(name = "validerTFJO", displayName = "OgMo.Valider.TFJO")
	//@TransactionTimeout(value = 160000)
	public void validerTFJO(List<Transaction> data, String user, int year, int month) throws Exception {

		// Recuperation de la DataSource du Core Banking
		if(dsCBS == null) findCBSDataSystem();
		Date dco = getDateComptable();
		//Long numEve = getLastEveNum(dsCBS);
		Date dvaDebit = getDvaDebit();
		Date dvaCredit = getDvaCredit();

		Map<Long,Transaction> mapTrans = new HashMap<Long,Transaction>();
		for(Transaction t : data) mapTrans.put(t.getId(),t);

		// Marqueur du mode nuit
		boolean nuit = isModeNuit();

		// Initialisation de la liste des EC a poster
		List<bkmvti> mvts = new ArrayList<bkmvti>(); // Ajoute
		List<Subscriber> subs = new ArrayList<Subscriber>();

		// Parcours de la liste des abonnements a comptabiliser
		List<bkeve> eves = dao.filter(bkeve.class, null, RestrictionsContainer.getInstance().add(Restrictions.in("transaction",data)), null, null, 0, -1);


		// Ouverture d'une cnx vers la BD du Core Banking
		if(conCBS == null || conCBS.isClosed()) conCBS = getSystemConnection(dsCBS);

		// Suspension temporaire du mode blocage dans la BD du Core Banking
		if(dsCBS.getDbConnectionString().indexOf("informix") > 0) conCBS.createStatement().executeUpdate("SET ISOLATION TO DIRTY READ");

		// Desactivation du mode AUTO COMMIT
		conCBS.setAutoCommit(false);


		// Initialisation d'un preparateur de requetes
		String req = "update bkcom set sin = sin - ?  where age=? and ncp=? and clc=?";
		PreparedStatement psAmt = conCBS.prepareStatement(req);

		for(bkeve eve : eves) {

			Transaction tx = mapTrans.get(eve.getTransaction().getId());
			//Long numEve = getLastEveNum(dsCBS);
			Long numEve = (long) (new Random().nextInt(900000) + 100000);

			// Si l'objet courant a ete selectionne par l'utilisateur et ses ecritures sont equilibrees
			if(eve.isEquilibre()) { //if(c.isSelected() && c.getEve().isEquilibre()) {

				// Si le solde du compte est suffisant
				if(getSolde(tx.getSubscriber().getFirstAccount(), nuit) > tx.getTtc()){

					if(eve.getEve().equals("000000")) eve.setEve(OgMoHelper.padText(String.valueOf(numEve), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"));

					// Parcours des ecritures comptables
					for(bkmvti mvt : eve.getEcritures()) {

						// MAJ des parametres des EC generes
						mvt.setUti(user);
						mvt.setDco(dco);
						if(tx.getStatus().equals(TransactionStatus.REGUL)){
							/**Subscriber s = c.getSubscriber();
							String phoneNumber = s.getFirstPhone();
							String lib = "FRAIS  RGUL/" + datop + "/" + (s.getCustomerName().trim().length()>=8 ? s.getCustomerName().trim().toUpperCase().substring(0, 8) : s.getCustomerName().trim().toUpperCase()) + "/" + (phoneNumber != null ? phoneNumber.replaceAll("237", "") : phoneNumber);
							 */
							mvt.setLib( mvt.getLib().replaceFirst(" MAC", " RGUL") );
							if(mvt.getLib().contains("HIP")) mvt.setLib(mvt.getLib().replaceFirst("HIP", "LS"));

							if(mvt.getSen().equals("D")){
								mvt.setDva(dvaDebit);
								mvt.setDin(new Date());
							}

							if(mvt.getSen().equals("C")){
								mvt.setDva(dvaCredit);
								mvt.setDin(new Date());
							}

							//mobileMoneyDAO.getEntityManager().merge(mvt);
						}
						if(mvt.getEve().equals("000000")) mvt.setEve(eve.getEve());

					}

					// Reduction du solde du client
					String numCompte = tx.getSubscriber().getFirstAccount();
					psAmt.setDouble(1, tx.getTtc());
					psAmt.setString(2, numCompte.split("-")[0]);
					psAmt.setString(3, numCompte.split("-")[1]);
					psAmt.setString(4, numCompte.split("-")[2]);
					psAmt.addBatch();
					//reduitSolde(tx.getSubscriber().getFirstAccount(), tx.getTtc(), nuit);
					// MAJ du statut de la transaction
					tx.setStatus(TransactionStatus.SUCCESS);
					tx.setPosted(Boolean.TRUE);
					tx.setDateTraitement(new Date());

					//mobileMoneyDAO.updateSubscriber(s);


				}else {
					// On positionne le status de l'operation en REGUL
					tx.setDateTraitement(new Date());
					tx.setStatus(TransactionStatus.REGUL);

				}
				Subscriber s = tx.getSubscriber();
				s.setDateSaveDernCompta(s.getDateDernCompta());
				s.setDateDernCompta(getNextFacturationDate(s.getDateDernCompta()));
				subs.add(s);

				// Chargement de la liste des EC a poster
				if(!tx.getStatus().equals(TransactionStatus.REGUL)) mvts.addAll(eve.getEcritures()); // Ajoute

				// MAJ des evenements, transactions, abonnements a comptabiliser et ecritures par cascade
				//mobileMoneyDAO.updateTransaction(tx);

			}

		}

		psAmt.executeBatch();

		// Initialisation d'un preparateur de requetes
		PreparedStatement ps = conCBS.prepareStatement(new bkmvti().getSaveQuery());

		// Parcours de la liste des EC a poster
		for(bkmvti m : mvts) {

			// Ajout dans le lot
			ps = m.addPrepareStatement(ps);

			// Ajout du Lot i
			ps.addBatch();

		}

		// Lancement de l'execution du Lot de requetes sur le serveur DELTA
		ps.executeBatch();

		// Commit
		conCBS.setAutoCommit(true);

		// Fermeture de la cnx preparee
		ps.close(); ps = null; psAmt.close(); psAmt = null;
		//if(conCBS != null ) conCBS.close();

		//processClose(subs, data);

		//processupdateTFJO(subs, data);
		dao.updateSubscriber(subs);
		dao.updateTransaction(data);

		// Liberation des ressources
		mvts.clear();
		mapTrans.clear();
		data.clear();
		subs.clear();

	}


	public Date getDvaCredit() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 1);

		while(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) cal.add(Calendar.DATE, 1);

		return cal.getTime();
	}

	public Date getDvaDebit() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1);

		while(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) cal.add(Calendar.DATE, -1);

		return cal.getTime();
	}


	public Date getLastFacturationDate(Date date){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		int day =  cal.get(Calendar.MONTH) == Calendar.FEBRUARY ? 28 : 30;
		return DateUtils.addDays(date, -day);
	}



	public Date getNextFacturationDate(Date date){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		int day =  cal.get(Calendar.MONTH) == Calendar.FEBRUARY ? 28 : 30;
		return DateUtils.addDays(date, day);
	}



	private Double getSolde(String numCompte, boolean nuit) throws Exception {

		return getSoldeInCoreBanking(numCompte);

	}



	private List<bkeve> buildEventsForComptabilisation(List<Subscriber> subs, Double tva, Date dateCompta) throws Exception {
		// Initialisation de la liste a retourner
		List<bkeve> data = new ArrayList<bkeve>();

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();

		// Lecture des parametres generaux
		params = findParameters();

		// Initialisations des ResultSets
		ResultSet rsCpteAbonne = null, rsCpteComs = null, rsCpteTVA = null, rsCpteLiaison = null, rsLiaisonComs = null, rsLiaisonTVA = null;

		// Initialisations
		Long numEc = 1l;
		Date dco = getDateComptable();
		Date dvaDebit = getDvaDebit();

		//Long numEve = getLastEveNum(dsCBS);

		/**
		 * *******************************************************************
		 * RECUPERATION DES COMPTES DE COMMISSIONS ET DE TAXES DANS DELTA
		 * *******************************************************************
		 */

		// Si le compte des commissions a ete parametre
		if(params.getNumCompteCommissions() != null && !params.getNumCompteCommissions().isEmpty()) 

			// Recuperation du numero de cpte des commissions
			rsCpteComs = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(7).getQuery(), new Object[]{ params.getNumCompteCommissions().split("-")[0], params.getNumCompteCommissions().split("-")[1], params.getNumCompteCommissions().split("-")[2] });

		if(rsCpteComs != null) rsCpteComs.next();

		// Si le numero de cpte TVA a ete parametre
		if(params.getNumCompteTVA() != null && !params.getNumCompteTVA().isEmpty())

			// Recuperation du numero de compte TVA
			rsCpteTVA = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(7).getQuery(), new Object[]{ params.getNumCompteTVA().split("-")[0], params.getNumCompteTVA().split("-")[1], params.getNumCompteTVA().split("-")[2] });

		if(rsCpteTVA != null) rsCpteTVA.next();


		// Parcours de la liste des abonnes passee en parametre
		for(Subscriber s : subs){

			//System.out.println("**************** s.getNameUtiAbon() **************** : " + s.getNameUtiAbon());


			if(s.getFirstAccount()!= null){

				////System.out.println("**************** s.getFirstAccount()!= null **************** : " + s.getCustomerId());


				String datop = new SimpleDateFormat("ddMMyy").format(getNextFacturationDate(s.getDateDernCompta()));
				//Long numEve = getLastEveNum(dsCBS);
				Long numEve = (long) (new Random().nextInt(900000) + 100000);
				Double com = 0d;
				if(s.getProfil()!=null){
					com = s.getProfil().getCommissions();
				}else{
					com = s.getCommissions();
				}

				// Initialisation de la transaction a comptabiliser
				Transaction tx = new Transaction(TypeOperation.COMPTABILISATION, s, 0d, s.getFirstAccount(), s.getFirstPhone() , new Date(), TransactionStatus.PROCESSING, com, Math.ceil( com * (1 + tva/100) ), dateCompta );

				// Initialisation de l'evenement a generer OgMoHelper.padText(String.valueOf(numEve), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0")
				//bkeve eve = new bkeve(tx, params.getCodeOperation(), "000000", "001", tx.getAmount(), "VIRMAC", dco, params.getCodeUtil(), 1d, tx.getCommissions(), tx.getTaxes(), tx.getTtc());
				bkeve eve = new bkeve(tx, params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEve), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), "001", tx.getAmount(), "VIRMAC", dco, params.getCodeUtil(), 1d, tx.getCommissions(), tx.getTaxes(), tx.getTtc());

				// Recherche du cpte de l'abonne
				rsCpteAbonne = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(6).getQuery(), new Object[]{ tx.getAccount().split("-")[0], tx.getAccount().split("-")[1], tx.getAccount().split("-")[2] });

				// Si le cpte de l'abonne existe dans DELTA
				if(rsCpteAbonne != null && rsCpteAbonne.next()) {

					if(rsCpteAbonne.getString("ife").equals("N") && rsCpteAbonne.getString("cfe").equals("N") && rsCpteAbonne.getString("cha").startsWith("37")){

						// Ajout du debiteur
						eve.setDebiteur(rsCpteAbonne.getString("age"), rsCpteAbonne.getString("dev"), rsCpteAbonne.getString("ncp"), rsCpteAbonne.getString("suf"), rsCpteAbonne.getString("clc"), rsCpteAbonne.getString("cli"), s.getCustomerName(), " ", tx.getCommissions(), tx.getCommissions(), dvaDebit, rsCpteAbonne.getDouble("sde"));

						// Ajout du Crediteur
						eve.setCrediteur(rsCpteComs.getString("age"), rsCpteComs.getString("dev"), rsCpteComs.getString("ncp"), rsCpteComs.getString("suf"), rsCpteComs.getString("clc"), rsCpteComs.getString("cli"), rsCpteComs.getString("inti"), "   ", tx.getCommissions(), tx.getCommissions(), dvaDebit, rsCpteComs.getDouble("sde"));

						// Libelle de l'evenement
						eve.setLib1(datop + "/" + tx.getTypeOperation().toString().substring(0, 5) + "/MAC/" + tx.getPhoneNumber());


						/***-**************************************
						 * *** GENERATION DES ECRITURES COMPTABLES
						 * *****************************************
						 */

						// Debit du cpte client du montant valeurCom + valeurTVA
						eve.getEcritures().add( new bkmvti(rsCpteAbonne.getString("age"), rsCpteAbonne.getString("dev"), rsCpteAbonne.getString("cha"), rsCpteAbonne.getString("ncp"), rsCpteAbonne.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteAbonne.getString("clc"), dco, null, rsCpteAbonne.getDate("dva"), tx.getTtc(), "D", "FRAIS MAC/" + datop + "/" + (tx.getPhoneNumber() != null ? tx.getPhoneNumber().replaceAll("237", "") : tx.getPhoneNumber()) + "/" + (s.getCustomerName().trim().length()>=8 ? s.getCustomerName().trim().toUpperCase().substring(0, 8) : s.getCustomerName().trim().toUpperCase()) , "N", s.getId().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteAbonne.getString("age"), rsCpteAbonne.getString("dev"), tx.getTtc(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;

						// Credit cpte Comissions de valeurCom
						eve.getEcritures().add( new bkmvti(rsCpteComs.getString("age"), rsCpteComs.getString("dev"), rsCpteComs.getString("cha"), rsCpteComs.getString("ncp"), rsCpteComs.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteComs.getString("clc"), dco, null, rsCpteComs.getDate("dva"), tx.getCommissions(), "C", "COM MAC/" + datop + "/" + (tx.getPhoneNumber() != null ? tx.getPhoneNumber().replaceAll("237", "") : tx.getPhoneNumber()) + "/" + (s.getCustomerName().trim().length()>=8 ? s.getCustomerName().trim().toUpperCase().substring(0, 8) : s.getCustomerName().trim().toUpperCase()), "N", s.getId().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteComs.getString("age"), rsCpteComs.getString("dev"), tx.getCommissions(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;

						// Credit cpte TVA de valeurTVA
						eve.getEcritures().add( new bkmvti(rsCpteTVA.getString("age"), rsCpteTVA.getString("dev"), rsCpteTVA.getString("cha"), rsCpteTVA.getString("ncp"), rsCpteTVA.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteTVA.getString("clc"), dco, null, rsCpteTVA.getDate("dva"), tx.getTaxes(), "C", "TAX MAC/" + datop + "/" + (tx.getPhoneNumber() != null ? tx.getPhoneNumber().replaceAll("237", "") : tx.getPhoneNumber()) + "/" + (s.getCustomerName().trim().length()>=8 ? s.getCustomerName().trim().toUpperCase().substring(0, 8) : s.getCustomerName().trim().toUpperCase()), "N", s.getId().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteTVA.getString("age"), rsCpteTVA.getString("dev"), tx.getTaxes(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;

						//**** rsLiaisonComs = null, 


						// Si l'agence du compte de commission n'est pas le meme que celui du client
						if(!rsCpteAbonne.getString("age").equals(rsCpteComs.getString("age"))){

							// Recuperation du compte de liaison de l'agence du client
							rsCpteLiaison = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where age='"+ rsCpteAbonne.getString("age") +"' and dev='001' and ncp='"+ params.getNumCompteLiaison() +"'", null);

							// Recuperation du compte de liaison de l'agence du compte de commission
							rsLiaisonComs = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where age='"+ rsCpteComs.getString("age") +"' and dev='001' and ncp='"+ params.getNumCompteLiaison() +"'", null);

							// Si le compte de liaison existe
							if(rsCpteLiaison != null && rsCpteLiaison.next() && rsLiaisonComs != null && rsLiaisonComs.next()) {
								// Passage de l'ecriture dans les liaisons
								eve.getEcritures().add( new bkmvti(rsCpteLiaison.getString("age"), rsCpteLiaison.getString("dev"), rsCpteLiaison.getString("cha"), rsCpteLiaison.getString("ncp"), rsCpteLiaison.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaison.getString("clc"), dco, null, rsCpteLiaison.getDate("dva"), tx.getCommissions(), "C", "COM MAC/" + datop + "/" + (tx.getPhoneNumber() != null ? tx.getPhoneNumber().replaceAll("237", "") : tx.getPhoneNumber()) + "/" + (s.getCustomerName().trim().length()>=8 ? s.getCustomerName().trim().toUpperCase().substring(0, 8) : s.getCustomerName().trim().toUpperCase()), "O", s.getId().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaison.getString("age"), rsCpteLiaison.getString("dev"), tx.getCommissions(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
								eve.getEcritures().add( new bkmvti(rsLiaisonComs.getString("age"), rsLiaisonComs.getString("dev"), rsLiaisonComs.getString("cha"), rsLiaisonComs.getString("ncp"), rsLiaisonComs.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonComs.getString("clc"), dco, null, rsLiaisonComs.getDate("dva"), tx.getCommissions(), "D", "COM MAC/" + datop + "/" + (tx.getPhoneNumber() != null ? tx.getPhoneNumber().replaceAll("237", "") : tx.getPhoneNumber()) + "/" + (s.getCustomerName().trim().length()>=8 ? s.getCustomerName().trim().toUpperCase().substring(0, 8) : s.getCustomerName().trim().toUpperCase()), "O", s.getId().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonComs.getString("age"), rsLiaisonComs.getString("dev"), tx.getCommissions(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
							}

						}



						// Si l'agence du compte de TVA n'est pas le meme que celui du client
						if(!rsCpteAbonne.getString("age").equals(rsCpteComs.getString("age"))){
							CloseStream.fermeturesStream(rsCpteLiaison); rsCpteLiaison = null;
							
							// Recuperation du compte de liaison de l'agence du client
							rsCpteLiaison = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where age='"+ rsCpteAbonne.getString("age") +"' and dev='001' and ncp='"+ params.getNumCompteLiaison() +"'", null);

							// Recuperation du compte de liaison de l'agence du compte de commission
							rsLiaisonTVA = executeFilterSystemQuery(dsCBS, "select age, dev, cha, ncp, suf, clc, dva, sde, inti, utic from bkcom where age='"+ rsCpteTVA.getString("age") +"' and dev='001' and ncp='"+ params.getNumCompteLiaison() +"'", null);

							// Si le compte de liaison existe
							if(rsCpteLiaison != null && rsCpteLiaison.next() && rsLiaisonTVA != null && rsLiaisonTVA.next()) {
								// Passage de l'ecriture dans les liaisons
								eve.getEcritures().add( new bkmvti(rsCpteLiaison.getString("age"), rsCpteLiaison.getString("dev"), rsCpteLiaison.getString("cha"), rsCpteLiaison.getString("ncp"), rsCpteLiaison.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsCpteLiaison.getString("clc"), dco, null, rsCpteLiaison.getDate("dva"), tx.getTaxes(), "C", "TAX MAC/" + datop + "/" + (tx.getPhoneNumber() != null ? tx.getPhoneNumber().replaceAll("237", "") : tx.getPhoneNumber()) + "/" + (s.getCustomerName().trim().length()>=8 ? s.getCustomerName().trim().toUpperCase().substring(0, 8) : s.getCustomerName().trim().toUpperCase()), "O", s.getId().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsCpteLiaison.getString("age"), rsCpteLiaison.getString("dev"), tx.getTaxes(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
								eve.getEcritures().add( new bkmvti(rsLiaisonTVA.getString("age"), rsLiaisonTVA.getString("dev"), rsLiaisonTVA.getString("cha"), rsLiaisonTVA.getString("ncp"), rsLiaisonTVA.getString("suf"), params.getCodeOperation(), OgMoHelper.padText(String.valueOf(numEc), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, params.getCodeUtil(), eve.getEve(), rsLiaisonTVA.getString("clc"), dco, null, rsLiaisonTVA.getDate("dva"), tx.getTaxes(), "D", "TAX MAC/" + datop + "/" + (tx.getPhoneNumber() != null ? tx.getPhoneNumber().replaceAll("237", "") : tx.getPhoneNumber()) + "/" + (s.getCustomerName().trim().length()>=8 ? s.getCustomerName().trim().toUpperCase().substring(0, 8) : s.getCustomerName().trim().toUpperCase()), "O", s.getId().toString(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rsLiaisonTVA.getString("age"), rsLiaisonTVA.getString("dev"), tx.getTaxes(), null, null, null, null, null, null, null, null, eve.getNat(), "VA", null, null) );  numEc++;
							}

						}

					}


				}

				// Ajout de l'element a comptabiliser
				data.add(eve);
				CloseStream.fermeturesStream(rsCpteAbonne); rsCpteAbonne = null;
				CloseStream.fermeturesStream(rsCpteLiaison); rsCpteLiaison = null;
				
			}
		}


		// On libere les variables
		CloseStream.fermeturesStream(rsCpteComs); rsCpteComs = null;
		CloseStream.fermeturesStream(rsCpteTVA); rsCpteTVA = null;
		CloseStream.fermeturesStream(rsLiaisonComs); rsLiaisonComs = null;
		CloseStream.fermeturesStream(rsLiaisonTVA); rsLiaisonTVA = null;

		//if(conCBS != null ) conCBS.close();

		// retourner le liste des evenements à comptabiliser
		return data;

	}



	private void checkGlobalConfig(){
		if(params == null) params = findParameters();
	}


	public int getTotalAbonComptabilises() throws Exception {

		// DataStore vers Amplitude
		if(dsCBS == null) findCBSDataSystem();
		checkGlobalConfig();

		// Recuperation de la date comptable
		Date dco = getDateComptable();
		int nb = 0;

		// Execution de la requete de controle de l'equilibre
		ResultSet rs = executeFilterSystemQuery(dsCBS, "select count(*) as nb from bkmvti where ope=? and dco=? and cha like '37%' and lib like 'FRAIS%'", new Object[]{ params.getCodeOperation(), dco });

		// Construction de la liste
		if(rs != null && rs.next()) nb = rs.getInt("nb");

		CloseStream.fermeturesStream(rs);
		//if(conCBS != null ) conCBS.close();

		return nb;
	}


	public int getTotalAbonComptabilises(String util, String typeOp) throws Exception {

		// DataStore vers Amplitude
		if(dsCBS == null) findCBSDataSystem();
		checkGlobalConfig();

		// Recuperation de la date comptable
		Date dco = getDateComptable();
		int nb = 0;

		// Execution de la requete de controle de l'equilibre
		ResultSet rs = executeFilterSystemQuery(dsCBS, "select count(*) as nb from bkmvti where ope=? and dco=? and cha like '37%' and lib like ? and uti = ? ", new Object[]{ params.getCodeOperation(), dco, "%"+typeOp+"%", util });

		// Construction de la liste
		if(rs != null && rs.next()) nb = rs.getInt("nb");

		CloseStream.fermeturesStream(rs);
		//if(conCBS != null ) conCBS.close();

		return nb;
	}

	public Double getTotalComsAbonComptabilises() throws Exception {

		// DataStore vers Amplitude
		if(dsCBS == null) findCBSDataSystem();
		checkGlobalConfig();

		// Recuperation de la date comptable
		Date dco = getDateComptable();
		Double nb = 0d;

		// Execution de la requete de controle de l'equilibre
		ResultSet rs = executeFilterSystemQuery(dsCBS, "select sum(mon) as mnt from bkmvti where ope=? and dco=? and ncp=? and lib like 'COM %' and eve='000000'", new Object[]{ params.getCodeOperation(), dco, params.getNumCompteCommissions().split("-")[1] });

		// Construction de la liste
		if(rs != null && rs.next()) nb = rs.getDouble("mnt");

		CloseStream.fermeturesStream(rs);
		//if(conCBS != null ) conCBS.close();

		return nb;
	}


	public Double getTotalComsAbonComptabilises(String util, String typeOp) throws Exception {

		// DataStore vers Amplitude
		if(dsCBS == null) findCBSDataSystem();
		checkGlobalConfig();

		// Recuperation de la date comptable
		Date dco = getDateComptable();
		Double nb = 0d;

		// Execution de la requete de controle de l'equilibre
		//ResultSet rs = executeFilterSystemQuery(dsCBS, "select sum(mon) as mnt from bkmvti where ope=? and dco=? and ncp=? and lib like ? and eve='000000' and uti = ? ", new Object[]{ params.getCodeOperation(), dco, params.getNumCompteCommissions().split("-")[1], typeOp+"%", util });
		ResultSet rs = executeFilterSystemQuery(dsCBS, "select sum(mon) as mnt from bkmvti where ope=? and dco=? and ncp=? and lib like ? and uti = ? ", new Object[]{ params.getCodeOperation(), dco, params.getNumCompteCommissions().split("-")[1], typeOp+"%", util });

		// Construction de la liste
		if(rs != null && rs.next()) nb = rs.getDouble("mnt");

		CloseStream.fermeturesStream(rs);
		//if(conCBS != null ) conCBS.close();

		return nb;
	}


	public Double getTotalTaxAbonComptabilises() throws Exception {

		// DataStore vers Amplitude
		if(dsCBS == null) findCBSDataSystem();
		checkGlobalConfig();

		// Recuperation de la date comptable
		Date dco = getDateComptable();
		Double nb = 0d;

		// Execution de la requete de controle de l'equilibre
		ResultSet rs = executeFilterSystemQuery(dsCBS, "select sum(mon) as mnt from bkmvti where ope=? and dco=? and ncp=? and lib like 'TAX %' and eve='000000'", new Object[]{ params.getCodeOperation(), dco, params.getNumCompteTVA().split("-")[1] });

		// Construction de la liste
		if(rs != null && rs.next()) nb = rs.getDouble("mnt");

		CloseStream.fermeturesStream(rs);
		//if(conCBS != null ) conCBS.close();

		return nb;
	}


	public Double getTotalTaxAbonComptabilises(String util, String typeOp) throws Exception {

		// DataStore vers Amplitude
		if(dsCBS == null) findCBSDataSystem();
		checkGlobalConfig();

		// Recuperation de la date comptable
		Date dco = getDateComptable();
		Double nb = 0d;

		// Execution de la requete de controle de l'equilibre
		//ResultSet rs = executeFilterSystemQuery(dsCBS, "select sum(mon) as mnt from bkmvti where ope=? and dco=? and ncp=? and lib like ? and eve='000000' and uti = ? ", new Object[]{ params.getCodeOperation(), dco, params.getNumCompteTVA().split("-")[1], typeOp+"%", util });
		ResultSet rs = executeFilterSystemQuery(dsCBS, "select sum(mon) as mnt from bkmvti where ope=? and dco=? and ncp=? and lib like ? and uti = ? ", new Object[]{ params.getCodeOperation(), dco, params.getNumCompteTVA().split("-")[1], typeOp+"%", util });

		// Construction de la liste
		if(rs != null && rs.next()) nb = rs.getDouble("mnt");

		CloseStream.fermeturesStream(rs);
		//if(conCBS != null ) conCBS.close();

		return nb;
	}


	public List<Equilibre> getRapportEquilibre() throws Exception {

		// DataStore vers Amplitude
		if(dsCBS == null) findCBSDataSystem();
		checkGlobalConfig();

		// Recuperation de la date comptable
		Date dco = getDateComptable();

		// Initialisation de la liste des valeurs a retourner
		List<Equilibre> data = new ArrayList<Equilibre>();

		// Execution de la requete de controle de l'equilibre
		ResultSet rs = executeFilterSystemQuery(dsCBS, OgMoHelper.getQueryControlEquilibre(), new Object[]{ params.getCodeOperation(), dco });

		// Construction de la liste
		while(rs != null && rs.next()) data.add( new Equilibre(rs.getDate("dco"), rs.getString("uti"), rs.getString("ope"), rs.getString("sen"), rs.getInt("nbre"), rs.getDouble("total")) );

		// fermeture des cnx
		CloseStream.fermeturesStream(rs);

		//if(conCBS != null ) conCBS.close();

		// Retourne le rapport
		return data;

	}


	public List<Equilibre> getRapportEquilibre(String util, String typeOp) throws Exception {

		String op1;
		String op2;

		if(typeOp.equals("FACT_MAC")){
			op1 = TypeOperation.Wallet2Account.toString();
			op2 = TypeOperation.Account2Wallet.toString();
		} else{
			op1 = typeOp;
			op2 = typeOp;
		}

		// DataStore vers Amplitude
		if(dsCBS == null) findCBSDataSystem();
		checkGlobalConfig();

		// Recuperation de la date comptable
		Date dco = getDateComptable();

		// Initialisation de la liste des valeurs a retourner
		List<Equilibre> data = new ArrayList<Equilibre>();

		// Execution de la requete de controle de l'equilibre
		ResultSet rs = executeFilterSystemQuery(dsCBS, OgMoHelper.getQueryControlEquilibre(), new Object[]{ params.getCodeOperation(), dco, util, "%"+op1+"%", "%"+op2+"%" });

		// Construction de la liste
		while(rs != null && rs.next()) data.add( new Equilibre(rs.getDate("dco"), rs.getString("uti"), rs.getString("ope"), rs.getString("sen"), rs.getInt("nbre"), rs.getDouble("total")) );

		// fermeture des cnx
		CloseStream.fermeturesStream(rs);

		//if(conCBS != null ) conCBS.close();

		// Retourne le rapport
		return data;

	}




	public List<Doublon> getRapportDoublon() throws Exception {

		// DataStore vers Amplitude
		if(dsCBS == null) findCBSDataSystem();
		checkGlobalConfig();

		// Recuperation de la date comptable
		Date dco = getDateComptable();

		// Initialisation de la liste des valeurs a retourner
		List<Doublon> data = new ArrayList<Doublon>();

		// Execution de la requete de controle de l'equilibre
		ResultSet rs = executeFilterSystemQuery(dsCBS, OgMoHelper.getQueryControlDoublon(), new Object[]{ params.getCodeOperation(), dco });

		// Construction de la liste
		while(rs != null && rs.next()) data.add( new Doublon(rs.getString("age"), rs.getString("dev"), rs.getString("ncp"), rs.getString("ope"), rs.getString("eve"), rs.getString("pie"), rs.getString("lib"), rs.getDouble("mon"), rs.getString("sen"), rs.getInt("nbre")) );

		// fermeture des cnx
		CloseStream.fermeturesStream(rs);
		//if(conCBS != null ) conCBS.close();

		// Retourne le rapport
		return data;

	}


	public List<Doublon> getRapportDoublon(String util, String typeOp) throws Exception {

		String op1;
		String op2;

		if(typeOp.equals("PULL_PUSH")){
			op1 = TypeOperation.Wallet2Account.toString();
			op2 = TypeOperation.Account2Wallet.toString();
		} else{
			op1 = typeOp;
			op2 = typeOp;
		}

		// DataStore vers Amplitude
		if(dsCBS == null) findCBSDataSystem();
		checkGlobalConfig();

		// Recuperation de la date comptable
		Date dco = getDateComptable();

		// Initialisation de la liste des valeurs a retourner
		List<Doublon> data = new ArrayList<Doublon>();

		// Execution de la requete de controle de l'equilibre
		ResultSet rs = executeFilterSystemQuery(dsCBS, OgMoHelper.getQueryControlDoublon(), new Object[]{ params.getCodeOperation(), dco, util, "%"+op1+"%", "%"+op2+"%" });

		// Construction de la liste
		while(rs != null && rs.next()) data.add( new Doublon(rs.getString("age"), rs.getString("dev"), rs.getString("ncp"), rs.getString("ope"), rs.getString("eve"), rs.getString("pie"), rs.getString("lib"), rs.getDouble("mon"), rs.getString("sen"), rs.getInt("nbre")) );

		// fermeture des cnx
		CloseStream.fermeturesStream(rs);

		//if(conCBS != null ) conCBS.close();

		// Retourne le rapport
		return data;

	}



	public Integer checkTraitement(String uti, String ope){

		Integer resultat = 0;

		try{

			// DataStore vers Amplitude
			if(dsCBS == null) findCBSDataSystem();

			String Sql = " select count(*) as nombre from bkmvti where uti='"+uti+"' and ope='"+ope+"' group by sen,uti ";

			// Execution de la requete de controle de l'equilibre
			ResultSet rs = executeFilterSystemQuery(dsCBS, Sql, new Object[]{uti, ope });

			while(rs.next()){
				resultat = rs.getInt("nombre");				
			}

			CloseStream.fermeturesStream(rs);
			//if(conCBS != null ) conCBS.close();

		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
		return resultat;
	}




	@AllowedRole(name = "executeRapprochement", displayName = "OgMo.rapprocher.Transactions")
	public Rapprochement executeRapprochement(TypeOperation typeOpe, String user, String ncp, Date debut, Date fin) throws Exception {

		try {

			Rapprochement rappro = new Rapprochement();

			List<Transaction> transacs = new ArrayList<Transaction>();

			// Lecture des parametres generaux
			Parameters params = findParameters();


			// Initialisation de DataStore d'Amplitude
			if(dsCBS == null) findCBSDataSystem();

			String REQ_HISTO_DAP = "select bkhis.age,bkhis.dco,bkhis.dva,bkcom.cha,bkcom.dev,bkhis.ncp,bkcom.clc,bkhis.rlet,bkhis.eve, "
					+ " bkhis.lib,bkhis.ope,bkhis.sen,bkhis.mon,bkhis.pie,bkhis.uti,bkhis.dev,bkcom.inti,bkcom.sde "
					+ " from bkhis,bkcom "
					+ " where bkhis.ncp = ? and  bkhis.ope = ? and (bkhis.lib like '%A2W%' or bkhis.lib like '%W2A%') "
					+ " and bkhis.ncp=bkcom.ncp and bkhis.age=bkcom.age and bkhis.suf=bkcom.suf and bkhis.dev=bkcom.dev "
					+ " and bkhis.dev = '001' and bkhis.dco between ? and ? order by dco,sen";

			//Liste des transactions OM J-1 historisés dans le CBS
			ResultSet rs = executeFilterSystemQuery(dsCBS, REQ_HISTO_DAP, new Object[]{ncp, params.getCodeOpe(), debut, fin});

			// Parcours du resultat
			while(rs != null && rs.next()){

				// Ajout de l'element trouve a la collection
				Transaction trans = new Transaction();

				trans.setDate(rs.getDate("dco"));
				trans.setDatetime(rs.getDate("dco"));
				trans.setSens(rs.getString("sen").trim());
				trans.setAccount(rs.getString("ncp").trim());
				trans.setLibelle(rs.getString("lib").trim());

				if("C".equals(rs.getString("sen"))) {					
					trans.setMontCredit(rs.getDouble("mon"));
					trans.setMontDebit(0d);
				}					
				else {
					trans.setMontCredit(0d);
					trans.setMontDebit(rs.getDouble("mon"));
				}

				Double mont = rs.getDouble("mon");
				String tel = rs.getString("lib").trim().split("/")[0];
				if("MRCH".equals(tel)) tel = rs.getString("lib").trim().split("/")[1];

				trans.setReferenceLettrage(rs.getString("dco") + tel + Integer.toString(mont.intValue()));
				transacs.add(trans);
			}

			// Fermeture des connexions
			CloseStream.fermeturesStream(rs);

			if(!transacs.isEmpty() && transacs.size() > 0){

				//*** List<Transaction> transParcours = transacs;
				List<Transaction> transParcours = new ArrayList<Transaction>();
				List<Transaction> transReste = transacs;
				Integer totalTrans = transacs.size();

				// liste temporaire des operations lettrees trouvee
				List<Transaction> tmp = new ArrayList<Transaction>();
				List<Transaction> transactions = new ArrayList<Transaction>();

				double solde = 0d;
				int cpt = 0;
				while(cpt < transacs.size()){

					// On ajoute l'operation courante a la liste temporaire
					//*** tmp.add(t);

					transParcours = new ArrayList<Transaction>();
					transParcours = transReste;

					for(int i=transParcours.size()-1; i>=0; i--){
						if(transParcours.get(i).getReferenceLettrage().equals(transacs.get(cpt).getReferenceLettrage())) {
							tmp.add(transParcours.get(i));
						}
					}


					// Si plus d'une operation ont ete lettrees
					if(tmp.size() > 1) {
						//System.out.println("************** tmp.size() > 1 ************");

						// Calcul du solde des operations lettrees
						double sde = 0;
						for(Transaction op : tmp) {
							sde = sde  + (op.getMontCredit() - op.getMontDebit());
						}

						// Si le solde est null on supprime toutes les operations lettrees de la liste
						if(sde == 0) {
							transReste.removeAll(tmp);
							transactions.addAll(tmp);
						}

						solde = solde + sde;

					}

					// vidage de la liste temporaire
					tmp.clear();

					cpt++;
				}


				String suspens = "";
				for(Transaction t : transParcours) {
					suspens = suspens + t.getLibelle() + "-" + t.getSens() + "-" + ("C".equals(t.getSens()) ? Integer.toString(t.getMontCredit().intValue()) : Integer.toString(t.getMontDebit().intValue())) + ";";
					solde = solde  + (t.getMontCredit() - t.getMontDebit());
				}

				rappro.setTypeOperation(typeOpe);
				rappro.setLibelle("COMPENSATION_DAP_" + (TypeOperation.Wallet2Account.equals(typeOpe) ? "W2B_" : "B2W_") + ncp + "_" + new SimpleDateFormat("ddMMyyyy").format(new Date()));
				rappro.setDatetimeRappro(new Date());
				rappro.setDateRappro(new Date());
				rappro.setNcpRappro(ncp);
				rappro.setTypeRapprochement(TypeRapprochement.COMPENSATION);
				rappro.setUserRapprochement(user);
				rappro.setTotalTransactions(totalTrans);
				rappro.setTotalSuspens(transParcours.size());
				rappro.setSolde(solde);
				rappro.setSuspens(suspens);
				rappro.setTransactions(transactions);

			}

			//if(conCBS != null ) conCBS.close();

			return rappro;


		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}



	public Rapprochement executeImputation(TypeOperation typeOpe, String sens, String user, String comptes, Date debut, Date fin) throws Exception {

		try {

			Rapprochement rappro = new Rapprochement();

			List<Transaction> transacs = new ArrayList<Transaction>();
			List<Transaction> transOrange = new ArrayList<Transaction>();
			//*** List<Transaction> transParcours = transacs;
			List<Transaction> transParcours = new ArrayList<Transaction>();

			// liste temporaire des operations lettrees trouvee
			List<Transaction> susHist = new ArrayList<Transaction>();
			List<Transaction> transactions = new ArrayList<Transaction>();

			// Lecture des parametres generaux
			Parameters params = findParameters();

			//liste des suspens Orange non Regularises
			List<SuspensOrange> susOrangeNonReg = dao.filter(SuspensOrange.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("statut","NONREG")), null, null, 0, 5000);


			// Initialisation de DataStore d'Amplitude
			if(dsCBS == null) findCBSDataSystem();

			String REQ_HISTO_OGMO = "select bkhis.age,bkhis.dco,bkhis.dva,bkcom.cha,bkcom.dev,bkhis.ncp,bkcom.clc,bkhis.rlet,bkhis.eve, "
					+ " bkhis.lib,bkhis.ope,bkhis.sen,bkhis.mon,bkhis.pie,bkhis.uti,bkhis.dev,bkcom.inti,bkcom.sde "
					+ " from bkhis,bkcom "
					+ " where  bkhis.ope = ? and bkhis.sen = ? " + (comptes.isEmpty() ? "" : " and bkhis.ncp in " + comptes) + " and bkhis.ncp=bkcom.ncp "
					+ " and bkhis.age=bkcom.age and bkhis.suf=bkcom.suf and bkhis.dev=bkcom.dev "
					+ " and bkhis.dev = '001' and bkhis.dco between ? and ? order by dco,sen";

			//Liste des transactions OM J-1 historisés dans le CBS
			ResultSet rs = executeFilterSystemQuery(dsCBS, REQ_HISTO_OGMO, new Object[]{params.getCodeOpe(), sens, comptes, debut, fin});

			// Parcours du resultat
			while(rs != null && rs.next()){

				// Ajout de l'element trouve a la collection
				Transaction trans = new Transaction();

				trans.setDate(rs.getDate("dco"));
				trans.setDatetime(rs.getDate("dco"));
				trans.setSens(rs.getString("sen").trim());
				trans.setAccount(rs.getString("ncp").trim());
				trans.setLibelle(rs.getString("lib").trim());

				if("C".equals(sens)) {					
					trans.setMontCredit(rs.getDouble("mon"));
				}					
				else {
					trans.setMontDebit(rs.getDouble("mon"));
				}

				Double mont = rs.getDouble("mon");
				String tel = rs.getString("lib").trim().split("/")[0];
				if("MRCH".equals(tel)) tel = rs.getString("lib").trim().split("/")[1];

				trans.setReferenceLettrage(rs.getString("dco") + tel + sens + Integer.toString(mont.intValue()));
				transacs.add(trans);
			}

			// Fermeture des connexions
			CloseStream.fermeturesStream(rs);


			Integer totalTrans = transacs.size();
			Integer totalTransOrange = transOrange.size();
			if(!transacs.isEmpty() && transacs.size() > 0 && !transOrange.isEmpty() && transOrange.size() > 0 ){

				double solde = 0d;
				int cpt = 0;
				while(cpt < transacs.size()){

					// On ajoute l'operation courante a la liste temporaire
					//*** tmp.add(t);

					transParcours = new ArrayList<Transaction>();
					transParcours = transOrange;

					boolean trouve = Boolean.FALSE;
					for(int i=transParcours.size()-1; i>=0; i--){
						if(transParcours.get(i).getReferenceLettrage().equals(transacs.get(cpt).getReferenceLettrage())) {

							transactions.add(transacs.get(cpt));
							transactions.add(transParcours.get(i));
							transOrange.remove(transParcours.get(i));

							trouve = Boolean.TRUE;

							break;
						}
					}

					if(Boolean.FALSE.equals(trouve)){
						//Cas des Suspens

						List<SuspensOrange> susParcours = new ArrayList<SuspensOrange>();
						susParcours = susOrangeNonReg;

						boolean trouveSus = Boolean.FALSE;
						for(int i=susParcours.size()-1; i>=0; i--){
							if(susParcours.get(i).getReferenceLettrage().equals(transacs.get(cpt).getReferenceLettrage())) {

								transactions.add(transacs.get(cpt));
								transactions.add(new Transaction(susParcours.get(i)));

								susOrangeNonReg.remove(susParcours.get(i));

								//MAJ du Suspens
								susParcours.get(i).setStatut("REG");
								susParcours.get(i).setDateRegularisation(new Date());
								susParcours.get(i).setModeRegularisation("AUTO");
								dao.update(susParcours.get(i));

								trouveSus = Boolean.TRUE;

								break;
							}
						}

						if(Boolean.FALSE.equals(trouveSus)){
							susHist.add(transacs.get(cpt));
						}						

					}

					cpt++;
				}


				//Suspens CBS
				String suspens = "";
				for(Transaction t : susHist) {
					suspens = suspens + t.getLibelle() + "-" + t.getSens() + "-" + ("C".equals(t.getSens()) ? Integer.toString(t.getMontCredit().intValue()) : Integer.toString(t.getMontDebit().intValue())) + ";";
					solde = solde  + ("C".equals(sens) ? t.getMontCredit() : t.getMontDebit());
				}

				//Suspens ORANGE
				String suspensOrange = "";
				for(Transaction t : transOrange) {
					suspensOrange = suspensOrange + t.getLibelle() + "-" + t.getSens() + "-" + ("C".equals(t.getSens()) ? Integer.toString(t.getMontCredit().intValue()) : Integer.toString(t.getMontDebit().intValue())) + ";";
					SuspensOrange sus = new SuspensOrange(t, "NONREG", null, null);
					dao.save(sus);
				}

				rappro.setTypeOperation(typeOpe);
				rappro.setLibelle("IMPUTATION_TRANS_" + (TypeOperation.Wallet2Account.equals(typeOpe) ? "W2B_" : "B2W_") + "_" + new SimpleDateFormat("ddMMyyyy").format(new Date()));
				rappro.setDatetimeRappro(new Date());
				rappro.setDateRappro(new Date());
				rappro.setTypeRapprochement(TypeRapprochement.IMPUTATION);
				rappro.setUserRapprochement(user);
				rappro.setTotalTransactions(totalTrans);
				rappro.setTotalTransOrange(totalTransOrange);
				rappro.setTotalSuspens(susHist.size());
				rappro.setTotalSuspensOrange(transOrange.size());
				rappro.setSolde(solde);
				rappro.setSuspens(suspens);
				rappro.setSuspensOrange(suspensOrange);
				rappro.setTransactions(transactions);

			}

			//if(conCBS != null ) conCBS.close();

			return rappro;


		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}




	@SuppressWarnings("unchecked")
	public Date getLastRapprochement(TypeOperation typeOpe, TypeRapprochement typeRapprochement, String ncpRappro) throws Exception {

		Date result = null;

		List<Rapprochement> rapps = new ArrayList<Rapprochement>();
		if(TypeRapprochement.COMPENSATION.equals(typeRapprochement)){
			rapps = (List<Rapprochement>) dao.getEntityManager().createQuery("From Rapprochement r where r.dateRappro = (select max(r2.dateRappro) from Rapprochement r2 where r2.typeOperation = :typeOperation and r2.typeRapprochement = :typeRapprochement and r2.ncpRappro = :ncpRappro) ").setParameter("typeOperation", typeOpe).setParameter("typeRapprochement", typeRapprochement).setParameter("ncpRappro", ncpRappro).getResultList();
			if(rapps.size() > 0) {
				result = rapps.get(0).getDateRappro();
			}
		}
		else {
			rapps = (List<Rapprochement>) dao.getEntityManager().createQuery("From Rapprochement r where r.dateRappro = (select max(r2.dateRappro) from Rapprochement r2 where r2.typeOperation = :typeOperation and r2.typeRapprochement = :typeRapprochement) ").setParameter("typeOperation", typeOpe).setParameter("typeRapprochement", typeRapprochement).getResultList();
			if(rapps.size() > 0) {
				result = rapps.get(0).getDateRappro();
			}
		}
		rapps.clear();

		return result;

	}



	public List<Rapprochement> filterRapprochements(RestrictionsContainer rc, OrderContainer orders) {
		// TODO Auto-generated method stub
		return dao.filter(Rapprochement.class, null, rc, orders, null, 0, -1);
	}



	@SuppressWarnings("unchecked")
	public Date getLastTransaction() throws Exception {

		Date result = new Date();

		List<Transaction> trans = (List<Transaction>) dao.getEntityManager().createQuery("From Transaction t where t.datetime = (select max(t2.datetime) from Transaction t2 where t2.datetime >= :datejour ) ").setParameter("datejour", new Date()).getResultList();
		if(trans.size() > 0) {
			result = trans.get(0).getDate();
		}
		else {
			Calendar cal = new GregorianCalendar();
			cal.setTime(result);
			cal.set(Calendar.MINUTE, -20);
			result =  cal.getTime();
		}
		//*** System.err.println("************* getLastTransaction result ************ : " + result);

		return result;

	}



	@SuppressWarnings("unchecked")
	public void suiviReservation(String fileName, String heureDebut, String heureFin, boolean afterTFJO) throws Exception{

		List<bkeve> bkeves = new ArrayList<bkeve>();
		List<bkeve> bkeveRapps = new ArrayList<bkeve>();
		List<bkeve> bkeveSuspens = new ArrayList<bkeve>();
		List<bkeve> bkeveParcours = new ArrayList<bkeve>();
		List<bkeve> evePortals = new ArrayList<bkeve>();
		List<bkeve> evePortalRapps = new ArrayList<bkeve>();
		List<bkeve> evePortalSuspens = new ArrayList<bkeve>();
		List<Transaction> transactions = new ArrayList<Transaction>();

		RestrictionsContainer rc = RestrictionsContainer.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH':'mm':'ss");
		params = findParameters();

		Date dateOp = new Date();
		if(afterTFJO == Boolean.TRUE) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -1);
			dateOp = cal.getTime();
		}

		if(StringUtils.isBlank(heureFin)) heureFin = new SimpleDateFormat("mm").format(new Date());

		if(StringUtils.isNotBlank(heureDebut) || StringUtils.isNotBlank(heureFin)) {
			Date deb = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(dateOp) + " " + heureDebut + ":00:00");
			Date fin = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(new Date()) + " " + heureFin + ":59:59");
			rc.add(Restrictions.between("datetime", deb, fin));
		}

		// Ajout des restrictions au conteneur
		rc.add(Restrictions.or(Restrictions.eq("typeOperation",TypeOperation.Account2Wallet),Restrictions.eq("typeOperation",TypeOperation.Wallet2Account)));
		rc.add(Restrictions.eq("posted", Boolean.FALSE));
		rc.add(Restrictions.eq("completed", Boolean.TRUE));
		rc.add(Restrictions.eq("status", TransactionStatus.SUCCESS));
		OrderContainer orders = OrderContainer.getInstance().add(Order.desc("datetime"));
		transactions = filterTransactions(rc, orders);

		/*
		String req = "From Transaction where (typeOperation='"+TypeOperation.Account2Wallet+"' or typeOperation='"+TypeOperation.Wallet2Account+"') "
				+ "and status=:status and completed=true and posted=false ";

		if(StringUtils.isNotBlank(heureDebut) || StringUtils.isNotBlank(heureFin)) req = req +"and date_part('hour',date_op)>="+Double.valueOf(heureDebut)+" and date_part('hour',date_op)<="+Double.valueOf(heureFin)+" ";

		transactions = (List<Transaction>) dao.getEntityManager().createQuery(req).setParameter("status", TransactionStatus.SUCCESS).getResultList();
		 */

		if(transactions == null || transactions.isEmpty()) return;

		// Initialisation du filtre des transactions
		String in = "";

		// Recuperation  de l'id de chaque transaction selectionnee dans le filtre
		for( int i=0; i<transactions.size(); i++ ){
			in += transactions.get(i).getId() + ", "; 
		}

		// Construction du filtre sur les transactions
		if(!in.isEmpty()) in = "(" + in.substring(0, in.length()-2) + ")";

		// Si aucune transaction n'a ete selectionnee on sort
		if(in.isEmpty()) return ;

		// Recherche des code evenements et codes operations des transactions selectionnees
		System.out.println("*************** Recherche des code evenements et codes operations des transactions selectionnees ***************");
		evePortals = (List<bkeve>) dao.getEntityManager().createQuery("From bkeve e where e.transaction.id in "+ in +"").getResultList();

		// Si aucun evenement trouve on sort
		System.out.println("*************** Si aucun evenement trouve on sort ***************");
		if(evePortals == null || evePortals.isEmpty()) return;


		// Initialisation du filtre des evenements et du code operation
		String ope = params.getCodeOpe();

		String requeteSql = "select * from bkeve where eta='VA' and ope=? " + (StringUtils.isNotBlank(heureDebut) && StringUtils.isNotBlank(heureFin) ? " and hsai between ? and ? " : " ");

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();
		ResultSet rs = executeFilterSystemQuery(dsCBS, requeteSql, (StringUtils.isNotBlank(heureDebut) && StringUtils.isNotBlank(heureFin) ? new Object[]{ope,heureDebut,heureFin} : new Object[]{ope}) );
		while(rs.next()){
			bkeve evePortal = new bkeve();
			evePortal.setEve(rs.getString("eve"));
			evePortal.setDco(rs.getDate("dco"));
			evePortal.setMon1(rs.getDouble("mon1"));
			evePortal.setMon2(rs.getDouble("mon2"));
			evePortal.setNom1(rs.getString("nom1"));
			evePortal.setNom2(rs.getString("nom2"));
			evePortal.setAge1(rs.getString("age1"));
			evePortal.setAge2(rs.getString("age2"));
			evePortal.setNcp1(rs.getString("ncp1"));
			evePortal.setNcp2(rs.getString("ncp2"));
			evePortal.setClc1(rs.getString("clc1"));
			evePortal.setClc2(rs.getString("clc2"));
			evePortal.setSen1(rs.getString("sen1"));
			evePortal.setSen2(rs.getString("sen2"));
			evePortal.setEta(rs.getString("eta"));
			evePortal.setDsai(rs.getDate("dsai"));
			evePortal.setHsai(rs.getString("hsai"));

			bkeves.add(evePortal);
		}
		CloseStream.fermeturesStream(rs);
		if(bkeves == null || bkeves.isEmpty()) return;


		int cpt = 0;
		while(cpt < evePortals.size()){

			bkeveParcours = new ArrayList<bkeve>();
			bkeveParcours = bkeves;

			boolean trouve = Boolean.FALSE;
			for(int i=bkeveParcours.size()-1; i>=0; i--){
				if(bkeveParcours.get(i).getEve().equals(evePortals.get(cpt).getEve())) {

					evePortalRapps.add(evePortals.get(cpt));
					bkeveRapps.add(bkeveParcours.get(i));
					bkeves.remove(bkeveParcours.get(i));

					trouve = Boolean.TRUE;

					break;
				}
			}

			if(Boolean.FALSE.equals(trouve)){
				evePortalSuspens.add(evePortals.get(cpt));
			}

			cpt++;
		}

		bkeveSuspens.addAll(bkeves);

		exportRapprochmentBkeve(fileName, transactions, evePortals, bkeves, evePortalRapps, bkeveRapps, evePortalSuspens, bkeveSuspens);

		//if(conCBS != null ) conCBS.close();

	}



	// ***************************** GESTION DU MODE NUIT ***************************************

	//**** Méthode isTFJOPortalEnCours() ****
	// NUIT
	/**
	 * Determine si les TFJO sont en cours dans Portal
	 * @return true les TFJO sont en cours dans Portal, false sinon
	 * @throws Exception
	 */
	public boolean isTFJOPortalEnCours() throws Exception {

		boolean res = false;

		// Recuperation des parametres generaux du module
		// checkGlobalConfig(); //Parameters p = findParameters();
		params = findParameters();

		res = params.getTfjoEnCours();

		// Log
		//logger.info("TFJP Portal En cours : "+res);

		// Retourne le resultat 
		return res;
	}


	//**** Méthode setTFJOPortalEnCours(Boolean tfjo) ****
	// NUIT
	/**
	 * Mise a jour de la valeur du parametre tfjoPortalEnCours
	 * 
	 * @param tfjo valeur de l'etat du tfjo portal
	 * @throws Exception
	 */
	private void setTFJOPortalEnCours(Boolean tfjo) throws Exception {

		// Recuperation des parametres generaux du module
		// checkGlobalConfig(); 
		params = findParameters();

		// MAJ de l'etat des TFJO Portal
		if(params.getTfjoEnCours().booleanValue() != tfjo){
			//logger.info("Mise a jour de l'etat des TFJO Portal! TFJO = "+tfjo);
			params.setTfjoEnCours(tfjo); 
			params = dao.update(params);
		}

	}


	//**** Méthode processRetraiterTransactions() ****
	public void processRetraiterTransactions(){
		// checkGlobalConfig();
		params = findParameters();
		try{
			//logger.info("[MoMo] : IN ROBOT RETRAITEMENT");
			new Thread(new Runnable() {
				@Override
				public void run() {
					// code goes here.
					try {
						//logger.info("******************************** IN TASK RETRAITEMENT ********************************");


						if(!params.getTfjoEnCours()){
							try{
								//logger.info("**************************** RECHERCHE DES TRX A RETRAITER *****************************");
								List<Transaction> list = filterTransactionARetraiter();
								if(list.equals(null) || list.isEmpty()){
									logger.error("******************************** AUCUNE TRX ********************************");
									return;
								}
								logger.error("NBRE TRX : "+list.size());

								try{
									// Reposter les transactions dans le corebanking
									List<Transaction> trx = new ArrayList<Transaction>();
									trx = rePosterEvenementDansCoreBanking(list);

									if(!trx.isEmpty() && !trx.equals(null)){
										logger.error("**************************** Maj des transactions retraitees *****************************");
										for(Transaction tx : trx) tx.setaRetraiter(Boolean.FALSE);
										// Maj des transactions retraitees
										dao.updateTransaction(trx);
									}

								}catch(Exception e){
									// TODO: handle exception
									logger.error("Erreur lors du retraitement des transactions");
									e.printStackTrace();
								}

							}catch(Exception e){
								e.printStackTrace();
							}
						}
						else{
							logger.error("******************************** ROBOT RETRAITEMENT OFF ********************************");
						}


					}catch(Exception e){
						//e.printStackTrace();
					}
				}
			}).start();

			//cancelChecking();

		}catch(Exception e){
			e.printStackTrace();
		}
	}




	@Override
	public List<Transaction> filterTransactionARetraiter() {
		// TODO Auto-generated method stub

		RestrictionsContainer rc = RestrictionsContainer.getInstance();

		// verifier au lieu de completed ???
		rc.add(Restrictions.eq("aRetraiter",Boolean.TRUE));
		rc.add(Restrictions.eq("posted",Boolean.FALSE));

		return dao.filter(Transaction.class, null, rc, null, null, 0, -1);

	}





	//**** Méthode rePosterEvenementDansCoreBanking(List<Transaction> transactions) ****
	@Override
	public List<Transaction> rePosterEvenementDansCoreBanking(List<Transaction> transactions) throws Exception {

		List<Queries> query = new ArrayList();

		// TODO Auto-generated method stub
		// Initialisation de la liste des transactions a retourner
		List<Transaction> trx = new ArrayList<Transaction>();
		logger.error("[OGMO] : DEBUT RETRAITEMENT DES TRANSACTION INCRIMINEES");
		// Si la liste des transactions a retraiter est nulle, interrompre le traitement
		if(transactions.isEmpty()) return null;

		// Initialisation de DataStore d'Amplitude
		if(dsCBS == null) findCBSDataSystem();
		// checkGlobalConfig();
		params = findParameters();

		Map<Long,Transaction> mapTrans = new HashMap<Long,Transaction>();
		for(Transaction t : transactions) mapTrans.put(t.getId(),t);
		//logger.info("[MoMo] : Recuperation des evenements lies aux transaction");
		// s
		List<bkeve> eves = dao.filter(bkeve.class, null, RestrictionsContainer.getInstance().add(Restrictions.in("transaction",transactions)), null, LoaderModeContainer.getInstance().add("transaction", FetchMode.JOIN), 0, -1);
		//logger.info("[MoMo] : Parcours des evenements");
		// 
		for(bkeve eve : eves){

			if(eve != null) {

				//logger.info("[MoMo] : TRX = "+eve.getTransaction().toString());
				Transaction tx = mapTrans.get(eve.getTransaction().getId());

				boolean modeNuit = isModeNuit();
				boolean evePosted = false;
				String tableEvt = " BKEVE ";
				String tableOpe = "bkope";
				if(modeNuit){
					//tableEvt = "SYN_BKEVE";
					tableEvt = " bkeve_eod ";
					tableOpe = "syn_bkope";
				}

				//Recherche de la transaction dans bkeve
				ResultSet rsa = executeFilterSystemQuery(dsCBS, eve.getCheckQuery().replaceAll("BKEVE", "bkeve"), eve.getQueryCheckValues());
				if(rsa.next()){
					evePosted = true;
				}

				if(evePosted == false) {

					// Interrompre le traitement si le mode nuit est desactive
					//*** MAJ if(!isModeNuit()) return trx;

					try{
						//logger.info("[MoMo] : Mise a jour du dernier numero d'evenement");
						// ???
						query.add(new Queries(OgMoHelper.getDefaultCBSQueries().get(3).getQuery().replaceAll("bkope", tableOpe), new Object[]{ Long.valueOf(eve.getEve()), eve.getOpe() }));
						//*** MAJ executeUpdateSystemQuery(dsCBS, "update "+ (eve.getSuspendInTFJ().booleanValue() ? "syn_" : "") +"bkope set num=(select max(eve) from "+ tableEvt +" where ope='"+ params.getCodeOperation() +"') where ope='"+ params.getCodeOperation() +"' ", null);

						// Recuperation du dernier numero evenement du type operation (mode nuit)
						ResultSet rs = executeFilterSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(2).getQuery().replaceAll("bkope", tableOpe), new Object[]{ params.getCodeOperation() });

						// Calcul du numero d'evenement
						Long numEve = rs != null && rs.next() ? numEve = rs.getLong("num") + 1 : 1l;
						//logger.info("[MoMo] : Prochain num eve = "+numEve);

						// Fermeture de cnx
						CloseStream.fermeturesStream(rs);

						eve.setEve( OgMoHelper.padText(String.valueOf(numEve), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0") );
						//logger.info("[MoMo] : eve = "+eve.toString());
						for(bkmvti m : eve.getEcritures()) m.setEve(eve.getEve());

						// Enregistrement de l'evenement dans Amplitude
						query.add(new Queries(eve.getSaveQuery().replaceAll(" BKEVE ", tableEvt), eve.getQueryValues()));
						//***MAJ executeUpdateSystemQuery(dsCBS, eve.getSaveQuery().replaceAll("BKEVE", tableEvt), eve.getQueryValues());


						//logger.info("[MoMo] : Regularisation OK. On poursuit");

					}catch(java.sql.SQLException ex){
						logger.error("[OGMO ERROR] : "+ex.getMessage());
						logger.error("[OGMO ERROR] : Erreur lors de l'insertion dans la table "+tableEvt);
					}

					// MAJ du dernier numero d'evenement utilise pour le type operation
					query.add(new Queries(OgMoHelper.getDefaultCBSQueries().get(3).getQuery().replaceAll("bkope", "syn_bkope"), new Object[]{ Long.valueOf(eve.getEve()), eve.getOpe() }));
					//***MAJ executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(3).getQuery().replaceAll("bkope", "syn_bkope"), new Object[]{ Long.valueOf(eve.getEve()), eve.getOpe() });

					/* *** MAJ 
					if(isModeNuit()){

						// MAJ du solde indicatif du Compte Debiteur
						query.add(new Queries("insert into bksin (age, dev, ncp, suf, mon, orig, flag) values (?, ?, ?, ?, ?, ?, ?) ", new Object[]{ eve.getAge1(), eve.getDev1(), eve.getNcp1(), eve.getSuf1(), -eve.getMon1(), "WEB", "O" }));
						//***MAJ executeUpdateSystemQuery(dsCBS, "insert into bksin (age, dev, ncp, suf, mon, orig, flag) values (?, ?, ?, ?, ?, ?, ?) ", new Object[]{ eve.getAge1(), eve.getDev1(), eve.getNcp1(), eve.getSuf1(), -eve.getMon1(), "WEB", "O" });

						// MAJ du solde indicatif du compte Crediteur
						query.add(new Queries("insert into bksin (age, dev, ncp, suf, mon, orig, flag) values (?, ?, ?, ?, ?, ?, ?) ", new Object[]{ eve.getAge2(), eve.getDev2(), eve.getNcp2(), eve.getSuf2(), eve.getMon2(), "WEB", "O" }));
						//***MAJ executeUpdateSystemQuery(dsCBS, "insert into bksin (age, dev, ncp, suf, mon, orig, flag) values (?, ?, ?, ?, ?, ?, ?) ", new Object[]{ eve.getAge2(), eve.getDev2(), eve.getNcp2(), eve.getSuf2(), eve.getMon2(), "WEB", "O" }); //executeUpdateSystemQuery(dsCBS, "update bksin set mon = mon + ? where age= ? and ncp = ?", new Object[]{ eve.getMon2(), eve.getAge2(), eve.getNcp2() });

					} 
					// Si on est dans le mode Jour
					else {

					}
					 */


					// **************** MAJ des soldes *********************
					if(modeNuit == false){

						// Enregistrement de l'evenement dans Amplitude
						// executeUpdateSystemQuery(dsCBS, eve.getSaveQuery(), eve.getQueryValues());
						if(eve.getTransaction().getTypeOperation().equals(TypeOperation.Wallet2Account)){

							// MAJ du solde indicatif crediteur
							query.add(new Queries(OgMoHelper.getDefaultCBSQueries().get(5).getQuery(), new Object[]{eve.getTransaction().getAmount(), eve.getAge2(), eve.getNcp2(), eve.getClc2()  }));
							//***MAJ executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(5).getQuery(), new Object[]{transaction.getAmount(), eve.getAge2(), eve.getNcp2(), eve.getClc2()  });

							// MAJ du solde indicatif du compte debiteur
							query.add(new Queries(OgMoHelper.getDefaultCBSQueries().get(4).getQuery(), new Object[]{ eve.getTransaction().getAmount() , eve.getAge1(), eve.getNcp1(), eve.getClc1() }));
							//***MAJ executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(4).getQuery(), new Object[]{ transaction.getAmount() , eve.getAge1(), eve.getNcp1(), eve.getClc1() } );

						}else if(eve.getTransaction().getTypeOperation().equals(TypeOperation.Account2Wallet)){

							// MAJ du solde indicatif du compte debiteur
							query.add(new Queries(OgMoHelper.getDefaultCBSQueries().get(4).getQuery(), new Object[]{ eve.getMnt1() , eve.getAge1(), eve.getNcp1(), eve.getClc1() }));
							//***MAJ executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(4).getQuery(), new Object[]{ eve.getMnt1() , eve.getAge1(), eve.getNcp1(), eve.getClc1() } );

							// MAJ du solde indicatif crediteur
							query.add(new Queries(OgMoHelper.getDefaultCBSQueries().get(5).getQuery(), new Object[]{eve.getMnt1(), eve.getAge2(), eve.getNcp2(), eve.getClc2()  }));
							//***executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(5).getQuery(), new Object[]{eve.getMnt1(), eve.getAge2(), eve.getNcp2(), eve.getClc2()  });

						}else {

							// MAJ du solde indicatif du compte debiteur
							query.add(new Queries(OgMoHelper.getDefaultCBSQueries().get(4).getQuery(), new Object[]{ (eve.getTransaction().getTypeOperation().equals(TypeOperation.Wallet2Account) ? eve.getTransaction().getAmount() : eve.getMnt1()) , eve.getAge1(), eve.getNcp1(), eve.getClc1() }));
							//***executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(4).getQuery(), new Object[]{ (transaction.getTypeOperation().equals(TypeOperation.Wallet2Account) ? transaction.getAmount() : eve.getMnt1()) , eve.getAge1(), eve.getNcp1(), eve.getClc1() } );

							// MAJ du solde indicatif crediteur
							query.add(new Queries(OgMoHelper.getDefaultCBSQueries().get(5).getQuery(), new Object[]{eve.getTransaction().getAmount(), eve.getAge2(), eve.getNcp2(), eve.getClc2()  }));
							//***if(transaction.getTypeOperation().equals(TypeOperation.Wallet2Account) || transaction.getTypeOperation().equals(TypeOperation.Account2Wallet)) executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(5).getQuery(), new Object[]{transaction.getAmount(), eve.getAge2(), eve.getNcp2(), eve.getClc2()  });

						}

						// MAJ du dernier numero d'evenement utilise pour le type operation
						query.add(new Queries(OgMoHelper.getDefaultCBSQueries().get(3).getQuery().replaceAll("bkope", tableOpe), new Object[]{ Long.valueOf(eve.getEve()), eve.getOpe() }));
						//***MAJ executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(3).getQuery().replaceAll("bkope", tableOpe), new Object[]{ Long.valueOf(eve.getEve()), eve.getOpe() });

					}else{

						// MAJ du solde indicatif du Compte Debiteur  orig='WEB' , 'MON' ;  flag=''
						query.add(new Queries("insert into bksin (age, dev, ncp, suf, mon, orig, flag) values (?, ?, ?, ?, ?, ?, ?) ", new Object[]{ eve.getAge1(), eve.getDev1(), eve.getNcp1(), eve.getSuf1(), -eve.getMon1(), "WEB", "O" }));
						//***executeUpdateSystemQuery(dsCBS, "insert into bksin (age, dev, ncp, suf, mon, orig, flag) values (?, ?, ?, ?, ?, ?, ?) ", new Object[]{ eve.getAge1(), eve.getDev1(), eve.getNcp1(), eve.getSuf1(), -eve.getMon1(), "WEB", "O" });

						// MAJ du solde indicatif du compte Crediteur
						query.add(new Queries("insert into bksin (age, dev, ncp, suf, mon, orig, flag) values (?, ?, ?, ?, ?, ?, ?) ", new Object[]{ eve.getAge2(), eve.getDev2(), eve.getNcp2(), eve.getSuf2(), eve.getMon2(), "WEB", "O" }));
						//***executeUpdateSystemQuery(dsCBS, "insert into bksin (age, dev, ncp, suf, mon, orig, flag) values (?, ?, ?, ?, ?, ?, ?) ", new Object[]{ eve.getAge2(), eve.getDev2(), eve.getNcp2(), eve.getSuf2(), eve.getMon2(), "WEB", "O" });

						// MAJ du dernier numero d'evenement utilise pour le type operation
						query.add(new Queries(OgMoHelper.getDefaultCBSQueries().get(3).getQuery().replaceAll("bkope", tableOpe), new Object[]{ Long.valueOf(eve.getEve()), eve.getOpe() }));
						//***executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(3).getQuery().replaceAll("bkope", tableOpe), new Object[]{ Long.valueOf(eve.getEve()), eve.getOpe() });

						// MAJ du solde indicatif du compte debiteur
						//executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(9).getQuery(), new Object[]{ (transaction.getTypeOperation().equals(TypeOperation.Wallet2Account) ? transaction.getAmount() : eve.getMnt1()) , eve.getAge1(), eve.getNcp1()} );

						// MAJ du solde indicatif crediteur
						//if(transaction.getTypeOperation().equals(TypeOperation.Wallet2Account) || transaction.getTypeOperation().equals(TypeOperation.Account2Wallet)) executeUpdateSystemQuery(dsCBS, OgMoHelper.getDefaultCBSQueries().get(10).getQuery(), new Object[]{transaction.getAmount(), eve.getAge2(), eve.getNcp2()  });

					}

					executeUpdateSystemQuery(dsCBS, query);
				}


				// MAJ du statut de traitement de la transaction
				//				eve.getTransaction().setARetraiter(Boolean.FALSE);
				tx.setaRetraiter(Boolean.FALSE);

				// Sauvegarde l'evenement
				eve = dao.save(eve);

				// Ajout de la transaction dans la liste a retourner
				//				trx.add(eve.getTransaction());
				trx.add(tx);
			}

		}

		//if(conCBS != null ) conCBS.close();

		logger.error("[MoMo] : FIN RETRAITEMENT DES TRANSACTION INCRIMINEES");	
		return trx;
	}



	public String checkTypeClient(String matricule){
		String result = null;
		try
		{			
			// Initialisation de DataStore d'Amplitude
			if(dsCBS == null) findCBSDataSystem();

			String sql ="select tcli from bkcli where cli = '"+matricule+"' ";

			ResultSet rs = executeFilterSystemQuery(dsCBS, sql, new Object[]{});
			// Parcours du resultat
			while(rs != null && rs.next()){
				result = rs.getString("tcli").trim();
			}

			CloseStream.fermeturesStream(rs);

		}catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}


	@SuppressWarnings("unchecked")
	public List<User> getUsersDA(Branch agence, String matricule){

		List<User> users = new ArrayList<User>();
		List<String> usersAdd = new ArrayList<String>();
		List<ProcessUser> process = new ArrayList<ProcessUser>();

		if(agence == null) return null;

		if(StringUtils.isBlank(matricule)) {
			process = (List<ProcessUser>) dao.getEntityManager().createQuery("select processUsers From Process ue where ue.workflow.typeOperation.code=:codeTypeOpe and ue.etape =:etape ").setParameter("codeTypeOpe", "OUV").setParameter("etape", Etape.CONTROLEDA).getResultList();
		}
		else {
			TypeClient typeClient = "1".equals(checkTypeClient(matricule)) ? TypeClient.PARTICULIER : TypeClient.ENTREPRISE;
			process = (List<ProcessUser>) dao.getEntityManager().createQuery("select processUsers From Process ue where ue.workflow.typeOperation.code=:codeTypeOpe and ue.workflow.typeClient=:typeClient and ue.etape =:etape ").setParameter("codeTypeOpe", "OUV").setParameter("typeClient", typeClient).setParameter("etape", Etape.CONTROLEDA).getResultList();
		}

		if("00006".equals(agence.getCode()) || "00099".equals(agence.getCode())) {
			for(ProcessUser i : process){
				if(Boolean.FALSE.equals(i.getUser().getSuspended()) && !usersAdd.contains(i.getUser().getLogin())){
					users.add(i.getUser());	
					usersAdd.add(i.getUser().getLogin());
				}
			}
		}
		else {
			for(ProcessUser i : process){
				if(Boolean.FALSE.equals(i.getUser().getSuspended()) && agence.getId().equals(i.getBranch().getId()) && !usersAdd.contains(i.getUser().getLogin())){
					users.add(i.getUser());	
					usersAdd.add(i.getUser().getLogin());
				}
			}
		}


		usersAdd.clear();

		if(process.isEmpty() || process.size() == 0) return null;

		return users;
	}



	public synchronized int alerteCompteDormant(User user, String selectedAccount, String message) throws Exception{

		List<User> users = getUsersDA(user.getBranch(), selectedAccount.split("-")[1].substring(0, 7));

		String mailList = StringUtils.isNotBlank(user.getEmail()) ? user.getEmail() : "";
		if(users != null && !users.isEmpty()) {
			for(User u : users) {
				if(StringUtils.isNotBlank(u.getEmail())) mailList = StringUtils.isNotBlank(mailList) ? mailList + "," + u.getEmail() : u.getEmail();		
			}
		}

		// Lecture des parametres generaux
		Parameters params = findParameters();
		if(params== null) return 0;

		return orangeMoneyAPI.getAlertesDAO().sendSimpleMail(params.getHostApi(), params.getProtocoleApi(), params.getPortApi(), params.getUrlMail(), message.toUpperCase(), mailList, "MAC ORANGE : ALERTE COMPTE DORMANT", params.getEmail_sav());

	}



	public int alerteSMS(String message, String phone) throws Exception{
		try {
			// Lecture des parametres generaux
			Parameters params = findParameters();
			if(params== null) return 0;

			return orangeMoneyAPI.getAlertesDAO().sendSimpleSMS(params.getHostApi(), params.getProtocoleApi(), params.getPortApi(), params.getUrlSms(), message, phone);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}



	public String genererOTP(String phone) throws Exception{

		try {
			String otp = RandomStringUtils.randomNumeric(4);
			String message = "Cher(e) client(e), le code de confirmation de votre abonnement au produit MAC Orange est : " + otp;
			int response = alerteSMS(message.toUpperCase(), phone);

			if(response == 200) {
				return otp;
			}
			else {
				return "";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return "";

	}


	@Override
	public List<ClientProduit> resiliationsCBS()
			throws ClientProtocolException, IOException, JSONException, URISyntaxException, DAOAPIException {
		// TODO Auto-generated method stub
		// Lecture des parametres generaux
		Parameters params = findParameters();
		if(params== null) return null;
		try {
			return this.orangeMoneyAPI.getAlertesDAO().resiliations(params.getHostApi(), params.getProtocoleApi(), params.getPortApi(), params.getUrlPackage(), "OgMo-01");
		} catch (DAOAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}




	/**
	 * Methode d'enregistrement de l'evenement
	 */
	public bkeve registerEventToCoreBanking(bkeve eve) 
			throws JsonGenerationException, JsonMappingException, UnsupportedEncodingException, IOException,
			JSONException, DAOAPIException, URISyntaxException{
		params = findParameters();
		if(params== null) return null;
		String url = params.getUrlCbsApi();

		return orangeMoneyAPI.getSendEventToCbsDAO().sendEventToCoreBanking(url, eve);
	}


	/**
	 * Methode de retraitement des trx marquées apres tfjo module
	 */
	public boolean registerIgnoreEventsToCoreBanking(List<String> eve) 
			throws JsonGenerationException, JsonMappingException, UnsupportedEncodingException, IOException,
			JSONException, DAOAPIException, URISyntaxException{
		params = findParameters();
		if(params== null) return false;
		String url = params.getUrlCbsApi();

		return orangeMoneyAPI.getSendEventToCbsDAO().sendPostEOD(url, eve);
	}


	/**
	 * Methode d'annulation des evenements
	 */
	public bkeve reversalEventsCoreBanking(String eve) 
			throws JsonGenerationException, JsonMappingException, UnsupportedEncodingException, IOException,
			JSONException, DAOAPIException, URISyntaxException{
		params = findParameters();
		if(params== null) return null;
		String url = params.getUrlCbsApi();

		return orangeMoneyAPI.getSendEventToCbsDAO().reverseEventFromCoreBanking(url, eve);
	}

	/**
	 * Methode de verification de l'evenement dans le corebanking
	 * @param eve
	 * @return
	 */
	public bkeve checkEventInCoreBanking(String eve) 
			throws JsonGenerationException, JsonMappingException, UnsupportedEncodingException, IOException,
			JSONException, DAOAPIException, URISyntaxException{
		params = findParameters();
		if(params == null) return null;
		String url = params.getUrlCbsApi();

		return orangeMoneyAPI.getSendEventToCbsDAO().checkEventToCoreBanking(url, eve);
	}

	/**
	 * Methode de verification de solde dans le corebanking
	 * @return
	 */
	public Double getSoldeInCoreBanking(String ncp) 
			throws JsonGenerationException, JsonMappingException, UnsupportedEncodingException, IOException,
			JSONException, DAOAPIException, URISyntaxException{
		params = findParameters();
		if(params == null) return null;
		String url = params.getUrlCbsApi();

		return orangeMoneyAPI.getSendEventToCbsDAO().getSoldeToCoreBanking(url, ncp);
	}



	/**
	 * Methode de posting des ecritures comptables dans le Core Banking
	 */
	public boolean postEntriesToCoreBanking(Entries entry) 
			throws JsonGenerationException, JsonMappingException, UnsupportedEncodingException, IOException,
			JSONException, DAOAPIException, URISyntaxException{
		params = findParameters();
		if(params== null) return false;
		String url = params.getUrlCbsApi();

		try {
			return orangeMoneyAPI.getSendEventToCbsDAO().accountingentries(url, entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Methode de ecritures apres tfjo module
	 */
	public boolean sendAccountsEntriesToCoreBanking(List<bkmvti> bkmvti) 
			throws JsonGenerationException, JsonMappingException, UnsupportedEncodingException, IOException,
			JSONException, DAOAPIException, URISyntaxException{
		params = findParameters();
		if(params== null) return false;
		String url = params.getUrlCbsApi();

		return orangeMoneyAPI.getSendEventToCbsDAO().sendEntriesToCoreBanking(url, bkmvti);
	}
	
	


	//@SuppressWarnings("rawtypes")
	public void ignoirerEvenements(List<Transaction> transactions) throws JsonGenerationException, JsonMappingException, UnsupportedEncodingException, IOException,
	JSONException, DAOAPIException, URISyntaxException{

		/**
		 * Methode de ecritures apres tfjo module
		 */
		params = findParameters();
		if(params== null) return;
		String url = params.getUrlCbsApi();
		int i = 0;
		for(Transaction t : transactions) {
			i++;
			logger.info("********************* i : *********************  "+i);
			logger.info("********************* t.getId() : *********************  "+t.getId());
			Long txID = Long.valueOf(t.getId());
			List<bkeve> eves = dao.filter(bkeve.class, AliasesContainer.getInstance().add("transaction", "transaction"), RestrictionsContainer.getInstance().add(Restrictions.eq("transaction.id", txID)), null, null, 0, -1);

			// Recuperation de l'evenement genere par la transaction
			bkeve eve = eves.get(0);

			//envoi de l'evenement a l'API pour annulation
			logger.info("********************* IDEVE: *********************  "+eve.getId());
			
			
			bkeve eveChecked = orangeMoneyAPI.getSendEventToCbsDAO().checkEventToCoreBanking(url, eve.getId().toString());
			if(eveChecked != null || StringUtils.isNotBlank(eveChecked.getEve())) {
				bkeve eveb = orangeMoneyAPI.getSendEventToCbsDAO().sendPostReverseEvent(url, eve.getId().toString());
			}
			
		}

	}


}
