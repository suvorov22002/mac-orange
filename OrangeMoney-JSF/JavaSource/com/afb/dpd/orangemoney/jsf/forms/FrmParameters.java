/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.forms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;

import com.afb.dpd.orangemoney.jpa.entities.Parameters;
import com.afb.dpd.orangemoney.jpa.enums.ModeFacturation;
import com.afb.dpd.orangemoney.jpa.enums.Periodicite;
import com.afb.dpd.orangemoney.jpa.enums.TypeValeurFrais;
import com.afb.dpd.orangemoney.jpa.tools.TypeCompte;
import com.afb.dpd.orangemoney.jsf.models.AbstractPortalForm;
import com.afb.dpd.orangemoney.jsf.models.InformationDialog;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.models.PortalInformationHelper;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.afb.dpd.orangemoney.jsf.tools.SelectData;

/**
 * Formulaire de gestion des Parametres generaux
 * @author Francis KONCHOU
 * @version 1.0
 */
public class FrmParameters extends AbstractPortalForm {
	
	private Parameters params;
	
	private List<SelectData> types = new ArrayList<SelectData>();
	
	private List<SelectItem> typeValeurItems = new ArrayList<SelectItem>();
	private List<SelectItem> modeItems = new ArrayList<SelectItem>();
	private List<SelectItem> periodItems = new ArrayList<SelectItem>();
	
	/**
	 * Default Constructor
	 */
	public FrmParameters() {}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm() {
		
		//580
		
		// Appel Parent
		super.initForm();
				
		// Lecture des parametres
		params = OMViewHelper.appManager.consulterConfiguration();
		
		// Initialisation des types de comptes
		if(params.getAccountTypes() == null) params.setAccountTypes( new ArrayList<String>() );
		
		// Initialisation des commissions si elles n'esistent pas
		if(params.getCommissions() == null || params.getCommissions().isEmpty()) params.initCommissions();
		
		// Chargement de la liste des Types de comptes
		List<TypeCompte> accountTypes = OMViewHelper.appManager.filterTypeCompteFromAmplitude();
		
		if(accountTypes != null) {
			
			// Chargement des items de Types de comptes disponibles
			for(TypeCompte tc : accountTypes) types.add( new SelectData( (params.getAccountTypes().contains(tc.getCode()) ? Boolean.TRUE : Boolean.FALSE ), tc.getCode() + " : " + tc.getNom()) );
			
		}
		
		// Chargement des items de types de valeurs
		for(TypeValeurFrais to : TypeValeurFrais.getValues()) typeValeurItems.add( new SelectItem(to, to.getValue()) );
		
		// Items de modes de facturation
		modeItems.add( new SelectItem(null, " ") );
		for(ModeFacturation m : ModeFacturation.getValues()) modeItems.add( new SelectItem(m, m.getValue()) );
		
		// Items de Periodictes de facturation
		periodItems.add( new SelectItem(null, " ") );
		for(Periodicite p : Periodicite.getValues()) periodItems.add( new SelectItem(p, p.getValue()) );
		
		accountTypes.clear();
				
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Paramètres Généraux";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose();
		
		// Annulation des valeurs
		params = null; types.clear(); periodItems.clear(); modeItems.clear();
	}
	
