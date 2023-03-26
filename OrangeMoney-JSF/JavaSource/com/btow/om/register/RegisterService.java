/**
 * RegisterService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.btow.om.register;

public interface RegisterService extends javax.xml.rpc.Service {

/**
 * Service d'inscription OM-Banking
 */
    public java.lang.String getRegisterPortAddress();

    public com.btow.om.register.RegisterPort getRegisterPort() throws javax.xml.rpc.ServiceException;

    public com.btow.om.register.RegisterPort getRegisterPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
