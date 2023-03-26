/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.forms;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.codehaus.jettison.json.JSONException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.afb.dpd.orangemoney.jpa.entities.FactMonth;
import com.afb.dpd.orangemoney.jpa.entities.Parameters;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jpa.enums.TransactionStatus;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jpa.exception.DAOAPIException;
import com.afb.dpd.orangemoney.jpa.tools.Doublon;
import com.afb.dpd.orangemoney.jpa.tools.Equilibre;
import com.afb.dpd.orangemoney.jpa.tools.OgMoHelper;
import com.afb.dpd.orangemoney.jpa.tools.bkmvti;
import com.afb.dpd.orangemoney.jsf.models.AbstractPortalForm;
import com.afb.dpd.orangemoney.jsf.models.InformationDialog;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.models.PortalInformationHelper;
import com.afb.dpd.orangemoney.jsf.models.ReportViewerDialog;
import com.afb.dpd.orangemoney.jsf.servlet.WebResourceManager;
import com.afb.dpd.orangemoney.jsf.tools.OMTools;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

import afb.dsi.dpd.portal.jpa.entities.User;
import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

/**
 * Formulaire de Consultation des transactions
 * @author Francis KONCHOU
 * @version 1.0
 * 
 * 
 * 
 * modified by:
 * @author yves_labo 
 * @since 31/07/2021
 * @version 1.9
 * 
 * 
 * 
 * 
 */
public class FrmTransactions extends AbstractPortalForm {

	/**
	 * Liste des transactions
	 */
	List<Transaction> transactions = new ArrayList<Transaction>();
	List<Transaction> transactionsGlobal = new ArrayList<Transaction>();
	List<Transaction> transactionsTFJO = new ArrayList<Transaction>();
	List<Transaction> transRemove = new ArrayList<Transaction>();

	List<Equilibre> equilibre = new ArrayList<Equilibre>();
	List<Doublon> doublon = new ArrayList<Doublon>();

	/**
	 * Numeroteur de lignes
	 */
	private int num = 0, numEC = 0;

	/**
	 * Periode de dates saisie
	 */
	private String txtDateDeb = PortalHelper.DEFAULT_DATE_FORMAT.format(new Date());
	private String txtDateFin = PortalHelper.DEFAULT_DATE_FORMAT.format(new Date());

	private String txtPhone;


	/**
	 * Operation selectionnee
	 */
	private TypeOperation txtSearchOp = null;

	private Boolean txtSearchPosted;

	/**
	 * Statut recherche
	 */
	private TransactionStatus txtSearchStatus = null;

	/**
	 * Items de types d'operations
	 */
	private List<SelectItem> opItems = new ArrayList<SelectItem>();

	private List<SelectItem> postedItems = new ArrayList<SelectItem>();


	/**
	 * Items des statuts de transactions
	 */
	private List<SelectItem> statutItems = new ArrayList<SelectItem>();

	/**
	 * Transaction selectionnee dans la liste
	 */
	private Transaction selectedTransaction;

	List<bkmvti> ecritures = new ArrayList<bkmvti>();

	/**
	 * Fichier d'export des EC
	 */
	private String exportFileName = "OMW-OgMo-" + new SimpleDateFormat("ddMMyyHHmmss").format(new Date()) + ".unl";

	private String exportFileNameRapp = "RAPP-OgMo-" + new SimpleDateFormat("ddMMyyHHmmss").format(new Date()) + ".xlsx";

	/**
	 * Marqueur determinant si on a lance la generation du fichier des ecritures comptables
	 */
	private boolean fileIsGenerated = false, doReconci = Boolean.TRUE, doSearch = Boolean.FALSE, postFile = Boolean.FALSE, checkedTrans = Boolean.TRUE;

	private Parameters param ;

	private Integer page, parcoursPage, successPage, i, j, cpte, nb, nombreTrans, debParcours, finParcours, lotTrans; 

	private String indicatifLotTrans = "";

	/**
	 * Default Constructor
	 */
	public FrmTransactions() {}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm(){
		// TODO Auto-generated method stub
		super.initForm();

		param = OMViewHelper.appManager.findParameters();

		if(Boolean.TRUE.equals(param.getDoReconciliation())){
			doReconci = Boolean.TRUE;
		}
		else{
			doReconci = Boolean.FALSE;
		}

		doSearch = Boolean.FALSE; postFile = Boolean.FALSE; checkedTrans = Boolean.TRUE;

		// Chargement des items de types d'operations
		opItems.add( new SelectItem(null, "(Toutes)") );
		for(TypeOperation to : TypeOperation.getValues()) opItems.add( new SelectItem(to, to.getValue()) );

		// Chargement des items de statut
		statutItems.add( new SelectItem(null, " ") );
		for(TransactionStatus to : TransactionStatus.getValues()) statutItems.add( new SelectItem(to, to.getValue()) );

		// Chargement des items de Postage
		postedItems.add( new SelectItem(null, " ") );
		postedItems.add( new SelectItem(Boolean.TRUE, "Postées") );
		postedItems.add( new SelectItem(Boolean.FALSE, "Non postées") );

		ecritures = new ArrayList<bkmvti>();
		transactions = new ArrayList<Transaction>();

		page = 0; parcoursPage = 0; successPage = 0; i = 0; j = 0; cpte = 0; nb = 0; debParcours=0; finParcours=0; lotTrans=0;

		nombreTrans = 0;
		parcoursPage = 0;
		indicatifLotTrans = "";

		transactions = new ArrayList<Transaction>();
		transactionsTFJO = new ArrayList<Transaction>();
		transactionsGlobal = new ArrayList<Transaction>();

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Traitement des transactions Orange Money";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose();
		transactions.clear(); transactionsGlobal.clear(); transactionsTFJO.clear(); opItems.clear(); statutItems.clear(); txtSearchOp = null; txtSearchStatus = null; txtPhone = null;
		selectedTransaction = null; fileIsGenerated = false; closeEC(); postedItems.clear();
		if(isExportedFileExist()) new File( OMTools.getDownloadDir() + File.separator + exportFileName).delete();
		equilibre.clear(); doublon.clear();  doReconci = Boolean.TRUE;  param = null; indicatifLotTrans = null;
		page = null; parcoursPage = null; successPage = null; i = null; j = null; cpte = null; nb = null;  debParcours=null; finParcours=null; lotTrans=null;

	}

	public void init() {		
		ecritures = new ArrayList<bkmvti>();
		transactions = new ArrayList<Transaction>();
		transactionsGlobal = new ArrayList<Transaction>();
		doReconci = Boolean.TRUE;
		doSearch = Boolean.TRUE; postFile = Boolean.FALSE;
		fileIsGenerated = false;  num = 1;
	}




