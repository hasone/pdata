package com.cmcc.vrp.boss.zhuowang.bean;

import java.io.Serializable;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午11:11:42
*/
public class OrderFailInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -706875337853792055L;

    /**
     * 订购失败的手机号码
     */
    private String mobNum;

    /**
     * 失败编码
     * 01	用户不是指定订购的有效成员
     * 02	用户套餐枚举值错误
     * 03	省公司开通叠加包失败
     * 99	其他错误
     */
    private String errorCode;

    /**
     * 失败详细信息
     * 当Rsp=03/99时必填
     */
    private String errorDesc;

    public String getMobNum() {
        return mobNum;
    }

    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }


}
