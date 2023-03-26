/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.forms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.afb.dpd.orangemoney.jpa.entities.Exercice;
import com.afb.dpd.orangemoney.jpa.entities.FactMonth;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jpa.enums.TransactionStatus;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jpa.tools.Doublon;
import com.afb.dpd.orangemoney.jpa.tools.Equilibre;
import com.afb.dpd.orangemoney.jpa.tools.OgMoHelper;
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

import afb.dsi.dpd.portal.jpa.entities.User;

/**
 * Formulaire de comptabilisation des Commissions sur les transactions de Pull/Push
 * @author Francis KONCHOU
 * @version 1.0
 */
public class FrmTFJO extends AbstractPortalForm {
	
	
	/**
	 * 
	 */
	private String monthString = "";


	//private String traitement ="";


	private int month;

	private int year;

	private Exercice exercice = null;


	List<Equilibre> equilibre = new ArrayList<Equilibre>();
	List<Doublon> doublon = new ArrayList<Doublon>();
	
	int num = 1, nbParPage = 50, nbPages = 0, numPage = 0, nbAbo = 0; boolean fileIsGenerated = false;
	Double mntComs = 0d, mntTax = 0d;


	private Double montant = 0d;
	private Integer nombre = 0;
	private TransactionStatus status = null;
	
	public List<Transaction> list = new ArrayList<Transaction>();
			
	///*** public List<Comptabilisation> list = new ArrayList<Comptabilisation>();
	
	private String exportFileName = OMViewHelper.getSessionUser().getLogin()+"_tfjo-OrangeMoney-" + new SimpleDateFormat("ddMMyy").format(new Date()) + ".xls";
	
	
	/**
	 * Default Constructor
	 */
	public FrmTFJO() {}
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm(){
		// TODO Auto-generated method stub
		super.initForm();  numPage = 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Comptabilisation périodique des abonnements Orange Money";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose(); list.clear(); num = 1; fileIsGenerated = false;
		equilibre.clear(); doublon.clear(); nbAbo= 0; mntComs = 0d; mntTax = 0d; numPage = 0;
		monthString = ""; month = 0; montant = 0d; year = 0; exercice = null; nombre = 0;
	}
	
		
	
