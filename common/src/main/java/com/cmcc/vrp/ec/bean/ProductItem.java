package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/23.
 */
@XStreamAlias("Product")
public class ProductItem {

    @XStreamAlias("ProductName")
    String productName;

    @XStreamAlias("ProductId")
    String productCode;

    @XStreamAlias("Cost")
    Double cost;

    @XStreamAlias("Size")
    String size;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
