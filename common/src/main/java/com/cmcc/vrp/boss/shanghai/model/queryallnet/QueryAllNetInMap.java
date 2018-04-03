package com.cmcc.vrp.boss.shanghai.model.queryallnet;

import com.cmcc.vrp.boss.shanghai.model.common.InMap;

/**
 * Created by lilin on 2016/8/25.
 */
public class QueryAllNetInMap implements InMap {

    private String custServiceId;
    private String poSpecId;

    public String getCustServiceId() {
        return custServiceId;
    }

    public void setCustServiceId(String custServiceId) {
        this.custServiceId = custServiceId;
    }

    public String getPoSpecId() {
        return poSpecId;
    }

    public void setPoSpecId(String poSpecId) {
        this.poSpecId = poSpecId;
    }
}
