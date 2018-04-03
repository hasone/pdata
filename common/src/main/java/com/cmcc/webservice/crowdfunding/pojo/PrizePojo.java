package com.cmcc.webservice.crowdfunding.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Prize")
public class PrizePojo {
    
    @XStreamAlias("PrizeId")
    private Long prizeId;
    
    @XStreamAlias("PrizeName")
    private String prizeName;
    
    @XStreamAlias("Discount")
    private String discount;
    
    @XStreamAlias("ProductCode")
    private String productCode;
    
    @XStreamAlias("ProductName")
    private String productName;

    public Long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    

}
