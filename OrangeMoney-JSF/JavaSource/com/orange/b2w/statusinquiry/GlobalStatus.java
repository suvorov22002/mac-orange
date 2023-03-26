/**
 * GlobalStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.orange.b2w.statusinquiry;

import java.io.Serializable;
import java.util.HashMap;

public class GlobalStatus implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8700989903888355418L;
	
	private String _value_;
    private static HashMap _table_ = new HashMap();

    // Constructor
    protected GlobalStatus(java.lang.String value){
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _OK = "OK";
    public static final java.lang.String _ORANGE_RECONCILIATION = "ORANGE_RECONCILIATION";
    public static final java.lang.String _BANK_RECONCILIATION = "BANK_RECONCILIATION";
    public static final java.lang.String _ORANGE_BANK_RECONCILIATION = "ORANGE_BANK_RECONCILIATION";
    public static final java.lang.String _KO = "KO";
    public static final GlobalStatus OK = new GlobalStatus(_OK);
    public static final GlobalStatus ORANGE_RECONCILIATION = new GlobalStatus(_ORANGE_RECONCILIATION);
    public static final GlobalStatus BANK_RECONCILIATION = new GlobalStatus(_BANK_RECONCILIATION);
    public static final GlobalStatus ORANGE_BANK_RECONCILIATION = new GlobalStatus(_ORANGE_BANK_RECONCILIATION);
    public static final GlobalStatus KO = new GlobalStatus(_KO);
    public java.lang.String getValue() { return _value_;}
    public static GlobalStatus fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        GlobalStatus enumeration = (GlobalStatus)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static GlobalStatus fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(GlobalStatus.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "globalStatus"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
