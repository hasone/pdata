package com.cmcc.vrp.queue.pojo;

import com.cmcc.vrp.province.model.IndividualAccountRecord;
import com.cmcc.vrp.province.model.IndividualFlowcoinExchange;

/**
 * <p>Title:FlowcoinExchangePojo </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年11月28日
 */
public class FlowcoinExchangePojo {
    private IndividualAccountRecord IndividualAccountRecord;

    private IndividualFlowcoinExchange individualFlowcoinExchangeRecord;

    public IndividualAccountRecord getIndividualAccountRecord() {
        return IndividualAccountRecord;
    }

    public void setIndividualAccountRecord(IndividualAccountRecord individualAccountRecord) {
        IndividualAccountRecord = individualAccountRecord;
    }

    public IndividualFlowcoinExchange getIndividualFlowcoinExchangeRecord() {
        return individualFlowcoinExchangeRecord;
    }

    public void setIndividualFlowcoinExchangeRecord(
        IndividualFlowcoinExchange individualFlowcoinExchangeRecord) {
        this.individualFlowcoinExchangeRecord = individualFlowcoinExchangeRecord;
    }
}
