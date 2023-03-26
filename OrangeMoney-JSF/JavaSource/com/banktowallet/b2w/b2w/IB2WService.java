
package com.banktowallet.b2w.b2w;




import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import com.afb.dpd.orangemoney.jsf.tools.OMTools;
import com.afb.dpd.orangemoney.jsf.tools.OMViewHelper;
import com.banktowallet.b2w.b2w.CancelTransfer.TranRequestInfo;
import com.banktowallet.b2w.b2w._1.HeaderRequest;
import com.banktowallet.b2w.b2w._1.HeaderResponse;
import com.banktowallet.b2w.b2w._1.TransactionDetail;
import com.banktowallet.b2w.b2wold.BankExceptionCode;
import com.banktowallet.b2w.b2wold.RequestType;

/**
 *
 * @author Owner
 */
@WebService(serviceName = "IB2WService", portName = "B2WServicesPort", endpointInterface = "com.banktowallet.b2w.b2w.B2WServices", targetNamespace = "http://b2w.banktowallet.com/b2w", wsdlLocation = "WebContent/WEB-INF/wsdl/IB2WService/partner.wsdl")
public class IB2WService {

	/**
	 * cancelTransfer
	 * @param tranRequestInfo
	 * @return
	 */
	public com.banktowallet.b2w.b2w.CancelTransferResponse.Return cancelTransfer(com.banktowallet.b2w.b2w.CancelTransfer.TranRequestInfo tranRequestInfo){
		
		//TODO implement this method
		TranRequestInfo request = tranRequestInfo;
		HeaderRequest mmHeaderInfo = request.getMmHeaderInfo();
		String externalRefNo = request.getExternalRefNo();
		CancelTransferResponse.Return reponse = new CancelTransferResponse.Return();

		if(mmHeaderInfo.getOperatorCode() != null && mmHeaderInfo.getOperatorCode().equalsIgnoreCase(OMTools.operatorCode)){
			
			String requestId = mmHeaderInfo.getRequestId();
			String requestType = mmHeaderInfo.getRequestType();
			
			if(requestType.equalsIgnoreCase(RequestType.CANCELTRAN.toString()) && requestId!= null && !requestId.trim().isEmpty() && externalRefNo!= null && !externalRefNo.trim().isEmpty()){
				// Finds Subscriber
				Map<String, String> map = new HashMap<String, String>();
				try{

					map = OMViewHelper.appManager.processReversalTransaction(externalRefNo);
										
					HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
					rep.setResponseMessage(BankExceptionCode.mapCode.get(map.get("responseCode")));
					rep.setResponseCode(map.get("responseCode"));
					reponse.setCBAReferenceNo(map.get("CBAReferenceNo"));
					reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
					reponse.setMmHeaderInfo(rep);
					//return reponse;
					
				}catch(Exception e){
					e.printStackTrace(); 
					HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
					rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
					rep.setResponseCode("E01");
					reponse.setCBAReferenceNo(externalRefNo);
					reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
					reponse.setMmHeaderInfo(rep);
				}

			}else{
				HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
				rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
				rep.setResponseCode("E01");
				reponse.setCBAReferenceNo(externalRefNo);
				reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
				reponse.setMmHeaderInfo(rep);	
			}
			
		}else{
			HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
			rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
			rep.setResponseCode("E01");
			reponse.setCBAReferenceNo(externalRefNo);
			reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
			reponse.setMmHeaderInfo(rep);
		}

		return reponse;

	}
	
	

