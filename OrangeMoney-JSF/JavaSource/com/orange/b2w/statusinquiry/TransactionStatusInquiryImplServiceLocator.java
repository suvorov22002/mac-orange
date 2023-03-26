/**
 * TransactionStatusInquiryImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.orange.b2w.statusinquiry;

import com.afb.dpd.orangemoney.jsf.tools.OMTools;

public class TransactionStatusInquiryImplServiceLocator extends org.apache.axis.client.Service implements com.orange.b2w.statusinquiry.TransactionStatusInquiryImplService {

    public TransactionStatusInquiryImplServiceLocator() {
    }


    public TransactionStatusInquiryImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TransactionStatusInquiryImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TransactionStatusInquiryImplPort
    private java.lang.String TransactionStatusInquiryImplPort_address = OMTools.inquiryService;

    public java.lang.String getTransactionStatusInquiryImplPortAddress() {
        return TransactionStatusInquiryImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TransactionStatusInquiryImplPortWSDDServiceName = "TransactionStatusInquiryImplPort";

    public java.lang.String getTransactionStatusInquiryImplPortWSDDServiceName() {
        return TransactionStatusInquiryImplPortWSDDServiceName;
    }

    public void setTransactionStatusInquiryImplPortWSDDServiceName(java.lang.String name) {
        TransactionStatusInquiryImplPortWSDDServiceName = name;
    }

    public com.orange.b2w.statusinquiry.TransactionStatusInquiry_PortType getTransactionStatusInquiryImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TransactionStatusInquiryImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTransactionStatusInquiryImplPort(endpoint);
    }

    public com.orange.b2w.statusinquiry.TransactionStatusInquiry_PortType getTransactionStatusInquiryImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.orange.b2w.statusinquiry.TransactionStatusInquiryImplServiceSoapBindingStub _stub = new com.orange.b2w.statusinquiry.TransactionStatusInquiryImplServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getTransactionStatusInquiryImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTransactionStatusInquiryImplPortEndpointAddress(java.lang.String address) {
        TransactionStatusInquiryImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.orange.b2w.statusinquiry.TransactionStatusInquiry_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.orange.b2w.statusinquiry.TransactionStatusInquiryImplServiceSoapBindingStub _stub = new com.orange.b2w.statusinquiry.TransactionStatusInquiryImplServiceSoapBindingStub(new java.net.URL(TransactionStatusInquiryImplPort_address), this);
                _stub.setPortName(getTransactionStatusInquiryImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("TransactionStatusInquiryImplPort".equals(inputPortName)) {
            return getTransactionStatusInquiryImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "TransactionStatusInquiryImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "TransactionStatusInquiryImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TransactionStatusInquiryImplPort".equals(portName)) {
            setTransactionStatusInquiryImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
