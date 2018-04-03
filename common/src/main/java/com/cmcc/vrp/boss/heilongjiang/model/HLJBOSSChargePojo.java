package com.cmcc.vrp.boss.heilongjiang.model;

/**
 * 
 * @ClassName: HLJBOSSCallBackPojo 
 * @Description: 黑龙江充值结果异步通知报文封装类
 * @author: Rowe
 * @date: 2017年3月6日 下午5:25:08
 */
public class HLJBOSSChargePojo {
    
    private String opNumber;//平台过来的流水(BOSS返回请求流水号)
    
    private String status;//是否成功标识:Y,成功。N失败 
    
    private String phoneNo;//充值手机号
    
    private String errorDesc;//失败原因

    public String getOpNumber() {
        return opNumber;
    }

    public void setOpNumber(String opNumber) {
        this.opNumber = opNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
  
}
