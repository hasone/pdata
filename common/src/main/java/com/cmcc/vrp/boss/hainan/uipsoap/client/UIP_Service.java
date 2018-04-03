package com.cmcc.vrp.boss.hainan.uipsoap.client;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import java.net.URL;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:27:27
*/
public abstract interface UIP_Service extends Service {
    public abstract String getUIPSOAPAddress();

    public abstract UIP_PortType getUIPSOAP()
        throws ServiceException;

    public abstract UIP_PortType getUIPSOAP(URL paramURL)
        throws ServiceException;
}