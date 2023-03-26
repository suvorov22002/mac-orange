package com.afb.dpd.orangemoney.worker;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.xml.rpc.holders.StringHolder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.afb.dpd.orangemoney.jpa.entities.Parameters;
import com.afb.dpd.orangemoney.jpa.entities.TraceRobot;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jpa.enums.TransactionStatus;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jpa.tools.OgMoHelper;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.orange.b2w.statusinquiry.GlobalStatus;
import com.orange.b2w.statusinquiry.TypeID;


public class TransactionWorker {


	private static TimerTask task;

	private static java.util.Timer timer;

	private static int nbreTrans = 0;

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

					TraceRobot trace = new TraceRobot();
					trace.setDatetimeTrace(new Date());
					trace.setTypeExecution("RECONCILIATION-TRANSACTION");

					try {

						Parameters params = OMViewHelper.appManager.findParameters();
						if("ON".equals(params.getExecutionRobot())){
							trace.setExecutedMethod(Boolean.TRUE);
							process();
							trace.setTrace(nbreTrans > 0 ? "RECONCILIATION OK : " + nbreTrans : "RECONCILIATION");
						}
						else{
							trace.setTrace("EXECUTION RECONCILIATION OFF");
							trace.setExecutedMethod(Boolean.FALSE);
						}

						OMViewHelper.appDAOLocal.save(trace);

					}catch(Exception e){
						trace.setExecutedMethod(Boolean.FALSE);
						trace.setTrace("ERREUR EXECUTION");
						OMViewHelper.appDAOLocal.save(trace);
					}