	/**
	 *  accountToWalletTransfer
	 * @param mobileTransferRequest
	 * @return
	 */
	public com.banktowallet.b2w.b2w.AccountToWalletTransferResponse.Return accountToWalletTransfer(com.banktowallet.b2w.b2w.AccountToWalletTransfer.MobileTransferRequest mobileTransferRequest) {
		System.out.println("--accountToWalletTransfer------Init------------------------");
		//TODO implement this method
		com.banktowallet.b2w.b2w.AccountToWalletTransfer.MobileTransferRequest request = mobileTransferRequest;
		HeaderRequest mmHeaderInfo = request.getMmHeaderInfo();
		AccountToWalletTransferResponse.Return reponse = new AccountToWalletTransferResponse.Return();
		String externalRefNo = request.getExternalRefNo();

		if(mmHeaderInfo.getOperatorCode() != null && mmHeaderInfo.getOperatorCode().equalsIgnoreCase(OMTools.operatorCode)){

			String requestId = mmHeaderInfo.getRequestId();
			String requestType = mmHeaderInfo.getRequestType();
			String accountAlias = request.getAccountAlias();

			if(requestType.equalsIgnoreCase(RequestType.A2W.toString()) && requestId!= null && !requestId.trim().isEmpty() && accountAlias!= null && !accountAlias.trim().isEmpty()){

				// Finds Subscriber
				Map<String, String> map = new HashMap<String, String>();
				
				try {
					
					Date dStart = new Date();
					// programme, boucle dont il faut mesurer le temps
					System.out.println("--accountToWalletTransfer------GO------------------------");
					Double amount = request.getAmount();
					String requestToken = mmHeaderInfo.getRequestToken();
					String operatorCode = mmHeaderInfo.getOperatorCode();
					Double charge = request.getCharge();
					map = OMViewHelper.appManager.processA2W(accountAlias, amount, charge, requestId, externalRefNo, requestToken, operatorCode, accountAlias);
					System.out.println("--accountToWalletTransfer------END------------------------");				
					HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
					rep.setResponseMessage(BankExceptionCode.mapCode.get(map.get("responseCode")));
					rep.setResponseCode(map.get("responseCode"));
					reponse.setCBAReferenceNo(map.get("CBAReferenceNo"));
					reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
					reponse.setMmHeaderInfo(rep);	
					
					Date dStop = new Date();
					System.out.println("Temps : " + (dStop.getTime() - dStart.getTime() + " "));
					
					//return reponse;
				}catch(Exception e){
					e.printStackTrace(); 
					HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
					rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
					rep.setResponseCode("E01");
					reponse.setCBAReferenceNo(externalRefNo);
					reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
					reponse.setMmHeaderInfo(rep);
				}
				
			}else{
				HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
				rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
				rep.setResponseCode("E01");
				reponse.setCBAReferenceNo(externalRefNo);
				reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
				reponse.setMmHeaderInfo(rep);				
			}
			
		}else{
			HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
			rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
			rep.setResponseCode("E01");
			reponse.setCBAReferenceNo(externalRefNo);
			reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
			reponse.setMmHeaderInfo(rep);
		}

		System.out.println(reponse.getMmHeaderInfo().getResponseCode()+"--accountToWalletTransfer------OK------------------------"+reponse.toString());
		
		return reponse;
		//throw new UnsupportedOperationException("Not implemented yet.");

	}

