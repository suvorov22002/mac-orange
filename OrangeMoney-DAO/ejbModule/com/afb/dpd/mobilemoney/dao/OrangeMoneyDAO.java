package com.afb.dpd.mobilemoney.dao;

import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.afb.dpd.orangemoney.jpa.entities.Subscriber;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.yashiro.persistence.utils.dao.JPAGenericDAORulesBased;

/**
 * Session Bean implementation class 
 * @version 1.0
 */
@Stateless(name = IOrangeMoneyDAOLocal.SERVICE_NAME, mappedName = IOrangeMoneyDAOLocal.SERVICE_NAME, description = "")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OrangeMoneyDAO extends JPAGenericDAORulesBased implements IOrangeMoneyDAOLocal {

    /**
     * Default constructor. 
     */
    public OrangeMoneyDAO() {}

	/**
	 * Le gestionnaire d'entites
	 */
	@PersistenceContext(unitName = "OrangeMoney")
	private EntityManager manager;

	
	public EntityManager getEntityManager() {
		return manager;
	}

	
	
	@Asynchronous
	public void updateSubscriber(List<Subscriber> subs){
		//for(Subscriber s : subs) updateSubscriber(s);
		saveList(subs,true);
	}
	
	@Asynchronous
	public void updateTransaction(List<Transaction> trans){
		//for(Transaction s : trans) updateTransaction(s);
		saveList(trans,true);
	}
	
	@Asynchronous
	public void updateSubscriber(Subscriber s){
		Query q = manager.createNamedQuery(Subscriber.UPDATE_SUBSCRIBER);
		q.setParameter("dateSaveDernCompta",s.getDateDernCompta());
		q.setParameter("dateDernCompta",s.getDateSaveDernCompta());
		q.setParameter("id",s.getId());
		q.executeUpdate();
	}

	@Asynchronous
	public void updateTransaction(Transaction t){
		Query q = manager.createNamedQuery(Transaction.UPDATE_TRANSACTION);
		q.setParameter("status",t.getStatus());
		q.setParameter("posted",t.getPosted());
		q.setParameter("dateTraitement",t.getDateTraitement());
		q.setParameter("id",t.getId());
		q.executeUpdate();
	}
	
	
	
}
