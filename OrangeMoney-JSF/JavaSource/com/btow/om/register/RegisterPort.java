/**
 * RegisterPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.btow.om.register;

public interface RegisterPort extends java.rmi.Remote {

    /**
     * Demande inscription
     */
    public short ombRequest(java.lang.String msisdn, javax.xml.rpc.holders.StringHolder alias, short code_service, java.lang.String libelle, java.lang.String devise, java.lang.String key,java.lang.String active_date) throws java.rmi.RemoteException;

    /**
     * Cloture inscription
     */
    public java.lang.String ombClose(javax.xml.rpc.holders.StringHolder alias,java.lang.String close_date, java.lang.String orig, java.lang.String motif) throws java.rmi.RemoteException;
    public void KYCRequest(java.lang.String msisdn, java.lang.String key, javax.xml.rpc.holders.StringHolder status, javax.xml.rpc.holders.StringHolder lastname, javax.xml.rpc.holders.StringHolder firstname, javax.xml.rpc.holders.StringHolder dob, javax.xml.rpc.holders.StringHolder cin) throws java.rmi.RemoteException;
    
}
