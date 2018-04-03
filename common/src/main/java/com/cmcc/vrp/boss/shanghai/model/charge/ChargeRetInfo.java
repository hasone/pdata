package com.cmcc.vrp.boss.shanghai.model.charge;

import com.cmcc.vrp.boss.shanghai.model.paymember.SoapDataContainer;

/**
 * @author lgk8023
 *
 */
public class ChargeRetInfo {
    private SoapDataContainer soapDataContainer;

    private ChargeReturnContent returnMap;

    public SoapDataContainer getSoapDataContainer() {
        return soapDataContainer;
    }

    public void setSoapDataContainer(SoapDataContainer soapDataContainer) {
        this.soapDataContainer = soapDataContainer;
    }

    public ChargeReturnContent getReturnMap() {
        return returnMap;
    }

    public void setReturnMap(ChargeReturnContent returnMap) {
        this.returnMap = returnMap;
    }

}
