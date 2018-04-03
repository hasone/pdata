/**
 * @Title: StatisticModule.java
 * @Package com.cmcc.vrp.province.module
 * @author: qihang
 * @date: 2015年7月23日 下午4:47:32
 * @version V1.0
 */
package com.cmcc.vrp.province.module;

/**
 * @ClassName: StatisticModule
 * @Description: TODO
 * @author: qihang
 * @date: 2015年7月23日 下午4:47:32
 *
 */
public class StatisticModule {
    private String enterName;

    private Long presentCount;

    private Long redpacketCount;

    private Long monthlyCount;

    public String getEnterName() {
        return enterName;
    }

    public void setEnterName(String enterName) {
        this.enterName = enterName;
    }

    public Long getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(Long presentCount) {
        this.presentCount = presentCount;
    }

    public Long getRedpacketCount() {
        return redpacketCount;
    }

    public void setRedpacketCount(Long redpacketCount) {
        this.redpacketCount = redpacketCount;
    }

    public Long getMonthlyCount() {
        return monthlyCount;
    }

    public void setMonthlyCount(Long monthlyCount) {
        this.monthlyCount = monthlyCount;
    }


}
