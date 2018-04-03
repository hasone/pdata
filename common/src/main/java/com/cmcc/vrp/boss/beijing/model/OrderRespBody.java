package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/26.
 */
@XStreamAlias("WebBody")
public class OrderRespBody {

    @XStreamAlias("RetInfo")
    private OrderRetInfo retInfo;

    public OrderRetInfo getRetInfo() {
        return retInfo;
    }

    public void setRetInfo(OrderRetInfo retInfo) {
        this.retInfo = retInfo;
    }
}
