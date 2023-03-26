/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.forms;

import java.io.File;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.rpc.holders.StringHolder;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.afb.dpd.orangemoney.jpa.entities.Subscriber;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jpa.enums.StatutContrat;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jsf.models.AbstractPortalForm;
import com.afb.dpd.orangemoney.jsf.models.InformationDialog;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.models.PortalInformationHelper;
import com.afb.dpd.orangemoney.jsf.models.ReportViewerDialog;
import com.afb.dpd.orangemoney.jsf.servlet.WebResourceManager;
import com.afb.dpd.orangemoney.jsf.tools.OMTools;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.banktowallet.b2w.b2wold.BankExceptionCode;
import com.btow.om.register.RequestInitiator;
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

/**
 * Formulaire de filtre des souscriptions
 * @author Francis KONCHOU
 * @version 1.0
 */
public class FrmSouscriptions extends AbstractPortalForm {

	private List<Subscriber> souscriptions = new ArrayList<Subscriber>();

	private Date txtDateDeb = new Date();

	private Date txtDateFin = new Date();

	private String txtSearchClient;

	private String txtSearchCustId;

	private Subscriber selectedSubscriber = null;

	private Subscriber sigSubscriber = null;

	private Subscriber selectedRecu = null;

	private int num = 0;

	private Long idAnnulation, idDesactivation;

	private boolean fileIsGenerated = false;

	private String exportFileName = "ListeSouscriptions.xls";

	private String urlSignature = null;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH':'mm':'ss");

	/**
	 * Default Constructor
	 */
	public FrmSouscriptions() {}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm() {
		// TODO Auto-generated method stub
		super.initForm();
		
		txtDateDeb = new Date();
		txtDateFin = new Date();
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	public String getTitle() {
		return "Liste des souscriptions";
	};

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose();
		selectedRecu = null; selectedSubscriber = null; fileIsGenerated = false; exportFileName = null;
		if(souscriptions != null) souscriptions.clear(); txtSearchClient = null; txtSearchCustId = null; urlSignature = null; sigSubscriber = null;
	}

	public void filterSouscriptions() {

		try {
			RestrictionsContainer rc = RestrictionsContainer.getInstance();
			
			// Ajout de la restriction sur la periode de date
			//*** if(txtDateDeb != null && txtDateFin != null && !txtDateDeb.isEmpty() && !txtDateFin.isEmpty()) rc.add(Restrictions.between("date", formater.parse(txtDateDeb.concat(" 00:01") ), formater.parse(txtDateFin.concat(" 23:58") ) ));
			if(txtDateDeb != null && txtDateFin != null) {
				Date deb = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(txtDateDeb) + " " + "00:00:00");
				Date fin = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(txtDateFin) + " " + "23:59:59");
				// Initialisation d'un conteneur de restrictions
				rc.add(Restrictions.between("date", deb, fin));
			}
						
			// Ajout de la restriction sur le nom du client
			if(txtSearchClient != null && !txtSearchClient.isEmpty()) rc.add(Restrictions.like("customerName", "%".concat(txtSearchClient).concat("%") ));

			// Ajout de la restriction sur le code du client
			if(txtSearchCustId != null && !txtSearchCustId.isEmpty()) rc.add(Restrictions.eq("customerId", txtSearchCustId ));

			// Initialisation d'un conteneur d'ordres
			OrderContainer orders = OrderContainer.getInstance().add(Order.desc("date")).add(Order.asc("customerName"));

			// Filtre des souscriptions
			souscriptions = OMViewHelper.appManager.filterSubscriptions(rc, orders); 

			//System.out.println("Nbre de souscriptions trouvees = " + souscriptions.size());

