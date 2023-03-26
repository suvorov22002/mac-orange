package com.afb.dpd.orangemoney.jsf.forms;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.faces.model.SelectItem;
import javax.xml.rpc.holders.StringHolder;

import org.apache.commons.lang3.StringUtils;
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
import com.btow.om.register.RequestInitiator;
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

import afb.dsi.dpd.portal.jpa.entities.User;
import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

/**
 * FrmValidSubscriber
 * @author Owner
 *
 */
public class FrmValidSubscriber extends AbstractPortalForm{


	private List<Subscriber> souscriptions = new ArrayList<Subscriber>();

	private List<SelectItem> itemsStatuts = new ArrayList<SelectItem>();

	private Date txtDateDeb = new Date();

	private Date txtDateFin = new Date();

	private String txtSearchClient;

	private String txtSearchCustId;

	private Subscriber selectedSubscriber = null;

	private Subscriber sigSubscriber = null;

	private Subscriber selectedRecu = null;

	private String urlSignature = null;

	private int num = 0;

	private Long idAnnulation;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH':'mm':'ss");

	

	/**
	 * Default Constructor
	 */
	public FrmValidSubscriber() {}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm() {
		// TODO Auto-generated method stub
		super.initForm();
		itemsStatuts = new ArrayList<SelectItem>();
		for(StatutContrat s : StatutContrat.getValues()){
			itemsStatuts.add(new SelectItem(s,s.getValue()));
		}
		
		txtDateDeb = new Date();
		txtDateFin = new Date();
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	public String getTitle() {
		return "Validation des souscriptions";
	};

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose();
		selectedRecu = null; selectedSubscriber = null; urlSignature = null; sigSubscriber = null;
		if(souscriptions != null) souscriptions.clear(); txtSearchClient = null; txtSearchCustId = null;
	}

	public void filterSouscriptions() {

		try {

			// Initialisation d'un conteneur de restrictions
			RestrictionsContainer rc = RestrictionsContainer.getInstance();

			User user = OMViewHelper.getSessionUser();
			if(!StringUtils.equalsIgnoreCase("00006",user.getBranch().getCode()) && !StringUtils.equalsIgnoreCase("00099",user.getBranch().getCode())){
				//*** rc.add(Restrictions.eq("age",user.getBranch().getCode())); 
				rc.add(Restrictions.eq("ageAbon",user.getBranch().getCode())); 
			}

			rc.add(Restrictions.eq("status", StatutContrat.WAITING ));

			// Ajout de la restriction sur la periode de date
			//if(txtDateDeb != null && txtDateFin != null && !txtDateDeb.isEmpty() && !txtDateFin.isEmpty()) rc.add(Restrictions.between("date", formater.parse(txtDateDeb.concat(" 00:01") ), formater.parse(txtDateFin.concat(" 23:58") ) ));
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

			// Ajout de la restriction sur l'utilisateur d'abonnement
			rc.add(Restrictions.ne("utiabon", user.getLogin() ));


			// Initialisation d'un conteneur d'ordres
			OrderContainer orders = OrderContainer.getInstance().add(Order.desc("date")).add(Order.asc("customerName"));

			// Filtre des souscriptions
			souscriptions = OMViewHelper.appManager.filterSubscriptions(rc, orders); 

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

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
		} 

	}