	/**
	 * walletToAccountTransfer
	 * @param mobileTransferRequest
	 * @return
	 */
	public com.banktowallet.b2w.b2w.WalletToAccountTransferResponse.Return walletToAccountTransfer(com.banktowallet.b2w.b2w.WalletToAccountTransfer.MobileTransferRequest mobileTransferRequest) {
		
		System.out.println("--walletToAccountTransfer------Init------------------------");
		//TODO implement this method		
		com.banktowallet.b2w.b2w.WalletToAccountTransfer.MobileTransferRequest request = mobileTransferRequest;
		HeaderRequest mmHeaderInfo = request.getMmHeaderInfo();
		WalletToAccountTransferResponse.Return reponse = new WalletToAccountTransferResponse.Return();
		String externalRefNo = request.getExternalRefNo();

		if(mmHeaderInfo.getOperatorCode() != null && mmHeaderInfo.getOperatorCode().equalsIgnoreCase(OMTools.operatorCode)){

			String requestId = mmHeaderInfo.getRequestId();
			String requestType = mmHeaderInfo.getRequestType();
			//String affiliateCode = mmHeaderInfo.getAffiliateCode();
			String accountAlias = request.getAccountAlias();
			if(requestType.equalsIgnoreCase(RequestType.W2A.toString()) && requestId!= null && !requestId.trim().isEmpty() && accountAlias!= null && !accountAlias.trim().isEmpty()){
				// Finds Subscriber
				Map<String, String> map = new HashMap<String, String>();
				try {
					
					Date dStart = new Date();
					// programme, boucle dont il faut mesurer le temps
					
					System.out.println("--walletToAccountTransfer------GO------------------------");
					Double amount = request.getAmount();
					String requestToken = mmHeaderInfo.getRequestToken();
					String operatorCode = mmHeaderInfo.getOperatorCode();
					Double charge = request.getCharge();
					map = OMViewHelper.appManager.processW2A(accountAlias, amount, charge, requestId, externalRefNo, requestToken, operatorCode, accountAlias);
					System.out.println("--walletToAccountTransfer------ENd------------------------");					
					HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
					rep.setResponseMessage(BankExceptionCode.mapCode.get(map.get("responseCode")));
					rep.setResponseCode(map.get("responseCode"));
					reponse.setCBAReferenceNo(map.get("CBAReferenceNo"));
					reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
					reponse.setMmHeaderInfo(rep);	
					
					Date dStop = new Date();
					System.out.println("Temps : " + (dStop.getTime() - dStart.getTime() + " "));
					
					//return reponse;	
				} catch (Exception e) {
					
					HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
					rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
					rep.setResponseCode("E01");
					reponse.setCBAReferenceNo(externalRefNo);
					reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
					reponse.setMmHeaderInfo(rep);
					e.printStackTrace(); 
				}
			}else{
				HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
				rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
				rep.setResponseCode("E01");
				reponse.setCBAReferenceNo(externalRefNo);
				reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
				reponse.setMmHeaderInfo(rep);
			}
		}else{
			HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
			rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
			rep.setResponseCode("E01");
			reponse.setCBAReferenceNo(externalRefNo);
			reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
			reponse.setMmHeaderInfo(rep);
		}

		System.out.println(reponse.getMmHeaderInfo().getResponseCode()+"--walletToAccountTransfer------OK------------------------"+reponse.toString());
		
		return reponse;

		//throw new UnsupportedOperationException("Not implemented yet.");

	}

	
	/**
	 * getAccountBalance
	 * @param accountBalanceInquiryRequest
	 * @return
	 */
	public com.banktowallet.b2w.b2w.GetAccountBalanceResponse.Return getAccountBalance(com.banktowallet.b2w.b2w.GetAccountBalance.AccountBalanceInquiryRequest accountBalanceInquiryRequest) {
		
		//TODO implement this method		
		com.banktowallet.b2w.b2w.GetAccountBalance.AccountBalanceInquiryRequest request = accountBalanceInquiryRequest;
		HeaderRequest mmHeaderInfo = request.getMmHeaderInfo();
		GetAccountBalanceResponse.Return reponse = new GetAccountBalanceResponse.Return();
		String accountAlias = request.getAccountAlias();
		
		// && mmHeaderInfo.getOperatorCode().equalsIgnoreCase(OMTools.operatorCode)
		if(mmHeaderInfo.getOperatorCode() != null ){

			String requestId = mmHeaderInfo.getRequestId();
			String requestType = mmHeaderInfo.getRequestType();
			//String affiliateCode = mmHeaderInfo.getAffiliateCode();
						
			if(requestType.equalsIgnoreCase(RequestType.GETACCBAL.toString()) && requestId!= null && !requestId.trim().isEmpty() && accountAlias!= null && !accountAlias.trim().isEmpty()){

				// Finds Subscriber
				Map<String, String> map = new HashMap<String, String>();
				try {
					
					map = OMViewHelper.appManager.processBalance(accountAlias,requestId);
					
					HeaderResponse rep = new HeaderResponse(mmHeaderInfo);				
					rep.setResponseMessage(BankExceptionCode.mapCode.get(map.get("responseCode")));
					rep.setResponseCode(map.get("responseCode"));
					reponse.setMmHeaderInfo(rep);
					reponse.setAccountAlias(accountAlias);
					reponse.setCcy(OMTools.currency);
					reponse.setAvailableBalance(Double.valueOf(map.get("amount")));
					reponse.setCurrentBalance(Double.valueOf(map.get("amount")));
					
				} catch (Exception e) {
					e.printStackTrace(); 
					HeaderResponse rep = new HeaderResponse(mmHeaderInfo);				
					rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
					rep.setResponseCode("E01");
					reponse.setMmHeaderInfo(rep);
					reponse.setAccountAlias(accountAlias);
					reponse.setCcy(OMTools.currency);
					reponse.setAvailableBalance(0d);
					reponse.setCurrentBalance(0d);
				}
			}else{
				HeaderResponse rep = new HeaderResponse(mmHeaderInfo);				
				rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
				rep.setResponseCode("E01");
				reponse.setMmHeaderInfo(rep);
				reponse.setAccountAlias(accountAlias);
				reponse.setCcy(OMTools.currency);
				reponse.setAvailableBalance(0d);
				reponse.setCurrentBalance(0d);
			}
		}else{
			HeaderResponse rep = new HeaderResponse(mmHeaderInfo);				
			rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
			rep.setResponseCode("E01");
			reponse.setMmHeaderInfo(rep);
			reponse.setAccountAlias(accountAlias);
			reponse.setCcy(OMTools.currency);
			reponse.setAvailableBalance(0d);
			reponse.setCurrentBalance(0d);
		}
		
		return reponse;

	}
	
	

