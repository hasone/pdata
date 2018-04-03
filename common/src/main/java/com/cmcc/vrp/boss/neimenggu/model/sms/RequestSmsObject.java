/**
 * @Title: RequestSmsObject.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model.sms
 * @author: qihang
 * @date: 2016年2月22日 上午11:28:29
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.sms;

/**
 * @ClassName: RequestSmsObject
 * @Description: 短信object
 * @author: qihang
 * @date: 2016年2月22日 上午11:28:29
 *
 */
public class RequestSmsObject {
    SmsBusiParams busiParams;  //短信参数

    String busiCode;//接口编码

    public SmsBusiParams getBusiParams() {
        return busiParams;
    }

    public void setBusiParams(SmsBusiParams busiParams) {
        this.busiParams = busiParams;
    }

    public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }


}
