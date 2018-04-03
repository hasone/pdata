package com.cmcc.vrp.boss.shanghai.model.paymember;

import com.cmcc.vrp.boss.shanghai.model.common.ProdAttrMap;

/**
 * Created by leelyn on 2016/7/12.
 */
public class PayMemberProdAttrMap implements ProdAttrMap {
    private String srvPackage;
    private String limitCharge;
    private String memberSps;
    private String memberEffDate;
    private String accPeriodEffRule;
    private String giveValid;

    public String getGiveValid() {
        return giveValid;
    }

    public void setGiveValid(String giveValid) {
        this.giveValid = giveValid;
    }

    public String getSrvPackage() {
        return srvPackage;
    }

    public void setSrvPackage(String srvPackage) {
        this.srvPackage = srvPackage;
    }

    public String getLimitCharge() {
        return limitCharge;
    }

    public void setLimitCharge(String limitCharge) {
        this.limitCharge = limitCharge;
    }

    public String getMemberSps() {
        return memberSps;
    }

    public void setMemberSps(String memberSps) {
        this.memberSps = memberSps;
    }

    public String getMemberEffDate() {
        return memberEffDate;
    }

    public void setMemberEffDate(String memberEffDate) {
        this.memberEffDate = memberEffDate;
    }

    public String getAccPeriodEffRule() {
        return accPeriodEffRule;
    }

    public void setAccPeriodEffRule(String accPeriodEffRule) {
        this.accPeriodEffRule = accPeriodEffRule;
    }
}
