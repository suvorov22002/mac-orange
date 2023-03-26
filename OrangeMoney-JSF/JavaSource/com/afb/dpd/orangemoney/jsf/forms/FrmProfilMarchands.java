package com.afb.dpd.orangemoney.jsf.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.afb.dpd.orangemoney.jpa.entities.ProfilMarchands;
import com.afb.dpd.orangemoney.jsf.models.AbstractPortalForm;
import com.afb.dpd.orangemoney.jsf.models.InformationDialog;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.models.PortalInformationHelper;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;

/**
 * Formulaire de gestion des Profils Marchands
 * @author Yves FOKAM
 * @version 1.0
 */
public class FrmProfilMarchands extends AbstractPortalForm {

	/**
	 * Plage
	 */
	private ProfilMarchands plage;

	/**
	 * Plages
	 */
	List<ProfilMarchands> plages = new ArrayList<ProfilMarchands>();


	HashMap<Object, Object> mapProfils = new HashMap<Object, Object>();


	/**
	 * Default Constructor
	 */
	public FrmProfilMarchands() {}



	/**
	 * @return the plage
	 */
	public ProfilMarchands getPlage() {
		return plage;
	}



	/**
	 * @param plage the plage to set
	 */
	public void setPlage(ProfilMarchands plage) {
		this.plage = plage;
	}



	/**
	 * @return the plages
	 */
	public List<ProfilMarchands> getPlages() {
		return plages;
	}



	/**
	 * @param plages the plages to set
	 */
	public void setPlages(List<ProfilMarchands> plages) {
		this.plages = plages;
	}



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm() {

		// Appel Parent
		super.initForm();

		// Lecture des parametres
		plages = OMViewHelper.appManager.filterProfilMarchands();

		if(plages.isEmpty()){
			plages = new ArrayList<ProfilMarchands>();
			plages.add(new ProfilMarchands(ProfilMarchands.Default));
		}

		mapProfils = new HashMap<Object, Object>();
		for(ProfilMarchands p : plages){
			mapProfils.put(p.getProfilName().trim().toUpperCase(),p);
		}

	}


	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose();

