/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.forms;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.afb.dpd.orangemoney.jpa.entities.Parameters;
import com.afb.dpd.orangemoney.jpa.entities.ProfilMarchands;
import com.afb.dpd.orangemoney.jpa.entities.Rapprochement;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jpa.enums.TypeRapprochement;
import com.afb.dpd.orangemoney.jsf.models.AbstractPortalForm;
import com.afb.dpd.orangemoney.jsf.models.InformationDialog;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.models.PortalInformationHelper;
import com.afb.dpd.orangemoney.jsf.tools.OMTools;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

/**
 * Formulaire de gestion des rapprochements
 * @author Yves FOKAM
 * @version 1.0
 */
public class FrmRappCompensation extends AbstractPortalForm {

	
	private List<Transaction> transactions = new ArrayList<Transaction>();

	private List<Rapprochement> list = new ArrayList<Rapprochement>();

	
	private Rapprochement currentObject = new Rapprochement();
	
	private String exportFileName = "ListeRapprochements.xls", searchNcp;
	
	private boolean fileIsGenerated = false;
	
	private String txtDateCompB2W= PortalHelper.DEFAULT_DATE_FORMAT.format(new Date()), selectedNcpDAPB2W, selectedNcpDAPW2B;
	
	private String txtDateCompW2B= PortalHelper.DEFAULT_DATE_FORMAT.format(new Date());
		
	private Date dateDebut= null, dateFin= null;
	
	
	private int num = 0;
	
	private List<SelectItem> dAPB2WItems = new ArrayList<SelectItem>();
	private List<SelectItem> dAPW2BItems = new ArrayList<SelectItem>();
	private List<SelectItem> typeOpeItems = new ArrayList<SelectItem>();
	private List<SelectItem> typeRapproItems = new ArrayList<SelectItem>();
	

	private TypeOperation searchTypeOpe;
	private TypeRapprochement searchTypeRapp;
	

