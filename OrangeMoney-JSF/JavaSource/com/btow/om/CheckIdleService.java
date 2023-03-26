package com.btow.om;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.time.DateUtils;

import com.afb.dpd.orangemoney.jpa.entities.IdleOM;
import com.afb.dpd.orangemoney.jpa.entities.Parameters;
import com.afb.dpd.orangemoney.jsf.models.InformationDialog;
import com.afb.dpd.orangemoney.jsf.models.PortalInformationHelper;
import com.afb.dpd.orangemoney.jsf.tools.OMTools;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.yashiro.persistence.utils.dao.tools.encrypter.Encrypter;

/**
 * 
 * @author Owner
 *
 */
public class CheckIdleService {


	private TimerTask task = null;

	private Timer timer = null;

	private Date starts;
		
	public static final String idleOK = "true" ;
	public static final String idleNON = "false";


	public CheckIdleService(){
		process();
		initParameter();
	}


	/**
	 * creerTimer
	 */
	public void process(){

		try{						
			// Heure de lancement 
			init(2);
			initParameter();
						
		}catch(Exception e){
			e.printStackTrace();
			process();
		}
	}


	public void init(Integer val){

		try{						

			//Integer val = 1 ;
			// Heure de lancement 
			if(timer != null )timer.cancel();
			if(task != null )task.cancel();
			starts = new Date();
			task = new TimerTask(){
				@Override
				public void run(){
					try{ processWorker(); }catch(Exception e){e.printStackTrace();}
				}	
			};
			timer = new Timer(true);
			timer.schedule(task,DateUtils.addMinutes(starts, val),val*60*1000);

		}catch(Exception e){
			e.printStackTrace();
			process();
		}
	}


	/**
	 * process
	 */
	public void processWorker(){

		try{
			Parameters params = OMViewHelper.appDAOLocal.findByPrimaryKey(Parameters.class, Encrypter.getInstance().hashText(Parameters.CODE_PARAM), null);
			if(params.getIdleAuto()== null || Boolean.TRUE.equals(params.getIdleAuto())){
				boolean tfjo = false ; //OMViewHelper.appManager.isTfjEnCours();
				boolean responseCode = true;
				if(tfjo){
					responseCode = OMViewHelper.idle.setIdle(idleOK);
					OMViewHelper.appDAOLocal.save(new IdleOM(responseCode, tfjo, idleOK));
					init(5);
				}else{
					responseCode = OMViewHelper.idle.setIdle(idleNON);
					OMViewHelper.appDAOLocal.save(new IdleOM(responseCode, tfjo, idleNON));
					init(30);
				}
				System.out.println(responseCode+"----IDle Statut-------"+tfjo);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * process
	 */
	public void processActive(){

		try{
			boolean tfjo = false ; // OMViewHelper.appManager.isTfjEnCours();
			boolean responseCode = true;
			responseCode = OMViewHelper.idle.setIdle(idleNON);
			OMViewHelper.appDAOLocal.save(new IdleOM(responseCode, tfjo, idleNON));
			PortalInformationHelper.showInformationDialog("Operation effectuée avec succes.", InformationDialog.DIALOG_SUCCESS);

		}catch (Exception e) {
			// TODO: handle exception
		}

	}


	/**
	 * process
	 */
	public void processCancel(){

		try{
			boolean tfjo = false; //OMViewHelper.appManager.isTfjEnCours();
			boolean responseCode = true;
			responseCode = OMViewHelper.idle.setIdle(idleOK);
			OMViewHelper.appDAOLocal.save(new IdleOM(responseCode, tfjo, idleOK));
			PortalInformationHelper.showInformationDialog("Operation effectuée avec succes.", InformationDialog.DIALOG_SUCCESS);
		}catch (Exception e) {
			// TODO: handle exception
			PortalInformationHelper.showInformationDialog(e.toString(), InformationDialog.DIALOG_SUCCESS);
		}

	}

	/**
	 * process
	 */
	public void initParameter(){

		try{

			// Recherche des parametres par defaut
			Parameters params = OMViewHelper.appDAOLocal.findByPrimaryKey(Parameters.class, Encrypter.getInstance().hashText(Parameters.CODE_PARAM), null);
			if(params.getBankBIC() != null )OMTools.BIC = params.getBankBIC();
			if(params.getRegisterService() != null ) OMTools.registerService = params.getRegisterService()+OMTools.BIC;
			if(params.getInquiryService() != null ) OMTools.inquiryService = params.getInquiryService()+OMTools.BIC;
			if(params.getIdlService() != null ) OMTools.idlService = params.getIdlService()+OMTools.BIC;
			if(params.getCurrency() != null ) OMTools.currency = params.getCurrency();
			if(params.getOperatorCode() != null ) OMTools.operatorCode = params.getOperatorCode();
			if(params.getAffiliateCode() != null ) OMTools.affiliateCode = params.getAffiliateCode();
			if(params.getAllias() != null ) OMTools.Allias = params.getAllias();
			if(params.getBankPINLength() != null ) OMTools.bankPINLength = params.getBankPINLength();
			PortalInformationHelper.showInformationDialog("Operation effectuée avec succes.", InformationDialog.DIALOG_SUCCESS);

		}catch (Exception e) {
			// TODO: handle exception
			PortalInformationHelper.showInformationDialog(e.toString(), InformationDialog.DIALOG_SUCCESS);
		}

	}



}
