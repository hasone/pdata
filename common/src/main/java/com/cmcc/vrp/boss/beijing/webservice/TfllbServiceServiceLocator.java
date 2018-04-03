/**
 * TfllbServiceServiceLocator.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cmcc.vrp.boss.beijing.webservice;

/**
 * @author wujiamin
 * @date 2016年10月17日下午4:41:33
 */
public class TfllbServiceServiceLocator extends org.apache.axis.client.Service implements TfllbServiceService {

    // Use to get a proxy class for TfllbService
    private String TfllbService_address = "http://221.179.129.226/localdataserver/services/TfllbService";
    // The WSDD service name defaults to the port name.
    private String TfllbServiceWSDDServiceName = "TfllbService";
    private java.util.HashSet ports = null;

    public TfllbServiceServiceLocator() {
    }

    public TfllbServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TfllbServiceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    public String getTfllbServiceAddress() {
        return TfllbService_address;
    }

    public String getTfllbServiceWSDDServiceName() {
        return TfllbServiceWSDDServiceName;
    }

    public void setTfllbServiceWSDDServiceName(String name) {
        TfllbServiceWSDDServiceName = name;
    }

    public TfllbService_PortType getTfllbService() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TfllbService_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTfllbService(endpoint);
    }

    public TfllbService_PortType getTfllbService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cmcc.vrp.boss.beijing.webservice.TfllbServiceSoapBindingStub _stub 
                = new com.cmcc.vrp.boss.beijing.webservice.TfllbServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getTfllbServiceWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTfllbServiceEndpointAddress(String address) {
        TfllbService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (TfllbService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cmcc.vrp.boss.beijing.webservice.TfllbServiceSoapBindingStub _stub 
                    = new com.cmcc.vrp.boss.beijing.webservice.TfllbServiceSoapBindingStub(new java.net.URL(TfllbService_address), this);
                _stub.setPortName(getTfllbServiceWSDDServiceName());
                return _stub;
            }
        } catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " 
                + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("TfllbService".equals(inputPortName)) {
            return getTfllbService();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "TfllbServiceService");
    }

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://server.tflservices.common.bmcc.com", "TfllbService"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

        if ("TfllbService".equals(portName)) {
            setTfllbServiceEndpointAddress(address);
        } else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
