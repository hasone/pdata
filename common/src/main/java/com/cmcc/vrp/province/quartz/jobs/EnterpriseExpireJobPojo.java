package com.cmcc.vrp.province.quartz.jobs;

import java.io.Serializable;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午4:04:31
*/
public class EnterpriseExpireJobPojo implements Serializable {

    private static final long serialVersionUID = 276261984834630857L;

    private Long entId;//企业ID

    public EnterpriseExpireJobPojo() {
    }

    public EnterpriseExpireJobPojo(Long entId) {
        this.entId = entId;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

}
