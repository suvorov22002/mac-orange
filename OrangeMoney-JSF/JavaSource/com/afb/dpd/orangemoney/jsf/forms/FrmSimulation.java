/**
 * 
 */
package com.afb.dpd.orangemoney.jsf.forms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.afb.dpd.orangemoney.jpa.entities.RequestMessage;
import com.afb.dpd.orangemoney.jpa.entities.Subscriber;
import com.afb.dpd.orangemoney.jpa.entities.Transaction;
import com.afb.dpd.orangemoney.jpa.enums.StatutContrat;
import com.afb.dpd.orangemoney.jpa.enums.TypeOperation;
import com.afb.dpd.orangemoney.jsf.models.AbstractPortalForm;
import com.afb.dpd.orangemoney.jsf.models.InformationDialog;
import com.afb.dpd.orangemoney.jsf.models.PortalExceptionHelper;
import com.afb.dpd.orangemoney.jsf.models.PortalInformationHelper;
import com.afb.dpd.orangemoney.jsf.tools.OMTools;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.banktowallet.b2w.b2w._1.TransactionDetail;

/**
 * Modele du formulaire de simulation d'envoi de messages de transaction Orange Money
 * @author Francis KONCHOU
 * @version 1.0
 */
public class FrmSimulation extends AbstractPortalForm {

	/**
	 * Message recu du client
	 */
	private RequestMessage message = new RequestMessage(TypeOperation.Account2Wallet, "", "237", 0d, "");

	private Map<String, String> map = new HashMap<String, String>();

	private Subscriber subs = new Subscriber();
	
	private String compteur = "";
	

	/**
	 * Items de types d'operations
	 */
	private List<SelectItem> opItems = new ArrayList<SelectItem>();

	Transaction trx = null;

	/**
	 * Default Constructor
	 */
	public FrmSimulation() {}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#initForm()
	 */
	@Override
	public void initForm() {
		// TODO Auto-generated method stub
		super.initForm();

		subs = new Subscriber();

		// Chargement des items de types d'operations
		opItems.add( new SelectItem(TypeOperation.Account2Wallet, TypeOperation.Account2Wallet.getValue()) );
		opItems.add( new SelectItem(TypeOperation.Wallet2Account, TypeOperation.Wallet2Account.getValue()) );
		opItems.add( new SelectItem(TypeOperation.BALANCE, TypeOperation.BALANCE.getValue()) );
		opItems.add( new SelectItem(TypeOperation.MiniStatement, TypeOperation.MiniStatement.getValue()) );
		
		compteur = "";

	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Simulateur de transactions Orange Money";
	}

	/*
	 * (non-Javadoc)
	 * @see com.afb.dpd.mobilemoney.jsf.models.AbstractPortalForm#disposeResourcesOnClose()
	 */
	@Override
	public void disposeResourcesOnClose() {
		// TODO Auto-generated method stub
		super.disposeResourcesOnClose();

		message = null;  subs = null;
	}

