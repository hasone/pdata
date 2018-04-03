package com.cmcc.vrp.province.module;

import com.cmcc.vrp.province.model.MonthlyPresentRule;
import org.springframework.stereotype.Service;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午3:44:54
*/
@Service("monthlyPresentRuleModule")
public class MonthlyPresentRuleModule extends MonthlyPresentRule {

    private String entName;

    private String entCode;

    private String prdName;

    private String prdCode;

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getPrdName() {
        return prdName;
    }

    public void setPrdName(String prdName) {
        this.prdName = prdName;
    }

    public String getEntCode() {
        return entCode;
    }

    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

}