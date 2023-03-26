package com.afb.dpd.mobilemoney.dao;

/**
 * 
 * @author Yves LABO
 *
 */
public interface IOrangeMoneyDAOAPILocal{
	/**
	 * Nom du Service DAO
	 */
	public static final String SERVICE_NAME = "OrangeMoneyDAOAPILocal";
	
	public void reload();
	
	public AlertesDAO getAlertesDAO();
	
	public SendEventToCbsDAO getSendEventToCbsDAO();
	
	
}
