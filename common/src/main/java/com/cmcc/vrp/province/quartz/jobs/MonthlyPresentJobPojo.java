package com.cmcc.vrp.province.quartz.jobs;

import java.io.Serializable;

/**
 * 包月赠送
 * @author lgk8023
 *
 */
public class MonthlyPresentJobPojo implements Serializable {

    private static final long serialVersionUID = -4770893777156521085L;
    private Long ruleId;
    
    private Integer count; //定时任务次数

    public MonthlyPresentJobPojo() {
    }

    public MonthlyPresentJobPojo(Long ruleId, Integer count) {        
        this.ruleId = ruleId;
        this.count = count;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
