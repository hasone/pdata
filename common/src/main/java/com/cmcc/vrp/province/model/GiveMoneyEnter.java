package com.cmcc.vrp.province.model;

import java.util.Date;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:48:28
*/
public class GiveMoneyEnter {
    private Long enterId;
    private Long giveMoneyId;
    private Date createTime;
    private Date updateTime;

    //extend
    private String giveMoneyName;


    public String getGiveMoneyName() {
        return giveMoneyName;
    }

    public void setGiveMoneyName(String giveMoneyName) {
        this.giveMoneyName = giveMoneyName;
    }

    public Long getEnterId() {
        return enterId;
    }

    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }

    public Long getGiveMoneyId() {
        return giveMoneyId;
    }

    public void setGiveMoneyId(Long giveMoneyId) {
        this.giveMoneyId = giveMoneyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