	/**
	 * transferStatusInquiry
	 * @param tranRequestInfo
	 * @return
	 */
	public com.banktowallet.b2w.b2w.TransferStatusInquiryResponse.Return transferStatusInquiry(com.banktowallet.b2w.b2w.TransferStatusInquiry.TranRequestInfo tranRequestInfo){
		
		//TODO implement this method 		
		com.banktowallet.b2w.b2w.TransferStatusInquiry.TranRequestInfo request = tranRequestInfo;
		HeaderRequest mmHeaderInfo = request.getMmHeaderInfo();
		TransferStatusInquiryResponse.Return reponse = new TransferStatusInquiryResponse.Return();
		reponse.setExternalRefNo(mmHeaderInfo.getRequestId());
		String externalRefNo = request.getExternalRefNo();
		
		if(mmHeaderInfo.getOperatorCode() != null && mmHeaderInfo.getOperatorCode().equalsIgnoreCase(OMTools.operatorCode)){

			String requestId = mmHeaderInfo.getRequestId();
			String requestType = mmHeaderInfo.getRequestType();
			
			if(requestType.equalsIgnoreCase(RequestType.TRANINQ.toString()) && requestId!= null && !requestId.trim().isEmpty() && externalRefNo!= null && !externalRefNo.trim().isEmpty()){

				// Finds Subscriber
				Map<String, String> map = new HashMap<String, String>();
				
				try {
					
					map = OMViewHelper.appManager.processStatutTransaction(externalRefNo);
										
					HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
					rep.setResponseMessage(BankExceptionCode.mapCode.get(map.get("responseCode")));
					rep.setResponseCode(map.get("responseCode"));
					reponse.setCBAReferenceNo(map.get("CBAReferenceNo"));
					reponse.setMmHeaderInfo(rep);
					//return reponse;
					
				} catch (Exception e) {
					HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
					rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
					rep.setResponseCode("E01");
					reponse.setCBAReferenceNo(externalRefNo);	
					reponse.setMmHeaderInfo(rep);
					e.printStackTrace(); 
				}
			}else{
				HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
				rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
				rep.setResponseCode("E01");
				reponse.setCBAReferenceNo(externalRefNo);	
				reponse.setMmHeaderInfo(rep);
			}
		}else{
			HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
			rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
			rep.setResponseCode("E01");
			reponse.setCBAReferenceNo(externalRefNo);	
			reponse.setMmHeaderInfo(rep);
		}
		

		return reponse;
		//throw new UnsupportedOperationException("Not implemented yet.");

	}
	
	

