package com.cmcc.vrp.boss.sichuan.model.flowredpacket;

import org.apache.commons.lang.StringUtils;

/**
 * <!--[if !supportLists]-->1.1.1.4. <!--[endif]-->请求参数
 * 流量红包请求类，生成get需要的参数
 * @author Administrator
 *
 */
public class FlowRedPacketReq {
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
    private String opType;
    
    /**
     * 资费代码
     */
    private String prodPrcid;
    
    /**
     * 红包流量
     */
    private String redFlow;
    
    /**
     * 操作工号
     */
    private String loginNo;


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

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getProdPrcid() {
        return prodPrcid;
    }

    public void setProdPrcid(String prodPrcid) {
        this.prodPrcid = prodPrcid;
    }

    public String getRedFlow() {
        return redFlow;
    }

    public void setRedFlow(String redFlow) {
        this.redFlow = redFlow;
    }

    public String getLoginNo() {
        return loginNo;
    }

    public void setLoginNo(String loginNo) {
        this.loginNo = loginNo;
    }

    
   
    
    /**
     * 得到GET请求?后的参数,为空的不填充
     */
    public String getReqParams(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.isBlank(appKey) ? "&appKey=" : "&appKey=" + appKey);
        buffer.append(StringUtils.isBlank(loginNo) ? "&login_no=" : "&login_no=" + loginNo);
        buffer.append(StringUtils.isBlank(opType) ? "&op_type=" : "&op_type=" + opType);
        buffer.append(StringUtils.isBlank(phoneNo) ? "&phone_no=" : "&phone_no=" + phoneNo);
        buffer.append(StringUtils.isBlank(prodPrcid) ? "&prod_prcid=" : "&prod_prcid=" + prodPrcid);
        buffer.append(StringUtils.isBlank(redFlow) ? "&red_flow=" : "&red_flow=" + redFlow);
        buffer.append(StringUtils.isBlank(timeStamp) ? "&timeStamp=" : "&timeStamp=" + timeStamp);
        buffer.append(StringUtils.isBlank(userName) ? "&userName=" : "&userName=" + userName);
        String reqStr = buffer.toString();
        return reqStr.length()>0 ? reqStr.substring(1) : reqStr;
     
    }
    
 
    
    public static void main(String[] args){
        FlowRedPacketReq req = new FlowRedPacketReq();
        req.setAppKey("aaa");
        req.setPhoneNo("123456");
        System.out.println(req.getReqParams());
    }
   
}
