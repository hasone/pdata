/**
 * @Title: 	GroupInfoResp.java 
 * @Package com.cmcc.xinjiang.boss.model.response 
 * @author:	qihang
 * @date:	2016年3月30日 下午2:22:01 
 * @version	V1.0   
 */
package com.cmcc.vrp.boss.xinjiang.response;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: GroupInfoResp
 * @Description: 查询企业类返回对象
 * @author: qihang
 * @date: 2016年3月30日 下午2:22:01
 * 
 *        resultCode为0时返回正确，会有所有信息 resultCode非0时，会有boss返回的错误信息resultInfo
 * 
 */
public class GroupInfoResp {

    private String resultCode;// 状态码，只有是0时才会有后续信息

    private String resultInfo;// 状态信息

    private String custType;// 客户类型

    private String custState;// 客户状态

    private String psptTypeCode;// 证件类别

    private String psptId;// 证件号码

    private String custPasswd;// 客户密码

    private String openLimit;// 限制开户数

    private String developStaffId;// 发展员工

    private String developDepartId;// 发展渠道

    private String inDate;// 建档时间

    private String inStaffId;// 建档员工

    private String inDepartId;// 建档渠道

    private String scoreValue;// 客户积分

    private String creditClass;// 信用等级

    private String basicCreditValue;// 基本信用度

    private String creditValue;// 信用度

    private String custId;// 客户标识

    private String groupId;// 集团编码

    private String custName;// 集团名称

    private String groupType;// 集团类型

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getCustState() {
        return custState;
    }

    public void setCustState(String custState) {
        this.custState = custState;
    }

    public String getPsptTypeCode() {
        return psptTypeCode;
    }

    public void setPsptTypeCode(String psptTypeCode) {
        this.psptTypeCode = psptTypeCode;
    }

    public String getPsptId() {
        return psptId;
    }

    public void setPsptId(String psptId) {
        this.psptId = psptId;
    }

    public String getCustPasswd() {
        return custPasswd;
    }

    public void setCustPasswd(String custPasswd) {
        this.custPasswd = custPasswd;
    }

    public String getOpenLimit() {
        return openLimit;
    }

    public void setOpenLimit(String openLimit) {
        this.openLimit = openLimit;
    }

    public String getDevelopStaffId() {
        return developStaffId;
    }

    public void setDevelopStaffId(String developStaffId) {
        this.developStaffId = developStaffId;
    }

    public String getDevelopDepartId() {
        return developDepartId;
    }

    public void setDevelopDepartId(String developDepartId) {
        this.developDepartId = developDepartId;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getInStaffId() {
        return inStaffId;
    }

    public void setInStaffId(String inStaffId) {
        this.inStaffId = inStaffId;
    }

    public String getInDepartId() {
        return inDepartId;
    }

    public void setInDepartId(String inDepartId) {
        this.inDepartId = inDepartId;
    }

    public String getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(String scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getCreditClass() {
        return creditClass;
    }

    public void setCreditClass(String creditClass) {
        this.creditClass = creditClass;
    }

    public String getBasicCreditValue() {
        return basicCreditValue;
    }

    public void setBasicCreditValue(String basicCreditValue) {
        this.basicCreditValue = basicCreditValue;
    }

    public String getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(String creditValue) {
        this.creditValue = creditValue;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public static Map<String, String> getResponseMap(String bodyPacket) {

        // 解析亚信返回的字段，格式{X_RESULTINFO=["Trade OK!"],
        // X_LAST_RESULTINFO=["Trade OK!"], TRADE_ID=["9116033064603408"],
        // X_RESULTCODE=["0"], X_RECORDNUM=["1"]}
        bodyPacket = bodyPacket.replaceAll("\\{", "");
        bodyPacket = bodyPacket.replaceAll("}", "");
        bodyPacket = bodyPacket.replaceAll("\"", "");
        bodyPacket = bodyPacket.replaceAll(", ", ",");

        String[] parties = bodyPacket.split(",", -1);

        List<String> list = new LinkedList<String>();

        int former = -1;
        for (int i = 0; i < parties.length; i++) {
            if (parties[i].contains("]")) {
                String line = "";
                for (int j = former + 1; j < i; j++) {
                    line = line + parties[j] + ",";
                }
                line = line + parties[i];
                list.add(line);
                former = i;
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        for (String partie : list) {
            partie = partie.replaceAll("\\[", "");
            partie = partie.replaceAll("]", "");
            partie = partie.replaceAll("\"", "");

            String[] ss = partie.split("=");

            if (ss.length == 1) {// =后面没有字段,比如 RP_LINKMAN_JOBTITLE=
                map.put(ss[0], "");
            } else {// RSRV_STR22=1
                map.put(ss[0], ss[1]);
            }
        }

        return map;
    }

    /**
     * 分析boss返回的报文
     */
    public static GroupInfoResp analyseRespBodyPacket(String bodyPacket) {
        Map<String, String> paramMap = GroupInfoResp.getResponseMap(bodyPacket);
        GroupInfoResp resp = new GroupInfoResp();

        resp.setResultCode(paramMap.get("X_RESULTCODE"));

        resp.setResultInfo(paramMap.get("X_RESULTINFO"));

        if (resp.getResultCode().equals("0")) {// result_code为0时是代表成功
            resp.setCustType(paramMap.get("CUST_TYPE"));
            resp.setCustState(paramMap.get("CUST_STATE"));
            resp.setPsptTypeCode(paramMap.get("PSPT_TYPE_CODE"));
            resp.setPsptId(paramMap.get("PSPT_ID"));
            resp.setCustPasswd(paramMap.get("CUST_PASSWD"));
            resp.setOpenLimit(paramMap.get("OPEN_LIMIT"));
            resp.setDevelopStaffId(paramMap.get("DEVELOP_STAFF_ID"));
            resp.setDevelopDepartId(paramMap.get("DEVELOP_DEPART_ID"));
            resp.setInDate(paramMap.get("IN_DATE"));
            resp.setInStaffId(paramMap.get("IN_STAFF_ID"));
            resp.setInDepartId(paramMap.get("IN_DEPART_ID"));
            resp.setScoreValue(paramMap.get("SCORE_VALUE"));
            resp.setCreditClass(paramMap.get("CREDIT_CLASS"));
            resp.setBasicCreditValue(paramMap.get("BASIC_CREDIT_VALUE"));
            resp.setCreditValue(paramMap.get("CREDIT_VALUE"));
            resp.setCustId(paramMap.get("CUST_ID"));
            resp.setGroupId(paramMap.get("GROUP_ID"));
            resp.setCustName(paramMap.get("CUST_NAME"));
            resp.setGroupType(paramMap.get("GROUP_TYPE"));
        }
        return resp;
    }

}
