package com.cmcc.vrp.boss.shanghai.model.queryproduct;

/**
 * Created by lilin on 2016/8/26.
 */
public class ProductItem {

    private String prodId;
    private String productName;
    private String prodRate;

    public String getProdRate() {
        return prodRate;
    }

    public void setProdRate(String prodRate) {
        this.prodRate = prodRate;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
