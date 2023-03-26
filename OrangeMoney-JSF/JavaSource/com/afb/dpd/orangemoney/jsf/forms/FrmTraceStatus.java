/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.forms;

import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jsf.models.AbstractPortalForm;

/**
 * Modele de consultation des traces de statut d"une transaction
 * @author Yves LABO
 * @version 1.0
 */
public class FrmTraceStatus extends AbstractPortalForm {
	
	private Transaction currentObject = new Transaction();
	

	/**
	 * Default Constructor
	 */
	public FrmTraceStatus() {}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm() {
		// TODO Auto-generated method stub
		super.initForm();

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Consultation des traces de statut d'une transaction";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose();

		currentObject = null; 
	}	

	/**
	 * @return the currentObject
	 */
	public Transaction getCurrentObject() {
		return currentObject;
	}

	/**
	 * @param currentObject the currentObject to set
	 */
	public void setCurrentObject(Transaction currentObject) {
		this.currentObject = currentObject;
	}

	
}
