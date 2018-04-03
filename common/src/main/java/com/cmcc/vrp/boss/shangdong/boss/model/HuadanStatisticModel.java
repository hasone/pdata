package com.cmcc.vrp.boss.shangdong.boss.model;

/**
 * 话单统计的模型
 * 所有的01表示话单中01的条目
 * 所有的03表示话单中03的条目
 */
public class HuadanStatisticModel {
    int count01;
    
    int count03;
    
    Double originalPrize01;
    
    Double originalPrize03;
    
    Double discountPrize01;
    
    Double discountPrize03;
    
    public HuadanStatisticModel(){
        count01=0;
        count03=0;
        originalPrize01=0.0d;
        originalPrize03=0.0d;
        
        discountPrize01=0.0d;
        discountPrize03=0.0d;
    }
    

    public int getCount01() {
        return count01;
    }

    public void setCount01(int count01) {
        this.count01 = count01;
    }

    public int getCount03() {
        return count03;
    }

    public void setCount03(int count03) {
        this.count03 = count03;
    }

    public Double getOriginalPrize01() {
        return originalPrize01;
    }

    public void setOriginalPrize01(Double originalPrize01) {
        this.originalPrize01 = originalPrize01;
    }

    public Double getOriginalPrize03() {
        return originalPrize03;
    }

    public void setOriginalPrize03(Double originalPrize03) {
        this.originalPrize03 = originalPrize03;
    }

    public Double getDiscountPrize01() {
        return discountPrize01;
    }

    public void setDiscountPrize01(Double discountPrize01) {
        this.discountPrize01 = discountPrize01;
    }

    public Double getDiscountPrize03() {
        return discountPrize03;
    }

    public void setDiscountPrize03(Double discountPrize03) {
        this.discountPrize03 = discountPrize03;
    }
    
    
}
