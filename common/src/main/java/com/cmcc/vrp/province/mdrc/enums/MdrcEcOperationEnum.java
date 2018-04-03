/**
 * @Title: MdrcEcOperationEnum.java
 * @Package com.cmcc.vrp.province.mdrc.model
 * @author: qihang
 * @date: 2016年5月26日 下午4:49:14
 * @version V1.0
 */
package com.cmcc.vrp.province.mdrc.enums;

/**
 * @ClassName: MdrcEcOperationEnum
 * @Description: EC操作对象enum
 * @author: qihang
 * @date: 2016年5月26日 下午4:49:14
 */
public enum MdrcEcOperationEnum {

	/*变更操作
    Stock In：入库，前置要求，当前状态为New，变更后状态为Free；
	Activate：激活，前置要求，当前状态为Free，变更后状态为Activated；
	Deactivate：去激活，前置要求，当前状态为Activated，变更后状态为Free；
	Delete：注销删除，无前置要求，变更后状态为Deleted，无法变更为其他任何状态；
	Lock：锁定，前置要求，当前状态为Unlocked，变更后状态为Locked；
	Unlock：解锁，前置要求，当前状态为Locked，变更后状态为Unlocked；
	Extend：延期，变更后状态保持不变，有效期更新至新时间。*/

    STOCKIN("Stock In", "入库"),
    ACTIVATE("Activate", "激活"),
    DEACTIVATE("Deactivate", "去激活"),
    DELETE("Delete", "注销删除"),
    LOCK("Lock", "锁定"),
    UNLOCK("Unlock", "解锁"),
    EXTEND("Extend", "延期");

    String operCode;

    String operMessage;

    private MdrcEcOperationEnum(String operCode, String operMessage) {
        this.operCode = operCode;
        this.operMessage = operMessage;
    }

    /**
     * @param code
     * @return
     */
    public static MdrcEcOperationEnum fromCode(String code) {
        for (MdrcEcOperationEnum mdrcEcOperationEnum : MdrcEcOperationEnum.values()) {
            if (mdrcEcOperationEnum.getOperCode().equalsIgnoreCase(code)) {
                return mdrcEcOperationEnum;
            }
        }

        return null;
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String operCode) {
        this.operCode = operCode;
    }

    public String getOperMessage() {
        return operMessage;
    }

    public void setOperMessage(String operMessage) {
        this.operMessage = operMessage;
    }
}
