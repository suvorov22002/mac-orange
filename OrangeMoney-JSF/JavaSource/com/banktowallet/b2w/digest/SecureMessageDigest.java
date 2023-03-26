package com.banktowallet.b2w.digest;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MessageDigest
 * @author Francis KONCHOU
 * @version 1.0
 */
public class SecureMessageDigest {
	
	
	public static void main(String[] args) {
		String monMessage = "Mon message";

		calculerValeurDeHachage("MD2", monMessage);
		calculerValeurDeHachage("MD5", monMessage);
		calculerValeurDeHachage("SHA-1", monMessage);
		calculerValeurDeHachage("SHA-256", monMessage);
		calculerValeurDeHachage("SHA-384", monMessage);
		calculerValeurDeHachage("SHA-512", monMessage);

	}

	public static byte[] calculerValeurDeHachage(String algorithme,	String monMessage) {
		byte[] digest = null;
		try {
			MessageDigest sha = MessageDigest.getInstance(algorithme);
			//digest = sha.digest(monMessage.getBytes(), 1, 1);
			int val = sha.digest(monMessage.getBytes(), 1, 1);
			System.out.println("algorithme : " + algorithme);
			System.out.println("algorithme : " + val);
			System.out.println(bytesToHex(digest));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (DigestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return digest;
	}

	public static String bytesToHex(byte[] b) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		StringBuffer buffer = new StringBuffer();
		for (int j = 0; j < b.length; j++) {
			buffer.append(hexDigits[(b[j] >> 4) & 0x0f]);
			buffer.append(hexDigits[b[j] & 0x0f]);
		}
		return buffer.toString();
	}

}
