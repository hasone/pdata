package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by leelyn on 2016/5/23.
 */
@XStreamAlias("Response")
public class ProductsResp {

    @XStreamAlias("EnterpriseId")
    Long enterpriseId;

    @XStreamAlias("TotalCount")
    Integer totalCount;

    @XStreamAlias("Datetime")
    String responseTime;

    @XStreamAlias("Products")
    List<ProductItem> products;

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }
}
