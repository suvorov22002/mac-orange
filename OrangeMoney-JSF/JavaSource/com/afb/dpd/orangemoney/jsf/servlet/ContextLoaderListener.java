/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.servlet;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
//import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.afb.dpd.mobilemoney.dao.IOrangeMoneyDAOLocal;
import com.afb.dpd.orangemoney.business.IOrangeMoneyManagerRemote;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.afb.dpd.orangemoney.scanner.IScanner;
import com.afb.dpd.orangemoney.worker.ReconciliationWorker;
import com.afb.dpd.orangemoney.worker.TransactionWorker;

import afb.dsi.dpd.portal.business.facade.IFacadeManagerRemote;
import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

/**
 * Listener du chargement du Contexte de l'application
 * @author Francis KONCHOU
 * @version 2.0
 */
public class ContextLoaderListener implements ServletContextListener {

	/**
	 *  Contexte JNDI
	 */
	private Context ctx = null;

	/**
	 * Logger
	 */
	protected Log logger = LogFactory.getLog(ContextLoaderListener.class);

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {

		try {

			// Si le contexte n'est pas null
			if(ctx != null) ctx.close();

		} catch (Exception e) {

			// On relance l'exception
			throw new RuntimeException("Erreur lors de la fermeture du Contexte JNDI", e);

		}
	}



	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {

		try {

			// Initialisation du contexte JNDI
			ctx = new InitialContext();
			logger.info("Initialisation du Contexte OK !!!");

		} catch (Exception e) {
			// On relance l'exception
			throw new RuntimeException("Erreur lors de l'initialisation du Contexte JNDI", e);
		}

		try{

			/*** DEMARRAGE DES SERVICES METIERS ***/
			OMViewHelper.appManager = (IOrangeMoneyManagerRemote)ctx.lookup(OMViewHelper.APPLICATION_EAR + "/" + IOrangeMoneyManagerRemote.SERVICE_NAME + "/remote" );
			OMViewHelper.appDAOLocal = (IOrangeMoneyDAOLocal)ctx.lookup(OMViewHelper.APPLICATION_EAR + "/" + IOrangeMoneyDAOLocal.SERVICE_NAME + "/local" );
			logger.info("Demarrage des services metiers OK !!!");

		}catch(Exception e){
			// On relance l'exception
			throw new RuntimeException("Erreur lors du chargement des Services Métiers", e);

		}


		try{

			/*** DEMARRAGE DU SERVICE FACADE DU PORTAIL ***/
			OMViewHelper.portalFacadeManager = (IFacadeManagerRemote) ctx.lookup( PortalHelper.APPLICATION_EAR.concat("/").concat( IFacadeManagerRemote.SERVICE_NAME ).concat("/remote") );

		}catch(Exception e ){
			logger.info("Portail Inaccessible!!!");
		}
		
		try{

			/*** INTIALIZATIONS ***/
			OMViewHelper.appManager.initialisations();
			logger.info("Initialisations terminees avec succes!!!");

		}catch(Exception e){
			// Log
			logger.error("Une erreur s'est produite lors des initialisations !!!");
		}


		try{

			//Test.InitDeploy();
			//Demarrage du service
			IScanner scanner = (IScanner)ctx.lookup(OMViewHelper.APPLICATION_EAR + "/" + IScanner.SERVICE_NAME + "/remote" );
			scanner.scanAndInitialiseModule(PortalHelper.JBOSS_DEPLOY_DIR + File.separator + OMViewHelper.APPLICATION_EAR + ".ear");
			scanner = null;
						
			String KEY_DIR = System.getProperty("jboss.server.data.dir", ".") + File.separator + "keystore";
			System.setProperty("javax.net.ssl.trustStore", KEY_DIR+"\\keystore.ks");
			//*** System.setProperty("javax.net.ssl.trustStore", KEY_DIR+"\\new_keystore.ks");
			System.setProperty("javax.net.ssl.trustStorePassword", "b2wafriland");
			
			TransactionWorker.initChecking();
			
			ReconciliationWorker.initChecking();
			
			
			//System.clearProperty("javax.net.ssl.trustStore");
			//System.clearProperty("javax.net.ssl.trustStorePassword");
						
		}catch(Exception e){
			// Log
			logger.error("Erreur lors de l'auto-scan du module !!!");
			e.printStackTrace();
		}


	}
	
}
