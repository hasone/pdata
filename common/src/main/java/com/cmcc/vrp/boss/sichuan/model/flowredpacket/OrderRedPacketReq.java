package com.cmcc.vrp.boss.sichuan.model.flowredpacket;

import org.apache.commons.lang.StringUtils;

/**
 * <!--[if !supportLists]-->1.1.1.4. <!--[endif]-->请求参数
 * 流量红包请求类，生成get需要的参数
 * @author Administrator
 *
 */
public class OrderRedPacketReq {
    /**
     * 商家APPKEY
     */
    private String appKey;
    
    /**
     * 时间戳
     */
    private String timeStamp;
    
    /**
     * 登录帐号
     */
    private String userName;
    
    /**
     * 手机号码
     */
    private String phoneNo;
    
    /**
     * 操作类型
     */
    private String operateType;
    
    /**
     * 资费代码
     */
    private String prodPrcid;
    
    /**
     * 限制代码
     */
    private String limitCode;
    
    /**
     * 情侣号码
     */
    private String phoneSlv;
    
    /**
     * 操作工号
     */
    private String serviceNo;
 
    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getProdPrcid() {
        return prodPrcid;
    }

    public void setProdPrcid(String prodPrcid) {
        this.prodPrcid = prodPrcid;
    }

    public String getLimitCode() {
        return limitCode;
    }

    public void setLimitCode(String limitCode) {
        this.limitCode = limitCode;
    }

    public String getPhoneSlv() {
        return phoneSlv;
    }

    public void setPhoneSlv(String phoneSlv) {
        this.phoneSlv = phoneSlv;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    /**
     * 得到GET请求?后的参数,为空的不填充
     */
    public String getReqParams(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.isBlank(appKey) ? "&appKey=" : "&appKey=" + appKey);
        buffer.append(StringUtils.isBlank(limitCode) ? "&limit_code=" : "&limit_code=" + limitCode);
        buffer.append(StringUtils.isBlank(operateType) ? "&operate_type=" : "&operate_type=" + operateType);
        buffer.append(StringUtils.isBlank(phoneNo) ? "&phone_no=" : "&phone_no=" + phoneNo);
        buffer.append(StringUtils.isBlank(phoneSlv) ? "&phone_slv=" : "&phone_slv=" + phoneSlv);
        buffer.append(StringUtils.isBlank(prodPrcid) ? "&prod_prcid=" : "&prod_prcid=" + prodPrcid);
        buffer.append(StringUtils.isBlank(serviceNo) ? "&service_no=" : "&service_no=" + serviceNo);
        buffer.append(StringUtils.isBlank(timeStamp) ? "&timeStamp=" : "&timeStamp=" + timeStamp);
        buffer.append(StringUtils.isBlank(userName) ? "&userName=" : "&userName=" + userName);
        String reqStr = buffer.toString();
        return reqStr.length()>0 ? reqStr.substring(1) : reqStr;
     
    }
    
 
    
    public static void main(String[] args){
        OrderRedPacketReq req = new OrderRedPacketReq();
        req.setAppKey("aaa");
        req.setPhoneNo("123456");
        System.out.println(req.getReqParams());
    }
   
}
