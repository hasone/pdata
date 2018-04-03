package com.cmcc.vrp.province.service;

/**
 * 抢红包结果
 * <p>
 */
public class GrabResult {

    private Code code;
    private String size = "";
    public GrabResult(Code code) {
        this.code = code;
    }

    public GrabResult(Code code, String size) {
        this.code = code;
        this.size = size;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public static enum Code {
        OK(0L, "成功"), REDPACKET_NONEXIST(1L, "红包不存在"), REDPACKET_OUT_OF_DATE(
            2L, "红包已过期"), REDPACKET_STATUS_OFF(3L, "红包已下架"), REDPACKET_DELETED(
            4L, "红包已删除"), USER_MAX_COUNT(5L, "达到每用户最大红包数"), INVALID_ARGUMENTS(
            6L, "参数无效"), FAILED(100L, "抢红包失败");

        private long value;
        private String message;

        Code(long value, String message) {
            this.value = value;
            this.message = message;
        }

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
