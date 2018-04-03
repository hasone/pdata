/**
 *
 */
package com.cmcc.vrp.province.model;

import java.util.Date;

/**
 * <p>
 * Title:EntFlowControlRecord
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author xujue
 * @date 2016年9月21日
 */
public class EntFlowControlRecord {
    private Long id;

    private Long enterId;

    private Long count;

    private Integer type;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnterId() {
        return enterId;
    }

    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
