/**
 * @Title: ChargeLogPojo.java
 * @Package com.cmcc.vrp.province.boss.chongqing.web
 * @author: qihang
 * @date: 2015年9月17日 下午2:24:21
 * @version V1.0
 */
package com.cmcc.vrp.charge;

import com.cmcc.vrp.enums.ChargeType;

/**
 * @ClassName: ChargeLogPojo
 * @Description: TODO
 * @author: qihang
 * @date: 2015年9月17日 下午2:24:21
 */
public class ChargeLogPojo {

    private LogType logType = null; //日志类型，request和response
    private ChargeType chargeType;//充值类型
    private String serialNum;//该次充值的流水号
    private Long ruleId;
    private Long recordId;
    private Long entId;//企业Id
    private Long prdId;//产品Id
    private String telNum;//电话
    private String resultCode = null; //结果编码
    private String resultMsg = null;  //结果信息

    //包含了一次充值时的基本信息，包括了request和response的共有部分,初始化设logType为request
    public ChargeLogPojo(ChargeType chargeType, String serialNum, Long ruleId,
                         Long recordId, Long entId, Long prdId, String telNum) {

        this.chargeType = chargeType;
        this.serialNum = serialNum;
        this.ruleId = ruleId;
        this.recordId = recordId;
        this.entId = entId;
        this.prdId = prdId;
        this.telNum = telNum;
        logType = LogType.REQUEST;
    }

    //将pojo改成response类型
    public ChargeLogPojo setToResponse(ChargeResult chargeResult) {
        logType = LogType.RESPONSE;
        resultCode = chargeResult.getCode().getCode();
        resultMsg = chargeResult.getFailureReason();
        return this;
    }

    //得到打出的日志
    public String getLog() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("serialNum:" + serialNum + " ");
        buffer.append(logType.getType());
        buffer.append(" chargeType:" + chargeType);


        //在webservice接口内是不存在ruleId和recordId的
        if (ruleId != null) {
            buffer.append(" ruleId:" + ruleId);
        }
        if (recordId != null) {
            buffer.append(" recordId:" + recordId);
        }

        buffer.append(" entId:" + entId);
        buffer.append(" prdId:" + prdId);
        buffer.append(" telNum:" + telNum);

        //只有response时才有以下两个数据
        if (resultCode != null) {
            buffer.append(" result_code:" + resultCode);
        }
        if (resultMsg != null) {
            buffer.append(" result_msg:" + resultMsg);
        }

        return buffer.toString();
    }

    //充值日志的两种模式，request和response
    public enum LogType {
        REQUEST("request"),
        RESPONSE("response");

        private String type;

        private LogType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


}
