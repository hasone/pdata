package com.cmcc.webservice.constants;


/**
* <p>Title: </p>
* <p>Description: </p>
* @author lgk8023
* @date 2017年1月22日 下午5:05:10
*/
public class ChargeRecordStatus {

    public static final String mobile_regex = "^(13[456789]|15[012789]|18[23478]|147|178)[0-9]{8}";

    public enum ChargeMsg {
        success(100, "成功"),
        illegal_sign(10, "非法签名"),
        parameter_lost(11, "参数缺失"),
        invalid_product(12, "商品编码不存在"),
        invalid_num(13, "非法手机号码"),
        invalid_chargenum(14, "充值数量非法"),
        xmlparse_exception(15, "解析xml错误"),
        invalid_account(16, "充值主账号异常"),
        request_more_than_3min(17, "报文产生时间超过3分钟"),
        too_many_phoneNum(18, "一次充值的手机号码少于1"),
        invalid_enterprise(19, "企业编码不存在"),
        invalid_createTime(20, "创建时间错误"),
        system_exception(21, "系统异常"),
        order_exist(22, "该笔订单已存在，不能重复创建"),
        order_not_exist(23, "该笔订单未找到");

        private int code;
        private String msg;

        ChargeMsg(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }


    /**
    * <p>Title: </p>
    * <p>Description: </p>
    * @author lgk8023
    * @date 2017年1月22日 下午5:05:17
    */
    public interface FlowchargeType {
        public static final String FLOW_CHARGE_TYPE_QG = "QG";
        public static final String FLOW_CHARGE_TYPE_ZJ = "ZJ";
        public static final String FLOW_CHARGE_TYPE_HZ = "HZ";
    }
}
