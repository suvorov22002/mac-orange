/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.forms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jpa.enums.TransactionStatus;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jsf.models.AbstractPortalForm;
import com.afb.dpd.orangemoney.jsf.models.InformationDialog;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.models.PortalInformationHelper;
import com.afb.dpd.orangemoney.jsf.tools.OMTools;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.afb.dpd.orangemoney.worker.TransactionWorker;
import com.orange.b2w.statusinquiry.GlobalStatus;
import com.orange.b2w.statusinquiry.TypeID;
import com.yashiro.persistence.utils.dao.tools.OrderContainer;
import com.yashiro.persistence.utils.dao.tools.RestrictionsContainer;

import afb.dsi.dpd.portal.jpa.tools.PortalHelper;

/**
 * FrmUssdTrans
 * @author Francis KONCHOU
 * @version 1.0
 */
public class FrmUssdTrans extends AbstractPortalForm {

	/**
	 * Liste des transactions USSD
	 */
	private List<Transaction> list = new ArrayList<Transaction>();

	/**
	 * Transaction en cours
	 */
	private Transaction trans;

	/**
	 * Transaction incomplete
	 */
	private Transaction transInComplete;

	private Transaction transStatus;
	private Transaction transTraceStatus;

	/**
	 * Transaction selectionnee
	 */
	private String selectedTransId; 

	private Date txtDateDeb = new Date(), txtDateFin = new Date();
	private TypeOperation selectedTypOp;
	private TransactionStatus selectedTypStatus;
	private String nom;
	private String ncp;
	private String tel;
	private String selectedTypProcess, searchPhone;

	private List<SelectItem> typOpItem = new ArrayList<SelectItem>();
	private List<SelectItem> typStatusItem = new ArrayList<SelectItem>();
	private List<SelectItem> typProcessItem = new ArrayList<SelectItem>();

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH':'mm':'ss");

	private boolean fileIsGenerated = false;

	private boolean fileIsGeneratedReser = false;

	private boolean afterTFJO = false;

	private String exportFileName = "ListeTransactions.xls";

	private int num = 1;

	private String txtHeureDebut, txtHeureDebut2;
	private String txtHeureFin, txtHeureFin2;
	private List<SelectItem> itemsSelect = new ArrayList<SelectItem>();

	private String exportFileNameReser = "RESERVATION-OgMo-" + new SimpleDateFormat("ddMMyyyy").format(new Date()) + ".xlsx";


