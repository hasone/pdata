package com.cmcc.vrp.boss.jiangxi.webservice;

/**
 * @author lgk8023
 *
 */
public class IfAPServicePortTypeProxy implements com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortType {
    private String _endpoint = null;
    private com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortType ifAPServicePortType = null;

    public IfAPServicePortTypeProxy() {
        _initIfAPServicePortTypeProxy();
    }

    public IfAPServicePortTypeProxy(String endpoint) {
        _endpoint = endpoint;
        _initIfAPServicePortTypeProxy();
    }

    /**
     * 
     */
    private void _initIfAPServicePortTypeProxy() {
        try {
            ifAPServicePortType = (new com.cmcc.vrp.boss.jiangxi.webservice.IfAPServiceLocator()).getIfAPServicePort();
            if (ifAPServicePortType != null) {
                if (_endpoint != null) {
                    ((javax.xml.rpc.Stub) ifAPServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
                } else {
                    _endpoint = (String) ((javax.xml.rpc.Stub) ifAPServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
                }
            }

        } catch (javax.xml.rpc.ServiceException serviceException) {
        }
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
        if (ifAPServicePortType != null) {
            ((javax.xml.rpc.Stub) ifAPServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        }

    }

    public com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortType getIfAPServicePortType() {
        if (ifAPServicePortType == null) {
            _initIfAPServicePortTypeProxy();
        }

        return ifAPServicePortType;
    }

    /**
     *  (non-Javadoc)
     * @see com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortType#queryOrderInfo(java.lang.String)
     */
    public java.lang.String queryOrderInfo(java.lang.String arg0) throws java.rmi.RemoteException {
        if (ifAPServicePortType == null) {
            _initIfAPServicePortTypeProxy();
        }

        return ifAPServicePortType.queryOrderInfo(arg0);
    }

    /**
     *  (non-Javadoc)
     * @see com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortType#presentPhoneVolume(java.lang.String)
     */
    public java.lang.String presentPhoneVolume(java.lang.String arg0) throws java.rmi.RemoteException {
        if (ifAPServicePortType == null) {
            _initIfAPServicePortTypeProxy();
        }

        return ifAPServicePortType.presentPhoneVolume(arg0);
    }

    /**
     *  (non-Javadoc)
     * @see com.cmcc.vrp.boss.jiangxi.webservice.IfAPServicePortType#queryCorpAccount(java.lang.String)
     */
    public java.lang.String queryCorpAccount(java.lang.String arg0) throws java.rmi.RemoteException {
        if (ifAPServicePortType == null) {
            _initIfAPServicePortTypeProxy();
        }

        return ifAPServicePortType.queryCorpAccount(arg0);
    }

}