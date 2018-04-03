package com.cmcc.vrp.boss.neimenggu.model.send;

import com.cmcc.vrp.boss.neimenggu.model.PubInfo;


/**
 * @ClassName: SendChargeObject
 * @Description: 充值对象
 * @author: qihang
 * @date: 2016年4月11日 下午6:51:53
 */
public class SendChargeObject {
    PubInfo pubInfo;  //报头

    RequestChargeObject request;

    public PubInfo getPubInfo() {
        return pubInfo;
    }

    public void setPubInfo(PubInfo pubInfo) {
        this.pubInfo = pubInfo;
    }

    public RequestChargeObject getRequest() {
        return request;
    }

    public void setRequest(RequestChargeObject request) {
        this.request = request;
    }


}
