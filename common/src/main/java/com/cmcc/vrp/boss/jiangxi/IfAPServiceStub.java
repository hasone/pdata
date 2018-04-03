/**
 * IfAPServiceStub.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.2  Built on : May 02, 2016 (05:55:18 BST)
 */
package com.cmcc.vrp.boss.jiangxi;

/**
 * IfAPServiceStub java implementation
 */
public class IfAPServiceStub extends org.apache.axis2.client.Stub {
    private static int counter = 0;
    protected org.apache.axis2.description.AxisOperation[] _operations;

    //hashmaps to keep the fault mapping
    private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
    private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
    private java.util.HashMap faultMessageMap = new java.util.HashMap();
    private javax.xml.namespace.QName[] opNameArray = null;

    /**
     *Constructor that takes in a configContext
     */
    public IfAPServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext, String targetEndpoint)
            throws org.apache.axis2.AxisFault {
        this(configurationContext, targetEndpoint, false);
    }

    /**
     * Constructor that takes in a configContext  and useseperate listner
     */
    public IfAPServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext, String targetEndpoint,
            boolean useSeparateListener) throws org.apache.axis2.AxisFault {
        //To populate AxisService
        populateAxisService();
        populateFaults();

        _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext, _service);

        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
    }

    /**
     * Default Constructor
     */
    public IfAPServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext)
            throws org.apache.axis2.AxisFault {
        this(configurationContext, "http://hongbao.jxict.cn/jiekou/services/JxydHongbaoVolume");
    }

    /**
     * Default Constructor
     */
    public IfAPServiceStub() throws org.apache.axis2.AxisFault {
        this("http://hongbao.jxict.cn/jiekou/services/JxydHongbaoVolume");
    }

    /**
     * Constructor taking the target endpoint
     */
    public IfAPServiceStub(String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(null, targetEndpoint);
    }

    private static synchronized String getUniqueSuffix() {
        // reset the counter if it is greater than 99999
        if (counter > 99999) {
            counter = 0;
        }

        counter = counter + 1;

        return Long.toString(System.currentTimeMillis()) + "_" + counter;
    }

    /**
     * @return the opNameArray
     */
    public javax.xml.namespace.QName[] getOpNameArray() {
        return opNameArray;
    }

    /**
     * @param opNameArray the opNameArray to set
     */
    public void setOpNameArray(javax.xml.namespace.QName[] opNameArray) {
        this.opNameArray = opNameArray;
    }

    private void populateAxisService() throws org.apache.axis2.AxisFault {
        //creating the Service with a unique name
        _service = new org.apache.axis2.description.AxisService("IfAPService" + getUniqueSuffix());
        addAnonymousOperations();

        //creating the operations
        org.apache.axis2.description.AxisOperation __operation;

        _operations = new org.apache.axis2.description.AxisOperation[6];

        __operation = new org.apache.axis2.description.OutInAxisOperation();

        __operation
                .setName(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com", "queryCorpAccount"));
        _service.addOperation(__operation);

        _operations[0] = __operation;

        __operation = new org.apache.axis2.description.OutInAxisOperation();

        __operation.setName(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                "presentPhoneVolume"));
        _service.addOperation(__operation);

        _operations[1] = __operation;

        __operation = new org.apache.axis2.description.OutInAxisOperation();

        __operation.setName(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                "batchPresentPhoneVolume"));
        _service.addOperation(__operation);

        _operations[2] = __operation;

        __operation = new org.apache.axis2.description.OutInAxisOperation();

        __operation.setName(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com", "presentVolume"));
        _service.addOperation(__operation);

        _operations[3] = __operation;

        __operation = new org.apache.axis2.description.OutInAxisOperation();

        __operation.setName(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                "batchPresentVolume"));
        _service.addOperation(__operation);

        _operations[4] = __operation;

        __operation = new org.apache.axis2.description.OutInAxisOperation();

        __operation
                .setName(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com", "queryUserAccount"));
        _service.addOperation(__operation);

        _operations[5] = __operation;
    }

    //populates the faults
    private void populateFaults() {
    }

    /**
     * Auto generated method signature
     *
     * @param queryCorpAccount0
     */
    public QueryCorpAccountResponse queryCorpAccount(QueryCorpAccount queryCorpAccount0)
            throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;

        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0]
                    .getName());
            _operationClient.getOptions().setAction(
                    "http://webservice.hongbao.ict.cmcc.com/IfAPServicePortType/queryCorpAccountRequest");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), queryCorpAccount0,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                            "queryCorpAccount")), new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "queryCorpAccount"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody().getFirstElement(), QueryCorpAccountResponse.class);

            return (QueryCorpAccountResponse) object;
        } catch (org.apache.axis2.AxisFault f) {
            org.apache.axiom.om.OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                        "queryCorpAccount"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap
                                .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "queryCorpAccount"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(
                                faultElt.getQName(), "queryCorpAccount"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt, messageClass);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext != null && _messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature for Asynchronous Invocations
     *
     * @param queryCorpAccount0
     */
    public void startqueryCorpAccount(QueryCorpAccount queryCorpAccount0, final IfAPServiceCallbackHandler callback)
            throws java.rmi.RemoteException {
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient
                .createClient(_operations[0].getName());
        _operationClient.getOptions().setAction(
                "http://webservice.hongbao.ict.cmcc.com/IfAPServicePortType/queryCorpAccountRequest");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        addPropertyToOperationClient(_operationClient,
                org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

        // create SOAP envelope with that payload
        org.apache.axiom.soap.SOAPEnvelope env = null;
        final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

        //Style is Doc.
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), queryCorpAccount0,
                optimizeContent(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                        "queryCorpAccount")), new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                        "queryCorpAccount"));

        // adding SOAP soap_headers
        _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                try {
                    org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    Object object = fromOM(resultEnv.getBody().getFirstElement(), QueryCorpAccountResponse.class);
                    callback.receiveResultqueryCorpAccount((QueryCorpAccountResponse) object);
                } catch (org.apache.axis2.AxisFault e) {
                    callback.receiveErrorqueryCorpAccount(e);
                }
            }

            public void onError(Exception error) {
                if (error instanceof org.apache.axis2.AxisFault) {
                    org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
                    org.apache.axiom.om.OMElement faultElt = f.getDetail();

                    if (faultElt != null) {
                        if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt
                                .getQName(), "queryCorpAccount"))) {
                            //make the fault by reflection
                            try {
                                String exceptionClassName = (String) faultExceptionClassNameMap
                                        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                                "queryCorpAccount"));
                                Class exceptionClass = Class.forName(exceptionClassName);
                                java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                                Exception ex = (Exception) constructor.newInstance(f.getMessage());

                                //message class
                                String messageClassName = (String) faultMessageMap
                                        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                                "queryCorpAccount"));
                                Class messageClass = Class.forName(messageClassName);
                                Object messageObject = fromOM(faultElt, messageClass);
                                java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorqueryCorpAccount(new java.rmi.RemoteException(ex.getMessage(), ex));
                            } catch (ClassCastException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryCorpAccount(f);
                            } catch (ClassNotFoundException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryCorpAccount(f);
                            } catch (NoSuchMethodException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryCorpAccount(f);
                            } catch (java.lang.reflect.InvocationTargetException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryCorpAccount(f);
                            } catch (IllegalAccessException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryCorpAccount(f);
                            } catch (InstantiationException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryCorpAccount(f);
                            } catch (org.apache.axis2.AxisFault e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryCorpAccount(f);
                            }
                        } else {
                            callback.receiveErrorqueryCorpAccount(f);
                        }
                    } else {
                        callback.receiveErrorqueryCorpAccount(f);
                    }
                } else {
                    callback.receiveErrorqueryCorpAccount(error);
                }
            }

            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils
                        .getInboundFaultFromMessageContext(faultContext);
                onError(fault);
            }

            public void onComplete() {
                try {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                } catch (org.apache.axis2.AxisFault axisFault) {
                    callback.receiveErrorqueryCorpAccount(axisFault);
                }
            }
        });

        org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;

        if ((_operations[0].getMessageReceiver() == null) && _operationClient.getOptions().isUseSeparateListener()) {
            _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
            _operations[0].setMessageReceiver(_callbackReceiver);
        }

        //execute the operation client
        _operationClient.execute(false);
    }

    /**
     * Auto generated method signature
     *
     * @param presentPhoneVolume2
     */
    public PresentPhoneVolumeResponse presentPhoneVolume(PresentPhoneVolume presentPhoneVolume2)
            throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;

        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1]
                    .getName());
            _operationClient.getOptions().setAction(
                    "http://webservice.hongbao.ict.cmcc.com/IfAPServicePortType/presentPhoneVolumeRequest");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), presentPhoneVolume2,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                            "presentPhoneVolume")), new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "presentPhoneVolume"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody().getFirstElement(), PresentPhoneVolumeResponse.class);

            return (PresentPhoneVolumeResponse) object;
        } catch (org.apache.axis2.AxisFault f) {
            org.apache.axiom.om.OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                        "presentPhoneVolume"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap
                                .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "presentPhoneVolume"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(
                                faultElt.getQName(), "presentPhoneVolume"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt, messageClass);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext != null && _messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);

            }
        }
    }

    /**
     * Auto generated method signature for Asynchronous Invocations
     *
     * @param presentPhoneVolume2
     */
    public void startpresentPhoneVolume(PresentPhoneVolume presentPhoneVolume2,
            final IfAPServiceCallbackHandler callback) throws java.rmi.RemoteException {
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient
                .createClient(_operations[1].getName());
        _operationClient.getOptions().setAction(
                "http://webservice.hongbao.ict.cmcc.com/IfAPServicePortType/presentPhoneVolumeRequest");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        addPropertyToOperationClient(_operationClient,
                org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

        // create SOAP envelope with that payload
        org.apache.axiom.soap.SOAPEnvelope env = null;
        final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

        //Style is Doc.
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), presentPhoneVolume2,
                optimizeContent(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                        "presentPhoneVolume")), new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                        "presentPhoneVolume"));

        // adding SOAP soap_headers
        _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                try {
                    org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    Object object = fromOM(resultEnv.getBody().getFirstElement(), PresentPhoneVolumeResponse.class);
                    callback.receiveResultpresentPhoneVolume((PresentPhoneVolumeResponse) object);
                } catch (org.apache.axis2.AxisFault e) {
                    callback.receiveErrorpresentPhoneVolume(e);
                }
            }

            public void onError(Exception error) {
                if (error instanceof org.apache.axis2.AxisFault) {
                    org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
                    org.apache.axiom.om.OMElement faultElt = f.getDetail();

                    if (faultElt != null) {
                        if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt
                                .getQName(), "presentPhoneVolume"))) {
                            //make the fault by reflection
                            try {
                                String exceptionClassName = (String) faultExceptionClassNameMap
                                        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                                "presentPhoneVolume"));
                                Class exceptionClass = Class.forName(exceptionClassName);
                                java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                                Exception ex = (Exception) constructor.newInstance(f.getMessage());

                                //message class
                                String messageClassName = (String) faultMessageMap
                                        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                                "presentPhoneVolume"));
                                Class messageClass = Class.forName(messageClassName);
                                Object messageObject = fromOM(faultElt, messageClass);
                                java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorpresentPhoneVolume(new java.rmi.RemoteException(ex.getMessage(),
                                        ex));
                            } catch (ClassCastException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentPhoneVolume(f);
                            } catch (ClassNotFoundException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentPhoneVolume(f);
                            } catch (NoSuchMethodException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentPhoneVolume(f);
                            } catch (java.lang.reflect.InvocationTargetException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentPhoneVolume(f);
                            } catch (IllegalAccessException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentPhoneVolume(f);
                            } catch (InstantiationException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentPhoneVolume(f);
                            } catch (org.apache.axis2.AxisFault e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentPhoneVolume(f);
                            }
                        } else {
                            callback.receiveErrorpresentPhoneVolume(f);
                        }
                    } else {
                        callback.receiveErrorpresentPhoneVolume(f);
                    }
                } else {
                    callback.receiveErrorpresentPhoneVolume(error);
                }
            }

            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils
                        .getInboundFaultFromMessageContext(faultContext);
                onError(fault);
            }

            public void onComplete() {
                try {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                } catch (org.apache.axis2.AxisFault axisFault) {
                    callback.receiveErrorpresentPhoneVolume(axisFault);
                }
            }
        });

        org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;

        if ((_operations[1].getMessageReceiver() == null) && _operationClient.getOptions().isUseSeparateListener()) {
            _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
            _operations[1].setMessageReceiver(_callbackReceiver);
        }

        //execute the operation client
        _operationClient.execute(false);
    }

    /**
     * Auto generated method signature
     *
     * @param batchPresentPhoneVolume4
     */
    public BatchPresentPhoneVolumeResponse batchPresentPhoneVolume(BatchPresentPhoneVolume batchPresentPhoneVolume4)
            throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;

        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[2]
                    .getName());
            _operationClient.getOptions().setAction(
                    "http://webservice.hongbao.ict.cmcc.com/IfAPServicePortType/batchPresentPhoneVolumeRequest");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), batchPresentPhoneVolume4,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                            "batchPresentPhoneVolume")), new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "batchPresentPhoneVolume"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody().getFirstElement(), BatchPresentPhoneVolumeResponse.class);

            return (BatchPresentPhoneVolumeResponse) object;
        } catch (org.apache.axis2.AxisFault f) {
            org.apache.axiom.om.OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                        "batchPresentPhoneVolume"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap
                                .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                        "batchPresentPhoneVolume"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(
                                faultElt.getQName(), "batchPresentPhoneVolume"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt, messageClass);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext != null && _messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature for Asynchronous Invocations
     *
     * @param batchPresentPhoneVolume4
     */
    public void startbatchPresentPhoneVolume(BatchPresentPhoneVolume batchPresentPhoneVolume4,
            final IfAPServiceCallbackHandler callback) throws java.rmi.RemoteException {
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient
                .createClient(_operations[2].getName());
        _operationClient.getOptions().setAction(
                "http://webservice.hongbao.ict.cmcc.com/IfAPServicePortType/batchPresentPhoneVolumeRequest");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        addPropertyToOperationClient(_operationClient,
                org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

        // create SOAP envelope with that payload
        org.apache.axiom.soap.SOAPEnvelope env = null;
        final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

        //Style is Doc.
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), batchPresentPhoneVolume4,
                optimizeContent(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                        "batchPresentPhoneVolume")), new javax.xml.namespace.QName(
                        "http://webservice.hongbao.ict.cmcc.com", "batchPresentPhoneVolume"));

        // adding SOAP soap_headers
        _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                try {
                    org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    Object object = fromOM(resultEnv.getBody().getFirstElement(), BatchPresentPhoneVolumeResponse.class);
                    callback.receiveResultbatchPresentPhoneVolume((BatchPresentPhoneVolumeResponse) object);
                } catch (org.apache.axis2.AxisFault e) {
                    callback.receiveErrorbatchPresentPhoneVolume(e);
                }
            }

            public void onError(Exception error) {
                if (error instanceof org.apache.axis2.AxisFault) {
                    org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
                    org.apache.axiom.om.OMElement faultElt = f.getDetail();

                    if (faultElt != null) {
                        if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt
                                .getQName(), "batchPresentPhoneVolume"))) {
                            //make the fault by reflection
                            try {
                                String exceptionClassName = (String) faultExceptionClassNameMap
                                        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                                "batchPresentPhoneVolume"));
                                Class exceptionClass = Class.forName(exceptionClassName);
                                java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                                Exception ex = (Exception) constructor.newInstance(f.getMessage());

                                //message class
                                String messageClassName = (String) faultMessageMap
                                        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                                "batchPresentPhoneVolume"));
                                Class messageClass = Class.forName(messageClassName);
                                Object messageObject = fromOM(faultElt, messageClass);
                                java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorbatchPresentPhoneVolume(new java.rmi.RemoteException(ex
                                        .getMessage(), ex));
                            } catch (ClassCastException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentPhoneVolume(f);
                            } catch (ClassNotFoundException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentPhoneVolume(f);
                            } catch (NoSuchMethodException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentPhoneVolume(f);
                            } catch (java.lang.reflect.InvocationTargetException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentPhoneVolume(f);
                            } catch (IllegalAccessException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentPhoneVolume(f);
                            } catch (InstantiationException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentPhoneVolume(f);
                            } catch (org.apache.axis2.AxisFault e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentPhoneVolume(f);
                            }
                        } else {
                            callback.receiveErrorbatchPresentPhoneVolume(f);
                        }
                    } else {
                        callback.receiveErrorbatchPresentPhoneVolume(f);
                    }
                } else {
                    callback.receiveErrorbatchPresentPhoneVolume(error);
                }
            }

            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils
                        .getInboundFaultFromMessageContext(faultContext);
                onError(fault);
            }

            public void onComplete() {
                try {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                } catch (org.apache.axis2.AxisFault axisFault) {
                    callback.receiveErrorbatchPresentPhoneVolume(axisFault);
                }
            }
        });

        org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;

        if ((_operations[2].getMessageReceiver() == null) && _operationClient.getOptions().isUseSeparateListener()) {
            _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
            _operations[2].setMessageReceiver(_callbackReceiver);
        }

        //execute the operation client
        _operationClient.execute(false);
    }

    /**
     * Auto generated method signature
     *
     * @see com.cmcc.vrp.boss.jiangxi.IfAPService#presentVolume
     * @param presentVolume6
     */
    public PresentVolumeResponse presentVolume(PresentVolume presentVolume6) throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;

        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[3]
                    .getName());
            _operationClient.getOptions().setAction(
                    "http://webservice.hongbao.ict.cmcc.com/IfAPServicePortType/presentVolumeRequest");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), presentVolume6,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                            "presentVolume")), new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                            "presentVolume"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody().getFirstElement(), PresentVolumeResponse.class);

            return (PresentVolumeResponse) object;
        } catch (org.apache.axis2.AxisFault f) {
            org.apache.axiom.om.OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                        "presentVolume"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap
                                .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "presentVolume"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(
                                faultElt.getQName(), "presentVolume"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt, messageClass);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext != null && _messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature for Asynchronous Invocations
     *
     * @see com.cmcc.vrp.boss.jiangxi.IfAPService#startpresentVolume
     * @param presentVolume6
     */
    public void startpresentVolume(PresentVolume presentVolume6, final IfAPServiceCallbackHandler callback)
            throws java.rmi.RemoteException {
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient
                .createClient(_operations[3].getName());
        _operationClient.getOptions().setAction(
                "http://webservice.hongbao.ict.cmcc.com/IfAPServicePortType/presentVolumeRequest");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        addPropertyToOperationClient(_operationClient,
                org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

        // create SOAP envelope with that payload
        org.apache.axiom.soap.SOAPEnvelope env = null;
        final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

        //Style is Doc.
        env = toEnvelope(
                getFactory(_operationClient.getOptions().getSoapVersionURI()),
                presentVolume6,
                optimizeContent(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com", "presentVolume")),
                new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com", "presentVolume"));

        // adding SOAP soap_headers
        _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                try {
                    org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    Object object = fromOM(resultEnv.getBody().getFirstElement(), PresentVolumeResponse.class);
                    callback.receiveResultpresentVolume((PresentVolumeResponse) object);
                } catch (org.apache.axis2.AxisFault e) {
                    callback.receiveErrorpresentVolume(e);
                }
            }

            public void onError(Exception error) {
                if (error instanceof org.apache.axis2.AxisFault) {
                    org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
                    org.apache.axiom.om.OMElement faultElt = f.getDetail();

                    if (faultElt != null) {
                        if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt
                                .getQName(), "presentVolume"))) {
                            //make the fault by reflection
                            try {
                                String exceptionClassName = (String) faultExceptionClassNameMap
                                        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                                "presentVolume"));
                                Class exceptionClass = Class.forName(exceptionClassName);
                                java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                                Exception ex = (Exception) constructor.newInstance(f.getMessage());

                                //message class
                                String messageClassName = (String) faultMessageMap
                                        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                                "presentVolume"));
                                Class messageClass = Class.forName(messageClassName);
                                Object messageObject = fromOM(faultElt, messageClass);
                                java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorpresentVolume(new java.rmi.RemoteException(ex.getMessage(), ex));
                            } catch (ClassCastException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentVolume(f);
                            } catch (ClassNotFoundException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentVolume(f);
                            } catch (NoSuchMethodException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentVolume(f);
                            } catch (java.lang.reflect.InvocationTargetException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentVolume(f);
                            } catch (IllegalAccessException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentVolume(f);
                            } catch (InstantiationException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentVolume(f);
                            } catch (org.apache.axis2.AxisFault e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorpresentVolume(f);
                            }
                        } else {
                            callback.receiveErrorpresentVolume(f);
                        }
                    } else {
                        callback.receiveErrorpresentVolume(f);
                    }
                } else {
                    callback.receiveErrorpresentVolume(error);
                }
            }

            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils
                        .getInboundFaultFromMessageContext(faultContext);
                onError(fault);
            }

            public void onComplete() {
                try {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                } catch (org.apache.axis2.AxisFault axisFault) {
                    callback.receiveErrorpresentVolume(axisFault);
                }
            }
        });

        org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;

        if ((_operations[3].getMessageReceiver() == null) && _operationClient.getOptions().isUseSeparateListener()) {
            _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
            _operations[3].setMessageReceiver(_callbackReceiver);
        }

        //execute the operation client
        _operationClient.execute(false);
    }

    /**
     * Auto generated method signature
     *
     * @see com.cmcc.vrp.boss.jiangxi.IfAPService#batchPresentVolume
     * @param batchPresentVolume8
     */
    public BatchPresentVolumeResponse batchPresentVolume(BatchPresentVolume batchPresentVolume8)
            throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;

        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[4]
                    .getName());
            _operationClient.getOptions().setAction(
                    "http://webservice.hongbao.ict.cmcc.com/IfAPServicePortType/batchPresentVolumeRequest");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), batchPresentVolume8,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                            "batchPresentVolume")), new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "batchPresentVolume"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody().getFirstElement(), BatchPresentVolumeResponse.class);

            return (BatchPresentVolumeResponse) object;
        } catch (org.apache.axis2.AxisFault f) {
            org.apache.axiom.om.OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                        "batchPresentVolume"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap
                                .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "batchPresentVolume"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(
                                faultElt.getQName(), "batchPresentVolume"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt, messageClass);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext != null && _messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature for Asynchronous Invocations
     *
     * @see com.cmcc.vrp.boss.jiangxi.IfAPService#startbatchPresentVolume
     * @param batchPresentVolume8
     */
    public void startbatchPresentVolume(BatchPresentVolume batchPresentVolume8,
            final IfAPServiceCallbackHandler callback) throws java.rmi.RemoteException {
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient
                .createClient(_operations[4].getName());
        _operationClient.getOptions().setAction(
                "http://webservice.hongbao.ict.cmcc.com/IfAPServicePortType/batchPresentVolumeRequest");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        addPropertyToOperationClient(_operationClient,
                org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

        // create SOAP envelope with that payload
        org.apache.axiom.soap.SOAPEnvelope env = null;
        final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

        //Style is Doc.
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), batchPresentVolume8,
                optimizeContent(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                        "batchPresentVolume")), new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                        "batchPresentVolume"));

        // adding SOAP soap_headers
        _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                try {
                    org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    Object object = fromOM(resultEnv.getBody().getFirstElement(), BatchPresentVolumeResponse.class);
                    callback.receiveResultbatchPresentVolume((BatchPresentVolumeResponse) object);
                } catch (org.apache.axis2.AxisFault e) {
                    callback.receiveErrorbatchPresentVolume(e);
                }
            }

            public void onError(Exception error) {
                if (error instanceof org.apache.axis2.AxisFault) {
                    org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
                    org.apache.axiom.om.OMElement faultElt = f.getDetail();

                    if (faultElt != null) {
                        if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt
                                .getQName(), "batchPresentVolume"))) {
                            //make the fault by reflection
                            try {
                                String exceptionClassName = (String) faultExceptionClassNameMap
                                        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                                "batchPresentVolume"));
                                Class exceptionClass = Class.forName(exceptionClassName);
                                java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                                Exception ex = (Exception) constructor.newInstance(f.getMessage());

                                //message class
                                String messageClassName = (String) faultMessageMap
                                        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                                "batchPresentVolume"));
                                Class messageClass = Class.forName(messageClassName);
                                Object messageObject = fromOM(faultElt, messageClass);
                                java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorbatchPresentVolume(new java.rmi.RemoteException(ex.getMessage(),
                                        ex));
                            } catch (ClassCastException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentVolume(f);
                            } catch (ClassNotFoundException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentVolume(f);
                            } catch (NoSuchMethodException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentVolume(f);
                            } catch (java.lang.reflect.InvocationTargetException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentVolume(f);
                            } catch (IllegalAccessException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentVolume(f);
                            } catch (InstantiationException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentVolume(f);
                            } catch (org.apache.axis2.AxisFault e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorbatchPresentVolume(f);
                            }
                        } else {
                            callback.receiveErrorbatchPresentVolume(f);
                        }
                    } else {
                        callback.receiveErrorbatchPresentVolume(f);
                    }
                } else {
                    callback.receiveErrorbatchPresentVolume(error);
                }
            }

            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils
                        .getInboundFaultFromMessageContext(faultContext);
                onError(fault);
            }

            public void onComplete() {
                try {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                } catch (org.apache.axis2.AxisFault axisFault) {
                    callback.receiveErrorbatchPresentVolume(axisFault);
                }
            }
        });

        org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;

        if ((_operations[4].getMessageReceiver() == null) && _operationClient.getOptions().isUseSeparateListener()) {
            _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
            _operations[4].setMessageReceiver(_callbackReceiver);
        }

        //execute the operation client
        _operationClient.execute(false);
    }

    /**
     * Auto generated method signature
     *
     * @see com.cmcc.vrp.boss.jiangxi.IfAPService#queryUserAccount
     * @param queryUserAccount10
     */
    public QueryUserAccountResponse queryUserAccount(QueryUserAccount queryUserAccount10)
            throws java.rmi.RemoteException {
        org.apache.axis2.context.MessageContext _messageContext = null;

        try {
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[5]
                    .getName());
            _operationClient.getOptions().setAction(
                    "http://webservice.hongbao.ict.cmcc.com/IfAPServicePortType/queryUserAccountRequest");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            addPropertyToOperationClient(_operationClient,
                    org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();

            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;

            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), queryUserAccount10,
                    optimizeContent(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                            "queryUserAccount")), new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "queryUserAccount"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);

            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            Object object = fromOM(_returnEnv.getBody().getFirstElement(), QueryUserAccountResponse.class);

            return (QueryUserAccountResponse) object;
        } catch (org.apache.axis2.AxisFault f) {
            org.apache.axiom.om.OMElement faultElt = f.getDetail();

            if (faultElt != null) {
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                        "queryUserAccount"))) {
                    //make the fault by reflection
                    try {
                        String exceptionClassName = (String) faultExceptionClassNameMap
                                .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "queryUserAccount"));
                        Class exceptionClass = Class.forName(exceptionClassName);
                        java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                        Exception ex = (Exception) constructor.newInstance(f.getMessage());

                        //message class
                        String messageClassName = (String) faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(
                                faultElt.getQName(), "queryUserAccount"));
                        Class messageClass = Class.forName(messageClassName);
                        Object messageObject = fromOM(faultElt, messageClass);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    } catch (ClassCastException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                } else {
                    throw f;
                }
            } else {
                throw f;
            }
        } finally {
            if (_messageContext != null && _messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature for Asynchronous Invocations
     *
     * @see com.cmcc.vrp.boss.jiangxi.IfAPService#startqueryUserAccount
     * @param queryUserAccount10
     */
    public void startqueryUserAccount(QueryUserAccount queryUserAccount10, final IfAPServiceCallbackHandler callback)
            throws java.rmi.RemoteException {
        org.apache.axis2.client.OperationClient _operationClient = _serviceClient
                .createClient(_operations[5].getName());
        _operationClient.getOptions().setAction(
                "http://webservice.hongbao.ict.cmcc.com/IfAPServicePortType/queryUserAccountRequest");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        addPropertyToOperationClient(_operationClient,
                org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

        // create SOAP envelope with that payload
        org.apache.axiom.soap.SOAPEnvelope env = null;
        final org.apache.axis2.context.MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

        //Style is Doc.
        env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), queryUserAccount10,
                optimizeContent(new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                        "queryUserAccount")), new javax.xml.namespace.QName("http://webservice.hongbao.ict.cmcc.com",
                        "queryUserAccount"));

        // adding SOAP soap_headers
        _serviceClient.addHeadersToEnvelope(env);
        // create message context with that soap envelope
        _messageContext.setEnvelope(env);

        // add the message context to the operation client
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
            public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
                try {
                    org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    Object object = fromOM(resultEnv.getBody().getFirstElement(), QueryUserAccountResponse.class);
                    callback.receiveResultqueryUserAccount((QueryUserAccountResponse) object);
                } catch (org.apache.axis2.AxisFault e) {
                    callback.receiveErrorqueryUserAccount(e);
                }
            }

            public void onError(Exception error) {
                if (error instanceof org.apache.axis2.AxisFault) {
                    org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
                    org.apache.axiom.om.OMElement faultElt = f.getDetail();

                    if (faultElt != null) {
                        if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt
                                .getQName(), "queryUserAccount"))) {
                            //make the fault by reflection
                            try {
                                String exceptionClassName = (String) faultExceptionClassNameMap
                                        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                                "queryUserAccount"));
                                Class exceptionClass = Class.forName(exceptionClassName);
                                java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
                                Exception ex = (Exception) constructor.newInstance(f.getMessage());

                                //message class
                                String messageClassName = (String) faultMessageMap
                                        .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                                                "queryUserAccount"));
                                Class messageClass = Class.forName(messageClassName);
                                Object messageObject = fromOM(faultElt, messageClass);
                                java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorqueryUserAccount(new java.rmi.RemoteException(ex.getMessage(), ex));
                            } catch (ClassCastException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryUserAccount(f);
                            } catch (ClassNotFoundException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryUserAccount(f);
                            } catch (NoSuchMethodException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryUserAccount(f);
                            } catch (java.lang.reflect.InvocationTargetException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryUserAccount(f);
                            } catch (IllegalAccessException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryUserAccount(f);
                            } catch (InstantiationException e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryUserAccount(f);
                            } catch (org.apache.axis2.AxisFault e) {
                                // we cannot intantiate the class - throw the original Axis fault
                                callback.receiveErrorqueryUserAccount(f);
                            }
                        } else {
                            callback.receiveErrorqueryUserAccount(f);
                        }
                    } else {
                        callback.receiveErrorqueryUserAccount(f);
                    }
                } else {
                    callback.receiveErrorqueryUserAccount(error);
                }
            }

            public void onFault(org.apache.axis2.context.MessageContext faultContext) {
                org.apache.axis2.AxisFault fault = org.apache.axis2.util.Utils
                        .getInboundFaultFromMessageContext(faultContext);
                onError(fault);
            }

            public void onComplete() {
                try {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                } catch (org.apache.axis2.AxisFault axisFault) {
                    callback.receiveErrorqueryUserAccount(axisFault);
                }
            }
        });

        org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;

        if ((_operations[5].getMessageReceiver() == null) && _operationClient.getOptions().isUseSeparateListener()) {
            _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
            _operations[5].setMessageReceiver(_callbackReceiver);
        }

        //execute the operation client
        _operationClient.execute(false);
    }

    private boolean optimizeContent(javax.xml.namespace.QName opName) {
        if (opNameArray == null) {
            return false;
        }

        for (int i = 0; i < opNameArray.length; i++) {
            if (opName.equals(opNameArray[i])) {
                return true;
            }
        }

        return false;
    }

    private org.apache.axiom.om.OMElement toOM(QueryCorpAccount param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(QueryCorpAccount.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(QueryCorpAccountResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(QueryCorpAccountResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(PresentPhoneVolume param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            return param
                    .getOMElement(PresentPhoneVolume.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(PresentPhoneVolumeResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(PresentPhoneVolumeResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(BatchPresentPhoneVolume param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(BatchPresentPhoneVolume.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(BatchPresentPhoneVolumeResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(BatchPresentPhoneVolumeResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(PresentVolume param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(PresentVolume.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(PresentVolumeResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(PresentVolumeResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(BatchPresentVolume param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            return param
                    .getOMElement(BatchPresentVolume.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(BatchPresentVolumeResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(BatchPresentVolumeResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(QueryUserAccount param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(QueryUserAccount.MY_QNAME, org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.om.OMElement toOM(QueryUserAccountResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {
        try {
            return param.getOMElement(QueryUserAccountResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
            QueryCorpAccount param, boolean optimizeContent, javax.xml.namespace.QName elementQName)
            throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(QueryCorpAccount.MY_QNAME, factory));

            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    /* methods to provide back word compatibility */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
            PresentPhoneVolume param, boolean optimizeContent, javax.xml.namespace.QName elementQName)
            throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(PresentPhoneVolume.MY_QNAME, factory));

            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    /* methods to provide back word compatibility */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
            BatchPresentPhoneVolume param, boolean optimizeContent, javax.xml.namespace.QName elementQName)
            throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(BatchPresentPhoneVolume.MY_QNAME, factory));

            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    /* methods to provide back word compatibility */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
            PresentVolume param, boolean optimizeContent, javax.xml.namespace.QName elementQName)
            throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(PresentVolume.MY_QNAME, factory));

            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    /* methods to provide back word compatibility */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
            BatchPresentVolume param, boolean optimizeContent, javax.xml.namespace.QName elementQName)
            throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(BatchPresentVolume.MY_QNAME, factory));

            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    /* methods to provide back word compatibility */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
            QueryUserAccount param, boolean optimizeContent, javax.xml.namespace.QName elementQName)
            throws org.apache.axis2.AxisFault {
        try {
            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(QueryUserAccount.MY_QNAME, factory));

            return emptyEnvelope;
        } catch (org.apache.axis2.databinding.ADBException e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    /* methods to provide back word compatibility */

    /**
     *  get the default envelope
     */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory) {
        return factory.getDefaultEnvelope();
    }

    private Object fromOM(org.apache.axiom.om.OMElement param, Class type) throws org.apache.axis2.AxisFault {
        try {
            if (BatchPresentPhoneVolume.class.equals(type)) {
                return BatchPresentPhoneVolume.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (BatchPresentPhoneVolumeResponse.class.equals(type)) {
                return BatchPresentPhoneVolumeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (BatchPresentVolume.class.equals(type)) {
                return BatchPresentVolume.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (BatchPresentVolumeResponse.class.equals(type)) {
                return BatchPresentVolumeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (PresentPhoneVolume.class.equals(type)) {
                return PresentPhoneVolume.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (PresentPhoneVolumeResponse.class.equals(type)) {
                return PresentPhoneVolumeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (PresentVolume.class.equals(type)) {
                return PresentVolume.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (PresentVolumeResponse.class.equals(type)) {
                return PresentVolumeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (QueryCorpAccount.class.equals(type)) {
                return QueryCorpAccount.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (QueryCorpAccountResponse.class.equals(type)) {
                return QueryCorpAccountResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (QueryUserAccount.class.equals(type)) {
                return QueryUserAccount.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (QueryUserAccountResponse.class.equals(type)) {
                return QueryUserAccountResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        } catch (Exception e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }

        return null;
    }

    //http://hongbao.jxict.cn/jiekou/services/JxydHongbaoVolume
    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:47:55
    */
    public static class BatchPresentPhoneVolumeResponse implements org.apache.axis2.databinding.ADBBean {
        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.hongbao.ict.cmcc.com", "batchPresentPhoneVolumeResponse", "ns1");

        /**
         * field for Out
         */
        protected String localOut;

        private static String generatePrefix(String namespace) {
            if ("http://webservice.hongbao.ict.cmcc.com".equals(namespace)) {
                return "ns1";
            }

            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public String getOut() {
            return localOut;
        }

        /**
         * Auto generated setter method
         * @param param Out
         */
        public void setOut(String param) {
            this.localOut = param;
        }

        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
            return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME));
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, xmlWriter, false);
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter, boolean)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
                boolean serializeType) throws javax.xml.stream.XMLStreamException,
                org.apache.axis2.databinding.ADBException {
            String prefix = null;
            String namespace = null;

            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType) {
                String namespacePrefix = registerPrefix(xmlWriter, "http://webservice.hongbao.ict.cmcc.com");

                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                            + ":batchPresentPhoneVolumeResponse", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                            "batchPresentPhoneVolumeResponse", xmlWriter);
                }
            }

            namespace = "http://webservice.hongbao.ict.cmcc.com";
            writeStartElement(null, namespace, "out", xmlWriter);

            if (localOut == null) {
                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localOut);
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix, String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
            } else {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
                xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attValue);
            } else {
                xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace), namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String attributeNamespace = qname.getNamespaceURI();
            String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }

            String attributeValue;

            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(attributePrefix, namespace, attName, attributeValue);
            }
        }

        /**
         *  method to handle Qnames
         */
        private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();

            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);

                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix, namespaceURI);
                }

                if (prefix.trim().length() > 0) {
                    xmlWriter.writeCharacters(prefix + ":"
                            + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }

                    namespaceURI = qnames[i].getNamespaceURI();

                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);

                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix, namespaceURI);
                        }

                        if (prefix.trim().length() > 0) {
                            stringToWrite
                                    .append(prefix)
                                    .append(":")
                                    .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }

                xmlWriter.writeCharacters(stringToWrite.toString());
            }
        }

        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
                throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);

                    if ((uri == null) || (uri.length() == 0)) {
                        break;
                    }

                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }

                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }

            return prefix;
        }

        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory {
            private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
                    .getLog(Factory.class);

            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static BatchPresentPhoneVolumeResponse parse(javax.xml.stream.XMLStreamReader reader)
                    throws Exception {
                BatchPresentPhoneVolumeResponse object = new BatchPresentPhoneVolumeResponse();

                int event;
                javax.xml.namespace.QName currentQName = null;
                String nillableValue = null;
                String prefix = "";
                String namespaceuri = "";

                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    /*currentQName = reader.getName();*/

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                        String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");

                        if (fullTypeName != null) {
                            String nsPrefix = null;

                            if (fullTypeName.indexOf(":") > -1) {
                                nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                            }

                            nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                            String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                            if (!"batchPresentPhoneVolumeResponse".equals(type)) {
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);

                                return (BatchPresentPhoneVolumeResponse) ExtensionMapper.getTypeObject(nsUri, type,
                                        reader);
                            }
                        }
                    }

                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    //java.util.Vector handledAttributes = new java.util.Vector();

                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if ((reader.isStartElement() && new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "out").equals(reader.getName()))
                            || new javax.xml.namespace.QName("", "out").equals(reader.getName())) {
                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");

                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                            String content = reader.getElementText();

                            object.setOut(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        } else {
                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();
                    } else {
                        // 1 - A start element we are not expecting indicates an invalid parameter was passed
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if (reader.isStartElement()) {
                        // 2 - A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new Exception(e);
                }

                return object;
            }
        } //end of factory class
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:48:59
    */
    public static class PresentVolume implements org.apache.axis2.databinding.ADBBean {
        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.hongbao.ict.cmcc.com", "presentVolume", "ns1");

        /**
         * field for In0
         */
        protected String localIn0;

        private static String generatePrefix(String namespace) {
            if ("http://webservice.hongbao.ict.cmcc.com".equals(namespace)) {
                return "ns1";
            }

            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public String getIn0() {
            return localIn0;
        }

        /**
         * Auto generated setter method
         * @param param In0
         */
        public void setIn0(String param) {
            this.localIn0 = param;
        }

        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
            return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME));
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, xmlWriter, false);
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter, boolean)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
                boolean serializeType) throws javax.xml.stream.XMLStreamException,
                org.apache.axis2.databinding.ADBException {
            String prefix = null;
            String namespace = null;

            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType) {
                String namespacePrefix = registerPrefix(xmlWriter, "http://webservice.hongbao.ict.cmcc.com");

                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                            + ":presentVolume", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "presentVolume",
                            xmlWriter);
                }
            }

            namespace = "http://webservice.hongbao.ict.cmcc.com";
            writeStartElement(null, namespace, "in0", xmlWriter);

            if (localIn0 == null) {
                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localIn0);
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix, String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
            } else {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
                xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attValue);
            } else {
                xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace), namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String attributeNamespace = qname.getNamespaceURI();
            String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }

            String attributeValue;

            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(attributePrefix, namespace, attName, attributeValue);
            }
        }

        /**
         *  method to handle Qnames
         */
        private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();

            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);

                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix, namespaceURI);
                }

                if (prefix.trim().length() > 0) {
                    xmlWriter.writeCharacters(prefix + ":"
                            + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }

                    namespaceURI = qnames[i].getNamespaceURI();

                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);

                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix, namespaceURI);
                        }

                        if (prefix.trim().length() > 0) {
                            stringToWrite
                                    .append(prefix)
                                    .append(":")
                                    .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }

                xmlWriter.writeCharacters(stringToWrite.toString());
            }
        }

        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
                throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);

                    if ((uri == null) || (uri.length() == 0)) {
                        break;
                    }

                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }

                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }

            return prefix;
        }

        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory {
            private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
                    .getLog(Factory.class);

            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static PresentVolume parse(javax.xml.stream.XMLStreamReader reader) throws Exception {
                PresentVolume object = new PresentVolume();

                int event;
                javax.xml.namespace.QName currentQName = null;
                String nillableValue = null;
                String prefix = "";
                String namespaceuri = "";

                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    /*currentQName = reader.getName();*/

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                        String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");

                        if (fullTypeName != null) {
                            String nsPrefix = null;

                            if (fullTypeName.indexOf(":") > -1) {
                                nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                            }

                            nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                            String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                            if (!"presentVolume".equals(type)) {
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);

                                return (PresentVolume) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }

                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    /*java.util.Vector handledAttributes = new java.util.Vector();*/

                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if ((reader.isStartElement() && new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "in0").equals(reader.getName()))
                            || new javax.xml.namespace.QName("", "in0").equals(reader.getName())) {
                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");

                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                            String content = reader.getElementText();

                            object.setIn0(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        } else {
                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();
                    } else {
                        // 1 - A start element we are not expecting indicates an invalid parameter was passed
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if (reader.isStartElement()) {
                        // 2 - A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new Exception(e);
                }

                return object;
            }
        } //end of factory class
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:49:32
    */
    public static class ExtensionMapper {
        public static Object getTypeObject(String namespaceURI, String typeName, javax.xml.stream.XMLStreamReader reader)
                throws Exception {
            throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
        }
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:49:35
    */
    public static class BatchPresentPhoneVolume implements org.apache.axis2.databinding.ADBBean {
        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.hongbao.ict.cmcc.com", "batchPresentPhoneVolume", "ns1");

        /**
         * field for In0
         */
        protected String localIn0;

        private static String generatePrefix(String namespace) {
            if ("http://webservice.hongbao.ict.cmcc.com".equals(namespace)) {
                return "ns1";
            }

            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public String getIn0() {
            return localIn0;
        }

        /**
         * Auto generated setter method
         * @param param In0
         */
        public void setIn0(String param) {
            this.localIn0 = param;
        }

        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
            return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME));
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, xmlWriter, false);
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter, boolean)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
                boolean serializeType) throws javax.xml.stream.XMLStreamException,
                org.apache.axis2.databinding.ADBException {
            String prefix = null;
            String namespace = null;

            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType) {
                String namespacePrefix = registerPrefix(xmlWriter, "http://webservice.hongbao.ict.cmcc.com");

                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                            + ":batchPresentPhoneVolume", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                            "batchPresentPhoneVolume", xmlWriter);
                }
            }

            namespace = "http://webservice.hongbao.ict.cmcc.com";
            writeStartElement(null, namespace, "in0", xmlWriter);

            if (localIn0 == null) {
                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localIn0);
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix, String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
            } else {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
                xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attValue);
            } else {
                xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace), namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String attributeNamespace = qname.getNamespaceURI();
            String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }

            String attributeValue;

            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(attributePrefix, namespace, attName, attributeValue);
            }
        }

        /**
         *  method to handle Qnames
         */
        private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();

            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);

                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix, namespaceURI);
                }

                if (prefix.trim().length() > 0) {
                    xmlWriter.writeCharacters(prefix + ":"
                            + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }

                    namespaceURI = qnames[i].getNamespaceURI();

                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);

                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix, namespaceURI);
                        }

                        if (prefix.trim().length() > 0) {
                            stringToWrite
                                    .append(prefix)
                                    .append(":")
                                    .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }

                xmlWriter.writeCharacters(stringToWrite.toString());
            }
        }

        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
                throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);

                    if ((uri == null) || (uri.length() == 0)) {
                        break;
                    }

                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }

                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }

            return prefix;
        }

        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory {
            private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
                    .getLog(Factory.class);

            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static BatchPresentPhoneVolume parse(javax.xml.stream.XMLStreamReader reader) throws Exception {
                BatchPresentPhoneVolume object = new BatchPresentPhoneVolume();

                int event;
                javax.xml.namespace.QName currentQName = null;
                String nillableValue = null;
                String prefix = "";
                String namespaceuri = "";

                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    /*currentQName = reader.getName();*/

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                        String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");

                        if (fullTypeName != null) {
                            String nsPrefix = null;

                            if (fullTypeName.indexOf(":") > -1) {
                                nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                            }

                            nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                            String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                            if (!"batchPresentPhoneVolume".equals(type)) {
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);

                                return (BatchPresentPhoneVolume) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }

                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    /*java.util.Vector handledAttributes = new java.util.Vector();*/

                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if ((reader.isStartElement() && new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "in0").equals(reader.getName()))
                            || new javax.xml.namespace.QName("", "in0").equals(reader.getName())) {
                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");

                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                            String content = reader.getElementText();

                            object.setIn0(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        } else {
                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();
                    } else {
                        // 1 - A start element we are not expecting indicates an invalid parameter was passed
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if (reader.isStartElement()) {
                        // 2 - A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new Exception(e);
                }

                return object;
            }
        } //end of factory class
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:50:05
    */
    public static class BatchPresentVolumeResponse implements org.apache.axis2.databinding.ADBBean {
        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.hongbao.ict.cmcc.com", "batchPresentVolumeResponse", "ns1");

        /**
         * field for Out
         */
        protected String localOut;

        private static String generatePrefix(String namespace) {
            if ("http://webservice.hongbao.ict.cmcc.com".equals(namespace)) {
                return "ns1";
            }

            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public String getOut() {
            return localOut;
        }

        /**
         * Auto generated setter method
         * @param param Out
         */
        public void setOut(String param) {
            this.localOut = param;
        }

        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
            return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME));
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, xmlWriter, false);
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter, boolean)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
                boolean serializeType) throws javax.xml.stream.XMLStreamException,
                org.apache.axis2.databinding.ADBException {
            String prefix = null;
            String namespace = null;

            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType) {
                String namespacePrefix = registerPrefix(xmlWriter, "http://webservice.hongbao.ict.cmcc.com");

                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                            + ":batchPresentVolumeResponse", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                            "batchPresentVolumeResponse", xmlWriter);
                }
            }

            namespace = "http://webservice.hongbao.ict.cmcc.com";
            writeStartElement(null, namespace, "out", xmlWriter);

            if (localOut == null) {
                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localOut);
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix, String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
            } else {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
                xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attValue);
            } else {
                xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace), namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String attributeNamespace = qname.getNamespaceURI();
            String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }

            String attributeValue;

            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(attributePrefix, namespace, attName, attributeValue);
            }
        }

        /**
         *  method to handle Qnames
         */
        private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();

            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);

                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix, namespaceURI);
                }

                if (prefix.trim().length() > 0) {
                    xmlWriter.writeCharacters(prefix + ":"
                            + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }

                    namespaceURI = qnames[i].getNamespaceURI();

                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);

                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix, namespaceURI);
                        }

                        if (prefix.trim().length() > 0) {
                            stringToWrite
                                    .append(prefix)
                                    .append(":")
                                    .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }

                xmlWriter.writeCharacters(stringToWrite.toString());
            }
        }

        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
                throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);

                    if ((uri == null) || (uri.length() == 0)) {
                        break;
                    }

                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }

                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }

            return prefix;
        }

        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory {
            private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
                    .getLog(Factory.class);

            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static BatchPresentVolumeResponse parse(javax.xml.stream.XMLStreamReader reader) throws Exception {
                BatchPresentVolumeResponse object = new BatchPresentVolumeResponse();

                int event;
                javax.xml.namespace.QName currentQName = null;
                String nillableValue = null;
                String prefix = "";
                String namespaceuri = "";

                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    /*currentQName = reader.getName();*/

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                        String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");

                        if (fullTypeName != null) {
                            String nsPrefix = null;

                            if (fullTypeName.indexOf(":") > -1) {
                                nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                            }

                            nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                            String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                            if (!"batchPresentVolumeResponse".equals(type)) {
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);

                                return (BatchPresentVolumeResponse) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }

                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    /*java.util.Vector handledAttributes = new java.util.Vector();*/

                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if ((reader.isStartElement() && new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "out").equals(reader.getName()))
                            || new javax.xml.namespace.QName("", "out").equals(reader.getName())) {
                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");

                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                            String content = reader.getElementText();

                            object.setOut(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        } else {
                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();
                    }  else {
                        // 1 - A start element we are not expecting indicates an invalid parameter was passed
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if (reader.isStartElement()) {
                        // 2 - A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new Exception(e);
                }

                return object;
            }
        } //end of factory class
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:50:57
    */
    public static class BatchPresentVolume implements org.apache.axis2.databinding.ADBBean {
        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.hongbao.ict.cmcc.com", "batchPresentVolume", "ns1");

        /**
         * field for In0
         */
        protected String localIn0;

        private static String generatePrefix(String namespace) {
            if ("http://webservice.hongbao.ict.cmcc.com".equals(namespace)) {
                return "ns1";
            }

            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public String getIn0() {
            return localIn0;
        }

        /**
         * Auto generated setter method
         * @param param In0
         */
        public void setIn0(String param) {
            this.localIn0 = param;
        }

        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
            return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME));
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, xmlWriter, false);
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter, boolean)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
                boolean serializeType) throws javax.xml.stream.XMLStreamException,
                org.apache.axis2.databinding.ADBException {
            String prefix = null;
            String namespace = null;

            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType) {
                String namespacePrefix = registerPrefix(xmlWriter, "http://webservice.hongbao.ict.cmcc.com");

                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                            + ":batchPresentVolume", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "batchPresentVolume",
                            xmlWriter);
                }
            }

            namespace = "http://webservice.hongbao.ict.cmcc.com";
            writeStartElement(null, namespace, "in0", xmlWriter);

            if (localIn0 == null) {
                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localIn0);
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix, String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
            } else {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
                xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attValue);
            } else {
                xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace), namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String attributeNamespace = qname.getNamespaceURI();
            String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }

            String attributeValue;

            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(attributePrefix, namespace, attName, attributeValue);
            }
        }

        /**
         *  method to handle Qnames
         */
        private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();

            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);

                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix, namespaceURI);
                }

                if (prefix.trim().length() > 0) {
                    xmlWriter.writeCharacters(prefix + ":"
                            + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }

                    namespaceURI = qnames[i].getNamespaceURI();

                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);

                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix, namespaceURI);
                        }

                        if (prefix.trim().length() > 0) {
                            stringToWrite
                                    .append(prefix)
                                    .append(":")
                                    .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }

                xmlWriter.writeCharacters(stringToWrite.toString());
            }
        }

        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
                throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);

                    if ((uri == null) || (uri.length() == 0)) {
                        break;
                    }

                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }

                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }

            return prefix;
        }

        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory {
            private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
                    .getLog(Factory.class);

            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static BatchPresentVolume parse(javax.xml.stream.XMLStreamReader reader) throws Exception {
                BatchPresentVolume object = new BatchPresentVolume();

                int event;
                javax.xml.namespace.QName currentQName = null;
                String nillableValue = null;
                String prefix = "";
                String namespaceuri = "";

                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    /*currentQName = reader.getName();*/

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                        String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");

                        if (fullTypeName != null) {
                            String nsPrefix = null;

                            if (fullTypeName.indexOf(":") > -1) {
                                nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                            }

                            nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                            String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                            if (!"batchPresentVolume".equals(type)) {
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);

                                return (BatchPresentVolume) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }

                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    /*java.util.Vector handledAttributes = new java.util.Vector();*/

                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if ((reader.isStartElement() && new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "in0").equals(reader.getName()))
                            || new javax.xml.namespace.QName("", "in0").equals(reader.getName())) {
                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");

                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                            String content = reader.getElementText();

                            object.setIn0(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        } else {
                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();
                    } else {
                        // 1 - A start element we are not expecting indicates an invalid parameter was passed
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if (reader.isStartElement()) {
                        // 2 - A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new Exception(e);
                }

                return object;
            }
        } //end of factory class
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:51:30
    */
    public static class QueryCorpAccountResponse implements org.apache.axis2.databinding.ADBBean {
        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.hongbao.ict.cmcc.com", "queryCorpAccountResponse", "ns1");

        /**
         * field for Out
         */
        protected String localOut;

        private static String generatePrefix(String namespace) {
            if ("http://webservice.hongbao.ict.cmcc.com".equals(namespace)) {
                return "ns1";
            }

            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public String getOut() {
            return localOut;
        }

        /**
         * Auto generated setter method
         * @param param Out
         */
        public void setOut(String param) {
            this.localOut = param;
        }

        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
            return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME));
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, xmlWriter, false);
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter, boolean)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
                boolean serializeType) throws javax.xml.stream.XMLStreamException,
                org.apache.axis2.databinding.ADBException {
            String prefix = null;
            String namespace = null;

            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType) {
                String namespacePrefix = registerPrefix(xmlWriter, "http://webservice.hongbao.ict.cmcc.com");

                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                            + ":queryCorpAccountResponse", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                            "queryCorpAccountResponse", xmlWriter);
                }
            }

            namespace = "http://webservice.hongbao.ict.cmcc.com";
            writeStartElement(null, namespace, "out", xmlWriter);

            if (localOut == null) {
                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localOut);
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix, String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
            } else {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
                xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attValue);
            } else {
                xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace), namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String attributeNamespace = qname.getNamespaceURI();
            String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }

            String attributeValue;

            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(attributePrefix, namespace, attName, attributeValue);
            }
        }

        /**
         *  method to handle Qnames
         */
        private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();

            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);

                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix, namespaceURI);
                }

                if (prefix.trim().length() > 0) {
                    xmlWriter.writeCharacters(prefix + ":"
                            + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }

                    namespaceURI = qnames[i].getNamespaceURI();

                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);

                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix, namespaceURI);
                        }

                        if (prefix.trim().length() > 0) {
                            stringToWrite
                                    .append(prefix)
                                    .append(":")
                                    .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }

                xmlWriter.writeCharacters(stringToWrite.toString());
            }
        }

        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
                throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);

                    if ((uri == null) || (uri.length() == 0)) {
                        break;
                    }

                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }

                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }

            return prefix;
        }

        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory {
            private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
                    .getLog(Factory.class);

            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static QueryCorpAccountResponse parse(javax.xml.stream.XMLStreamReader reader) throws Exception {
                QueryCorpAccountResponse object = new QueryCorpAccountResponse();

                int event;
                javax.xml.namespace.QName currentQName = null;
                String nillableValue = null;
                String prefix = "";
                String namespaceuri = "";

                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    /*currentQName = reader.getName();*/

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                        String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");

                        if (fullTypeName != null) {
                            String nsPrefix = null;

                            if (fullTypeName.indexOf(":") > -1) {
                                nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                            }

                            nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                            String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                            if (!"queryCorpAccountResponse".equals(type)) {
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);

                                return (QueryCorpAccountResponse) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }

                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    /*java.util.Vector handledAttributes = new java.util.Vector();*/

                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if ((reader.isStartElement() && new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "out").equals(reader.getName()))
                            || new javax.xml.namespace.QName("", "out").equals(reader.getName())) {
                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");

                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                            String content = reader.getElementText();

                            object.setOut(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        } else {
                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();
                    } else {
                        // 1 - A start element we are not expecting indicates an invalid parameter was passed
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if (reader.isStartElement()) {
                        // 2 - A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new Exception(e);
                }

                return object;
            }
        } //end of factory class
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:52:15
    */
    public static class PresentPhoneVolume implements org.apache.axis2.databinding.ADBBean {
        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.hongbao.ict.cmcc.com", "presentPhoneVolume", "ns1");

        /**
         * field for In0
         */
        protected String localIn0;

        private static String generatePrefix(String namespace) {
            if ("http://webservice.hongbao.ict.cmcc.com".equals(namespace)) {
                return "ns1";
            }

            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public String getIn0() {
            return localIn0;
        }

        /**
         * Auto generated setter method
         * @param param In0
         */
        public void setIn0(String param) {
            this.localIn0 = param;
        }

        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
            return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME));
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, xmlWriter, false);
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter, boolean)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
                boolean serializeType) throws javax.xml.stream.XMLStreamException,
                org.apache.axis2.databinding.ADBException {
            String prefix = null;
            String namespace = null;

            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType) {
                String namespacePrefix = registerPrefix(xmlWriter, "http://webservice.hongbao.ict.cmcc.com");

                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                            + ":presentPhoneVolume", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "presentPhoneVolume",
                            xmlWriter);
                }
            }

            namespace = "http://webservice.hongbao.ict.cmcc.com";
            writeStartElement(null, namespace, "in0", xmlWriter);

            if (localIn0 == null) {
                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localIn0);
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix, String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
            } else {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
                xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attValue);
            } else {
                xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace), namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String attributeNamespace = qname.getNamespaceURI();
            String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }

            String attributeValue;

            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(attributePrefix, namespace, attName, attributeValue);
            }
        }

        /**
         *  method to handle Qnames
         */
        private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();

            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);

                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix, namespaceURI);
                }

                if (prefix.trim().length() > 0) {
                    xmlWriter.writeCharacters(prefix + ":"
                            + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }

                    namespaceURI = qnames[i].getNamespaceURI();

                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);

                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix, namespaceURI);
                        }

                        if (prefix.trim().length() > 0) {
                            stringToWrite
                                    .append(prefix)
                                    .append(":")
                                    .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }

                xmlWriter.writeCharacters(stringToWrite.toString());
            }
        }

        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
                throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);

                    if ((uri == null) || (uri.length() == 0)) {
                        break;
                    }

                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }

                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }

            return prefix;
        }

        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory {
            private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
                    .getLog(Factory.class);

            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static PresentPhoneVolume parse(javax.xml.stream.XMLStreamReader reader) throws Exception {
                PresentPhoneVolume object = new PresentPhoneVolume();

                int event;
                javax.xml.namespace.QName currentQName = null;
                String nillableValue = null;
                String prefix = "";
                String namespaceuri = "";

                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    /*currentQName = reader.getName();*/

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                        String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");

                        if (fullTypeName != null) {
                            String nsPrefix = null;

                            if (fullTypeName.indexOf(":") > -1) {
                                nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                            }

                            nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                            String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                            if (!"presentPhoneVolume".equals(type)) {
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);

                                return (PresentPhoneVolume) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }

                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    /* java.util.Vector handledAttributes = new java.util.Vector();*/

                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if ((reader.isStartElement() && new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "in0").equals(reader.getName()))
                            || new javax.xml.namespace.QName("", "in0").equals(reader.getName())) {
                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");

                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                            String content = reader.getElementText();

                            object.setIn0(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        } else {
                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();
                    } else {
                        // 1 - A start element we are not expecting indicates an invalid parameter was passed
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if (reader.isStartElement()) {
                        // 2 - A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new Exception(e);
                }

                return object;
            }
        } //end of factory class
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:53:39
    */
    public static class QueryUserAccountResponse implements org.apache.axis2.databinding.ADBBean {
        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.hongbao.ict.cmcc.com", "queryUserAccountResponse", "ns1");

        /**
         * field for Out
         */
        protected String localOut;

        private static String generatePrefix(String namespace) {
            if ("http://webservice.hongbao.ict.cmcc.com".equals(namespace)) {
                return "ns1";
            }

            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public String getOut() {
            return localOut;
        }

        /**
         * Auto generated setter method
         * @param param Out
         */
        public void setOut(String param) {
            this.localOut = param;
        }

        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
            return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME));
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, xmlWriter, false);
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter, boolean)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
                boolean serializeType) throws javax.xml.stream.XMLStreamException,
                org.apache.axis2.databinding.ADBException {
            String prefix = null;
            String namespace = null;

            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType) {
                String namespacePrefix = registerPrefix(xmlWriter, "http://webservice.hongbao.ict.cmcc.com");

                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                            + ":queryUserAccountResponse", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                            "queryUserAccountResponse", xmlWriter);
                }
            }

            namespace = "http://webservice.hongbao.ict.cmcc.com";
            writeStartElement(null, namespace, "out", xmlWriter);

            if (localOut == null) {
                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localOut);
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix, String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
            } else {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
                xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attValue);
            } else {
                xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace), namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String attributeNamespace = qname.getNamespaceURI();
            String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }

            String attributeValue;

            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(attributePrefix, namespace, attName, attributeValue);
            }
        }

        /**
         *  method to handle Qnames
         */
        private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();

            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);

                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix, namespaceURI);
                }

                if (prefix.trim().length() > 0) {
                    xmlWriter.writeCharacters(prefix + ":"
                            + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }

                    namespaceURI = qnames[i].getNamespaceURI();

                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);

                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix, namespaceURI);
                        }

                        if (prefix.trim().length() > 0) {
                            stringToWrite
                                    .append(prefix)
                                    .append(":")
                                    .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }

                xmlWriter.writeCharacters(stringToWrite.toString());
            }
        }

        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
                throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);

                    if ((uri == null) || (uri.length() == 0)) {
                        break;
                    }

                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }

                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }

            return prefix;
        }

        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory {
            private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
                    .getLog(Factory.class);

            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static QueryUserAccountResponse parse(javax.xml.stream.XMLStreamReader reader) throws Exception {
                QueryUserAccountResponse object = new QueryUserAccountResponse();

                int event;
                javax.xml.namespace.QName currentQName = null;
                String nillableValue = null;
                String prefix = "";
                String namespaceuri = "";

                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    /*currentQName = reader.getName();*/

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                        String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");

                        if (fullTypeName != null) {
                            String nsPrefix = null;

                            if (fullTypeName.indexOf(":") > -1) {
                                nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                            }

                            nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                            String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                            if (!"queryUserAccountResponse".equals(type)) {
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);

                                return (QueryUserAccountResponse) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }

                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    /*java.util.Vector handledAttributes = new java.util.Vector();*/

                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if ((reader.isStartElement() && new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "out").equals(reader.getName()))
                            || new javax.xml.namespace.QName("", "out").equals(reader.getName())) {
                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");

                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                            String content = reader.getElementText();

                            object.setOut(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        } else {
                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();
                    } else {
                        // 1 - A start element we are not expecting indicates an invalid parameter was passed
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if (reader.isStartElement()) {
                        // 2 - A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new Exception(e);
                }

                return object;
            }
        } //end of factory class
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:54:26
    */
    public static class PresentVolumeResponse implements org.apache.axis2.databinding.ADBBean {
        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.hongbao.ict.cmcc.com", "presentVolumeResponse", "ns1");

        /**
         * field for Out
         */
        protected String localOut;

        private static String generatePrefix(String namespace) {
            if ("http://webservice.hongbao.ict.cmcc.com".equals(namespace)) {
                return "ns1";
            }

            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public String getOut() {
            return localOut;
        }

        /**
         * Auto generated setter method
         * @param param Out
         */
        public void setOut(String param) {
            this.localOut = param;
        }

        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
            return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME));
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, xmlWriter, false);
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter, boolean)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
                boolean serializeType) throws javax.xml.stream.XMLStreamException,
                org.apache.axis2.databinding.ADBException {
            String prefix = null;
            String namespace = null;

            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType) {
                String namespacePrefix = registerPrefix(xmlWriter, "http://webservice.hongbao.ict.cmcc.com");

                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                            + ":presentVolumeResponse", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "presentVolumeResponse",
                            xmlWriter);
                }
            }

            namespace = "http://webservice.hongbao.ict.cmcc.com";
            writeStartElement(null, namespace, "out", xmlWriter);

            if (localOut == null) {
                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localOut);
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix, String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
            } else {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
                xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attValue);
            } else {
                xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace), namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String attributeNamespace = qname.getNamespaceURI();
            String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }

            String attributeValue;

            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(attributePrefix, namespace, attName, attributeValue);
            }
        }

        /**
         *  method to handle Qnames
         */
        private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();

            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);

                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix, namespaceURI);
                }

                if (prefix.trim().length() > 0) {
                    xmlWriter.writeCharacters(prefix + ":"
                            + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }

                    namespaceURI = qnames[i].getNamespaceURI();

                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);

                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix, namespaceURI);
                        }

                        if (prefix.trim().length() > 0) {
                            stringToWrite
                                    .append(prefix)
                                    .append(":")
                                    .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }

                xmlWriter.writeCharacters(stringToWrite.toString());
            }
        }

        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
                throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);

                    if ((uri == null) || (uri.length() == 0)) {
                        break;
                    }

                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }

                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }

            return prefix;
        }

        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory {
            private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
                    .getLog(Factory.class);

            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static PresentVolumeResponse parse(javax.xml.stream.XMLStreamReader reader) throws Exception {
                PresentVolumeResponse object = new PresentVolumeResponse();

                int event;
                javax.xml.namespace.QName currentQName = null;
                String nillableValue = null;
                String prefix = "";
                String namespaceuri = "";

                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    /*currentQName = reader.getName();*/

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                        String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");

                        if (fullTypeName != null) {
                            String nsPrefix = null;

                            if (fullTypeName.indexOf(":") > -1) {
                                nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                            }

                            nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                            String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                            if (!"presentVolumeResponse".equals(type)) {
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);

                                return (PresentVolumeResponse) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }

                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    /*java.util.Vector handledAttributes = new java.util.Vector();*/

                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if ((reader.isStartElement() && new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "out").equals(reader.getName()))
                            || new javax.xml.namespace.QName("", "out").equals(reader.getName())) {
                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");

                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                            String content = reader.getElementText();

                            object.setOut(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        } else {
                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();
                    } else {
                        // 1 - A start element we are not expecting indicates an invalid parameter was passed
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if (reader.isStartElement()) {
                        // 2 - A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new Exception(e);
                }

                return object;
            }
        } //end of factory class
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:55:00
    */
    public static class QueryCorpAccount implements org.apache.axis2.databinding.ADBBean {
        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.hongbao.ict.cmcc.com", "queryCorpAccount", "ns1");

        /**
         * field for In0
         */
        protected String localIn0;

        private static String generatePrefix(String namespace) {
            if ("http://webservice.hongbao.ict.cmcc.com".equals(namespace)) {
                return "ns1";
            }

            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public String getIn0() {
            return localIn0;
        }

        /**
         * Auto generated setter method
         * @param param In0
         */
        public void setIn0(String param) {
            this.localIn0 = param;
        }

        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
            return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME));
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, xmlWriter, false);
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter, boolean)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
                boolean serializeType) throws javax.xml.stream.XMLStreamException,
                org.apache.axis2.databinding.ADBException {
            String prefix = null;
            String namespace = null;

            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType) {
                String namespacePrefix = registerPrefix(xmlWriter, "http://webservice.hongbao.ict.cmcc.com");

                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                            + ":queryCorpAccount", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "queryCorpAccount",
                            xmlWriter);
                }
            }

            namespace = "http://webservice.hongbao.ict.cmcc.com";
            writeStartElement(null, namespace, "in0", xmlWriter);

            if (localIn0 == null) {
                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localIn0);
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix, String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
            } else {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
                xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attValue);
            } else {
                xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace), namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String attributeNamespace = qname.getNamespaceURI();
            String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }

            String attributeValue;

            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(attributePrefix, namespace, attName, attributeValue);
            }
        }

        /**
         *  method to handle Qnames
         */
        private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();

            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);

                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix, namespaceURI);
                }

                if (prefix.trim().length() > 0) {
                    xmlWriter.writeCharacters(prefix + ":"
                            + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }

                    namespaceURI = qnames[i].getNamespaceURI();

                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);

                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix, namespaceURI);
                        }

                        if (prefix.trim().length() > 0) {
                            stringToWrite
                                    .append(prefix)
                                    .append(":")
                                    .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }

                xmlWriter.writeCharacters(stringToWrite.toString());
            }
        }

        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
                throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);

                    if ((uri == null) || (uri.length() == 0)) {
                        break;
                    }

                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }

                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }

            return prefix;
        }

        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory {
            private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
                    .getLog(Factory.class);

            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static QueryCorpAccount parse(javax.xml.stream.XMLStreamReader reader) throws Exception {
                QueryCorpAccount object = new QueryCorpAccount();

                int event;
                javax.xml.namespace.QName currentQName = null;
                String nillableValue = null;
                String prefix = "";
                String namespaceuri = "";

                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    /*currentQName = reader.getName();*/

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                        String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");

                        if (fullTypeName != null) {
                            String nsPrefix = null;

                            if (fullTypeName.indexOf(":") > -1) {
                                nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                            }

                            nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                            String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                            if (!"queryCorpAccount".equals(type)) {
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);

                                return (QueryCorpAccount) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }

                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    /*java.util.Vector handledAttributes = new java.util.Vector();*/

                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if ((reader.isStartElement() && new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "in0").equals(reader.getName()))
                            || new javax.xml.namespace.QName("", "in0").equals(reader.getName())) {
                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");

                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                            String content = reader.getElementText();

                            object.setIn0(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        } else {
                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();
                    } else {
                        // 1 - A start element we are not expecting indicates an invalid parameter was passed
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if (reader.isStartElement()) {
                        // 2 - A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new Exception(e);
                }

                return object;
            }
        } //end of factory class
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:55:33
    */
    public static class PresentPhoneVolumeResponse implements org.apache.axis2.databinding.ADBBean {
        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.hongbao.ict.cmcc.com", "presentPhoneVolumeResponse", "ns1");

        /**
         * field for Out
         */
        protected String localOut;

        private static String generatePrefix(String namespace) {
            if ("http://webservice.hongbao.ict.cmcc.com".equals(namespace)) {
                return "ns1";
            }

            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public String getOut() {
            return localOut;
        }

        /**
         * Auto generated setter method
         * @param param Out
         */
        public void setOut(String param) {
            this.localOut = param;
        }

        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
            return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME));
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, xmlWriter, false);
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter, boolean)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
                boolean serializeType) throws javax.xml.stream.XMLStreamException,
                org.apache.axis2.databinding.ADBException {
            String prefix = null;
            String namespace = null;

            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType) {
                String namespacePrefix = registerPrefix(xmlWriter, "http://webservice.hongbao.ict.cmcc.com");

                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                            + ":presentPhoneVolumeResponse", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                            "presentPhoneVolumeResponse", xmlWriter);
                }
            }

            namespace = "http://webservice.hongbao.ict.cmcc.com";
            writeStartElement(null, namespace, "out", xmlWriter);

            if (localOut == null) {
                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localOut);
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix, String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
            } else {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
                xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attValue);
            } else {
                xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace), namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String attributeNamespace = qname.getNamespaceURI();
            String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }

            String attributeValue;

            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(attributePrefix, namespace, attName, attributeValue);
            }
        }

        /**
         *  method to handle Qnames
         */
        private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();

            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);

                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix, namespaceURI);
                }

                if (prefix.trim().length() > 0) {
                    xmlWriter.writeCharacters(prefix + ":"
                            + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }

                    namespaceURI = qnames[i].getNamespaceURI();

                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);

                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix, namespaceURI);
                        }

                        if (prefix.trim().length() > 0) {
                            stringToWrite
                                    .append(prefix)
                                    .append(":")
                                    .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }

                xmlWriter.writeCharacters(stringToWrite.toString());
            }
        }

        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
                throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);

                    if ((uri == null) || (uri.length() == 0)) {
                        break;
                    }

                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }

                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }

            return prefix;
        }

        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory {
            private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
                    .getLog(Factory.class);

            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static PresentPhoneVolumeResponse parse(javax.xml.stream.XMLStreamReader reader) throws Exception {
                PresentPhoneVolumeResponse object = new PresentPhoneVolumeResponse();

                int event;
                javax.xml.namespace.QName currentQName = null;
                String nillableValue = null;
                String prefix = "";
                String namespaceuri = "";

                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    /*currentQName = reader.getName();*/

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                        String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");

                        if (fullTypeName != null) {
                            String nsPrefix = null;

                            if (fullTypeName.indexOf(":") > -1) {
                                nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                            }

                            nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                            String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                            if (!"presentPhoneVolumeResponse".equals(type)) {
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);

                                return (PresentPhoneVolumeResponse) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }

                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    /*java.util.Vector handledAttributes = new java.util.Vector();*/

                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if ((reader.isStartElement() && new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "out").equals(reader.getName()))
                            || new javax.xml.namespace.QName("", "out").equals(reader.getName())) {
                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");

                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                            String content = reader.getElementText();

                            object.setOut(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        } else {
                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();
                    } else {
                        // 1 - A start element we are not expecting indicates an invalid parameter was passed
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if (reader.isStartElement()) {
                        // 2 - A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new Exception(e);
                }

                return object;
            }
        } //end of factory class
    }

    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 上午10:56:26
    */
    public static class QueryUserAccount implements org.apache.axis2.databinding.ADBBean {
        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://webservice.hongbao.ict.cmcc.com", "queryUserAccount", "ns1");

        /**
         * field for In0
         */
        protected String localIn0;

        private static String generatePrefix(String namespace) {
            if ("http://webservice.hongbao.ict.cmcc.com".equals(namespace)) {
                return "ns1";
            }

            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public String getIn0() {
            return localIn0;
        }

        /**
         * Auto generated setter method
         * @param param In0
         */
        public void setIn0(String param) {
            this.localIn0 = param;
        }

        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException {
            return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(this, MY_QNAME));
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException {
            serialize(parentQName, xmlWriter, false);
        }

        /**
         *  (non-Javadoc)
         * @see org.apache.axis2.databinding.ADBBean#serialize(javax.xml.namespace.QName, javax.xml.stream.XMLStreamWriter, boolean)
         */
        public void serialize(final javax.xml.namespace.QName parentQName, javax.xml.stream.XMLStreamWriter xmlWriter,
                boolean serializeType) throws javax.xml.stream.XMLStreamException,
                org.apache.axis2.databinding.ADBException {
            String prefix = null;
            String namespace = null;

            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType) {
                String namespacePrefix = registerPrefix(xmlWriter, "http://webservice.hongbao.ict.cmcc.com");

                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)) {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                            + ":queryUserAccount", xmlWriter);
                } else {
                    writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "queryUserAccount",
                            xmlWriter);
                }
            }

            namespace = "http://webservice.hongbao.ict.cmcc.com";
            writeStartElement(null, namespace, "in0", xmlWriter);

            if (localIn0 == null) {
                // write the nil attribute
                writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "nil", "1", xmlWriter);
            } else {
                xmlWriter.writeCharacters(localIn0);
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(String prefix, String namespace, String localPart,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(String prefix, String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String writerPrefix = xmlWriter.getPrefix(namespace);

            if (writerPrefix != null) {
                xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
            } else {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
                xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(String namespace, String attName, String attValue,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attValue);
            } else {
                xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace), namespace, attName, attValue);
            }
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(String namespace, String attName, javax.xml.namespace.QName qname,
                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            String attributeNamespace = qname.getNamespaceURI();
            String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }

            String attributeValue;

            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if ("".equals(namespace)) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(attributePrefix, namespace, attName, attributeValue);
            }
        }

        /**
         *  method to handle Qnames
         */
        private void writeQName(javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            String namespaceURI = qname.getNamespaceURI();

            if (namespaceURI != null) {
                String prefix = xmlWriter.getPrefix(namespaceURI);

                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix, namespaceURI);
                }

                if (prefix.trim().length() > 0) {
                    xmlWriter.writeCharacters(prefix + ":"
                            + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames, javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException {
            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                StringBuffer stringToWrite = new StringBuffer();
                String namespaceURI = null;
                String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }

                    namespaceURI = qnames[i].getNamespaceURI();

                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);

                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix, namespaceURI);
                        }

                        if (prefix.trim().length() > 0) {
                            stringToWrite
                                    .append(prefix)
                                    .append(":")
                                    .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }

                xmlWriter.writeCharacters(stringToWrite.toString());
            }
        }

        /**
         * Register a namespace prefix
         */
        private String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, String namespace)
                throws javax.xml.stream.XMLStreamException {
            String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null) {
                prefix = generatePrefix(namespace);

                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

                while (true) {
                    String uri = nsContext.getNamespaceURI(prefix);

                    if ((uri == null) || (uri.length() == 0)) {
                        break;
                    }

                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }

                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }

            return prefix;
        }

        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory {
            private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
                    .getLog(Factory.class);

            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static QueryUserAccount parse(javax.xml.stream.XMLStreamReader reader) throws Exception {
                QueryUserAccount object = new QueryUserAccount();

                int event;
                javax.xml.namespace.QName currentQName = null;
                String nillableValue = null;
                String prefix = "";
                String namespaceuri = "";

                try {
                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    /*currentQName = reader.getName();*/

                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                        String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");

                        if (fullTypeName != null) {
                            String nsPrefix = null;

                            if (fullTypeName.indexOf(":") > -1) {
                                nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                            }

                            nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                            String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                            if (!"queryUserAccount".equals(type)) {
                                //find namespace for the prefix
                                String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);

                                return (QueryUserAccount) ExtensionMapper.getTypeObject(nsUri, type, reader);
                            }
                        }
                    }

                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    /*java.util.Vector handledAttributes = new java.util.Vector();*/

                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if ((reader.isStartElement() && new javax.xml.namespace.QName(
                            "http://webservice.hongbao.ict.cmcc.com", "in0").equals(reader.getName()))
                            || new javax.xml.namespace.QName("", "in0").equals(reader.getName())) {
                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");

                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)) {
                            String content = reader.getElementText();

                            object.setIn0(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));
                        } else {
                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();
                    } else {
                        // 1 - A start element we are not expecting indicates an invalid parameter was passed
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }

                    while (!reader.isStartElement() && !reader.isEndElement()) {
                        reader.next();
                    }

                    if (reader.isStartElement()) {
                        // 2 - A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                    }
                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new Exception(e);
                }

                return object;
            }
        } //end of factory class
    }
}
