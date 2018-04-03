/**
 * @Title: QueryBusiParams.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model
 * @author: qihang
 * @date: 2016年2月2日 上午8:53:38
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model.send;

/**
 * @ClassName: QueryBusiParams
 * @Description: 充值参数类
 * @author: qihang
 * @date: 2016年2月2日 上午8:53:38
 *
 */
public class ChargeBusiParams {
    String outSysSn;//外系统发起时流水

    String groupId;//集团编号

    String phoneId;//手机号码

    String actType;//操作类型

    String prodId;//集团产品ID

    public String getOutSysSn() {
        return outSysSn;
    }

    public void setOutSysSn(String outSysSn) {
        this.outSysSn = outSysSn;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }


}
