package com.afb.dpd.orangemoney.jsf.tools;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.afb.dpd.mobilemoney.dao.IOrangeMoneyDAOLocal;
import com.afb.dpd.orangemoney.business.IOrangeMoneyManagerRemote;
import com.btow.om.IdlePortProxy;
import com.btow.om.register.RegisterPortProxy;
import com.orange.b2w.statusinquiry.TransactionStatusInquiryProxy;

import afb.dsi.dpd.portal.business.facade.IFacadeManagerRemote;
import afb.dsi.dpd.portal.jpa.entities.User;
import afb.dsi.dpd.portal.jpa.tools.PortalHelper;
//*** import entreeRela.metier.utils.IEntreeRelaManagerRemote;

/**
 * Utilitaire de presentation
 * @author Yves LABO
 * @version 2.0
 */
public class OMViewHelper {

	/**
	 * Service principal de gestion des 
	 */
	public static IOrangeMoneyManagerRemote appManager;
	
	/**
	 * Nom de l'application
	 */
	public final static String APPLICATION_EAR = "OrangeMoney";
	
	
	public final static String EntreeRelationCOM_EAR = "EntreeRelationCOM";
	
	/**
	 * Service de gestion des utilisateurs du portail
	 */
	public static IOrangeMoneyDAOLocal appDAOLocal;
	
	/**
	 * Service Facade du portail
	 */
	public static IFacadeManagerRemote portalFacadeManager;
	
	/**
	 * Service Entréee en relation
	 */
	//*** public static IEntreeRelaManagerRemote entreeRelaManager;
	
	
	/**
	 * Service Facade du portail
	 */
	public static RegisterPortProxy register = new RegisterPortProxy();
	
	/**
	 * Service Facade du portail
	 */
	public static TransactionStatusInquiryProxy inquiry = new TransactionStatusInquiryProxy();
	
	/**
	 * Service Facade du portail
	 */
	public static IdlePortProxy idle = new IdlePortProxy();
	
	
	/**
	 * Methode permettant d'obtenir le Bean Manage ClientArea
	 * @return	Bean Manage ClientArea
	 */
	public static Object getSessionManagedBean( String managedBeanName ) {
		
		// On retourne le Bean
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(managedBeanName);
	}
	

	/**
	 * Methode permettant d'obtenir le Bean Manage ClientArea
	 * @return	Bean Manage ClientArea
	 */
	public static void setSessionManagedBean( String managedBeanName, Object bean ) {
		
		// On retourne le Bean
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(managedBeanName, bean);
	}
	
	public static User getSessionUser() {
		return (User) ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).getAttribute(PortalHelper.CONNECTED_USER_SESSION_NAME);
	}
	

	/**
	 * Methode permettant d'obtenir le contexte de l'application
	 * @return	Contexte de l'application
	 */
	public static String getApplicationContext() {

		// Contexte de la servlet
		String servletContextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		
		// Requete entiere
		String requestURL = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString().replace(servletContextPath, "");
		
		// On retourne le contest
		return requestURL;
	}
	
		
}