		// Annulation des valeurs
		plages = null;  mapProfils.clear();
	}


	public void processAdd() {

		if(plages == null ) plages = new ArrayList<ProfilMarchands>();
		plages.add(new ProfilMarchands());
	}

	public void savePamareters() {

		try {

			for(ProfilMarchands prof : plages){

				if(StringUtils.isBlank(prof.getProfilName())){PortalInformationHelper.showInformationDialog("Veuillez saisir le nom pour un Marchand !",InformationDialog.DIALOG_INFORMATION); return ;}

				if(StringUtils.isBlank(prof.getMaxPullAmountTrans()) || Double.valueOf(prof.getMaxPullAmountTrans()) < 0){PortalInformationHelper.showInformationDialog("Veuillez saisir le montant Max valide du Pull pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				prof.setMaxPullAmount(Double.valueOf(prof.getMaxPullAmountTrans()));

				if(StringUtils.isBlank(prof.getMaxPushAmountTrans()) || Double.valueOf(prof.getMaxPushAmountTrans()) < 0){PortalInformationHelper.showInformationDialog("Veuillez saisir le montant Max valide du Push pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				prof.setMaxPushAmount(Double.valueOf(prof.getMaxPushAmountTrans()));


				if(StringUtils.isBlank(prof.getMaxPullAmountDayTrans()) || Double.valueOf(prof.getMaxPullAmountDayTrans()) < 0){PortalInformationHelper.showInformationDialog("Veuillez saisir le montant Max valide par jour du Pull pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				prof.setMaxPullAmountDay(Double.valueOf(prof.getMaxPullAmountDayTrans()));

				if(StringUtils.isBlank(prof.getMaxPushAmountDayTrans()) || Double.valueOf(prof.getMaxPushAmountDayTrans()) < 0){PortalInformationHelper.showInformationDialog("Veuillez saisir le montant Max valide par jour du Push pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				prof.setMaxPushAmountDay(Double.valueOf(prof.getMaxPushAmountDayTrans()));


				if(StringUtils.isBlank(prof.getMaxPullAmountWeekTrans()) || Double.valueOf(prof.getMaxPullAmountWeekTrans()) < 0){PortalInformationHelper.showInformationDialog("Veuillez saisir le montant Max valide par semaine du Pull pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				prof.setMaxPullAmountWeek(Double.valueOf(prof.getMaxPullAmountWeekTrans()));

				if(StringUtils.isBlank(prof.getMaxPushAmountWeekTrans()) || Double.valueOf(prof.getMaxPushAmountWeekTrans()) < 0){PortalInformationHelper.showInformationDialog("Veuillez saisir le montant Max valide par semaine du Push pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				prof.setMaxPushAmountWeek(Double.valueOf(prof.getMaxPushAmountWeekTrans()));


				if(StringUtils.isBlank(prof.getMaxPullAmountMonthTrans()) || Double.valueOf(prof.getMaxPullAmountMonthTrans()) < 0){PortalInformationHelper.showInformationDialog("Veuillez saisir le montant Max valide par mois du Pull pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				prof.setMaxPullAmountMonth(Double.valueOf(prof.getMaxPullAmountMonthTrans()));

				if(StringUtils.isBlank(prof.getMaxPushAmountMonthTrans()) || Double.valueOf(prof.getMaxPushAmountMonthTrans()) < 0){PortalInformationHelper.showInformationDialog("Veuillez saisir le montant Max valide par mois du Push pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				prof.setMaxPushAmountMonth(Double.valueOf(prof.getMaxPushAmountMonthTrans()));


				if(prof.getMaxPullAmount() >  prof.getMaxPullAmountDay()){PortalInformationHelper.showInformationDialog("Le montant max A2W par operation doit etre inferieur au  montant par jour  pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				if(prof.getMaxPullAmountDay() >  prof.getMaxPullAmountWeek()){PortalInformationHelper.showInformationDialog("Le montant max A2W par jour doit etre inferieur au  montant par semaine  pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				if(prof.getMaxPullAmountWeek() >  prof.getMaxPullAmountMonth()){PortalInformationHelper.showInformationDialog("Le montant max A2W par semaine doit etre inferieur au  montant par mois  pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}

				if(prof.getMaxPushAmount() >  prof.getMaxPushAmountDay()){PortalInformationHelper.showInformationDialog("Le montant max W2A par operation doit etre inferieur au  montant par jour  pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				if(prof.getMaxPushAmountDay() >  prof.getMaxPushAmountWeek()){PortalInformationHelper.showInformationDialog("Le montant max W2A par jour doit etre inferieur au  montant par semaine  pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				if(prof.getMaxPushAmountWeek() >  prof.getMaxPushAmountMonth()){PortalInformationHelper.showInformationDialog("Le montant max W2A par semaine doit etre inferieur au  montant par mois  pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}


				if(StringUtils.isBlank(prof.getCommissionsTrans()) || Double.valueOf(prof.getCommissionsTrans()) < 0){PortalInformationHelper.showInformationDialog("Veuillez saisir le montant des commissions pour le Marchand " + prof.getProfilName() + " !",InformationDialog.DIALOG_INFORMATION); return ;}
				prof.setCommissions(Double.valueOf(prof.getCommissionsTrans()));


				//Vérification des comptes
				if(!checkBonneSaisieCompte(prof.getNcpDAPPull())) return;
				if(!checkBonneSaisieCompte(prof.getNcpDAPPush())) return;
				if(!checkBonneSaisieCompte(prof.getNumCompteMTN())) return;
				
				
				/*
				if(StringUtils.isBlank(prof.getNcpDAPPull()) || prof.getNcpDAPPull().trim().length() != 20 || prof.getNcpDAPPull().split("-")[0].trim().length() != 5 || prof.getNcpDAPPull().split("-")[1].trim().length() != 11 || prof.getNcpDAPPull().split("-")[2].trim().length() != 2){
					// Msg Info
					PortalInformationHelper.showInformationDialog("Veuillez saisir un numéro de compte DAP A2W valide pour le Marchand: " + prof.getProfilName(), InformationDialog.DIALOG_WARNING);
					// Annulation
					return;
				} 
				if(StringUtils.isBlank(prof.getNcpDAPPush()) || prof.getNcpDAPPush().trim().length() != 20 || prof.getNcpDAPPush().split("-")[0].trim().length() != 5 || prof.getNcpDAPPush().split("-")[1].trim().length() != 11 || prof.getNcpDAPPush().split("-")[2].trim().length() != 2){
					// Msg Info
					PortalInformationHelper.showInformationDialog("Veuillez saisir un numéro de compte DAP W2A valide pour le Marchand: " + prof.getProfilName(), InformationDialog.DIALOG_WARNING);
					// Annulation
					return;
				}
				if(StringUtils.isBlank(prof.getNumCompteMTN()) || prof.getNumCompteMTN().trim().length() != 20 || prof.getNumCompteMTN().split("-")[0].trim().length() != 5 || prof.getNumCompteMTN().split("-")[1].trim().length() != 11 || prof.getNumCompteMTN().split("-")[2].trim().length() != 2){
					// Msg Info
					PortalInformationHelper.showInformationDialog("Veuillez saisir un numéro de compte Orange valide pour le Marchand: " + prof.getProfilName(), InformationDialog.DIALOG_WARNING);
					// Annulation
					return;
				}
				*/


				//*** ProfilMarchands profilMarchands = new ProfilMarchands ();
				ProfilMarchands profilMarchands = (ProfilMarchands) mapProfils.get(prof.getProfilName().trim().toUpperCase());
				if(prof.getId() == null && profilMarchands != null){
					// Msg Info
					PortalInformationHelper.showInformationDialog("Le libellé suivant a deja ete paramatre : " + prof.getProfilName(), InformationDialog.DIALOG_WARNING);
					// Annulation
					return;
				}

				mapProfils.put(prof.getProfilName().trim().toUpperCase(),prof);
				prof.setProfilName(prof.getProfilName().trim().toUpperCase());
			}

			// Enregistrement des parametres
			OMViewHelper.appManager.saveProfilMarchands(plages);

			// Message d'information
			PortalInformationHelper.showInformationDialog("Les parametres ont ete sauvegardés avec succes.", InformationDialog.DIALOG_SUCCESS);

		} catch(Exception e){

			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}



	private boolean checkBonneSaisieCompte(String numCompte) {

		try {

			if(StringUtils.isBlank(numCompte) || numCompte.trim().length() != 20 || numCompte.split("-")[0].trim().length() != 5 || numCompte.split("-")[1].trim().length() != 11 || numCompte.split("-")[2].trim().length() != 2){
				// Msg Info
				PortalInformationHelper.showInformationDialog("Numero de compte invalide : " + numCompte, InformationDialog.DIALOG_WARNING);
				// Annulation
				return false;
			}
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



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Paramétrage profils marchands";
	}





}
