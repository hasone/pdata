/**
 * @Title: RespInfo.java
 * @Package com.cmcc.vrp.neimenggu.boss.json.model
 * @author: qihang
 * @date: 2016年2月5日 上午11:21:10
 * @version V1.0
 */
package com.cmcc.vrp.boss.neimenggu.model;

import com.cmcc.vrp.boss.neimenggu.model.resp.RespDataObject;
import com.cmcc.vrp.boss.neimenggu.model.resp.RespInfoObject;
import com.cmcc.vrp.boss.neimenggu.model.resp.RespSignObject;


/**
 * @ClassName: RespInfo
 * @Description: 返回信息类
 * @author: qihang
 * @date: 2016年2月5日 上午11:21:10
 *
 */
public class RespInfo {
    RespInfoObject respInfo;   //返回报头信息

    RespDataObject respData;  //返回信息

    RespSignObject sign;   //返回签名信息


    public RespInfoObject getRespInfo() {
        return respInfo;
    }

    public void setRespInfo(RespInfoObject respInfo) {
        this.respInfo = respInfo;
    }

    public RespDataObject getRespData() {
        return respData;
    }

    public void setRespData(RespDataObject respData) {
        this.respData = respData;
    }

    public RespSignObject getSign() {
        return sign;
    }

    public void setSign(RespSignObject sign) {
        this.sign = sign;
    }


}
