package com.orange.b2w.statusinquiry;

import java.rmi.RemoteException;


public class TransactionStatusInquiryProxy implements com.orange.b2w.statusinquiry.TransactionStatusInquiry_PortType {

	private String _endpoint = null;
	private com.orange.b2w.statusinquiry.TransactionStatusInquiry_PortType transactionStatusInquiry = null;

	public TransactionStatusInquiryProxy() {
		_initTransactionStatusInquiryProxy();
	}

	public TransactionStatusInquiryProxy(String endpoint) {
		_endpoint = endpoint;
		_initTransactionStatusInquiryProxy();
	}

	private void _initTransactionStatusInquiryProxy() {
		try {
			transactionStatusInquiry = (new com.orange.b2w.statusinquiry.TransactionStatusInquiryImplServiceLocator()).getTransactionStatusInquiryImplPort();
			if (transactionStatusInquiry != null) {
				if (_endpoint != null)
					((javax.xml.rpc.Stub)transactionStatusInquiry)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
				else
					_endpoint = (String)((javax.xml.rpc.Stub)transactionStatusInquiry)._getProperty("javax.xml.rpc.service.endpoint.address");
			}

		}
		catch (javax.xml.rpc.ServiceException serviceException) {}
	}

	public String getEndpoint() {
		return _endpoint;
	}

	public void setEndpoint(String endpoint) {
		_endpoint = endpoint;
		if (transactionStatusInquiry != null)
			((javax.xml.rpc.Stub)transactionStatusInquiry)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);

	}

	public com.orange.b2w.statusinquiry.TransactionStatusInquiry_PortType getTransactionStatusInquiry() {
		if (transactionStatusInquiry == null)
			_initTransactionStatusInquiryProxy();
		return transactionStatusInquiry;
	}

	@Override
	public LastTransactions lastTransactions(String alias)throws RemoteException {
		if (transactionStatusInquiry == null)
			_initTransactionStatusInquiryProxy();
		    return transactionStatusInquiry.lastTransactions(alias);
	}

	@Override
	public TransactionStatus transactionStatusInquiry(TypeID typeID, String ID)throws RemoteException {
		if (transactionStatusInquiry == null)
			_initTransactionStatusInquiryProxy();
		
		return transactionStatusInquiry.transactionStatusInquiry(typeID, ID);
	}

}

