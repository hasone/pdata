package com.cmcc.vrp.boss.shangdong.boss.model;

/**
 * 账单涉及到的统计模型
 * successCount:充值成功个数 
 * originalPrize：总原价
 * discountPrize：总折扣价
 *
 */
public class BillStatisticModel {
    int successCount = 0;
    
    Double originalPrize = 0.0d;
    
    Double discountPrize = 0.0d;

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public Double getOriginalPrize() {
        return originalPrize;
    }

    public void setOriginalPrize(Double originalPrize) {
        this.originalPrize = originalPrize;
    }

    public Double getDiscountPrize() {
        return discountPrize;
    }

    public void setDiscountPrize(Double discountPrize) {
        this.discountPrize = discountPrize;
    }
}
