package com.cmcc.vrp.boss.hunan.model;

/**
 * @ClassName: HNBOSSProduct
 * @Description: 湖南省BOSS集团产品信息封装类
 * @author: Rowe
 * @date: 2016年4月7日 下午4:56:06
 */
public class HNBOSSProduct {
    private String PRODUCT_ID;//产品ID

    private String PRODUCT_NAME;//产品名称

    private String DISCNT_CODE;//资费编码

    private String DISCNT_NAME;//资费描述

    public String getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public void setPRODUCT_ID(String pRODUCT_ID) {
        PRODUCT_ID = pRODUCT_ID;
    }

    public String getPRODUCT_NAME() {
        return PRODUCT_NAME;
    }

    public void setPRODUCT_NAME(String pRODUCT_NAME) {
        PRODUCT_NAME = pRODUCT_NAME;
    }

    public String getDISCNT_CODE() {
        return DISCNT_CODE;
    }

    public void setDISCNT_CODE(String dISCNT_CODE) {
        DISCNT_CODE = dISCNT_CODE;
    }

    public String getDISCNT_NAME() {
        return DISCNT_NAME;
    }

    public void setDISCNT_NAME(String dISCNT_NAME) {
        DISCNT_NAME = dISCNT_NAME;
    }

}
