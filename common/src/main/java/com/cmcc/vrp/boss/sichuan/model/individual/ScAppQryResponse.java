package com.cmcc.vrp.boss.sichuan.model.individual;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 四川boss查询套餐内流量余额返回响应
 * @author wujiamin
 * @date 2016年11月1日
 */
public class ScAppQryResponse{
    private String resCode;
    
    private String resMsg;
    
    @JSONField(name="detail_msg")
    private String detailMsg;
    
    private OutData outData;

    
    class OutData {
        @JSONField(name="PHONE_NO")
        String phoneNo;
        
        @JSONField(name="GPRS_CNT_INFO")
        String gprsCntInfo;

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getGprsCntInfo() {
            return gprsCntInfo;
        }

        public void setGprsCntInfo(String gprsCntInfo) {
            this.gprsCntInfo = gprsCntInfo;
        }
    }


    public String getResCode() {
        return resCode;
    }


    public void setResCode(String resCode) {
        this.resCode = resCode;
    }


    public String getResMsg() {
        return resMsg;
    }


    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }


    public String getDetailMsg() {
        return detailMsg;
    }


    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }


    public OutData getOutData() {
        return outData;
    }


    public void setOutData(OutData outData) {
        this.outData = outData;
    }
}
