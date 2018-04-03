package com.cmcc.vrp.boss.shanghai.model.queryproduct;

import com.cmcc.vrp.boss.shanghai.model.common.InMap;

/**
 * Created by lilin on 2016/8/25.
 */
public class QueryProductInMap implements InMap {
    private String offerId;
    private String roleId;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
