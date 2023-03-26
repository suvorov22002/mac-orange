package com.btow.om.register;

public class RegisterPortProxy implements com.btow.om.register.RegisterPort {
	private String _endpoint = null;
	private com.btow.om.register.RegisterPort registerPort = null;

	public RegisterPortProxy() {
		_initRegisterPortProxy();
	}

	public RegisterPortProxy(String endpoint) {
		_endpoint = endpoint;
		_initRegisterPortProxy();
	}

	private synchronized void _initRegisterPortProxy() {
		try {

			registerPort = (new com.btow.om.register.RegisterServiceLocator()).getRegisterPort();
			if (registerPort != null) {
				if (_endpoint != null)
					((javax.xml.rpc.Stub)registerPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
				else
					_endpoint = (String)((javax.xml.rpc.Stub)registerPort)._getProperty("javax.xml.rpc.service.endpoint.address");
			}

		}
		catch (javax.xml.rpc.ServiceException serviceException) {}
	}
	
	
	public String getEndpoint() {
		return _endpoint;
	}

	public synchronized void setEndpoint(String endpoint) {
		_endpoint = endpoint;
		if (registerPort != null)
			((javax.xml.rpc.Stub)registerPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);

	}

	public synchronized com.btow.om.register.RegisterPort getRegisterPort() {
		if (registerPort == null)
			_initRegisterPortProxy();
		return registerPort;
	}

	public synchronized short ombRequest(java.lang.String msisdn, javax.xml.rpc.holders.StringHolder alias, short code_service, java.lang.String libelle, java.lang.String devise, java.lang.String key,java.lang.String active_date) throws java.rmi.RemoteException{
		if (registerPort == null)
			_initRegisterPortProxy();
		return registerPort.ombRequest(msisdn, alias, code_service, libelle, devise, key, active_date);
	}

	public synchronized java.lang.String ombClose(javax.xml.rpc.holders.StringHolder alias,java.lang.String close_date, java.lang.String orig, java.lang.String motif) throws java.rmi.RemoteException{
		if (registerPort == null)
			_initRegisterPortProxy();
		return registerPort.ombClose(alias, close_date, orig, motif);
	}

	public synchronized void KYCRequest(java.lang.String msisdn, java.lang.String key, javax.xml.rpc.holders.StringHolder status, javax.xml.rpc.holders.StringHolder lastname, javax.xml.rpc.holders.StringHolder firstname, javax.xml.rpc.holders.StringHolder dob, javax.xml.rpc.holders.StringHolder cin) throws java.rmi.RemoteException{
		if (registerPort == null)
			_initRegisterPortProxy();
		registerPort.KYCRequest(msisdn, key, status, lastname, firstname, dob, cin);
	}


}