package com.cmcc.vrp.province.quartz.jobs;

import java.io.Serializable;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午4:05:29
*/
public class EnterpriseInterfaceExpireJobPojo implements Serializable{
    private String date;//倒数几天
    
    private Long entId;

    public EnterpriseInterfaceExpireJobPojo() {
    }

    public EnterpriseInterfaceExpireJobPojo(String date, Long entId) {
        this.date = date;
        this.entId = entId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }
}
