/**
 * @Title: RespDataObject.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model
 * @author: qihang
 * @date: 2016年2月5日 上午11:17:08
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.resp;

/**
 * @ClassName: RespDataObject
 * @Description:
 * @author: qihang
 * @date: 2016年2月5日 上午11:17:08
 *
 */
public class RespDataObject {
    String resp_key;

    String resp_msg;

    RespSignObject sign;

    public String getResp_key() {
        return resp_key;
    }

    public void setResp_key(String resp_key) {
        this.resp_key = resp_key;
    }

    public String getResp_msg() {
        return resp_msg;
    }

    public void setResp_msg(String resp_msg) {
        this.resp_msg = resp_msg;
    }

    public RespSignObject getSign() {
        return sign;
    }

    public void setSign(RespSignObject sign) {
        this.sign = sign;
    }


}
