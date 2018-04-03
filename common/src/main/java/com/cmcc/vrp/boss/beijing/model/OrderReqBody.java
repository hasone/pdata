package com.cmcc.vrp.boss.beijing.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by leelyn on 2016/5/26.
 */
@XStreamAlias("WebBody")
public class OrderReqBody {

    @XStreamAlias("Params")
    private OrderReqPara reqPara;

    public OrderReqPara getReqPara() {
        return reqPara;
    }

    public void setReqPara(OrderReqPara reqPara) {
        this.reqPara = reqPara;
    }
}