	/**
	 * Default Constructor
	 */
	public FrmUssdTrans(){}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm() {
		// TODO Auto-generated method stub
		super.initForm();  num = 1;

		// Chargement des items de types d'operations
		typOpItem.add( new SelectItem(null, "(Toutes)") );
		for(TypeOperation to : TypeOperation.getValues()) typOpItem.add( new SelectItem(to, to.getValue()) );

		// Chargement des etats des transactions
		typStatusItem.add( new SelectItem(null, " ") );
		for(TransactionStatus to : TransactionStatus.getValues()) typStatusItem.add( new SelectItem(to, to.getValue()) );

		/*
		typStatusItem.add( new SelectItem(TransactionStatus.SUCCESS, "Validé") );
		typStatusItem.add( new SelectItem(TransactionStatus.CANCEL, "Echec") );
		typStatusItem.add( new SelectItem(TransactionStatus.PROCESSING, "En Cours") );
		 */
		nom = "";

		// Chargement des niveaux de traitement
		typProcessItem.add( new SelectItem(null, " ") );
		//typProcessItem.add( new SelectItem("valide", "Validé") );
		//typProcessItem.add( new SelectItem("REVERSE", "Annulé") );

		txtHeureDebut = new SimpleDateFormat("HH").format(new Date());
		txtHeureDebut2 = new SimpleDateFormat("HH").format(new Date());

		itemsSelect.add( new SelectItem(null, "-hh-") );
		int i=0;
		while(i < 24){
			if(i < 10){
				itemsSelect.add(new SelectItem(0+Integer.toString(i),0+Integer.toString(i) + "h"));
			}
			else{
				itemsSelect.add(new SelectItem(Integer.toString(i),Integer.toString(i) + "h"));
			}

			i++;
		}

		afterTFJO = false;

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub Consulter les transactions Orange Mobile Money
		return "Consultation des Transactions Orange Walet Banking";
	}




	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose();
		searchPhone = null; typProcessItem.clear(); selectedTypProcess = null; selectedTypStatus = null;
		selectedTypOp = null; typOpItem.clear(); typStatusItem.clear(); num = 1; list.clear(); fileIsGenerated = false; fileIsGeneratedReser = false; afterTFJO = false;
		exportFileName = null; transInComplete = null; transStatus = null; transTraceStatus = null; txtHeureDebut = null; txtHeureFin = null; txtHeureDebut2 = null; txtHeureFin2 = null; itemsSelect.clear();
	}

	/**
	 * Recherche la liste des transactions ussd
	 */
	public void filterTransactions(){

		try {

			if(StringUtils.isNotBlank(txtHeureDebut) && StringUtils.isNotBlank(txtHeureFin) && Integer.parseInt(txtHeureFin) < Integer.parseInt(txtHeureDebut)){

				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Heure de fin doit etre supérieure a Heure de debut pour une même journée !",InformationDialog.DIALOG_WARNING);
				// On retourne false
				return ;

			}

			Date deb = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(txtDateDeb) + " " + (StringUtils.isNotBlank(txtHeureDebut) ? txtHeureDebut : "00") + ":00:00");
			Date fin = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(txtDateFin) + " " + (StringUtils.isNotBlank(txtHeureFin) ? txtHeureFin : "23") + ":59:59");

			// Initialisation d'un conteneur de restrictions
			RestrictionsContainer rc = RestrictionsContainer.getInstance().add(Restrictions.between("datetime", deb, fin));

			// Ajout des restrictions au conteneur
			if(selectedTypOp != null ) rc.add(Restrictions.eq("typeOperation", selectedTypOp));
			if(selectedTypStatus != null ) rc.add(Restrictions.eq("status", selectedTypStatus));
			if(nom != null  && !nom.trim().isEmpty()) rc.add(Restrictions.ilike("subscriber.customerName","%"+nom+"%",MatchMode.ANYWHERE));
			if(tel != null  && !tel.trim().isEmpty()) rc.add(Restrictions.ilike("phoneNumber","%"+tel+"%",MatchMode.ANYWHERE));
			if(ncp != null  && !ncp.trim().isEmpty()) rc.add(Restrictions.ilike("account","%"+ncp+"%",MatchMode.ANYWHERE));
			//*** rc.add(Restrictions.le("datetime",DateUtils.addMinutes(new Date(),-2)));
			OrderContainer orders = OrderContainer.getInstance().add(Order.desc("datetime"));
			list = OMViewHelper.appManager.filterTransactions(rc, orders);

			//list = OMViewHelper.appManager.filterTransactions(deb, fin, selectedTypOp, selectedTypStatus, nom, tel, ncp);

			num = 1;

		}catch(Exception ex){
			// Traitement de m'exception
			PortalExceptionHelper.threatException(ex);
		}

	}




	/**
	 * Recherche la liste des transactions ussd avec statut  chez Orange
	 */
	public void filterTransactionsStatus(){

		try {

			// Initialisation d'un conteneur de restrictions
			//*** RestrictionsContainer rc = RestrictionsContainer.getInstance().add(Restrictions.between("datetime", sdf.parseObject(txtDateDeb + " 00:00:00"), sdf.parseObject(txtDateFin + " 23:59:00")));
			if(StringUtils.isNotBlank(txtHeureDebut) && StringUtils.isNotBlank(txtHeureFin) && Integer.parseInt(txtHeureFin) <= Integer.parseInt(txtHeureDebut)){

				// Affichage de la Boite de dialogue d'information
				PortalInformationHelper.showInformationDialog("Heure de fin doit etre supérieure a Heure de debut pour une même journée !",InformationDialog.DIALOG_WARNING);
				// On retourne false
				return ;

			}

			Date deb = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(txtDateDeb) + " " + (StringUtils.isNotBlank(txtHeureDebut) ? txtHeureDebut : "00") + ":00:00");
			Date fin = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(txtDateFin) + " " + (StringUtils.isNotBlank(txtHeureFin) ? txtHeureFin : "23") + ":59:59");


			// Initialisation d'un conteneur de restrictions
			RestrictionsContainer rc = RestrictionsContainer.getInstance().add(Restrictions.between("datetime", deb, fin));
			// Ajout des restrictions au conteneur
			if(selectedTypOp != null ) rc.add(Restrictions.eq("typeOperation", selectedTypOp));
			if(selectedTypStatus != null ) rc.add(Restrictions.eq("status", selectedTypStatus));
			if(nom != null  && !nom.trim().isEmpty()) rc.add(Restrictions.ilike("subscriber.customerName","%"+nom+"%",MatchMode.ANYWHERE));
			if(tel != null  && !tel.trim().isEmpty()) rc.add(Restrictions.ilike("phoneNumber","%"+tel+"%",MatchMode.ANYWHERE));
			if(ncp != null  && !ncp.trim().isEmpty()) rc.add(Restrictions.ilike("account","%"+ncp+"%",MatchMode.ANYWHERE));
			//*** rc.add(Restrictions.le("datetime",DateUtils.addMinutes(new Date(),-2)));
			OrderContainer orders = OrderContainer.getInstance().add(Order.desc("datetime"));
			list = OMViewHelper.appManager.filterTransactions(rc, orders);

			for(Transaction t :list){

				t.setRequestId("");

				try {

					String KEY_DIR = System.getProperty("jboss.server.data.dir", ".") + File.separator + "keystore";
					//System.getProperties().setProperty("javax.net.ssl.trustStore",KEY_DIR+"\\new_keystore.ks");
					System.getProperties().setProperty("javax.net.ssl.trustStore",KEY_DIR+"\\keystore.ks");
					System.getProperties().setProperty("javax.net.ssl.trustStorePassword", "b2wafriland");


					//LastTransactions lt = OMViewHelper.inquiry.lastTransactions(t.getAccountAlias());
					//if(lt != null ) System.out.println(t.getSubscriber().getSubfirstname()+t.getAccountAlias()+"----lt----"+lt.getReturnCode()+"----");
					//if(lt.getTransactions() != null )System.out.println("--Gst---"+Arrays.toString(lt.getTransactions()));

					//System.out.println("------------ t.getExternalRefNo() --------------" + t.getExternalRefNo());

					com.orange.b2w.statusinquiry.TransactionStatus ts = OMViewHelper.inquiry.transactionStatusInquiry(TypeID.BANK_ID, t.getExternalRefNo().toString());
					if(ts != null )System.out.println("-2--ts--Null-"+ts);
					if(ts != null ){
						t.setOperatorCode(ts.getReturnCode());
						//System.out.println(t.getSubscriber().getSubfirstname()+"-2--ts---"+ts.getReturnCode()+"-----"+t.getExternalRefNo());
						if(ts.getDetailedStatuses() != null )System.out.println("-2--Dst---"+Arrays.toString(ts.getDetailedStatuses()));
						System.out.println("-2--Gst ts.getStatus() --- "+ts.getStatus());
						System.out.println("-2--Trans--- ts.getTransaction() "+ts.getTransaction());
						if(GlobalStatus.OK.equals(ts.getStatus())){
							t.setCompletStatus(TransactionStatus.SUCCESS);
						}if(GlobalStatus.KO.equals(ts.getStatus())){
							t.setCompletStatus(TransactionStatus.FAILED);
						}else{
							t.setCompletStatus(TransactionStatus.SUCCESS);
						}
					}else{
						t.setCompletStatus(TransactionStatus.PROCESSING);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

				if(t.getExceptionCode() != null)t.setRequestId(t.getExceptionCode().name());

			}

			num = 1;

		}catch(Exception ex){
			// Traitement de m'exception
			PortalExceptionHelper.threatException(ex);
		}

	}




	public void exportListeExcell(){
		try {

			if(list.isEmpty()){
				PortalInformationHelper.showInformationDialog("Aucune transaction dans la liste !",InformationDialog.DIALOG_INFORMATION);
				return;
			}

			exportFileName = "LISTE_TRANSACTIONS_"+new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date())+".xlsx";
			OMViewHelper.appManager.exportTransactionIntoExcelFile(list,exportFileName);

			fileIsGenerated = true;

			// Message d'information
			PortalInformationHelper.showInformationDialog("Export effectue avec succes! Vous pouvez telecharger le fichier généré.", InformationDialog.DIALOG_SUCCESS);

		} catch (Exception ex) {
			PortalExceptionHelper.threatException(ex);
			fileIsGenerated = false;
		}
	}


	public boolean isExportedFileExist() {
		return new File( OMTools.getDownloadDir() + exportFileName).exists() && fileIsGenerated;
	}



	public void reconciliationManuelle(){
		try {
			TransactionWorker.process();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			txtHeureDebut = new SimpleDateFormat("HH").format(new Date());
			Date deb = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(new Date()) + " " + (StringUtils.isNotBlank(txtHeureDebut) ? txtHeureDebut : "00") + ":00:00");
			Date fin = sdf.parse(PortalHelper.DEFAULT_DATE_FORMAT.format(new Date()) + " " + (StringUtils.isNotBlank(txtHeureFin) ? txtHeureFin : "23") + ":59:59");

			// Initialisation d'un conteneur de restrictions
			RestrictionsContainer rc = RestrictionsContainer.getInstance().add(Restrictions.between("datetime", deb, fin));

			// Ajout des restrictions au conteneur
			if(selectedTypOp != null ) rc.add(Restrictions.eq("typeOperation", selectedTypOp));
			if(selectedTypStatus != null ) rc.add(Restrictions.eq("status", selectedTypStatus));
			if(nom != null  && !nom.trim().isEmpty()) rc.add(Restrictions.ilike("subscriber.customerName","%"+nom+"%",MatchMode.ANYWHERE));
			if(tel != null  && !tel.trim().isEmpty()) rc.add(Restrictions.ilike("phoneNumber","%"+tel+"%",MatchMode.ANYWHERE));
			if(ncp != null  && !ncp.trim().isEmpty()) rc.add(Restrictions.ilike("account","%"+ncp+"%",MatchMode.ANYWHERE));
			//*** rc.add(Restrictions.le("datetime",DateUtils.addMinutes(new Date(),-2)));
			OrderContainer orders = OrderContainer.getInstance().add(Order.desc("datetime"));
			list = OMViewHelper.appManager.filterTransactions(rc, orders);

			//list = OMViewHelper.appManager.filterTransactions(deb, fin, selectedTypOp, selectedTypStatus, nom, tel, ncp);

			num = 1;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	/**
	 * Recherche la liste des transactions ussd
	 */
	public void executerReconciliation() {

		try {

			if(list == null || list.isEmpty()) return;

			// Initialisation de la liste des trx a reconcilier
			List<Transaction> tmp = new ArrayList<Transaction>();

			// Recuperation des transactions selectionnees
			for(Transaction t : list) if(t.isSelected()) tmp.add(t);

			if(tmp.isEmpty()) {

				// Msg d'information
				PortalInformationHelper.showInformationDialog("Aucune Transaction sélectionnée.", InformationDialog.DIALOG_WARNING);

			} else {

				// Execution de la reconciliation
				//OMViewHelper.appManager.executerReconciliation(tmp);

				// Suppression des transactions reconciliees de la liste
				for(int i=list.size()-1; i>=0; i--) if(list.get(i).isSelected()) list.remove(i);

				// Msg de confirmation
				PortalInformationHelper.showInformationDialog("Les Transactions sélectionnées ont été ré-exécutées avec succès!", InformationDialog.DIALOG_INFORMATION);

			}

		} catch(Exception ex) {

			// Traitement de m'exception
			PortalExceptionHelper.threatException(ex);
		}

		num = 1;
	}



	/**
	 * Methode de consultation du statut d'une transaction chez Orange
	 */
	public void checkStatusTransOrange() {

		try{

			// Recuperation du formulaire d'Analyse
			FrmTraceStatus traceStatusViewer = (FrmTraceStatus) OMViewHelper.getSessionManagedBean("frmTraceStatus");

			traceStatusViewer.setCurrentObject(TransactionWorker.getStatusTransaction(transStatus));

			// Ouverture du Formulaire d'analyse
			traceStatusViewer.open();

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
		}

	}



	public void suiviReservationFond() {

		if(StringUtils.isBlank(txtHeureFin2)) txtHeureFin2 = new SimpleDateFormat("mm").format(new Date());
		if(StringUtils.isBlank(txtHeureDebut2)){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Heure de debut non renseignée !",InformationDialog.DIALOG_WARNING);
			// On retourne false
			return ;
		}
		
		if(afterTFJO == Boolean.FALSE && StringUtils.isNotBlank(txtHeureDebut2) && StringUtils.isNotBlank(txtHeureFin2) && Integer.parseInt(txtHeureFin2) <= Integer.parseInt(txtHeureDebut2)){
			// Affichage de la Boite de dialogue d'information
			PortalInformationHelper.showInformationDialog("Heure de fin doit etre supérieure a Heure de debut pour une même journée !",InformationDialog.DIALOG_WARNING);
			// On retourne false
			return ;
		}

		try {
			OMViewHelper.appManager.suiviReservation(exportFileNameReser, txtHeureDebut2, txtHeureFin2, afterTFJO);
			fileIsGeneratedReser = true;

			// Message d'information
			PortalInformationHelper.showInformationDialog("Controle des reservations effectuee avec succes !", InformationDialog.DIALOG_SUCCESS);
		}catch(Exception ex) {
			// Traitement de l'exception
			//PortalExceptionHelper.threatException(ex);
			ex.printStackTrace();
		}
	}


	public boolean isExportedFileExistReser() {
		return new File( OMTools.getDownloadDir() + File.separator + exportFileNameReser).exists() && fileIsGeneratedReser;
	}


	/**
	 * @return the trans
	 */
	public Transaction getTrans() {
		return trans;
	}

	/**
	 * @param trans the trans to set
	 */
	public void setTrans(Transaction trans) {
		this.trans = trans;
	}


	public Transaction getTransInComplete() {
		return transInComplete;
	}

	public void setTransInComplete(Transaction transInComplete) {
		this.transInComplete = transInComplete;

		if(this.transInComplete == null){
			PortalInformationHelper.showInformationDialog("Veuillez choisir une transaction !",InformationDialog.DIALOG_INFORMATION);
			return;
		}

		if(Boolean.TRUE.equals(transInComplete.isCompleted())){
			PortalInformationHelper.showInformationDialog("Transaction déjà complétée !",InformationDialog.DIALOG_INFORMATION);
			return;
		}
		if(Boolean.TRUE.equals(transInComplete.getPosted())){
			PortalInformationHelper.showInformationDialog("Transaction déjà postée !",InformationDialog.DIALOG_INFORMATION);
			return;
		}

		if(!TypeOperation.Account2Wallet.equals(transInComplete.getTypeOperation()) && !TypeOperation.Wallet2Account.equals(transInComplete.getTypeOperation())){
			PortalInformationHelper.showInformationDialog("Transaction non reconciliable !",InformationDialog.DIALOG_INFORMATION);
			return;
		}
		try {
			transInComplete = TransactionWorker.processTransaction(transInComplete);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		try{

			Transaction transac  = OMViewHelper.appManager.findByPrimaryKey(Transaction.class, transInComplete.getId(), null);			
			if(Boolean.TRUE.equals(transac.isCompleted())){
				filterTransactions();
				PortalInformationHelper.showInformationDialog("Transaction complétée avec succes !",InformationDialog.DIALOG_SUCCESS);
			}	
			else{
				PortalInformationHelper.showInformationDialog("Denouement non effectué !",InformationDialog.DIALOG_WARNING);
			}


		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
		}
	}




	public Transaction getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(Transaction transStatus) {
		this.transStatus = transStatus; 
		if(this.transStatus != null) checkStatusTransOrange();
	}



	/**
	 * @return the transTraceStatus
	 */
	public Transaction getTransTraceStatus() {
		return transTraceStatus;
	}

	/**
	 * @param transTraceStatus the transTraceStatus to set
	 */
	public void setTransTraceStatus(Transaction transTraceStatus) {
		this.transTraceStatus = transTraceStatus;
		if(this.transTraceStatus == null) return;
		try{

			// Recuperation du formulaire d'Analyse
			FrmTraceStatus traceStatusViewer = (FrmTraceStatus) OMViewHelper.getSessionManagedBean("frmTraceStatus");

			traceStatusViewer.setCurrentObject(this.transTraceStatus);

			// Ouverture du Formulaire d'analyse
			traceStatusViewer.open();

		}catch(Exception e){
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	public String getNcp() {
		return ncp;
	}

	public void setNcp(String ncp) {
		this.ncp = ncp;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public void setList(List<Transaction> list) {
		this.list = list;
	}

	public void setTypOpItem(List<SelectItem> typOpItem) {
		this.typOpItem = typOpItem;
	}

	public void setTypStatusItem(List<SelectItem> typStatusItem) {
		this.typStatusItem = typStatusItem;
	}

	public void setTypProcessItem(List<SelectItem> typProcessItem) {
		this.typProcessItem = typProcessItem;
	}

	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return the selectedTransId
	 */
	public String getSelectedTransId() {
		return selectedTransId;
	}

	/**
	 * @param selectedTransId the selectedTransId to set
	 */
	public void setSelectedTransId(String selectedTransId) {
		this.selectedTransId = selectedTransId;
	}

	/**
	 * @return the list
	 */
	public List<Transaction> getList() {
		return list;
	}

	/**
	 * @return the txtDateDeb
	 */
	public Date getTxtDateDeb() {
		return txtDateDeb;
	}

	/**
	 * @param txtDateDeb the txtDateDeb to set
	 */
	public void setTxtDateDeb(Date txtDateDeb) {
		this.txtDateDeb = txtDateDeb;
	}

	/**
	 * @return the txtDateFin
	 */
	public Date getTxtDateFin() {
		return txtDateFin;
	}

	/**
	 * @param txtDateFin the txtDateFin to set
	 */
	public void setTxtDateFin(Date txtDateFin) {
		this.txtDateFin = txtDateFin;
	}

	public String getTxtHeureDebut() {
		return txtHeureDebut;
	}

	public void setTxtHeureDebut(String txtHeureDebut) {
		this.txtHeureDebut = txtHeureDebut;
	}

	public String getTxtHeureFin() {
		return txtHeureFin;
	}

	public void setTxtHeureFin(String txtHeureFin) {
		this.txtHeureFin = txtHeureFin;
	}

	/**
	 * @return the txtHeureDebut2
	 */
	public String getTxtHeureDebut2() {
		return txtHeureDebut2;
	}

	/**
	 * @param txtHeureDebut2 the txtHeureDebut2 to set
	 */
	public void setTxtHeureDebut2(String txtHeureDebut2) {
		this.txtHeureDebut2 = txtHeureDebut2;
	}

	/**
	 * @return the txtHeureFin2
	 */
	public String getTxtHeureFin2() {
		return txtHeureFin2;
	}

	/**
	 * @param txtHeureFin2 the txtHeureFin2 to set
	 */
	public void setTxtHeureFin2(String txtHeureFin2) {
		this.txtHeureFin2 = txtHeureFin2;
	}

	public List<SelectItem> getItemsSelect() {
		return itemsSelect;
	}

	public void setItemsSelect(List<SelectItem> itemsSelect) {
		this.itemsSelect = itemsSelect;
	}

	/**
	 * @return the searchPhone
	 */
	public String getSearchPhone() {
		return searchPhone;
	}

	/**
	 * @param searchPhone the searchPhone to set
	 */
	public void setSearchPhone(String searchPhone) {
		this.searchPhone = searchPhone;
	}


	/**
	 * @return the typOpItem
	 */
	public List<SelectItem> getTypOpItem() {
		return typOpItem;
	}

	/**
	 * @return the typStatusItem
	 */
	public List<SelectItem> getTypStatusItem() {
		return typStatusItem;
	}

	/**
	 * @return the typProcessItem
	 */
	public List<SelectItem> getTypProcessItem() {
		return typProcessItem;
	}

	/**
	 * @return the selectedTypOp
	 */
	public TypeOperation getSelectedTypOp() {
		return selectedTypOp;
	}

	/**
	 * @param selectedTypOp the selectedTypOp to set
	 */
	public void setSelectedTypOp(TypeOperation selectedTypOp) {
		this.selectedTypOp = selectedTypOp;
	}

	/**
	 * @return the selectedTypStatus
	 */
	public TransactionStatus getSelectedTypStatus() {
		return selectedTypStatus;
	}

	/**
	 * @param selectedTypStatus the selectedTypStatus to set
	 */
	public void setSelectedTypStatus(TransactionStatus selectedTypStatus) {
		this.selectedTypStatus = selectedTypStatus;
	}

	/**
	 * @return the selectedTypProcess
	 */
	public String getSelectedTypProcess() {
		return selectedTypProcess;
	}

	/**
	 * @param selectedTypProcess the selectedTypProcess to set
	 */
	public void setSelectedTypProcess(String selectedTypProcess) {
		this.selectedTypProcess = selectedTypProcess;
	}

	public int getNum() {
		return num++;
	}


	public String getExportFileName() {
		return exportFileName;
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}

	/**
	 * @return the exportFileNameReser
	 */
	public String getExportFileNameReser() {
		return exportFileNameReser;
	}

	/**
	 * @param exportFileNameReser the exportFileNameReser to set
	 */
	public void setExportFileNameReser(String exportFileNameReser) {
		this.exportFileNameReser = exportFileNameReser;
	}

	public boolean isFileIsGenerated() {
		return fileIsGenerated;
	}

	public void setFileIsGenerated(boolean fileIsGenerated) {
		this.fileIsGenerated = fileIsGenerated;
	}

	/**
	 * @return the fileIsGeneratedReser
	 */
	public boolean isFileIsGeneratedReser() {
		return fileIsGeneratedReser;
	}

	/**
	 * @param fileIsGeneratedReser the fileIsGeneratedReser to set
	 */
	public void setFileIsGeneratedReser(boolean fileIsGeneratedReser) {
		this.fileIsGeneratedReser = fileIsGeneratedReser;
	}

	/**
	 * @return the afterTFJO
	 */
	public boolean isAfterTFJO() {
		return afterTFJO;
	}

	/**
	 * @param afterTFJO the afterTFJO to set
	 */
	public void setAfterTFJO(boolean afterTFJO) {
		this.afterTFJO = afterTFJO;
	}



}