	/**
	 * Methode de test des preconditions d'envoi d'un message
	 * @return
	 */
	private boolean preConditionOK() throws Exception {

		trx = null;

		// Test du montant
		System.out.println("*********** message.getAmount() ************* : " + message.getAmount());
		if(message.getAmount() <= 0 && (message.getOperation().equals(TypeOperation.Account2Wallet) || message.getOperation().equals(TypeOperation.Wallet2Account))) {

			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("Montant incorrect", InformationDialog.DIALOG_WARNING);

			return false;
		}

		System.out.println("*********** message.getPhoneNumber() ************* : " + message.getPhoneNumber());
		subs = OMViewHelper.appManager.findSubscriberFromPhoneNumber(message.getPhoneNumber());

		// Test du numero de telephone
		if( subs == null ) {

			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("Numero de telephone inexistant!", InformationDialog.DIALOG_WARNING);

			return false;
		}

		// Test du numero de telephone
		if( subs.getStatus().equals(StatutContrat.SUSPENDU) ) {

			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("La souscription de ce client a été suspendue. Impossible d'effectuer des transactions Pull/Push from account", InformationDialog.DIALOG_WARNING);

			return false;
		}

		// Lecture du compte
		message.setAccount( subs.getAccounts().get(0) );

		// Test de l'existence du compte 
		if( OMViewHelper.appManager.isCompteFerme(message.getAccount()) ){

			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("Numéro de Compte inexistant ou en instance de fermeture. Impossible d'effectuer des transactions Pull/Push from account", InformationDialog.DIALOG_WARNING);

			return false;
		}

		// Si c'est un retrait (verification du solde)	
		if(message.getOperation().equals(TypeOperation.Account2Wallet)) {

			// Test du solde du compte 
			/**if( OMViewHelper.appManager.isSoldeSuffisant(message.getAccount(), message.getAmount()) ) {

				// Message d'avertissement
				PortalInformationHelper.showInformationDialog("Solde du compte insuffisant. Impossible de faire le PULL", InformationDialog.DIALOG_WARNING);

				return false;
			}*/
		}

		// test du PIN
		/* controle commenté
		if( !subs.getBankPIN().equals( Encrypter.getInstance().encryptText( message.getBankPIN() ) ) ) {

			// Message d'avertissement
			PortalInformationHelper.showInformationDialog("Code PIN incorrect", InformationDialog.DIALOG_WARNING);

			return false;
		}
		 */

		// Initialisation de la transaction
		trx = new Transaction(message.getOperation(), subs, message.getAmount(), message.getAccount(), message.getPhoneNumber());

		// Tout est OK
		return true;
	}

