
package com.cmcc.vrp.boss.guangdongpool.wsdl;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.cmcc.vrp.boss.guangdongpool.wsdl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _AdcSiResponse_QNAME = new QName("http://adc.siinterface.com/", "AdcSiResponse");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _AdcSiRequest_QNAME = new QName("http://adc.siinterface.com/", "AdcSiRequest");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _AdcSiResponseSIAppID_QNAME = new QName("http://adc.siinterface.com/", "SIAppID");
    private final static QName _AdcSiResponseResultMsg_QNAME = new QName("http://adc.siinterface.com/", "ResultMsg");
    private final static QName _AdcSiResponseTransID_QNAME = new QName("http://adc.siinterface.com/", "TransID");
    private final static QName _AdcSiResponseBizCode_QNAME = new QName("http://adc.siinterface.com/", "BizCode");
    private final static QName _AdcSiResponseTimeStamp_QNAME = new QName("http://adc.siinterface.com/", "TimeStamp");
    private final static QName _AdcSiResponseResultCode_QNAME = new QName("http://adc.siinterface.com/", "ResultCode");
    private final static QName _AdcSiResponseVersion_QNAME = new QName("http://adc.siinterface.com/", "Version");
    private final static QName _AdcSiResponseSvcCont_QNAME = new QName("http://adc.siinterface.com/", "SvcCont");
    private final static QName _AdcSiInterfaceRequest_QNAME = new QName("http://adc.siinterface/", "request");
    private final static QName _AdcSiRequestSign_QNAME = new QName("http://adc.siinterface.com/", "Sign");
    private final static QName _AdcSiInterfaceResponseAdcSiInterfaceResult_QNAME = new QName("http://adc.siinterface/", "AdcSiInterfaceResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.cmcc.vrp.boss.guangdongpool.wsdl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AdcSiInterfaceResponse }
     * 
     */
    public AdcSiInterfaceResponse createAdcSiInterfaceResponse() {
        return new AdcSiInterfaceResponse();
    }

    /**
     * Create an instance of {@link AdcSiResponse }
     * 
     */
    public AdcSiResponse createAdcSiResponse() {
        return new AdcSiResponse();
    }

    /**
     * Create an instance of {@link AdcSiInterface }
     * 
     */
    public AdcSiInterface createAdcSiInterface() {
        return new AdcSiInterface();
    }

    /**
     * Create an instance of {@link AdcSiRequest }
     * 
     */
    public AdcSiRequest createAdcSiRequest() {
        return new AdcSiRequest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdcSiResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "AdcSiResponse")
    public JAXBElement<AdcSiResponse> createAdcSiResponse(AdcSiResponse value) {
        return new JAXBElement<AdcSiResponse>(_AdcSiResponse_QNAME, AdcSiResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdcSiRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "AdcSiRequest")
    public JAXBElement<AdcSiRequest> createAdcSiRequest(AdcSiRequest value) {
        return new JAXBElement<AdcSiRequest>(_AdcSiRequest_QNAME, AdcSiRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "SIAppID", scope = AdcSiResponse.class)
    public JAXBElement<String> createAdcSiResponseSIAppID(String value) {
        return new JAXBElement<String>(_AdcSiResponseSIAppID_QNAME, String.class, AdcSiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "ResultMsg", scope = AdcSiResponse.class)
    public JAXBElement<String> createAdcSiResponseResultMsg(String value) {
        return new JAXBElement<String>(_AdcSiResponseResultMsg_QNAME, String.class, AdcSiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "TransID", scope = AdcSiResponse.class)
    public JAXBElement<String> createAdcSiResponseTransID(String value) {
        return new JAXBElement<String>(_AdcSiResponseTransID_QNAME, String.class, AdcSiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "BizCode", scope = AdcSiResponse.class)
    public JAXBElement<String> createAdcSiResponseBizCode(String value) {
        return new JAXBElement<String>(_AdcSiResponseBizCode_QNAME, String.class, AdcSiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "TimeStamp", scope = AdcSiResponse.class)
    public JAXBElement<String> createAdcSiResponseTimeStamp(String value) {
        return new JAXBElement<String>(_AdcSiResponseTimeStamp_QNAME, String.class, AdcSiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "ResultCode", scope = AdcSiResponse.class)
    public JAXBElement<String> createAdcSiResponseResultCode(String value) {
        return new JAXBElement<String>(_AdcSiResponseResultCode_QNAME, String.class, AdcSiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "Version", scope = AdcSiResponse.class)
    public JAXBElement<String> createAdcSiResponseVersion(String value) {
        return new JAXBElement<String>(_AdcSiResponseVersion_QNAME, String.class, AdcSiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "SvcCont", scope = AdcSiResponse.class)
    public JAXBElement<String> createAdcSiResponseSvcCont(String value) {
        return new JAXBElement<String>(_AdcSiResponseSvcCont_QNAME, String.class, AdcSiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdcSiRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface/", name = "request", scope = AdcSiInterface.class)
    public JAXBElement<AdcSiRequest> createAdcSiInterfaceRequest(AdcSiRequest value) {
        return new JAXBElement<AdcSiRequest>(_AdcSiInterfaceRequest_QNAME, AdcSiRequest.class, AdcSiInterface.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "SIAppID", scope = AdcSiRequest.class)
    public JAXBElement<String> createAdcSiRequestSIAppID(String value) {
        return new JAXBElement<String>(_AdcSiResponseSIAppID_QNAME, String.class, AdcSiRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "Sign", scope = AdcSiRequest.class)
    public JAXBElement<String> createAdcSiRequestSign(String value) {
        return new JAXBElement<String>(_AdcSiRequestSign_QNAME, String.class, AdcSiRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "TransID", scope = AdcSiRequest.class)
    public JAXBElement<String> createAdcSiRequestTransID(String value) {
        return new JAXBElement<String>(_AdcSiResponseTransID_QNAME, String.class, AdcSiRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "BizCode", scope = AdcSiRequest.class)
    public JAXBElement<String> createAdcSiRequestBizCode(String value) {
        return new JAXBElement<String>(_AdcSiResponseBizCode_QNAME, String.class, AdcSiRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "TimeStamp", scope = AdcSiRequest.class)
    public JAXBElement<String> createAdcSiRequestTimeStamp(String value) {
        return new JAXBElement<String>(_AdcSiResponseTimeStamp_QNAME, String.class, AdcSiRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "Version", scope = AdcSiRequest.class)
    public JAXBElement<String> createAdcSiRequestVersion(String value) {
        return new JAXBElement<String>(_AdcSiResponseVersion_QNAME, String.class, AdcSiRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface.com/", name = "SvcCont", scope = AdcSiRequest.class)
    public JAXBElement<String> createAdcSiRequestSvcCont(String value) {
        return new JAXBElement<String>(_AdcSiResponseSvcCont_QNAME, String.class, AdcSiRequest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdcSiResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://adc.siinterface/", name = "AdcSiInterfaceResult", scope = AdcSiInterfaceResponse.class)
    public JAXBElement<AdcSiResponse> createAdcSiInterfaceResponseAdcSiInterfaceResult(AdcSiResponse value) {
        return new JAXBElement<AdcSiResponse>(_AdcSiInterfaceResponseAdcSiInterfaceResult_QNAME, AdcSiResponse.class, AdcSiInterfaceResponse.class, value);
    }

}