	private void readSelectedTypes() {
		
		params.setAccountTypes( new ArrayList<String>() );
		
		for(SelectData sd : types) {
			if(sd.getChecked().booleanValue()) params.getAccountTypes().add( sd.getValue().split(" : ")[0].trim() );
		}
		
	}
	
	
	public void savePamareters() {
		
		try {
			
			// Check de la bonne saisie des numeros de compte
			if(!checkBonneSaisieCompte(params.getNcpCom())) return;
			//if(!checkBonneSaisieCompte(params.getNumCompteLiaison())) return;
			if(!checkBonneSaisieCompte(params.getNcpTVA())) return;
			if(!checkBonneSaisieCompte(params.getNcpOrange())) return;
			if(!checkBonneSaisieCompte(params.getNcpDAPAcountWalet())) return;
			if(!checkBonneSaisieCompte(params.getNcpDAPWaletAcount())) return;
			if(!checkBonneSaisieCompte(params.getNcpCliOrange())) return;
			
			
			if(StringUtils.isBlank(params.getMaxAWAmountTrans()) || Double.valueOf(params.getMaxAWAmountTrans()) < 0){
				PortalInformationHelper.showInformationDialog("Veuillez saisir le montant max valide du W2A !",InformationDialog.DIALOG_INFORMATION);
				return ;
			}
			params.setMaxAWAmount(Double.valueOf(params.getMaxAWAmountTrans()));
						
			if(StringUtils.isBlank(params.getMaxWAAmountTrans()) || Double.valueOf(params.getMaxWAAmountTrans()) < 0){
				PortalInformationHelper.showInformationDialog("Veuillez saisir le montant max valide du A2W !",InformationDialog.DIALOG_INFORMATION);
				return ;
			}
			params.setMaxWAAmount(Double.valueOf(params.getMaxWAAmountTrans()));
			
			
			if(StringUtils.isBlank(params.getMaxAWAmountDaysTrans()) || Double.valueOf(params.getMaxAWAmountDaysTrans()) < 0){
				PortalInformationHelper.showInformationDialog("Veuillez saisir le montant max valide par jour du A2W !",InformationDialog.DIALOG_INFORMATION);
				return ;
			}
			params.setMaxAWAmountDays(Double.valueOf(params.getMaxAWAmountDaysTrans()));
						
			if(StringUtils.isBlank(params.getMaxWAAmountDaysTrans()) || Double.valueOf(params.getMaxWAAmountDaysTrans()) < 0){
				PortalInformationHelper.showInformationDialog("Veuillez saisir le montant max valide par jour du W2A !",InformationDialog.DIALOG_INFORMATION);
				return ;
			}
			params.setMaxWAAmountDays(Double.valueOf(params.getMaxWAAmountDaysTrans()));
			
			
			if(StringUtils.isBlank(params.getMaxAWAmountWeeksTrans()) || Double.valueOf(params.getMaxAWAmountWeeksTrans()) < 0){
				PortalInformationHelper.showInformationDialog("Veuillez saisir le montant max valide par semaine du A2W !",InformationDialog.DIALOG_INFORMATION);
				return ;
			}
			params.setMaxAWAmountWeeks(Double.valueOf(params.getMaxAWAmountWeeksTrans()));
						
			if(StringUtils.isBlank(params.getMaxWAAmountWeeksTrans()) || Double.valueOf(params.getMaxWAAmountWeeksTrans()) < 0){
				PortalInformationHelper.showInformationDialog("Veuillez saisir le montant max valide par semaine du W2A !",InformationDialog.DIALOG_INFORMATION);
				return ;
			}
			params.setMaxWAAmountWeeks(Double.valueOf(params.getMaxWAAmountWeeksTrans()));
			
			
			if(StringUtils.isBlank(params.getMaxAWAmountMonthsTrans()) || Double.valueOf(params.getMaxAWAmountMonthsTrans()) < 0){
				PortalInformationHelper.showInformationDialog("Veuillez saisir le montant max valide par mois du A2W !",InformationDialog.DIALOG_INFORMATION);
				return ;
			}
			params.setMaxAWAmountMonths(Double.valueOf(params.getMaxAWAmountMonthsTrans()));
						
			if(StringUtils.isBlank(params.getMaxWAAmountMonthsTrans()) || Double.valueOf(params.getMaxWAAmountMonthsTrans()) < 0){
				PortalInformationHelper.showInformationDialog("Veuillez saisir le montant max valide par mois du W2A !",InformationDialog.DIALOG_INFORMATION);
				return ;
			}
			params.setMaxWAAmountMonths(Double.valueOf(params.getMaxWAAmountMonthsTrans()));
			
			
			if(params.getMaxWAAmount() >  params.getMaxWAAmountDays()){PortalInformationHelper.showInformationDialog("Le montant max W2A par operation doit etre inferieur au  montant par jour !",InformationDialog.DIALOG_INFORMATION); return ;}
			if(params.getMaxWAAmountDays() >  params.getMaxWAAmountWeeks()){PortalInformationHelper.showInformationDialog("Le montant max W2A par jour doit etre inferieur au  montant par semaine !",InformationDialog.DIALOG_INFORMATION); return ;}
			if(params.getMaxWAAmountWeeks() >  params.getMaxWAAmountMonths()){PortalInformationHelper.showInformationDialog("Le montant max W2A par semaine doit etre inferieur au  montant par mois !",InformationDialog.DIALOG_INFORMATION); return ;}
			
			if(params.getMaxAWAmount() >  params.getMaxAWAmountDays()){PortalInformationHelper.showInformationDialog("Le montant max A2W par operation doit etre inferieur au  montant par jour !",InformationDialog.DIALOG_INFORMATION); return ;}
			if(params.getMaxAWAmountDays() >  params.getMaxAWAmountWeeks()){PortalInformationHelper.showInformationDialog("Le montant max A2W par jour doit etre inferieur au  montant par semaine !",InformationDialog.DIALOG_INFORMATION); return ;}
			if(params.getMaxAWAmountWeeks() >  params.getMaxAWAmountMonths()){PortalInformationHelper.showInformationDialog("Le montant max A2W par semaine doit etre inferieur au  montant par mois !",InformationDialog.DIALOG_INFORMATION); return ;}
			
			
			if(StringUtils.isBlank(params.getEmail_sav())){
				// Msg Info
				PortalInformationHelper.showInformationDialog("Veuillez saisir une adresse mail pour l'envoi de mail SVP", InformationDialog.DIALOG_WARNING);
				// Annulation
				return;
			}
			
			/*
			
			
			if(StringUtils.isBlank(params.getSmtpServerName())){
				// Msg Info
				PortalInformationHelper.showInformationDialog("Veuillez définir un serveur pour l'envoi de mail SVP", InformationDialog.DIALOG_WARNING);
				// Annulation
				return;
			}
			
			if(StringUtils.isBlank(params.getPortEnvoiMail())){
				// Msg Info
				PortalInformationHelper.showInformationDialog("Veuillez définir un port pour l'envoi de mail SVP", InformationDialog.DIALOG_WARNING);
				// Annulation
				return;
			}
			
			if(StringUtils.isBlank(params.getPwd_sav())){
				// Msg Info
				PortalInformationHelper.showInformationDialog("Veuillez définir un mot de passe pour l'envoi de mail SVP", InformationDialog.DIALOG_WARNING);
				// Annulation
				return;
			}
			*/
			
			
			if(!StringUtils.isNotBlank(params.getCodeOpe())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le code opération pour les transactions !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getCodeOpeCompt())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le code opération pour la comptabilisation !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			
			
			
			if(!StringUtils.isNotBlank(params.getIpAdressAmpli())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir l'adresse IP du serveur Amplitude !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getPortServerAmpli())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le port du serveur Amplitude !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getUserLoginServerAmpli())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le login de l'utilisateur du serveur Amplitude !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getUserPasswordServerAmpli())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le mot de passe de l'utilisateur du serveur Amplitude !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			
			
			if(!StringUtils.isNotBlank(params.getFilePathAmpli())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le repertoire de fichier Amplitude !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getFileNameAmpli())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le nom de fichier Amplitude !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(params.getFileNameAmpli().trim().length() > 15){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Le nombre de cartactère du nom de fichier Amplitude est superieur à 15 !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getFileExtensionAmpli())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir l'extension de fichier Amplitude !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getCheminSaveFile())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez définir le repertoire de sauvegarde des fichiers du TFJO !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			File rep = new File(params.getCheminSaveFile());
			if (!rep.isDirectory()) {
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Le repertoire de sauvegarde des fichiers du TFJO n'existe pas !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			Pattern pattern = Pattern.compile("\\p{Punct}");
			Matcher matcher = pattern.matcher(params.getFileNameAmpli());
			boolean res = matcher.find();
			if(res == true){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Presence de caracteres speciaux dans le libelle du fichier a integrer !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			Matcher matcher2 = pattern.matcher(params.getFileExtensionAmpli());
			res = matcher2.find();
			if(res == true){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Presence de caracteres speciaux dans le libelle de l'extension !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getOppositions())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir les codes opposition pour les transactions en sens crédit !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			
			
			if(!StringUtils.isNotBlank(params.getTypeOpesDormant())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir les types d'operations à verifier pour les comptes dormants !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getDelaiDormant())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le delai pour un compte en statut dormant !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getDelaiNewAccount())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le delai pour un nouveau compte !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			
			if(!StringUtils.isNotBlank(params.getHostApi())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le host de l'API Manager !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getProtocoleApi())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le protocole de l'API Manager !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getPortApi())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le port de l'API Manager !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getUrlSms())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir l'Url pour l'envoi de SMS !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getUrlMail())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir l'Url pour l'envoi de Mail !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getUrlPackage())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir l'Url pour la gestion des packages !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(!StringUtils.isNotBlank(params.getUrlCbsApi())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir l'Url des APIs du CBS !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			
			if(StringUtils.isBlank(params.getHeureVerifResiliation())){
				// Msg Info
				PortalInformationHelper.showInformationDialog("Veuillez saisir les horaires de verifications des resiliations SVP", InformationDialog.DIALOG_WARNING);
				// Annulation
				return;
			}
			
			if(!StringUtils.isNotBlank(params.getSeuilReconciliation())){
				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Veuillez saisir le seuil de réconciliation !",InformationDialog.DIALOG_INFORMATION);
				// On retourne false
				return ;
			}
			
			if(StringUtils.isBlank(params.getLotTransactions())){
				params.setLotTransactions("3000");
			}
			
			
			
			// Recupere les types de compte selectionnes
			readSelectedTypes();
			
			// Enregistrement des parametres
			OMViewHelper.appManager.saveParameters(params);
			
			// Message d'information
			PortalInformationHelper.showInformationDialog("Les parametres ont ete sauvegardés avec succes.", InformationDialog.DIALOG_SUCCESS);
			
		} catch(Exception e){
			
			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);
			
		}
		
	}
	
	private boolean checkBonneSaisieCompte(String numCompte) {
		
		try {
			
			if(numCompte.split("-").length != 3) {
				PortalInformationHelper.showInformationDialog("Format de numero de compte incorrect (Format = xxxxx-xxxxxxxxxxx-xx)", InformationDialog.DIALOG_WARNING);
				return false;
			}
			
			if(OMViewHelper.appManager.isCompteFerme(numCompte)) {
				PortalInformationHelper.showInformationDialog("Compte " + numCompte + " inexistant ou en instance de fermeture!", InformationDialog.DIALOG_WARNING);
				return false;
			}
			
		} catch(Exception e) {
			PortalInformationHelper.showInformationDialog("Format de numero de compte incorrect (Format = xxxxx-xxxxxxxxxxx-xx)", InformationDialog.DIALOG_WARNING);
			return false;
		}
		
		return true;
		
	}
	
	
	/**
	 * @return the params
	 */
	public Parameters getParams() {
		return params;
	}

	/**
	 * 
	 * @return
	 */
	public List<SelectData> getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(List<SelectData> types) {
		this.types = types;
	}

	/**
	 * @return the typeValeurItems
	 */
	public List<SelectItem> getTypeValeurItems() {
		return typeValeurItems;
	}

	/**
	 * @return the modeItems
	 */
	public List<SelectItem> getModeItems() {
		return modeItems;
	}

	/**
	 * @return the periodItems
	 */
	public List<SelectItem> getPeriodItems() {
		return periodItems;
	}
	
	
}
