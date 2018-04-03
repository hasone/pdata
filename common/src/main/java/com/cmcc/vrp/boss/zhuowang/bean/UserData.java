package com.cmcc.vrp.boss.zhuowang.bean;

/**
 * 订购信息
 *
 * @author qinpo
 */
public class UserData {

    private boolean isRecharge = false;

    private long rechargeId;
    private Long id;
    /**
     * 企业id
     */
    private Long enterpriseId;
    /**
     * appid
     */
    private String appKey;
    /**
     * 充值请求商品编码
     */
    private String productCode;
    /**
     * 用户手机号
     */
    private String mobNum;
    /**
     * 订购叠加套餐
     */
    private String userPackage;

    public boolean isRecharge() {
        return isRecharge;
    }

    public void setRecharge(boolean isRecharge) {
        this.isRecharge = isRecharge;
    }

    public long getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(long rechargeId) {
        this.rechargeId = rechargeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public String getUserPackage() {
        return userPackage;
    }

    public void setUserPackage(String userPackage) {
        this.userPackage = userPackage;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        UserData user = (UserData) obj;
        if (!mobNum.equals(user.mobNum)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
