/**
 * @Title: RespInfo.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model
 * @author: qihang
 * @date: 2016年2月5日 上午11:19:06
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.resp;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: RespInfo
 * @Description: 返回状态信息类
 * @author: qihang
 * @date: 2016年2月5日 上午11:19:06
 *
 */
public class RespInfoObject {
    String code = "-1";

    String message = "";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSussess() {
        return !StringUtils.isEmpty(code) && "0".equals(code);
    }

}
