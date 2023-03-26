/**
 * TransactionStatusInquiryImplServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.orange.b2w.statusinquiry;

public class TransactionStatusInquiryImplServiceSoapBindingStub extends org.apache.axis.client.Stub implements com.orange.b2w.statusinquiry.TransactionStatusInquiry_PortType {
	private java.util.Vector cachedSerClasses = new java.util.Vector();
	private java.util.Vector cachedSerQNames = new java.util.Vector();
	private java.util.Vector cachedSerFactories = new java.util.Vector();
	private java.util.Vector cachedDeserFactories = new java.util.Vector();

	static org.apache.axis.description.OperationDesc [] _operations;

	static {
		_operations = new org.apache.axis.description.OperationDesc[2];
		_initOperationDesc1();
	}

	private static void _initOperationDesc1(){
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("TransactionStatusInquiry");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "typeID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "typeID"), com.orange.b2w.statusinquiry.TypeID.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "transactionStatus"));
		oper.setReturnClass(com.orange.b2w.statusinquiry.TransactionStatus.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "statusInquiry"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[0] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("LastTransactions");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "alias"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "lastTransactions"));
		oper.setReturnClass(com.orange.b2w.statusinquiry.LastTransactions.class);
		oper.setReturnQName(new javax.xml.namespace.QName("", "lastTransactions"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[1] = oper;

	}

	public TransactionStatusInquiryImplServiceSoapBindingStub() throws org.apache.axis.AxisFault {
		this(null);
	}

	public TransactionStatusInquiryImplServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public TransactionStatusInquiryImplServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}
		((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
		java.lang.Class cls;
		javax.xml.namespace.QName qName;
		javax.xml.namespace.QName qName2;
		java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
		java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
		java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
		java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
		java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
		java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
		java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
		java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
		java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
		java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "detailedStatus");
		cachedSerQNames.add(qName);
		cls = com.orange.b2w.statusinquiry.DetailedStatus.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "detailedStatuses");
		cachedSerQNames.add(qName);
		cls = com.orange.b2w.statusinquiry.DetailedStatus[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "detailedStatus");
		qName2 = new javax.xml.namespace.QName("", "detailedStatus");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "globalStatus");
		cachedSerQNames.add(qName);
		cls = com.orange.b2w.statusinquiry.GlobalStatus.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(enumsf);
		cachedDeserFactories.add(enumdf);

		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "lastTransactions");
		cachedSerQNames.add(qName);
		cls = com.orange.b2w.statusinquiry.LastTransactions.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "step");
		cachedSerQNames.add(qName);
		cls = com.orange.b2w.statusinquiry.Step.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(enumsf);
		cachedDeserFactories.add(enumdf);

		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "stepStatus");
		cachedSerQNames.add(qName);
		cls = com.orange.b2w.statusinquiry.StepStatus.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(enumsf);
		cachedDeserFactories.add(enumdf);

		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "transaction");
		cachedSerQNames.add(qName);
		cls = com.orange.b2w.statusinquiry.Transaction.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "transactions");
		cachedSerQNames.add(qName);
		cls = com.orange.b2w.statusinquiry.Transaction[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "transaction");
		qName2 = new javax.xml.namespace.QName("", "transaction");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "transactionStatus");
		cachedSerQNames.add(qName);
		cls = com.orange.b2w.statusinquiry.TransactionStatus.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "transactionType");
		cachedSerQNames.add(qName);
		cls = com.orange.b2w.statusinquiry.TransactionType.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(enumsf);
		cachedDeserFactories.add(enumdf);

		qName = new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "typeID");
		cachedSerQNames.add(qName);
		cls = com.orange.b2w.statusinquiry.TypeID.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(enumsf);
		cachedDeserFactories.add(enumdf);

	}

	protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
		try {
			org.apache.axis.client.Call _call = super._createCall();
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}
			java.util.Enumeration keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				java.lang.String key = (java.lang.String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}
			// All the type mapping information is registered
			// when the first call is made.
			// The type mapping information is actually registered in
			// the TypeMappingRegistry of the service, which
			// is the reason why registration is only needed for the first call.
			synchronized (this) {
				if (firstCall()) {
					// must set encoding style before registering serializers
					_call.setEncodingStyle(null);
					for (int i = 0; i < cachedSerFactories.size(); ++i) {
						java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
						javax.xml.namespace.QName qName =
							(javax.xml.namespace.QName) cachedSerQNames.get(i);
						java.lang.Object x = cachedSerFactories.get(i);
						if (x instanceof Class) {
							java.lang.Class sf = (java.lang.Class)
							cachedSerFactories.get(i);
							java.lang.Class df = (java.lang.Class)
							cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
						else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
							org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
							cachedSerFactories.get(i);
							org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
							cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
					}
				}
			}
			return _call;
		}
		catch (java.lang.Throwable _t) {
			throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
		}
	}

	public com.orange.b2w.statusinquiry.TransactionStatus transactionStatusInquiry(com.orange.b2w.statusinquiry.TypeID typeID, java.lang.String ID) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "TransactionStatusInquiry"));

		
		setRequestHeaders(_call);
		setAttachments(_call);
		try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {typeID, ID});

		if (_resp instanceof java.rmi.RemoteException) {
			throw (java.rmi.RemoteException)_resp;
		}
		else {
			extractAttachments(_call);
			try {
				return (com.orange.b2w.statusinquiry.TransactionStatus) _resp;
			} catch (java.lang.Exception _exception) {
				return (com.orange.b2w.statusinquiry.TransactionStatus) org.apache.axis.utils.JavaUtils.convert(_resp, com.orange.b2w.statusinquiry.TransactionStatus.class);
			}
		}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public com.orange.b2w.statusinquiry.LastTransactions lastTransactions(java.lang.String alias) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[1]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "LastTransactions"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try { java.lang.Object _resp = _call.invoke(new java.lang.Object[] {alias});

		if (_resp instanceof java.rmi.RemoteException) {
			throw (java.rmi.RemoteException)_resp;
		}
		else {
			extractAttachments(_call);
			try {
				return (com.orange.b2w.statusinquiry.LastTransactions) _resp;
			} catch (java.lang.Exception _exception) {
				return (com.orange.b2w.statusinquiry.LastTransactions) org.apache.axis.utils.JavaUtils.convert(_resp, com.orange.b2w.statusinquiry.LastTransactions.class);
			}
		}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

}
