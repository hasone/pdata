package com.cmcc.vrp.province.model;

/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午2:47:26
*/
public class EnterprisesCount {

    private String createTime;//时间点

    private Integer enterCount;//新增企业数量

    private Integer totalCount;//累计新增企业数量

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getEnterCount() {
        return enterCount;
    }

    public void setEnterCount(Integer enterCount) {
        this.enterCount = enterCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }


}