	/**
	 * getMiniStatement
	 * @param miniStatementRequest
	 * @return
	 */
	public com.banktowallet.b2w.b2w.GetMiniStatementResponse.Return getMiniStatement(com.banktowallet.b2w.b2w.GetMiniStatement.MiniStatementRequest miniStatementRequest){
		
		//TODO implement this method	
		com.banktowallet.b2w.b2w.GetMiniStatement.MiniStatementRequest request = miniStatementRequest;
		HeaderRequest mmHeaderInfo = request.getMmHeaderInfo();
		GetMiniStatementResponse.Return reponse = new GetMiniStatementResponse.Return();
		
		if(mmHeaderInfo.getOperatorCode() != null && mmHeaderInfo.getOperatorCode().equalsIgnoreCase(OMTools.operatorCode)){
			
			String requestId = mmHeaderInfo.getRequestId();
			String requestType = mmHeaderInfo.getRequestType();
			//String affiliateCode = mmHeaderInfo.getAffiliateCode();
			String accountAlias = request.getAccountAlias();
			
			if(requestType.equalsIgnoreCase(RequestType.GETMINI.toString()) && requestId!= null && !requestId.trim().isEmpty() && accountAlias!= null && !accountAlias.trim().isEmpty()){
				
				// Finds Subscriber
				Map<String, String> map = new HashMap<String, String>();
				List<com.afb.dpd.orangemoney.jpa.entities.TransactionDetail> details = new ArrayList<com.afb.dpd.orangemoney.jpa.entities.TransactionDetail>();
				
				try{
			
					details = OMViewHelper.appManager.processGetMiniStament(accountAlias,requestId);
					
					List<TransactionDetail> transactionList = new ArrayList<TransactionDetail>();
					for(com.afb.dpd.orangemoney.jpa.entities.TransactionDetail d : details){
						d.setCcy(OMTools.currencyXAF);
						transactionList.add(new TransactionDetail(d));
						map = d.getMap();
					}

					HeaderResponse rep = new HeaderResponse(mmHeaderInfo);					
					if(map.get("responseCode").equalsIgnoreCase("000")){
						rep.setResponseMessage(BankExceptionCode.mapCode.get(map.get("responseCode")));
						rep.setResponseCode(map.get("responseCode"));
						reponse.setMmHeaderInfo(rep);
						reponse.setTransactionList(transactionList);
					}else{
						reponse.setMmHeaderInfo(rep);
						rep.setResponseCode(map.get("responseCode"));
						rep.setResponseMessage(BankExceptionCode.mapCode.get(map.get("responseCode")));
					}
					
					return reponse;
					
				} catch (Exception e) {
					HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
					rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
					rep.setResponseCode("E01");
					reponse.setMmHeaderInfo(rep);
					e.printStackTrace(); 
				}
			}else{
				HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
				rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
				rep.setResponseCode("E01");
				reponse.setMmHeaderInfo(rep);
			}	
		}else{
			
			HeaderResponse rep = new HeaderResponse(mmHeaderInfo);
			rep.setResponseMessage(BankExceptionCode.mapCode.get("E01"));
			rep.setResponseCode("E01");
			reponse.setMmHeaderInfo(rep);			
		}

		return reponse;
		//throw new UnsupportedOperationException("Not implemented yet.");

	}
	
}


