package com.cmcc.vrp.boss;

/**
 * BOSS操作返回结果抽象类，各BOSS渠道可以根据需要拓展这个类
 * <p>
 * Created by sunyiwei on 2016/4/8.
 */
public abstract class AbstractBossOperationResultImpl implements BossOperationResult {
    protected String resultCode;
    protected String resultDesc;

    private String fingerPrint;

    private String systemNum;

    private Long entId;

    @Override
    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    @Override
    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    @Override
    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
}
