package com.cmcc.vrp.enums;

/**
 * 供应商状态
 * Created by qinqinyan on 2017/3/3.
 */
public enum SupplierStatus {
	OFF(0, "下架"),
    ON(1, "上架");

    private int code;
    private String message;

    SupplierStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @param code
     * @return
     */
    public static SupplierStatus fromCode(int code) {
        for (SupplierStatus st : SupplierStatus.values()) {
            if (st.getCode()==code) {
                return st;
            }
        }

        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