			// Initialisation du compteur
			num = 1;

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}
	
	
	
	public void filterModifications() {

		try {

			// Initialisation d'un conteneur de restrictions
			RestrictionsContainer rc = RestrictionsContainer.getInstance();
			
			rc.add(Restrictions.isNotNull("utiMod"));
			
			// Ajout de la restriction sur la periode de date
			
			if(txtDateDeb != null && txtDateFin != null) {
				Date deb = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(txtDateDeb) + " " + "00:00:00");
				Date fin = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(txtDateFin) + " " + "23:59:59");
				// Initialisation d'un conteneur de restrictions
				rc.add(Restrictions.between("dateMod", deb, fin));
			}

			// Ajout de la restriction sur le nom du client
			if(txtSearchClient != null && !txtSearchClient.isEmpty()) rc.add(Restrictions.like("customerName", "%".concat(txtSearchClient).concat("%") ));

			// Ajout de la restriction sur le code du client
			if(txtSearchCustId != null && !txtSearchCustId.isEmpty()) rc.add(Restrictions.eq("customerId", txtSearchCustId ));

			// Initialisation d'un conteneur d'ordres
			OrderContainer orders = OrderContainer.getInstance().add(Order.desc("dateMod")).add(Order.asc("customerName"));

			// Filtre des souscriptions
			souscriptions = OMViewHelper.appManager.filterSubscriptions(rc, orders); 

			System.out.println("Nbre de Modifications trouvees = " + souscriptions.size());

			// Initialisation du compteur
			num = 1;

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}



	public void filterAbonnesNonFactures() {

		try {

			// Filtre des souscriptions
			souscriptions = OMViewHelper.appManager.findAllSubscriberNonFactures(); 

			// Initialisation du compteur
			num = 1;

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}

	public void facturerAbonnesNonFactures() {

		try {

			// Filtre des souscriptions
			OMViewHelper.appManager.facturerListSubscribers(souscriptions); 

			// Initialisation du compteur
			num = 1;

			// Message d'information
			PortalInformationHelper.showInformationDialog("Facturation avec succès!", InformationDialog.DIALOG_SUCCESS);

		} catch(RemoteException e){
			e.printStackTrace();
			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);
		}catch(Exception e){
			e.printStackTrace();
			PortalExceptionHelper.threatException(e);

		}

	}


	/**
	 * Methode d'annulation du contrat de souscription
	 */
	public void annulerSouscription() {

		try {

			Subscriber subscriber = OMViewHelper.appDAOLocal.findByPrimaryKey(Subscriber.class,idAnnulation,null);

			String _return ="";
			if(subscriber.getStatus().equals(StatutContrat.SUSPENDU)){

				String msisdn = subscriber.getSubmsisdn();
				String txtbankPIN = OMTools.BIC+msisdn+DateFormatUtils.format(new Date(),"mmss");
				if(txtbankPIN.length() < 22){
					int v = 22-txtbankPIN.length();
					txtbankPIN = txtbankPIN+RandomStringUtils.randomAlphanumeric(v);
				}
				txtbankPIN = txtbankPIN.trim().toUpperCase();

				StringHolder alias = new StringHolder(txtbankPIN);
				short code_service = OMTools.All;
				String libelle = subscriber.getCustomerName().trim().length() > 25  ? subscriber.getCustomerName().trim().substring(0,25) : subscriber.getCustomerName().trim();
				String devise = OMTools.currency;
				String key = subscriber.getSubkey();
				String  active_date = DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");

				short val = OMViewHelper.register.ombRequest(msisdn, alias, code_service, libelle, devise, key, active_date);
				_return = String.valueOf(val);
			}else{
				StringHolder alias = new StringHolder(subscriber.getBankPIN());
				String  close_date = DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
				String orig = RequestInitiator.Bank.toString();
				String motif = RequestInitiator.Client.toString();
				_return = OMViewHelper.register.ombClose(alias, close_date, orig, motif);
			}

			if(_return.equalsIgnoreCase(OMTools.OK)){
				// Annulation
				OMViewHelper.appManager.annulerSouscription(idAnnulation,OMViewHelper.getSessionUser());
				for(Subscriber s : souscriptions) {
					if(s.getId().equals(idAnnulation)) {
						if(s.getStatus().equals(StatutContrat.SUSPENDU)){
							s.setStatus(StatutContrat.ACTIF);
							s.setHistoSubscriber((s.getHistoSubscriber()!=null ? s.getHistoSubscriber()+"_":"")+"ACTIVATION|"+OMViewHelper.getSessionUser().getLogin()+"|"+new SimpleDateFormat("ddMMyyHHmm").format(new Date())+"|"+"A" );

						}else {
							s.setStatus(StatutContrat.SUSPENDU);
							s.setHistoSubscriber((s.getHistoSubscriber()!=null ? s.getHistoSubscriber()+"_":"")+"SUSPENSION|"+OMViewHelper.getSessionUser().getLogin()+"|"+new SimpleDateFormat("ddMMyyHHmm").format(new Date())+"|"+"S" );
							String message = "Cher(e) client(e), votre abonnement au produit MAC ORANGE a ete suspendu. Rapprochez vous de votre agence pour plus d'informations.";
							try {
								OMViewHelper.appManager.alerteSMS(message.toUpperCase(), "237" + s.getSubmsisdn());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						break;
					}
				}
				// Message d'information
				PortalInformationHelper.showInformationDialog("Le contrat de souscription a été annulée avec succès!", InformationDialog.DIALOG_SUCCESS);
			}else{
				PortalInformationHelper.showInformationDialog(BankExceptionCode.mapCode.get(_return)+" !!", InformationDialog.DIALOG_ERROR);
				return;
			}

			urlSignature = null;

		} catch(RemoteException e){
			e.printStackTrace();
			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);
		}catch(Exception e){
			e.printStackTrace();
			PortalExceptionHelper.threatException(e);

		}

	}


	
	
	public void desactiverSouscription(){
		
		if(idDesactivation == null){
			// Message d'information
			PortalInformationHelper.showInformationDialog("Impossible d'effectuer cette operation. L'element choisi est vide !", InformationDialog.DIALOG_WARNING);
		}
		
		Subscriber subscriber = OMViewHelper.appDAOLocal.findByPrimaryKey(Subscriber.class,idDesactivation,null);
		if(subscriber.getStatus().equals(StatutContrat.ACTIF)){
			subscriber.setStatus(StatutContrat.WAITING);
			subscriber.setSuspensTemp((subscriber.getSuspensTemp()!=null ? subscriber.getSuspensTemp()+"_":"")+OMViewHelper.getSessionUser().getLogin()+"|"+new SimpleDateFormat("ddMMyyHHmm").format(new Date())+"|"+"W" );
			subscriber.setHistoSubscriber((subscriber.getHistoSubscriber()!=null ? subscriber.getHistoSubscriber()+"_":"")+"DESACTIVATION|"+OMViewHelper.getSessionUser().getLogin()+"|"+new SimpleDateFormat("ddMMyyHHmm").format(new Date())+"|"+"W" );
			OMViewHelper.appDAOLocal.update(subscriber);
			// Message d'information
			PortalInformationHelper.showInformationDialog("Souscription mise en attente avec succès!", InformationDialog.DIALOG_SUCCESS);
		}
		else{
			subscriber.setStatus(StatutContrat.ACTIF);
			subscriber.setSuspensTemp((subscriber.getSuspensTemp()!=null ? subscriber.getSuspensTemp()+"_":"")+OMViewHelper.getSessionUser().getLogin()+"|"+new SimpleDateFormat("ddMMyyHHmm").format(new Date())+"|"+"A" );
			subscriber.setHistoSubscriber((subscriber.getHistoSubscriber()!=null ? subscriber.getHistoSubscriber()+"_":"")+"ACTIVATION|"+OMViewHelper.getSessionUser().getLogin()+"|"+new SimpleDateFormat("ddMMyyHHmm").format(new Date())+"|"+"A" );

			OMViewHelper.appDAOLocal.update(subscriber);
			// Message d'information
			PortalInformationHelper.showInformationDialog("Suspension levée avec succès!", InformationDialog.DIALOG_SUCCESS);
		}

		
	}
	


	/**
	 * Consultation de la signature d'un client dans le core banking
	 */
	public void consultationSignatureCBS() {

		try {

			// Recherche de la signature du client
			urlSignature = OMViewHelper.appManager.getLienSig(sigSubscriber.getAccFact().split("-")[0], sigSubscriber.getFirstAccount().split("-")[1], "  ", sigSubscriber.getCustomerId(), new Date(), new SimpleDateFormat("HHmmss").format(new Date()), OMViewHelper.getSessionUser().getLogin());

			sigSubscriber.setConsulSig(true);

		} catch(Exception e){
			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}





	/**
	 * Imprime 
	 */
	public void printRecu() {

		try{

			Transaction transaction = OMViewHelper.appManager.findTransactionBySubscriber(selectedRecu.getId()) ;

			if(transaction == null) transaction = new Transaction(TypeOperation.SUBSCRIPTION, selectedRecu, 0d, selectedRecu.getAccounts().get(0), selectedRecu.getPhoneNumbers().get(0));

			List<Transaction> data = new ArrayList<Transaction>(); data.add(transaction);

			// Recuperation du visualisateur d'etats dans le FacesContext
			ReportViewerDialog viewer = (ReportViewerDialog) OMViewHelper.getSessionManagedBean("reportViewerDialog");

			// initialisation du visualisateur
			if(viewer != null) {

				// Lecture du Type mime du fichier a afficher
				viewer.setMimeType(WebResourceManager.mimes.get("pdf"));

				// Initialisation de la map des parametres de l'etat
				HashMap<Object, Object> map = new HashMap<Object, Object>();

				map.put("logoAFB", OMTools.getLogoAFB());
				map.put("logoOgMo", OMTools.getLogoOgMo());
				map.put("logo", OMTools.getLogoEntete());
				map.put("SUBREPORT_DIR", OMTools.getReportsDir());
				map.put("codeUser", OMViewHelper.getSessionUser().getLogin());

				// Lecture du flux de donnees
				viewer.setStreamData( OMTools.getReportPDFBytes( OMTools.getReportsDir().concat("recuSouscription.jasper"), map, data)  );
				
				// Ouverture du Visualisateur
				viewer.open();
			}

		}catch(Exception ex) {

			// Traitement de l'exception
			//PortalExceptionHelper.threatException(ex);
			ex.printStackTrace();

		}

	}



	public void exportListeExcell(){
		try {

			if(souscriptions.isEmpty()){
				PortalInformationHelper.showInformationDialog("Aucune souscription dans la liste !",InformationDialog.DIALOG_INFORMATION);
				return;
			}

			exportFileName = "LISTE_SOUSCRIPTIONS_"+new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date())+".xlsx";
			OMViewHelper.appManager.exportSouscriptionIntoExcelFile(souscriptions,exportFileName);

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
	public List<Subscriber> getSouscriptions() {
		return souscriptions;
	}

	/**
	 * @return the txtDateDeb
	 */
	public Date getTxtDateDeb() {
		return txtDateDeb;
	}

	/**
	 * @param txtDateDeb the txtDateDeb to set
	 */
	public void setTxtDateDeb(Date txtDateDeb) {
		this.txtDateDeb = txtDateDeb;
	}

	/**
	 * @return the txtDateFin
	 */
	public Date getTxtDateFin() {
		return txtDateFin;
	}

	/**
	 * @param txtDateFin the txtDateFin to set
	 */
	public void setTxtDateFin(Date txtDateFin) {
		this.txtDateFin = txtDateFin;
	}

	/**
	 * @return the txtSearchClient
	 */
	public String getTxtSearchClient() {
		return txtSearchClient;
	}

	/**
	 * @param txtSearchClient the txtSearchClient to set
	 */
	public void setTxtSearchClient(String txtSearchClient) {
		this.txtSearchClient = txtSearchClient;
	}

	/**
	 * @return the txtSearchCustId
	 */
	public String getTxtSearchCustId() {
		return txtSearchCustId;
	}

	/**
	 * @param txtSearchCustId the txtSearchCustId to set
	 */
	public void setTxtSearchCustId(String txtSearchCustId) {
		this.txtSearchCustId = txtSearchCustId;
	}

	/**
	 * @return the selectedSubscriber
	 */
	public Subscriber getSelectedSubscriber() {
		return selectedSubscriber;
	}

	/**
	 * @return the idAnnulation
	 */
	public Long getIdAnnulation() {
		return idAnnulation;
	}

	/**
	 * @param idAnnulation the idAnnulation to set
	 */
	public void setIdAnnulation(Long idAnnulation) {
		this.idAnnulation = idAnnulation;

		if(this.idAnnulation != null) annulerSouscription();

	}
	


	public Long getIdDesactivation() {
		return idDesactivation;
	}

	public void setIdDesactivation(Long idDesactivation) {
		this.idDesactivation = idDesactivation;

		if(this.idDesactivation != null) desactiverSouscription();
	}


	/**
	 * @return the selectedRecu
	 */
	public Subscriber getSelectedRecu() {
		return selectedRecu;
	}

	/**
	 * @param selectedRecu the selectedRecu to set
	 */
	public void setSelectedRecu(Subscriber selectedRecu) {
		this.selectedRecu = selectedRecu;

		if(this.selectedRecu != null) printRecu();

	}



	/**
	 * @return the sigSubscriber
	 */
	public Subscriber getSigSubscriber() {
		return sigSubscriber;
	}

	/**
	 * @param consulSubscriber the consulSubscriber to set
	 */
	public void setSigSubscriber(Subscriber sigSubscriber) {
		this.sigSubscriber = sigSubscriber;
		if(this.sigSubscriber != null) consultationSignatureCBS();
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

	/**
	 * @return the urlSignature
	 */
	public String getUrlSignature() {
		return urlSignature;
	}

	/**
	 * @param urlSignature the urlSignature to set
	 */
	public void setUrlSignature(String urlSignature) {
		this.urlSignature = urlSignature;
	}


}
