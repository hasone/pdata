package com.cmcc.vrp.boss.hunan.model;

/**
 * @ClassName: HNBOSSAccount
 * @Description: 湖南省BOSS集团产品余额信息封装类
 * @author: Rowe
 * @date: 2016年4月7日 下午4:56:54
 */
public class HNBOSSAccount {

    private String IN_MODE_CODE;

    private String X_RESULTCODE;

    private String X_RESULTCOUNT;

    private String X_RESULTINFO;

    private String X_RESULTSIZE;

    private String X_RSPCODE;

    private String X_RSPDESC;

    private String X_RSPTYPE;

    private String GROUP_ID;//集团ID

    private String GROUP_NAME;//集团名称

    private String GROUP_ADDR;//集团地址

    private String GROUP_MEMO;//公司简介

    private String ACCT_FEE;//账户余额

    public String getGROUP_ID() {
        return GROUP_ID;
    }

    public void setGROUP_ID(String gROUP_ID) {
        GROUP_ID = gROUP_ID;
    }

    public String getGROUP_NAME() {
        return GROUP_NAME;
    }

    public void setGROUP_NAME(String gROUP_NAME) {
        GROUP_NAME = gROUP_NAME;
    }

    public String getGROUP_ADDR() {
        return GROUP_ADDR;
    }

    public void setGROUP_ADDR(String gROUP_ADDR) {
        GROUP_ADDR = gROUP_ADDR;
    }

    public String getGROUP_MEMO() {
        return GROUP_MEMO;
    }

    public void setGROUP_MEMO(String gROUP_MEMO) {
        GROUP_MEMO = gROUP_MEMO;
    }

    public String getACCT_FEE() {
        return ACCT_FEE;
    }

    public void setACCT_FEE(String aCCT_FEE) {
        ACCT_FEE = aCCT_FEE;
    }

    public String getIN_MODE_CODE() {
        return IN_MODE_CODE;
    }

    public void setIN_MODE_CODE(String iN_MODE_CODE) {
        IN_MODE_CODE = iN_MODE_CODE;
    }

    public String getX_RESULTCODE() {
        return X_RESULTCODE;
    }

    public void setX_RESULTCODE(String x_RESULTCODE) {
        X_RESULTCODE = x_RESULTCODE;
    }

    public String getX_RESULTCOUNT() {
        return X_RESULTCOUNT;
    }

    public void setX_RESULTCOUNT(String x_RESULTCOUNT) {
        X_RESULTCOUNT = x_RESULTCOUNT;
    }

    public String getX_RESULTINFO() {
        return X_RESULTINFO;
    }

    public void setX_RESULTINFO(String x_RESULTINFO) {
        X_RESULTINFO = x_RESULTINFO;
    }

    public String getX_RESULTSIZE() {
        return X_RESULTSIZE;
    }

    public void setX_RESULTSIZE(String x_RESULTSIZE) {
        X_RESULTSIZE = x_RESULTSIZE;
    }

    public String getX_RSPCODE() {
        return X_RSPCODE;
    }

    public void setX_RSPCODE(String x_RSPCODE) {
        X_RSPCODE = x_RSPCODE;
    }

    public String getX_RSPDESC() {
        return X_RSPDESC;
    }

    public void setX_RSPDESC(String x_RSPDESC) {
        X_RSPDESC = x_RSPDESC;
    }

    public String getX_RSPTYPE() {
        return X_RSPTYPE;
    }

    public void setX_RSPTYPE(String x_RSPTYPE) {
        X_RSPTYPE = x_RSPTYPE;
    }

}
