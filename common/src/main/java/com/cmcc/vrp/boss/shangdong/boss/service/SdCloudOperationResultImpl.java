package com.cmcc.vrp.boss.shangdong.boss.service;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;

/**
 * Created by qihang on 2016/11/09.
 */
public class SdCloudOperationResultImpl extends AbstractBossOperationResultImpl {
    
    /**
     * 默认正确编码
     */
    private static final String SUCCESS_CODE = "200";
    
    /**
     * 默认http错误编码
     */
    private static final String ERR_CODE = "500";
    
    /**
     * 默认http错误信息
     */
    private static final String HTTP_ERR_MSG = "上游平台维护中";
    
    /**
     * 默认充值参数配置错误
     */
    private static final String PARAM_ERR_MSG = "充值参数配置错误";
    
    /**
     * 默认充值参数配置错误
     */
    private static final String QUERY_ERR_MSG = "查询返回信息错误";
    
    private boolean isAsyc =false;
    
    
    public SdCloudOperationResultImpl(String code,String message,boolean isAsyc){
        this.resultCode = code;
        this.resultDesc = message;
        this.isAsyc = isAsyc;
    }
    
    /**
     * 
     * 默认的错误信息，resultCode置为500，resultDesc为上游平台维护中
     * @return SdCloudOperationResultImpl
     */
    public static SdCloudOperationResultImpl initHttpErrResult(boolean isAsyc){
        return new SdCloudOperationResultImpl(ERR_CODE,HTTP_ERR_MSG,isAsyc);
    }
    
    /**
     * 
     * 默认的错误信息，resultCode置为500，resultDesc为参数配置错误
     * @return SdCloudOperationResultImpl
     */
    public static SdCloudOperationResultImpl initParamErrResult(boolean isAsyc){
        return new SdCloudOperationResultImpl(ERR_CODE,PARAM_ERR_MSG,isAsyc);
    }
    
    /**
     * 
     * 默认的错误信息，resultCode置为500，resultDesc为参数配置错误
     * @return SdCloudOperationResultImpl
     */
    public static SdCloudOperationResultImpl initQueryErrResult(boolean isAsyc){
        return new SdCloudOperationResultImpl(ERR_CODE,QUERY_ERR_MSG,isAsyc);
    }
    
    @Override
    public boolean isSuccess() {
        if (this.resultCode != null) {
            return this.resultCode.equals(SUCCESS_CODE);
        }
        return false;
    }
    
    @Override
    public boolean isAsync() {
        return isAsyc;
    }
    
    @Override
    public Object getOperationResult() {
        return null;
    }
    
    @Override
    public boolean isNeedQuery() {
        return false;
    }
}
