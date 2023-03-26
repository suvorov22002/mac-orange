/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.afb.dpd.orangemoney.jpa.tools;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import afb.dsi.dpd.portal.business.facade.IFacadeManagerRemote;
import afb.dsi.dpd.portal.jpa.entities.DataSystem;
import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

/**
 * Classe de connexion aux bases de données
 * les paramètre de connexions sont récupérés via la BD du portail
 * @author Narcisse
 */
public class ConnexionDB {

	private static Connection connexion;
	private static DataSystem dsCBS;
	private static IFacadeManagerRemote portalFacade;
	private static final Log logger = LogFactory.getLog(ConnexionDB.class);


	public static Connection getConnection(String db){
		try {
			getCBSDataSystem(db);
			//            if("DELTA-V10".equals(db))
			connexion = getSystemConnection();
			//            else
			//                connexion = getSystemConnectionOracle();
			return connexion;
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ConnexionDB.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException("Le pilote n'a pas été trouvé");
		} catch (SQLException ex) {
			Logger.getLogger(ConnexionDB.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException("Paramètre de connexion invalide");
		} catch (Exception ex) {
			Logger.getLogger(ConnexionDB.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException("Paramètre de connexion invalide\n\n"+ex.getMessage());
		} 
	}

	
	
	public static void closeConnection() throws Exception {
		if(ConnexionDB.connexion != null) ConnexionDB.connexion.close();
	}

	
	
	/**
	 * Methode de demarrage du service facade du portail
	 */
	private static void startPortalFacade() {
		try {
			// Initialisation du contexte
			Context ctx = new InitialContext();

			// Demarrage du service Facade
			portalFacade = (IFacadeManagerRemote) ctx.lookup(PortalHelper.APPLICATION_EAR + "/" + IFacadeManagerRemote.SERVICE_NAME + "/remote");

		} catch(Exception ex) {
			logger.error("Impossible de demarrer le service Facade du Portail");
		}
	}

	
	
	@SuppressWarnings("unchecked")
	private static void getCBSDataSystem(String db) {
		try {
			if(portalFacade == null) startPortalFacade();

			if(portalFacade == null) return;

			List<DataSystem> ds = (List<DataSystem>) portalFacade.filterByQuery("From DataSystem ds where ds.code='"+db+"'", null); 

			dsCBS = ds != null && !ds.isEmpty() ? ds.get(0) : null;

		} catch(Exception ex) {
			logger.error("Impossible de trouver la DS relative au code "+db);
		}

	}

	
	
	private static Connection getSystemConnection() throws Exception {
		Class.forName(dsCBS.getProviderClassName());
		
		return DriverManager.getConnection( dsCBS.getDbConnectionString(), dsCBS.getDbUserName(), dsCBS.getDbPassword() );
	}



}
