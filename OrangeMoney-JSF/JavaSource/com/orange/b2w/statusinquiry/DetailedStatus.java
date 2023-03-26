/**
 * DetailedStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.orange.b2w.statusinquiry;

import java.io.Serializable;
import java.util.Calendar;

public class DetailedStatus  implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8402776439574633264L;

	private com.orange.b2w.statusinquiry.Step step;

    private StepStatus status;

    private Calendar requestDate;

    public DetailedStatus() {
    }

    public DetailedStatus(
           com.orange.b2w.statusinquiry.Step step,
           com.orange.b2w.statusinquiry.StepStatus status,
           java.util.Calendar requestDate) {
           this.step = step;
           this.status = status;
           this.requestDate = requestDate;
    }


    /**
     * Gets the step value for this DetailedStatus.
     * 
     * @return step
     */
    public com.orange.b2w.statusinquiry.Step getStep() {
        return step;
    }


    /**
     * Sets the step value for this DetailedStatus.
     * 
     * @param step
     */
    public void setStep(com.orange.b2w.statusinquiry.Step step) {
        this.step = step;
    }


    /**
     * Gets the status value for this DetailedStatus.
     * 
     * @return status
     */
    public com.orange.b2w.statusinquiry.StepStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this DetailedStatus.
     * 
     * @param status
     */
    public void setStatus(com.orange.b2w.statusinquiry.StepStatus status) {
        this.status = status;
    }


    /**
     * Gets the requestDate value for this DetailedStatus.
     * 
     * @return requestDate
     */
    public java.util.Calendar getRequestDate() {
        return requestDate;
    }


    /**
     * Sets the requestDate value for this DetailedStatus.
     * 
     * @param requestDate
     */
    public void setRequestDate(java.util.Calendar requestDate) {
        this.requestDate = requestDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DetailedStatus)) return false;
        DetailedStatus other = (DetailedStatus) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.step==null && other.getStep()==null) || 
             (this.step!=null &&
              this.step.equals(other.getStep()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.requestDate==null && other.getRequestDate()==null) || 
             (this.requestDate!=null &&
              this.requestDate.equals(other.getRequestDate())));
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
        if (getStep() != null) {
            _hashCode += getStep().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getRequestDate() != null) {
            _hashCode += getRequestDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DetailedStatus.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "detailedStatus"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("step");
        elemField.setXmlName(new javax.xml.namespace.QName("", "step"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "step"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://statusinquiry.b2w.orange.com/", "stepStatus"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "requestDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
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
		return "DetailedStatus [step=" + step + ", status=" + status + ", requestDate=" + requestDate + "]";
	}

}
