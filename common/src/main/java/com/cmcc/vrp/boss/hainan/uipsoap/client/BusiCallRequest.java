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
* @date 2017年1月22日 上午10:20:43
*/
public class BusiCallRequest
    implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1247856958449881023L;
    private static TypeDesc typeDesc = new TypeDesc(BusiCallRequest.class, true);

    static {
        typeDesc.setXmlType(new QName("http://www.linkage.com/UIP/", ">busiCallRequest"));
        ElementDesc elemField = new ElementDesc();
        elemField.setFieldName("requestMessage");
        elemField.setXmlName(new QName("", "requestMessage"));
        elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    private String requestMessage;
    private Object __equalsCalc = null;
    private boolean __hashCodeCalc = false;

    public BusiCallRequest() {
    }

    public BusiCallRequest(String requestMessage) {
        this.requestMessage = requestMessage;
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

    public String getRequestMessage() {
        return this.requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    /**
     *  (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof BusiCallRequest)) {
            return false;
        }
        BusiCallRequest other = (BusiCallRequest) obj;
        //if (obj == null) return false;
        if (this == obj) {
            return true;
        }
        if (this.__equalsCalc != null) {
            return this.__equalsCalc == obj;
        }
        this.__equalsCalc = obj;

        boolean _equals =
            ((this.requestMessage == null) && (other.getRequestMessage() == null)) || (
                (this.requestMessage != null) &&
                    (this.requestMessage.equals(other.getRequestMessage())));
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
        if (getRequestMessage() != null) {
            _hashCode += getRequestMessage().hashCode();
        }
        this.__hashCodeCalc = false;
        return _hashCode;
    }
}