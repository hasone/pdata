/**
 * @Title: RequestObject.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model
 * @author: qihang
 * @date: 2016年2月2日 上午9:16:13
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.send;

/**
 * @ClassName: RequestObject
 * @Description:
 * @author: qihang
 * @date: 2016年2月2日 上午9:16:13
 *
 */
public class RequestChargeObject {
    ChargeBusiParams busiParams;//充值参数

    String busiCode;  //接口编码：OP_AcceptGroupProd

    public ChargeBusiParams getBusiParams() {
        return busiParams;
    }

    public void setBusiParams(ChargeBusiParams busiParams) {
        this.busiParams = busiParams;
    }

    public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }


}
