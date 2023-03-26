package com.afb.dpd.mobilemoney.dao;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless(name=IOrangeMoneyDAOAPILocal.SERVICE_NAME,mappedName=IOrangeMoneyDAOAPILocal.SERVICE_NAME)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OrangeMoneyAPI implements IOrangeMoneyDAOAPILocal{
	
	private AlertesDAO alertesDAO; 
	private SendEventToCbsDAO sendEventToCbsDAO; 
	
	public OrangeMoneyAPI() {
		
	}
	
	@PostConstruct
	public void init() {
		this.alertesDAO = new AlertesDAO();
		this.sendEventToCbsDAO = new SendEventToCbsDAO();
	}
	
	public void reload(){
		this.init();
	}
	

	@Override
	public AlertesDAO getAlertesDAO() {
		// TODO Auto-generated method stub
		return alertesDAO;
	}
	
	
	@Override
	public SendEventToCbsDAO getSendEventToCbsDAO() {
		// TODO Auto-generated method stub
		return sendEventToCbsDAO;
	}
	
	
}
