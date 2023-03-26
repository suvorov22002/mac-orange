/**
 * TransactionStatusInquiry_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.orange.b2w.statusinquiry;

public interface TransactionStatusInquiry_PortType extends java.rmi.Remote {
    public com.orange.b2w.statusinquiry.TransactionStatus transactionStatusInquiry(com.orange.b2w.statusinquiry.TypeID typeID, java.lang.String ID) throws java.rmi.RemoteException;
    public com.orange.b2w.statusinquiry.LastTransactions lastTransactions(java.lang.String alias) throws java.rmi.RemoteException;
}
