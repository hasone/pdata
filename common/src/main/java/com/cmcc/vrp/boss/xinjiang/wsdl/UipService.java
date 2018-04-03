/**
 * UIP_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cmcc.vrp.boss.xinjiang.wsdl;

import java.net.URL;

import javax.xml.rpc.ServiceException;

/**
 * 自动生成的代码
 *
 */
public interface UipService extends javax.xml.rpc.Service {
    public String getUIPSOAPAddress();

    public UipPortType getUIPSOAP() throws ServiceException;

    public UipPortType getUIPSOAP(URL portAddress) throws ServiceException;
    
    public void setUIPSOAPAddress(String uipsoapAddress);
}
