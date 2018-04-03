/**
 * TfllbService_PortType.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cmcc.vrp.boss.beijing.webservice;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午9:55:30
*/
/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午9:55:47
*/
public interface TfllbService_PortType extends java.rmi.Remote {
    /**
     * @param xmlParams
     * @return
     * @throws java.rmi.RemoteException
     */
    public String tf_tfzycx(String xmlParams) throws java.rmi.RemoteException;

    /**
     * @param xmlParams
     * @return
     * @throws java.rmi.RemoteException
     */
    public String tf_tjzd(String xmlParams) throws java.rmi.RemoteException;

    /**
     * @param xmlParams
     * @return
     * @throws java.rmi.RemoteException
     */
    public String tf_ddlbcx(String xmlParams) throws java.rmi.RemoteException;

    /**
     * @param xmlParams
     * @return
     * @throws java.rmi.RemoteException
     */
    public String tf_ddmxcx(String xmlParams) throws java.rmi.RemoteException;

    /**
     * @param xmlParams
     * @return
     * @throws java.rmi.RemoteException
     */
    public String tf_tfzyhz(String xmlParams) throws java.rmi.RemoteException;
}
