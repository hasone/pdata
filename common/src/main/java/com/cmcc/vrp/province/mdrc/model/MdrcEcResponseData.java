/**
 * @Title: MdrcEcResponseData.java
 * @Package com.cmcc.vrp.province.mdrc.model
 * @author: qihang
 * @date: 2016年5月26日 下午3:58:23
 * @version V1.0
 */
package com.cmcc.vrp.province.mdrc.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @ClassName: MdrcEcResponseData
 * @Description: 提供给营销卡ec接口的返回结果
 * @author: qihang
 * @date: 2016年5月26日 下午3:58:23
 */
@XStreamAlias("Card")
public class MdrcEcResponseData {
    //响应代码， 10000为成功，10001为部分成功， 10002为失败
    @XStreamAlias("RspCode")
    private String rspCode;

    //响应描述
    @XStreamAlias("RspDesc")
    private String rspDesc;

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspDesc() {
        return rspDesc;
    }

    public void setRspDesc(String rspDesc) {
        this.rspDesc = rspDesc;
    }

}
