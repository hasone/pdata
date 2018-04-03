package com.cmcc.vrp.enums;

/**
 * 卡记录入库时错误信息枚举
 *
 * @author luozuwu
 */
public enum StoreRspCode {

    /**
     * 文件级错误类型
     */
    F0001("F0001", "文件类别不正确，日增量文件的文件名是以ec开头的"),
    F0002("F0002", "接口文件名不正确，与接口规范定义不匹配"),
    F0003("F0003", "文件名中的创建日期不正确，主要指文件名中的yyyymmdd不是有效的日期格式"),
    F0005("F0005", "文件名中文件类型不正确，指文件不是以 .txt 结尾"),
    F0006("F0006", "头记录标识错误，填的不是‘10’"),
    F0010("F0010", "尾记录标识错误，填的不是‘90’"),
    F0011("F0011", "头记录中记录汇总条数不正确，不等于实际记录总条数"),

    /**
     * 记录级错误类型
     */
    F010("F010", "记录字段数错误，按分割符分隔后，不是2段"),
    F020("F020", "该卡号不是27位数字"),
    F030("F030", "该卡号不存在对应的卡记录"),
    F040("F040", "该卡号其他错误");

    private String code;
    private String desc;

    private StoreRspCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * @param code
     * @return
     */
    public static StoreRspCode fromCode(String code) {
        for (StoreRspCode rc : StoreRspCode.values()) {
            if (rc.getCode().equalsIgnoreCase(code)) {
                return rc;
            }
        }

        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