	/**
	 * Methode de recherche des transactions sur les criteres saisis
	 */
	public void filterTransactions(){

		try {

			// Initialisation du compteur
			num = 1;

			param = OMViewHelper.appManager.findParameters();

			try{

				transactions = new ArrayList<Transaction>();  
				transactions = OMViewHelper.appManager.getTransactionTFJOs(-1);

				doSearch = Boolean.TRUE;
				postFile = Boolean.FALSE;

			}catch(Exception ex) {

				// Traitement de l'exception
				//PortalExceptionHelper.threatException(ex);
				ex.printStackTrace();

			}


		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}

	/**
	 * Etrait les ecritures comptables des transactions selectionnees dans un fichier
	 */
	public void extraireEcritures() {

		try {

			// Extraction des ecritures comptables des transactions selectionnees
			ecritures = new ArrayList<bkmvti>();
			ecritures = OMViewHelper.appManager.extractECFromTransactions(transactions);

			fileIsGenerated = true;	

			// Message d'information
			//PortalInformationHelper.showInformationDialog("Export effectue avec succes! Vous pouvez telecharger le fichier genere.", InformationDialog.DIALOG_SUCCESS);

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
			fileIsGenerated = false;
		}

	}

	/**
	 * Etrait les ecritures comptables des transactions selectionnees dans un fichier
	 */
	public void consulterEcritures(){

		try{

			// Extraction des ecritures comptables des transactions selectionnees
			ecritures = new ArrayList<bkmvti>();
			ecritures = OMViewHelper.appManager.getECFromTransactions(transactions, false);

			numEC = 1;

		}catch(Exception e){

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}



	/**
	 * Poste les ecritures comptables des transactions selectionnees dans Delta
	 */
	public void posterEcritures() {

		try {

			// Postage des ecritures comptables dans Delta
			OMViewHelper.appManager.posterTransactionsDansCoreBanking(transactions);

			// Message d'information
			PortalInformationHelper.showInformationDialog("Les écritures comptables des transactions selectionnées ont été postées avec succès dans le Core Banking!", InformationDialog.DIALOG_SUCCESS);

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}




	/**
	 * Poste les ecritures comptables des transactions selectionnees dans Delta
	 */
	public void executerCompensationOld(){

		try{

			param = OMViewHelper.appManager.findParameters();

			if(Boolean.TRUE.equals(param.getDoReconciliation()) && Boolean.TRUE.equals(doReconci)){
				// Message d'information
				PortalInformationHelper.showInformationDialog("Bien vouloir reconcilier les transactions afin de poursuivre avec le TFJO !", InformationDialog.DIALOG_WARNING);
				return;
			}


			Date dateDeb = null;
			Date dateFin = null;

			User user = OMViewHelper.getSessionUser();
			String mois = DateFormatUtils.format(new Date(), OMTools.monthpatern);
			// Postage des ecritures comptables dans Delta PortalHelper.DEFAULT_DATE_FORMAT.parse(txtDateDeb) PortalHelper.DEFAULT_DATE_FORMAT.parse(txtDateFin)
			FactMonth fac = OMViewHelper.appManager.executerCompensation(dateDeb,dateFin,user.getLogin(),mois);

			if(fac == null ){

				PortalInformationHelper.showInformationDialog("Le systéme n'a détecté aucune transaction à comptabiliser. Rassurez-vous d'avoir effectuer la reconciliation globale ! !", InformationDialog.DIALOG_INFORMATION);

			}else{

				// Afficher le rapport FactMonth fac
				printRecu(fac,mois);

				if(Boolean.TRUE.equals(param.getDoReconciliation())){
					doReconci = Boolean.TRUE;
				}
				else{
					doReconci = Boolean.FALSE;
				}

				// Message d'information
				PortalInformationHelper.showInformationDialog("Compensation des Opérations Orange Money de la période ["+txtDateDeb + ", " + txtDateFin +" ] effectuée avec succès!", InformationDialog.DIALOG_SUCCESS);

			}

		}catch(Exception e){
			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}



	public void getIndictifLot(){	
		try {

			lotTrans = StringUtils.isBlank(param.getLotTransactions())  ? 3000 : Integer.parseInt(param.getLotTransactions());
			transactionsGlobal = OMViewHelper.appManager.getTransactionTFJOs(-1);			
			nombreTrans = transactionsGlobal.size();
			int reste = nombreTrans % lotTrans;
			nb = nombreTrans / lotTrans;
			if(nombreTrans <= lotTrans) {
				page = 1;
			}			
			else {
				page = (reste > 0 ? 1 : 0) + nb;
			}

			indicatifLotTrans = "Nombre total de transactions : " + nombreTrans + ", Pour " + page + " TFJO(s)" ;

			checkedTrans = Boolean.FALSE;
			doSearch = Boolean.TRUE;
			postFile = Boolean.TRUE;

			debParcours = 0;
			finParcours = lotTrans;

		} catch (Exception e) {
		}
	}



	/**
	 * Methode de recherche des transactions sur les criteres saisis
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	public void checkEC(){		

		// Initialisation du compteur
		num = 1;

		param = OMViewHelper.appManager.findParameters();


		/*

		if(!StringUtils.isNotBlank(param.getIpAdressAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir l'adresse IP du serveur Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getPortServerAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir le port du serveur Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getUserLoginServerAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir le login de l'utilisateur du serveur Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getUserPasswordServerAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir le mot de passe de l'utilisateur du serveur Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}



		if(!StringUtils.isNotBlank(param.getFilePathAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir le repertoire de fichier Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getFileNameAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir le nom de fichier Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(param.getFileNameAmpli().trim().length() > 15){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Le nombre de cartactère du nom de fichier Amplitude est superieur à 15 !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getFileExtensionAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir l'extension de fichier Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getCheminSaveFile())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez définir le repertoire de sauvegarde des fichiers du TFJO !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		Pattern pattern = Pattern.compile("\\p{Punct}");
		Matcher matcher = pattern.matcher(param.getFileNameAmpli());
		boolean res = matcher.find();
		if(res == true){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Presence de caracteres speciaux dans le libelle du fichier a integrer !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		Matcher matcher2 = pattern.matcher(param.getFileExtensionAmpli());
		res = matcher2.find();
		if(res == true){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Presence de caracteres speciaux dans le libelle de l'extension !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		String userCbs = param.getUserLoginServerAmpli().trim();
		String pwd = param.getUserPasswordServerAmpli();
		boolean transfert;
		try {
			transfert = CSftp.checkDir(OMTools.getDownloadDir() + File.separator + exportFileName, userCbs,param.getIpAdressAmpli().trim(),param.getFilePathAmpli().trim(), "",pwd);
			if(transfert == false){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Bien vouloir verifier les parametres de depot du fichier dans le Core Banking !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
		} catch (Exception e1) {
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Bien vouloir verifier les parametres de depot du fichier dans le Core Banking !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}
		 */

		Date dateDeb = null;
		Date dateFin = null;

		User user = OMViewHelper.getSessionUser();
		String mois = DateFormatUtils.format(new Date(), OMTools.monthpatern);


		try{

			if(OMViewHelper.appManager.getTransactionTFJOStatusProcess().size() > 0) {
				PortalInformationHelper.showInformationDialog("Bien vouloir proceder à la comptabilisation, car nous avons des transactions en attente de comptabilisation !", InformationDialog.DIALOG_WARNING);
				checkedTrans = Boolean.FALSE;
				doSearch = Boolean.FALSE;
				postFile = Boolean.TRUE;
				return;
			}

			if(debParcours >= nombreTrans) {
				PortalInformationHelper.showInformationDialog("Nombre maximum de transactions à comptabiliser atteint pour cette serie !", InformationDialog.DIALOG_INFORMATION);
				return;
			}

			/*
			int lotParcours = nombreTrans - cpte >= lot ? lot : nombreTrans - cpte;

			System.out.println("*************** lot *************** : " + lot);
			System.out.println("*************** lotParcours *************** : " + lotParcours);
			 */

			transactions = new ArrayList<Transaction>();  
			transactions = nombreTrans >= lotTrans ? transactionsGlobal.subList(debParcours, finParcours) :  transactionsGlobal;
			
			List<Transaction> transactionSimu = new ArrayList<Transaction>();  
			HashMap<Object, Object> mapTrans = new HashMap<Object, Object>();

			if(transactions.isEmpty() || transactions.size() == 0){
				PortalInformationHelper.showInformationDialog("Le systéme n'a détecté aucune transaction à comptabiliser !", InformationDialog.DIALOG_INFORMATION);
				return;
			}

			String typeOp = "";
			String sens = "";

			transactionsTFJO = transactions;
			
			for(Transaction t : transactions) {

				if((TypeOperation.Account2Wallet.equals(t.getTypeOperation()) || TypeOperation.Wallet2Account.equals(t.getTypeOperation())) && t.isCompleted() == true && t.getPosted() == false) {
					if(TypeOperation.Account2Wallet.equals(t.getTypeOperation())) {typeOp = "A2W";	sens = "D";}
					if(TypeOperation.Wallet2Account.equals(t.getTypeOperation())) {typeOp = "W2A";	sens = "C";}

					String datop = new SimpleDateFormat("ddMMyyHHmm").format(t.getDate());
					String cle  = t.getPhoneNumber().trim() + datop + Integer.toString(t.getAmount().intValue()) + typeOp;
					transactionSimu.add(t);		
					mapTrans.put(cle, t);
				}
				else if((TypeOperation.BALANCE.equals(t.getTypeOperation()) || TypeOperation.COMPTABILISATION.equals(t.getTypeOperation()) || TypeOperation.MiniStatement.equals(t.getTypeOperation()) || TypeOperation.SUBSCRIPTION.equals(t.getTypeOperation())) && t.getPosted() == false) {
					transactionSimu.add(t);
				}
				/*
				else {
					System.out.println("********** transactions non consideree ********** : " + t.getId() + " - " + t.getTypeOperation() + " - " + t.getLibelle() + " - " + t.isCompleted() + " - " + t.getPosted());
				}
				 */
			}

			transactions = new ArrayList<Transaction>(); 
			transactions = transactionSimu;

			if(transactions.isEmpty() || transactions.size() == 0){
				PortalInformationHelper.showInformationDialog("Aucune transaction à comptabiliser !", InformationDialog.DIALOG_INFORMATION);
				return;
			}


			//Debut Code simulation

			/*
			int cpt = 1;
			int nbr = 0;
			while(nbr < transactions.size() && cpt <= 30) {
				System.out.println("*************** transactions.get(nbr).getAccount().trim().split(-)[1].substring(0, 3) *************** : " + transactions.get(nbr).getAccount().trim().split("-")[1].substring(0, 3));

				int carac = Integer.parseInt(transactions.get(nbr).getAccount().trim().split("-")[1].substring(0, 3));
				System.out.println("*************** carac *************** : " + carac);

				if((TypeOperation.Account2Wallet.equals(transactions.get(nbr).getTypeOperation()) || TypeOperation.Wallet2Account.equals(transactions.get(nbr).getTypeOperation())) && carac < 50 && !"05455361051".equals(transactions.get(nbr).getAccount().trim().split("-")[1]) ) {
					System.out.println("*************** transactions.get(nbr).getAccount().trim().split(-)[1] *************** : " + transactions.get(nbr).getAccount().trim().split("-")[1]);
					transactionSimu.add(transactions.get(nbr));
					cpt++;
				}

				nbr++;
			}

			transactions = new ArrayList<Transaction>();
			transactions = transactionSimu;

			System.out.println("*************** transactionSimu.size() *************** : " + transactionSimu.size());
			System.out.println("*************** transactions.size() *************** : " + transactions.size());
			 */
			//Fin Code simulation



			// Postage des ecritures comptables dans Delta PortalHelper.DEFAULT_DATE_FORMAT.parse(txtDateDeb) PortalHelper.DEFAULT_DATE_FORMAT.parse(txtDateFin)
			//ecritures = new ArrayList<bkmvti>();
			//ecritures = OMViewHelper.appManager.genererEcritureTFJOs(transactions);

			/*
			//Generation des ecritures comptables
			System.out.println("*************** Generation des ecritures comptables ***************");
			try {
				// Extraction des ecritures comptables des transactions selectionnees
				ecritures = new ArrayList<bkmvti>();
				ecritures = OMViewHelper.appManager.genererEcritureTFJOs(transactions);
				fileIsGenerated = true;	

			} catch(Exception e) {
				// Traitement de l'exception
				PortalExceptionHelper.threatException(e);
				fileIsGenerated = false;
			}
			 */



			//Generation des ecritures comptables
			System.out.println("*************** Generation des ecritures comptables ***************");
			ecritures = new ArrayList<bkmvti>();
			if(transactions.size() > 3000 ){
				System.out.println("*************** transactions.size() > 1000 transactions.size() *************** : " + transactions.size());
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
					ecritures.addAll(OMViewHelper.appManager.genererEcritureTFJOs(trans));
					nb = nb - 1;
					i = j;
				}

				trans = new ArrayList<Transaction>(transactions.subList(i,j+reste));
				ecritures.addAll(OMViewHelper.appManager.genererEcritureTFJOs(trans));

			}else{
				ecritures.addAll(OMViewHelper.appManager.genererEcritureTFJOs(transactions));				
			}


			if(ecritures.isEmpty() || ecritures.size() == 0){
				PortalInformationHelper.showInformationDialog("Le systéme n'a détecté aucune écriture à comptabiliser !", InformationDialog.DIALOG_INFORMATION);
				return;
			}
			
			//Construction des MAPs d'ecritures
			HashMap<Object, Object> mapOrange = new HashMap<Object, Object>();
			HashMap<Object, Object> mapCli = new HashMap<Object, Object>();
			List<bkmvti> ecrituresOrange = new ArrayList<bkmvti>();
			List<bkmvti> ecrituresCli = new ArrayList<bkmvti>();
			List<bkmvti> ecrituresOrangeRapp = new ArrayList<bkmvti>();
			List<bkmvti> ecrituresCliRapp = new ArrayList<bkmvti>();
			List<bkmvti> ecrituresOrangeUnijamb = new ArrayList<bkmvti>();
			List<bkmvti> ecrituresCliUnijamb = new ArrayList<bkmvti>();
			List<bkmvti> ecrituresRestantes = new ArrayList<bkmvti>();
			List<bkmvti> ecrituresRemove = new ArrayList<bkmvti>();
			List<String> listeCle = new ArrayList<String>();


			//Similuation de l'ajout des ecritures unijambistes
			/*
			bkmvti e1 = ecritures.get(1);
			bkmvti e2 = ecritures.get(2);

			System.out.println("*************** ecritures.size() sans ajout *************** : " + ecritures.size());
			ecritures.add( new bkmvti("00001", "001", e1.getCha(), "04942231051", e1.getSuf(), "", OgMoHelper.padText(String.valueOf(0001), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, "AUTO", "", e1.getClc(), new Date(), null, e1.getDva(), 18500d, "D", "673731110/2703191600/A2W", "N", "XXXXXX", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, e1.getAge(), e1.getDev(), 18500d, null, null, null, null, null, null, null, null, e1.getNat(), "VA", null, null) ); 
			ecritures.add( new bkmvti("00001", "001", e2.getCha(), "04996661003", e2.getSuf(), "", OgMoHelper.padText(String.valueOf(0002), OgMoHelper.LEFT, OgMoHelper.TAILLE_CODE_EVE, "0"), null, "AUTO", "", e2.getClc(), new Date(), null, e2.getDva(), 19500d, "C", "670535656/2703191030/A2W", "N", "XXXXXX", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, e2.getAge(), e2.getDev(), 19500d, null, null, null, null, null, null, null, null, e2.getNat(), "VA", null, null) ); 
			 */


			/*
			bkmvti e1 = ecritures.get(1); e1.setLib("673731110/2703191600/A2W"); e1.setNcp("04942231051"); e1.setAge("00001"); e1.setDev("001"); e1.setSen("D"); e1.setMon(18500d);
			bkmvti e2 = ecritures.get(2); e2.setLib("670535656/2703191030/A2W"); e2.setNcp("04996661003"); e2.setAge("00002"); e2.setDev("001"); e2.setSen("C"); e2.setMon(19500d);

			ecritures.add(e1);
			ecritures.add(e2);
			 */


			for(bkmvti e : ecritures) {

				String tel = e.getLib().trim().split("/")[0];
				String date = e.getLib().trim().split("/")[1];
				String typeOpe = e.getLib().trim().split("/")[2];
				if("MRCH".equals(tel)) {
					tel = e.getLib().trim().split("/")[1];
					date = e.getLib().trim().split("/")[2];
					typeOpe = e.getLib().trim().split("/")[3];
				}

				if(date.trim().length() >= 8) date = date.trim().substring(0, 8);

				String cle  = tel + date + Integer.toString(e.getMon().intValue()) + typeOpe;
				e.setCleLettrage(cle);
				e.setTypeOperation(typeOpe);

				if(e.getNcp().equals(param.getNcpCliOrange().trim().split("-")[1]) && (typeOpe.equals("A2W") || (typeOpe.equals("W2A")))) {
					if(!mapOrange.containsKey(cle+e.getSen())) {
						mapOrange.put(cle+e.getSen(), e);	
					}
					ecrituresOrange.add(e);

				}
				else if(e.getNcp().trim().substring(0, 1).equals("0") && !e.getNcp().equals(param.getNcpCliOrange().trim().split("-")[1]) && (typeOpe.equals("A2W") || (typeOpe.equals("W2A")))) {
					if(!mapCli.containsKey(cle+e.getSen())) {
						mapCli.put(cle+e.getSen(), e);	
					}
					ecrituresCli.add(e);
				}

				/*
				if(e.getNcp().equals("45920090100") && e.getAge().equals("00001")) {
					e.setNcp("04848251051"); e.setClc("20");
				}
				if(e.getNcp().equals("45920090100") && e.getAge().equals("00002")) {
					e.setNcp("45260390700"); e.setClc("88");
				}
				 */

			}

			//Rapprochement des ecritures
			//System.out.println("*************** Rapprochement des ecritures ***************");

			for(bkmvti e : ecrituresCli) {

				boolean result = mapOrange.containsKey(e.getCleLettrage() + (e.getTypeOperation().equals("A2W") ? "C" : "D" ));
				if(mapOrange.containsKey(e.getCleLettrage() + (e.getTypeOperation().equals("A2W") ? "C" : "D" ))) {

					ecrituresCliRapp.add(e);
					ecrituresOrangeRapp.add((bkmvti) mapOrange.get(e.getCleLettrage() + (e.getTypeOperation().equals("A2W") ? "C" : "D" )));

					mapOrange.remove(e.getCleLettrage() + (e.getTypeOperation().equals("A2W") ? "C" : "D" ));
					mapCli.remove(e.getCleLettrage()+e.getSen());

				}
			}

			/*
			bkmvti e1 = ecrituresCli.get(1);
			bkmvti e2 = ecrituresOrange.get(2);
			mapCli.put(e1.getCleLettrage()+e1.getSen(), e1);
			mapOrange.put(e2.getCleLettra2.getSen(), e2);
			 */

			// vidage des ecritures unijambistes
			Iterator itr = mapCli.entrySet().iterator();
			while(itr.hasNext()) {
				Map.Entry mapentry = (Map.Entry) itr.next();
				bkmvti e = (bkmvti) mapentry.getValue();

				ecrituresCliUnijamb.add(e);
				listeCle.add(e.getCleLettrage());
				
				//Suppression des transactions concernées par les ecritures unijambistes
				Transaction trans = (Transaction) mapTrans.get(e.getCleLettrage());
				if(trans != null){
					transRemove.add(trans);
					transactions.remove(trans);
					mapTrans.remove(e.getCleLettrage() + e.getSen());
				}

			}
			
			itr = mapOrange.entrySet().iterator();
			while(itr.hasNext()) {
				Map.Entry mapentry = (Map.Entry) itr.next();
				bkmvti e = (bkmvti) mapentry.getValue();

				ecrituresOrangeUnijamb.add(e);
				listeCle.add(e.getCleLettrage());
				
				//Suppression des transactions concernées par les ecritures unijambistes
				Transaction trans = (Transaction) mapTrans.get(e.getCleLettrage());
				if(trans != null){
					transRemove.add(trans);
					transactions.remove(trans);
					mapTrans.remove(e.getCleLettrage() + e.getSen());
				}
			}


			/*
			if(ecrituresCliUnijamb.size() > 0 ) ecritures.removeAll(ecrituresCliUnijamb);
			if(ecrituresOrangeUnijamb.size() > 0 ) ecritures.removeAll(ecrituresOrangeUnijamb);
			 */

			//Mise à jour de la liste des ecritures restantes
			if(ecritures.size() > 3000 ){
				System.out.println("*************** ecritures.size() > 1000 ecritures.size() *************** : " + ecritures.size());
				int i = 0;
				int cpt = 3000;
				int size = ecritures.size();
				int reste = size % cpt;
				System.out.println("*************** reste *************** : " + reste);
				int nb = size / cpt;
				int j = 0;
				List<bkmvti> ecrits = new ArrayList<bkmvti>();
				while(nb > 0){
					System.out.println("*************** cpt *************** : " + cpt);
					j = j + cpt;
					System.out.println("*************** i *************** : " + i);
					System.out.println("*************** j *************** : " + j);
					ecrits = new ArrayList<bkmvti>(ecritures.subList(i,j));
					System.out.println("*************** ecrits.size() *************** : " + ecrits.size());
					for(bkmvti e : ecrits) {
						if(!listeCle.contains(e.getCleLettrage())) {
							ecrituresRestantes.add(e);
						}
						else {
							System.out.println("*************** ecrituresRemove.add e.getCleLettrage() *************** : " + e.getCleLettrage());
							ecrituresRemove.add(e);
						}
					}
					nb = nb - 1;
					i = j;
				}

				ecrits = new ArrayList<bkmvti>(ecritures.subList(i,j+reste));
				System.out.println("*************** ecrits.size() reste *************** : " + ecrits.size());
				for(bkmvti e : ecrits) {
					if(!listeCle.contains(e.getCleLettrage())) {
						ecrituresRestantes.add(e);
					}
					else {
						ecrituresRemove.add(e);
					}
				}

			}else{
				for(bkmvti e : ecritures) {
					if(!listeCle.contains(e.getCleLettrage())) {
						ecrituresRestantes.add(e);
					}
					else {
						ecrituresRemove.add(e);
					}
				}				
			}
			

			if(ecritures.isEmpty() || ecritures.size() == 0){
				PortalInformationHelper.showInformationDialog("Le systéme n'a détecté aucune écriture à comptabiliser !", InformationDialog.DIALOG_INFORMATION);
				return;
			}

			//OMViewHelper.appManager.exportRapprochmentBkmvti(exportFileNameRapp, ecritures, ecrituresCli, ecrituresOrange, ecrituresCliRapp, ecrituresOrangeRapp, ecrituresCliUnijamb, ecrituresOrangeUnijamb, ecrituresRemove, transRemove);

			// Initialisation d'un document Excel
			SXSSFWorkbook wb = new SXSSFWorkbook();

			if(ecritures.size() > 0 ) exportECBkmvti(ecritures, wb, "ALL_ECRITURES");
			if(ecrituresCli.size() > 0 ) exportECBkmvti(ecrituresCli, wb, "ECRITURES_NCP_CLIENT");
			if(ecrituresOrange.size() > 0 ) exportECBkmvti(ecrituresOrange, wb, "ECRITURES_NCP_ORANGE");
			if(ecrituresCliRapp.size() > 0 ) exportECBkmvti(ecrituresCliRapp, wb, "ECRITURES_RAPPROCHEES_NCP_CLIENT");
			if(ecrituresOrangeRapp.size() > 0 ) exportECBkmvti(ecrituresOrangeRapp, wb, "ECRITURES_RAPPROCHEES_NCP_ORANGE");
			if(ecrituresCliUnijamb.size() > 0 ) exportECBkmvti(ecrituresCliUnijamb, wb, "ECRITURES_UNIJAMBISTES_NCP_CLIENT");
			if(ecrituresOrangeUnijamb.size() > 0 ) exportECBkmvti(ecrituresOrangeUnijamb, wb, "ECRITURES_UNIJAMBISTES_NCP_ORANGE");
			if(ecrituresRemove.size() > 0 ) exportECBkmvti(ecrituresRemove, wb, "ECRITURES_SUPPRIMEES");

			if(transRemove.size() > 0 ) exportTransactions(transRemove, wb, "TRANSACTIONS_SUPPRIMEES");

			exportFileNameRapp = "RAPP-OgMo-" + new SimpleDateFormat("ddMMyyHHmmss").format(new Date()) +"_"+(parcoursPage+1)+ ".xlsx";
			// Sauvegarde du fichier
			FileOutputStream fileOut = new FileOutputStream(PortalHelper.JBOSS_DATA_DIR + File.separator + PortalHelper.PORTAL_RESOURCES_DATA_DIR + File.separator + PortalHelper.PORTAL_DOWNLOAD_DATA_DIR + File.separator + exportFileNameRapp);
			wb.write(fileOut);
			fileOut.close();


			ecritures = new ArrayList<bkmvti>();
			ecritures = ecrituresRestantes;

			//Mise à jour de la liste des transactions restantes
			/*
			transactions = new ArrayList<Transaction>(); 
			itr = mapTrans.entrySet().iterator();
			while(itr.hasNext()) {
				Map.Entry mapentry = (Map.Entry) itr.next();
				transactions.add((Transaction) mapentry.getValue());
			}
			 */

			/*
			result = result + ">>> Écritures compensation unijambitses : " + "\n";
			for (bkmvti e : ecrituresOrangeUnijamb) {
				result = result + "   *** " + e.getLib() + "__" + e.getMon() + "\n";
			}

			System.out.println("*************** result *************** : " + result);
			 */

			/*
			if(ecrituresCliUnijamb.size() > 0 || ecrituresOrangeUnijamb.size() > 0){
				// Message d'information
				PortalInformationHelper.showInformationDialog("Verificaion des ecritures comptables effectuee  Ecritures comptables unijambitses recensée(s)! \n" + result, InformationDialog.DIALOG_SUCCESS);
			}
			else {
				// Message d'information
				PortalInformationHelper.showInformationDialog("Verificaion des ecritures comptables effectuee avec succes : " + "\n Nombre ecritures unijambitses sur le client : " + ecrituresCliUnijamb.size() + "\n Nombre ecritures unijambitses sur le compte Orange : " + ecrituresOrangeUnijamb.size(), InformationDialog.DIALOG_SUCCESS);
			}
			 */

			if(ecrituresCliUnijamb.size() == 0 && ecrituresOrangeUnijamb.size() == 0) {
				for(Transaction t : transactionsTFJO) {
					Transaction transac  = OMViewHelper.appManager.findByPrimaryKey(Transaction.class, t.getId(), null);			
					transac.setTfjoStatus(TransactionStatus.PROCESSING.name());					
					OMViewHelper.appDAOLocal.update(transac);
				}
			}

			//*** transactionsTFJO.clear();
			//*** transactionsTFJO = OMViewHelper.appManager.getTransactionTFJOStatusProcess();
			
			// Message d'information
			PortalInformationHelper.showInformationDialog("Verificaion des ecritures comptables effectuee avec succes \n" + " Nombre ecritures unijambitses sur le client : " + ecrituresCliUnijamb.size() + "\n" + "Nombre ecritures unijambitses sur le compte Orange : " + ecrituresOrangeUnijamb.size() + "\n" + "Nombre de transactions supprimées : " + transRemove.size(), InformationDialog.DIALOG_SUCCESS);

			/*
			transactionSimu.clear();
			listeCle.clear();
			ecrituresOrange.clear();
			ecrituresCli.clear();
			ecrituresOrangeRapp.clear();
			ecrituresCliRapp.clear();
			ecrituresOrangeUnijamb.clear();
			ecrituresRestantes.clear();
			mapOrange.clear();
			mapCli.clear();
			 */

			checkedTrans = Boolean.FALSE;
			doSearch = Boolean.FALSE;
			postFile = Boolean.TRUE;
			fileIsGenerated = true;
			transactions.clear();

		}catch(Exception ex) {

			// Traitement de l'exception
			//PortalExceptionHelper.threatException(ex);
			ex.printStackTrace();

		}

	}




	public void tfjoTransactions(){

		try{

			List<Transaction> trans = new ArrayList<Transaction>();
			if(parcoursPage == 0) {

				transactions = new ArrayList<Transaction>();  
				transactions = OMViewHelper.appManager.getTransactionTFJOs(StringUtils.isBlank(param.getLotTransactions())  ? 3000 : Integer.parseInt(param.getLotTransactions()));

				if(transactions.isEmpty() || transactions.size() == 0){
					PortalInformationHelper.showInformationDialog("Le systéme n'a détecté aucune transaction à comptabiliser !", InformationDialog.DIALOG_INFORMATION);
					return;
				}

				ecritures = new ArrayList<bkmvti>();	

				i = 0;
				j = 0;
				cpte = 1000;
				int size = transactions.size();
				int reste = size % cpte;
				nb = size / cpte;
				page = (reste > 0 ? 1 : 0) + nb;

				if(transactions.size() < 1000 ){					
					ecritures = OMViewHelper.appManager.genererEcritureTFJOs(transactions);
					parcoursPage++;	

					boolean resultat = transferEcritures(ecritures);
					if(resultat) {
						successPage++; 
						PortalInformationHelper.showInformationDialog("TFJO N° " + parcoursPage + "effectue avec succes, " + successPage + " operations(s) reussie(s) / " + page, InformationDialog.DIALOG_SUCCESS);
					}	
					else {
						PortalInformationHelper.showInformationDialog("TFJO N° " + parcoursPage + "en echec, " + successPage + " operations(s) reussie(s) / " + page, InformationDialog.DIALOG_SUCCESS);
					}
				}	

			}

			if(parcoursPage < page) {

				j = j + cpte;
				trans = new ArrayList<Transaction>(transactions.subList(i,j));
				ecritures = OMViewHelper.appManager.genererEcritureTFJOs(trans);
				nb = nb - 1;
				i = j;
				parcoursPage++;	

				boolean resultat = transferEcritures(ecritures);
				if(resultat) {
					successPage++; 
					PortalInformationHelper.showInformationDialog("TFJO N° " + parcoursPage + "effectue avec succes, " + successPage + " operations(s) reussie(s) / " + page, InformationDialog.DIALOG_SUCCESS);
				}	
				else {
					PortalInformationHelper.showInformationDialog("TFJO N° " + parcoursPage + "en echec, " + successPage + " operations(s) reussie(s) / " + page, InformationDialog.DIALOG_SUCCESS);
				}
			}
			else if(parcoursPage == page) {
				PortalInformationHelper.showInformationDialog("Fin du TFJO " + successPage + " operations(s) reussie(s) / " + page, InformationDialog.DIALOG_SUCCESS);			
			}

		}catch(Exception ex) {
			ex.printStackTrace();
		}

	}




	public boolean transferEcritures(List<bkmvti> entries) {

		Date dateAbon = null;
		if(transactions.size() > 0) {
			dateAbon = transactions.get(0).getSubscriber().getDate();
		}
		else {
			dateAbon = new Date();
		}

		String mois = DateFormatUtils.format(new Date(), OMTools.monthpatern);

		FactMonth fac = null;

		exportFileName = "OMW-OgMo-" + new SimpleDateFormat("ddMMyyHHmmss").format(new Date()) + ".unl";

		try {
			fac = OMViewHelper.appManager.visualiserRapportTFJOs(entries, dateAbon,OMViewHelper.getSessionUser().getLogin(),mois,OMTools.getDownloadDir() + File.separator + exportFileName, param);

			if(fac == null ){
				return false;

			}else{			
				ecritures = new ArrayList<bkmvti>();

				// Afficher le rapport FactMonth fac
				printRecu(fac,mois);
				// Message d'information
				return true;

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}



	public void transferFileEC(){

		param = OMViewHelper.appManager.findParameters();

		/*
		if(!StringUtils.isNotBlank(param.getIpAdressAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir l'adresse IP du serveur Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getPortServerAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir le port du serveur Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getUserLoginServerAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir le login de l'utilisateur du serveur Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getUserPasswordServerAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir le mot de passe de l'utilisateur du serveur Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getFilePathAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir le repertoire de fichier Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getFileNameAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir le nom de fichier Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getFileExtensionAmpli())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir l'extension de fichier Amplitude !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(!StringUtils.isNotBlank(param.getCheminSaveFile())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez définir le repertoire de sauvegarde des fichiers !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}
		 */

		if(!StringUtils.isNotBlank(param.getCodeOpeCompt())){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir le code opération pour la comptabilisation !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}

		if(parcoursPage >= page) {
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Impossible de proceder à la comptabilisation, le nombre total de lot à comptabiliser est atteint !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;	
		}


		try {

			transactionsTFJO = OMViewHelper.appManager.getTransactionTFJOStatusProcess();
			if(transactionsTFJO.size() == 0 || transactionsTFJO.isEmpty()){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Aucune transaction à comptabiliser !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}

			ecritures.clear();
			ecritures = OMViewHelper.appManager.genererEcritureTFJOs(transactionsTFJO);


			if(ecritures.size() == 0 || ecritures.isEmpty()){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Aucune ecriture à poster !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}


			try{	

				//Compteur du nombre de TFJO
				parcoursPage++;
				Date dateAbon = transactionsTFJO.size() > 0 ? transactionsTFJO.get(0).getSubscriber().getDate() : new Date();				
				String mois = DateFormatUtils.format(new Date(), OMTools.monthpatern);

				FactMonth fac = null;
				exportFileName = "OMW-OgMo-" + new SimpleDateFormat("ddMMyyHHmmss").format(new Date()) +"_"+parcoursPage+ ".unl"; 
				fac = OMViewHelper.appManager.visualiserRapportTFJOs(ecritures, dateAbon,OMViewHelper.getSessionUser().getLogin(),mois,OMTools.getDownloadDir() + File.separator + exportFileName, param);
				if(fac == null ){
					for(Transaction t : transactionsTFJO) {
						Transaction transac  = OMViewHelper.appManager.findByPrimaryKey(Transaction.class, t.getId(), null);			
						transac.setTfjoStatus("");					
						OMViewHelper.appDAOLocal.update(transac);
					}

					indicatifLotTrans = "TFJO N° " + parcoursPage + " en echec, " + successPage + " operations(s) reussie(s) / " + page ;
					PortalInformationHelper.showInformationDialog(indicatifLotTrans, InformationDialog.DIALOG_INFORMATION);
					parcoursPage--;

				}else{
					ecritures.clear();					
					//Annulation des evenements
					List<String> eves = OMViewHelper.appManager.annulerEves(transactionsTFJO);

					//Nombre de lot de TFJO ayant réussi
					successPage++;					
					
					if(finParcours < nombreTrans) { 
						debParcours = finParcours;
						finParcours = nombreTrans - finParcours >= lotTrans ? finParcours+lotTrans : finParcours+(nombreTrans - finParcours);
					}

					// Afficher le rapport FactMonth fac
					printRecu(fac,mois);
					
					eves.clear();
					// Message d'information
					indicatifLotTrans = "TFJO N° " + parcoursPage + " effectue avec succes, " + successPage + " operations(s) reussie(s) / " + page;
					PortalInformationHelper.showInformationDialog(indicatifLotTrans, InformationDialog.DIALOG_SUCCESS);					
				}

				doSearch = Boolean.TRUE; postFile = Boolean.FALSE;				
				checkedTrans = Boolean.FALSE;
				doSearch = Boolean.FALSE;
				postFile = Boolean.TRUE;

				if(cpte == nombreTrans) {
					checkedTrans = Boolean.TRUE;
					doSearch = Boolean.FALSE;
					postFile = Boolean.FALSE;
				}
				else {
					checkedTrans = Boolean.FALSE;
					doSearch = Boolean.TRUE;
					postFile = Boolean.FALSE;
				}

			}catch(Exception ex){
				// Traitement de l'exception
				PortalExceptionHelper.threatException(ex);
			}

			transactionsTFJO.clear();

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}




	/**
	 * Imprime la balance agee
	 */
	public void printRecu(FactMonth fac, String mois) {

		try{

			// Recuperation du visualisateur d'etats dans le FacesContext
			ReportViewerDialog viewer = (ReportViewerDialog) OMViewHelper.getSessionManagedBean("reportViewerDialog");

			// initialisation du visualisateur
			if(viewer != null){

				// Lecture du Type mime du fichier a afficher
				viewer.setMimeType(WebResourceManager.mimes.get("pdf"));

				// Simulation
				//FactMonth fac = SmsBankingViewHelper.appManager.FacturationAbonnement(login,exerc.getCode(),month,monthString);

				HashMap<Object, Object> map = new HashMap<Object, Object>();
				map.put("mois", mois);
				map.put("titre", "Traitement des Transactions");

				// Affichage du rapport de traitement 
				List<FactMonth> data = new ArrayList<FactMonth>();
				data.add(fac);

				// Lecture du flux de donnees
				byte[] flux = OMTools.getReportPDFBytes( OMTools.getReportsDir().concat("RapportFact.jasper"), map, data);
				viewer.setStreamData( flux );
				viewer.buildReport("RapportFact.jasper",data, map);


				//Deplacement du fichier
				archiverDocs(flux);			


				// Ouverture du Visualisateur
				viewer.open();

			}

		}catch(Exception ex) {

			// Traitement de l'exception
			//PortalExceptionHelper.threatException(ex);
			ex.printStackTrace();

		}

	}



	public void archiverDocs(byte[] flux) {

		try {

			//Deplacer Rapport TFJO
			FileOutputStream fileOuputStream = new FileOutputStream(OMTools.getReportsDir() + "OGMO-" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())+"_"+parcoursPage+".pdf");
			fileOuputStream.write(flux);
			fileOuputStream.close();
			File f = new File(OMTools.getReportsDir() + "OGMO-" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())+"_"+parcoursPage+".pdf");

			DeplacerFichier(f, "OrangeMoney");

			//Suppression Rapport TFJO
			f.delete();


			//Deplacer File EC
			File fec = new File(OMTools.getDownloadDir() + File.separator + exportFileName);
			DeplacerFichier(fec, "OrangeMoney");

			//Deplacer File RAPP
			File frapp = new File(OMTools.getDownloadDir() + File.separator + exportFileNameRapp);
			DeplacerFichier(frapp, "OrangeMoney");

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}



	/**
	 * 
	 * @param file
	 * @param agence
	 */
	public void DeplacerFichier(File file, String Nom){

		try{

			String name = file.getName();
			//*** System.out.println("*************** name *************** : " + name);
			byte[] fileByte = loadTemporairyUploadedFile(file);

			copyFile(fileByte, Nom, name);

		}catch (Exception e){
			e.printStackTrace();
		}

	}



	/**
	 * 
	 * @param file
	 * @return
	 */
	private static byte[] loadTemporairyUploadedFile(File file)
	{
		if (file == null) {
			return null;
		}
		if (!file.exists()) {
			return null;
		}
		if (!file.isFile()) {
			return null;
		}
		if (!file.canRead()) {
			return null;
		}
		BufferedInputStream bis = null;
		try
		{
			bis = new BufferedInputStream(new FileInputStream(file));
		}
		catch (Exception e)
		{
			throw new RuntimeException("enterprisepanel.uploadlistener.errorontmpfileloading");
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			int data = 0;
			while ((data = bis.read()) != -1) {
				baos.write(data);
			}
		}
		catch (Exception e)
		{
			try
			{
				bis.close();
			}
			catch (Exception localException1) {}
			throw new RuntimeException("enterprisepanel.uploadlistener.errorontmpfileloading");
		}
		try
		{
			bis.close();
		}
		catch (Exception localException2) {}
		return baos.toByteArray();
	}



	/**
	 * 
	 * @param donnee
	 * @param chemin
	 * @param name
	 */
	public void copyFile(byte[] donnee, String Nom, String name){

		try{

			param = OMViewHelper.appManager.findParameters();
			String chemin = param.getCheminSaveFile();
			//*** System.out.println("*************** chemin *************** : " + chemin);

			//String chemin = "E:\\ACQUISITION\\ARCHIVES";
			SimpleDateFormat formaterYear = new SimpleDateFormat("yyyy");
			SimpleDateFormat formaterDate = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formaterMonth = new SimpleDateFormat("MMMMM");
			//SimpleDateFormat formaterHeur = new SimpleDateFormat("HH:mm:ss");
			Date aujourdhui = new Date();

			String annee = formaterYear.format(aujourdhui);
			String mois = formaterMonth.format(aujourdhui);
			String jour = formaterDate.format(aujourdhui);
			//String heure = formaterHeur.format(aujourdhui).replaceAll(":","");

			chemin = chemin+"//"+annee+"//"+mois+"//"+jour+"//"+Nom;
			//name = heure+"_"+name;

			// Chemin
			File rep = new File(chemin);
			if (!rep.isDirectory()) rep.mkdirs();

			FileOutputStream fileOuputStream = new FileOutputStream(chemin+"/"+name);
			fileOuputStream.write(donnee);
			fileOuputStream.close();

		}catch(Exception e){

			e.printStackTrace();

		}

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



	public void purgerEcritures() {

		try {

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}



	public void ignorerTransaction() throws JsonGenerationException, JsonMappingException, UnsupportedEncodingException, IOException,
	JSONException, DAOAPIException, URISyntaxException{
		// Initialisation d'un conteneur de restrictions
		RestrictionsContainer rc = RestrictionsContainer.getInstance().add(Restrictions.eq("tfjoStatus", "CANCEL"));
		// Ajout des restrictions au conteneur 

		String mot = "-/ ORANGE_STATUS:NULL / 25112021";
		// Ajout des restrictions au conteneur
		rc.add(Restrictions.ilike("reverseTrans","%"+mot+"%",MatchMode.ANYWHERE));
		OrderContainer orders = OrderContainer.getInstance().add(Order.desc("datetime"));
		List<Transaction> transacs = OMViewHelper.appManager.filterTransactions(rc, orders);

		System.out.println("*************** ignorerTransaction transacs.size() ***************** : " + transacs.size());
		OMViewHelper.appManager.ignoirerEvenements(transacs);

		PortalInformationHelper.showInformationDialog("Ignorance fait avec succes !!!", InformationDialog.DIALOG_SUCCESS);

	}



	/**
	 * @return the transactions
	 */
	public List<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @return the num
	 */
	public int getNum() {
		return num++;
	}

	/**
	 * @return the numEC
	 */
	public int getNumEC() {
		return numEC++;
	}

	/**
	 * @return the txtDateDeb
	 */
	public String getTxtDateDeb() {
		return txtDateDeb;
	}

	/**
	 * @param txtDateDeb the txtDateDeb to set
	 */
	public void setTxtDateDeb(String txtDateDeb) {
		this.txtDateDeb = txtDateDeb;
	}

	/**
	 * @return the txtDateFin
	 */
	public String getTxtDateFin() {
		return txtDateFin;
	}

	/**
	 * @param txtDateFin the txtDateFin to set
	 */
	public void setTxtDateFin(String txtDateFin) {
		this.txtDateFin = txtDateFin;
	}


	public String getTxtPhone() {
		return txtPhone;
	}

	public void setTxtPhone(String txtPhone) {
		this.txtPhone = txtPhone;
	}

	/**
	 * @return the opItems
	 */
	public List<SelectItem> getOpItems() {
		return opItems;
	}

	/**
	 * @return the txtSearchOp
	 */
	public TypeOperation getTxtSearchOp() {
		return txtSearchOp;
	}

	/**
	 * @param txtSearchOp the txtSearchOp to set
	 */
	public void setTxtSearchOp(TypeOperation txtSearchOp) {
		this.txtSearchOp = txtSearchOp;
	}

	/**
	 * @return the selectedTransaction
	 */
	public Transaction getSelectedTransaction() {
		return selectedTransaction;
	}

	/**
	 * @param selectedTransaction the selectedTransaction to set
	 */
	public void setSelectedTransaction(Transaction selectedTransaction) {
		this.selectedTransaction = selectedTransaction;
	}

	/**
	 * @return the statutItems
	 */
	public List<SelectItem> getStatutItems() {
		return statutItems;
	}

	/**
	 * @return the txtSearchStatus
	 */
	public TransactionStatus getTxtSearchStatus() {
		return txtSearchStatus;
	}

	/**
	 * @param txtSearchStatus the txtSearchStatus to set
	 */
	public void setTxtSearchStatus(TransactionStatus txtSearchStatus) {
		this.txtSearchStatus = txtSearchStatus;
	}

	/**
	 * @return the exportFileName
	 */
	public String getExportFileName() {
		return exportFileName;
	}

	public String getExportFileNameRapp() {
		return exportFileNameRapp;
	}

	/**
	 * @return the txtSearchPosted
	 */
	public Boolean getTxtSearchPosted() {
		return txtSearchPosted;
	}

	/**
	 * @param txtSearchPosted the txtSearchPosted to set
	 */
	public void setTxtSearchPosted(Boolean txtSearchPosted) {
		this.txtSearchPosted = txtSearchPosted;
	}

	/**
	 * @return the postedItems
	 */
	public List<SelectItem> getPostedItems() {
		return postedItems;
	}

	public boolean isExportedFileExist() {
		return new File( OMTools.getDownloadDir() + File.separator + exportFileNameRapp).exists() && fileIsGenerated;
	}

	/**
	 * @return the ecritures
	 */
	public List<bkmvti> getEcritures() {
		return ecritures;
	}

	public String getECFileDefinition() {
		return ecritures != null && !ecritures.isEmpty() ? "FrmEC.xhtml" : "";
	}

	public void closeEC() {
		ecritures.clear();
	}

	public String getTotalDebit() {
		double mnt = 0;

		for(bkmvti mvt : ecritures){
			mnt += mvt.getSen().equals("D") ? mvt.getMon() : 0;
		}

		return OgMoHelper.espacement(mnt);
	}

	public String getTotalCredit() {
		double mnt = 0;

		for(bkmvti mvt : ecritures){
			mnt += mvt.getSen().equals("C") ? mvt.getMon() : 0;
		}

		return OgMoHelper.espacement(mnt);
	}

	public String getTotalCommissions() {
		double mnt = 0;

		/*for(bkmvti mvt : ecritures){
			mnt += mvt.getSen().equals("C") ? mvt.getMon() : 0;
		}*/

		return OgMoHelper.espacement(mnt);
	}

	public String getTotalTaxes() {
		double mnt = 0;

		/*for(bkmvti mvt : ecritures){
			mnt += mvt.getSen().equals("C") ? mvt.getMon() : 0;
		}*/

		return OgMoHelper.espacement(mnt);
	}




	//****************************************************************************************
	//****************************************************************************************


	public boolean isFormRapportOpen(){
		return !equilibre.isEmpty();
	}

	public String getFrmRapportName(){
		return "frmRapportControl";
	}
	public String getFrmRapportFileDefinition(){
		return isFormRapportOpen() ? "FrmRapportControl.xhtml" : ( ((FrmTFJO) OMViewHelper.getSessionManagedBean("frmTFJO")).isFormRapportOpen() ? "FrmRapportControl.xhtml" : "emptyPage.xhtml");
	}


	/**
	 * @return the equilibre
	 */
	public List<Equilibre> getEquilibre() {
		return equilibre;
	}

	/**
	 * @return the doublon
	 */
	public List<Doublon> getDoublon() {
		return doublon;
	}

	/**
	 * @return the doReconci
	 */
	public boolean isDoReconci() {
		return doReconci;
	}

	/**
	 * @param doReconci the doReconci to set
	 */
	public void setDoReconci(boolean doReconci) {
		this.doReconci = doReconci;
	}

	public boolean isDoSearch() {
		return doSearch;
	}

	public void setDoSearch(boolean doSearch) {
		this.doSearch = doSearch;
	}


	public boolean isPostFile() {
		return postFile;
	}

	public void setPostFile(boolean postFile) {
		this.postFile = postFile;
	}

	/**
	 * @return the checkedTrans
	 */
	public boolean isCheckedTrans() {
		return checkedTrans;
	}

	/**
	 * @param checkedTrans the checkedTrans to set
	 */
	public void setCheckedTrans(boolean checkedTrans) {
		this.checkedTrans = checkedTrans;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the nombreTrans
	 */
	public int getNombreTrans() {
		return nombreTrans;
	}

	/**
	 * @param nombreTrans the nombreTrans to set
	 */
	public void setNombreTrans(int nombreTrans) {
		this.nombreTrans = nombreTrans;
	}

	/**
	 * @return the indicatifLotTrans
	 */
	public String getIndicatifLotTrans() {
		return indicatifLotTrans;
	}

	/**
	 * @param indicatifLotTrans the indicatifLotTrans to set
	 */
	public void setIndicatifLotTrans(String indicatifLotTrans) {
		this.indicatifLotTrans = indicatifLotTrans;
	}





}
