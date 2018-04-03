/**
 * @Title: 	SendParam.java 
 * @Package com.cmcc.xinjiang.boss.model.request 
 * @author:	qihang
 * @date:	2016年3月30日 下午6:53:35 
 * @version	V1.0   
 */
package com.cmcc.vrp.boss.xinjiang.request;

/** 
 * @ClassName:	SendParam 
 * @Description:  旧版赠送流量的基本参数
 * @author:	qihang
 * @date:	2016年3月30日 下午6:53:35 
 *  
 */
public class SendParam implements ServiceBasicParam {

    // {X_SUBTRANS_CODE=[\"ProcessGroupTransTrafficApp\"],
    // GROUP_ID=[\"9919000065\"], USER_ID=[\"9016012501900494\"],
    // OPR_NUMB=[\"11111111111111111\"], SERIAL_NUMBER=[\"13659948687\"],
    // INFO_VALUE=[\"10\"], TRADE_STAFF_ID=[\"SUPERUSR\"],
    // TRADE_DEPART_ID=[\"0000\"], TRADE_CITY_CODE=[\"0000\"]};
    
    private String groupId; // 企业Id 用户提供

    private String userId; // 企业UserId，5.6. 集团用户产品流量池信息查询得到（QueryResourcePool）

    private String oprNumb; // 充值流水号

    private String serialNumber; // 手机号

    private String infoValue; // 流量个数

    public SendParam(String groupId, String userId, String oprNumb,
            String serialNumber, String infoValue) {

        this.groupId = groupId;
        this.userId = userId;
        this.oprNumb = oprNumb;
        this.serialNumber = serialNumber;
        this.infoValue = infoValue;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOprNumb() {
        return oprNumb;
    }

    public void setOprNumb(String oprNumb) {
        this.oprNumb = oprNumb;
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

    /**
     * 转化为packet
     */
    public String toPacket() {
        return "{X_SUBTRANS_CODE=[\"ProcessGroupTransTrafficApp\"], "
                + "GROUP_ID=[\""
                + groupId
                + "\"], "
                + "USER_ID=[\""
                + userId
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
