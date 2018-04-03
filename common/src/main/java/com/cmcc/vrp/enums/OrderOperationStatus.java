package com.cmcc.vrp.enums;


/**
 * 
 * @ClassName: OrderOperationStatus 
 * @Description: 山东订单推送接口操作结果
 * @author: Rowe
 * @date: 2017年5月26日 下午1:45:05
 */
public enum OrderOperationStatus {

    NORMAL(0, "NORMAL"), //正常状态：订购操作、变更操作、恢复操作处理结果
    DELETE(1, "DELETE"), //删除状态：删除操作结果
    PAUSE(2, "PAUSE");//暂停状态：暂停操作结果

    private OrderOperationStatus(Integer opStatus, String opStatusDesc) {
        this.opStatus = opStatus;
        this.opStatusDesc = opStatusDesc;
    }

    private Integer opStatus;

    private String opStatusDesc;

    /**
     * 
     * @Title: getOpStatus 
     * @Description: TODO
     * @return
     * @return: Integer
     */
    public Integer getOpStatus() {
        return opStatus;
    }

    /**
     * 
     * @Title: setOpStatus 
     * @Description: TODO
     * @param opStatus
     * @return: void
     */
    public void setOpStatus(Integer opStatus) {
        this.opStatus = opStatus;
    }

    /**
     * 
     * @Title: getOpStatusDesc 
     * @Description: TODO
     * @return
     * @return: String
     */
    public String getOpStatusDesc() {
        return opStatusDesc;
    }

    /**
     * 
     * @Title: setOpStatusDesc 
     * @Description: TODO
     * @param opStatusDesc
     * @return: void
     */
    public void setOpStatusDesc(String opStatusDesc) {
        this.opStatusDesc = opStatusDesc;
    }
    
    /**
     * 
     * @Title: getOpStatus 
     * @Description: 根据状态码获取
     * @param opStatus
     * @return
     * @return: OrderOperationStatus
     */
    public static OrderOperationStatus getOpStatus(Integer opStatus) {
        if (opStatus == null) {
            return null;
        }
        for (OrderOperationStatus item : OrderOperationStatus.values()) {
            if (item.getOpStatus().equals(opStatus)) {
                return item;
            }
        }
        return null;
    }

}
