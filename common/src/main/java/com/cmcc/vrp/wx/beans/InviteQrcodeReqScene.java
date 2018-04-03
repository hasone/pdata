package com.cmcc.vrp.wx.beans;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * InviteQrcodeReqInfo.java
 * @author wujiamin
 * @date 2017年2月23日
 * 
 * edit by qinqinyan on 20170712
 * 修改为使用字符创形式的二维码参数
 * sceneId是平台侧的adminId，但是到微信侧后不具备实际意义，因此更改为adminId|openid
 */
public class InviteQrcodeReqScene {
    
    /*@JSONField(name="scene_id")
    private Integer sceneId;

    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }*/
    
    @JSONField(name="scene_str")
    private String sceneStr;

    public String getSceneStr() {
        return sceneStr;
    }

    public void setSceneStr(String sceneStr) {
        this.sceneStr = sceneStr;
    }
}
