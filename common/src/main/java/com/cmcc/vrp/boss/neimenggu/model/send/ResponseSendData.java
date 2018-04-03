/**
 * @Title: ResponseSendData.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model.send
 * @author: qihang
 * @date: 2016年2月5日 上午11:13:45
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.send;

/**
 * @ClassName: ResponseSendData
 * @Description: 充值返回的业务信息
 * @author: qihang
 * @date: 2016年2月5日 上午11:13:45
 *
 */
public class ResponseSendData {
    //{"outSysSn":"14234243242","resultCode":"-28518","resultMsg":"执行节点(node_id\u003d2)时失败:当前订单状态为[1]，不允许进行订单分解！ hint:当前订单状态为[1]，不允许进行订单分解！"}
//{"outSysSn":"14234245468","resultCode":"0","resultMsg":"no error","bossSn":"711602000043062","effDateTime":"2016-02-22","expDateTime":"2016-03-01"}
    String outSysSn;//流水号

    String resultCode;//交易状态，0：成功  其他失败


    String resultMsg;//业务处理信息

    String bossSn;//BOSS订购流水

    String effDateTime;//订购产品生效时间

    String expDateTime;//订购产品失效时间

    public String getBossSn() {
        return bossSn;
    }

    public void setBossSn(String bossSn) {
        this.bossSn = bossSn;
    }

    public String getEffDateTime() {
        return effDateTime;
    }

    public void setEffDateTime(String effDateTime) {
        this.effDateTime = effDateTime;
    }

    public String getExpDateTime() {
        return expDateTime;
    }

    public void setExpDateTime(String expDateTime) {
        this.expDateTime = expDateTime;
    }

    public String getOutSysSn() {
        return outSysSn;
    }

    public void setOutSysSn(String outSysSn) {
        this.outSysSn = outSysSn;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }


}
