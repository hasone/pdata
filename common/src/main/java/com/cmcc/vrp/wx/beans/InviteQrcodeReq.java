package com.cmcc.vrp.wx.beans;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 向微信公众号请求邀请二维码的请求对象
 * InviteQrcodeReq.java
 * @author wujiamin
 * @date 2017年2月23日
 * 
 * edit by qinqinyan on 2017/07/12 
 * 将actionName修改为临时字符串
 */
public class InviteQrcodeReq {
    @JSONField(name="expire_seconds")
    private Integer expireSecond;//该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
    
    @JSONField(name="action_name")
    private String actionName = "QR_STR_SCENE"; //二维码类型，QR_SCENE为临时整型参数,QR_STR_SCENE为临时字符串,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
   
    @JSONField(name="action_info")
    private InviteQrcodeReqActionInfo actionInfo; // 二维码详细信息

    public Integer getExpireSecond() {
        return expireSecond;
    }

    public void setExpireSecond(Integer expireSecond) {
        this.expireSecond = expireSecond;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public InviteQrcodeReqActionInfo getActionInfo() {
        return actionInfo;
    }

    public void setActionInfo(InviteQrcodeReqActionInfo actionInfo) {
        this.actionInfo = actionInfo;
    }
}
