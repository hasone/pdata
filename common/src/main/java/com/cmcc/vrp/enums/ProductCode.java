package com.cmcc.vrp.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName: ProductCode
 * @Description: TODO
 * @author: luozuwu
 * @date: 2015年8月19日 上午10:41:55
 */
public enum ProductCode {

    LEVEL_0(0, "10MB"),
    LEVEL_1(1, "30MB"),
    LEVEL_2(2, "70MB"),
    LEVEL_3(3, "150MB"),
    LEVEL_4(4, "500MB"),
    LEVEL_5(5, "1GB"),
    LEVEL_6(6, "2GB"),
    LEVEL_7(7, "3GB"),
    LEVEL_8(8, "4GB"),
    LEVEL_9(9, "6GB"),
    LEVEL_10(10, "11GB"),
    LEVEL_DEFAULT(99, "DEFAULT_VALUE");

    private Integer code;

    private String message;

    ProductCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, Integer> toMap() {
        Map<String, Integer> map = new LinkedHashMap<String, Integer>();
        for (ProductCode item : ProductCode.values()) {
            map.put(item.getMessage(), item.getCode());
        }
        return map;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
