package com.cmcc.vrp.boss.shanghai.model.querycount;

import com.cmcc.vrp.boss.shanghai.model.common.InMap;

/**
 * @author lgk8023
 *
 */
public class QueryUsableBalanceOfFlowInMap implements InMap {

    private String acctId;
    
    private String mainBillId;
    
    private String offerId;

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getMainBillId() {
        return mainBillId;
    }

    public void setMainBillId(String mainBillId) {
        this.mainBillId = mainBillId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    
}
