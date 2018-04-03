package com.cmcc.vrp.province.module;

/**
 * 企业效益统计模型
 *
 * @author JamieWu
 */
public class EnterpriseBenefitModule {
    private double benefit;

    private Long enterId;

    public double getBenefit() {
        return benefit;
    }

    public void setBenefit(double benefit) {
        this.benefit = benefit;
    }

    public Long getEnterId() {
        return enterId;
    }

    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }


}
