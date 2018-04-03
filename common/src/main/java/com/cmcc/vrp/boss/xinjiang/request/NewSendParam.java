package com.cmcc.vrp.boss.xinjiang.request;

/**
 * 新版boss赠送流量需要的参数
 *
 */
public class NewSendParam implements ServiceBasicParam {
    private String groupId; // 企业Id 用户提供

    private String serialNumber; // 手机号

    private String infoValue; // 流量个数

    private String oprNumb; // 充值流水号

    public NewSendParam(String groupId, String serialNumber, String infoValue,
            String oprNumb) {
        super();
        this.groupId = groupId;
        this.serialNumber = serialNumber;
        this.infoValue = infoValue;
        this.oprNumb = oprNumb;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getInfoValue() {
        return infoValue;
    }

    public void setInfoValue(String infoValue) {
        this.infoValue = infoValue;
    }

    public String getOprNumb() {
        return oprNumb;
    }

    public void setOprNumb(String oprNumb) {
        this.oprNumb = oprNumb;
    }

    /**
     * 转化为packet
     */
    public String toPacket() {
        return "{X_SUBTRANS_CODE=[\"ProcessGroupTransTrafficOpen\"], "
                + "GROUP_ID=[\""
                + groupId
                + "\"], "
                + "OPR_NUMB=[\""
                + oprNumb
                + "\"], "
                + "SERIAL_NUMBER=[\""
                + serialNumber
                + "\"], "
                + "INFO_VALUE=[\""
                + infoValue
                + "\"], "
                + "TRADE_STAFF_ID=[\"SUPERUSR\"], TRADE_DEPART_ID=[\"0000\"], TRADE_CITY_CODE=[\"0000\"]}";
    }

}
