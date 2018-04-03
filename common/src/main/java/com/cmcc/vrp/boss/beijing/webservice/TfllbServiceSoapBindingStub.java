/**
 * TfllbServiceSoapBindingStub.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cmcc.vrp.boss.beijing.webservice;

/**
 * ws
 */
public class TfllbServiceSoapBindingStub extends org.apache.axis.client.Stub implements TfllbService_PortType {
    static org.apache.axis.description.OperationDesc[] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[5];
        _initOperationDesc1();
    }

    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    public TfllbServiceSoapBindingStub() throws org.apache.axis.AxisFault {
        this(null);
    }

    public TfllbServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        this(service);
        super.cachedEndpoint = endpointURL;
    }

    public TfllbServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
    }

    private static void _initOperationDesc1() {
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("tf_tfzycx");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "xmlParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "tf_tfzycxReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("tf_tjzd");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "xmlParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "tf_tjzdReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("tf_ddlbcx");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "xmlParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "tf_ddlbcxReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("tf_ddmxcx");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "xmlParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "tf_ddmxcxReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("tf_tfzyhz");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "xmlParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "tf_tfzyhzReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            return _call;
        } catch (Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    /**
     *
     * @param xmlParams
     * @return
     * @throws java.rmi.RemoteException
     */
    public String tf_tfzycx(String xmlParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "tf_tfzycx"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{xmlParams});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    /**
     *
     * @param xmlParams
     * @return
     * @throws java.rmi.RemoteException
     */
    public String tf_tjzd(String xmlParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "tf_tjzd"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{xmlParams});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    /**
     *
     * @param xmlParams
     * @return
     * @throws java.rmi.RemoteException
     */
    public String tf_ddlbcx(String xmlParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "tf_ddlbcx"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{xmlParams});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    /**
     * 
     * @param xmlParams
     * @return
     * @throws java.rmi.RemoteException
     */
    public String tf_ddmxcx(String xmlParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "tf_ddmxcx"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{xmlParams});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

    /**
     *  (non-Javadoc)
     * @see com.cmcc.vrp.boss.beijing.webservice.TfllbService_PortType#tf_tfzyhz(java.lang.String)
     */
    public String tf_tfzyhz(String xmlParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "tf_tfzyhz"));

        setRequestHeaders(_call);
        setAttachments(_call);
        try {
            Object _resp = _call.invoke(new Object[]{xmlParams});

            if (_resp instanceof java.rmi.RemoteException) {
                throw (java.rmi.RemoteException) _resp;
            } else {
                extractAttachments(_call);
                try {
                    return (String) _resp;
                } catch (Exception _exception) {
                    return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
                }
            }
        } catch (org.apache.axis.AxisFault axisFaultException) {
            throw axisFaultException;
        }
    }

}
