package com.cmcc.vrp.province.model;

/**
 * 用户企业关系类
 * */
public class AdminEnter {
    private Long adminId;

    private Long enterId;

    private String eName;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getEnterId() {
        return enterId;
    }

    public void setEnterId(Long enterId) {
        this.enterId = enterId;
    }

    /**
     * @return
     */
    public String geteName() {
        return eName;
    }

    /**
     * @param eName
     */
    public void seteName(String eName) {
        this.eName = eName;
    }

}