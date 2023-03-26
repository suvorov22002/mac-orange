/**
 * TransactionStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.orange.b2w.statusinquiry;

import java.util.Arrays;


/**
 * TransactionStatus
 * @author Francis K
 * @version 1.0
 */
public class TransactionStatus  implements java.io.Serializable {
	
    /**
	 *  serialVersionUID
	 */
	private static final long serialVersionUID = -4661721003084032696L;

	private java.lang.String returnCode;

    private com.orange.b2w.statusinquiry.Transaction transaction;

    private com.orange.b2w.statusinquiry.GlobalStatus status;

    private com.orange.b2w.statusinquiry.DetailedStatus[] detailedStatuses;

    public TransactionStatus() {
    }

    public TransactionStatus(
           java.lang.String returnCode,
           com.orange.b2w.statusinquiry.Transaction transaction,
           com.orange.b2w.statusinquiry.GlobalStatus status,
           com.orange.b2w.statusinquiry.DetailedStatus[] detailedStatuses) {
           this.returnCode = returnCode;
           this.transaction = transaction;
           this.status = status;
           this.detailedStatuses = detailedStatuses;
    }


    /**
     * Gets the returnCode value for this TransactionStatus.
     * 
     * @return returnCode
     */
    public java.lang.String getReturnCode() {
        return returnCode;
    }


    /**
     * Sets the returnCode value for this TransactionStatus.
     * 
     * @param returnCode
     */
    public void setReturnCode(java.lang.String returnCode) {
        this.returnCode = returnCode;
    }


    /**
     * Gets the transaction value for this TransactionStatus.
     * 
     * @return transaction
     */
    public com.orange.b2w.statusinquiry.Transaction getTransaction() {
        return transaction;
    }


    /**
     * Sets the transaction value for this TransactionStatus.
     * 
     * @param transaction
     */
    public void setTransaction(com.orange.b2w.statusinquiry.Transaction transaction) {
        this.transaction = transaction;
    }


    /**
     * Gets the status value for this TransactionStatus.
     * 
     * @return status
     */
    public com.orange.b2w.statusinquiry.GlobalStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this TransactionStatus.
     * 
     * @param status
     */
    public void setStatus(com.orange.b2w.statusinquiry.GlobalStatus status) {
        this.status = status;
    }


    /**
     * Gets the detailedStatuses value for this TransactionStatus.
     * 
     * @return detailedStatuses
     */
    public com.orange.b2w.statusinquiry.DetailedStatus[] getDetailedStatuses() {
        return detailedStatuses;
    }


    /**
     * Sets the detailedStatuses value for this TransactionStatus.
     * 
     * @param detailedStatuses
     */
    public void setDetailedStatuses(com.orange.b2w.statusinquiry.DetailedStatus[] detailedStatuses) {
        this.detailedStatuses = detailedStatuses;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransactionStatus)) return false;
        TransactionStatus other = (TransactionStatus) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.returnCode==null && other.getReturnCode()==null) || 
             (this.returnCode!=null &&
              this.returnCode.equals(other.getReturnCode()))) &&
            ((this.transaction==null && other.getTransaction()==null) || 
             (this.transaction!=null &&
              this.transaction.equals(other.getTransaction()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.detailedStatuses==null && other.getDetailedStatuses()==null) || 
             (this.detailedStatuses!=null &&
              java.util.Arrays.equals(this.detailedStatuses, other.getDetailedStatuses())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getReturnCode() != null) {
            _hashCode += getReturnCode().hashCode();
        }
        if (getTransaction() != null) {
            _hashCode += getTransaction().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getDetailedStatuses() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDetailedStatuses());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDetailedStatuses(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransactionStatus.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "transactionStatus"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "returnCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transaction");
        elemField.setXmlName(new javax.xml.namespace.QName("", "transaction"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "transaction"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "globalStatus"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detailedStatuses");
        elemField.setXmlName(new javax.xml.namespace.QName("", "detailedStatuses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "detailedStatus"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "detailedStatus"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

    /*
	@Override
	public String toString() {
		return "TransactionStatus [__equalsCalc=" + __equalsCalc
				+ ", __hashCodeCalc=" + __hashCodeCalc + ", detailedStatuses="
				+ Arrays.toString(detailedStatuses) + ", returnCode="
				+ returnCode + ", status=" + status + ", transaction="
				+ transaction + "]";
	}
	*/
	
	@Override
	public String toString() {
		return "TransactionStatus [returnCode=" + returnCode + ", transaction=" + transaction + ", status=" + status
				+ ", detailedStatuses=" + Arrays.toString(detailedStatuses) + "]";
	}
    
   
}
