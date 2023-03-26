/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.forms;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.afb.dpd.orangemoney.jpa.entities.Parameters;
import com.afb.dpd.orangemoney.jpa.entities.ProfilMarchands;
import com.afb.dpd.orangemoney.jpa.entities.Rapprochement;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jpa.enums.TypeRapprochement;
import com.afb.dpd.orangemoney.jpa.tools.CSftp;
import com.afb.dpd.orangemoney.jsf.models.AbstractPortalForm;
import com.afb.dpd.orangemoney.jsf.models.InformationDialog;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.models.PortalInformationHelper;
import com.afb.dpd.orangemoney.jsf.tools.OMTools;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;
import com.yashiro.persistence.utils.dao.tools.encrypter.Encrypter;

import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

/**
 * Formulaire de gestion des rapprochements
 * @author Yves FOKAM
 * @version 1.0
 */
public class FrmRappImputation extends AbstractPortalForm {

	
	private List<Transaction> transactions = new ArrayList<Transaction>();

	private List<Rapprochement> list = new ArrayList<Rapprochement>();

	
	private Rapprochement currentObject = new Rapprochement();
	
	private String exportFileName = "ListeRapprochements.xls", searchNcp;
	
	private boolean fileIsGenerated = false;
	
	private String txtDateCompB2W= PortalHelper.DEFAULT_DATE_FORMAT.format(new Date()), comptesB2W, comptesW2B;
	
	private String txtDateCompW2B= PortalHelper.DEFAULT_DATE_FORMAT.format(new Date());
		
	private Date dateDebut= null, dateFin= null;
	
	
	private int num = 0;
	
	private List<SelectItem> typeOpeItems = new ArrayList<SelectItem>();
	private List<SelectItem> typeRapproItems = new ArrayList<SelectItem>();
	

	private TypeOperation searchTypeOpe;
	private TypeRapprochement searchTypeRapp;
	

	/**
	 * Default Constructor
	 */
	public FrmRappImputation() {}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm() {
		// TODO Auto-generated method stub
		super.initForm();  clear();
		
		List<ProfilMarchands> profilMarchands = OMViewHelper.appManager.filterProfilMarchands();
		
		comptesB2W = "";
		comptesW2B = "";
		for(ProfilMarchands p : profilMarchands) {			
			 comptesB2W += "'" + p.getNcpDAPPull().trim().subSequence(6, 17) + "'" + ", ";
			 comptesW2B += "'" + p.getNcpDAPPush().trim().subSequence(6, 17) + "'" + ", ";
		}
		profilMarchands.clear();
		
		// Lecture des parametres
		Parameters	params = OMViewHelper.appManager.consulterConfiguration();
		comptesB2W += "'" + params.getNcpDAPAcountWalet().trim().subSequence(6, 17) + "'" + ", ";
		comptesW2B += "'" + params.getNcpDAPWaletAcount().trim().subSequence(6, 17) + "'" + ", ";
		
		if(!comptesB2W.isEmpty()) comptesB2W = "(".concat( comptesB2W.substring(0, comptesB2W.length()-2) ).concat(")");
		if(!comptesW2B.isEmpty()) comptesW2B = "(".concat( comptesW2B.substring(0, comptesW2B.length()-2) ).concat(")");
		
		
		typeOpeItems = new ArrayList<SelectItem>();
		typeOpeItems.add(new SelectItem(null, "---Choisir---")); 
		typeOpeItems.add(new SelectItem(TypeOperation.Account2Wallet, TypeOperation.Account2Wallet.getValue()));
		typeOpeItems.add(new SelectItem(TypeOperation.Wallet2Account, TypeOperation.Wallet2Account.getValue()));
		
				
		list = new ArrayList<Rapprochement>();
		transactions = new ArrayList<Transaction>();
		
		dateDebut= new Date(); dateFin= new Date();
		
		
		
	}

	
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	public String getTitle() {
		return "Imputation des transactions";
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
		txtDateCompB2W = null; txtDateCompW2B= null; typeOpeItems.clear(); typeRapproItems.clear();  comptesB2W = null; comptesW2B = null;
	}

	
	
	public void clear(){
		num = 1; searchNcp = null; 	searchTypeOpe = null; searchTypeRapp = null; dateDebut = null; dateFin = null;
	}
	
	
	
