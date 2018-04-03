package com.cmcc.vrp.enums;

/**
 * WxExchangeNumLimit.java
 * @author wujiamin
 * @date 2017年4月6日
 */
public enum WxExchangeNumLimit {
    product_10(10L,50000),
    product_30(30L,5000),
    product_70(70L,5000),
    product_150(150L,5000);

    private Long productSize;

    private Integer limitNum;

    private WxExchangeNumLimit(Long productSize, Integer limitNum) {
        this.setProductSize(productSize);
        this.setLimitNum(limitNum);
    }

    public Long getProductSize() {
        return productSize;
    }

    public void setProductSize(Long productSize) {
        this.productSize = productSize;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }
    
    public static Integer getLimitNumBySize(Long productSize) {
        for (WxExchangeNumLimit item : WxExchangeNumLimit.values()) {            
            if (productSize.equals(item.getProductSize() * 1024)) {
                return item.getLimitNum();
            }
        }
        return null;
    }


}
