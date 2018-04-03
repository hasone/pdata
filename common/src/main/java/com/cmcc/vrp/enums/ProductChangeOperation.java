/**
 *
 */
package com.cmcc.vrp.enums;

/**
 * <p>Title:ProductChangeOperation </p>
 * <p>Description: </p>
 *
 * @author xujue
 * @date 2016年7月21日
 */
public enum ProductChangeOperation {

    DELETE(0, "删除企业产品"),
    ADD(1, "新增企业产品"),
    CHANGE_DISCOUNT(2, "变更企业产品折扣"),
    CHANGE_PRODUCT_TEMPLATE(3, "变更产品模板");

    private int value;
    private String desc;

    ProductChangeOperation(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
