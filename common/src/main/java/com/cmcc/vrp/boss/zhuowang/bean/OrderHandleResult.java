package com.cmcc.vrp.boss.zhuowang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 流量订购处理结果(回调返回的结果)
 * 
 * @author qinpo
 *
 */
public class OrderHandleResult implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6501051373925709196L;

    /**
     * BBOSS流水号
     */
    private String operSeq;

    /**
     * 处理成功的记录总数
     */
    private int succNum;

    /**
     * 处理成功的手机号码
     */
    private List<String> succMobNum;

    /**
     * 处理失败的记录总数
     */
    private int failNum;
    /**
     * 处理失败的记录
     */
    private List<OrderFailInfo> failInfos;

    public String getOperSeq() {
        return operSeq;
    }

    public void setOperSeq(String operSeq) {
        this.operSeq = operSeq;
    }

    public int getSuccNum() {
        return succNum;
    }

    public void setSuccNum(int succNum) {
        this.succNum = succNum;
    }

    public List<String> getSuccMobNum() {
        return succMobNum;
    }

    public void setSuccMobNum(List<String> succMobNum) {
        this.succMobNum = succMobNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }

    public List<OrderFailInfo> getFailInfos() {
        return failInfos;
    }

    public void setFailInfos(List<OrderFailInfo> failInfos) {
        this.failInfos = failInfos;
    }

}
