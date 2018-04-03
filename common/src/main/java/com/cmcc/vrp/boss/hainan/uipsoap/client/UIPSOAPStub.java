package com.cmcc.vrp.boss.hainan.uipsoap.client;

import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.DeserializerFactory;
import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.encoding.ser.EnumDeserializerFactory;
import org.apache.axis.encoding.ser.EnumSerializerFactory;
import org.apache.axis.encoding.ser.SimpleDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListSerializerFactory;
import org.apache.axis.encoding.ser.SimpleSerializerFactory;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

import javax.xml.namespace.QName;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Vector;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:25:31
*/
public class UIPSOAPStub extends Stub
    implements UIP_PortType {
    static OperationDesc[] _operations = new OperationDesc[2];

    static {
        _initOperationDesc1();
    }

    private Vector cachedSerClasses = new Vector();
    private Vector cachedSerQNames = new Vector();
    private Vector cachedSerFactories = new Vector();
    private Vector cachedDeserFactories = new Vector();

    public UIPSOAPStub() throws AxisFault {
        this(null);
    }

    public UIPSOAPStub(URL endpointURL, javax.xml.rpc.Service service) throws AxisFault {
        this(service);
        this.cachedEndpoint = endpointURL;
    }

    public UIPSOAPStub(javax.xml.rpc.Service service) throws AxisFault {
        if (service == null) {
            this.service = new org.apache.axis.client.Service();
        } else {
            this.service = service;
        }
        ((org.apache.axis.client.Service) this.service).setTypeMappingVersion("1.2");

        Class beansf = BeanSerializerFactory.class;
        Class beandf = BeanDeserializerFactory.class;
        Class enumsf = EnumSerializerFactory.class;
        Class enumdf = EnumDeserializerFactory.class;
        Class arraysf = ArraySerializerFactory.class;
        Class arraydf = ArrayDeserializerFactory.class;
        Class simplesf = SimpleSerializerFactory.class;
        Class simpledf = SimpleDeserializerFactory.class;
        Class simplelistsf = SimpleListSerializerFactory.class;
        Class simplelistdf = SimpleListDeserializerFactory.class;
        QName qName = new QName("http://www.linkage.com/UIP/", ">busiCallRequest");
        this.cachedSerQNames.add(qName);
        Class cls = BusiCallRequest.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);

        qName = new QName("http://www.linkage.com/UIP/", ">busiCallResponse");
        this.cachedSerQNames.add(qName);
        cls = BusiCallResponse.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);

        qName = new QName("http://www.linkage.com/UIP/", ">businessCallRequest");
        this.cachedSerQNames.add(qName);
        cls = BusinessCallRequest.class;
        this.cachedSerClasses.add(cls);
        this.cachedSerFactories.add(beansf);
        this.cachedDeserFactories.add(beandf);
    }

    private static void _initOperationDesc1() {
        OperationDesc oper = new OperationDesc();
        oper.setName("busiCall");
        ParameterDesc param = new ParameterDesc(new QName("http://www.linkage.com/UIP/", "busiCallRequest"), org.apache.axis.description.ParameterDesc.IN, new QName("http://www.linkage.com/UIP/", ">busiCallRequest"), BusiCallRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.linkage.com/UIP/", ">busiCallResponse"));
        oper.setReturnClass(BusiCallResponse.class);
        oper.setReturnQName(new QName("http://www.linkage.com/UIP/", "busiCallResponse"));
        oper.setStyle(Style.DOCUMENT);
        oper.setUse(Use.LITERAL);
        _operations[0] = oper;

        oper = new OperationDesc();
        oper.setName("businessCall");
        param = new ParameterDesc(new QName("http://www.linkage.com/UIP/", "businessCallRequest"), org.apache.axis.description.ParameterDesc.IN, new QName("http://www.linkage.com/UIP/", ">businessCallRequest"), BusinessCallRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new QName("", "businessCallResponse"));
        oper.setStyle(Style.DOCUMENT);
        oper.setUse(Use.LITERAL);
        _operations[1] = oper;
    }

    protected Call createCall() throws RemoteException {
        try {
            Call _call = super._createCall();
            if (this.maintainSessionSet) {
                _call.setMaintainSession(this.maintainSession);
            }
            if (this.cachedUsername != null) {
                _call.setUsername(this.cachedUsername);
            }
            if (this.cachedPassword != null) {
                _call.setPassword(this.cachedPassword);
            }
            if (this.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(this.cachedEndpoint);
            }
            if (this.cachedTimeout != null) {
                _call.setTimeout(this.cachedTimeout);
            }
            if (this.cachedPortName != null) {
                _call.setPortName(this.cachedPortName);
            }
            Enumeration keys = this.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                _call.setProperty(key, this.cachedProperties.get(key));
            }

            synchronized (this) {
                if (firstCall()) {
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < this.cachedSerFactories.size(); i++) {
                        Class cls = (Class) this.cachedSerClasses.get(i);
                        QName qName =
                            (QName) this.cachedSerQNames.get(i);
                        Object x = this.cachedSerFactories.get(i);
                        if ((x instanceof Class)) {
                            Class sf =
                                (Class) this.cachedSerFactories.get(i);
                            Class df =
                                (Class) this.cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        } else if ((x instanceof javax.xml.rpc.encoding.SerializerFactory)) {
                            org.apache.axis.encoding.SerializerFactory sf =
                                (org.apache.axis.encoding.SerializerFactory) this.cachedSerFactories.get(i);
                            DeserializerFactory df =
                                (DeserializerFactory) this.cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        } catch (Throwable _t) {

            throw new AxisFault("Failure trying to get the Call object", _t);
        }
    }

    /**
     *  (non-Javadoc)
     * @see com.cmcc.vrp.boss.hainan.uipsoap.client.UIP_PortType#busiCall(com.cmcc.vrp.boss.hainan.uipsoap.client.BusiCallRequest)
     */
    public BusiCallResponse busiCall(BusiCallRequest busiCallRequest) throws RemoteException {
        if (this.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setEncodingStyle(null);
        _call.setProperty("sendXsiTypes", Boolean.FALSE);
        _call.setProperty("sendMultiRefs", Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("", "busiCall"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{busiCallRequest});

            if ((_resp instanceof RemoteException)) {
                throw ((RemoteException) _resp);
            }

            extractAttachments(_call);
            try {
                return (BusiCallResponse) _resp;
            } catch (Exception _exception) {
                return (BusiCallResponse) JavaUtils.convert(_resp, BusiCallResponse.class);
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    /**
     *  (non-Javadoc)
     * @see com.cmcc.vrp.boss.hainan.uipsoap.client.UIP_PortType#businessCall(com.cmcc.vrp.boss.hainan.uipsoap.client.BusinessCallRequest)
     */
    public String businessCall(BusinessCallRequest businessCallRequest) throws RemoteException {
        if (this.cachedEndpoint == null) {
            throw new NoEndPointException();
        }
        Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setEncodingStyle(null);
        _call.setProperty("sendXsiTypes", Boolean.FALSE);
        _call.setProperty("sendMultiRefs", Boolean.FALSE);
        _call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new QName("", "businessCall"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{businessCallRequest});

            if ((_resp instanceof RemoteException)) {
                throw ((RemoteException) _resp);
            }

            extractAttachments(_call);
            try {
                return (String) _resp;
            } catch (Exception _exception) {
                return (String) JavaUtils.convert(_resp, String.class);
            }
        } catch (AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }
}