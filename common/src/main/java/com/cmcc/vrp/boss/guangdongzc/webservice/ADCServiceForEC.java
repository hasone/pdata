/**
 * ADCServiceForEC.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cmcc.vrp.boss.guangdongzc.webservice;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午9:56:47
*/
public interface ADCServiceForEC extends javax.xml.rpc.Service {
    public String getNGADCServicesForECAddress();

    public com.cmcc.vrp.boss.guangdongzc.webservice.NGADCServiceForEC getNGADCServicesForEC() throws javax.xml.rpc.ServiceException;

    public com.cmcc.vrp.boss.guangdongzc.webservice.NGADCServiceForEC getNGADCServicesForEC(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
