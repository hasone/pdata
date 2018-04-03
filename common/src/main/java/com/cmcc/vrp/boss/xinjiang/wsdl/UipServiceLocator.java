/**
 * UIP_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cmcc.vrp.boss.xinjiang.wsdl;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

/**
 * 自动生成的代码
 * @author Administrator
 *
 */
public class UipServiceLocator extends Service implements UipService {

    public UipServiceLocator() {
    }

    public UipServiceLocator(EngineConfiguration config) {
        super(config);
    }

    public UipServiceLocator(String wsdlLoc, QName sName)
            throws ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UIPSOAP
    private String uipsoapAddress = "";

    // private String UIPSOAP_address =
    // "http://10.238.249.5:43000/uip_inws/services/UIPSOAP";

    public String getUIPSOAPAddress() {
        return uipsoapAddress;
    }

    public void setUIPSOAPAddress(String uipsoapAddress) {
        this.uipsoapAddress = uipsoapAddress;
    }

    // The WSDD service name defaults to the port name.
    private String uipsoapwsddServicename = "UIPSOAP";

    public String getUIPSOAPWSDDServiceName() {
        return uipsoapwsddServicename;
    }

    public void setUIPSOAPWSDDServiceName(String name) {
        uipsoapwsddServicename = name;
    }

    public UipPortType getUIPSOAP() throws ServiceException {
        URL endpoint;
        try {
            endpoint = new URL(uipsoapAddress);
        } catch (MalformedURLException e) {
            throw new ServiceException(e);
        }
        return getUIPSOAP(endpoint);
    }

    public UipPortType getUIPSOAP(URL portAddress) throws ServiceException {
        try {
            UIPSOAPSoapBindingStub stub = new UIPSOAPSoapBindingStub(
                    portAddress, this);
            stub.setPortName(getUIPSOAPWSDDServiceName());
            return stub;
        } catch (AxisFault e) {
            return null;
        }
    }

    public void setUIPSOAPEndpointAddress(String address) {
        uipsoapAddress = address;
    }

    /**
     * For the given interface, get the stub implementation. If this service has
     * no port for the given interface, then ServiceException is thrown.
     */
    public Remote getPort(Class serviceEndpointInterface)
            throws ServiceException {
        try {
            if (UipPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                UIPSOAPSoapBindingStub stub = new UIPSOAPSoapBindingStub(
                        new URL(uipsoapAddress), this);
                stub.setPortName(getUIPSOAPWSDDServiceName());
                return stub;
            }
        } catch (java.lang.Throwable t) {
            throw new ServiceException(t);
        }
        throw new ServiceException(
                "There is no stub implementation for the interface:  "
                        + (serviceEndpointInterface == null ? "null"
                                : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation. If this service has
     * no port for the given interface, then ServiceException is thrown.
     */
    public Remote getPort(QName portName, Class serviceEndpointInterface)
            throws ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("UIPSOAP".equals(inputPortName)) {
            return getUIPSOAP();
        } else {
            Remote stub = getPort(serviceEndpointInterface);
            ((Stub) stub).setPortName(portName);
            return stub;
        }
    }

    public QName getServiceName() {
        return new QName("http://www.linkage.com/UIP/", "UIP");
    }

    private HashSet ports = null;

    public Iterator getPorts() {
        if (ports == null) {
            ports = new HashSet();
            ports.add(new QName("http://www.linkage.com/UIP/", "UIPSOAP"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(String portName, String address)
            throws ServiceException {

        if ("UIPSOAP".equals(portName)) {
            setUIPSOAPEndpointAddress(address);
        } else { // Unknown Port Name
            throw new ServiceException(
                    " Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(QName portName, java.lang.String address)
            throws ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
