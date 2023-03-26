/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.forms;

import java.io.File;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.xml.rpc.holders.StringHolder;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.afb.dpd.orangemoney.jpa.entities.Parameters;
import com.afb.dpd.orangemoney.jpa.entities.Subscriber;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jpa.enums.Langue;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jsf.models.AbstractPortalForm;
import com.afb.dpd.orangemoney.jsf.models.InformationDialog;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.models.PortalInformationHelper;
import com.afb.dpd.orangemoney.jsf.models.ReportViewerDialog;
import com.afb.dpd.orangemoney.jsf.servlet.WebResourceManager;
import com.afb.dpd.orangemoney.jsf.tools.OMTools;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.afb.dpd.orangemoney.jsf.tools.SelectData;
import com.banktowallet.b2w.b2wold.BankExceptionCode;

import afb.dsi.dpd.portal.jpa.entities.User;

/**
 * Formulaire de gestion de la Souscription au produit Orange Money
 * @author Francis KONCHOU
 * @version 1.0
 */
public class FrmSubscriber extends AbstractPortalForm {

	private Subscriber subscriber;

	private Parameters param;

	private String txtCustomerId;

	private String submsisdn;

	private String subkey;

	String urlSignature = null;

	private String txtbankPIN ;
	private int compteurOtp = 0;

	private boolean telephoneChecked = false;
	private boolean accountChecked = false;
	private boolean kycOrangeChecked = false;
	private boolean confirmOtp = false;
	private boolean  mailCompteDormant = false;


	private List<SelectItem> phoneItem = new ArrayList<SelectItem>();
	private List<SelectItem> accountItem = new ArrayList<SelectItem>();
	private List<SelectItem> langueItem = new ArrayList<SelectItem>();
	private List<SelectData> phones = new ArrayList<SelectData>();
	private List<SelectData> accounts = new ArrayList<SelectData>();

	private List<String> list = new ArrayList<String>();

	private String selectedAccount, selectedPhone, otp;

	/**
	 * Default Constructor
	 */
	public FrmSubscriber() {}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm() {

		// Appel Parent
		super.initForm();

		// Recuperation des parametres
		param = OMViewHelper.appManager.findParameters();
		telephoneChecked = false;
		accountChecked = false;
		kycOrangeChecked = false;

		selectedAccount = ""; 
		selectedPhone = "";

		compteurOtp = 0;
		confirmOtp = false;

		// Liste des langues
		langueItem = new ArrayList<SelectItem>();
		langueItem.add( new SelectItem(null, "----Choisir----") );
		for(Langue d : Langue.getValues()){
			langueItem.add( new SelectItem(d.name(), d.getValue()) );
		}

		String KEY_DIR = System.getProperty("jboss.server.data.dir", ".") + File.separator + "keystore";
		//***System.setProperty("javax.net.ssl.trustStore", KEY_DIR+"\\new_keystore.ks");
		System.setProperty("javax.net.ssl.trustStore", KEY_DIR+"\\keystore.ks");
		System.setProperty("javax.net.ssl.trustStorePassword", "b2wafriland");

		// Initialisation visuelle du formulaire
		clearForm();
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Souscription Orange Money";
	}



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose(){

		System.clearProperty("javax.net.ssl.trustStore");
		System.clearProperty("javax.net.ssl.trustStorePassword");

		// TODO Auto-generated method stub
		super.disposeResourcesOnClose();

		phoneItem.clear(); accountItem.clear(); param = null; selectedAccount = null; selectedPhone = null; 
		subscriber = null; txtCustomerId = null; urlSignature = null; telephoneChecked = false; accountChecked = false; kycOrangeChecked = false;
		list.clear(); otp = null; compteurOtp = 0; confirmOtp = false; langueItem.clear(); accounts.clear(); phones.clear();

	}

