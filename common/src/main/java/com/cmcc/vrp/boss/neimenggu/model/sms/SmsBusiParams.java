/**
 * @Title: SmsBusiParams.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model.sms
 * @author: qihang
 * @date: 2016年2月22日 上午11:26:06
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.sms;

/**
 * @ClassName: SmsBusiParams
 * @Description: 短信参数
 * @author: qihang
 * @date: 2016年2月22日 上午11:26:06
 *
 */
public class SmsBusiParams {
    String phone_id;

    String message;

    String flag; //短信发送标识 默认"WSZF"

    public String getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(String phone_id) {
        this.phone_id = phone_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


}
