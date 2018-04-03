package com.cmcc.vrp.province.model;

/**
 * Created by qinqinyan on 2016/12/21.
 * modified by wuguoping on 2017/06/08
 * 用于广东流量卡平台
 */
public class GdPrdInfo {

    private String userProductCode;//主体产品编码,在User节点中
    private String subProductCode;//附加产品编码,在Product节点中
    private String serviceCode; //服务编码

    /**
     * 使用的月数
     * 流量自由充，流量统付卡等产品成员套餐周期遵循以下规则，周期传值分别如下：
     *  月    包：1
     *  季度包：3
     *  半年包：6
     *  年度包：12
     */
    private String useCycle;
   
    /**
     * 是否是体验包，体验包不需要Services节点
     * 规定如下：
     *  体验包：0(不需要)
     *  非体验包：1(需要)
     */
    private String isNeedServicesNode;
    
    
    /**
     * @return the useCycle
     */
    public String getUseCycle() {
        return useCycle;
    }

    /**
     * @param useCycle the useCycle to set
     */
    public void setUseCycle(String useCycle) {
        this.useCycle = useCycle;
    }

    /**
     * @return the isNeedServicesNode
     */
    public String getIsNeedServicesNode() {
        return isNeedServicesNode;
    }

    /**
     * @param isNeedServicesNode the isNeedServicesNode to set
     */
    public void setIsNeedServicesNode(String isNeedServicesNode) {
        this.isNeedServicesNode = isNeedServicesNode;
    }

    public String getUserProductCode() {
        return userProductCode;
    }

    public void setUserProductCode(String userProductCode) {
        this.userProductCode = userProductCode;
    }

    public String getSubProductCode() {
        return subProductCode;
    }

    public void setSubProductCode(String subProductCode) {
        this.subProductCode = subProductCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