					//Relance du service MAC Orange
					try {
						relanceService();
					} catch (Exception e) {
						// TODO Auto-generated catch block
					}

				}	
			};

			Parameters parameter = OMViewHelper.appManager.findParameters();
			timer = new java.util.Timer(true);
			timer.schedule(task, DateUtils.addMinutes(new Date(), parameter.getDelaiRobot()), TimeUnit.MINUTES.toMillis(parameter.getPeriodeLancementRobot()));

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

					TraceRobot trace = new TraceRobot();
					trace.setDatetimeTrace(new Date());
					trace.setTypeExecution("RECONCILIATION-TRANSACTION");

					try {

						Parameters params = OMViewHelper.appManager.findParameters();
						if("ON".equals(params.getExecutionRobot())){
							trace.setExecutedMethod(Boolean.TRUE);
							process();
							trace.setTrace(nbreTrans > 0 ? "RECONCILIATION OK : " + nbreTrans : "RECONCILIATION"); 

						}
						else{
							trace.setTrace("EXECUTION RECONCILIATION OFF");
							trace.setExecutedMethod(Boolean.FALSE);
						}

						OMViewHelper.appDAOLocal.save(trace);

					}catch(Exception e){
						trace.setExecutedMethod(Boolean.FALSE);
						trace.setTrace("ERREUR EXECUTION");
						OMViewHelper.appDAOLocal.save(trace);
					}


					//Relance du service MAC Orange
					try {
						relanceService();
					} catch (Exception e) {
						// TODO Auto-generated catch block
					}


				}	
			};

			Parameters parameter = OMViewHelper.appManager.findParameters();
			timer = new java.util.Timer(true);
			timer.schedule(task, DateUtils.addMinutes(new Date(), parameter.getDelaiRobot()), TimeUnit.MINUTES.toMillis(parameter.getPeriodeLancementRobot()));	

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




	@SuppressWarnings("unused")
	public static void process(){

		System.out.println("------------ robot process complete --------------");

		try{

			nbreTrans = 0;

			Parameters parameter = OMViewHelper.appManager.findParameters();

			int lotTrans = StringUtils.isBlank(parameter.getLotTransactions())  ? 3000 : Integer.parseInt(parameter.getLotTransactions());
			List<Transaction> list = OMViewHelper.appManager.filterTransactionProcessing();
			if(list.size() > lotTrans) list = new ArrayList<Transaction>(list.subList(0,lotTrans));

			for(Transaction t :list){

				System.out.println("------------ t.getExternalRefNo() -------------- " + t.getExternalRefNo());

				try{

					/*
					String KEY_DIR = System.getProperty("jboss.server.data.dir", ".") + File.separator + "keystore";
					System.getProperties().setProperty("javax.net.ssl.trustStore",KEY_DIR+"\\new_keystore.ks");
					System.getProperties().setProperty("javax.net.ssl.trustStorePassword", "b2wafriland");
					 */
					

					//Code production
					try {

						String reverseTrans = "";
						if(StringUtils.isNotBlank(t.getExternalRefNo())) {

							com.orange.b2w.statusinquiry.TransactionStatus ts = null;
							try {
								ts = OMViewHelper.inquiry.transactionStatusInquiry(TypeID.BANK_ID, t.getExternalRefNo().toString());

								if(ts != null ){

									nbreTrans++;

									if(TypeOperation.Account2Wallet.equals(t.getTypeOperation())) {
										if(GlobalStatus.OK.equals(ts.getStatus())){
											reverseTrans = "REVERSE_TRANS:KO / ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
											OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.TRUE, null, ts.toString());
										}
										else if(GlobalStatus.KO.equals(ts.getStatus())){
											Map<String, String> map = OMViewHelper.appManager.processReversalTransaction(t.getId().toString());
											reverseTrans = "REVERSE_TRANS:OK / ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
											OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL, ts.toString());
										}
										else{
											reverseTrans = "-/ ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
											OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.FALSE, null, ts.toString());
										}

									}
									else {
										if(GlobalStatus.KO.equals(ts.getStatus()) && TransactionStatus.SUCCESS.equals(t.getStatus())){
											Map<String, String> map = OMViewHelper.appManager.processReversalTransaction(t.getId().toString());									
											reverseTrans = "REVERSE_TRANS:OK / ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
											OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL, ts.toString());
										}else if(GlobalStatus.OK.equals(ts.getStatus())){
											reverseTrans = "REVERSE_TRANS:KO / ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
											OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.TRUE, null, ts.toString());
										}
										else {
											if(StringUtils.isBlank(t.getReverseTrans())) {
												reverseTrans = "-/ ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
												OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.FALSE, null, ts.toString());
											}
											else {
												reverseTrans = "REVERSE_TRANS:OK / ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
												OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL, ts.toString());
											}
										}
									}
								}
								else {
									reverseTrans = "-/ ORANGE_STATUS:NULL / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
									//OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL);
									OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.FALSE, null, null);
								}

							} catch (Exception e) {

								reverseTrans = e.getMessage() + " - REVERSE_TRANS:EXCEPTION/ ORANGE_STATUS: / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
								//OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL);
								OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.FALSE, null, null);
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					


					//TESTS
					/*
					try {

						String reverseTrans = "";
						String traceStatus = "";
						if(StringUtils.isNotBlank(t.getExternalRefNo())) {

							String ts = null;
							try {
								ts = "OK";
							} catch (Exception e) {
							}

							traceStatus = "TransactionStatus [ returnCode="
									+ 200 + " " + t.toString();

							if(ts != null ){

								nbreTrans++;

								if(TypeOperation.Account2Wallet.equals(t.getTypeOperation())) {
									if("OK".equals(ts)){
										reverseTrans = "REVERSE_TRANS:KO / ORANGE_STATUS : " + ts + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
										OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.TRUE, null, traceStatus);
									}
									else {
										reverseTrans = "REVERSE_TRANS:OK / ORANGE_STATUS : " + ts + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
										OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.FALSE, null, traceStatus);
									}
								}
								else {
									if("KO".equals(ts) && TransactionStatus.SUCCESS.equals(t.getStatus())){
										Map<String, String> map = OMViewHelper.appManager.processReversalTransaction(t.getId().toString());									
										reverseTrans = "REVERSE_TRANS:OK / ORANGE_STATUS : " + ts + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
										OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL, traceStatus);
									}else if("OK".equals(ts)){
										reverseTrans = "REVERSE_TRANS:KO / ORANGE_STATUS : " + ts + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
										OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.TRUE, null, traceStatus);
									}
									else {
										if(StringUtils.isBlank(t.getReverseTrans())) {
											reverseTrans = "-/ ORANGE_STATUS : " + ts + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
											OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.FALSE, null, traceStatus);
										}
										else {
											reverseTrans = "REVERSE_TRANS:OK / ORANGE_STATUS : " + ts + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
											OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL, traceStatus);
										}
									}
								}
							}
							else {
								reverseTrans = "-/ ORANGE_STATUS:NULL / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
								OMViewHelper.appManager.updateTransReconcliation(t.getId(), reverseTrans, Boolean.FALSE, TransactionStatus.CANCEL, null);
							}

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					*/

				}catch(Exception e){
					// TODO: handle exception
					//e.printStackTrace();
				}

			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}



	@SuppressWarnings("unused")
	public static Transaction processTransaction(Transaction transaction){

		//*** System.out.println("------------ robot process complete --------------");

		try{

			//Bon code
			String reverseTrans = "";
			com.orange.b2w.statusinquiry.TransactionStatus ts = null;
			try {
				ts = OMViewHelper.inquiry.transactionStatusInquiry(TypeID.BANK_ID, transaction.getExternalRefNo().toString());

				if(ts != null ){

					nbreTrans++;

					if(TypeOperation.Account2Wallet.equals(transaction.getTypeOperation())) {
						if(GlobalStatus.OK.equals(ts.getStatus())){
							reverseTrans = "REVERSE_TRANS:KO / ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
							OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.TRUE, null, ts.toString());
						}
						else if(GlobalStatus.KO.equals(ts.getStatus())){
							Map<String, String> map = OMViewHelper.appManager.processReversalTransaction(transaction.getId().toString());
							reverseTrans = "REVERSE_TRANS:OK / ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
							OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL, ts.toString());
						}
						else{
							reverseTrans = "-/ ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
							OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.FALSE, null, ts.toString());
						}

					}
					else {
						if(GlobalStatus.KO.equals(ts.getStatus()) && TransactionStatus.SUCCESS.equals(transaction.getStatus())){
							Map<String, String> map = OMViewHelper.appManager.processReversalTransaction(transaction.getId().toString());									
							reverseTrans = "REVERSE_TRANS:OK / ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
							OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL, ts.toString());
						}else if(GlobalStatus.OK.equals(ts.getStatus())){
							reverseTrans = "REVERSE_TRANS:KO / ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
							OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.TRUE, null, ts.toString());
						}
						else {
							if(StringUtils.isBlank(transaction.getReverseTrans())) {
								reverseTrans = "-/ ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
								OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.FALSE, null, ts.toString());
							}
							else {
								reverseTrans = "REVERSE_TRANS:OK / ORANGE_STATUS : " + ts.getStatus() + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
								OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL, ts.toString());
							}
						}
					}
				}
				else {
					reverseTrans = "-/ ORANGE_STATUS:NULL / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
					//OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL);
					OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.FALSE, null, null);
				}

			} catch (Exception e) {

				reverseTrans = e.getMessage() + " - REVERSE_TRANS:EXCEPTION/ ORANGE_STATUS: / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
				//OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL);
				OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.FALSE, null, null);
			}



			//TESTS
			/*
			try {

				String reverseTrans = "";
				String traceStatus = "";
				if(StringUtils.isNotBlank(transaction.getExternalRefNo())) {

					String ts = null;
					try {
						ts = "OK";
					} catch (Exception e) {
					}

					traceStatus = "TransactionStatus [ returnCode="
							+ 200 + " " + transaction.toString();

					if(ts != null ){

						nbreTrans++;

						if(TypeOperation.Account2Wallet.equals(transaction.getTypeOperation())) {
							if("OK".equals(ts)){
								reverseTrans = "REVERSE_TRANS:KO / ORANGE_STATUS : " + ts + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
								OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.TRUE, null, traceStatus);
							}
							else {
								reverseTrans = "REVERSE_TRANS:OK / ORANGE_STATUS : " + ts + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
								OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.FALSE, null, traceStatus);
							}
						}
						else {
							if("KO".equals(ts) && TransactionStatus.SUCCESS.equals(transaction.getStatus())){
								Map<String, String> map = OMViewHelper.appManager.processReversalTransaction(transaction.getId().toString());									
								reverseTrans = "REVERSE_TRANS:OK / ORANGE_STATUS : " + ts + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
								OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL, traceStatus);
							}else if("OK".equals(ts)){
								reverseTrans = "REVERSE_TRANS:KO / ORANGE_STATUS : " + ts + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
								OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.TRUE, null, traceStatus);
							}
							else {
								if(StringUtils.isBlank(transaction.getReverseTrans())) {
									reverseTrans = "-/ ORANGE_STATUS : " + ts + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
									OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.FALSE, null, traceStatus);
								}
								else {
									reverseTrans = "REVERSE_TRANS:OK / ORANGE_STATUS : " + ts + " / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
									OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.TRUE, TransactionStatus.CANCEL, traceStatus);
								}
							}
						}
					}
					else {
						reverseTrans = "-/ ORANGE_STATUS:NULL / " +  new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
						OMViewHelper.appManager.updateTransReconcliation(transaction.getId(), reverseTrans, Boolean.FALSE, TransactionStatus.CANCEL, null);
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			*/

		}catch(Exception e){
			e.printStackTrace();
		}

		return transaction;

	}

		

	public static Transaction getStatusTransaction(Transaction transaction){

		try{			
			//Bon code
			com.orange.b2w.statusinquiry.TransactionStatus ts = OMViewHelper.inquiry.transactionStatusInquiry(TypeID.BANK_ID, transaction.getExternalRefNo().toString());

			if(ts != null ){
				transaction.setTraceStatus(ts.toString());
			}
			else {
				transaction.setTraceStatus("IMPOSSIBLE D'OBTENIR LE STATUT DE LA TRANSACTION CHEZ LE PARTENAIRE ORANGE");
			}


		}catch(Exception e){
			e.printStackTrace();
		}

		return transaction;

	}




	public static void relanceService(){

		try {

			//*** System.err.println("************* relanceService ************");
			if(OgMoHelper.getNbreMinutesBetween(OMViewHelper.appManager.getLastTransaction(),new Date()) > 15) {
				//***System.err.println("************* relanceService OgMoHelper.getNbreMinutesBetween(new Date(), OMViewHelper.appManager.getLastTransaction()) > 15 ************");
				//*** if(OgMoHelper.getNbreMinutesBetween(new Date(), OMViewHelper.appManager.getLastTransaction()) < 30) {
				try{
					StringHolder status = new StringHolder();
					StringHolder lastname = new StringHolder();
					StringHolder firstname = new StringHolder();
					StringHolder dob = new StringHolder();
					StringHolder cin = new StringHolder();

					//*** OMViewHelper.register.KYCRequestRelanceService(status, lastname, firstname, dob, cin);
					OMViewHelper.register.KYCRequest("695673175", "TEST01AFB", status, lastname, firstname, dob, cin);

				} catch(RemoteException e){
					// Affichage de l'exception
					PortalExceptionHelper.threatException(e);
				}catch(Exception e){
					PortalExceptionHelper.threatException(e);

				}
			}

		} catch (Exception e1) {
		}

	}



}
