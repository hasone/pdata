/**
 *
 */
package com.cmcc.vrp.boss.gansu.model;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年5月16日
 */
public class ProductInfo {

    private String prodId;

    private int prodType;

    private int actionType;

    private String busType;

    private String preProdId;

    private String serviceId;

    private String veType;

    public String getVeType() {
        return veType;
    }

    public void setVeType(String veType) {
        this.veType = veType;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public int getProdType() {
        return prodType;
    }

    public void setProdType(int prodType) {
        this.prodType = prodType;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getPreProdId() {
        return preProdId;
    }

    public void setPreProdId(String preProdId) {
        this.preProdId = preProdId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }


}