	/**
	 * Verifie si les pre-conditions sont respectees avant la validation de la souscription
	 * @return
	 */
	private boolean preconditionsOK() {

		// Si aucun compte n'a ete selectionne
		if( subscriber == null)  {

			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("Aucun client sélectionné. Impossible de valider la souscription", InformationDialog.DIALOG_WARNING);

			// Annulation
			return false;
		}


		// Si aucun compte n'a ete selectionne
		if( subscriber.getCustomerId() == null)  {

			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("Veuillez recherche un client par son code SVP!", InformationDialog.DIALOG_WARNING);

			// Annulation
			return false;
		}


		// Si aucun compte n'a ete selectionne
		if( subscriber.getAccounts().isEmpty() )  {

			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("Aucun compte n'a été sélectionné. Impossible de valider la souscription", InformationDialog.DIALOG_WARNING);

			// Annulation
			return false;
		}

		// Si le nbre de cpte selectionnes depasse le maximum parametre
		if( subscriber.getAccounts().size() > param.getMaxAccounts() )  {

			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("Le nombre maximum de comptes à fournir est de "+ param.getMaxAccounts() +". Impossible de poursuivre l'opération.", InformationDialog.DIALOG_WARNING);

			// Annulation
			return false;
		}

		// Si aucun numero de telephone n'a ete fourni
		if( subscriber.getPhoneNumbers().isEmpty() )  {

			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("Aucun numero de téléphone fournit. Impossible de valider la souscription.", InformationDialog.DIALOG_WARNING);

			// Annulation
			return false;
		}

		// Si aucun numero de telephone n'a ete fourni
		/**for(String s : subscriber.getPhoneNumbers()) if( !s.startsWith(OgMoHelper.PHONES_MASK) || s.length() != OgMoHelper.PHONES_LENGTH )  {

			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("Numéro de Téléphone Incorrect!.", InformationDialog.DIALOG_WARNING);

			// Annulation
			return false;
		}*/

		// Si le nbre de numeros de telephones fourni depasse le maximum parametre
		if( subscriber.getPhoneNumbers().size() > param.getMaxPhoneNumbers() )  {

			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("Le nombre maximum de numéros de téléphones autorisé est de "+ param.getMaxPhoneNumbers() +". Impossible de poursuivre l'opération", InformationDialog.DIALOG_WARNING);

			// Annulation
			return false;
		}

		// Si l'un des numeros de telephone fournit appatient deja a un client
		for(String s : subscriber.getPhoneNumbers()) {

			if(OMViewHelper.appManager.findSubscriberFromPhoneNumber(s) != null) {

				// Message d'avertissement
				PortalInformationHelper.showInformationDialog("le numéro de téléphone "+ s +" appartient déjà à un autre client.", InformationDialog.DIALOG_WARNING);

				// Annulation
				return false;

			}

		}
		

		return true;
	}

	/**
	 * Recuperation des num de tel et des comptes saisis sur le form de souscription
	 */
	private void readSubscriverInfos() {

		// Lecture du code client
		subscriber.setCustomerId( txtCustomerId );

		subscriber.getPhoneNumbers().clear(); subscriber.getAccounts().clear();

		// Si la liste des phones n'est pas vide
		System.out.println("---------phones.size() ------- : " + phones.size());
		/* ***
		if(phones != null && !phones.isEmpty()) {

			// Recuperation des n° de tel saisis
			for(SelectData sd : phones) if(sd.getValue() != null && !sd.getValue().trim().isEmpty() ) subscriber.getPhoneNumbers().add( sd.getValue().replaceAll(" ", "") );
		}
		*/

		// S'il existe au moins un compte
		// subscriber.getAccounts().clear();
		System.out.println("---------accounts.size() ------- : " + accounts.size());
		/* *** 
		if(accounts != null && !accounts.isEmpty()) {

			// Recuperation des n° de cpte selectionnes
			// System.out.println("----------------");
			for(SelectData sd : accounts){
				// System.out.println("------NO--------"+sd.getValue());
				if(sd.getChecked().equals(Boolean.TRUE)){
					// System.out.println("-------OK---------"+sd.getValue());
					subscriber.getAccounts().add(sd.getValue());
				}
			}

			if(subscriber.getAccounts().isEmpty() && accounts.size() == 1){
				for(SelectData sd : accounts){
					subscriber.getAccounts().add(sd.getValue());
				}
			}

		}
		*/
		
		if(StringUtils.isNotBlank(selectedAccount)) subscriber.getAccounts().add(selectedAccount);
		if(StringUtils.isNotBlank(selectedPhone)) subscriber.getPhoneNumbers().add("237" + selectedPhone);
	}


