package com.cmcc.vrp.enums;

/**
 * 个人账户记录的状态
 *
 * @author wujiamin
 * @date 2016年9月27日下午2:30:53
 */
public enum IndividualAccountRecordStatus {
    PROCESSING(0, "进行中"),
    SUCCESS(1, "成功"),
    FAIL(2, "失败");

    private Integer value;
    private String desc;

    IndividualAccountRecordStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
