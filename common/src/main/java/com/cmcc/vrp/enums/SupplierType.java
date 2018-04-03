package com.cmcc.vrp.enums;

/**
 * 运营商
 * <p>
 * Created by sunyiwei on 2016/8/24.
 */
public enum SupplierType {
    MOBILE("M", "中国移动"),
    UNICOM("U", "中国联通"),
    TELECOM("T", "中国电信");

    private String code;
    private String message;

    SupplierType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @param code
     * @return
     */
    public static SupplierType fromCode(String code) {
        for (SupplierType st : SupplierType.values()) {
            if (st.getCode().equalsIgnoreCase(code)) {
                return st;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
