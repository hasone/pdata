/**
 * @Title: 	SendRespBossResult.java 
 * @Package com.cmcc.vrp.province.boss.xinjiang.response 
 * @author:	qihang
 * @date:	2016年5月6日 下午1:53:13 
 * @version	V1.0   
 */
package com.cmcc.vrp.boss.xinjiang.response;

import com.cmcc.vrp.boss.BossOperationResult;

/** 
 * @ClassName:	SendRespBossResult 
 * @Description:  赠送结果
 * @author:	qihang
 * @date:	2016年5月6日 下午1:53:13 
 *  
 */
public class SendRespBossResult implements BossOperationResult {
    
    SendResp sendResp;
    
    
    public SendRespBossResult(SendResp sendResp) {
    	this.sendResp = sendResp;
    }
    
    @Override
    public String getResultCode() {
    	return sendResp.getResultCode();
    
    }
    
    @Override
    public boolean isSuccess() {
    	if(sendResp.getResultCode().equals("0")){
    	    return true;
    	}
    	return false;
    }
    
    
    @Override
    public String getResultDesc() {
    	return sendResp.getResultInfo();
    }
    
    
    @Override
    public Object getOperationResult() {
    	return null;
    }
    
    /**
     * 初始化失败结果
     * @param resultCode
     * @param resultInfo
     * @return
     */
    public static SendRespBossResult initFailedObject(String resultCode,String resultInfo){
    	SendResp sendResp =new SendResp();
    	sendResp.setResultCode(resultCode);
    	sendResp.setResultInfo(resultInfo);
    	
    	SendRespBossResult result =new SendRespBossResult(sendResp);
    	return result;
    }
    
    @Override
    public boolean isAsync() {
        return false;
    }
    
    @Override
    public boolean isNeedQuery() {
        return false;
    }
    
    @Override
    public String getFingerPrint() {
        return null;
    }
    
    @Override
    public String getSystemNum() {
        return null;
    }
    
    @Override
    public Long getEntId() {
        return null;
    }

}
