package com.cmcc.vrp.province.model;
/**123
 * 
 * 用户修改保存
 *
 */
public class AdminChangeOperator {
    private Long id;

    private Long enterId;

    private Long adminId;

    private String destName;

    private String destPhone;

    private String comment;

    private Integer deleteFlag;

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

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName == null ? null : destName.trim();
    }

    public String getDestPhone() {
        return destPhone;
    }

    public void setDestPhone(String destPhone) {
        this.destPhone = destPhone == null ? null : destPhone.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}