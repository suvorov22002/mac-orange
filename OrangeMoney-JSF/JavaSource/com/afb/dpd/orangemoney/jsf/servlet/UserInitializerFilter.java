/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.servlet;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;

import afb.dsi.dpd.portal.business.facade.IFacadeManagerRemote;
import afb.dsi.dpd.portal.jpa.entities.User;
import afb.dsi.dpd.portal.jpa.tools.PortalHelper;
//*** import entreeRela.metier.utils.IEntreeRelaManagerRemote;

/**
 * Filtre d'initialisation des Parametres Utilisateurs apres connexion
 * @author Yves LABO
 * @version 1.0
 */
public class UserInitializerFilter implements Filter {
	
	/**
	 * Le Logger
	 */
	protected static Log logger = LogFactory.getLog(UserInitializerFilter.class);
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		
		// On caste la requete
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		
		// La Session
		HttpSession session = request.getSession(true);
		
		// Adresse IP du Client
		String clientIPAdress = request.getRemoteAddr();
		
		// Recuperation de l'id de l'utilisateur connecte
		Long uid = request.getParameter("uid") == null ? null : Long.valueOf(request.getParameter("uid")) ;
		
		// Si l'id de l'utilisateur n'est pas null
		if(uid != null) {
			
			// Si la session ne contient pas encore l'utilisateur connecte
			if (session.getAttribute( PortalHelper.CONNECTED_USER_SESSION_NAME ) == null ) {
				
				//System.out.println("---------------------------");
				
				try{
					if ( OMViewHelper.portalFacadeManager == null ) OMViewHelper.portalFacadeManager = (IFacadeManagerRemote) new InitialContext().lookup( PortalHelper.APPLICATION_EAR.concat("/").concat( IFacadeManagerRemote.SERVICE_NAME ).concat("/remote") );
				}catch(Exception e){}
				
				try{
					//*** OMViewHelper.entreeRelaManager = (IEntreeRelaManagerRemote) new InitialContext().lookup( PortalHelper.APPLICATION_EAR.concat("/").concat( IEntreeRelaManagerRemote.SERVICE_NAME ).concat("/remote") );
				}catch(Exception e){}
				
				
				
				// Si le service Facade du portail est demarre
				if ( OMViewHelper.portalFacadeManager != null ) {
					
					// Recherche de l'utilisateur connecte
					User user = (User) OMViewHelper.portalFacadeManager.findByProperty(User.class, "id", uid);
					
					// Si l'utilisateur a ete retrouve
					if( user != null ) {
						
						// Lecture de l'adresse ip du poste utilisateur
						user.setIpAddress(clientIPAdress);
						
						// On Positionne l'Utilisateur
						session.setAttribute(PortalHelper.CONNECTED_USER_SESSION_NAME, user );
						
					}
						
				}
				
				//System.out.println("---------------------------");
				
			}
			
		}
		
		// On filtre
		chain.doFilter(servletRequest, servletResponse);
	}
	
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {}

}
