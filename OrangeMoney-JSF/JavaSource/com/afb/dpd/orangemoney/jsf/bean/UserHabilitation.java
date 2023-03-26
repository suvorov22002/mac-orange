/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.bean;

import java.util.ArrayList;
import java.util.List;

import afb.dsi.dpd.portal.jpa.entities.Role;
import afb.dsi.dpd.portal.jpa.entities.User;

import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;

/**
 * Classe de gestion des habilitations visuelles de l'utilisateur connecte
 * @author Francis K
 * @version 1.0
 */
public class UserHabilitation {
	
	/**
	 * Liste des roles de l'utilisateur sur le module
	 */
	private List<Role> roles = new ArrayList<Role>();
	
	// false
	private boolean btnValiderSouscriptionEnabled = false;
	private boolean btnModifierSouscriptionEnabled = false;
	private boolean btnConfimerSouscriptionEnabled = false;
	private boolean btnModifierPINEnabled = false;
	private boolean btnConsulterSouscriptionsEnabled = false;
	private boolean btnAnnulerSouscriptionEnabled = false;
	private boolean btnTransferPINToCustomerEnabled = false;
	
	private boolean btnConsulterTransactionsEnabled = false;
	private boolean btnExportECIntoFileEnabled = false;
	private boolean btnConsulterECEnabled = false;
	private boolean btnPosterECIntoDeltaEnabled = false;
	private boolean btnArchiverTransacEnabled = false;
	private boolean btnPurgerTransacEnabled = false;
	private boolean btnEffectuerSimulationEnabled = false;
	
	private boolean btnConsulterConfigurationEnabled = false;
	private boolean btnModifierConfigurationEnabled = false;
	
	private boolean btnFilterUSSDTransactionEnabled = false;
	private boolean btnCompleteTransactionsEnabled = false;
	private boolean btnExecuteReconciliationEnabled = false;
	
	private boolean btnExecuterTFJOEnabled = false;
	private boolean btnValiderTFJOEnabled = false;
	private boolean btnExportTFJOExcelEnabled = false;
	
	private boolean btnProfilSouscriptionEnabled = false;
	
	private boolean btnSaveBranchMailsEnabled = false;
	private boolean btnUpdateBranchMailsEnabled = false;
	private boolean btnDeleteBranchMailsEnabled = false;
	
	private boolean btnExecuteRapprochementEnabled = false;
	private boolean btnSuiviRobotEnabled = false;
	
	
	/**
	 * Default Constructor
	 */
	public UserHabilitation() {
		
		try {
			
			// Recuperation de l'utilisateur connecte dans la session
			User connected = OMViewHelper.getSessionUser();
			
			// Recuperation des roles de l'utilisateur pour le module
			roles = OMViewHelper.portalFacadeManager.filterUserRoleFromModule(connected.getId(), "OgMo-01");
			
			// Si l'utilisateur possede des roles sur le module
			if(roles != null && !roles.isEmpty()) {
			
				// Parcours des roles de l'utilisateur
				for(Role r : roles) {
					
					if(r.getName().equals("saveSubscriber")) btnValiderSouscriptionEnabled = true;
					else if(r.getName().equals("activerSubscriptions")) btnConfimerSouscriptionEnabled = true;
					else if(r.getName().equals("updateBankPIN")) btnModifierPINEnabled = true;
					else if(r.getName().equals("updateSubscriber")) btnModifierSouscriptionEnabled = true;
					else if(r.getName().equals("filterSubscriptions")) btnConsulterSouscriptionsEnabled = true;
					else if(r.getName().equals("annulerSouscription")) btnAnnulerSouscriptionEnabled = true;
					else if(r.getName().equals("sendCodePINBySMS")) btnTransferPINToCustomerEnabled = true;
					
					else if(r.getName().equals("filterTransactions")) btnConsulterTransactionsEnabled = true;
					else if(r.getName().equals("completeTransactions")) btnCompleteTransactionsEnabled = true;
					else if(r.getName().equals("extractECFromSelectedTransactionsIntoFile")) btnExportECIntoFileEnabled = true;
					else if(r.getName().equals("getECFromTransactions")) btnConsulterECEnabled = true;
					else if(r.getName().equals("posterTransactionsDansCoreBanking")) btnPosterECIntoDeltaEnabled = true;
					else if(r.getName().equals("archiverTransactions")) btnArchiverTransacEnabled = true;
					else if(r.getName().equals("purgerTransactions")) btnPurgerTransacEnabled = true;
					
					else if(r.getName().equals("consulterConfiguration")) btnConsulterConfigurationEnabled = true;
					else if(r.getName().equals("saveParameters")) btnModifierConfigurationEnabled = true;
					else if(r.getName().equals("filterUSSDTransactions")) btnFilterUSSDTransactionEnabled = true;
					else if(r.getName().equals("executerReconciliation")) btnExecuteReconciliationEnabled = true;
					
					else if(r.getName().equals("executerTFJO")) btnExecuterTFJOEnabled = true;
					else if(r.getName().equals("validerTFJO")) btnValiderTFJOEnabled = true;
					else if(r.getName().equals("exportComptabilisationIntoExcelFile")) btnExportTFJOExcelEnabled = true;
					else if(r.getName().equals("executerSimulation")) btnEffectuerSimulationEnabled = true;
					
					else if(r.getName().equals("OgMo-saveBranchMails")) btnSaveBranchMailsEnabled = true;
					else if(r.getName().equals("OgMo-updateBranchMails")) btnUpdateBranchMailsEnabled = true;
					else if(r.getName().equals("OgMo-deleteBranchMails")) btnDeleteBranchMailsEnabled = true;
										
					else if(r.getName().equals("profilSubscriptions")) btnProfilSouscriptionEnabled = true;   
					else if(r.getName().equals("executeRapprochement")) btnExecuteRapprochementEnabled = true;
					
					else if(r.getName().equals("suiviRobot")) btnSuiviRobotEnabled = true;
					
					
				}
				
			}
			
		} catch(Exception e){ e.printStackTrace(); }
		
	}

