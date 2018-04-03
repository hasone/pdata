package com.cmcc.vrp.boss.hainan.uipsoap.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:26:35
*/
public abstract interface UIP_PortType extends Remote {
    /**
     * @param paramBusiCallRequest
     * @return
     * @throws RemoteException
     */
    public abstract BusiCallResponse busiCall(BusiCallRequest paramBusiCallRequest)
        throws RemoteException;

    /**
     * @param paramBusinessCallRequest
     * @return
     * @throws RemoteException
     */
    public abstract String businessCall(BusinessCallRequest paramBusinessCallRequest)
        throws RemoteException;
}