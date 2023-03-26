package com.afb.dpd.orangemoney.jpa.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Type d'operations
 * @author Francis KONCHOU
 * @version 1.0
 */
public enum ExceptionCode {
	
	/**
	 * Pull from account
	 */
	SubscriberInvalidPIN("Invalid.Bank.PIN"),
	SubscriberInvalidPhone("Invalid.Phone.Number"),
	SubscriberInvalidAccount("Invalid.Account.Number"),
	SubscriberInvalidAmount("Invalid.Amount"),
	
	SubscriberInvalidAmountTransaction("Invalid.Amount.Transaction"),
	SubscriberInvalidAmountDay("Invalid.Amount.Day"),
	SubscriberInvalidAmountWeek("Invalid.Amount.Week"),
	SubscriberInvalidAmountMonth("Invalid.Amount.Month"),
	
	SubscriberSuspended("Suspended.Contract"),
	
	BankClosingAccount("Close.Or.Closing.Account.Error"),
	BankBlockingDebitAccount("Debit.Blocking.Account.Error"),
	BankBlockingCreditAccount("Credit.Blocking.Account.Error"),
	BankInsufficientBalance("Insufficient.Balance"),
	OrangeMoneyExceededAmount("OGMo.Ceiling.Exceeded"),
	BankExceededAmount("Bank.Ceiling.Exceeded"),
	BankEndOfDay("Bank.EndOftDay.Exceeded"),
	BankException("Bank.Exception.Exceeded"),
	
	WalletException("Wallet.Exception.Exceeded"),
	
	NetWorkSendingMoMoRequest("Sending.MoMo.Resquest.Error"),
	NetWorkReceivingMoMoRequestResponse("Receiving.MoMo.Request.Response.Error"),
	NetWorkReceivingMoMoRequestConfirmation("Receiving.MoMo.Request.Confirmation.Error"),
	NetWorkSendingMoMoRequestConfirmation("Sending.MoMo.Request.Confirmation.Error"),
	
	SystemCoreBankingSystemAcces("Bank.Core.Banking.System.Error"),
	SystemCommittingTransaction("Bank.Commit.Transaction.Error");
	
	/**
	200("OK: validation"),
	
	302("KO: invalid request"),
	
	303 KO: Wrong MSISDN format
	
	304 KO: Unknown alias
	
	306 Invalid Currency
	
	307 Invalid service code
	
	601 KO: Alias already exists
	
	602 KO: Unknown request or timeout
	
	603 KO: Invalid activation key
	
	604 KO: Platform unavailable*/
	
	/**
	 * Valeur
	 */
	private String value;
	
	/**
	 * Constructeur
	 * @param value
	 */
	private ExceptionCode(String value){
		this.setValue(value);
	}
	
	/**
	 * Retourne la liste des valeus
	 * @return
	 */
	public static List<ExceptionCode> getValues() {
		
		// Initialisation de la collection a retourner
		List<ExceptionCode> ops = new ArrayList<ExceptionCode>();
		
		// Ajout des valeurs
		ops.add(SubscriberInvalidPIN);
		ops.add(SubscriberInvalidPhone);
		ops.add(SubscriberInvalidAccount);
		
		ops.add(BankClosingAccount);
		ops.add(BankBlockingDebitAccount);
		ops.add(BankBlockingCreditAccount);
		ops.add(BankInsufficientBalance);
		ops.add(OrangeMoneyExceededAmount);
		ops.add(BankExceededAmount);
		
		ops.add(NetWorkSendingMoMoRequest);
		ops.add(NetWorkReceivingMoMoRequestResponse);
		ops.add(NetWorkReceivingMoMoRequestConfirmation);
		ops.add(NetWorkSendingMoMoRequestConfirmation);
		
		ops.add(SystemCoreBankingSystemAcces);
		ops.add(SystemCommittingTransaction);
		
		ops.add(WalletException);
		
		ops.add(SubscriberInvalidAmountTransaction);
		ops.add(SubscriberInvalidAmountDay);
		ops.add(SubscriberInvalidAmountWeek);
		ops.add(SubscriberInvalidAmountMonth);
				
		
		// Retourne la collection
		return ops;
		
	}
	
	

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
