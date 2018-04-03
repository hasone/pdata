package com.cmcc.vrp.boss.neimenggu;

import com.cmcc.vrp.boss.AbstractBossOperationResultImpl;
import com.cmcc.vrp.boss.neimenggu.model.send.ResponseSendData;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 上午10:59:11
*/
public class NMBossOperationResultImpl extends AbstractBossOperationResultImpl {

    private String outSysSn;//流水号

    private String bossSn;//BOSS订购流水

    private String effDateTime;//订购产品生效时间

    private String expDateTime;//订购产品失效时间

    public NMBossOperationResultImpl(ResponseSendData datas) {
        this.resultCode = datas.getResultCode();
        this.resultDesc = datas.getResultMsg();
        this.outSysSn = datas.getOutSysSn();
        this.bossSn = datas.getBossSn();
        this.effDateTime = datas.getEffDateTime();
        this.expDateTime = datas.getExpDateTime();
    }

    @Override
    public boolean isSuccess() {
        return resultCode.equals("0");
    }

    @Override
    public Object getOperationResult() {
        // TODO Auto-generated method stub
        return this;
    }

    public String getOutSysSn() {
        return outSysSn;
    }

    public void setOutSysSn(String outSysSn) {
        this.outSysSn = outSysSn;
    }

    public String getBossSn() {
        return bossSn;
    }

    public void setBossSn(String bossSn) {
        this.bossSn = bossSn;
    }

    public String getEffDateTime() {
        return effDateTime;
    }

    public void setEffDateTime(String effDateTime) {
        this.effDateTime = effDateTime;
    }

    public String getExpDateTime() {
        return expDateTime;
    }

    public void setExpDateTime(String expDateTime) {
        this.expDateTime = expDateTime;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean isNeedQuery() {
        return false;
    }
}
