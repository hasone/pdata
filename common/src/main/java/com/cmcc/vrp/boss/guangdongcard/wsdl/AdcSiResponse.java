package com.cmcc.vrp.boss.guangdongcard.wsdl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>AdcSiResponse complex type的 Java 类。
 * <p>
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;complexType name="AdcSiResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BizCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ActionCode" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="TimeStamp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIAppID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TestFlag" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Dealkind" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Priority" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ResultCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResultMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SvcCont" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdcSiResponse", namespace = "http://adc.siinterface.com/", propOrder = {
    "bizCode",
    "transID",
    "actionCode",
    "timeStamp",
    "siAppID",
    "testFlag",
    "dealkind",
    "version",
    "priority",
    "resultCode",
    "resultMsg",
    "svcCont"
})
public class AdcSiResponse {

    @XmlElementRef(name = "BizCode", namespace = "http://adc.siinterface.com/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> bizCode;
    @XmlElementRef(name = "TransID", namespace = "http://adc.siinterface.com/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> transID;
    @XmlElement(name = "ActionCode")
    protected Integer actionCode;
    @XmlElementRef(name = "TimeStamp", namespace = "http://adc.siinterface.com/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> timeStamp;
    @XmlElementRef(name = "SIAppID", namespace = "http://adc.siinterface.com/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> siAppID;
    @XmlElement(name = "TestFlag")
    protected Integer testFlag;
    @XmlElement(name = "Dealkind")
    protected Integer dealkind;
    @XmlElementRef(name = "Version", namespace = "http://adc.siinterface.com/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> version;
    @XmlElement(name = "Priority")
    protected Integer priority;
    @XmlElementRef(name = "ResultCode", namespace = "http://adc.siinterface.com/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> resultCode;
    @XmlElementRef(name = "ResultMsg", namespace = "http://adc.siinterface.com/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> resultMsg;
    @XmlElementRef(name = "SvcCont", namespace = "http://adc.siinterface.com/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> svcCont;

    /**
     * 获取bizCode属性的值。
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getBizCode() {
        return bizCode;
    }

    /**
     * 设置bizCode属性的值。
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setBizCode(JAXBElement<String> value) {
        this.bizCode = value;
    }

    /**
     * 获取transID属性的值。
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getTransID() {
        return transID;
    }

    /**
     * 设置transID属性的值。
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setTransID(JAXBElement<String> value) {
        this.transID = value;
    }

    /**
     * 获取actionCode属性的值。
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getActionCode() {
        return actionCode;
    }

    /**
     * 设置actionCode属性的值。
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setActionCode(Integer value) {
        this.actionCode = value;
    }

    /**
     * 获取timeStamp属性的值。
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getTimeStamp() {
        return timeStamp;
    }

    /**
     * 设置timeStamp属性的值。
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setTimeStamp(JAXBElement<String> value) {
        this.timeStamp = value;
    }

    /**
     * 获取siAppID属性的值。
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getSIAppID() {
        return siAppID;
    }

    /**
     * 设置siAppID属性的值。
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setSIAppID(JAXBElement<String> value) {
        this.siAppID = value;
    }

    /**
     * 获取testFlag属性的值。
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getTestFlag() {
        return testFlag;
    }

    /**
     * 设置testFlag属性的值。
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setTestFlag(Integer value) {
        this.testFlag = value;
    }

    /**
     * 获取dealkind属性的值。
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getDealkind() {
        return dealkind;
    }

    /**
     * 设置dealkind属性的值。
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setDealkind(Integer value) {
        this.dealkind = value;
    }

    /**
     * 获取version属性的值。
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getVersion() {
        return version;
    }

    /**
     * 设置version属性的值。
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setVersion(JAXBElement<String> value) {
        this.version = value;
    }

    /**
     * 获取priority属性的值。
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 设置priority属性的值。
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setPriority(Integer value) {
        this.priority = value;
    }

    /**
     * 获取resultCode属性的值。
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getResultCode() {
        return resultCode;
    }

    /**
     * 设置resultCode属性的值。
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setResultCode(JAXBElement<String> value) {
        this.resultCode = value;
    }

    /**
     * 获取resultMsg属性的值。
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getResultMsg() {
        return resultMsg;
    }

    /**
     * 设置resultMsg属性的值。
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setResultMsg(JAXBElement<String> value) {
        this.resultMsg = value;
    }

    /**
     * 获取svcCont属性的值。
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public JAXBElement<String> getSvcCont() {
        return svcCont;
    }

    /**
     * 设置svcCont属性的值。
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    public void setSvcCont(JAXBElement<String> value) {
        this.svcCont = value;
    }

}
