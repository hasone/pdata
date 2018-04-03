package com.cmcc.vrp.wx.beans;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * InviteQrcodeReqActionInfo.java
 * @author wujiamin
 * @date 2017年2月27日
 */
public class InviteQrcodeReqActionInfo {
    @JSONField(name="scene")
    private InviteQrcodeReqScene scene;

    public InviteQrcodeReqScene getScene() {
        return scene;
    }

    public void setScene(InviteQrcodeReqScene scene) {
        this.scene = scene;
    }
}