	/**
	 * @return the btnConfimerSouscriptionEnabled
	 */
	public boolean isBtnConfimerSouscriptionEnabled() {
		return btnConfimerSouscriptionEnabled;
	}

	public boolean isMenuPullPushEnabled() {
		return btnValiderSouscriptionEnabled || btnModifierSouscriptionEnabled || btnModifierPINEnabled || btnConsulterSouscriptionsEnabled || btnAnnulerSouscriptionEnabled || btnTransferPINToCustomerEnabled;
	}

	public boolean isMenuTraitementEnabled(){
		return btnConsulterTransactionsEnabled || btnExportECIntoFileEnabled || btnConsulterECEnabled || btnPosterECIntoDeltaEnabled || btnArchiverTransacEnabled || btnPurgerTransacEnabled || btnEffectuerSimulationEnabled || btnSuiviRobotEnabled;
	}
	
	public boolean isMenuConfigsEnabled(){
		return btnConsulterConfigurationEnabled || btnModifierConfigurationEnabled;
	}
	
	public boolean isMenuRapprochementEnabled(){
		return btnExecuteRapprochementEnabled;
	}

	public boolean isMenuSouscriptionEnabled() {
		return btnValiderSouscriptionEnabled;
	}

	public boolean isMenuModifierContratEnabled() {
		return btnModifierSouscriptionEnabled || btnModifierPINEnabled;
	}

	public boolean isMenuStatsSouscriptionsEnabled() {
		return btnConsulterSouscriptionsEnabled || btnAnnulerSouscriptionEnabled || btnTransferPINToCustomerEnabled;
	}

	public boolean isMenuSimulationEnabled(){
		return btnEffectuerSimulationEnabled;
	}

	public boolean isMenuStatsTransactionsEnabled(){
		return btnConsulterTransactionsEnabled || btnExportECIntoFileEnabled || btnConsulterECEnabled || btnPosterECIntoDeltaEnabled || btnArchiverTransacEnabled || btnPurgerTransacEnabled;
	}

	/**
	 * @return the btnValiderSouscriptionEnabled
	 */
	public boolean isBtnValiderSouscriptionEnabled() {
		return btnValiderSouscriptionEnabled;
	}

	/**
	 * @return the btnModifierSouscriptionEnabled
	 */
	public boolean isBtnModifierSouscriptionEnabled() {
		return btnModifierSouscriptionEnabled;
	}

	/**
	 * @return the btnModifierPINEnabled
	 */
	public boolean isBtnModifierPINEnabled() {
		return btnModifierPINEnabled;
	}

	/**
	 * @return the btnConsulterSouscriptionsEnabled
	 */
	public boolean isBtnConsulterSouscriptionsEnabled() {
		return btnConsulterSouscriptionsEnabled;
	}

	/**
	 * @return the btnAnnulerSouscriptionEnabled
	 */
	public boolean isBtnAnnulerSouscriptionEnabled() {
		return btnAnnulerSouscriptionEnabled;
	}

	/**
	 * @return the btnTransferPINToCustomerEnabled
	 */
	public boolean isBtnTransferPINToCustomerEnabled() {
		return btnTransferPINToCustomerEnabled;
	}

