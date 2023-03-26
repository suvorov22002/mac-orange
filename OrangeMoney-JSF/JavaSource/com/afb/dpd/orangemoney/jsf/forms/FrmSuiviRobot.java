/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.forms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.afb.dpd.orangemoney.jpa.entities.Parameters;
import com.afb.dpd.orangemoney.jpa.entities.TraceRobot;
import com.afb.dpd.orangemoney.jsf.models.AbstractPortalForm;
import com.afb.dpd.orangemoney.jsf.models.InformationDialog;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.models.PortalInformationHelper;
import com.afb.dpd.orangemoney.jsf.tools.OMTools;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.afb.dpd.orangemoney.worker.ReconciliationWorker;
import com.afb.dpd.orangemoney.worker.TransactionWorker;
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

/**
 * FrmSuiviRobot
 * @author 	Yves LABO
 * @version 1.0
 */
public class FrmSuiviRobot extends AbstractPortalForm {

	private List<TraceRobot> listTraces = new ArrayList<TraceRobot>();

	private Parameters params;

	private Date txtDateDeb = new Date(), txtDateFin = new Date();
	private String executionRobot, executionAlerte;


	private boolean fileIsGenerated = false, robotLancer = true, alerteLancer = true;

	private String exportFileName = "ListeTraceRobots.xls", lastExecution, lastExecutionAlerte;

	private int num = 1;

