package com.cmcc.vrp.boss.shanghai.model.paymember;

import com.cmcc.vrp.boss.shanghai.model.common.InMap;
import com.cmcc.vrp.boss.shanghai.model.common.ProdAttrMap;

/**
 * Created by lilin on 2016/8/25.
 */
public class PayMemberInMap implements InMap {
    private String poSpecId;
    private String bossInsOfferId;
    private String prodSpecId;
    private String memBillId;
    private String channelType;
    private ProdAttrMap prodAttrMap;
    private String TransIDO;

    public String getTransIDO() {
        return TransIDO;
    }

    public void setTransIDO(String transIDO) {
        TransIDO = transIDO;
    }

    public String getPoSpecId() {
        return poSpecId;
    }

    public void setPoSpecId(String poSpecId) {
        this.poSpecId = poSpecId;
    }

    public String getBossInsOfferId() {
        return bossInsOfferId;
    }

    public void setBossInsOfferId(String bossInsOfferId) {
        this.bossInsOfferId = bossInsOfferId;
    }

    public String getProdSpecId() {
        return prodSpecId;
    }

    public void setProdSpecId(String prodSpecId) {
        this.prodSpecId = prodSpecId;
    }

    public String getMemBillId() {
        return memBillId;
    }

    public void setMemBillId(String memBillId) {
        this.memBillId = memBillId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public ProdAttrMap getProdAttrMap() {
        return prodAttrMap;
    }

    public void setProdAttrMap(ProdAttrMap prodAttrMap) {
        this.prodAttrMap = prodAttrMap;
    }
}
