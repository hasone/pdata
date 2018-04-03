/**
 *
 */
package com.cmcc.vrp.boss.gansu.model;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author xj
 * @date 2016年5月16日
 */
public class GSChargeRequestServiceContent {

    private String newBrand;

    private String newPlanId;

    private int validType;

    private List<ProductInfo> prodInfoList;

    public String getNewBrand() {
        return newBrand;
    }

    public void setNewBrand(String newBrand) {
        this.newBrand = newBrand;
    }

    public String getNewPlanId() {
        return newPlanId;
    }

    public void setNewPlanId(String newPlanId) {
        this.newPlanId = newPlanId;
    }

    public int getValidType() {
        return validType;
    }

    public void setValidType(int validType) {
        this.validType = validType;
    }

    public List<ProductInfo> getProdInfoList() {
        return prodInfoList;
    }

    public void setProdInfoList(List<ProductInfo> prodInfoList) {
        this.prodInfoList = prodInfoList;
    }


}