	/**
	 * @return the btnConsulterTransactionsEnabled
	 */
	public boolean isBtnConsulterTransactionsEnabled() {
		return btnConsulterTransactionsEnabled;
	}

	/**
	 * @return the btnExportECIntoFileEnabled
	 */
	public boolean isBtnExportECIntoFileEnabled() {
		return btnExportECIntoFileEnabled;
	}

	/**
	 * @return the btnConsulterECEnabled
	 */
	public boolean isBtnConsulterECEnabled() {
		return btnConsulterECEnabled;
	}

	/**
	 * @return the btnPosterECIntoDeltaEnabled
	 */
	public boolean isBtnPosterECIntoDeltaEnabled() {
		return btnPosterECIntoDeltaEnabled;
	}

	/**
	 * @return the btnArchiverTransacEnabled
	 */
	public boolean isBtnArchiverTransacEnabled() {
		return btnArchiverTransacEnabled;
	}

	/**
	 * @return the btnPurgerTransacEnabled
	 */
	public boolean isBtnPurgerTransacEnabled() {
		return btnPurgerTransacEnabled;
	}

	/**
	 * @return the btnEffectuerSimulationEnabled
	 */
	public boolean isBtnEffectuerSimulationEnabled() {
		return btnEffectuerSimulationEnabled;
	}

	/**
	 * @return the btnConsulterConfigurationEnabled
	 */
	public boolean isBtnConsulterConfigurationEnabled() {
		return btnConsulterConfigurationEnabled;
	}

	/**
	 * @return the btnModifierConfigurationEnabled
	 */
	public boolean isBtnModifierConfigurationEnabled() {
		return btnModifierConfigurationEnabled;
	}

	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @return the btnFilterUSSDTransactionEnabled
	 */
	public boolean isBtnFilterUSSDTransactionEnabled() {
		return btnFilterUSSDTransactionEnabled;
	}

	/**
	 * @return the btnExecuteReconciliationEnabled
	 */
	public boolean isBtnExecuteReconciliationEnabled() {
		return btnExecuteReconciliationEnabled;
	}
	
	public boolean isMenuUSSDTransEnabled(){
		return btnFilterUSSDTransactionEnabled || btnExecuteReconciliationEnabled || btnConsulterTransactionsEnabled;
	}

	/**
	 * @return the btnExecuterTFJOEnabled
	 */
	public boolean isBtnExecuterTFJOEnabled() {
		return btnExecuterTFJOEnabled;
	}

	/**
	 * @return the btnValiderTFJOEnabled
	 */
	public boolean isBtnValiderTFJOEnabled() {
		return btnValiderTFJOEnabled;
	}

	/**
	 * @return the btnExportTFJOExcelEnabled
	 */
	public boolean isBtnExportTFJOExcelEnabled() {
		return btnExportTFJOExcelEnabled;
	}
	
	public boolean isMenuTFJOEnabled(){
		return btnExportTFJOExcelEnabled || btnValiderTFJOEnabled || btnExecuterTFJOEnabled;
	}
	
	
	/**
	 * @return the btnProfilSouscriptionEnabled
	 */
	public boolean isBtnProfilSouscriptionEnabled() {
		return btnProfilSouscriptionEnabled;
	}

	/**
	 * @return the btnCompleteTransactionsEnabled
	 */
	public boolean isBtnCompleteTransactionsEnabled() {
		return btnCompleteTransactionsEnabled;
	}

	/**
	 * @return the btnSaveBranchMailsEnabled
	 */
	public boolean isBtnSaveBranchMailsEnabled() {
		return btnSaveBranchMailsEnabled;
	}

	/**
	 * @return the btnUpdateBranchMailsEnabled
	 */
	public boolean isBtnUpdateBranchMailsEnabled() {
		return btnUpdateBranchMailsEnabled;
	}

	/**
	 * @return the btnDeleteBranchMailsEnabled
	 */
	public boolean isBtnDeleteBranchMailsEnabled() {
		return btnDeleteBranchMailsEnabled;
	}
	
	
	public boolean isMenuBranchMailsEnabled(){
		return btnSaveBranchMailsEnabled || btnUpdateBranchMailsEnabled || btnDeleteBranchMailsEnabled;
	}

	public boolean isBtnExecuteRapprochementEnabled() {
		return btnExecuteRapprochementEnabled;
	}

	/**
	 * @return the btnSuiviRobotEnabled
	 */
	public boolean isBtnSuiviRobotEnabled() {
		return btnSuiviRobotEnabled;
	}
	
	
	
}
