package com.cmcc.vrp.boss.hainan.uipsoap.client;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

import javax.xml.namespace.QName;
import java.io.Serializable;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:22:13
*/
public class BusiCallResponse
    implements Serializable {
    private static TypeDesc typeDesc = new TypeDesc(BusiCallResponse.class, true);

    static {
        typeDesc.setXmlType(new QName("http://www.linkage.com/UIP/", ">busiCallResponse"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("responseMessage");
        elemField.setXmlName(new QName("", "responseMessage"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    private String responseMessage;
    private Object __equalsCalc = null;
    private boolean __hashCodeCalc = false;

    public BusiCallResponse() {
    }

    public BusiCallResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public static TypeDesc getTypeDesc() {
        return typeDesc;
    }

    public static Serializer getSerializer(String mechType, Class _javaType, QName _xmlType) {
        return
            new BeanSerializer(
                _javaType, _xmlType, typeDesc);
    }

    public static Deserializer getDeserializer(String mechType, Class _javaType, QName _xmlType) {
        return
            new BeanDeserializer(
                _javaType, _xmlType, typeDesc);
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    /**
     *  (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof BusiCallResponse)) {
            return false;
        }
        BusiCallResponse other = (BusiCallResponse) obj;
        //if (obj == null) return false;
        if (this == obj) {
            return true;
        }
        if (this.__equalsCalc != null) {
            return this.__equalsCalc == obj;
        }
        this.__equalsCalc = obj;

        boolean _equals =
            ((this.responseMessage == null) && (other.getResponseMessage() == null)) || (
                (this.responseMessage != null) &&
                    (this.responseMessage.equals(other.getResponseMessage())));
        this.__equalsCalc = null;
        return _equals;
    }

    /**
     *  (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public synchronized int hashCode() {
        if (this.__hashCodeCalc) {
            return 0;
        }
        this.__hashCodeCalc = true;
        int _hashCode = 1;
        if (getResponseMessage() != null) {
            _hashCode += getResponseMessage().hashCode();
        }
        this.__hashCodeCalc = false;
        return _hashCode;
    }
}