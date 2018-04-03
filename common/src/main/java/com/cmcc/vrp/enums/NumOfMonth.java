package com.cmcc.vrp.enums;

import org.springframework.util.NumberUtils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public enum NumOfMonth {

    ONE(1, "1个月"),
    TWO(2, "2个月"),
    THREE(3, "3个月"),
    FOUR(4, "4个月"),
    FIVE(5, "5个月"),
    SIX(6, "6个月"),
    SEVEN(7, "7个月"),
    EIGHT(8, "8个月"),
    NINE(9, "9个月"),
    TEN(10, "10个月"),
    ELEVEN(11, "11个月"),
    TWELVE(12, "12个月"),;

    private Integer code;

    private String message;

    NumOfMonth(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return
     */
    public static Map<String, String> toMap() {
        Map<String, String> map = new TreeMap<String, String>(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                Long first = NumberUtils.parseNumber(o1, Long.class);
                Long second = NumberUtils.parseNumber(o2, Long.class);
                return first.intValue() - second.intValue();
            }
        });

        for (NumOfMonth item : NumOfMonth.values()) {
            map.put(item.getCode().toString(), item.getMessage());
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