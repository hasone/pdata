/**
 * IfAPServiceCallbackHandler.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.2  Built on : May 02, 2016 (05:55:18 BST)
 */
package com.cmcc.vrp.boss.jiangxi;


/**
 *  IfAPServiceCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class IfAPServiceCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public IfAPServiceCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public IfAPServiceCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for queryCorpAccount method
     * override this method for handling normal response from queryCorpAccount operation
     */
    public void receiveResultqueryCorpAccount(
        com.cmcc.vrp.boss.jiangxi.IfAPServiceStub.QueryCorpAccountResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from queryCorpAccount operation
     */
    public void receiveErrorqueryCorpAccount(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for presentPhoneVolume method
     * override this method for handling normal response from presentPhoneVolume operation
     */
    public void receiveResultpresentPhoneVolume(
        com.cmcc.vrp.boss.jiangxi.IfAPServiceStub.PresentPhoneVolumeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from presentPhoneVolume operation
     */
    public void receiveErrorpresentPhoneVolume(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for batchPresentPhoneVolume method
     * override this method for handling normal response from batchPresentPhoneVolume operation
     */
    public void receiveResultbatchPresentPhoneVolume(
        com.cmcc.vrp.boss.jiangxi.IfAPServiceStub.BatchPresentPhoneVolumeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from batchPresentPhoneVolume operation
     */
    public void receiveErrorbatchPresentPhoneVolume(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for presentVolume method
     * override this method for handling normal response from presentVolume operation
     */
    public void receiveResultpresentVolume(
        com.cmcc.vrp.boss.jiangxi.IfAPServiceStub.PresentVolumeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from presentVolume operation
     */
    public void receiveErrorpresentVolume(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for batchPresentVolume method
     * override this method for handling normal response from batchPresentVolume operation
     */
    public void receiveResultbatchPresentVolume(
        com.cmcc.vrp.boss.jiangxi.IfAPServiceStub.BatchPresentVolumeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from batchPresentVolume operation
     */
    public void receiveErrorbatchPresentVolume(Exception e) {
    }

    /**
     * auto generated Axis2 call back method for queryUserAccount method
     * override this method for handling normal response from queryUserAccount operation
     */
    public void receiveResultqueryUserAccount(
        com.cmcc.vrp.boss.jiangxi.IfAPServiceStub.QueryUserAccountResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from queryUserAccount operation
     */
    public void receiveErrorqueryUserAccount(Exception e) {
    }
}
