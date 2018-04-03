package com.cmcc.vrp.province.activity.model;

/**
 * -------------------
 * 活动时间
 * -------------------
 * startTime    活动开始时间
 * endTime      活动结束时间
 *
 * @Author qinqinyan
 * @Date 16/10/28
 */
public class AutoTimePojo {
    /*private Date startTime;
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }*/
    private String startTime;

    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
