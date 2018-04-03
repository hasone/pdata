/**
 * IfAPServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cmcc.vrp.boss.jiangxi.webservice;

/**
 * @author lgk8023
 *
 */
public class IfAPServiceLocator extends org.apache.axis.client.Service implements com.cmcc.vrp.boss.jiangxi.webservice.IfAPService {

    public IfAPServiceLocator() {
    }

    public IfAPServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IfAPServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IfAPServicePort
    private java.lang.String IfAPServicePort_address = "http://flux.jx139.com:6060/flux_interface/services/JxydHongbaoVolume";

    public java.lang.String getIfAPServicePortAddress() {
        return IfAPServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IfAPServicePortWSDDServiceName = "IfAPServicePort";

    public java.lang.String getIfAPServicePortWSDDServiceName() {
        return IfAPServicePortWSDDServiceName;
    }

    public void setIfAPServicePortWSDDServiceName(java.lang.String name) {
        IfAPServicePortWSDDServiceName = name;
    }

    public com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortType getIfAPServicePort() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IfAPServicePort_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIfAPServicePort(endpoint);
    }

    public com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortType getIfAPServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cmcc.vrp.boss.jiangxi.webservice.IfAPServiceSoapBindingStub _stub = new com.cmcc.vrp.boss.jiangxi.webservice.IfAPServiceSoapBindingStub(
                    portAddress, this);
            _stub.setPortName(getIfAPServicePortWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIfAPServicePortEndpointAddress(java.lang.String address) {
        IfAPServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation. If this service has no port for the given interface, then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cmcc.vrp.boss.jiangxi.webservice.IfAPServiceSoapBindingStub _stub = new com.cmcc.vrp.boss.jiangxi.webservice.IfAPServiceSoapBindingStub(
                        new java.net.URL(IfAPServicePort_address), this);
                _stub.setPortName(getIfAPServicePortWSDDServiceName());
                return _stub;
            }
        } catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException(
                "There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation. If this service has no port for the given interface, then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("IfAPServicePort".equals(inputPortName)) {
            return getIfAPServicePort();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.interfaces.flux.ict.cmcc.com/", "IfAPService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.interfaces.flux.ict.cmcc.com/", "IfAPServicePort"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

        if ("IfAPServicePort".equals(portName)) {
            setIfAPServicePortEndpointAddress(address);
        } else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
