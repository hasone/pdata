package com.cmcc.vrp.boss.hainan.uipsoap.client;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:27:34
*/
public class UIP_ServiceLocator extends Service
    implements UIP_Service {
    private String UIPSOAP_address = "http://hiboss.4ggogo.com/uip_inws/services/UIPSOAP";

    private String UIPSOAPWSDDServiceName = "UIPSOAP";

    private HashSet ports = null;

    public UIP_ServiceLocator() {
    }

    public UIP_ServiceLocator(EngineConfiguration config) {
        super(config);
    }

    public UIP_ServiceLocator(String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    public String getUIPSOAPAddress() {
        return this.UIPSOAP_address;
    }

    public String getUIPSOAPWSDDServiceName() {
        return this.UIPSOAPWSDDServiceName;
    }

    public void setUIPSOAPWSDDServiceName(String name) {
        this.UIPSOAPWSDDServiceName = name;
    }

    public UIP_PortType getUIPSOAP() throws ServiceException {
        URL endpoint = null;
        try {
            endpoint = new URL(this.UIPSOAP_address);
        } catch (MalformedURLException e) {
            throw new ServiceException(e);
        }

        return getUIPSOAP(endpoint);
    }

    public UIP_PortType getUIPSOAP(URL portAddress) throws ServiceException {
        try {
            UIPSOAPStub _stub = new UIPSOAPStub(portAddress, this);
            _stub.setPortName(getUIPSOAPWSDDServiceName());
            return _stub;
        } catch (AxisFault e) {
        }
        return null;
    }

    public void setUIPSOAPEndpointAddress(String address) {
        this.UIPSOAP_address = address;
    }

    public Remote getPort(Class serviceEndpointInterface)
        throws ServiceException {
        try {
            if (UIP_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                UIPSOAPStub _stub = new UIPSOAPStub(new URL(this.UIPSOAP_address), this);
                _stub.setPortName(getUIPSOAPWSDDServiceName());
                return _stub;
            }
        } catch (Throwable t) {
            throw new ServiceException(t);
        }
        throw new ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    public Remote getPort(QName portName, Class serviceEndpointInterface)
        throws ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("UIPSOAP".equals(inputPortName)) {
            return getUIPSOAP();
        }

        Remote _stub = getPort(serviceEndpointInterface);
        ((Stub) _stub).setPortName(portName);
        return _stub;
    }

    public QName getServiceName() {
        return new QName("http://www.linkage.com/UIP/", "UIP");
    }

    public Iterator getPorts() {
        if (this.ports == null) {
            this.ports = new HashSet();
            this.ports.add(new QName("http://www.linkage.com/UIP/", "UIPSOAP"));
        }
        return this.ports.iterator();
    }

    public void setEndpointAddress(String portName, String address)
        throws ServiceException {
        if ("UIPSOAP".equals(portName)) {
            setUIPSOAPEndpointAddress(address);
        } else {
            throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    public void setEndpointAddress(QName portName, String address)
        throws ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }
}