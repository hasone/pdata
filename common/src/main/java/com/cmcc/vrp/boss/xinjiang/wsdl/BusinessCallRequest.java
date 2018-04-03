package com.cmcc.vrp.boss.xinjiang.wsdl;

import javax.xml.namespace.QName;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

/**
 * BusinessCallRequest.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006
 * (06:55:48 PDT) WSDL2Java emitter.
 */

public class BusinessCallRequest implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String requestMessage;

    public BusinessCallRequest() {
    }

    public BusinessCallRequest(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    /**
     * Gets the requestMessage value for this BusinessCallRequest.
     * 
     * @return requestMessage
     */
    public String getRequestMessage() {
        return requestMessage;
    }

    /**
     * Sets the requestMessage value for this BusinessCallRequest.
     * 
     * @param requestMessage
     */
    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    private Object equalsCalc = null;

    /**
     * equals
     */
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof BusinessCallRequest)){
            return false;
        }
        BusinessCallRequest other = (BusinessCallRequest) obj;
        if (this == obj){
            return true;
        }
        
        if (equalsCalc != null) {
            return (equalsCalc == obj);
        }
        equalsCalc = obj;
        boolean equals;
        equals = (this.requestMessage == null && other
                .getRequestMessage() == null) || (this.requestMessage != null && this.requestMessage
                .equals(other.getRequestMessage()));
        equalsCalc = null;
        return equals;
    }

    private boolean hashCodeCalc = false;

    /**
     * hashcode
     */
    public synchronized int hashCode() {
        if (hashCodeCalc) {
            return 0;
        }
        hashCodeCalc = true;
        int hashCode = 1;
        if (getRequestMessage() != null) {
            hashCode += getRequestMessage().hashCode();
        }
        hashCodeCalc = false;
        return hashCode;
    }

    // Type metadata
    private static TypeDesc typeDesc = new TypeDesc(BusinessCallRequest.class,
            true);

    static {
        typeDesc.setXmlType(new QName("http://www.linkage.com/UIP/",
                "businessCallRequest"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("requestMessage");
        elemField.setXmlName(new QName("", "requestMessage"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema",
                "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static Serializer getSerializer(String mechType, Class javaType,
            QName xmlType) {
        return new BeanSerializer(javaType, xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static Deserializer getDeserializer(String mechType,
            Class javaType, QName xmlType) {
        return new BeanDeserializer(javaType, xmlType, typeDesc);
    }

}
