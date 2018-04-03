/**
 * TfllbServiceService.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cmcc.vrp.boss.beijing.webservice;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午9:49:49
*/
public interface TfllbServiceService extends javax.xml.rpc.Service {
    public String getTfllbServiceAddress();

    public TfllbService_PortType getTfllbService() throws javax.xml.rpc.ServiceException;

    public TfllbService_PortType getTfllbService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
