package com.cmcc.vrp.queue.task.strategy;

import com.cmcc.vrp.province.model.ChargeRecord;
import com.cmcc.vrp.province.model.SerialNum;

/**
 * ChargeRecordSerialNum
 *
 */
public class ChargeRecordSerialNum {
    private ChargeRecord chargeRecord;
    private SerialNum serialNum;
    
    public ChargeRecordSerialNum(ChargeRecord chargeRecord, SerialNum serialNum) {
        this.chargeRecord = chargeRecord;
        this.serialNum = serialNum;
    }
    
    public ChargeRecord getChargeRecord() {
        return chargeRecord;
    }
    public void setChargeRecord(ChargeRecord chargeRecord) {
        this.chargeRecord = chargeRecord;
    }
    public SerialNum getSerialNum() {
        return serialNum;
    }
    public void setSerialNum(SerialNum serialNum) {
        this.serialNum = serialNum;
    }
    
    
}
