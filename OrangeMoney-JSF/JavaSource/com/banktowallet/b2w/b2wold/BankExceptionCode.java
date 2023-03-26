package com.banktowallet.b2w.b2wold;

import java.util.HashMap;

/**
 * Code Erreur que les Services de la bank peuvent retourner
 * @author Francis K
 * @version 1.0
 */
public class BankExceptionCode {
	
		
	public static HashMap<String, String> mapCode = new HashMap<String, String>(); 
	
	 static {
		 
		 mapCode.put("000", "Success");
		 mapCode.put("010", "Bank Technical Error");
		 mapCode.put("E01", "Bank Technical Error");
		 mapCode.put("E02", "Bank Technical Error");
		 mapCode.put("E03", "Bank Technical Error");
		 mapCode.put("E04", "Bank Technical Error");
		 mapCode.put("E05", "Bank Technical Error");
		 mapCode.put("E06", "Bank Technical Error");
		 mapCode.put("E07", "Bank Technical Error");
		 mapCode.put("E08", "Bank Technical Error");
		 mapCode.put("E10", "Bank Technical Error");
		 mapCode.put("E09", "Bank Technical Error");
		 mapCode.put("E11", "Operation in Timeout. Use TransferStatusInquiry to check status");
		 mapCode.put("E12", "Bank Technical Error");
		 mapCode.put("E13", "Unauthorized Request on Account (crossed thresolds)");
		 mapCode.put("E14", "Unauthorized Request on Account (crossed thresolds)");
		 mapCode.put("E15", "Unauthorized Request on Account (crossed thresolds)");
		 mapCode.put("E16", "Insufficient Balance");
		 mapCode.put("E17", "Bank Technical Error");
		 mapCode.put("E18", "Bank Technical Error");
		 mapCode.put("E19", "Bank Technical Error");
		 mapCode.put("E20", "Bank Technical Error");
		 mapCode.put("E22", "Transaction Violates Account's Minimum Balance");
		 mapCode.put("E35", "Bank Technical Error");
		 mapCode.put("E99", "Bank Technical Error");
		 
		 
		 mapCode.put("200", "OK: validation");
		 mapCode.put("302", "KO: invalid request");
		 mapCode.put("303", "KO: Wrong MSISDN format");
		 mapCode.put("304", "KO: Unknown alias");
		 mapCode.put("306", "Invalid Currency");
		 mapCode.put("307", "Invalid service code");
		 mapCode.put("601", "KO: Alias already exists");
		 mapCode.put("602", "KO: Unknown request or timeout");
		 mapCode.put("603", "KO: Invalid activation key");
		 mapCode.put("604", "KO: Platform unavailable");
		
				 
	 }

}
