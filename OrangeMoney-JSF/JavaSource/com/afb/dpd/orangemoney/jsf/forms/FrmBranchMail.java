/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.forms;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.afb.dpd.orangemoney.jpa.entities.BranchMails;
import com.afb.dpd.orangemoney.jsf.models.AbstractPortalForm;
import com.afb.dpd.orangemoney.jsf.models.InformationDialog;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.models.PortalInformationHelper;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

import afb.dsi.dpd.portal.jpa.entities.Branch;
import afb.dsi.dpd.portal.jpa.entities.User;

/**
 * Modele du Formulaire de recherche des dossiers à traiter
 * @author Yves LABO
 * @version 2.0
 */
public class FrmBranchMail extends AbstractPortalForm {

	/**
	 * Liste des BranchMails
	 */
	private List<BranchMails> list = new ArrayList<BranchMails>();
	private BranchMails currentObject = new BranchMails();

	private List<SelectItem> userItem = new ArrayList<SelectItem>();
	private List<SelectItem> mailItems = new ArrayList<SelectItem>();
	private List<SelectItem> branchItems = new ArrayList<SelectItem>();
	HashMap<Object, Object> mapBranch = new HashMap<Object, Object>();
	HashMap<Object, Object> mapBranchExist = new HashMap<Object, Object>();


	private Long selectedUser, deletedId;
	private int num = 1;

	private String searchUser, selectedBranch, deletedUser;

	private boolean modification = Boolean.FALSE;

	/**
	 * Default Constructor
	 */
	public FrmBranchMail(){}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dsi.ddl.AnalyseCredit.jsf.models.AbstractPortalForm#initForm()
	 */

