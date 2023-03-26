/**
 * Transaction.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.orange.b2w.statusinquiry;

import java.text.SimpleDateFormat;

public class Transaction  implements java.io.Serializable {
	
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 9217693576421175542L;

	private java.lang.String orangeID;

    private java.lang.String bankID;

    private java.util.Calendar dateTime;

    private com.orange.b2w.statusinquiry.TransactionType type;

    private double amount;

    public Transaction() {
    }

    public Transaction(
           java.lang.String orangeID,
           java.lang.String bankID,
           java.util.Calendar dateTime,
           com.orange.b2w.statusinquiry.TransactionType type,
           double amount) {
           this.orangeID = orangeID;
           this.bankID = bankID;
           this.dateTime = dateTime;
           this.type = type;
           this.amount = amount;
    }


    /**
     * Gets the orangeID value for this Transaction.
     * 
     * @return orangeID
     */
    public java.lang.String getOrangeID() {
        return orangeID;
    }


    /**
     * Sets the orangeID value for this Transaction.
     * 
     * @param orangeID
     */
    public void setOrangeID(java.lang.String orangeID) {
        this.orangeID = orangeID;
    }


    /**
     * Gets the bankID value for this Transaction.
     * 
     * @return bankID
     */
    public java.lang.String getBankID() {
        return bankID;
    }


    /**
     * Sets the bankID value for this Transaction.
     * 
     * @param bankID
     */
    public void setBankID(java.lang.String bankID) {
        this.bankID = bankID;
    }


    /**
     * Gets the dateTime value for this Transaction.
     * 
     * @return dateTime
     */
    public java.util.Calendar getDateTime() {
        return dateTime;
    }


    /**
     * Sets the dateTime value for this Transaction.
     * 
     * @param dateTime
     */
    public void setDateTime(java.util.Calendar dateTime) {
        this.dateTime = dateTime;
    }


    /**
     * Gets the type value for this Transaction.
     * 
     * @return type
     */
    public com.orange.b2w.statusinquiry.TransactionType getType() {
        return type;
    }


    /**
     * Sets the type value for this Transaction.
     * 
     * @param type
     */
    public void setType(com.orange.b2w.statusinquiry.TransactionType type) {
        this.type = type;
    }


    /**
     * Gets the amount value for this Transaction.
     * 
     * @return amount
     */
    public double getAmount() {
        return amount;
    }


    /**
     * Sets the amount value for this Transaction.
     * 
     * @param amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Transaction)) return false;
        Transaction other = (Transaction) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.orangeID==null && other.getOrangeID()==null) || 
             (this.orangeID!=null &&
              this.orangeID.equals(other.getOrangeID()))) &&
            ((this.bankID==null && other.getBankID()==null) || 
             (this.bankID!=null &&
              this.bankID.equals(other.getBankID()))) &&
            ((this.dateTime==null && other.getDateTime()==null) || 
             (this.dateTime!=null &&
              this.dateTime.equals(other.getDateTime()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            this.amount == other.getAmount();
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
        if (getOrangeID() != null) {
            _hashCode += getOrangeID().hashCode();
        }
        if (getBankID() != null) {
            _hashCode += getBankID().hashCode();
        }
        if (getDateTime() != null) {
            _hashCode += getDateTime().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        _hashCode += new Double(getAmount()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Transaction.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "transaction"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orangeID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orangeID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bankID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "transactionType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "amount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
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


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Transaction [orangeID=" + orangeID + ", bankID=" + bankID + ", dateTime=" + dateTime + ", type=" + type
				+ ", amount=" + amount + ", __equalsCalc=" + __equalsCalc + ", __hashCodeCalc=" + __hashCodeCalc + "]";
	}
    
}
