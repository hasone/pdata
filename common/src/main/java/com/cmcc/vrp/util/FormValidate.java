package com.cmcc.vrp.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: FormValidate
 * @Description: 正则表达式的所有类型，参数和错误返回结果
 * @author: qihang
 * @date: 2015-3-5
 */
public enum FormValidate {
    MOBILE_PHONE("mobile", "^1[3-8][0-9]{9}$", "不是有效的手机号码!"),
    EMAIL_ADDRESS_REGEX("email", "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$", "不是有效的邮箱地址!"),
    CHINESE_LOWER_UPPER_UNDERLINE_NUMBER_REGEX("chinese", "^[\u4E00-\u9FA5A-Za-z0-9_&]+$", "只能由汉字、英文字符、下划线或数字组成!"),
    LOWER_UPPER_UNDERLINE_NUMBER_REGEX("english", "^[A-Za-z0-9_]+$", "只能字母、数字或下划线组成!"),
    NUMBER_REGEX("number", "^\\d+$", "只能由数字组成!"),
    NON_NEGATIVE_REGEX("positive_num", "/^[1-9]+\\d*$", "只能是正数！"),
    POSITIVE_REGEX("positive_num", "^[1-9]\\d*|0$", "只能是非负正数！");


    //验证类型
    private String validate_type;
    //验证的正则表达式
    private String validate_pattern;
    //验证错误时返回的结果
    private String result_message;

    FormValidate(String validate_type, String validate_pattern, String result_message) {
        this.validate_type = validate_type;
        this.validate_pattern = validate_pattern;
        this.result_message = result_message;
    }

    public static Map<String, String> toPatternMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (FormValidate item : FormValidate.values()) {
            map.put(item.getValidate_type(), item.getValidate_pattern());
        }
        return map;
    }

    public static Map<String, String> toMessageMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (FormValidate item : FormValidate.values()) {
            map.put(item.getValidate_type(), item.getResult_message());
        }
        return map;
    }

    public String getValidate_type() {
        return validate_type;
    }

    public void setValidate_type(String validate_type) {
        this.validate_type = validate_type;
    }

    public String getValidate_pattern() {
        return validate_pattern;
    }

    public void setValidate_pattern(String validate_pattern) {
        this.validate_pattern = validate_pattern;
    }

    public String getResult_message() {
        return result_message;
    }

    public void setResult_message(String result_message) {
        this.result_message = result_message;
    }


}
