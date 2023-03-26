package com.btow.om.register;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.orange.b2w.statusinquiry.TransactionStatus;
import com.orange.b2w.statusinquiry.TransactionStatusInquiryProxy;
import com.orange.b2w.statusinquiry.TypeID;

public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {

			//Logger logger 
			//AfrilandIdle idle = new AfrilandIdle();
			//AfrilandIdlePort service1 = idle.getService();
			//System.out.println("-----------------"+service1.setIdle(""));			
			//AfrilandInquiry inquiry = new AfrilandInquiry();
			//AfrilandInquiryPort service2 = inquiry.getService();
			//System.out.println("-----------------"+service2.LastTransactions(LastTransactions));
			//System.out.println("-----------------"+service2.TransactionStatusInquiry(transactionStatusInquiry));

			System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\Owner\\.keystore");
			System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

			//RegisterPortProxy serv = new RegisterPortProxy();
			System.out.println("-------OK----------");



			//URL wsdlURL = this.getClass().getClassLoader().getResource("MyWebService.wsdl");

			//RegisterPort serv = reg.getService();
			System.out.println("-------OK----------");
			//KYCRequest request = new KYCRequest();
			//KYCREQ res = new KYCREQ();
			//res.setKey("CSPU537Z");
			//res.setMsisdn("7702100003");
			//request.setKYCREQ(res);
			System.out.println("-------GO----------"+DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));


			try {

				//System.out.println("-----------"+DateFormatUtils.format(new Date(),"mmss"));


				TransactionStatusInquiryProxy st = new TransactionStatusInquiryProxy();
				TransactionStatus status = st.transactionStatusInquiry(TypeID.BANK_ID,"1821588");

				System.out.println("-----------------"+status.getReturnCode());
				System.out.println("-----------------"+status.getStatus());
				System.out.println("-----------------"+status.getTransaction());

				status = st.transactionStatusInquiry(TypeID.ORANGE_ID,"1821588");

				System.out.println("-----------------"+status.getReturnCode());
				System.out.println("-----------------"+status.getStatus());
				System.out.println("-----------------"+status.getTransaction());

			} catch (Exception e) {
				e.printStackTrace();
			}

			/**try {
				System.out.println("-----------------"+serv.KYCRequest(res));
			} catch (Exception e) {
				e.printStackTrace();
			}*/


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
