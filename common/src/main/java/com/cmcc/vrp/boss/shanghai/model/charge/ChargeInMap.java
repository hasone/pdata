package com.cmcc.vrp.boss.shanghai.model.charge;

import com.cmcc.vrp.boss.shanghai.model.common.InMap;

/**
 * @author lgk8023
 *
 */
public class ChargeInMap implements InMap{
    private String mainBillId;
    
    private String offerId;
    
    private String memOrdProds;
    
    private String memBillId;

    private String serialNum;

    public String getMainBillId() {
        return mainBillId;
    }

    public void setMainBillId(String mainBillId) {
        this.mainBillId = mainBillId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getMemOrdProds() {
        return memOrdProds;
    }

    public void setMemOrdProds(String memOrdProds) {
        this.memOrdProds = memOrdProds;
    }

    public String getMemBillId() {
        return memBillId;
    }

    public void setMemBillId(String memBillId) {
        this.memBillId = memBillId;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

}
