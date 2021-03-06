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
public class MdrcEcChargeResponseData {
    //响应代码， 10000为成功, 10001为失败
    @XStreamAlias("RspCode")
    private String rspCode;

    //响应描述
    @XStreamAlias("RspDesc")
    private String rspDesc;

    @XStreamAlias("SerialNum")
    private String serialNum;

    @XStreamAlias("PltSerialNum")
    private String pltSerialNum;

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

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getPltSerialNum() {
        return pltSerialNum;
    }

    public void setPltSerialNum(String pltSerialNum) {
        this.pltSerialNum = pltSerialNum;
    }
}
