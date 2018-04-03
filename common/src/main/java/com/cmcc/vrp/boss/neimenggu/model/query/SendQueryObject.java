/**
 * @Title: SendQueryObject.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model.query
 * @author: qihang
 * @date: 2016年2月2日 下午3:48:23
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.query;

import com.cmcc.vrp.boss.neimenggu.model.PubInfo;


/**
 * @ClassName: SendQueryObject
 * @Description: 传输的对象
 * @author: qihang
 * @date: 2016年2月2日 下午3:48:23
 *
 */
public class SendQueryObject {
    //报头
    PubInfo pubInfo;
    //请文
    RequestQueryObject request;

    public PubInfo getPubInfo() {
        return pubInfo;
    }

    public void setPubInfo(PubInfo pubInfo) {
        this.pubInfo = pubInfo;
    }

    public RequestQueryObject getRequest() {
        return request;
    }

    public void setRequest(RequestQueryObject request) {
        this.request = request;
    }


}
