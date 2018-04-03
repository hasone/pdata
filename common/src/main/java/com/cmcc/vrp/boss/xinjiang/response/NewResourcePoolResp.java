package com.cmcc.vrp.boss.xinjiang.response;

import java.util.Map;

/**
 * 得到新版返回结果
 *
 */
public class NewResourcePoolResp {
    private String resultCode;// 状态码,为0时才有流量池信息

    private String resultInfo;// 状态信息

    private String groupId;

    private String addValue;

    private String startDate;

    private String endDate;

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAddValue() {
        return addValue;
    }

    public void setAddValue(String addValue) {
        this.addValue = addValue;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 分析返回结果
     * @param bodyPacket
     * @return
     */
    public static NewResourcePoolResp analyseRespBodyPacket(String bodyPacket) {
        Map<String, String> paramMap = GroupInfoResp.getResponseMap(bodyPacket);
        NewResourcePoolResp resp = new NewResourcePoolResp();

        resp.setResultCode(paramMap.get("X_RESULTCODE"));

        resp.setResultInfo(paramMap.get("X_RESULTINFO"));

        if (!resp.getResultCode().equals("0")) {

        } else { //为0时
            resp.setEndDate(paramMap.get("END_DATE"));
            resp.setGroupId(paramMap.get("GROUP_ID"));
            resp.setStartDate(paramMap.get("START_DATE"));
            resp.setAddValue(paramMap.get("ALL_VALUE"));

        }
        return resp;
    }

    public boolean isExist() {
        if (getResultCode() != null && getResultCode().equals("0")) {
            return true;
        }
        return false;
    }
}