	/**
	 * Methode d'envoi du message au serveur pour traitement
	 */
	public void sendMessage() {

		try {
			
			if(StringUtils.isBlank(compteur)) {
				System.out.println("****************** sendMessage Test des preconditions ******************");
				// Test des preconditions
				if(!preConditionOK()) return;

				// Postage de l'evenement dans Delta
				//MobileMoneyViewHelper.appManager.posterEvenementDansCoreBanking(trx);
				String requestId = "OR" + new SimpleDateFormat("yyMMdd").format(new Date()) + RandomStringUtils.randomAlphanumeric(6).toUpperCase();

				if(message.getOperation().equals(TypeOperation.Account2Wallet)) {
					System.out.println("****************** sendMessage Account2Wallet ******************");
					map = OMViewHelper.appManager.processA2W(subs.getBankPIN(), message.getAmount(), 0d, requestId, requestId, requestId, "ORANGEMONEYCX", subs.getBankPIN());
				}	
				else if(message.getOperation().equals(TypeOperation.Wallet2Account)) {
					System.out.println("****************** sendMessage Wallet2Account ******************");
					map = OMViewHelper.appManager.processW2A(subs.getBankPIN(), message.getAmount(), 0d, requestId, requestId, requestId, "ORANGEMONEYCX", subs.getBankPIN());
				}
				else if(message.getOperation().equals(TypeOperation.BALANCE)) {
					System.out.println("****************** sendMessage BALANCE ******************");
					map = OMViewHelper.appManager.processBalance(subs.getBankPIN(), requestId);
				}
				else if(message.getOperation().equals(TypeOperation.MiniStatement)) {
					System.out.println("****************** sendMessage MiniStatement ******************");
					List<com.afb.dpd.orangemoney.jpa.entities.TransactionDetail> details = OMViewHelper.appManager.processGetMiniStament(subs.getBankPIN(), requestId);
					List<TransactionDetail> transactionList = new ArrayList<TransactionDetail>();
					for(com.afb.dpd.orangemoney.jpa.entities.TransactionDetail d : details){
						d.setCcy(OMTools.currencyXAF);
						transactionList.add(new TransactionDetail(d));
						map = d.getMap();
					}
				}
			}
			else {
				int i = 0;
				while(i<Integer.parseInt(compteur)) {
					
					String amountString = RandomStringUtils.randomNumeric(3);
					int amount = Integer.parseInt(amountString);
					while((amount % 100) != 0 || amount < 199) {
						amountString = RandomStringUtils.randomNumeric(3);
						amount = Integer.parseInt(amountString);
					}					
					message.setAmount(Double.parseDouble(amountString));
					System.out.println("****************** sendMessage message.getAmount() ****************** : " + message.getAmount());
					
					
					//Obtention des numero de téléphon
					String[] arr={"237694700692", "237696323145", "237698339297", "237695673175", "237696184709", "237694926147", "237696522512", "237696184709", "237699566725"};
			      	Random r=new Random();        
			      	int randomNumber=r.nextInt(arr.length);
			      	System.out.println(arr[randomNumber]);
			      	message.setPhoneNumber(String.valueOf(arr[randomNumber]));
			      	System.out.println("****************** sendMessage message.getPhoneNumber() ****************** : " + message.getPhoneNumber());
					
					if(preConditionOK()) {
						
						String requestId = "OR" + new SimpleDateFormat("yyMMdd").format(new Date()) + RandomStringUtils.randomAlphanumeric(6).toUpperCase();
						
						if(message.getOperation().equals(TypeOperation.Account2Wallet)) {
							System.out.println("****************** sendMessage Account2Wallet ******************");
							map = OMViewHelper.appManager.processA2W(subs.getBankPIN(), message.getAmount(), 0d, requestId, requestId, requestId, "ORANGEMONEYCX", subs.getBankPIN());
						}	
						else if(message.getOperation().equals(TypeOperation.Wallet2Account)) {
							System.out.println("****************** sendMessage Wallet2Account ******************");
							map = OMViewHelper.appManager.processW2A(subs.getBankPIN(), message.getAmount(), 0d, requestId, requestId, requestId, "ORANGEMONEYCX", subs.getBankPIN());
						}
						else if(message.getOperation().equals(TypeOperation.BALANCE)) {
							System.out.println("****************** sendMessage BALANCE ******************");
							map = OMViewHelper.appManager.processBalance(subs.getBankPIN(), requestId);
						}
						else if(message.getOperation().equals(TypeOperation.MiniStatement)) {
							System.out.println("****************** sendMessage MiniStatement ******************");
							List<com.afb.dpd.orangemoney.jpa.entities.TransactionDetail> details = OMViewHelper.appManager.processGetMiniStament(subs.getBankPIN(), requestId);
							List<TransactionDetail> transactionList = new ArrayList<TransactionDetail>();
							for(com.afb.dpd.orangemoney.jpa.entities.TransactionDetail d : details){
								d.setCcy(OMTools.currencyXAF);
								transactionList.add(new TransactionDetail(d));
								map = d.getMap();
							}
						}		
						
					}

					i++;
					System.out.println("---------------------------- message.getOperation() compteur i ---------------------------- : " + message.getOperation() + " ****** " + i);
					
					
				}
			}

		} catch(Exception e) {

			e.printStackTrace();

			// Traitement de l'exception
			PortalExceptionHelper.threatException(e);
		}

	}

	private void sendSMSConfirmation() {

		OMViewHelper.appManager.sendSMS("Cher Abonne. Votre cpte N° "+ message.getAccount().split("-")[1] +" a ete "+ (message.getOperation().equals(TypeOperation.Account2Wallet) ? "Debite" : "Credite") +" du montant "+ message.getAmount().toString() +" lors d'une transaction Pull/Push from account", message.getPhoneNumber());

	}

	
	public static String generateString() {
        //*** String uuid = UUID.randomUUID().;
        return  "OR" + new SimpleDateFormat("yyMMdd").format(new Date()) + RandomStringUtils.randomAlphanumeric(6).toUpperCase();
    }
	
	
	/**
	 * @return the message
	 */
	public RequestMessage getMessage() {
		return message;
	}

	/**
	 * @return the opItems
	 */
	public List<SelectItem> getOpItems() {
		return opItems;
	}

	/**
	 * @return the compteur
	 */
	public String getCompteur() {
		return compteur;
	}

	/**
	 * @param compteur the compteur to set
	 */
	public void setCompteur(String compteur) {
		this.compteur = compteur;
	}

	
	
	
	
}
