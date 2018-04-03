/**
 * @Title: SendSmsObject.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model.sms
 * @author: qihang
 * @date: 2016年2月22日 上午11:37:53
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.sms;

import com.cmcc.vrp.boss.neimenggu.model.PubInfo;


/**
 * @ClassName: SendSmsObject
 * @Description: TODO
 * @author: qihang
 * @date: 2016年2月22日 上午11:37:53
 *
 */
public class SendSmsObject {
    PubInfo pubInfo;

    RequestSmsObject request;

    public PubInfo getPubInfo() {
        return pubInfo;
    }

    public void setPubInfo(PubInfo pubInfo) {
        this.pubInfo = pubInfo;
    }

    public RequestSmsObject getRequest() {
        return request;
    }

    public void setRequest(RequestSmsObject request) {
        this.request = request;
    }


}
