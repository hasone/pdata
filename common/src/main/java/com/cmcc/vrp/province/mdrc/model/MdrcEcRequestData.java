/**
 * @Title: MdrcEcRequestData.java
 * @Package com.cmcc.vrp.province.mdrc.model
 * @author: qihang
 * @date: 2016年5月26日 下午4:47:57
 * @version V1.0
 */
package com.cmcc.vrp.province.mdrc.model;

import com.cmcc.vrp.province.mdrc.enums.MdrcEcOperationEnum;
import com.cmcc.vrp.province.mdrc.enums.MdrcEcOperationEnumConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * @ClassName: MdrcEcRequestData
 * @Description: 营销卡ec接口与后台关联使用的请求
 * @author: qihang
 * @date: 2016年5月26日 下午4:47:57
 */
public class MdrcEcRequestData {
    /**
     * 操作类型
     */
    @XStreamAlias("Operate")
    @XStreamConverter(value = MdrcEcOperationEnumConverter.class)
    private MdrcEcOperationEnum operateType;

    /**
     * 入库，激活，退卡，延期，锁定，解锁，销卡使用
     * <p>
     * 换卡时作为原始卡号
     */
    @XStreamAlias("CardNum")
    private String originCardInfo;

    /**
     * 目标卡号，换卡操作使用，其余传空
     */
    @XStreamAlias("ExchangeCards")
    private String destCardInfo;

    /**
     * 用于延期操作，其余传空
     * <p>
     * 待变更日期：必需晚于操作提交时间，必需晚于原过期日期，操作命令必须为Extend；
     */
    @XStreamAlias("ExpireDate")
    private String expireDate;

    /**
     * 用于激活操作，企业在BOSS侧的编码
     */
    @XStreamAlias("GroupId")
    private String groupId;

    /**
     * 用于激活操作，产品编码
     */
    @XStreamAlias("ProductCode")
    private String productCode;

    public MdrcEcOperationEnum getOperateType() {
        return operateType;
    }

    public void setOperateType(MdrcEcOperationEnum operateType) {
        this.operateType = operateType;
    }

    public String getOriginCardInfo() {
        return originCardInfo;
    }

    public void setOriginCardInfo(String originCardInfo) {
        this.originCardInfo = originCardInfo;
    }

    public String getDestCardInfo() {
        return destCardInfo;
    }

    public void setDestCardInfo(String destCardInfo) {
        this.destCardInfo = destCardInfo;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
