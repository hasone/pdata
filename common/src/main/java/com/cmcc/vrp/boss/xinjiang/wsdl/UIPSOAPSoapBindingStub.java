/**
 * UIPSOAPSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cmcc.vrp.boss.xinjiang.wsdl;

import java.util.Vector;

import org.apache.axis.description.OperationDesc;

/**
 * 自动生成的代码
 *
 */
public class UIPSOAPSoapBindingStub extends org.apache.axis.client.Stub
        implements com.cmcc.vrp.boss.xinjiang.wsdl.UipPortType {
    private Vector cachedSerClasses = new java.util.Vector();
    private Vector cachedSerQNames = new java.util.Vector();
    private Vector cachedSerFactories = new java.util.Vector();
    private Vector cachedDeserFactories = new java.util.Vector();

    static OperationDesc[] operations;

    static {
        operations = new org.apache.axis.description.OperationDesc[1];
        initOperationDesc1();
    }

    private static void initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("businessCall");
        param = new org.apache.axis.description.ParameterDesc(
                new javax.xml.namespace.QName("http://www.linkage.com/UIP/",
                        "businessCallRequest"),
                org.apache.axis.description.ParameterDesc.IN,
                new javax.xml.namespace.QName("http://www.linkage.com/UIP/",
                        "businessCallRequest"),
                com.cmcc.vrp.boss.xinjiang.wsdl.BusinessCallRequest.class,
                false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName(
                "http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName(
                "http://www.linkage.com/UIP/", "businessCallResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        operations[0] = oper;

    }

    public UIPSOAPSoapBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public UIPSOAPSoapBindingStub(java.net.URL endpointURL,
            javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public UIPSOAPSoapBindingStub(javax.xml.rpc.Service service)
            throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service)
                .setTypeMappingVersion("1.2");
        java.lang.Class cls;
        javax.xml.namespace.QName qName;
        javax.xml.namespace.QName qName2;
        java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
        java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
        java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
        java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
        java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
        java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
        java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
        java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
        java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
        java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        qName = new javax.xml.namespace.QName("http://www.linkage.com/UIP/",
                "businessCallRequest");
        cachedSerQNames.add(qName);
        cls = com.cmcc.vrp.boss.xinjiang.wsdl.BusinessCallRequest.class;
        cachedSerClasses.add(cls);
        cachedSerFactories.add(beansf);
        cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall()
            throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call call = super._createCall();
            if (super.maintainSessionSet) {
                call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses
                                .get(i);
                        javax.xml.namespace.QName qName = (javax.xml.namespace.QName) cachedSerQNames
                                .get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            Class sf = (Class) cachedSerFactories.get(i);
                            Class df = (Class) cachedDeserFactories.get(i);
                            call.registerTypeMapping(cls, qName, sf, df, false);
                        } else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories
                                    .get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory) cachedDeserFactories
                                    .get(i);
                            call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return call;
        } catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault(
                    "Failure trying to get the Call object", t);
        }
    }

    /**
     * 自动生成的方法
     */
    public java.lang.String businessCall(
            com.cmcc.vrp.boss.xinjiang.wsdl.BusinessCallRequest businessCallRequest)
            throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call call = createCall();
        call.setOperation(operations[0]);
        call.setUseSOAPAction(true);
        call.setSOAPActionURI("");
        call.setEncodingStyle(null);
        call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR,
                Boolean.FALSE);
        call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
                Boolean.FALSE);
        call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        call.setOperationName(new javax.xml.namespace.QName("", "businessCall"));

        setRequestHeaders(call);
        setAttachments(call);
        try {
            java.lang.Object resp = call
                    .invoke(new java.lang.Object[] { businessCallRequest });

            if (resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) resp;
            } else {
                extractAttachments(call);
                try {
                    return (java.lang.String) resp;
                } catch (java.lang.Exception _exception) {
                    return (java.lang.String) org.apache.axis.utils.JavaUtils
                            .convert(resp, java.lang.String.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

}
