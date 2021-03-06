/**
 * ADCServiceForECLocator.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cmcc.vrp.wx.flowcoin.webservice;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午9:57:16
*/
public class ADCServiceForECLocator extends org.apache.axis.client.Service implements ADCServiceForEC {

    // Use to get a proxy class for NGADCServicesForEC
    private String NGADCServicesForEC_address = "http://221.179.7.250/NGADCInfcText/NGADCServicesForEC.svc/NGADCServicesForEC";
    // The WSDD service name defaults to the port name.
    private String NGADCServicesForECWSDDServiceName = "NGADCServicesForEC";
    private java.util.HashSet ports = null;

    public ADCServiceForECLocator() {
    }

    public ADCServiceForECLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ADCServiceForECLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    public String getNGADCServicesForECAddress() {
        return NGADCServicesForEC_address;
    }

    public String getNGADCServicesForECWSDDServiceName() {
        return NGADCServicesForECWSDDServiceName;
    }

    public void setNGADCServicesForECWSDDServiceName(String name) {
        NGADCServicesForECWSDDServiceName = name;
    }

    public com.cmcc.vrp.wx.flowcoin.webservice.NGADCServiceForEC getNGADCServicesForEC() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NGADCServicesForEC_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNGADCServicesForEC(endpoint);
    }

    public com.cmcc.vrp.wx.flowcoin.webservice.NGADCServiceForEC getNGADCServicesForEC(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cmcc.vrp.wx.flowcoin.webservice.NGADCServicesForECStub _stub = new com.cmcc.vrp.wx.flowcoin.webservice.NGADCServicesForECStub(portAddress, this);
            _stub.setPortName(getNGADCServicesForECWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNGADCServicesForECEndpointAddress(String address) {
        NGADCServicesForEC_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.cmcc.vrp.wx.flowcoin.webservice.NGADCServiceForEC.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cmcc.vrp.wx.flowcoin.webservice.NGADCServicesForECStub _stub = new com.cmcc.vrp.wx.flowcoin.webservice.NGADCServicesForECStub(new java.net.URL(NGADCServicesForEC_address), this);
                _stub.setPortName(getNGADCServicesForECWSDDServiceName());
                return _stub;
            }
        } catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
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
        if ("NGADCServicesForEC".equals(inputPortName)) {
            return getNGADCServicesForEC();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "ADCServiceForEC");
    }

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "NGADCServicesForEC"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

        if ("NGADCServicesForEC".equals(portName)) {
            setNGADCServicesForECEndpointAddress(address);
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