	/**
	 * Methode d'annulation du contrat de souscription
	 */
	public void annulerSouscription() {

		try {

			// Annulation
			OMViewHelper.appManager.annulerSouscription(idAnnulation,OMViewHelper.getSessionUser());

			// MAJ de l'objet correspondant dans la liste
			for(Subscriber s : souscriptions) {
				if(s.getId().equals(idAnnulation)) {
					s.setStatus(StatutContrat.SUSPENDU);
					String message = "Cher(e) client(e), votre abonnement au produit MAC ORANGE a ete suspendu. Rapprochez vous de votre agence pour plus d'informations.";
					try {
						OMViewHelper.appManager.alerteSMS(message.toUpperCase(), "237" + s.getSubmsisdn());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				}
			}

			// Message d'information
			PortalInformationHelper.showInformationDialog("Le contrat de souscription a été annulée avec succès!", InformationDialog.DIALOG_SUCCESS);

		} catch(Exception e) {

			// Traitement de l'exception
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
				map.put("logoMoMo", OMTools.getLogoOgMo());
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


	public void processSave() {

		try {

			// Filtre des souscriptions
			User user = OMViewHelper.getSessionUser();
			for(Subscriber s : souscriptions){
				if(s.isConsulSig() && StringUtils.isBlank(s.getUtiValid())) {
					s.setUtiValid(user.getLogin());
					s.setDateValid(new Date());
					s.setAgeMod(user.getBranch().getCode());
					s.setLibAgeMod(user.getBranch().getName());
					if(StatutContrat.ACTIF.equals(s.getStatus())) {
						String message = "Cher(e) client(e), votre abonnement au produit MAC ORANGE a ete validee avec succes";
						s.setHistoSubscriber((s.getHistoSubscriber()!=null ? s.getHistoSubscriber()+"_":"")+"ACTIVATION|"+OMViewHelper.getSessionUser().getLogin()+"|"+new SimpleDateFormat("ddMMyyHHmm").format(new Date())+"|"+"A" );

						try {
							OMViewHelper.appManager.alerteSMS(message.toUpperCase(), "237" + s.getSubmsisdn());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}
					else if(StatutContrat.SUSPENDU.equals(s.getStatus())) {

						// Annulation
						String _return ="";
						StringHolder alias = new StringHolder(s.getBankPIN());
						String  close_date = DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
						String orig = RequestInitiator.Bank.toString();
						String motif = RequestInitiator.Client.toString();
						_return = OMViewHelper.register.ombClose(alias, close_date, orig, motif);

						if(_return.equalsIgnoreCase(OMTools.OK)){

							String message = "Cher(e) client(e), votre abonnement au produit MAC ORANGE n'a pas ete validee. Rapprochez vous de votre agence pour plus d'informations.";
							s.setHistoSubscriber((s.getHistoSubscriber()!=null ? s.getHistoSubscriber()+"_":"")+"SUSPENSION|"+OMViewHelper.getSessionUser().getLogin()+"|"+new SimpleDateFormat("ddMMyyHHmm").format(new Date())+"|"+"S" );
							
							try {
								OMViewHelper.appManager.alerteSMS(message.toUpperCase(), "237" + s.getSubmsisdn());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
						}
					}
					OMViewHelper.appDAOLocal.update(s);
				}				
			}


			// Message d'information
			PortalInformationHelper.showInformationDialog("Enregistré avec succès !", InformationDialog.DIALOG_SUCCESS);

		} catch(Exception e) {

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
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
	 * @return the itemsStatuts
	 */
	public List<SelectItem> getItemsStatuts() {
		return itemsStatuts;
	}

	/**
	 * @param itemsStatuts the itemsStatuts to set
	 */
	public void setItemsStatuts(List<SelectItem> itemsStatuts) {
		this.itemsStatuts = itemsStatuts;
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

	/**
	 * @param selectedSubscriber the selectedSubscriber to set
	 */
	public void setSelectedSubscriber(Subscriber selectedSubscriber) {
		this.selectedSubscriber = selectedSubscriber;

		if(this.selectedSubscriber != null){

			try {

				// Envoi du PIN par SMS
				OMViewHelper.appManager.sendCodePINBySMS(this.selectedSubscriber);

				// Message d'information
				PortalInformationHelper.showInformationDialog("Le PIN Banque du client lui a été transféré par SMS!", InformationDialog.DIALOG_SUCCESS);

			} catch(Exception ex) {

				// Traitement de l'exception
				PortalExceptionHelper.threatException(ex);
			}

		}
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