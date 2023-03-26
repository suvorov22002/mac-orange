/**
 * Step.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.orange.b2w.statusinquiry;

public class Step implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected Step(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _ORANGE_CREDIT = "ORANGE_CREDIT";
    public static final java.lang.String _ORANGE_DEBIT = "ORANGE_DEBIT";
    public static final java.lang.String _ORANGE_AUTHORIZATION = "ORANGE_AUTHORIZATION";
    public static final java.lang.String _BANK_CREDIT = "BANK_CREDIT";
    public static final java.lang.String _BANK_DEBIT = "BANK_DEBIT";
    public static final java.lang.String _BANK_CANCEL = "BANK_CANCEL";
    public static final java.lang.String _BANK_STATUS_INQUIRY = "BANK_STATUS_INQUIRY";
    public static final Step ORANGE_CREDIT = new Step(_ORANGE_CREDIT);
    public static final Step ORANGE_DEBIT = new Step(_ORANGE_DEBIT);
    public static final Step ORANGE_AUTHORIZATION = new Step(_ORANGE_AUTHORIZATION);
    public static final Step BANK_CREDIT = new Step(_BANK_CREDIT);
    public static final Step BANK_DEBIT = new Step(_BANK_DEBIT);
    public static final Step BANK_CANCEL = new Step(_BANK_CANCEL);
    public static final Step BANK_STATUS_INQUIRY = new Step(_BANK_STATUS_INQUIRY);
    public java.lang.String getValue() { return _value_;}
    public static Step fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        Step enumeration = (Step)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static Step fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Step.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "step"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
