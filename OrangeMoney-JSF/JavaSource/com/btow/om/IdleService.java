/**
 * IdleService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.btow.om;

public interface IdleService extends javax.xml.rpc.Service {

/**
 * Service de dispo OM-Banking
 */
    public java.lang.String getIdlePortAddress();

    public com.btow.om.IdlePort getIdlePort() throws javax.xml.rpc.ServiceException;

    public com.btow.om.IdlePort getIdlePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
