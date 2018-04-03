package com.cmcc.vrp.boss.xinjiang.wsdl;

import com.cmcc.vrp.boss.xinjiang.wsdl.BusinessCallRequest;
import com.cmcc.vrp.boss.xinjiang.wsdl.UipServiceLocator;
import com.cmcc.vrp.boss.xinjiang.wsdl.UipPortType;

import javax.xml.rpc.ServiceException;

/**
 * 自动生成的代码
 * 
 * @author qihang
 *
 */
public class UipProxy implements UipPortType {
    private String endpoint = null;
    private UipPortType uipPorttype = null;

    public UipProxy() {
        initUIPProxy();
    }

    public UipProxy(String endpoint) {
        this.endpoint = endpoint;
        initUIPProxy();
    }

    private void initUIPProxy() {
        try {
            uipPorttype = (new UipServiceLocator()).getUIPSOAP();
            if (uipPorttype != null) {
                if (endpoint != null) {
                    ((javax.xml.rpc.Stub) uipPorttype)._setProperty(
                            "javax.xml.rpc.service.endpoint.address", endpoint);
                } else {
                    endpoint = (String) ((javax.xml.rpc.Stub) uipPorttype)
                            ._getProperty("javax.xml.rpc.service.endpoint.address");
                }
            }

        } catch (ServiceException serviceException) {

        }
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        if (uipPorttype != null){
            ((javax.xml.rpc.Stub) uipPorttype)._setProperty(
                    "javax.xml.rpc.service.endpoint.address", endpoint);
        }
    }

    
    public UipPortType getUIPPortType() {
        if (uipPorttype == null){
            initUIPProxy();
        }
        return uipPorttype;
    }

    /**
     * 自动生成的代码
     */
    public java.lang.String businessCall(BusinessCallRequest businessCallRequest)
            throws java.rmi.RemoteException {
        if (uipPorttype == null){
            initUIPProxy();
        }
        return uipPorttype.businessCall(businessCallRequest);
    }

}