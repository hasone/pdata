/**
 * @Title: 	TransCode.java 
 * @Package com.cmcc.xinjiang.boss.enums 
 * @author:	qihang
 * @date:	2016年3月29日 下午10:37:19 
 * @version	V1.0   
 */
package com.cmcc.vrp.boss.xinjiang.enums;

/** 
 * @ClassName:	TransCode 
 * @Description:  接口类型
 * @author:	qihang
 * @date:	2016年3月29日 下午10:37:19 
 *  
 */
public enum TransCode {    
    GetGroupInfo("ITF_FLHQ_GetGroupInfo","XINJ_UNHQ_getEcInfo","集团用户信息查询"),
    GetTradeFlowsInfo("ITF_FLHQ_GetTradeFlowsInfo","XINJ_UNHQ_getTradeFlowsInfo","用户交易记录查询"),
    TcsGrpIntf("ITF_FLHT_TcsGrpIntf","XINJ_UNHT_proGrpTransApp","集团流量转赠接口"),           //旧版
    QueryResourcePool("ITF_FLHQ_QueryResourcePool","XINJ_UNHQ_queryECResourcePool","集团用户产品流量池信息查询"),     //旧版
    TcsGrpIntfNew("ITF_FLHT_TcsGrpIntf","XINJ_UNHT_operEcTransTraffic","新集团流量转赠接口"),            //新版
    QueryResourcePoolNew("ITF_FLHQ_QueryResourcePoolOpen","XINJ_UNHQ_qryECResPoolOpen","新集团用户产品流量池信息查询");     //新版
    
    private String code;
    
    private String httpCode;
    
    private String msg;
    
    private TransCode(String code, String httpCode, String msg) {
        this.code = code;
        this.httpCode = httpCode;
        this.msg = msg;
    }

    public String getCode() {
    	return code;
    }
    
    public void setCode(String code) {
    	this.code = code;
    }
    
    public String getMsg() {
    	return msg;
    }
    
    public void setMsg(String msg) {
    	this.msg = msg;
    }

    public String getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(String httpCode) {
        this.httpCode = httpCode;
    }
    
    
}
