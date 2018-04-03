package com.cmcc.vrp.province.model;

/**
 * Created by leelyn on 2016/7/1.
 */
public class AccountPrizeMap {

    private Double accountCount; // 某一产品对应的账户余额

    private Long prdId; //平台产品的id

    private Integer prizeType;

    private Long prizeCount; // 某一类奖品的数量

    private Long prizeSize; // 该类奖品的流量额度

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public Double getAccountCount() {
        return accountCount;
    }

    public void setAccountCount(Double accountCount) {
        this.accountCount = accountCount;
    }

    public Long getPrizeCount() {
        return prizeCount;
    }

    public void setPrizeCount(Long prizeCount) {
        this.prizeCount = prizeCount;
    }

    public Long getPrizeSize() {
        return prizeSize;
    }

    public void setPrizeSize(Long prizeSize) {
        this.prizeSize = prizeSize;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }
}
