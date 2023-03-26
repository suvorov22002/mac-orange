package com.afb.dpd.mobilemoney.dao;

import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Local;

import com.afb.dpd.orangemoney.jpa.entities.Subscriber;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.yashiro.persistence.utils.dao.IJPAGenericDAO;

/**
 * Service DAO Local 
 * @author Francis KONCHOU
 * @version 2.0
 */
@Local
public interface IOrangeMoneyDAOLocal extends IJPAGenericDAO {

	/**
	 * Nom du Service DAO de gestion des utilisateurs
	 */
	public static final String SERVICE_NAME = "OrangeMoneyDAOLocal";
	
	/**
	 * MAJ d'une liste d'abonnements
	 * @param subs liste d'abonnements
	 */
	@Asynchronous
	public void updateSubscriber(List<Subscriber> subs);
	
	/**
	 * MAJ d'une liste de transactions
	 * @param trans liste de transactions
	 */
	@Asynchronous
	public void updateTransaction(List<Transaction> trans);
	
	/**
	 * MAJ d'un abonnement
	 * @param s abonnement
	 */
	public void updateSubscriber(Subscriber s);
	
	/**
	 * MAJ d'une transaction
	 * @param t transaction
	 */
	public void updateTransaction(Transaction t);
	
}
