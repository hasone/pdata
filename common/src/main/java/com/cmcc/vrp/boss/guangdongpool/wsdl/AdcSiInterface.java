
package com.cmcc.vrp.boss.guangdongpool.wsdl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="request" type="{http://adc.siinterface.com/}AdcSiRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "request"
    })
@XmlRootElement(name = "AdcSiInterface")
public class AdcSiInterface {

    @XmlElementRef(name = "request", namespace = "http://adc.siinterface/", type = JAXBElement.class, required = false)
    protected JAXBElement<AdcSiRequest> request;

    /**
     * 获取request属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AdcSiRequest }{@code >}
     *     
     */
    public JAXBElement<AdcSiRequest> getRequest() {
        return request;
    }

    /**
     * 设置request属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AdcSiRequest }{@code >}
     *     
     */
    public void setRequest(JAXBElement<AdcSiRequest> value) {
        this.request = value;
    }

}