	/**
	 * Execution de la Comptabilisation
	 */
	@SuppressWarnings("unused")
	public void executerTFJO(){

		// Recuperation des parametres de l'utilisateur connecte
		User user = OMViewHelper.getSessionUser();
		String login = "";
		if (user != null ) login = user.getLogin();
		
		List<Transaction> compta = new ArrayList<Transaction>();
		// Recuperer la date de traitement
		Date dateCompt =  new Date(); 
		try {
			// Verifier si les TFJO on deja ete effectues (Transaction avec le status PROCESSING)
			list = OMViewHelper.appManager.chargerDonneesComptabiliserTFJO(dateCompt);

			if(!list.isEmpty()){
				// Comptabilisation de operations
				somme(list);
				numPage = 1;
				num = 1;
				compta.clear();
				PortalInformationHelper.showInformationDialog("Traitement de la facturation en cours.\n\n Veuillez exécuter la comptabilisation.", InformationDialog.DIALOG_INFORMATION);
				status = TransactionStatus.PROCESSING;
				return;
			}
			compta.clear();

			// Facturation des abonnes
			OMViewHelper.appManager.executerTFJO2(dateCompt); //list = OMViewHelper.appManager.executerTFJO2();

			// Chargement des operations de Comptabilisation des facturations
			list = OMViewHelper.appManager.chargerDonneesComptabiliserTFJO(dateCompt);
			somme(list);

			// Msg d'information
			if(list.isEmpty()) PortalInformationHelper.showInformationDialog("Aucune comptabilisation a effectuer cette période!", InformationDialog.DIALOG_INFORMATION);
			numPage = 1;
			num = 1;

		} catch(Exception ex) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
		}
		status = TransactionStatus.PROCESSING;
	}

	
	
	/**
	 * Execution de la Comptabilisation
	 */
	public void executerRegulTFJO(){

		try {

			List<Transaction> values = OMViewHelper.appDAOLocal.filter(Transaction.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("typeOperation",TypeOperation.COMPTABILISATION)).add(Restrictions.eq("status", TransactionStatus.REGUL)).add(Restrictions.or(Restrictions.isNull("dateControle"), Restrictions.lt("dateControle", new Date()))), OrderContainer.getInstance().add(Order.desc("dateCompta")), null, 0, 1);
			// Msg d'information
			if(values.isEmpty()) {
				PortalInformationHelper.showInformationDialog("Toutes les comptabilisations en REGUL ont été traitées ce jour, veuillez réessayer demain!", InformationDialog.DIALOG_INFORMATION);
				return;
			}
			
			// Chargement des operations de Comptabilisation des REGUL
			list = OMViewHelper.appManager.chargerDonneesComptabiliserRegul();

			// Msg d'information
			if(list.isEmpty()) PortalInformationHelper.showInformationDialog("Aucune comptabilisation a effectuer, veuillez réessayer!", InformationDialog.DIALOG_INFORMATION);

			numPage = 1;
			num = 1;
			somme(list);
		} catch(Exception ex) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
		}
		status = TransactionStatus.REGUL;
	}


	private void somme(List<Transaction> list){
		montant = 0d;
		for(Transaction tx : list) {
			montant += tx.getTtc();
		}
		nombre = list.size();
	}
	
	
	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}

	public Integer getNombre() {
		return nombre;
	}

	public void setNombre(Integer nombre) {
		this.nombre = nombre;
	}
	
	
	public void pageSuivante(){

		try {
			if(numPage > 1 && list.isEmpty()) return;
			numPage++;
			//chargerDonnees();
			num = 1;

		} catch(Exception ex) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
		}
	}

	public void pagePrecedente(){

		try {
			num = 1;
			if(numPage == 1) return;
			numPage--;
			//chargerDonnees();
		} catch(Exception ex) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
		}
	}
	
	
	/**
	 * Validation de la Comptabilisation
	 */
	public void validerTFJO(){

		if(status==null) {
			PortalInformationHelper.showInformationDialog("Chargez d'abord les opérations à comptabiliser", InformationDialog.DIALOG_WARNING);
			status = null;
			return;
		}

		if(list.isEmpty()) {
			PortalInformationHelper.showInformationDialog("Aucune opération à comptabiliser", InformationDialog.DIALOG_WARNING);
			status = null;
			return;
		}

		try {

			// Validation de la comptabilisation
			OMViewHelper.appManager.validerTFJO(list, OMViewHelper.getSessionUser().getLogin(), year, month);
			
			list.clear();
			montant = 0d;
			nombre = 0;

			// Recuperation des Rapports de Controle
			if(status.equals(TransactionStatus.REGUL)){
				equilibre = OMViewHelper.appManager.getRapportEquilibre(OMViewHelper.getSessionUser().getLogin(), OMTools.RGUL_MAC);
				doublon = OMViewHelper.appManager.getRapportDoublon(OMViewHelper.getSessionUser().getLogin(), OMTools.RGUL_MAC);
				nbAbo = OMViewHelper.appManager.getTotalAbonComptabilises(OMViewHelper.getSessionUser().getLogin(), OMTools.RGUL_MAC);
				mntComs = OMViewHelper.appManager.getTotalComsAbonComptabilises(OMViewHelper.getSessionUser().getLogin(), OMTools.RGUL_COM);
				mntTax = OMViewHelper.appManager.getTotalTaxAbonComptabilises(OMViewHelper.getSessionUser().getLogin(), OMTools.RGUL_TAX);
			}else {
				equilibre = OMViewHelper.appManager.getRapportEquilibre(OMViewHelper.getSessionUser().getLogin(), OMTools.FACT_MAC);
				doublon = OMViewHelper.appManager.getRapportDoublon(OMViewHelper.getSessionUser().getLogin(), OMTools.FACT_MAC);
				nbAbo = OMViewHelper.appManager.getTotalAbonComptabilises(OMViewHelper.getSessionUser().getLogin(), OMTools.FACT_MAC);
				mntComs = OMViewHelper.appManager.getTotalComsAbonComptabilises(OMViewHelper.getSessionUser().getLogin(), OMTools.FACT_COM);
				mntTax = OMViewHelper.appManager.getTotalTaxAbonComptabilises(OMViewHelper.getSessionUser().getLogin(), OMTools.FACT_TAX);
			}
			/*equilibre = OMViewHelper.appManager.getRapportEquilibre();
			doublon = OMViewHelper.appManager.getRapportDoublon();
			nbAbo = OMViewHelper.appManager.getTotalAbonComptabilises();
			mntComs = OMViewHelper.appManager.getTotalComsAbonComptabilises();
			mntTax = OMViewHelper.appManager.getTotalTaxAbonComptabilises();*/

			// Msg d'information
			String msg = status.equals(TransactionStatus.REGUL) ? "de régularisation" : "de facturation  "; // du mois de +monthString;
			PortalInformationHelper.showInformationDialog("Les écritures comptables "+msg+" ont été postées avec succès dans le Core Banking!", InformationDialog.DIALOG_INFORMATION);
			
			num = 1;

		} catch(Exception ex) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
		}
		status = null;
	}
	
	
	public boolean isFormRapportOpen(){
		return !equilibre.isEmpty();
	}

	/**
	 * Imprime le rapport de la simulation
	 */
	public void imprimerRapportSimulation(){

		try{

			if(list == null || list.isEmpty()) return;

			// Recuperation du visualisateur d'etats dans le FacesContext
			ReportViewerDialog viewer = (ReportViewerDialog) OMViewHelper.getSessionManagedBean("reportViewerDialog");

			// initialisation du visualisateur
			if(viewer != null) {

				// Lecture du Type mime du fichier a afficher
				viewer.setMimeType(WebResourceManager.mimes.get("pdf"));

				// Initialisation de la map des parametres de l'etat
				HashMap<Object, Object> map = new HashMap<Object, Object>();

				map.put("logo", OMTools.getLogoEntete());
				map.put("SUBREPORT_DIR", OMTools.getReportsDir());
				map.put("codeUser", OMViewHelper.getSessionUser().getLogin());

				// Lecture du flux de donnees
				viewer.setStreamData( OMTools.getReportPDFBytes( OMTools.getReportsDir().concat("SimulationFacturation.jasper"), map, list)  );

				// Ouverture du Visualisateur
				viewer.open();
			}

		}catch(Exception ex) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
			ex.printStackTrace();

		}

	}

	/**
	 * Extrait les ecritures comptables des transactions selectionnees sous excel
	 */
	public void extraireECSousExcel(){

		if(list.isEmpty()) {
			PortalInformationHelper.showInformationDialog("La liste est vide! Impossible d'effectuer cette action.", InformationDialog.DIALOG_WARNING);
			return;
		}

		try {

			// Export des donnees de comptabilisation
			OMViewHelper.appManager.exportComptaIntoExcelFile(list, exportFileName);

			// Msg d'information
			PortalInformationHelper.showInformationDialog("Les écritures comptables ont été extraites! Veuillez cliquer sur le lien pour télécharger le fichier.", InformationDialog.DIALOG_INFORMATION);
			num = 1; fileIsGenerated = true;

		} catch(Exception ex) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
		}
	}
	
	
	
	/**
	 * @return the list
	 */
	public List<Transaction> getList() {
		return list;
	}

	/**
	 * @return the num
	 */
	public int getNum() {
		return num++;
	}

	public String getExportFileName() {
		return exportFileName;
	}

	/**
	 * Determine si le fichier des ecritures cptables a ete genere
	 * @return true si le fichier des ecritures comptables a tet genere, false sinon
	 */
	public boolean isExportedFileExist() {
		return new File( OMTools.getDownloadDir() + File.separator + exportFileName).exists() && fileIsGenerated;
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
	 * @return the nbAbo
	 */
	public int getNbAbo() {
		return nbAbo;
	}

	public String getFormattedMntComs() {
		return OgMoHelper.espacement(mntComs);
	}

	public String getFormattedMntTaxes() {
		return OgMoHelper.espacement(mntTax);
	}

	/**
	 * @return the nbParPage
	 */
	public int getNbParPage() {
		return nbParPage;
	}

	/**
	 * @param nbParPage the nbParPage to set
	 */
	public void setNbParPage(int nbParPage) {
		this.nbParPage = nbParPage;
	}

	/**
	 * @return the nbPages
	 */
	public int getNbPages() {
		return nbPages;
	}

	/**
	 * @param nbPages the nbPages to set
	 */
	public void setNbPages(int nbPages) {
		this.nbPages = nbPages;
	}

	/**
	 * @return the numPage
	 */
	public int getNumPage() {
		return numPage;
	}

	/**
	 * @return the monthString
	 */
	public String getMonthString() {
		return monthString;
	}

	/**
	 * @param monthString the monthString to set
	 */
	public void setMonthString(String monthString) {
		this.monthString = monthString;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	
	
	//***************************************************************************************
	//***************************************************************************************
	//***************************************************************************************
	
	
	
	/**
	 * Execution de la Comptabilisation
	 */
	/*
	public void executerTFJO(){
		
		try {
			
			// Comptabilisation de operations
			list = OMViewHelper.appManager.executerTFJO2();

			// Msg d'information
			if(list.isEmpty()) PortalInformationHelper.showInformationDialog("Aucune comptabilisation a effectuer cette période!", InformationDialog.DIALOG_INFORMATION);
			
			num = 1;
			
		} catch(Exception ex) {
			
			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
		}
	}
	*/
	

	/**
	 * Validation de la Comptabilisation
	 */
	/*
	public void validerTFJO(){
		
		if(list.isEmpty()) {
			PortalInformationHelper.showInformationDialog("Aucune opération à comptabiliser", InformationDialog.DIALOG_WARNING);
			return;
		}

		try {
			
			// Validation de la comptabilisation
			User user = OMViewHelper.getSessionUser();
			//FactMonth fac = null; //OMViewHelper.appManager.validerTFJO(list,user.getLogin());
			FactMonth fac = OMViewHelper.appManager.validerTFJO(list,user.getLogin());

			// Suppression des transactions reconciliees de la liste
			for(int i=list.size()-1; i>=0; i--) if(list.get(i).isSelected()) list.remove(i);
			
			// Msg d'information
			PortalInformationHelper.showInformationDialog("Les écritures comptables ont été postées avec succès dans le Core Banking!", InformationDialog.DIALOG_INFORMATION);
			num = 1;
			
			String mois = DateFormatUtils.format(new Date(), OMTools.monthpatern);
			// Afficher le rapport FactMonth fac
			printRecu(fac,mois);
			
		}catch(Exception ex){

			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);

		}
		
	}
	*/
	
	

	/**
	 * Imprime le rapport de la simulation
	 */
	/*
	public void imprimerRapportSimulation(){

		try{
			
			if(list == null || list.isEmpty()) return;
			
			// Recuperation du visualisateur d'etats dans le FacesContext
			ReportViewerDialog viewer = (ReportViewerDialog) OMViewHelper.getSessionManagedBean("reportViewerDialog");
			
			// initialisation du visualisateur
			if(viewer != null) {
				
				// Lecture du Type mime du fichier a afficher
				viewer.setMimeType(WebResourceManager.mimes.get("pdf"));
				
				// Initialisation de la map des parametres de l'etat
				HashMap<Object, Object> map = new HashMap<Object, Object>();
				
				map.put("logo", OMTools.getLogoEntete());
				map.put("SUBREPORT_DIR", OMTools.getReportsDir());
                map.put("codeUser", OMViewHelper.getSessionUser().getLogin());

				// Lecture du flux de donnees
				viewer.setStreamData( OMTools.getReportPDFBytes( OMTools.getReportsDir().concat("SimulationFacturation.jasper"), map, list)  );
				
				// Ouverture du Visualisateur
				viewer.open();
			}
			
		}catch(Exception ex) {
			
			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
			ex.printStackTrace();
			
		}
		
	}
	*/
	

	/**
	 * Extrait les ecritures comptables des transactions selectionnees sous excel
	 */
	/*
	public void extraireECSousExcel(){

		if(list.isEmpty()) {
			PortalInformationHelper.showInformationDialog("La liste est vide! Impossible d'effectuer cette action.", InformationDialog.DIALOG_WARNING);
			return;
		}
		
		try {
			
			// Validation de la comptabilisation
			OMViewHelper.appManager.exportComptabilisationIntoExcelFile(list, exportFileName);
			
			// Msg d'information
			PortalInformationHelper.showInformationDialog("Les écritures comptables ont été extraites! Veuillez cliquer sur le lien pour télécharger le fichier.", InformationDialog.DIALOG_INFORMATION);
			num = 1; fileIsGenerated = true;
			
		} catch(Exception ex) {
			
			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
		}
	}
	*/
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
				
				//System.out.println(mois+"-----------------------------"+fac);
				
				HashMap<Object, Object> map = new HashMap<Object, Object>();
				map.put("mois", mois);
				map.put("titre", "Facturation Abonnement");

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

	
}
