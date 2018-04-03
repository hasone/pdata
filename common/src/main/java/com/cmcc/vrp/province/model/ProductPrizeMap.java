package com.cmcc.vrp.province.model;

/**
 * Created by leelyn on 2016/7/14.
 */
public class ProductPrizeMap {

    private Long prizeCount; // 某一类奖品的数量

    private Long prizeSize; // 该类奖品的流量额度

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
}
