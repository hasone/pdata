/**
 * @Title: RespSignObject.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model
 * @author: qihang
 * @date: 2016年2月5日 上午11:16:19
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.resp;

/**
 * @ClassName: RespSignObject
 * @Description: 返回签名信息类
 * @author: qihang
 * @date: 2016年2月5日 上午11:16:19
 *
 */
public class RespSignObject {
    boolean isSigned;

    String sign;

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean isSigned) {
        this.isSigned = isSigned;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


}