	public void imputation(TypeOperation typeOpe, String sens, String comptes){
		
		try {

			Date debut = null;
			Date fin = null;

			//Recherche de la dernière date de rapprochement
			Date dateLastRapp = OMViewHelper.appManager.getLastRapprochement(typeOpe, TypeRapprochement.IMPUTATION, comptes);
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
			
			
			
			//Traitement du fichier Orange
			/* A DECOMMENTER POUR SON IMPLEMENTATION
			String pwd = Encrypter.getInstance().decryptText(param.getUserPasswordServerSmart());
			boolean verifFile = CSftp.searchFileRejet("REJ_" + currentObject.getId() + currentObject.getLibelle() + "CHARGEMENTCARTE.txt", param.getUserLoginServerSmart().trim(), 
					param.getIpAdressSmart().trim(),param.getFilePathSmartRejet().trim(), "", 
					pwd);
			
			if(verifFile == Boolean.FALSE){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Fichier de transactions Orange inexistant !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			
			if(verifFile == true){
				List<Transaction> listTransOrge = new ArrayList<Transaction>();
				
				SAXBuilder sxb = new SAXBuilder();
				
				//InputStreamReader isr = new InputStreamReader( jarFile.getInputStream(jarEntry) );
		        //BufferedReader reader = new BufferedReader(isr);
		        
				Document document = sxb.build(OMTools.getPiecesJointesDir() + "B2W_CCEICMCX_CM_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "0000.xml");
				
				Element racine = document.getRootElement();
				
				List<Element> listEtudiants = racine.getChildren("partnerRequests");
				Iterator<Element> i = listEtudiants.iterator();
				while(i.hasNext())	{
					
					Transaction transOrge = new Transaction();
					
					Element courant = (Element)i.next();
					
					if("bank2wallet".equals(courant.getChild("type").getText())) transOrge.setTypeOperation(TypeOperation.Account2Wallet);
					if("wallet2bank".equals(courant.getChild("type").getText())) transOrge.setTypeOperation(TypeOperation.Wallet2Account);
					if("statement".equals(courant.getChild("type").getText())) transOrge.setTypeOperation(TypeOperation.MiniStatement);
					if("balance".equals(courant.getChild("type").getText())) transOrge.setTypeOperation(TypeOperation.BALANCE);
					
					transOrge.setDate( new SimpleDateFormat("dd/MM/yyyy").parse( courant.getChild("requestDate").getText().trim().substring(0, 10)));
					transOrge.setAmount(StringUtils.isNotBlank(courant.getChild("amount").getText()) ? Double.parseDouble(courant.getChild("amount").getText()) : null);
					transOrge.setAccountAlias(courant.getChild("alias").getText());
					transOrge.setRequestId(courant.getChild("requestCode").getText());
										
					transOrge.setReferenceLettrage(new SimpleDateFormat("yyyy-MM-dd").format(transOrge.getDate()) + transOrge.getAccountAlias().trim().substring(8, 17) + sens + Integer.toString(transOrge.getAmount() != null ? transOrge.getAmount().intValue() : 0));
					
					if("000".equals(courant.getChild("responseCode"))) listTransOrge.add(transOrge);
					
					//reader.close();
				}

			}
			*/
			

			//Exécution du rapprochement
			Rapprochement rappro = OMViewHelper.appManager.executeImputation(typeOpe, sens, OMViewHelper.getSessionUser().getLogin(), comptes, debut, fin);
			
			if(rappro != null){
				
				if(StringUtils.isNotBlank(rappro.getSuspens())){
					String detail = "";
					for(String s : rappro.getSuspens().split(";")) detail += "** " + s + "\n";
					
					rappro.setDetailSuspens(detail);
				}
				currentObject = rappro;
				
				//Sauvegarde du rapprochement
				OMViewHelper.appDAOLocal.save(rappro);
								
				// Message d'information
				PortalInformationHelper.showInformationDialog("Rapprochement effectue avec succes !", InformationDialog.DIALOG_SUCCESS);
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
	
	
	
	
	public void imputationB2W(){
		
		num = 1;
		
		if(StringUtils.isBlank(comptesB2W)) {

			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Aucun compte DAP B2W à vérifier !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}
		
		imputation(TypeOperation.Account2Wallet, "C", comptesB2W);
	}
	
	
	
	public void imputationW2B(){
		
		num = 1;
		
		if(StringUtils.isBlank(comptesB2W)) {

			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Aucun compte DAP B2W à vérifier !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}
		
		imputation(TypeOperation.Wallet2Account, "D", comptesW2B);
		
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
			if(searchTypeRapp != null && StringUtils.isNotBlank(searchTypeRapp.getValue())) rc.add(Restrictions.eq("typeRapprochement", TypeRapprochement.IMPUTATION ));

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
