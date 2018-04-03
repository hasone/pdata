/**
 * @Title: BossConnectionType.java
 * @Package com.cmcc.vrp.province.enums
 * @author: qihang
 * @date: 2015年4月29日 下午1:56:28
 * @version V1.0
 */
package com.cmcc.vrp.enums;

/**
 * @ClassName: BossConnectionType
 * @Description: TODO
 * @author: qihang
 * @date: 2015年4月29日 下午1:56:28
 *
 */
public enum BossConnectionType {
    SINGLE_SEND(1, "100251", "SingleSendService"),//赠送个人业务
    ENTERPRO_QUERY(2, "100249", "EnterProQuery"),//赠送业务查询
    ENTERSEND_QUERY(3, "100250", "EnterSendQuery"),//赠送业务明细查询
    PATCH_SEND(4, "100246", "PatchSendService"),   //批量赠送业务
    PATCHSEND_TELQUERY(5, "100247", "PatchSendTelQuery"), //批量赠送业务查询
    PATCHSEND_RESULTQUERY(6, "100248", "PatchSendResultQuery"),//批量赠送处理结果查询
    ENTERPRO_TELQUERY(7, "200004", "EnterProWithTelQuery"),//根据集团编号查询其下的产品及产品用户
    ENTERORDER_QUERY(8, "200272", "EnterOrderQuery"),//根据集团产品用户id查询其下所订购的在用的流量产品，返回产品用户所订购的在用的在CS_GRPBATCHGIVEPRESENT_CONF表里的产品
    CONTACTENTER_QUERY(9, "200001", "ContactEnterQuery");//根据关键人联系人、密码取得集团编号和集团名称


    private int code;

    private String message;

    private String connectionName;

    BossConnectionType(int code, String message, String connectionName) {
        this.code = code;
        this.message = message;
        this.connectionName = connectionName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }


}