	public void checkSelectedAccount() {
		
		accountChecked = false;
		mailCompteDormant = false;
		
		if(StringUtils.isBlank(selectedAccount)) {
			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("Veuillez choisir un compte !", InformationDialog.DIALOG_WARNING);

			// Annulation
			return;
		}
		
		if(OMViewHelper.appManager.checkNewAccount(selectedAccount)) {
			
			// Recherche de la signature du client
			try {
				urlSignature = OMViewHelper.appManager.getLienSig(selectedAccount.split("-")[0], selectedAccount.split("-")[1], "  ", subscriber.getCustomerId(), new Date(), new SimpleDateFormat("HHmmss").format(new Date()), OMViewHelper.getSessionUser().getLogin());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			accountChecked = true;			 
			
		}
		else {
			
			boolean result = OMViewHelper.appManager.verifierCompteDormant(selectedAccount);
			if(result == Boolean.TRUE) {
				
				if(OMViewHelper.appManager.checkTransactionDay(selectedAccount)) {
					// Recherche de la signature du client
					try {
						urlSignature = OMViewHelper.appManager.getLienSig(selectedAccount.split("-")[0], selectedAccount.split("-")[1], "  ", subscriber.getCustomerId(), new Date(), new SimpleDateFormat("HHmmss").format(new Date()), OMViewHelper.getSessionUser().getLogin());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					accountChecked = true; 
					mailCompteDormant = true; 
				}
				else {
					accountChecked = false;
					try {
						String message = "Chers collegues, un abonnement au produit mac orange, est encours d'initiation sur le compte " + selectedAccount + ", en statut dormant. Merci d'avance pour votre vigilance !";
						OMViewHelper.appManager.alerteCompteDormant(OMViewHelper.getSessionUser(), selectedAccount, message);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	

					
					// Message d'avertissement
					PortalInformationHelper.showInformationDialog("Le compte " + selectedAccount + " est dormant !", InformationDialog.DIALOG_WARNING);

					// Annulation
					return;
				}
				
			}
			else {
				// Recherche de la signature du client
				try {
					urlSignature = OMViewHelper.appManager.getLienSig(selectedAccount.split("-")[0], selectedAccount.split("-")[1], "  ", subscriber.getCustomerId(), new Date(), new SimpleDateFormat("HHmmss").format(new Date()), OMViewHelper.getSessionUser().getLogin());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				accountChecked = true;
			}
						

			/*
			try {

				System.out.println("************ result ************* : " + result);
				if(result == Boolean.TRUE) {

					try {
						OMViewHelper.appManager.alerteCompteDormant(OMViewHelper.getSessionUser(), selectedAccount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	

					// Message d'avertissement
					PortalInformationHelper.showInformationDialog("Le compte " + selectedAccount + " est dormant !", InformationDialog.DIALOG_WARNING);

					// Annulation
					return;
				}

			}catch(Exception e){
				e.printStackTrace();
				// Affichage de l'exception
				PortalExceptionHelper.threatException(e);
			}
			
			if(result == Boolean.TRUE) return;

			// Recherche de la signature du client
			try {
				urlSignature = OMViewHelper.appManager.getLienSig(selectedAccount.split("-")[0], selectedAccount.split("-")[1], "  ", subscriber.getCustomerId(), new Date(), new SimpleDateFormat("HHmmss").format(new Date()), OMViewHelper.getSessionUser().getLogin());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			accountChecked = true;
			
			*/			
		}
		
	}

	
	/**
	 * Validation du numero de telephone du client dans le CBS
	 * @return
	 */
	@SuppressWarnings("unused")
	private Boolean isValidFormattedPhoneNumber(String s){
		
		List<String> mtn = Arrays.asList("67", "68", "650", "651", "652", "653", "654");
		
		Boolean trouve = false;
		if(s.length()==12 && s.startsWith("237")){
//			logger.info(s.substring(2, 5)+" - "+s.substring(2, 6));
			if(mtn.contains(s.substring(3, 5)) || mtn.contains(s.substring(3, 6)))
				trouve = true;
		}
		
		return trouve;
	}


	/**
	 * Enregistre la souscription du client au service 
	 */
	public void saveSouscription() {

		try {

			if(subscriber == null) throw new Exception("Client inexistant! Impossible d'effectuer l'opération");

			// Lit les infos de souscription saisies sur le formulaire
			readSubscriverInfos();

			// Si les preconditions ne sont pas respectees on annule l'operation
			if( !preconditionsOK() ){
				//subscriber.getPhoneNumbers().clear(); subscriber.getAccounts().clear();
				return;
			}
			
			if(kycOrangeChecked == false){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez identifier le client chez le partenaire !",InformationDialog.DIALOG_WARNING);
				// On retourne false
				return;
			}

			//Generation OTP
			String otp = "";
			try {
				otp = OMViewHelper.appManager.genererOTP("237" + selectedPhone);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("***************** saveSouscription otp **************** : " + otp);
			if(StringUtils.isBlank(otp)){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("L'OTP n'a pas ete genere !",InformationDialog.DIALOG_WARNING);
				// On retourne false
				return;
			}
			subscriber.setGeneratedOtp(otp);


			// Recuperation du formulaire de confirmation
			confirmOtp = true;
			list.add("Code confirmation abonnement : ");

		}catch(Exception e){
			e.printStackTrace();
			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}



	/**
	 * Methode de confirmation Abonnement
	 */
	public void confirmAbonnement() {

		//Vérification des informations
		if(list.isEmpty() || list.size() == 0){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Vous ne pouvez effectuer la confirmation de l'abonnement !",InformationDialog.DIALOG_WARNING);
			// On retourne false
			return;
		}

		if(compteurOtp == 3) {
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("La clé d'abonnement n'est plus valide, veuillez ressayer un autre abonnement !",InformationDialog.DIALOG_WARNING);
			otp = null; subscriber = null; clearForm();
			compteurOtp = 0; confirmOtp = true;
			// On retourne false
			return ;
		}

		//Vérification des informations
		if(StringUtils.isBlank(otp)){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez saisir l'OTP de confirmation de l'abonnement !",InformationDialog.DIALOG_WARNING);
			// On retourne false
			return;
		}

		if(!otp.equals(subscriber.getGeneratedOtp())) {
			compteurOtp++;
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("L'OTP saisi n'est pas valide !",InformationDialog.DIALOG_WARNING);
			// On retourne false
			return;
		}


		try {
			// Enregistrement dans la Plateforme Orange Money

			// Enregistrement des parametres BNKSXGWE
			//*** txtbankPIN = OMTools.BIC+submsisdn+DateFormatUtils.format(new Date(),"mmss");
			txtbankPIN = OMTools.BIC+submsisdn+otp;
			if(txtbankPIN.length() < OMTools.bankPINLength){
				int v = OMTools.bankPINLength - txtbankPIN.length();
				txtbankPIN = txtbankPIN+RandomStringUtils.randomNumeric(v);
			}
			txtbankPIN = txtbankPIN.trim().toUpperCase();

			String msisdn = subscriber.getSubmsisdn();
			StringHolder alias = new StringHolder(txtbankPIN);
			short code_service = OMTools.All;
			String libelle = subscriber.getCustomerName().trim().length() > 25  ? subscriber.getCustomerName().trim().substring(0,25) : subscriber.getCustomerName().trim();
			String devise = OMTools.currency;
			String key = subscriber.getSubkey();
			String  active_date = DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");

			short _return = OMViewHelper.register.ombRequest(msisdn, alias, code_service, libelle, devise, key, active_date);

			
			if(_return == 200 ){

				subscriber.setSubmsisdn(submsisdn);
				subscriber.setSubkey(subkey);
				subscriber.setSubstatus(substatus);
				subscriber.setSubfirstname(subfirstname);
				subscriber.setSublastname(sublastname);
				subscriber.setSubdob(subdob);
				subscriber.setSubcin(subcin);

				subscriber.setBankPIN(txtbankPIN);
				subscriber.setOgMoPIN(alias.value);
				User user = OMViewHelper.getSessionUser();
				subscriber.setUtiabon(user.getLogin());
				subscriber.setNameUtiAbon(user.getName());
				subscriber.setAgeAbon(user.getBranch().getCode());
				subscriber.setLibAgeAbone(user.getBranch().getName());
				subscriber.setHistoSubscriber((subscriber.getHistoSubscriber()!=null ? subscriber.getHistoSubscriber()+"_":"")+"ABONNEMENT|"+OMViewHelper.getSessionUser().getLogin()+"|"+new SimpleDateFormat("ddMMyyHHmm").format(new Date())+"|"+"W" );
				
				
				subscriber = OMViewHelper.appManager.saveSubscriber(subscriber);

				if(subscriber == null){
					// Message d'avertissement
					PortalInformationHelper.showInformationDialog("Le Solde du Compte est inférieur au montant de la Souscription ", InformationDialog.DIALOG_WARNING);
					return;
				}
				
				//Notification du client
				String message = "Cher(e) client(e), votre abonnement au produit MAC ORANGE est en attente de validation";
				try {
					OMViewHelper.appManager.alerteSMS(message.toUpperCase(), "237" + selectedPhone);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				//Notification abonnement compte dormant
				if(mailCompteDormant == true) {
					try {
						String msg = "Chers collegues, un abonnement au produit mac orange, a ete effectue sur le compte " + selectedAccount + ", en statut dormant. Merci d'avance pour votre vigilance pour la validation de cette souscription !";
						OMViewHelper.appManager.alerteCompteDormant(OMViewHelper.getSessionUser(), selectedAccount, msg);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// Message d'information
				PortalInformationHelper.showInformationDialog("Souscription effectuée avec succes.", InformationDialog.DIALOG_SUCCESS);

				// Impression du recu
				printRecu();

				// Reinitialisation du formulaire
				clearForm(); txtCustomerId = "";
				subscriber = null;
				subkey = "";
				submsisdn = "";
				substatus = "";
				sublastname = "";
				subfirstname = "";
				subdob = "";
				subcin = "";
				list = new ArrayList<String>();

			}else{
				//Notification du client
				String message = "Cher(e) client(e), votre initiative d'abonnement au produit MAC ORANGE a ete annulee";
				try {
					OMViewHelper.appManager.alerteSMS(message.toUpperCase(), "237" + selectedPhone);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

				PortalInformationHelper.showInformationDialog(BankExceptionCode.mapCode.get(String.valueOf(_return))+" !!", InformationDialog.DIALOG_ERROR);
				return;

			}			
			
			
			/* **** TESTS ****
			subscriber.setSubmsisdn(submsisdn);
			subscriber.setSubkey(subkey);
			subscriber.setSubstatus(substatus);
			subscriber.setSubfirstname(subfirstname);
			subscriber.setSublastname(sublastname);
			subscriber.setSubdob(subdob);
			subscriber.setSubcin(subcin);

			subscriber.setBankPIN(txtbankPIN);
			subscriber.setOgMoPIN(alias.value);
			User user = OMViewHelper.getSessionUser();
			subscriber.setUtiabon(user.getLogin());
			subscriber.setNameUtiAbon(user.getName());
			subscriber.setAgeAbon(user.getBranch().getCode());
			subscriber.setLibAgeAbone(user.getBranch().getName());
			subscriber = OMViewHelper.appManager.saveSubscriber(subscriber);

			if(subscriber == null){
				// Message d'avertissement
				PortalInformationHelper.showInformationDialog("Le Solde du Compte est inférieur au montant de la Souscription ", InformationDialog.DIALOG_WARNING);
				return;
			}
			
			//Notification du client
			String message = "Cher(e) client(e), votre abonnement au produit MAC ORANGE est en attente de validation";
			try {
				OMViewHelper.appManager.alerteSMS(message.toUpperCase(), "237" + selectedPhone);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			//Notification abonnement compte dormant
			System.out.println("****************** mailCompteDormant ***************** : " + mailCompteDormant);
			if(mailCompteDormant == true) {
				try {
					String msg = "Chers collegues, un abonnement au produit mac orange, a ete effectue sur le compte " + selectedAccount + ", en statut dormant. Merci d'avance pour votre vigilance pour la validation de cette souscription !";
					OMViewHelper.appManager.alerteCompteDormant(OMViewHelper.getSessionUser(), selectedAccount, msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// Message d'information
			PortalInformationHelper.showInformationDialog("Souscription effectuée avec succes.", InformationDialog.DIALOG_SUCCESS);

			// Impression du recu
			printRecu();

			// Reinitialisation du formulaire
			clearForm(); txtCustomerId = "";
			subscriber = null;
			subkey = "";
			submsisdn = "";
			substatus = "";
			sublastname = "";
			subfirstname = "";
			subdob = "";
			subcin = "";
			list = new ArrayList<String>();
			*/
			

		} catch(RemoteException e){
			e.printStackTrace();
			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);
		}catch(Exception e){
			e.printStackTrace();
			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);
		}


	}





	private String substatus;
	private String sublastname;
	private String subfirstname;
	private String subdob;
	private String subcin;

	/**
	 * Initialise les champs du formulaire
	 */
	private void clearForm() {

		//subscriber = null;

		// Initialisation des listes
		accountItem = new ArrayList<SelectItem>();
		phoneItem = new ArrayList<SelectItem>();
		urlSignature = null; compteurOtp = 0;  otp = null; confirmOtp = false;  kycOrangeChecked = false;
		selectedAccount = null; selectedPhone = null; subkey = null;  telephoneChecked = false;  accountChecked = false;	
		subscriber = new Subscriber();  list = new ArrayList<String>();  mailCompteDormant = false;
		// Initialisation de la liste des numeros de telephones a fournir
		//for(int i=1; i<=param.getMaxPhoneNumbers(); i++) phones.add( new SelectData("237") ); // subscriber.getPhoneNumbers().add("");

	}



	public void findSubscriberFromOmW(){

		try{
			
			phones.add( new SelectData("237"+selectedPhone));
			
			submsisdn = selectedPhone;
			//**** submsisdn = "7702100003";
			
			// Control s'il sagit d'une souscription existante
			//System.out.println(subkey+"-subkey----txtCustomerId----"+txtCustomerId);
			Subscriber subs = OMViewHelper.appManager.findSubscriber(txtCustomerId,subkey);
			//System.out.println("-----subscriber----"+subscriber);

			if(subs != null){
				// Message d'information
				PortalInformationHelper.showInformationDialog("Ce Client a déjà souscrit au produit !", InformationDialog.DIALOG_WARNING);
				//return;
			}

			String KEY_DIR = System.getProperty("jboss.server.data.dir", ".") + File.separator + "keystore";
			//System.setProperty("javax.net.ssl.trustStore", KEY_DIR+"\\keystore.ks");
			//System.setProperty("javax.net.ssl.trustStorePassword", "b2wafriland");

			if(submsisdn == null || submsisdn.trim().isEmpty()){
				// Message d'information
				PortalInformationHelper.showInformationDialog("Veuillez choisir le numero de telephone pour l'abonnement !!", InformationDialog.DIALOG_WARNING);
				return;
			}

			if(subkey == null || subkey.trim().isEmpty()){
				// Message d'information
				PortalInformationHelper.showInformationDialog("Saisir la key !!", InformationDialog.DIALOG_WARNING);
				return;
			}

			//if(subs == null) subscriber = new Subscriber();

			StringHolder status = new StringHolder();
			StringHolder lastname = new StringHolder();
			StringHolder firstname = new StringHolder();
			StringHolder dob = new StringHolder();
			StringHolder cin = new StringHolder();

			KEY_DIR = System.getProperty("jboss.server.data.dir", ".") + File.separator + "keystore";
			//System.setProperty("javax.net.ssl.trustStore", KEY_DIR+"\\keystore.ks");
			//System.setProperty("javax.net.ssl.trustStorePassword", "b2wafriland");

			//String javaHomePath = System.getProperty("java.home");
			//String keystore = javaHomePath + "\\lib\\security\\cacerts";
			//String storepass= "changeit";
			//String storetype= "JKS";	
			//System.getProperties().setProperty("javax.net.ssl.trustStore",KEY_DIR+"\\new_keystore.ks");
			System.getProperties().setProperty("javax.net.ssl.trustStore",KEY_DIR+"\\keystore.ks");
			//**** System.getProperties().setProperty("javax.net.ssl.trustStore",KEY_DIR+"\\sandbox.orange-money.com.ks");
			System.getProperties().setProperty("javax.net.ssl.trustStorePassword", "b2wafriland");
			//System.getProperties().setProperty("javax.net.ssl.keyStore",keystore);
			//System.getProperties().setProperty("javax.net.ssl.keyStorePassword",storepass);
			//System.getProperties().setProperty("javax.net.ssl.keyStoreType", storetype);

			OMViewHelper.register.KYCRequest(submsisdn.trim(), subkey.trim(), status, lastname, firstname, dob, cin);

			if(status.value != null){

				if(status.value.equalsIgnoreCase(OMTools.OK)){
					subscriber.setSubmsisdn(submsisdn);
					subscriber.setSubkey(subkey);
					subscriber.setSubstatus(status.value);
					subscriber.setSubfirstname(firstname.value);
					subscriber.setSublastname(lastname.value);
					subscriber.setSubdob(dob.value);
					subscriber.setSubcin(cin.value);

					substatus = status.value;
					sublastname = lastname.value;
					subfirstname = firstname.value;
					subdob = dob.value;
					subcin = cin.value;
					kycOrangeChecked = true;
				}else{
					PortalInformationHelper.showInformationDialog(BankExceptionCode.mapCode.get(status.value)+" !!", InformationDialog.DIALOG_ERROR);
					return;
				}

			}
			
			
			
			/* **** Tests ****
			subscriber.setSubmsisdn(submsisdn);
			subscriber.setSubkey(subkey);
			subscriber.setSubstatus("xxx");
			subscriber.setSubfirstname("xxx");
			subscriber.setSublastname("xxx");
			subscriber.setSubdob("xxx");
			subscriber.setSubcin("xxx");

			substatus = "xxx";
			sublastname = "xxx";
			subfirstname = "xxx";
			subdob = "xxx";
			subcin = "xxx";
			kycOrangeChecked = true;
			*/

		}catch(Exception e){
			e.printStackTrace();
			PortalExceptionHelper.threatException(e);

		}

	}


	/**
	 * Recherche un client dans le core banking a partir de son code
	 * @param customerDetail 
	 */
	public void findSubscriberFromCBS() {

		try {

			// Reinitialisation du formulaire
			clearForm();
			
			if(!StringUtils.isNotBlank(param.getTypeOpesDormant())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir les types d'operations à verifier pour les comptes dormants !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(param.getDelaiDormant())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le delai pour un compte en statut dormant !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(param.getDelaiNewAccount())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le delai pour un nouveau compte !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(param.getHostApi())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le host de l'API Manager !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(param.getProtocoleApi())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le protocole de l'API Manager !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(param.getPortApi())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le port de l'API Manager !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(param.getUrlSms())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir l'Url pour l'envoi de SMS !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(param.getUrlMail())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir l'Url pour l'envoi de Mail !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}

			// Recherche du client correspondant dans Amplitude
			//System.out.println("-----txtCustomerId----"+txtCustomerId);
			subscriber = OMViewHelper.appManager.findCustomerFromAmplitude(txtCustomerId);
			//System.out.println("-----subscriber----"+subscriber);

			if(subscriber != null){

				// Si le client possede au moins 1 compte
				if(subscriber.getAccounts() != null && !subscriber.getAccounts().isEmpty()){

					// Chargement des items de comptes
					accountItem.add(new SelectItem(null, "----Choisir----"));
					for(String s : subscriber.getAccounts()) {
						accountItem.add(new SelectItem(s, s));
						accounts.add( new SelectData( s) );
					}

					// Chargement des items de phoneNumbers
					phoneItem.add(new SelectItem(null, "----Choisir----"));
					for(String s : subscriber.getPhoneNumbers()) {
						phoneItem.add(new SelectItem(s, s));
					}

					subscriber.getAccounts().clear();
					subscriber.getPhoneNumbers().clear();

					subscriber.setSubmsisdn(submsisdn);
					subscriber.setSubkey(subkey);
					subscriber.setSubstatus(substatus);
					subscriber.setSubfirstname(subfirstname);
					subscriber.setSublastname(sublastname);
					subscriber.setSubdob(subdob);
					subscriber.setSubcin(subcin);


					if(subscriber.getPhoneNumbers() != null && !subscriber.getPhoneNumbers().isEmpty()){
						for(String s : subscriber.getPhoneNumbers()) phoneItem.add(new SelectItem(s.replace(" ", ""), s.replace(" ", "")));						
					}

					// Chargement des items de comptes
					if(subscriber.getAccounts() != null && !subscriber.getAccounts().isEmpty()){
						for(String s : subscriber.getAccounts()) accountItem.add(new SelectItem(s, s));
					}

				}

				//if(subscriber.getBankPIN() != null && !subscriber.getBankPIN().trim().isEmpty()) txtbankPIN = subscriber.getBankPIN();

			}else{
				// Message d'information
				PortalInformationHelper.showInformationDialog("Compte inexistant , Fermé ou en Contentieux!", InformationDialog.DIALOG_WARNING);
			}

		} catch(Exception e){
			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}



	/**
	 * Imprime la balance agee
	 */
	public void printRecu() {

		try{

			Transaction transaction = OMViewHelper.appManager.findTransactionBySubscriber(subscriber.getId()) ;

			if(transaction == null) transaction = new Transaction(TypeOperation.SUBSCRIPTION, subscriber, 0d, subscriber.getAccounts().get(0), subscriber.getPhoneNumbers().get(0));

			List<Transaction> data = new ArrayList<Transaction>(); data.add(transaction);

			// Recuperation du visualisateur d'etats dans le FacesContext
			ReportViewerDialog viewer = (ReportViewerDialog) OMViewHelper.getSessionManagedBean("reportViewerDialog");

			// initialisation du visualisateur
			if(viewer != null){

				// Lecture du Type mime du fichier a afficher
				viewer.setMimeType(WebResourceManager.mimes.get("pdf"));

				// Initialisation de la map des parametres de l'etat
				HashMap<Object, Object> map = new HashMap<Object, Object>();

				map.put("logoAFB", OMTools.getLogoAFB());
				map.put("logo", OMTools.getLogoEntete());
				map.put("logoOgMo", OMTools.getLogoOgMo());
				map.put("SUBREPORT_DIR", OMTools.getReportsDir());
				map.put("codeUser", OMViewHelper.getSessionUser().getLogin());

				// Lecture du flux de donnees
				viewer.setStreamData( OMTools.getReportPDFBytes( OMTools.getReportsDir().concat("recuSouscription.jasper"), map, data)  );
				viewer.buildReport(data, map);

				// Ouverture du Visualisateur
				viewer.open();
			}

		}catch(Exception ex) {

			// Traitement de l'exception
			//PortalExceptionHelper.threatException(ex);
			ex.printStackTrace();

		}

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
	 * @return the txtCustomerId
	 */
	public String getTxtCustomerId() {
		return txtCustomerId;
	}

	/**
	 * @param txtCustomerId the txtCustomerId to set
	 */
	public void setTxtCustomerId(String txtCustomerId) {
		this.txtCustomerId = txtCustomerId;
	}


	public String getTxtbankPIN() {
		return txtbankPIN;
	}

	public void setTxtbankPIN(String txtbankPIN) {
		if(txtbankPIN!= null && !txtbankPIN.trim().isEmpty())this.txtbankPIN = txtbankPIN;
	}



	/**
	 * @return the param
	 */
	public Parameters getParam() {
		return param;
	}

	/**
	 * @param param the param to set
	 */
	public void setParam(Parameters param) {
		this.param = param;
	}

	/**
	 * @return the submsisdn
	 */
	public String getSubmsisdn() {
		return submsisdn;
	}

	/**
	 * @param submsisdn the submsisdn to set
	 */
	public void setSubmsisdn(String submsisdn) {
		this.submsisdn = submsisdn;
	}

	/**
	 * @return the subkey
	 */
	public String getSubkey() {
		return subkey;
	}

	/**
	 * @param subkey the subkey to set
	 */
	public void setSubkey(String subkey){
		this.subkey = subkey;
	}

	/**
	 * @param urlSignature the urlSignature to set
	 */
	public void setUrlSignature(String urlSignature){
		this.urlSignature = urlSignature;
	}


	/**
	 * @return the accountItems
	 *
	public List<SelectItem> getAccountItems() {
		return accountItems;
	}*/



	public List<SelectItem> getPhoneItem() {
		return phoneItem;
	}



	public void setPhoneItem(List<SelectItem> phoneItem) {
		this.phoneItem = phoneItem;
	}



	public List<SelectItem> getAccountItem() {
		return accountItem;
	}



	public void setAccountItem(List<SelectItem> accountItem) {
		this.accountItem = accountItem;
	}


	/**
	 * 
	 * @return
	 */
	public String getUrlSignature() {
		return urlSignature;
	}

	/**
	 * @return the telephoneChecked
	 */
	public boolean isTelephoneChecked() {
		return telephoneChecked;
	}

	/**
	 * @param telephoneChecked the telephoneChecked to set
	 */
	public void setTelephoneChecked(boolean telephoneChecked) {
		this.telephoneChecked = telephoneChecked;
	}

	/**
	 * @return the accountChecked
	 */
	public boolean isAccountChecked() {
		return accountChecked;
	}

	/**
	 * @param accountChecked the accountChecked to set
	 */
	public void setAccountChecked(boolean accountChecked) {
		this.accountChecked = accountChecked;
	}

	/**
	 * @return the selectedAccount
	 */
	public String getSelectedAccount() {
		return selectedAccount;
	}

	/**
	 * @param selectedAccount the selectedAccount to set
	 */
	public void setSelectedAccount(String selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	public String getSelectedPhone() {
		return selectedPhone;
	}

	public void setSelectedPhone(String selectedPhone) {
		this.selectedPhone = selectedPhone;
		telephoneChecked = false;
		if(StringUtils.isBlank(this.selectedPhone)) return;
		if(StringUtils.isBlank(this.selectedPhone)) {
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Veuillez choisir un numero de telephone valide SVP !",InformationDialog.DIALOG_INFORMATION);
			// On retourne false
			return ;
		}
		telephoneChecked = true;
	}

	public List<String> getList() {
		return list;
	}


	public void setList(List<String> list) {
		this.list = list;
	}


	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}


	public boolean isConfirmOtp() {
		return confirmOtp;
	}

	public void setConfirmOtp(boolean confirmOtp) {
		this.confirmOtp = confirmOtp;
	}

	/**
	 * @return the compteurOtp
	 */
	public int getCompteurOtp() {
		return compteurOtp;
	}

	/**
	 * @param compteurOtp the compteurOtp to set
	 */
	public void setCompteurOtp(int compteurOtp) {
		this.compteurOtp = compteurOtp;
	}
	

	public List<SelectItem> getLangueItem() {
		return langueItem;
	}

	public void setLangueItem(List<SelectItem> langueItem) {
		this.langueItem = langueItem;
	}


	/**
	 * @param accounts the accounts to set
	 */
	public void setAccounts(List<SelectData> accounts) {
		this.accounts = accounts;
	}

	/**
	 * @return the phones
	 */
	public List<SelectData> getPhones() {
		return phones;
	}

	/**
	 * @param phones the phones to set
	 */
	public void setPhones(List<SelectData> phones) {
		this.phones = phones;
	}
	
}

