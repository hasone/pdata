package com.cmcc.vrp.boss.henan.model;

/**
 * Created by leelyn on 2016/6/24.
 */
public class HaConstats {

    public static final String PRODUCE_URL = "http://211.138.30.208:9410/oppf";

    public static final String TEST_URL = "http://211.138.30.208:20110/oppf";

    public static final String HENANAT_KET = "hananaccesstoken";

    public enum CallbackResp {
        success("00000", "流量平台回调成功");

        private String code;
        private String msg;

        CallbackResp(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public enum CallbackReq {
        success("00000", "调用成功");

        private String code;
        private String msg;

        CallbackReq(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public enum QueryOrderResp {
        success("00000", "调用成功");

        private String code;
        private String msg;

        QueryOrderResp(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public enum QueryStatusResp {
        success("00000", "调用成功");

        private String code;
        private String msg;

        QueryStatusResp(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public enum QueryStatus {
        INIT("W", "正在受理"),
        DEALING("M", "全网业务集团公司受理中"),
        SUCCESS("Y", "受理成功"),
        FAILD("N", "受理失败"),
        EXCEPTION("E", "反馈异常");

        private String code;
        private String msg;

        QueryStatus(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
