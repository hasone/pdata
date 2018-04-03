package com.cmcc.vrp.province.model.statement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * 汇总的信息
 * <p>
 * Created by sunyiwei on 2016/9/19.
 */
@XStreamAlias("Response")
public class TotalStat {
    @XStreamAlias("FingerPrints")
    List<FingerprintStat> fingerprintStatList; //按fingerprint统计的信息

    @XStreamAlias("TotalCount")
    private int totalCount; //总条数

    @XStreamAlias("TotalFlowCount")
    private float totalFlowCount; //总流量数，以M为单位

    @XStreamAlias("TotalPrice")
    private float totalPrice; //总价格

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public float getTotalFlowCount() {
        return totalFlowCount;
    }

    public void setTotalFlowCount(float totalFlowCount) {
        this.totalFlowCount = totalFlowCount;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<FingerprintStat> getFingerprintStatList() {
        return fingerprintStatList;
    }

    public void setFingerprintStatList(List<FingerprintStat> fingerprintStatList) {
        this.fingerprintStatList = fingerprintStatList;
    }
}
