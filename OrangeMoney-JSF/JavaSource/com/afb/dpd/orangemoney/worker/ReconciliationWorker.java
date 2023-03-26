package com.afb.dpd.orangemoney.worker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.xml.rpc.holders.StringHolder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.afb.dpd.orangemoney.jpa.entities.Parameters;
import com.afb.dpd.orangemoney.jpa.entities.Resiliation;
import com.afb.dpd.orangemoney.jpa.entities.Subscriber;
import com.afb.dpd.orangemoney.jpa.entities.TraceRobot;
import com.afb.dpd.orangemoney.jpa.enums.StatutContrat;
import com.afb.dpd.orangemoney.jpa.tools.ClientProduit;
import com.afb.dpd.orangemoney.jpa.tools.StatusAbon;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.tools.OMTools;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.btow.om.register.RequestInitiator;
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;


public class ReconciliationWorker {


	private static TimerTask task;

	private static java.util.Timer timer;

	/**
	 * 
	 */
	public static void initChecking(){
		try{

			if(task != null) task.cancel();
			if(timer != null) timer.cancel();


			task = new TimerTask(){
				@Override
				public void run(){

					process();

					checkingRobot();
				}	
			};

			Parameters parameter = OMViewHelper.appManager.findParameters();
			timer = new java.util.Timer(true);
			timer.schedule(task, DateUtils.addMinutes(new Date(), parameter.getDelaiReconciliation()), TimeUnit.MINUTES.toMillis(parameter.getPeriodeAlerte()));	

		}catch(Exception e){
			e.printStackTrace();
		}
	}




