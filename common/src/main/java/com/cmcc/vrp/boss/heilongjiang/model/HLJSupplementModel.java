package com.cmcc.vrp.boss.heilongjiang.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:40:41
*/
public class HLJSupplementModel {
    String iOfferId;  //赠送资费代码

    String iEffLength; //赠送月份数

    String iEffType;  //赠送生效方式

    /**
     * @return
     */
    public String getiOfferId() {
        return iOfferId;
    }

    /**
     * @param iOfferId
     */
    public void setiOfferId(String iOfferId) {
        this.iOfferId = iOfferId;
    }

    /**
     * @return
     */
    public String getiEffLength() {
        return iEffLength;
    }

    /**
     * @param iEffLength
     */
    public void setiEffLength(String iEffLength) {
        this.iEffLength = iEffLength;
    }

    /**
     * @return
     */
    public String getiEffType() {
        return iEffType;
    }

    /**
     * @param iEffType
     */
    public void setiEffType(String iEffType) {
        this.iEffType = iEffType;
    }
}