	/**
	 * Default Constructor
	 */
	public FrmSuiviRobot(){}



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm() {
		// TODO Auto-generated method stub
		super.initForm();  num = 1;
		// Lecture des parametres
		params = OMViewHelper.appManager.findParameters();
		
		txtDateDeb = new Date();
		txtDateFin = new Date();
		
		//*** lastExecution = "Date dernière exécution du robot : " + OMViewHelper.appManager.lastExecutionRobot();
		
		//*** lastExecutionAlerte = "Date dernière exécution des alertes : " + OMViewHelper.appManager.lastExecutionAlerte();

		if("ON".equals(params.getLancementRobot())) robotLancer = true; else robotLancer = false;

		if("ON".equals(params.getLancementAlerte())) alerteLancer = true; else alerteLancer = false;

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub Consulter les transactions Orange Mobile Money
		return "Suivi des traces du robot";
	}



	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose(); executionRobot = null; executionAlerte = null;  num = 1;
		listTraces.clear(); fileIsGenerated = false;
		exportFileName = null;  txtDateDeb = null;  txtDateFin = null; params = null; lastExecution = null; lastExecutionAlerte = null;
	}



	/**
	 * Recherche la liste des transactions ussd
	 */
	public void filterTraceRobots(){

		try {
			num = 1;

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

			if(txtDateDeb == null || txtDateFin == null){
				// Msg Info
				PortalInformationHelper.showInformationDialog("Veuillez saisir toutes les dates de recherche SVP", InformationDialog.DIALOG_INFORMATION);
				// Annulation
				return;
			}
			
			Date deb = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(txtDateDeb) + " 00:00:00");
			Date fin = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(txtDateFin) + " 23:59:59");

			// Initialisation d'un conteneur de restrictions
			RestrictionsContainer rc = RestrictionsContainer.getInstance().add(Restrictions.between("datetimeTrace", deb, fin));
			
			OrderContainer orders = OrderContainer.getInstance().add(Order.desc("datetimeTrace"));
			listTraces = OMViewHelper.appManager.filterTraceRobots(rc, orders);



		}catch(Exception ex){
			// Traitement de m'exception
			PortalExceptionHelper.threatException(ex);
		}

	}





	public void actualiserPage() {

		try {

			lastExecution = "Date dernière exécution du robot : " + OMViewHelper.appManager.lastExecutionRobot();

		} catch(Exception e){

			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}
	
	
	public void actualiserPageAlerte() {

		try {

			lastExecution = "Date dernière exécution des alertes : " + OMViewHelper.appManager.lastExecutionAlerte();

		} catch(Exception e){

			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}
	
	
	
	public void saveExecution() {

		try {

			if(StringUtils.isBlank(executionRobot)){
				PortalInformationHelper.showInformationDialog("Veuillez choisir un élément !", InformationDialog.DIALOG_WARNING);
				return;
			}

			params.setExecutionRobot(executionRobot);

			// Enregistrement des parametres
			OMViewHelper.appManager.saveParameters(params);

			// Message d'information
			PortalInformationHelper.showInformationDialog("Information sauvegardée avec succes.", InformationDialog.DIALOG_SUCCESS);

		} catch(Exception e){

			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}
	
	
	
	public void saveExecutionAlerte() {

		try {

			if(StringUtils.isBlank(executionAlerte)){
				PortalInformationHelper.showInformationDialog("Veuillez choisir un élément !", InformationDialog.DIALOG_WARNING);
				return;
			}

			params.setExecutionAlerte(executionAlerte);

			// Enregistrement des parametres
			OMViewHelper.appManager.saveParameters(params);

			// Message d'information
			PortalInformationHelper.showInformationDialog("Information sauvegardée avec succes.", InformationDialog.DIALOG_SUCCESS);

		} catch(Exception e){

			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}
	



	public void demarrerRobot() {

		try {

			TransactionWorker.runChecking();
			params.setLancementRobot("ON");

			// Enregistrement des parametres
			OMViewHelper.appManager.saveParameters(params);
			params = OMViewHelper.appManager.findParameters();
			if("ON".equals(params.getLancementRobot())) robotLancer = true; else robotLancer = false;

			// Message d'information
			PortalInformationHelper.showInformationDialog("Robot lancé avec succes.", InformationDialog.DIALOG_SUCCESS);

		} catch(Exception e){

			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}




	public void arreterRobot() {

		try {

			TransactionWorker.cancelChecking();
			params.setLancementRobot("OFF");

			// Enregistrement des parametres
			OMViewHelper.appManager.saveParameters(params);
			params = OMViewHelper.appManager.findParameters();
			if("ON".equals(params.getLancementRobot())) robotLancer = true; else robotLancer = false;
			
			// Message d'information
			PortalInformationHelper.showInformationDialog("Robot arreté avec succes.", InformationDialog.DIALOG_SUCCESS);

		} catch(Exception e){

			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}

	
	
	
	
	public void demarrerAlerte() {

		try {

			ReconciliationWorker.runChecking();
			params.setLancementAlerte("ON");

			// Enregistrement des parametres
			OMViewHelper.appManager.saveParameters(params);
			params = OMViewHelper.appManager.findParameters();
			if("ON".equals(params.getLancementAlerte())) alerteLancer = true; else alerteLancer = false;

			// Message d'information
			PortalInformationHelper.showInformationDialog("Alertes lancées avec succes.", InformationDialog.DIALOG_SUCCESS);

		} catch(Exception e){

			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}




	public void arreterAlerte() {

		try {

			ReconciliationWorker.cancelChecking();
			params.setLancementAlerte("OFF");

			// Enregistrement des parametres
			OMViewHelper.appManager.saveParameters(params);
			params = OMViewHelper.appManager.findParameters();
			if("ON".equals(params.getLancementAlerte())) alerteLancer = true; else alerteLancer = false;
			
			// Message d'information
			PortalInformationHelper.showInformationDialog("Alertes arretées avec succes.", InformationDialog.DIALOG_SUCCESS);

		} catch(Exception e){

			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);

		}

	}




	public void exportListeExcell(){
		try {

			if(listTraces.isEmpty()){
				PortalInformationHelper.showInformationDialog("Aucune trace dans la liste !",InformationDialog.DIALOG_INFORMATION);
				return;
			}

			exportFileName = "LISTE_TRACES_"+new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date())+".xlsx";
			//**** OMViewHelper.appManager.exportTraceRobotIntoExcelFile(list,exportFileName);

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



	public void setNum(int num) {
		this.num = num;
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


	public int getNum() {
		return num++;
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

	public String getExecutionRobot() {
		return executionRobot;
	}

	public void setExecutionRobot(String executionRobot) {
		this.executionRobot = executionRobot;
	}

	public List<TraceRobot> getListTraces() {
		return listTraces;
	}

	public void setListTraces(List<TraceRobot> listTraces) {
		this.listTraces = listTraces;
	}

	/**
	 * @return the params
	 */
	public Parameters getParams() {
		return params;
	}

	public boolean isRobotLancer() {
		return robotLancer;
	}

	public void setRobotLancer(boolean robotLancer) {
		this.robotLancer = robotLancer;
	}

	public String getLastExecution() {
		return lastExecution;
	}

	public void setLastExecution(String lastExecution) {
		this.lastExecution = lastExecution;
	}



	/**
	 * @return the executionAlerte
	 */
	public String getExecutionAlerte() {
		return executionAlerte;
	}



	/**
	 * @param executionAlerte the executionAlerte to set
	 */
	public void setExecutionAlerte(String executionAlerte) {
		this.executionAlerte = executionAlerte;
	}



	/**
	 * @return the alerteLancer
	 */
	public boolean isAlerteLancer() {
		return alerteLancer;
	}



	/**
	 * @param alerteLancer the alerteLancer to set
	 */
	public void setAlerteLancer(boolean alerteLancer) {
		this.alerteLancer = alerteLancer;
	}



	/**
	 * @return the lastExecutionAlerte
	 */
	public String getLastExecutionAlerte() {
		return lastExecutionAlerte;
	}



	/**
	 * @param lastExecutionAlerte the lastExecutionAlerte to set
	 */
	public void setLastExecutionAlerte(String lastExecutionAlerte) {
		this.lastExecutionAlerte = lastExecutionAlerte;
	}



}
