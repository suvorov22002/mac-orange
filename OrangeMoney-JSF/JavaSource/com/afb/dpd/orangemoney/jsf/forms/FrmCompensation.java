package com.afb.dpd.orangemoney.jsf.forms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import afb.dsi.dpd.portal.jpa.entities.User;
import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

import com.afb.dpd.orangemoney.jpa.entities.Compensation;
import com.afb.dpd.orangemoney.jpa.entities.FactMonth;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jpa.enums.TransactionStatus;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
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
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

/**
 * 
 * @author Francis K
 * @version 1.0
 */
public class FrmCompensation extends AbstractPortalForm{


	/**
	 * Liste des transactions
	 */
	List<Transaction> transactions = new ArrayList<Transaction>();
	
	/**
	 * Liste des transactions
	 */
	List<Compensation> compensations = new ArrayList<Compensation>();

	/**
	 * Numeroteur de lignes
	 */
	private int num = 0, numEC = 0;

	/**
	 * Periode de dates saisie
	 */
	private String txtDateDeb = PortalHelper.DEFAULT_DATE_FORMAT.format(new Date());
	private String txtDateFin = PortalHelper.DEFAULT_DATE_FORMAT.format(new Date());

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
	private String exportFileName = "OMW-OgMo-" + new SimpleDateFormat("ddMMyy").format(new Date()) + ".unl";

	/**
	 * Marqueur determinant si on a lance la generation du fichier des ecritures comptables
	 */
	private boolean fileIsGenerated = false;

	/**
	 * Default Constructor
	 */
	public FrmCompensation() {}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm(){
		// TODO Auto-generated method stub
		super.initForm();

		// Chargement des items de types d'operations
		//opItems.add( new SelectItem(null, "(Toutes)") );
		//for(TypeOperation to : TypeOperation.getValues()) opItems.add( new SelectItem(to, to.getValue()) );

		// Chargement des items de statut
		// statutItems.add( new SelectItem(null, " ") );
		// for(TransactionStatus to : TransactionStatus.getValues()) statutItems.add( new SelectItem(to, to.getValue()) );

		// Chargement des items de Postage
		//postedItems.add( new SelectItem(null, " ") );
		//postedItems.add( new SelectItem(Boolean.TRUE, "Compensées") );
		//postedItems.add( new SelectItem(Boolean.FALSE, "Non Compensées") );
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Compensation des transactions Orange Money";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose();
		transactions.clear(); opItems.clear(); statutItems.clear(); txtSearchOp = null; txtSearchStatus = null;
		selectedTransaction = null; fileIsGenerated = false; closeEC(); postedItems.clear();
		if(isExportedFileExist()) new File( OMTools.getDownloadDir() + File.separator + exportFileName).delete();

	}

	/**
	 * Methode de recherche des transactions sur les criteres saisis
	 */
	public void filterTransactions(){

		try {

			// Initialisation d'un conteneur de restrictions
			RestrictionsContainer rc = RestrictionsContainer.getInstance();

			// Ajout de la restriction sur la periode de date
			if(txtDateDeb != null && txtDateFin != null && !txtDateDeb.isEmpty() && !txtDateFin.isEmpty()) rc.add(Restrictions.between("date", OgMoHelper.sdf.parse(txtDateDeb.concat(" 00:01") ), OgMoHelper.sdf.parse(txtDateFin.concat(" 23:58") ) ));

			// Ajout de la restriction sur l'operation
			if(txtSearchOp != null) rc.add(Restrictions.eq("typeOperation",txtSearchOp));

			// Ajout de la restriction sur l'etat de la transaction
			if(txtSearchStatus != null) rc.add(Restrictions.eq("status",txtSearchStatus));

			// Ajout de la restriction sur l'etat de postage dans Delta
			if(txtSearchPosted != null) rc.add(Restrictions.eq("posted", txtSearchPosted ));

			// Initialisation d'un conteneur d'ordres
			OrderContainer orders = OrderContainer.getInstance().add(Order.desc("date")) ;

			// Filtre des transactions
			transactions = OMViewHelper.appManager.filterTransactions(rc, orders); 

			// Initialisation du compteur
			num = 1;

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
			OMViewHelper.appManager.extractECFromSelectedTransactionsIntoFile(transactions, OMTools.getDownloadDir() + File.separator + exportFileName);

			fileIsGenerated = true;

			// Message d'information
			PortalInformationHelper.showInformationDialog("Export effectue avec succes! Vous pouvez telecharger le fichier genere.", InformationDialog.DIALOG_SUCCESS);

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
	public void executerCompensation(){

		try{

			// Recherche des parametres generaux
			/**Parameters p = OMViewHelper.appManager.findParameters();

			// Si le module est desactive
			if(!p.getActive().booleanValue()){
				// Message d'information
				PortalInformationHelper.showInformationDialog("Un TFJ est en cours d'exécution, il n'est pas possible d'exécuter la Compensation.", InformationDialog.DIALOG_WARNING);
				return;
			}*/

			Date dateDeb = null;
			Date dateFin = null;

			User user = OMViewHelper.getSessionUser();
			String mois = DateFormatUtils.format(new Date(), OMTools.monthpatern);
			// Postage des ecritures comptables dans Delta PortalHelper.DEFAULT_DATE_FORMAT.parse(txtDateDeb) PortalHelper.DEFAULT_DATE_FORMAT.parse(txtDateFin)
			FactMonth fac = OMViewHelper.appManager.executerCompensation(dateDeb,dateFin,user.getLogin(),mois);

			if(fac == null ){
				
				PortalInformationHelper.showInformationDialog("Le systéme n'a détecté aucune transaction à comptabiliser !", InformationDialog.DIALOG_INFORMATION);
								
			}else{

				// Afficher le rapport FactMonth fac
				printRecu(fac,mois);

				// Message d'information
				PortalInformationHelper.showInformationDialog("Compensation des Opérations Orange Money de la période ["+txtDateDeb + ", " + txtDateFin +" ] effectuée avec succès!", InformationDialog.DIALOG_SUCCESS);
			
			}

		}catch(Exception e){
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
				viewer.setStreamData( OMTools.getReportPDFBytes( OMTools.getReportsDir().concat("RapportFact.jasper"), map, data)  );
				viewer.buildReport("RapportFact.jasper",data, map);

				// Ouverture du Visualisateur
				viewer.open();

			}

		}catch(Exception ex) {

			// Traitement de l'exception
			//PortalExceptionHelper.threatException(ex);
			ex.printStackTrace();

		}

	}

	public void archiverEcritures() {

		try {

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}

	public void purgerEcritures() {

		try {

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);

		}

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
		return new File( OMTools.getDownloadDir() + File.separator + exportFileName).exists() && fileIsGenerated;
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
	
}