	/**
	 * 
	 */
	public static void runChecking(){
		try{

			if(task != null) task.cancel();
			if(timer != null) timer.cancel();


			task = new TimerTask(){
				@Override
				public void run(){

					process();

					checkingRobot();
				}	
			};

			Parameters parameter = OMViewHelper.appManager.findParameters();
			timer = new java.util.Timer(true);
			timer.schedule(task, DateUtils.addMinutes(new Date(), parameter.getDelaiReconciliation()), TimeUnit.MINUTES.toMillis(parameter.getPeriodeAlerte()));

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * 
	 */
	public static void cancelChecking(){
		try{

			if(task != null) task.cancel();
			if(timer != null) timer.cancel();

			task = null;
			timer = null;

		}catch(Exception e){
			e.printStackTrace();
		}
	}




	public static void process(){

		TraceRobot trace = new TraceRobot();
		trace.setDatetimeTrace(new Date());
		trace.setTypeExecution("ALERTE-RECONCILIATION");

		Parameters params = OMViewHelper.appManager.findParameters();
		if("ON".equals(params.getExecutionAlerte())){
			try {
				String result = OMViewHelper.appManager.checkTransactionsNonreconcilie();
				
				System.out.println("******************** ReconciliationWorker process result ******************* : " + result);

				if(StringUtils.isNotBlank(result)){
					trace.setExecutedMethod(Boolean.TRUE);
					trace.setTrace("TRANSACTIONS NON RECONCILIEES : " + result);
					
					/*
					if(Integer.parseInt(result) > 0) {
						// Enregistrement des parametres
						params.setLancementRobot("ON");
						OMViewHelper.appManager.saveParameters(params);
						TransactionWorker.runChecking();						
					}
					*/
				}
				else{
					trace.setExecutedMethod(Boolean.FALSE);
					trace.setTrace("RAS");
				}
				OMViewHelper.appDAOLocal.save(trace);
			} catch (Exception e) {
			}

		}
		else{
			trace.setTrace("EXECUTION ALERTE RECONCILIATION OFF");
			trace.setExecutedMethod(Boolean.FALSE);
			OMViewHelper.appDAOLocal.save(trace);
		}
		
		

		//Verification des resiliations dans le CBS
		TraceRobot traceResil = new TraceRobot();
		traceResil.setDatetimeTrace(new Date());
		traceResil.setTypeExecution("VERIFICATION-RESILIATION");
		HashMap<String, String> mapHeure = new HashMap<String, String>();
		if("ON".equals(params.getExecutionAlerte()) && StringUtils.isNotBlank(params.getHeureVerifResiliation()) && StringUtils.isNotBlank(params.getUrlPackage())) {
			for(String s : params.getHeureVerifResiliation().split("-")) {
				if(!s.equals("")) mapHeure.put(s, s);
			}

			try {
				if(mapHeure.containsKey(new SimpleDateFormat("HH").format(new Date()))) {
					if("ON".equals(params.getExecutionRobot())){
						traceResil.setTrace("VERIFICATION-RESILIATION OK");
						traceResil.setExecutedMethod(Boolean.TRUE);
						processResiliation();
					}
					else{
						traceResil.setTrace("VERIFICATION-RESILIATION OFF");
						traceResil.setExecutedMethod(Boolean.FALSE);
					}
				}
				else {
					traceResil.setExecutedMethod(Boolean.FALSE);
					traceResil.setTrace("VERIFICATION-RESILIATION HORAIRE INADE");;
				}
				OMViewHelper.appDAOLocal.save(traceResil);
			} catch (Exception e) {
			}
		}
		else {
			traceResil.setExecutedMethod(Boolean.FALSE);
			traceResil.setTrace("AUCUN PARAMETRE DEFINI : HORAIRE - URL");
			OMViewHelper.appDAOLocal.save(traceResil);
		}
		

	}



	public static void checkingRobot(){

		TraceRobot trace = new TraceRobot();
		trace.setDatetimeTrace(new Date());
		trace.setTypeExecution("ALERTE-RECONCILIATION");

		try {

			Parameters params = OMViewHelper.appManager.findParameters();
			if("ON".equals(params.getExecutionAlerte())){
				String result = OMViewHelper.appManager.checkRobotReconciliation();

				if(StringUtils.isNotBlank(result)){
					trace.setExecutedMethod(Boolean.TRUE);
					trace.setTrace(result);
				}
				else{
					trace.setExecutedMethod(Boolean.FALSE);
					trace.setTrace("RAS");
				}
			}
			else{
				trace.setExecutedMethod(Boolean.FALSE);
				trace.setTrace("VERIFICATION-RESILIATION OFF");
			}

			OMViewHelper.appDAOLocal.save(trace);

		}catch(Exception e){
		}

	}




	public static void processResiliation(){
		try {
			
			List<Resiliation> resiliations = OMViewHelper.appDAOLocal.filter(Resiliation.class, null, RestrictionsContainer.getInstance().add(Restrictions.eq("dateResiliation", new SimpleDateFormat("ddMMyyyy").format(new Date()))), null, null, 0, -1);
			HashMap<Object, Object> mapResi = new HashMap<Object, Object>();
			for(Resiliation r : resiliations) {
				mapResi.put(r.getClient(), r);
			}
			
			List<ClientProduit> results = OMViewHelper.appManager.resiliationsCBS();
			for(ClientProduit c : results){
				if(StatusAbon.RESILIE.equals(c.getStatut()) && !mapResi.containsKey(c.getMatricule())) {
					// Initialisation d'un conteneur de restrictions
					RestrictionsContainer rc = RestrictionsContainer.getInstance();

					// Ajout de la restriction sur le code du client
					rc.add(Restrictions.eq("customerId", c.getMatricule()));
					//*** rc.add(Restrictions.ne("dateResiliationCBS", new SimpleDateFormat("ddMMyyyy").format(new Date())));

					// Initialisation d'un conteneur d'ordres
					OrderContainer orders = OrderContainer.getInstance().add(Order.desc("date"));

					// Filtre des souscriptions
					List<Subscriber> souscriptions = OMViewHelper.appManager.filterSubscriptions(rc, orders);
					
					//annulation du contrat de souscription
					for(Subscriber  s : souscriptions) annulerSouscription(s.getId());
					
				}
			}
		} catch (Exception e) {

		}
	}


	/**
	 * Methode d'annulation du contrat de souscription
	 */
	public static void annulerSouscription(Long idAnnulation) {

		try {

			Subscriber subscriber = OMViewHelper.appDAOLocal.findByPrimaryKey(Subscriber.class,idAnnulation,null);

			String _return ="";
			if(subscriber.getStatus().equals(StatutContrat.SUSPENDU)){
				return;
			}else{
				
				//TEST
				/*
				subscriber.setUtiSuspendu("AUTO");
				subscriber.setDateSuspendu(new Date());
				subscriber.setDateResiliationCBS(new SimpleDateFormat("ddMMyyyy").format(new Date()));
				subscriber.setStatus(StatutContrat.SUSPENDU);
				OMViewHelper.appDAOLocal.update(subscriber);
				
				//Trace de la Resiliation
				Resiliation resi = new Resiliation(null, subscriber.getId(), subscriber.getCustomerId(), subscriber.getSubmsisdn(), subscriber.getCustomerName(), new SimpleDateFormat("ddMMyyyy").format(new Date()), new SimpleDateFormat("HH:mm:ss").format(new Date()), "AUTO");
				OMViewHelper.appDAOLocal.save(resi);
				*/
				
								
				// Annulation PROD
				StringHolder alias = new StringHolder(subscriber.getBankPIN());
				String  close_date = DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
				String orig = RequestInitiator.Bank.toString();
				String motif = RequestInitiator.Client.toString();
				_return = OMViewHelper.register.ombClose(alias, close_date, orig, motif);


				if(_return.equalsIgnoreCase(OMTools.OK)){

					subscriber.setUtiSuspendu("AUTO");
					subscriber.setDateSuspendu(new Date());
					subscriber.setStatus(StatutContrat.SUSPENDU);
					OMViewHelper.appDAOLocal.update(subscriber);
					
					//Trace de la Resiliation
					Resiliation resi = new Resiliation(null, subscriber.getId(), subscriber.getCustomerId(), subscriber.getSubmsisdn(), subscriber.getCustomerName(), new SimpleDateFormat("ddMMyyyy").format(new Date()), new SimpleDateFormat("HH:mm:ss").format(new Date()), "AUTO");
					OMViewHelper.appDAOLocal.save(resi);
				
					// Notification
					String message = "Cher(e) client(e), votre abonnement au produit MAC ORANGE a ete suspendu. Rapprochez vous de votre agence pour plus d'informations.";
					try {
						OMViewHelper.appManager.alerteSMS(message.toUpperCase(), "237" + subscriber.getSubmsisdn());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					subscriber.setUtiSuspendu("AUTO");
					subscriber.setDateSuspendu(new Date());
					subscriber.setStatus(StatutContrat.SUSPENDU);
					OMViewHelper.appDAOLocal.update(subscriber);
					//Trace de la Resiliation
					Resiliation resi = new Resiliation(null, subscriber.getId(), subscriber.getCustomerId(), subscriber.getSubmsisdn(), subscriber.getCustomerName(), new SimpleDateFormat("ddMMyyyy").format(new Date()), new SimpleDateFormat("HH:mm:ss").format(new Date()), "AUTO");
					OMViewHelper.appDAOLocal.save(resi);
				}
			}

		} catch(Exception e){
			e.printStackTrace();
			// Affichage de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}


}