	@Override
	public void initForm() {
		// TODO Auto-generated method stub
		super.initForm(); clear();

		try {

			// Chargement des items du combo Agence
			List<Branch> branches = OMViewHelper.appDAOLocal.filter(Branch.class, null, RestrictionsContainer.getInstance().add(Restrictions.isNotNull("parent")), OrderContainer.getInstance().add(Order.asc("name")), null, 0, -1); // DAOLocal.filter(Branch.class, AliasesContainer.getInstance().add("type", "type"), rc, OrderContainer.getInstance().add(Order.asc("code")), null, 0, -1);
			branchItems = new ArrayList<SelectItem>();
			mapBranch = new HashMap<Object, Object>();branchItems.add(new SelectItem(null, " "));
			for(Branch b : branches) if(b.getType().getCode().equals("AG")) {
				mapBranch.put(b.getCode(),b);
				branchItems.add(new SelectItem(b.getCode(), b.getName() ));
			}
			branches.clear();


			RestrictionsContainer rc = RestrictionsContainer.getInstance();
			list = OMViewHelper.appManager.filterBranchMails(rc); // DAOLocal.filter(Branch.class, AliasesContainer.getInstance().add("type", "type"), rc, OrderContainer.getInstance().add(Order.asc("code")), null, 0, -1);
			mapBranchExist = new HashMap<Object, Object>();
			for(BranchMails b : list){
				mapBranchExist.put(b.getCodeAgence(),b);
			}

		} catch(Exception ex){

			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dsi.ddl.AnalyseCredit.jsf.models.AbstractPortalForm#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Paramétrage des adresses mails par agence";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dsi.ddl.AnalyseCredit.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose(); clear(); list.clear();

		// Liberation des ressources
		mailItems.clear();  branchItems.clear(); deletedId = null;
		mapBranch.clear(); userItem.clear();

	}


	public void clear(){
		selectedBranch = null; selectedUser = null; deletedUser = null;
		modification = Boolean.FALSE;  currentObject = new BranchMails();
	}



	/**
	 * Methode de recherche des process
	 */
	@SuppressWarnings("unused")
	public void sauvegarder(){

		num = 1;

		if(selectedBranch == null){
			// Msg Info
			PortalInformationHelper.showInformationDialog("Veuillez sélectionner une Agence SVP", InformationDialog.DIALOG_WARNING);
			// Annulation
			return;
		}
		
		currentObject.setCodeAgence(selectedBranch);

		Branch branch = new Branch ();
		BranchMails branchMails = new BranchMails ();
		
		branchMails = (BranchMails) mapBranchExist.get(currentObject.getCodeAgence());
		if(modification == Boolean.FALSE && branchMails != null){
			// Msg Info
			PortalInformationHelper.showInformationDialog("Veuillez sélectionner une autre agence, car celle-ci a deja été enregistree", InformationDialog.DIALOG_WARNING);
			// Annulation
			return;
		}

		branch = (Branch) mapBranch.get(currentObject.getCodeAgence());
		currentObject.setLibAgence(branch != null ? branch.getName() : null);
		
		if(currentObject.getMails().size() == 0){
			// Msg Info
			PortalInformationHelper.showInformationDialog("Veuillez définir au moins une adresse mail SVP", InformationDialog.DIALOG_WARNING);
			// Annulation
			return;
		}

		try{

			BranchMails savedObj = new BranchMails();
			if(currentObject.getId() != null){
				savedObj = OMViewHelper.appManager.updateBranchMails(currentObject);
			}
			else{
				savedObj = OMViewHelper.appManager.saveBranchMails(currentObject);
			}

			// MAJ de la liste
			RestrictionsContainer rc = RestrictionsContainer.getInstance();
			list = OMViewHelper.appManager.filterBranchMails(rc); // DAOLocal.filter(Branch.class, AliasesContainer.getInstance().add("type", "type"), rc, OrderContainer.getInstance().add(Order.asc("code")), null, 0, -1);
			mapBranchExist = new HashMap<Object, Object>();
			for(BranchMails b : list){
				mapBranchExist.put(b.getCodeAgence(),b);
			}

			savedObj = null;
			clear();

			// Msg d'information
			PortalInformationHelper.showInformationDialog("Enregistrement effectue avec succes!", InformationDialog.DIALOG_SUCCESS);

		}catch(Exception ex){

			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
		}

	}



	/**
	 * Recherche d'un utilisateur par une  partie du nom
	 */
	public void searchUsers(){

		try {
			
			// Test de la saisie d'un critere de recherche
			if(searchUser == null || searchUser.trim().isEmpty()){
				PortalInformationHelper.showInformationDialog("Veuillez saisir un critère de recherche SVP", InformationDialog.DIALOG_WARNING);
				return;
			}

			// Initialisation de la liste des items de Users
			userItem = new ArrayList<SelectItem>();
			userItem.add( new SelectItem(null, "") );

			// Recherche des utilisateurs en fonction du critere saisi
			List<User> usrs = OMViewHelper.appDAOLocal.filter(User.class, null, RestrictionsContainer.getInstance().add(Restrictions.ilike("name", "%" + searchUser.trim() + "%")).add(Restrictions.eq("suspended", Boolean.FALSE)), OrderContainer.getInstance().add(Order.asc("name")), null, 0, -1);

			// Chargement de la liste des items de Users
			for(User u : usrs) userItem.add( new SelectItem(u.getId(), u.getName()) );

			// Liberation memoire
			usrs.clear();
			searchUser = null;

		}catch(Exception ex){

			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
		}

	}




	/**
	 * Ajout de l'utilisateur a la liste des utilisateurs de l'evenement
	 */
	public void addUser(){

		try {

			// Test de la selection d'un utilisateur
			if(selectedUser == null) return;

			// Test de la selection d'une agence
			if(selectedBranch == null) return;

			// Recherche de l'utilisateur selectionne
			User user = OMViewHelper.appDAOLocal.findByPrimaryKey(User.class, selectedUser, null);

			if(Boolean.TRUE.equals(user.getSuspended())){
				PortalInformationHelper.showInformationDialog("Cet utilisateur a déjà été suspendu !", InformationDialog.DIALOG_WARNING);
				return;
			}

			// Recherche si l'utilsateur n'existe pas deja
			for(String p : currentObject.getMails()){
				if(user.getEmail().equals(p)){
					PortalInformationHelper.showInformationDialog("Cette adresse mail a déjà été ajouté à la liste! ", InformationDialog.DIALOG_WARNING);
					return;
				}
			}

			// Initialisation de la liste des utilisateurs
			if(currentObject.getMails() == null) currentObject.setMails( new ArrayList<String>() );

			// Ajout de l'utilisateur a la liste
			currentObject.getMails().add( user.getEmail() );

			searchUser = "";

		}catch(Exception ex){

			// Traitement de l'exception
			PortalExceptionHelper.threatException(ex);
		}
		num = 1;
	}



	/**
	 * Methode de suppression d'un Type de valeur
	 */
	public void delete() {

		try {

			// Suppression du degre de gravite
			OMViewHelper.appManager.deleteBranchMails(deletedId);

			// MAJ de la liste
			RestrictionsContainer rc = RestrictionsContainer.getInstance();
			list = OMViewHelper.appManager.filterBranchMails(rc); // DAOLocal.filter(Branch.class, AliasesContainer.getInstance().add("type", "type"), rc, OrderContainer.getInstance().add(Order.asc("code")), null, 0, -1);
			mapBranchExist = new HashMap<Object, Object>();
			for(BranchMails b : list){
				mapBranchExist.put(b.getCodeAgence(),b);
			}
			// Reinitialisation du formulaire
			clear();

			// Msg d'information
			PortalInformationHelper.showInformationDialog("Suppression effectuee avec succes!", InformationDialog.DIALOG_INFORMATION);

		} catch(Exception ex) {

			// Traitement de l'exception 
			PortalExceptionHelper.threatException(ex);
		}

	}



	/**
	 * @return the selectedBranch
	 */
	public String getSelectedBranch() {
		return selectedBranch;
	}

	/**
	 * @param selectedBranch the selectedBranch to set
	 */
	public void setSelectedBranch(String selectedBranch) {
		this.selectedBranch = selectedBranch;
	}

	/**
	 * @return the branchItems
	 */
	public List<SelectItem> getBranchItems() {
		return branchItems;
	}

	public void setBranchItems(List<SelectItem> branchItems) {
		this.branchItems = branchItems;
	}


	/**
	 * @return the num
	 */
	public int getNum() {
		return num++;
	}

	public List<BranchMails> getList() {
		return list;
	}

	public void setList(List<BranchMails> list) {
		this.list = list;
	}

	/**
	 * @return the currentObject
	 */
	public BranchMails getCurrentObject() {
		return currentObject;
	}

	/**
	 * @param currentObject the currentObject to set
	 */
	public void setCurrentObject(BranchMails currentObject) {
		this.currentObject = currentObject;

		if(this.currentObject == null) return;

		setSelectedBranch(this.currentObject.getCodeAgence());

		modification = Boolean.TRUE;
	}

	/**
	 * @return the mailItems
	 */
	public List<SelectItem> getMailItems() {
		return mailItems;
	}

	/**
	 * @param mailItems the mailItems to set
	 */
	public void setMailItems(List<SelectItem> mailItems) {
		this.mailItems = mailItems;
	}

	/**
	 * @return the userItem
	 */
	public List<SelectItem> getUserItem() {
		return userItem;
	}

	/**
	 * @param userItem the userItem to set
	 */
	public void setUserItem(List<SelectItem> userItem) {
		this.userItem = userItem;
	}


	public boolean isModification() {
		return modification;
	}

	public void setModification(boolean modification) {
		this.modification = modification;
	}


	/**
	 * @return the searchUser
	 */
	public String getSearchUser() {
		return searchUser;
	}

	/**
	 * @param searchUser the searchUser to set
	 */
	public void setSearchUser(String searchUser) {
		this.searchUser = searchUser;
	}


	/**
	 * @return the selectedUser
	 */
	public Long getSelectedUser() {
		return selectedUser;
	}

	/**
	 * @param selectedUser the selectedUser to set
	 */
	public void setSelectedUser(Long selectedUser) {
		this.selectedUser = selectedUser;
	}

	/**
	 * @return the deletedUser
	 */
	public String getDeletedUser() {
		return deletedUser;
	}


	/**
	 * @param deletedUser the deletedUser to set
	 */
	@SuppressWarnings("rawtypes")
	public void setDeletedUser(String deletedUser) {
		this.deletedUser = deletedUser;
		if(this.deletedUser == null) return;

		if(selectedUser == null) return;

		Iterator itr = currentObject.getMails().iterator();
		while(itr.hasNext()) {
			Object element = itr.next();
			if(((String) element).equals(deletedUser)){
				itr.remove();
				break;
			}
		}
	}


	/**
	 * @return the deletedId
	 */
	public Long getDeletedId() {
		return deletedId;
	}

	/**
	 * @param deletedId the deletedId to set
	 */
	public void setDeletedId(Long deletedId) {
		this.deletedId = deletedId;
		if(deletedId != null) delete();
	}


}
