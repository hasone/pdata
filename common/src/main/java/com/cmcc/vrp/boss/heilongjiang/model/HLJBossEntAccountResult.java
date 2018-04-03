package com.cmcc.vrp.boss.heilongjiang.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.cmcc.vrp.boss.BossOperationResult;

/**
 * 
 * @ClassName: HLJBossEntAccountResult 
 * @Description: 黑龙江集团可赠送流量查询接口结果封装类
 * @author: Rowe
 * @date: 2017年8月28日 下午5:02:42
 */
public class HLJBossEntAccountResult implements BossOperationResult{

    private static String QUERY_SUCCESS_CODE = "0000000"; //查询成功标识
    
    @JSONField(name="code")
    private String code;
    
    @JSONField(name="message")
    private String message;
    
    @JSONField(name="promptMsg")
    private String promptMsg;
    
    @JSONField(name="GOODS_INFO_LIST")
    private List<HLJBossProduct> goodsInfoList;
    
    @Override
    public String getResultCode() {
        return code;
    }

    @Override
    public boolean isSuccess() {
        if(StringUtils.isNotBlank(code) && QUERY_SUCCESS_CODE.equalsIgnoreCase(code)){
            return true;
        }
        return false;
    }

    @Override
    public boolean isAsync() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getResultDesc() {
        return promptMsg;
    }

    @Override
    public Object getOperationResult() {
        return goodsInfoList;
    }

    @Override
    public boolean isNeedQuery() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getFingerPrint() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSystemNum() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getEntId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPromptMsg() {
        return promptMsg;
    }

    public void setPromptMsg(String promptMsg) {
        this.promptMsg = promptMsg;
    }

    public List<HLJBossProduct> getGoodsInfoList() {
        return goodsInfoList;
    }

    public void setGoodsInfoList(List<HLJBossProduct> goodsInfoList) {
        this.goodsInfoList = goodsInfoList;
    }


}
