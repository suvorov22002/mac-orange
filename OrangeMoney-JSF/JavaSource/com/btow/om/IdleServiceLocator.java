/**
 * IdleServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.btow.om;

import com.afb.dpd.orangemoney.jsf.tools.OMTools;

public class IdleServiceLocator extends org.apache.axis.client.Service implements com.btow.om.IdleService {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


/**
 * Service de dispo OM-Banking
 */

    public IdleServiceLocator() {
    }


    public IdleServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IdleServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IdlePort
    private java.lang.String IdlePort_address = OMTools.idlService;
    

    public java.lang.String getIdlePortAddress() {
        return IdlePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IdlePortWSDDServiceName = "IdlePort";

    public java.lang.String getIdlePortWSDDServiceName() {
        return IdlePortWSDDServiceName;
    }

    public void setIdlePortWSDDServiceName(java.lang.String name) {
        IdlePortWSDDServiceName = name;
    }

    public com.btow.om.IdlePort getIdlePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IdlePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIdlePort(endpoint);
    }

    public com.btow.om.IdlePort getIdlePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.btow.om.IdleBindingStub _stub = new com.btow.om.IdleBindingStub(portAddress, this);
            _stub.setPortName(getIdlePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIdlePortEndpointAddress(java.lang.String address) {
        IdlePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.btow.om.IdlePort.class.isAssignableFrom(serviceEndpointInterface)) {
                com.btow.om.IdleBindingStub _stub = new com.btow.om.IdleBindingStub(new java.net.URL(IdlePort_address), this);
                _stub.setPortName(getIdlePortWSDDServiceName());
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
        if ("IdlePort".equals(inputPortName)) {
            return getIdlePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://om.btow.com", "IdleService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://om.btow.com", "IdlePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("IdlePort".equals(portName)) {
            setIdlePortEndpointAddress(address);
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
