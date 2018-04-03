/**
 * NGEC.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cmcc.vrp.boss.guangdong.webservice;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午9:58:57
*/
public class NGEC implements java.io.Serializable {
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NGEC.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://adc.ecinterface/", "NGEC"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("origDomain");
        elemField.setXmlName(new javax.xml.namespace.QName("http://adc.ecinterface/", "OrigDomain"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BIPCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://adc.ecinterface/", "BIPCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BIPVer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://adc.ecinterface/", "BIPVer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transIDO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://adc.ecinterface/", "TransIDO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("areacode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://adc.ecinterface/", "Areacode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ECCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://adc.ecinterface/", "ECCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ECUserName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://adc.ecinterface/", "ECUserName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ECUserPwd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://adc.ecinterface/", "ECUserPwd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://adc.ecinterface/", "ProcessTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("response");
        elemField.setXmlName(new javax.xml.namespace.QName("http://adc.ecinterface/", "Response"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://adc.ecinterface/", "Response"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("svcCont");
        elemField.setXmlName(new javax.xml.namespace.QName("http://adc.ecinterface/", "SvcCont"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    private String origDomain;
    private String BIPCode;
    private String BIPVer;
    private String transIDO;
    private String areacode;
    private String ECCode;
    private String ECUserName;
    private String ECUserPwd;
    private String processTime;
    private com.cmcc.vrp.boss.guangdong.webservice.Response response;
    private String svcCont;
    private Object __equalsCalc = null;
    private boolean __hashCodeCalc = false;


    public NGEC() {
    }


    public NGEC(
        String origDomain,
        String BIPCode,
        String BIPVer,
        String transIDO,
        String areacode,
        String ECCode,
        String ECUserName,
        String ECUserPwd,
        String processTime,
        com.cmcc.vrp.boss.guangdong.webservice.Response response,
        String svcCont) {
        this.origDomain = origDomain;
        this.BIPCode = BIPCode;
        this.BIPVer = BIPVer;
        this.transIDO = transIDO;
        this.areacode = areacode;
        this.ECCode = ECCode;
        this.ECUserName = ECUserName;
        this.ECUserPwd = ECUserPwd;
        this.processTime = processTime;
        this.response = response;
        this.svcCont = svcCont;
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
        String mechType,
        Class _javaType,
        javax.xml.namespace.QName _xmlType) {
        return
            new org.apache.axis.encoding.ser.BeanSerializer(
                _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
        String mechType,
        Class _javaType,
        javax.xml.namespace.QName _xmlType) {
        return
            new org.apache.axis.encoding.ser.BeanDeserializer(
                _javaType, _xmlType, typeDesc);
    }

    /**
     * Gets the origDomain value for this NGEC.
     *
     * @return origDomain
     */
    public String getOrigDomain() {
        return origDomain;
    }

    /**
     * Sets the origDomain value for this NGEC.
     *
     * @param origDomain
     */
    public void setOrigDomain(String origDomain) {
        this.origDomain = origDomain;
    }

    /**
     * Gets the BIPCode value for this NGEC.
     *
     * @return BIPCode
     */
    public String getBIPCode() {
        return BIPCode;
    }

    /**
     * Sets the BIPCode value for this NGEC.
     *
     * @param BIPCode
     */
    public void setBIPCode(String BIPCode) {
        this.BIPCode = BIPCode;
    }

    /**
     * Gets the BIPVer value for this NGEC.
     *
     * @return BIPVer
     */
    public String getBIPVer() {
        return BIPVer;
    }

    /**
     * Sets the BIPVer value for this NGEC.
     *
     * @param BIPVer
     */
    public void setBIPVer(String BIPVer) {
        this.BIPVer = BIPVer;
    }

    /**
     * Gets the transIDO value for this NGEC.
     *
     * @return transIDO
     */
    public String getTransIDO() {
        return transIDO;
    }

    /**
     * Sets the transIDO value for this NGEC.
     *
     * @param transIDO
     */
    public void setTransIDO(String transIDO) {
        this.transIDO = transIDO;
    }

    /**
     * Gets the areacode value for this NGEC.
     *
     * @return areacode
     */
    public String getAreacode() {
        return areacode;
    }

    /**
     * Sets the areacode value for this NGEC.
     *
     * @param areacode
     */
    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    /**
     * Gets the ECCode value for this NGEC.
     *
     * @return ECCode
     */
    public String getECCode() {
        return ECCode;
    }

    /**
     * Sets the ECCode value for this NGEC.
     *
     * @param ECCode
     */
    public void setECCode(String ECCode) {
        this.ECCode = ECCode;
    }

    /**
     * Gets the ECUserName value for this NGEC.
     *
     * @return ECUserName
     */
    public String getECUserName() {
        return ECUserName;
    }

    /**
     * Sets the ECUserName value for this NGEC.
     *
     * @param ECUserName
     */
    public void setECUserName(String ECUserName) {
        this.ECUserName = ECUserName;
    }

    /**
     * Gets the ECUserPwd value for this NGEC.
     *
     * @return ECUserPwd
     */
    public String getECUserPwd() {
        return ECUserPwd;
    }

    /**
     * Sets the ECUserPwd value for this NGEC.
     *
     * @param ECUserPwd
     */
    public void setECUserPwd(String ECUserPwd) {
        this.ECUserPwd = ECUserPwd;
    }

    /**
     * Gets the processTime value for this NGEC.
     *
     * @return processTime
     */
    public String getProcessTime() {
        return processTime;
    }

    /**
     * Sets the processTime value for this NGEC.
     *
     * @param processTime
     */
    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    /**
     * Gets the response value for this NGEC.
     *
     * @return response
     */
    public com.cmcc.vrp.boss.guangdong.webservice.Response getResponse() {
        return response;
    }

    /**
     * Sets the response value for this NGEC.
     *
     * @param response
     */
    public void setResponse(com.cmcc.vrp.boss.guangdong.webservice.Response response) {
        this.response = response;
    }

    /**
     * Gets the svcCont value for this NGEC.
     *
     * @return svcCont
     */
    public String getSvcCont() {
        return svcCont;
    }

    /**
     * Sets the svcCont value for this NGEC.
     *
     * @param svcCont
     */
    public void setSvcCont(String svcCont) {
        this.svcCont = svcCont;
    }

    /**
     *  (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof NGEC)) {
            return false;
        }
        NGEC other = (NGEC) obj;
        //if (obj == null) return false;
        if (this == obj) {
            return true;
        }
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = ((this.origDomain == null && other.getOrigDomain() == null) ||
                (this.origDomain != null &&
                    this.origDomain.equals(other.getOrigDomain()))) &&
            ((this.BIPCode == null && other.getBIPCode() == null) ||
                (this.BIPCode != null &&
                    this.BIPCode.equals(other.getBIPCode()))) &&
            ((this.BIPVer == null && other.getBIPVer() == null) ||
                (this.BIPVer != null &&
                    this.BIPVer.equals(other.getBIPVer()))) &&
            ((this.transIDO == null && other.getTransIDO() == null) ||
                (this.transIDO != null &&
                    this.transIDO.equals(other.getTransIDO()))) &&
            ((this.areacode == null && other.getAreacode() == null) ||
                (this.areacode != null &&
                    this.areacode.equals(other.getAreacode()))) &&
            ((this.ECCode == null && other.getECCode() == null) ||
                (this.ECCode != null &&
                    this.ECCode.equals(other.getECCode()))) &&
            ((this.ECUserName == null && other.getECUserName() == null) ||
                (this.ECUserName != null &&
                    this.ECUserName.equals(other.getECUserName()))) &&
            ((this.ECUserPwd == null && other.getECUserPwd() == null) ||
                (this.ECUserPwd != null &&
                    this.ECUserPwd.equals(other.getECUserPwd()))) &&
            ((this.processTime == null && other.getProcessTime() == null) ||
                (this.processTime != null &&
                    this.processTime.equals(other.getProcessTime()))) &&
            ((this.response == null && other.getResponse() == null) ||
                (this.response != null &&
                    this.response.equals(other.getResponse()))) &&
            ((this.svcCont == null && other.getSvcCont() == null) ||
                (this.svcCont != null &&
                    this.svcCont.equals(other.getSvcCont())));
        __equalsCalc = null;
        return _equals;
    }

    /**
     *  (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getOrigDomain() != null) {
            _hashCode += getOrigDomain().hashCode();
        }
        if (getBIPCode() != null) {
            _hashCode += getBIPCode().hashCode();
        }
        if (getBIPVer() != null) {
            _hashCode += getBIPVer().hashCode();
        }
        if (getTransIDO() != null) {
            _hashCode += getTransIDO().hashCode();
        }
        if (getAreacode() != null) {
            _hashCode += getAreacode().hashCode();
        }
        if (getECCode() != null) {
            _hashCode += getECCode().hashCode();
        }
        if (getECUserName() != null) {
            _hashCode += getECUserName().hashCode();
        }
        if (getECUserPwd() != null) {
            _hashCode += getECUserPwd().hashCode();
        }
        if (getProcessTime() != null) {
            _hashCode += getProcessTime().hashCode();
        }
        if (getResponse() != null) {
            _hashCode += getResponse().hashCode();
        }
        if (getSvcCont() != null) {
            _hashCode += getSvcCont().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
