package com.cmcc.vrp.queue.task.strategy;

import com.cmcc.vrp.charge.ChargeResult;
import com.cmcc.vrp.province.model.ChargeRecord;

/**
 * Info
 *
 */
public class Info {
    private ChargeRecord chargeRecord;
    private ChargeResult chargeResult;

    public Info(ChargeRecord chargeRecord, ChargeResult chargeResult) {
        this.chargeRecord = chargeRecord;
        this.chargeResult = chargeResult;
    }

    public ChargeRecord getChargeRecord() {
        return chargeRecord;
    }

    
    public void setChargeRecord(ChargeRecord chargeRecord) {
        this.chargeRecord = chargeRecord;
    }

    public void setChargeResult(ChargeResult chargeResult) {
        this.chargeResult = chargeResult;
    }

    public ChargeResult getChargeResult() {
        return chargeResult;
    }
}
