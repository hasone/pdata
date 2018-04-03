
/**
 * @ClassName: RspCode.java
 * @Description: 营销卡验证时的错误枚举
 * @author: sunyiwei
 * @date 2015年7月16日 - 下午4:37:45
 * @version : 1.0
 */

package com.cmcc.vrp.enums;

/**
 * 营销卡验证时的错误枚举
 */
public enum ActivateLockRspCode {
    F010("F010", "按分割符分隔后，记录不是2段"),
    F020("F020", "不是27位数字"),
    F030("F030", "该卡尚未入库，不能激活"),
    F040("F040", "该卡已被锁定，不能激活"),
    F050("F050", "该卡已被绑定，不能激活"),
    F060("F060", "该卡已失效，不能激活"),
    F070("F070", "该卡号不存在对应的卡记录"),
    F080("F080", "其他错误");

    private String code;
    private String desc;

    private ActivateLockRspCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * @param code
     * @return
     */
    public static ActivateLockRspCode fromCode(String code) {
        for (ActivateLockRspCode rc : ActivateLockRspCode.values()) {
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