	/**
	 * Default Constructor
	 */
	public FrmRappCompensation() {}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm() {
		// TODO Auto-generated method stub
		super.initForm();  clear();
		
		List<ProfilMarchands> profilMarchands = OMViewHelper.appManager.filterProfilMarchands();
		dAPB2WItems = new ArrayList<SelectItem>();
		dAPW2BItems = new ArrayList<SelectItem>();
		
		dAPB2WItems.add(new SelectItem(null, "---Choisir---"));
		dAPW2BItems.add(new SelectItem(null, "---Choisir---"));
		
		for(ProfilMarchands p : profilMarchands) {
			dAPB2WItems.add(new SelectItem(p.getNcpDAPPull().trim().subSequence(6, 17), p.getNcpDAPPull().trim().subSequence(6, 17) + " : " + p.getProfilName()));
			dAPW2BItems.add(new SelectItem(p.getNcpDAPPush().trim().subSequence(6, 17), p.getNcpDAPPush().trim().subSequence(6, 17) + " : " + p.getProfilName()));
		}
		profilMarchands.clear();
		
		
		// Lecture des parametres
		Parameters	params = OMViewHelper.appManager.consulterConfiguration();
		dAPB2WItems.add(new SelectItem(params.getNcpDAPAcountWalet().trim().subSequence(6, 17), params.getNcpDAPAcountWalet().trim().subSequence(6, 17) + " : " + "ENDS USERS"));
		dAPW2BItems.add(new SelectItem(params.getNcpDAPWaletAcount().trim().subSequence(6, 17), params.getNcpDAPWaletAcount().trim().subSequence(6, 17) + " : " + "ENDS USERS"));
		
		typeOpeItems = new ArrayList<SelectItem>();
		typeOpeItems.add(new SelectItem(null, "---Choisir---")); 
		typeOpeItems.add(new SelectItem(TypeOperation.Account2Wallet, TypeOperation.Account2Wallet.getValue()));
		typeOpeItems.add(new SelectItem(TypeOperation.Wallet2Account, TypeOperation.Wallet2Account.getValue()));
		
		
		typeRapproItems = new ArrayList<SelectItem>();
		typeRapproItems.add(new SelectItem(null, "---Choisir---")); 
		for(TypeRapprochement t : TypeRapprochement.getValues()) typeRapproItems.add(new SelectItem(t, t.getValue()));
		
		list = new ArrayList<Rapprochement>();
		transactions = new ArrayList<Transaction>();
		
		dateDebut= new Date(); dateFin= new Date();
		
	}

	
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	public String getTitle() {
		return "Compensation des comptes DAP";
	};
	
	

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose();  clear(); list.clear();
		currentObject = null; transactions.clear();  fileIsGenerated = false; exportFileName = null;
		txtDateCompB2W = null; txtDateCompW2B= null; dAPB2WItems.clear(); dAPW2BItems.clear();	
		typeOpeItems.clear(); typeRapproItems.clear();
	}

	
	
	public void clear(){
		selectedNcpDAPB2W = null; selectedNcpDAPW2B = null; num = 1; searchNcp = null;
		searchTypeOpe = null; searchTypeRapp = null; dateDebut = null; dateFin = null;
	}
	
	
	
	public void compensation(TypeOperation typeOpe, String ncp){
		
		try {

			Date debut = null;
			Date fin = null;

			//Recherche de la dernière date de rapprochement
			Date dateLastRapp = OMViewHelper.appManager.getLastRapprochement(typeOpe, TypeRapprochement.COMPENSATION, ncp);
			if(dateLastRapp == null) {
				debut = DateUtils.addDays(new Date(), -1);
				fin = DateUtils.addDays(new Date(), -1);
			}
			else {
				debut = dateLastRapp;
				fin = DateUtils.addDays(new Date(), -1);
			}

			
			DateFormat formater = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
			debut = formater.parse("08/02/2019");
			fin = formater.parse("08/02/2019");

			//Exécution du rapprochement
			Rapprochement rappro = OMViewHelper.appManager.executeRapprochement(typeOpe, OMViewHelper.getSessionUser().getLogin(), ncp, debut, fin) ;

			if(rappro != null){
				
				if(StringUtils.isNotBlank(rappro.getSuspens())){
					String detail = "";
					for(String s : rappro.getSuspens().split(";")) detail += "** " + s + "\n";
					
					rappro.setDetailSuspens(detail);
				}
				currentObject = rappro;
				
				//Sauvegarde du rapprohement
				OMViewHelper.appDAOLocal.save(rappro);
								
				// Message d'information
				PortalInformationHelper.showInformationDialog("Rapprochement effectue avec succes!", InformationDialog.DIALOG_SUCCESS);
			}
			else {
				// Message d'information
				PortalInformationHelper.showInformationDialog("Rapprochement non effectue !", InformationDialog.DIALOG_INFORMATION);
			}

		} catch (Exception ex) {
			PortalExceptionHelper.threatException(ex);
			fileIsGenerated = false;
		}
		
	}
	
	
	
	
	public void compensationB2W(){
		num = 1;
		
		if(StringUtils.isBlank(selectedNcpDAPB2W)) {

			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez chosir un compte DAP B2W à rapprocher !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}
		
		compensation(TypeOperation.Account2Wallet, selectedNcpDAPB2W);
	}
	
	
	
	public void compensationW2B(){
		num = 1;
		
		if(StringUtils.isBlank(selectedNcpDAPW2B)) {

			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez chosir un compte DAP W2B à rapprocher !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}
		
		compensation(TypeOperation.Wallet2Account, selectedNcpDAPW2B);
	}
	



	public void exportListeExcell(){
		try {

			if(transactions.isEmpty()){
				PortalInformationHelper.showInformationDialog("Aucune souscription dans la liste !",InformationDialog.DIALOG_INFORMATION);
				return;
			}

			exportFileName = "LISTE_SOUSCRIPTIONS_"+new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date())+".xlsx";
			//*** OMViewHelper.appManager.exportSouscriptionIntoExcelFile(transactions,exportFileName);

			fileIsGenerated = true;

			// Message d'information
			PortalInformationHelper.showInformationDialog("Export effectue avec succes! Vous pouvez telecharger le fichier généré.", InformationDialog.DIALOG_SUCCESS);

		} catch (Exception ex) {
			PortalExceptionHelper.threatException(ex);
			fileIsGenerated = false;
		}
	}


	public boolean isExportedFileExist() {
		return new File( OMTools.getDownloadDir() + exportFileName).exists() && fileIsGenerated;
	}


	
	public boolean differenceDates(Date d1, Date d2) throws Exception{
		
		if((d1.getTime() - d2.getTime())/86400000 < 0 ){
			PortalInformationHelper.showInformationDialog("La date de Fin doit être Supérieure ou égale à la date de Début !", InformationDialog.DIALOG_SUCCESS);
			return false;
		}  
		return true ;

	}
	
	
	
	
	/**
	 * Methode de recherche des rapprochements
	 */
	public void rechercherRappro(){

		try {

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

			// Initialisation d'un conteneur de restrictions
			RestrictionsContainer rc = RestrictionsContainer.getInstance();
			
			if(dateDebut == null || dateFin == null){
				// Msg Info
				PortalInformationHelper.showInformationDialog("Veuillez saisir une période SVP", InformationDialog.DIALOG_WARNING);
				// Annulation
				return;
			}

			// Ajout de la restriction sur la periode de date
			Date deb = null, fin = null;
			if(dateDebut != null && dateFin != null){
				if(!differenceDates(dateFin, dateDebut)) return;

				deb = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(dateDebut) + " 00:00:00");
				fin = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(dateFin) + " 23:59:59");
				rc.add(Restrictions.between("dateRappro", deb, fin ));
			}



			// Ajout de la restriction sur le compte 
			if(StringUtils.isNotBlank(searchNcp)) rc.add(Restrictions.eq("ncpRappro", searchNcp ));

			// Ajout de la restriction sur le type Operation
			if(searchTypeOpe != null && StringUtils.isNotBlank(searchTypeOpe.getValue())) rc.add(Restrictions.eq("typeOperation", searchTypeOpe ));

			// Ajout de la restriction sur le type Operation
			if(searchTypeRapp != null && StringUtils.isNotBlank(searchTypeRapp.getValue())) rc.add(Restrictions.eq("typeRapprochement", TypeRapprochement.COMPENSATION ));

			// Initialisation d'un conteneur d'ordres
			OrderContainer orders = OrderContainer.getInstance().add(Order.desc("dateRappro")).add(Order.asc("ncpRappro"));


			// Filtre des rapprochements
			list = OMViewHelper.appManager.filterRapprochements(rc, orders); 

			// Initialisation du compteur
			num = 1;

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}
	
	

	public String getExportFileName() {
		return exportFileName;
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}

	public boolean isFileIsGenerated() {
		return fileIsGenerated;
	}

	public void setFileIsGenerated(boolean fileIsGenerated) {
		this.fileIsGenerated = fileIsGenerated;
	}

	public Rapprochement getCurrentObject() {
		return currentObject;
	}

	public void setCurrentObject(Rapprochement currentObject) {
		this.currentObject = currentObject;
	}

	

	/**
	 * Numeroteur de lignes dans la grille
	 * @return
	 */
	public int getNum() {
		return num++;
	}

	/**
	 * @return the souscriptions
	 */
	public List<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @return the txtDateCompB2W
	 */
	public String getTxtDateCompB2W() {
		return txtDateCompB2W;
	}

	/**
	 * @param txtDateCompB2W the txtDateCompB2W to set
	 */
	public void setTxtDateCompB2W(String txtDateCompB2W) {
		this.txtDateCompB2W = txtDateCompB2W;
	}
	
	public String getTxtDateCompW2B() {
		return txtDateCompW2B;
	}

	public void setTxtDateCompW2B(String txtDateCompW2B) {
		this.txtDateCompW2B = txtDateCompW2B;
	}

	
	
	public String getSelectedNcpDAPB2W() {
		return selectedNcpDAPB2W;
	}

	public void setSelectedNcpDAPB2W(String selectedNcpDAPB2W) {
		this.selectedNcpDAPB2W = selectedNcpDAPB2W;
	}

	public String getSelectedNcpDAPW2B() {
		return selectedNcpDAPW2B;
	}

	public void setSelectedNcpDAPW2B(String selectedNcpDAPW2B) {
		this.selectedNcpDAPW2B = selectedNcpDAPW2B;
	}

	public List<SelectItem> getdAPB2WItems() {
		return dAPB2WItems;
	}

	public void setdAPB2WItems(List<SelectItem> dAPB2WItems) {
		this.dAPB2WItems = dAPB2WItems;
	}

	public List<SelectItem> getdAPW2BItems() {
		return dAPW2BItems;
	}

	public void setdAPW2BItems(List<SelectItem> dAPW2BItems) {
		this.dAPW2BItems = dAPW2BItems;
	}

	public List<Rapprochement> getList() {
		return list;
	}

	public void setList(List<Rapprochement> list) {
		this.list = list;
	}
	
	
	
	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public String getSearchNcp() {
		return searchNcp;
	}

	public void setSearchNcp(String searchNcp) {
		this.searchNcp = searchNcp;
	}

	public TypeOperation getSearchTypeOpe() {
		return searchTypeOpe;
	}

	public void setSearchTypeOpe(TypeOperation searchTypeOpe) {
		this.searchTypeOpe = searchTypeOpe;
	}

	public TypeRapprochement getSearchTypeRapp() {
		return searchTypeRapp;
	}

	public void setSearchTypeRapp(TypeRapprochement searchTypeRapp) {
		this.searchTypeRapp = searchTypeRapp;
	}

	public List<SelectItem> getTypeOpeItems() {
		return typeOpeItems;
	}

	public void setTypeOpeItems(List<SelectItem> typeOpeItems) {
		this.typeOpeItems = typeOpeItems;
	}

	public List<SelectItem> getTypeRapproItems() {
		return typeRapproItems;
	}

	public void setTypeRapproItems(List<SelectItem> typeRapproItems) {
		this.typeRapproItems = typeRapproItems;
	}
		
	
}
